package model.estrutura;

import model.Aluno;
import model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class AlunoQuantidade {

	private Aluno aluno;
	private List<Disciplina> listaDisciplina = new ArrayList<Disciplina>();	
	
	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	public List<Disciplina> getListaDisciplina() {
		return listaDisciplina;
	}
	public void setListaDisciplina(List<Disciplina> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}	
}
