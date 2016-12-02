package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;

public class TurmaAlunos {
	
	private int quantidadeTotal;
	private String IngressoAlunos;
	private List<AprovacoesQuantidade> listaAprovacoesQuantidade = new ArrayList<AprovacoesQuantidade>();	
	
	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}
	public void setQuantidadeTotal(int quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}
	public String getIngressoAlunos() {
		return IngressoAlunos;
	}
	public void setIngressoAlunos(String ingressoAlunos) {
		IngressoAlunos = ingressoAlunos;
	}
	public List<AprovacoesQuantidade> getListaAprovacoesQuantidade() {
		return listaAprovacoesQuantidade;
	}
	public void setListaAprovacoesQuantidade(
			List<AprovacoesQuantidade> listaAprovacoesQuantidade) {
		this.listaAprovacoesQuantidade = listaAprovacoesQuantidade;
	}
}