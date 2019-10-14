package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Pessoa;
import br.ufjf.coordenacao.sistemagestaocurso.model.PessoaCurso;
import br.ufjf.coordenacao.sistemagestaocurso.repository.CursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.PessoaCursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.PessoaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;


@Named
@ViewScoped
public class CadastroPessoaController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PessoaRepository pessoas ;
	@Inject
	private CursoRepository cursos ;
	@Inject
	private PessoaCursoRepository pessoaCursoRepository ;
	
	private Curso curso = new Curso();
	private Pessoa pessoa = new Pessoa();
	private Pessoa pessoaSelecionada = new Pessoa();
	private PessoaCurso pessoaCursoSelecionada = new PessoaCurso();
	private List<Pessoa> listaPessoa = new ArrayList<Pessoa>();	
	private List<Pessoa> listaPessoaFiltradas ;
	private List<PessoaCurso> listaPessoaCurso = new ArrayList<PessoaCurso>();
	private boolean lgCodigoCurso = false;
	private boolean lgNomeCurso = false;
	private boolean lgCodigoPessoaCurso = false;
	private boolean lgNomePessoaCurso = false;	
	//private EstruturaArvore estruturaArvore;

	//========================================================= METODOS ==================================================================================//

	@PostConstruct
	public void init() {
		//estruturaArvore = EstruturaArvore.getInstance();
		//listaPessoa = new ArrayList<Pessoa>();	
		listaPessoa = (List<Pessoa>) pessoas.listarTodos();
	}

	public void onRowEdit(RowEditEvent event) {
		pessoa = (Pessoa) event.getObject();
		if (pessoa.getNome().equals("") || pessoa.getNome() == null){
			FacesMessage msg = new FacesMessage("Nome usu�rio inv�lido!" );	
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (pessoa.getSiape().equals("") || pessoa.getSiape() == null){
			FacesMessage msg = new FacesMessage("SIAPE/Login usu�rio inv�lido!" );	
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		pessoa.setNome(pessoa.getNome().toUpperCase());
		FacesMessage msg = new FacesMessage("Usu�rio Editado!",pessoa.getNome() );	
		FacesContext.getCurrentInstance().addMessage(null, msg);
		pessoas.persistir(pessoa);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edi��o Cancelada!", ((Pessoa) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	@Transactional
	public void incluirPessoa(){
		if (pessoa.getSiape() == null || pessoa.getSiape().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Matr�cula!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (pessoa.getNome() == null || pessoa.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (curso.getNome() == null || curso.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome Curso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (curso.getCodigo() == null || curso.getCodigo().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo C�digo Curso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (pessoa.getSiape() != null && pessoa.getNome() != null && curso.getCodigo() != null && curso.getNome() != null  ){	
			pessoa.setSenha("13f0c7f89f69905f52f76d42a22c5678");
			Pessoa pessoaAuxiliar = new Pessoa();
			pessoaAuxiliar = pessoas.buscarPorSiapePessoa(pessoa.getSiape());
			if(pessoaAuxiliar == null){
				pessoaAuxiliar = pessoas.buscarPorNomePessoa(pessoa.getNome());
			}
			if(pessoaAuxiliar == null){
				pessoa = pessoas.persistir(pessoa);
				
				/*Pessoa teste = pessoas.porid(pessoa.getId());
				if(teste != null)
					System.out.println("NAO EH NULL");
				else
					System.out.println("EH NULL");
				
				manager.flush();*/
				PessoaCurso pessoaCurso = new PessoaCurso();
				pessoaCurso.setCurso(curso);
				pessoaCurso.setPessoa(pessoa);
				pessoaCursoRepository.persistir(pessoaCurso);
				init();
			}
			else {
				FacesMessage msg = new FacesMessage("Usu�rio ja cadastrado!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			pessoa = new Pessoa();
			curso = new Curso();
			lgCodigoCurso = false;
			lgNomeCurso = false;
		}
	}

	@Transactional
	public void incluiPessoaCurso(){
		PessoaCurso pessoaCurso = new PessoaCurso();
		pessoaCurso.setCurso(curso);
		pessoaCurso.setPessoa(pessoaSelecionada);
		if (curso == null ){
			FacesMessage msg = new FacesMessage("Curso Inv�lido!");
			FacesContext.getCurrentInstance().addMessage(null, msg);	
			return;
		}
		pessoaCursoRepository.persistir(pessoaCurso);
		carregaPessoaCurso();
		curso = new Curso();
		lgCodigoPessoaCurso = false;
		lgNomePessoaCurso = false;
	}
	public void limpaPessoa(){
		pessoa = new Pessoa();
		curso = new Curso();
		lgCodigoCurso = false;
		lgNomeCurso = false;
	}

	public void limpaPessoaCurso(){
		curso = new Curso();
		lgCodigoPessoaCurso = false;
		lgNomePessoaCurso = false;
	}

	@Transactional
	public void deletarPessoa(){

		pessoas.remover(pessoaSelecionada);
		listaPessoa.clear();
		List<Pessoa> listaPessoaAuxiliar = new ArrayList<Pessoa>();	
		listaPessoaAuxiliar = (List<Pessoa>) pessoas.listarTodos();
		listaPessoa.clear();
		for(Pessoa p:listaPessoaAuxiliar){
			listaPessoa.add(p);
		}		
		//estruturaArvore.resetStanciaPessoa();
	}

	public List<String> cursoCodigos(String codigo) {	
		codigo = codigo.toUpperCase();
		List<String> todos = cursos.buscarTodosCodigosCurso(codigo);
		return todos;	
	}
	
	public List<Curso> cursoNomes(String codigo) {	
		codigo = codigo.toUpperCase();
		List<Curso> todos = cursos.buscarTodosNomesObjetoCurso(codigo);
		return todos;	
	}


	public void onItemSelectCodigoCurso() {
		curso = cursos.buscarPorCodigo(curso.getCodigo());
		lgCodigoCurso = true;
		lgNomeCurso = true;
	}

	public void onItemSelectNomeCurso() {
		lgCodigoCurso = true;
		lgNomeCurso = true;
	}

	public void onItemSelectPessoaCurso() {
		curso = cursos.buscarPorCodigo(curso.getCodigo());
		lgCodigoPessoaCurso = true;
		lgNomePessoaCurso = true;
	}

	public void carregaPessoaCurso(){
		listaPessoaCurso = new ArrayList<PessoaCurso>();
		List<PessoaCurso> todos = pessoaCursoRepository.buscarTodasPessoaCursoPorPessoa(pessoaSelecionada.getId());
		while(!todos.isEmpty()){  
			listaPessoaCurso.add(todos.remove(0));
		}
	}

	@Transactional
	public void deletarPessoaCurso(){
		List<PessoaCurso> listaPessoaCursoAuxiliar = (List<PessoaCurso>) pessoaCursoRepository.buscarTodasPessoaCursoPorPessoa(pessoaSelecionada.getId());
		if(listaPessoaCursoAuxiliar.size() == 1){
			FacesMessage msg = new FacesMessage("Usuário deve ter pelo menos um curso associado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		pessoaCursoRepository.remover(pessoaCursoSelecionada);
		listaPessoaCursoAuxiliar.remove(pessoaCursoSelecionada);
		listaPessoaCurso.clear();
		for(PessoaCurso p:listaPessoaCursoAuxiliar){
			listaPessoaCurso.add(p);
		}
		//estruturaArvore.resetStanciaPessoa();
	}

	public void resetarSenha(){
		if (pessoaSelecionada != null){ 
			pessoaSelecionada.setSenha("13f0c7f89f69905f52f76d42a22c5678");
			pessoas.persistir(pessoaSelecionada);
			FacesMessage msg = new FacesMessage("Senha resetada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	//========================================================= GET - SET ==================================================================================//

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public List<Pessoa> getListaPessoaFiltradas() {
		return listaPessoaFiltradas;
	}

	public void setListaPessoaFiltradas(List<Pessoa> listaPessoaFiltradas) {
		this.listaPessoaFiltradas = listaPessoaFiltradas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public boolean isLgCodigoCurso() {
		return lgCodigoCurso;
	}

	public void setLgCodigoCurso(boolean lgCodigoCurso) {
		this.lgCodigoCurso = lgCodigoCurso;
	}

	public boolean isLgNomeCurso() {
		return lgNomeCurso;
	}

	public void setLgNomeCurso(boolean lgNomeCurso) {
		this.lgNomeCurso = lgNomeCurso;
	}

	public boolean isLgCodigoPessoaCurso() {
		return lgCodigoPessoaCurso;
	}

	public void setLgCodigoPessoaCurso(boolean lgCodigoPessoaCurso) {
		this.lgCodigoPessoaCurso = lgCodigoPessoaCurso;
	}

	public boolean isLgNomePessoaCurso() {
		return lgNomePessoaCurso;
	}

	public void setLgNomePessoaCurso(boolean lgNomePessoaCurso) {
		this.lgNomePessoaCurso = lgNomePessoaCurso;
	}

	public List<PessoaCurso> getListaPessoaCurso() {
		return listaPessoaCurso;
	}

	public void setListaPessoaCurso(List<PessoaCurso> listaPessoaCurso) {
		this.listaPessoaCurso = listaPessoaCurso;
	}

	public PessoaCurso getPessoaCursoSelecionada() {
		return pessoaCursoSelecionada;
	}

	public void setPessoaCursoSelecionada(PessoaCurso pessoaCursoSelecionada) {
		this.pessoaCursoSelecionada = pessoaCursoSelecionada;
	}
}

