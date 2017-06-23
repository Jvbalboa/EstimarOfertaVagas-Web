package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;

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
