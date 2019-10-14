package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Equivalencia;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.GradeDisciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.PreRequisito;
import br.ufjf.coordenacao.sistemagestaocurso.repository.CursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.EquivalenciaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeDisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.PreRequisitoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;

@Named
@ViewScoped
public class ImportarGradeController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private GradeRepository gradeRepository;
	
	@Inject
	private GradeDisciplinaRepository gradeDisciplinaRepository;
	
	@Inject
	private PreRequisitoRepository preRequisitoRepository;
	
	@Inject
	private EquivalenciaRepository equivalenciaRepository;
	
	@Inject
	private CursoRepository cursoRepository;
	
	private Grade grade;
	private Curso curso;
	private String codigoCurso;
	private List<String> codigosCursos;
	private List<String> codigosGrades;
	private String codigoGrade;
	private String codigoNovaGrade;
	private boolean lgCodigoCurso;
	private boolean lgCodigoGrade;
	private boolean lgCodigoNovaGrade;

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		this.grade = null;
		this.codigosCursos = new ArrayList<String>();
		this.lgCodigoCurso = false;
		this.lgCodigoGrade = false;
		this.lgCodigoNovaGrade = true;
	}
	
	public List<String> buscarCodigosCursos(String valor) {
		List<String> codigosEncontrados = new ArrayList<String>();
		valor = valor.toUpperCase();
		
		if (this.codigosCursos.isEmpty()) {
			this.codigosCursos = cursoRepository.buscarTodosCodigosCurso();
		}
		
		for (String codigo : this.codigosCursos) {
			if (codigo.contains(valor)) {
				codigosEncontrados.add(codigo);
			}
		}
		
		return codigosEncontrados;
	}
	
	public void onItemSelectCodigoCurso() {
		this.curso = cursoRepository.buscarPorCodigo(codigoCurso);
		this.codigosGrades = this.gradeRepository.buscarTodosCodigosGrade(this.curso.getId());
		//this.lgCodigoGrade = false;
	}
	
	public List<String> gradeCodigos(String codigo) {	
		codigo = codigo.toUpperCase();
		List<String> codigos = new ArrayList<String>();
		
		for (String codigoGrade : this.codigosGrades){
			if(codigoGrade.contains(codigo)){
				codigos.add(codigoGrade); 
			}
		}
		
		return codigos;
	}
	
	public void onItemSelectCodigoGrade() {
		this.lgCodigoCurso = true;
		this.lgCodigoGrade = true;
		this.lgCodigoNovaGrade = false;
	}

	public void buscarGrade() {
		Grade aux = gradeRepository.buscarPorCodigoGrade(this.codigoGrade, this.curso);

		if (aux == null) {
			FacesMessage msg = new FacesMessage("Grade não Encontrada");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		} else
		{
			this.grade = aux;
		}
	}

	public void abrirDialogo() {
		HashMap<String, Object> opcoes = new HashMap<String, Object>();
		opcoes.put("modal", true);
		opcoes.put("resizable", false);
		opcoes.put("contentWidth", 800);
		opcoes.put("contentHeight", 300);

		RequestContext.getCurrentInstance().openDialog("ImportarGrade", opcoes, null);
	}

	@Transactional
	public void importaGrade(ActionEvent actionEvent) {
			buscarGrade();

			if (this.grade != null) {

				for (Grade gradeSelecionada : curso.getGrupoGrades()) {
					if (gradeSelecionada.getCodigo().equals(this.codigoNovaGrade)) {
						addMessage("Grade já existente");
						return;
					}
				}
				
				Grade novaGrade = new Grade();
				novaGrade.setCodigo(this.codigoNovaGrade);
				novaGrade.setCurso(this.usuarioController.getAutenticacao().getCursoSelecionado());
				novaGrade.setHorasAce(grade.getHorasAce());
				novaGrade.setHorasEletivas(grade.getHorasEletivas());
				novaGrade.setHorasOpcionais(grade.getHorasOpcionais());
				novaGrade.setNumeroMaximoPeriodos(grade.getNumeroMaximoPeriodos());
				novaGrade.setPeriodoInicio(grade.getPeriodoInicio());

				novaGrade = gradeRepository.persistir(novaGrade);
				
				Hibernate.initialize(novaGrade.getGrupoGradeDisciplina());
				
				for (GradeDisciplina g : grade.getGrupoGradeDisciplina()) {
					GradeDisciplina gd = new GradeDisciplina();
					gd.setDisciplina(g.getDisciplina());
					gd.setGrade(novaGrade);
					gd.setTipoDisciplina(g.getTipoDisciplina());
					gd.setCarregou(g.getCarregou());
					gd.setExcluirIra(g.getExcluirIra());
					gd.setPeriodo(g.getPeriodo());

					gd = gradeDisciplinaRepository.persistir(gd);
					
					Hibernate.initialize(gd.getPreRequisito());
					for (PreRequisito p : g.getPreRequisito()) {
						PreRequisito pq = new PreRequisito();

						pq.setDisciplina(p.getDisciplina());
						pq.setGradeDisciplina(gd);
						pq.setTipo(p.getTipo());

						preRequisitoRepository.persistir(pq);
					}
				}
				
				Hibernate.initialize(grade.getGrupoEquivalencia());
				
				for (Equivalencia E : grade.getGrupoEquivalencia()) {
					Equivalencia e = new Equivalencia();
					e.setDisciplinaEquivalente(E.getDisciplinaEquivalente());
					e.setDisciplinaGrade(E.getDisciplinaGrade());
					e.setGrade(novaGrade);

					equivalenciaRepository.persistir(e);
				}

				usuarioController.setReseta(true);
				usuarioController.atualizarPessoaLogada();
				curso.getGrupoGrades().add(novaGrade);

				this.grade = null;
				
				this.codigoCurso = "";
				this.codigoGrade = "";
				this.codigoNovaGrade = "";
				
				addMessage("Grade clonada com sucesso!");
			}

	}

	private void addMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public String getCodigoCurso() {
		return this.codigoCurso;
	}

	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}


	public String getCodigoNovaGrade() {
		return codigoNovaGrade;
	}

	public void setCodigoNovaGrade(String codigoNovaGrade) {
		this.codigoNovaGrade = codigoNovaGrade;
	}

	public String getCodigoGrade() {
		return codigoGrade;
	}

	public void setCodigoGrade(String codigoGrade) {
		this.codigoGrade = codigoGrade;
	}
	
	public boolean isLgCodigoCurso() {
		return lgCodigoCurso;
	}

	public void setLgCodigoCurso(boolean lgCodigoCurso) {
		this.lgCodigoCurso = lgCodigoCurso;
	}

	public boolean isLgCodigoGrade() {
		return lgCodigoGrade;
	}

	public void setLgCodigoGrade(boolean lgCodigoGrade) {
		this.lgCodigoGrade = lgCodigoGrade;
	}

	public boolean isLgCodigoNovaGrade() {
		return lgCodigoNovaGrade;
	}

	public void setLgCodigoNovaGrade(boolean lgCodigoNovaGrade) {
		this.lgCodigoNovaGrade = lgCodigoNovaGrade;
	}
}
