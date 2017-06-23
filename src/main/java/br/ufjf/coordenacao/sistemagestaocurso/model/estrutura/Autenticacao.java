package br.ufjf.coordenacao.sistemagestaocurso.model.estrutura;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Pessoa;

public class Autenticacao {
	
	private String login;
	private String senha;
	private List<String> perfis = new ArrayList<String>();	
	private String token;
	private Pessoa pessoa;
	private String tipoAcesso;
	private String selecaoIdentificador;
	private String selecaoCurso;
	private Curso cursoSelecionado;
	private String semestreSelecionado;
	private List<String> perfisCursos = new ArrayList<String>();
	private String maiorPermissao;
	
	public Autenticacao(){}
	
	
	public Autenticacao(String login,String senha){
		
		this.senha =  senha;
		this.login = login;
		
		
	}
	
	public String getLogin() {
	
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public List<String> getPerfis() {
		return perfis;
	}
	public void setPerfis(List<String> perfis) {
		this.perfis = perfis;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public String getTipoAcesso() {
		return tipoAcesso;
	}
	public void setTipoAcesso(String tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}
	public String getSelecaoIdentificador() {
		return selecaoIdentificador;
	}
	public void setSelecaoIdentificador(String selecaoIdentificador) {
		this.selecaoIdentificador = selecaoIdentificador;
	}
	public String getSelecaoCurso() {
		return selecaoCurso;
	}
	public void setSelecaoCurso(String selecaoCurso) {
		this.selecaoCurso = selecaoCurso;
	}
	public Curso getCursoSelecionado() {
		return cursoSelecionado;
	}
	public void setCursoSelecionado(Curso cursoSelecionado) {
		this.cursoSelecionado = cursoSelecionado;
	}
	public String getSemestreSelecionado() {
		return semestreSelecionado;
	}
	public void setSemestreSelecionado(String semestreSelecionado) {
		this.semestreSelecionado = semestreSelecionado;
	}
	public List<String> getPerfisCursos() {
		return perfisCursos;
	}
	public void setPerfisCursos(List<String> perfisCursos) {
		this.perfisCursos = perfisCursos;
	}
	public String getMaiorPermissao() {
		return maiorPermissao;
	}
	public void setMaiorPermissao(String maiorPermissao) {
		this.maiorPermissao = maiorPermissao;
	}
	
}
	
	
