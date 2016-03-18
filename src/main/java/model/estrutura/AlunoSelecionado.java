package model.estrutura;

import model.Aluno;

public class AlunoSelecionado {	
	
	private String gradeIngresso;
	private Integer PeriodoCorrente;
	private String matricula;
	private String nomeAluno;
	private Aluno aluno;	
	
	public String getGradeIngresso() {
		return gradeIngresso;
	}
	public void setGradeIngresso(String gradeIngresso) {
		this.gradeIngresso = gradeIngresso;
	}
	public Integer getPeriodoCorrente() {
		return PeriodoCorrente;
	}
	public void setPeriodoCorrente(Integer periodoCorrente) {
		PeriodoCorrente = periodoCorrente;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNomeAluno() {
		return nomeAluno;
	}
	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
}
