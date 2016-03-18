package model.estrutura;

public class AlunoSituacao {
	
	private String matricula;
	private String nome;
	private int quantidadeReprovacoesNota;
	private int quantidadeReprovacoesFreq;
	private int quantidadeAprovados;
	private int quantidadeMatriculas;
	
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getQuantidadeReprovacoesNota() {
		return quantidadeReprovacoesNota;
	}
	public void setQuantidadeReprovacoesNota(int quantidadeReprovacoesNota) {
		this.quantidadeReprovacoesNota = quantidadeReprovacoesNota;
	}
	public int getQuantidadeReprovacoesFreq() {
		return quantidadeReprovacoesFreq;
	}
	public void setQuantidadeReprovacoesFreq(int quantidadeReprovacoesFreq) {
		this.quantidadeReprovacoesFreq = quantidadeReprovacoesFreq;
	}
	public int getQuantidadeMatriculas() {
		return quantidadeMatriculas;
	}
	public void setQuantidadeMatriculas(int quantidadeMatriculas) {
		this.quantidadeMatriculas = quantidadeMatriculas;
	}
	public int getQuantidadeAprovados() {
		return quantidadeAprovados;
	}
	public void setQuantidadeAprovados(int quantidadeAprovados) {
		this.quantidadeAprovados = quantidadeAprovados;
	}
	
	
}