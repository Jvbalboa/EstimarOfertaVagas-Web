package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;

public class GradeHistorico {
	
	
	private String nome;
	private String matricula;
	private ArrayList<String> historicoAluno;
	private String opcionaisSituacaoDisciplina;	

	public String getOpcionaisSituacaoDisciplina() {
		return opcionaisSituacaoDisciplina;
	}

	public void setOpcionaisSituacaoDisciplina(String opcionaisSituacaoDisciplina) {
		this.opcionaisSituacaoDisciplina = opcionaisSituacaoDisciplina;
	}

	public GradeHistorico(ArrayList<String> historicoAluno,String opcionaisSituacaoDisciplina,String nome,String matricula) {

		this.historicoAluno = historicoAluno;
		this.opcionaisSituacaoDisciplina = opcionaisSituacaoDisciplina;  
		this.nome = nome;
		this.matricula = matricula;
	}

	public ArrayList<String> getHistoricoAluno() {
		return historicoAluno;
	}

	public void setHistoricoAluno(ArrayList<String> historicoAluno) {
		this.historicoAluno = historicoAluno;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}	
}