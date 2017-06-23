package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;

public class AprovacoesQuantidade {
	
	private Integer quantidadeAprovacoes;
	private float percentualAprovados;
	private List<AlunoQuantidade> listaAlunoQuantidade = new ArrayList<AlunoQuantidade>();
	
	public Integer getQuantidadeAprovacoes() {
		return quantidadeAprovacoes;
	}
	public void setQuantidadeAprovacoes(Integer quantidadeAprovacoes) {
		this.quantidadeAprovacoes = quantidadeAprovacoes;
	}
	public float getPercentualAprovados() {
		return percentualAprovados;
	}
	public void setPercentualAprovados(float percentualAprovados) {
		this.percentualAprovados = percentualAprovados;
	}
	public List<AlunoQuantidade> getListaAlunoQuantidade() {
		return listaAlunoQuantidade;
	}
	public void setListaAlunoQuantidade(List<AlunoQuantidade> listaAlunoQuantidade) {
		this.listaAlunoQuantidade = listaAlunoQuantidade;
	}
}
