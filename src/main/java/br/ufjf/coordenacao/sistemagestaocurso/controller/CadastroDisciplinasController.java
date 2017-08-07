package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.Ordenar;
import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;



@Named
@ViewScoped
public class CadastroDisciplinasController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	
	@Inject
	private DisciplinaRepository disciplinas;
	
	private Ordenar ordenar = new Ordenar();	
	private Disciplina disciplina = new Disciplina();
	private List<Disciplina> listaDisciplinasFiltradas ;
	private List<Disciplina> listaDisciplinas = new ArrayList<Disciplina>();	
	//private EstruturaArvore estruturaArvore;	

	//========================================================= METODOS ==================================================================================//

	@PostConstruct
	public void init() {
		//estruturaArvore = EstruturaArvore.getInstance();
		listaDisciplinas = (List<Disciplina>) disciplinas.listarTodos();
		ordenar.DisciplinaOrdenarCodigo(listaDisciplinas);
	}

	@Transactional
	public void onRowEdit(RowEditEvent event) {
		Disciplina disciplina = (Disciplina) event.getObject();		
		if (disciplina.getNome().equals("") || disciplina.getNome() == null){
			FacesMessage msg = new FacesMessage("Nome disciplina inválido!" );	
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		disciplina.setCodigo(disciplina.getCodigo().toUpperCase());
		disciplina.setNome(disciplina.getNome().toUpperCase());
		
		try{
			disciplinas.persistir(disciplina);
			FacesMessage msg = new FacesMessage("Disciplina Editada!",((Disciplina) event.getObject()).getNome() );
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch(Exception e)
		{
			FacesMessage msg = new FacesMessage("Ocorreu um problema!",((Disciplina) event.getObject()).getNome() );
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edição Cancelada!", ((Disciplina) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	

	@Transactional
	public void incluirDisciplina(){
		if (disciplina.getCodigo() == null || disciplina.getCodigo().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getNome() == null || disciplina.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getCargaHoraria() == null || disciplina.getCargaHoraria() == 0){
			FacesMessage msg = new FacesMessage("Preecha o campo Carga Horária!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if (disciplinas.buscarPorCodigoDisciplina(disciplina.getCodigo().toUpperCase()) != null){
			FacesMessage msg = new FacesMessage("Disciplina já cadastrada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getCargaHoraria() != null  && disciplina.getCodigo() != null && disciplina.getNome() != null){	
			disciplina.setNome(disciplina.getNome().toUpperCase());
			disciplina.setCodigo(disciplina.getCodigo().toUpperCase());		
			disciplina = disciplinas.persistir(disciplina);
			listaDisciplinas.add(disciplina);
			ordenar.DisciplinaOrdenarCodigo(listaDisciplinas);
			disciplina = new Disciplina();
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:disciplinas");
			dataTable.clearInitialState();
			dataTable.reset();
		}
	}

	public void limpaDisciplina(){
		disciplina = new Disciplina();
	}

	//========================================================= GET - SET ==================================================================================//

	public List<Disciplina> getListaDisciplinas() {
		return listaDisciplinas;
	}

	public void setListaDisciplinas(List<Disciplina> listaDisciplinas) {
		this.listaDisciplinas = listaDisciplinas;
	}

	public List<Disciplina> getListaDisciplinasFiltradas() {
		return listaDisciplinasFiltradas;
	}

	public void setListaDisciplinasFiltradas(
			List<Disciplina> listaDisciplinasFiltradas) {
		this.listaDisciplinasFiltradas = listaDisciplinasFiltradas;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

