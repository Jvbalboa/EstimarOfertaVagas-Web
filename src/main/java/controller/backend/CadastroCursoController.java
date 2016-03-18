package controller.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import model.Curso;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import controller.util.EstruturaArvore;
import controller.util.Ordenar;
import dao.Interface.CursoDAO;


@Named
@ViewScoped
public class CadastroCursoController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private Curso curso = new Curso();
	private Ordenar ordenar = new Ordenar();	
	private CursoDAO cursoDAO ;	
	private List<Curso> listaCursoFiltradas ;
	private List<Curso> listaCurso = new ArrayList<Curso>();	
	private EstruturaArvore estruturaArvore;

	//========================================================= METODOS ==================================================================================//

	@PostConstruct
	public void init() {		
		estruturaArvore = EstruturaArvore.getInstance();
		cursoDAO = estruturaArvore.getCursoDAO();
		listaCurso = new ArrayList<Curso>();	
		listaCurso = (List<Curso>) cursoDAO.recuperarTodos();		
		ordenar.CursoOrdenarCodigo(listaCurso);		
	}

	public void onRowEdit(RowEditEvent event) {		
		Curso curso = (Curso) event.getObject();		
		if (curso.getNome().equals("") || curso.getNome() == null){
			FacesMessage msg = new FacesMessage("Nome disciplina inválido!" );	
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}		
		curso.setCodigo(curso.getCodigo().toUpperCase());
		curso.setNome(curso.getNome().toUpperCase());
		FacesMessage msg = new FacesMessage("Curso Editado!",((Curso) event.getObject()).getNome() );
		FacesContext.getCurrentInstance().addMessage(null, msg);
		cursoDAO.editar(curso);		
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edição Cancelada!", ((Curso) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void incluirCurso(){

		curso.setCodigo(curso.getCodigo().trim());
		curso.setNome(curso.getNome().trim());

		if (curso.getCodigo() == null || curso.getCodigo().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (curso.getNome() == null || curso.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		if (cursoDAO.buscarPorCodigo(curso.getCodigo().toUpperCase()) != null){
			FacesMessage msg = new FacesMessage("Curso já existe!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}

		if (curso.getCodigo() != null && curso.getNome() != null){
			curso.setNome(curso.getNome().toUpperCase());
			curso.setCodigo(curso.getCodigo().toUpperCase());	
			cursoDAO.persistir(curso);			
			init();			
			curso = new Curso();			
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:cursos");
			dataTable.clearInitialState();
			dataTable.reset();
		}

	}

	public void limpaCurso(){		
		curso = new Curso();
	}	

	//========================================================= GET - SET ==================================================================================//


	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}

	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Curso> getListaCurso() {
		return listaCurso;
	}

	public void setListaCurso(List<Curso> listaCurso) {
		this.listaCurso = listaCurso;
	}

	public List<Curso> getListaCursoFiltradas() {
		return listaCursoFiltradas;
	}

	public void setListaCursoFiltradas(List<Curso> listaCursoFiltradas) {
		this.listaCursoFiltradas = listaCursoFiltradas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

