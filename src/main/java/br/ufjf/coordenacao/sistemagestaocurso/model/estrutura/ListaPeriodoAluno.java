package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;

public class ListaPeriodoAluno {

	
	private String IngressoAlunos;
	private Integer maximoEncontrado;
	private ArrayList<PeriodoAluno> listaPeriodoAluno = new ArrayList<PeriodoAluno>();	
	
	public String getIngressoAlunos() {
		return IngressoAlunos;
	}
	public void setIngressoAlunos(String ingressoAlunos) {
		IngressoAlunos = ingressoAlunos;
	}
	public ArrayList<PeriodoAluno> getListaPeriodoAluno() {
		return listaPeriodoAluno;
	}
	public void setListaPeriodoAluno(ArrayList<PeriodoAluno> listaPeriodoAluno) {
		this.listaPeriodoAluno = listaPeriodoAluno;
	}
	
	public Integer getMaximoEncontrado() {
		return maximoEncontrado;
	}
	public void setMaximoEncontrado(Integer maximoEncontrado) {
		this.maximoEncontrado = maximoEncontrado;
	}	
}
