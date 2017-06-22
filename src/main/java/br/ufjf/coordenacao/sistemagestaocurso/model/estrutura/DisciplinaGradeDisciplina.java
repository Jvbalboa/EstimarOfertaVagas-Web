package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.GradeDisciplina;

public class DisciplinaGradeDisciplina {
	
	private GradeDisciplina gradeDisciplina;
	private Disciplina disciplina;	
	
	public GradeDisciplina getGradeDisciplina() {
		return gradeDisciplina;
	}
	public void setGradeDisciplina(GradeDisciplina gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}
	public Disciplina getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
}