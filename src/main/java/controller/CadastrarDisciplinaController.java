package controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import model.GradeDisciplina;
import dao.CadastrarDisciplinaDAO;


@ManagedBean
@ViewScoped
public class CadastrarDisciplinaController implements Serializable {

	
	
//========================================================= VARIABLES ==================================================================================//

	public GradeDisciplina gradeDisciplina = new GradeDisciplina();
	public CadastrarDisciplinaDAO cadastrarDisciplinaDAO = new CadastrarDisciplinaDAO();
	
	
	public void salvar(){
		
		cadastrarDisciplinaDAO.Inserir(gradeDisciplina);
			
		}
	
	
	
	
	public GradeDisciplina getGradeDisciplina() {
		return gradeDisciplina;
	}


	public void setGradeDisciplina(GradeDisciplina gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}


	public CadastrarDisciplinaDAO getCadastrarDisciplinaDAO() {
		return cadastrarDisciplinaDAO;
	}


	public void setCadastrarDisciplinaDAO(
			CadastrarDisciplinaDAO cadastrarDisciplinaDAO) {
		this.cadastrarDisciplinaDAO = cadastrarDisciplinaDAO;
	}



	
	



}
