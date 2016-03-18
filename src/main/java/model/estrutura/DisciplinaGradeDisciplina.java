package model.estrutura;

import model.Disciplina;
import model.GradeDisciplina;

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