package br.ufjf.coordenacao.sistemagestaocurso.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.Historico;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;

@Named(value="exportarHistorico")
public class ExportarHistorico  {
	/**
	 * 
	 */
	
	
	@Inject
	UsuarioController usuarioController;
	
	@Inject
	AlunoRepository alunoRepository;

	
	/**
	 * 
	 */
	private List<Historico> historicoCurso;
	private Curso curso = new Curso();
	private List<Aluno> alunos = new ArrayList<Aluno>();
	private String nomeArquivo;
		
	@PostConstruct
	public void init()
	{
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		nomeArquivo = "historico_curso_" + curso.getCodigo() + "_" + usuarioController.getAutenticacao().getSemestreSelecionado();
		
		historicoCurso = new ArrayList<Historico>();
		alunos = alunoRepository.buscarTodosAlunoCursoObjeto(curso.getId());
		for(Aluno aluno : alunos) {
			List<Historico> historicos = aluno.getGrupoHistorico();
			for(Historico h : historicos) {				
				historicoCurso.add(h);
			}
		}
	}
	
	//********************************************************
	
	public String getNomeArquivo() {
		return this.nomeArquivo;
	}
	
	public List<Historico> getHistoricoCurso() {
		this.init();
		return historicoCurso;
	}


	public void setHistoricoCurso(List<Historico> historicoCurso) {
		this.historicoCurso = historicoCurso;
	}
	
}
