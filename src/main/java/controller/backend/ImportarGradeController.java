package controller.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import controller.util.EstruturaArvore;
import controller.util.UsuarioController;
import dao.EquivalenciaDAOImpl;
import dao.PreRequisitoDAOImpl;
import dao.Interface.CursoDAO;
import dao.Interface.EquivalenciaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.PreRequisitoDAO;
import model.Curso;
import model.Disciplina;
import model.Equivalencia;
import model.Grade;
import model.GradeDisciplina;
import model.PreRequisito;

@Named
@ViewScoped
public class ImportarGradeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GradeDAO gradeDAO;
	private GradeDisciplinaDAO gradeDisciplinaDAO;
	private CursoDAO cursoDAO;
	private Grade grade;
	private Curso curso;
	private EstruturaArvore estruturaArvore;
	private PreRequisitoDAO preRequisitoDAO;
	private EquivalenciaDAO equivalenciaDAO;
	
	private String codImportar;
	private String novaGrade;
	private boolean achouGrade;
	
	@Inject
	private UsuarioController usuarioController;
	
	@PostConstruct
	public void init()
	{
		this.estruturaArvore = EstruturaArvore.getInstance();
		this.gradeDAO = this.estruturaArvore.getGradeDAO();
		this.gradeDisciplinaDAO = this.estruturaArvore.getGradeDisciplinaDAO();
		this.cursoDAO = this.estruturaArvore.getCursoDAO();
		this.preRequisitoDAO = new PreRequisitoDAOImpl();
		this.equivalenciaDAO =  new EquivalenciaDAOImpl();		
		
		this.grade = new Grade();
		
		//TODO alterar para curso selecionado com usuarioController.getAutenticacao().getCursoSelecionado().getCodigo() 
		this.curso = cursoDAO.buscarPorCodigo("35A");//usuarioController.getAutenticacao().getCursoSelecionado().getCodigo());
	}
	
	public void duplicaGrade()
	{
		
	}
	
	public void buscarGrade(){

		achouGrade = false;
		
		for(Grade gradeSelecionada : curso.getGrupoGrades()){
			//addMessage(gradeSelecionada.getCodigo());
			if (gradeSelecionada.getCodigo().equals(this.codImportar)){
				this.grade = gradeSelecionada;
				achouGrade = true;
				break;
			}
		}
		
		if(!achouGrade){
				FacesMessage msg = new FacesMessage("Grade n‹o Encontrada");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
	}
	
	public static void main(String[] args)
	{
		ImportarGradeController igc = new ImportarGradeController();
		
		igc.setNovaGrade("12020");
		igc.setCodImportar("12014");
		
		igc.init();
		igc.importaGrade(null);
	}
	
	public void abrirDialogo()
	{
		HashMap<String, Object> opcoes = new HashMap<String, Object>();
		opcoes.put("modal", true);
		opcoes.put("resizable", false);
		opcoes.put("contentHeight", 300);
		
		RequestContext.getCurrentInstance().openDialog("ImportarGrade", opcoes, null);
	}
	
	public void importaGrade(ActionEvent actionEvent)
	{
		//addMessage(this.codImportar + " ---> " + this.novaGrade);
		
		buscarGrade();
		
		if(this.achouGrade)
		{

			for(Grade gradeSelecionada : curso.getGrupoGrades()){
				if (gradeSelecionada.getCodigo().equals(this.novaGrade)){
					addMessage("Grade j‡ existente");
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
				
				//System.out.println(grade.getCodigo() + grade.getCurso().getId());
				gradeDAO.persistir(novaGrade);
				
				
				
				for(GradeDisciplina g: grade.getGrupoGradeDisciplina())
				{
					GradeDisciplina gd = new GradeDisciplina();
					gd.setDisciplina(g.getDisciplina());
					gd.setGrade(novaGrade);
					gd.setTipoDisciplina(g.getTipoDisciplina());
					gd.setCarregou(g.getCarregou());
					gd.setExcluirIra(g.getExcluirIra());
					gd.setPeriodo(g.getPeriodo());
					
					gradeDisciplinaDAO.persistir(gd);
					
					for(PreRequisito p: g.getPreRequisito())
					{
						PreRequisito pq = new PreRequisito();
						
						pq.setDisciplina(p.getDisciplina());
						pq.setGradeDisciplina(gd);
						pq.setTipo(p.getTipo());
						
						preRequisitoDAO.persistir(pq);
					}
				}
				
				for(Equivalencia E: grade.getGrupoEquivalencia())
				{
					Equivalencia e = new Equivalencia();
					e.setDisciplinaEquivalente(E.getDisciplinaEquivalente());
					e.setDisciplinaGrade(E.getDisciplinaGrade());
					e.setGrade(novaGrade);
					
					equivalenciaDAO.persistir(e);
				}
				
				usuarioController.setReseta(true);
				usuarioController.atualizarPessoaLogada();
				curso.getGrupoGrades().add(novaGrade);
				
				FacesMessage msg = new FacesMessage("Grade importada com sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			
		}
		
		
	}
	
	private void addMessage(String message)
	{
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public boolean isAchouGrade() {
		return achouGrade;
	}

	public void setAchouGrade(boolean achouGrade) {
		this.achouGrade = achouGrade;
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
