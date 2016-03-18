package model.estrutura;

import java.util.ArrayList;

import model.Aluno;

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
