package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;

public class PeriodoAluno {	
	
	private Integer periodoReal;
	private ArrayList<Aluno> listaAlunosPeriodo = new ArrayList<Aluno>();

	public ArrayList<Aluno> getListaAlunosPeriodo() {
		return listaAlunosPeriodo;
	}

	public void setListaAlunosPeriodo(ArrayList<Aluno> listaAlunosPeriodo) {
		this.listaAlunosPeriodo = listaAlunosPeriodo;
	}

	public Integer getPeriodoReal() {
		return periodoReal;
	}

	public void setPeriodoReal(Integer periodoReal) {
		this.periodoReal = periodoReal;
	}
}
