package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;



public class TotalizadorCurso {
	
	private String gradeIngresso;
	private List<Integer> listaTotalizados = new ArrayList<Integer>();	

	public List<Integer> getListaTotalizados() {
		return listaTotalizados;
	}

	public void setListaTotalizados(List<Integer> listaTotalizados) {
		this.listaTotalizados = listaTotalizados;
	}

	public String getGradeIngresso() {
		return gradeIngresso;
	}

	public void setGradeIngresso(String gradeIngresso) {
		this.gradeIngresso = gradeIngresso;
	}
}
