package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.Ordenar;
import br.ufjf.coordenacao.sistemagestaocurso.repository.CursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

@Named
@ViewScoped
public class CadastroCursoController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private Curso curso = new Curso();
	private Ordenar ordenar = new Ordenar();
	
	@Inject
	private CursoRepository cursos ;	
	private List<Curso> listaCursoFiltradas ;
	private List<Curso> listaCurso = new ArrayList<Curso>();

	//========================================================= METODOS ==================================================================================//

	@PostConstruct
	public void init() {		
		//estruturaArvore = EstruturaArvore.getInstance();

		listaCurso = new ArrayList<Curso>();	
		listaCurso = (List<Curso>) cursos.listarTodos();		
		ordenar.CursoOrdenarCodigo(listaCurso);		
	}

	@Transactional
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
		cursos.persistir(curso);		
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edição Cancelada!", ((Curso) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	@Transactional
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
		if (cursos.buscarPorCodigo(curso.getCodigo().toUpperCase()) != null){
			FacesMessage msg = new FacesMessage("Curso já existe!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}

		if (curso.getCodigo() != null && curso.getNome() != null){
			curso.setNome(curso.getNome().toUpperCase());
			curso.setCodigo(curso.getCodigo().toUpperCase());	
			cursos.persistir(curso);			
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
	
	@Transactional
	public void deletarCurso(){
		cursos.remover(curso);
		List<Curso> listaCursoAuxiliar = new ArrayList<Curso>();	
		listaCursoAuxiliar = (List<Curso>) cursos.listarTodos();
		listaCurso.clear();
		for(Curso curso:listaCursoAuxiliar){
			listaCurso.add(curso);
		}		
	}

	//========================================================= GET - SET ==================================================================================//

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

