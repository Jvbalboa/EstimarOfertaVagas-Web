package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.HashMap;

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
import br.ufjf.coordenacao.sistemagestaocurso.repository.EquivalenciaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeDisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.PreRequisitoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;

@Named
@ViewScoped
public class ImportarGradeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private GradeRepository grades;

	@Inject
	private GradeDisciplinaRepository gradeDisciplinas;
	
	@Inject
	private PreRequisitoRepository preRequisitos;
	
	@Inject
	private EquivalenciaRepository equivalencias;
	
	private Grade grade;
	private Curso curso;
	// private EstruturaArvore estruturaArvore;
	private String codImportar;
	private String novaGrade;
	//private boolean achouGrade;

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		// this.estruturaArvore = EstruturaArvore.getInstance();

		this.grade = null;

		this.curso = usuarioController.getAutenticacao().getCursoSelecionado();
	}

	public void buscarGrade() {
		
		Grade aux = grades.buscarPorCodigoGrade(this.codImportar, this.curso);

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
		opcoes.put("contentHeight", 300);

		RequestContext.getCurrentInstance().openDialog("ImportarGrade", opcoes, null);
	}

	@Transactional
	public void importaGrade(ActionEvent actionEvent) {
		// addMessage(this.codImportar + " ---> " + this.novaGrade);
			buscarGrade();

			if (this.grade != null) {

				for (Grade gradeSelecionada : curso.getGrupoGrades()) {
					if (gradeSelecionada.getCodigo().equals(this.novaGrade)) {
						addMessage("Grade já existente");
						return;
					}
				}
				Grade novaGrade = new Grade();
				novaGrade.setCodigo(this.novaGrade);
				novaGrade.setCurso(this.grade.getCurso());
				novaGrade.setHorasAce(grade.getHorasAce());
				novaGrade.setHorasEletivas(grade.getHorasEletivas());
				novaGrade.setHorasOpcionais(grade.getHorasOpcionais());
				novaGrade.setNumeroMaximoPeriodos(grade.getNumeroMaximoPeriodos());
				novaGrade.setPeriodoInicio(grade.getPeriodoInicio());

				// System.out.println(grade.getCodigo() +
				// grade.getCurso().getId());
				novaGrade = grades.persistir(novaGrade);
				
				Hibernate.initialize(novaGrade.getGrupoGradeDisciplina());
				
				for (GradeDisciplina g : grade.getGrupoGradeDisciplina()) {
					GradeDisciplina gd = new GradeDisciplina();
					gd.setDisciplina(g.getDisciplina());
					gd.setGrade(novaGrade);
					gd.setTipoDisciplina(g.getTipoDisciplina());
					gd.setCarregou(g.getCarregou());
					gd.setExcluirIra(g.getExcluirIra());
					gd.setPeriodo(g.getPeriodo());

					gd = gradeDisciplinas.persistir(gd);
					
					Hibernate.initialize(gd.getPreRequisito());
					for (PreRequisito p : g.getPreRequisito()) {
						PreRequisito pq = new PreRequisito();

						pq.setDisciplina(p.getDisciplina());
						pq.setGradeDisciplina(gd);
						pq.setTipo(p.getTipo());

						preRequisitos.persistir(pq);
					}
				}
				
				Hibernate.initialize(grade.getGrupoEquivalencia());
				
				for (Equivalencia E : grade.getGrupoEquivalencia()) {
					Equivalencia e = new Equivalencia();
					e.setDisciplinaEquivalente(E.getDisciplinaEquivalente());
					e.setDisciplinaGrade(E.getDisciplinaGrade());
					e.setGrade(novaGrade);

					equivalencias.persistir(e);
				}

				usuarioController.setReseta(true);
				usuarioController.atualizarPessoaLogada();
				curso.getGrupoGrades().add(novaGrade);

				this.grade = null;

				this.codImportar = "";
				this.novaGrade = "";

				/*
				 * usuarioController.setReseta(true);
				 * usuarioController.atualizarPessoaLogada();
				 */

				addMessage("Grade clonada com sucesso!");
			}

	}

	private void addMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String getNovaGrade() {
		return novaGrade;
	}

	public void setNovaGrade(String novaGrade) {
		this.novaGrade = novaGrade;
	}

	public String getCodImportar() {
		return codImportar;
	}

	public void setCodImportar(String codImportar) {
		this.codImportar = codImportar;
	}
}
