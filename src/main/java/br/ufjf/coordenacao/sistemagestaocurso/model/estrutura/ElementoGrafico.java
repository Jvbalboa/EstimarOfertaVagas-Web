package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;

public class ElementoGrafico {
	
	private Integer numero;
	private Float ira;
	private List<Aluno> listaAluno = new ArrayList<Aluno>();	
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public List<Aluno> getListaAluno() {
		return listaAluno;
	}
	public void setListaAluno(List<Aluno> listaAluno) {
		this.listaAluno = listaAluno;
	}
	public Float getIra() {
		return ira;
	}
	public void setIra(Float ira) {
		this.ira = ira;
	}
	
	
	
	
	
}