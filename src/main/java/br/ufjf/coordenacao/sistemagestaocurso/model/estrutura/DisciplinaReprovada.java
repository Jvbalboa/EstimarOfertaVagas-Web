package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.Historico;

public class DisciplinaReprovada {

	private List<Historico> listaReprovadosFreq = new ArrayList<Historico>();
	private List<Historico> listaAprov = new ArrayList<Historico>();
	private List<Historico> listaReprovadosNota = new ArrayList<Historico>();
	private List<Historico> listaMatriculados = new ArrayList<Historico>();
	private Disciplina disciplinaSelecionada;
	private List<AlunoSituacao> listaAlunoSituacao = new ArrayList<AlunoSituacao>();
	private int quantidadeAlunos;
	private int quantidadeAlunosAprovados;
	private int quantidadeAlunosReprovadosFreq;
	private int quantidadeAlunosReprovadosNota;
	private int quantidadeTentativas;
	private int quantidadeReprovacoesFreq;
	private int quantidadeAprovados;
	private int quantidadeReprovacoesNota;
	private boolean processado = false;
	private List<Historico> listaTotal = new ArrayList<Historico>();

	/*****************************************************************************************************************************************/
		
	public AlunoSituacao buscaAlunoSituacao(Aluno aluno){
		for (AlunoSituacao alunoSituacaoQuestao : listaAlunoSituacao){
			if (alunoSituacaoQuestao.getMatricula().equals(aluno.getMatricula() )){
				return alunoSituacaoQuestao;
			}
		}
		AlunoSituacao alunoSituacao = new AlunoSituacao();
		alunoSituacao.setMatricula(aluno.getMatricula());
		alunoSituacao.setNome(aluno.getNome());
		alunoSituacao.setQuantidadeMatriculas(0);
		alunoSituacao.setQuantidadeReprovacoesFreq(0);
		alunoSituacao.setQuantidadeReprovacoesNota(0);
		listaAlunoSituacao.add(alunoSituacao);
		return alunoSituacao;

	}

	private void calculaQuantidades(){
		if (processado == false){
			quantidadeTentativas = listaTotal.size();
			quantidadeReprovacoesFreq = listaReprovadosFreq.size();
			quantidadeReprovacoesNota = listaReprovadosNota.size();	
			quantidadeAprovados = listaAprov.size();	
			List<String> listaQuantidade = new ArrayList<String>();			
			AlunoSituacao alunoSituacaoQuestao;
			
			
			
			for(Historico historicoQuestao : listaReprovadosFreq){				
				alunoSituacaoQuestao = buscaAlunoSituacao(historicoQuestao.getAluno());
				alunoSituacaoQuestao.setQuantidadeReprovacoesFreq(alunoSituacaoQuestao.getQuantidadeReprovacoesFreq() + 1);
				alunoSituacaoQuestao.setQuantidadeMatriculas(alunoSituacaoQuestao.getQuantidadeMatriculas() + 1);
				if(!listaQuantidade.contains(historicoQuestao.getAluno().getMatricula())){
					listaQuantidade.add(historicoQuestao.getAluno().getMatricula());
				}
			}			
			quantidadeAlunosReprovadosFreq = listaQuantidade.size();
			listaQuantidade = new ArrayList<String>();
			
			for(Historico historicoQuestao : listaReprovadosNota){				
				alunoSituacaoQuestao = buscaAlunoSituacao(historicoQuestao.getAluno());
				alunoSituacaoQuestao.setQuantidadeReprovacoesNota(alunoSituacaoQuestao.getQuantidadeReprovacoesNota() + 1);
				alunoSituacaoQuestao.setQuantidadeMatriculas(alunoSituacaoQuestao.getQuantidadeMatriculas() + 1);
				if(!listaQuantidade.contains(historicoQuestao.getAluno().getMatricula())){
					listaQuantidade.add(historicoQuestao.getAluno().getMatricula());
				}
			}
			quantidadeAlunosReprovadosNota = listaQuantidade.size();			
			listaQuantidade = new ArrayList<String>();
			
			for(Historico historicoQuestao : listaAprov){				
				alunoSituacaoQuestao = buscaAlunoSituacao(historicoQuestao.getAluno());
				alunoSituacaoQuestao.setQuantidadeAprovados(alunoSituacaoQuestao.getQuantidadeAprovados() + 1);
				alunoSituacaoQuestao.setQuantidadeMatriculas(alunoSituacaoQuestao.getQuantidadeMatriculas() + 1);
				if(!listaQuantidade.contains(historicoQuestao.getAluno().getMatricula())){
					listaQuantidade.add(historicoQuestao.getAluno().getMatricula());
				}
			}
			quantidadeAlunosAprovados = listaQuantidade.size();			
			listaQuantidade = new ArrayList<String>();
			
			
			
			
			
			for(Historico historicoQuestao : listaMatriculados){				
				alunoSituacaoQuestao = buscaAlunoSituacao(historicoQuestao.getAluno());
				alunoSituacaoQuestao.setQuantidadeMatriculas(alunoSituacaoQuestao.getQuantidadeMatriculas() + 1);
				if(!listaQuantidade.contains(historicoQuestao.getAluno().getMatricula())){
					listaQuantidade.add(historicoQuestao.getAluno().getMatricula());
				}
			}
			quantidadeAlunos = listaQuantidade.size() + quantidadeAlunosReprovadosNota + quantidadeAlunosReprovadosFreq + quantidadeAlunosAprovados;
			processado = true;
		}	
	}
	
	
	
	
	
	public int getQuantidadeAlunosAprovados() {
		calculaQuantidades();
		return quantidadeAlunosAprovados;
	}

	

	public int getQuantidadeAprovados() {
		calculaQuantidades();
		return quantidadeAprovados;
	}

	

	public int getQuantidadeAlunos() {
		calculaQuantidades();
		return quantidadeAlunos;
	}

	public int getQuantidadeAlunosReprovadosFreq() {
		calculaQuantidades();
		return quantidadeAlunosReprovadosFreq;
	}

	public int getQuantidadeAlunosReprovadosNota() {
		calculaQuantidades();
		return quantidadeAlunosReprovadosNota;
	}

	public int getQuantidadeTentativas() {
		calculaQuantidades();
		return quantidadeTentativas;
	}

	public int getQuantidadeReprovacoesFreq() {
		calculaQuantidades();
		return quantidadeReprovacoesFreq;
	}

	public int getQuantidadeReprovacoesNota() {
		calculaQuantidades();
		return quantidadeReprovacoesNota;
	}

	public List<Historico> getListaTotal() {
		return listaTotal;
	}

	public void setListaTotal(List<Historico> listaTotal) {
		this.listaTotal = listaTotal;
	}

	public List<AlunoSituacao> getListaAlunoSituacao() {
		return listaAlunoSituacao;
	}

	public void setListaAlunoSituacao(List<AlunoSituacao> listaAlunoSituacao) {
		this.listaAlunoSituacao = listaAlunoSituacao;
	}
	
	
	public List<Historico> getListaReprovadosFreq() {
		return listaReprovadosFreq;
	}

	public void setListaReprovadosFreq(List<Historico> listaReprovadosFreq) {
		this.listaReprovadosFreq = listaReprovadosFreq;
	}

	public List<Historico> getListaReprovadosNota() {
		return listaReprovadosNota;
	}

	public void setListaReprovadosNota(List<Historico> listaReprovadosNota) {
		this.listaReprovadosNota = listaReprovadosNota;
	}

	public List<Historico> getListaMatriculados() {
		return listaMatriculados;
	}

	public void setListaMatriculados(List<Historico> listaMatriculados) {
		this.listaMatriculados = listaMatriculados;
	}

	public Disciplina getDisciplinaSelecionada() {
		return disciplinaSelecionada;
	}

	public void setDisciplinaSelecionada(Disciplina disciplinaSelecionada) {
		this.disciplinaSelecionada = disciplinaSelecionada;
	}

	public List<Historico> getListaAprov() {
		return listaAprov;
	}

	public void setListaAprov(List<Historico> listaAprov) {
		this.listaAprov = listaAprov;
	}

	public boolean isProcessado() {
		return processado;
	}

	public void setProcessado(boolean processado) {
		this.processado = processado;
	}

	public void setQuantidadeAlunos(int quantidadeAlunos) {
		this.quantidadeAlunos = quantidadeAlunos;
	}

	public void setQuantidadeAlunosAprovados(int quantidadeAlunosAprovados) {
		this.quantidadeAlunosAprovados = quantidadeAlunosAprovados;
	}

	public void setQuantidadeAlunosReprovadosFreq(int quantidadeAlunosReprovadosFreq) {
		this.quantidadeAlunosReprovadosFreq = quantidadeAlunosReprovadosFreq;
	}

	public void setQuantidadeAlunosReprovadosNota(int quantidadeAlunosReprovadosNota) {
		this.quantidadeAlunosReprovadosNota = quantidadeAlunosReprovadosNota;
	}

	public void setQuantidadeTentativas(int quantidadeTentativas) {
		this.quantidadeTentativas = quantidadeTentativas;
	}

	public void setQuantidadeReprovacoesFreq(int quantidadeReprovacoesFreq) {
		this.quantidadeReprovacoesFreq = quantidadeReprovacoesFreq;
	}

	

	public void setQuantidadeReprovacoesNota(int quantidadeReprovacoesNota) {
		this.quantidadeReprovacoesNota = quantidadeReprovacoesNota;
	}

	
	
	
}