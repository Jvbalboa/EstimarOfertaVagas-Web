package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Historico;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;

@Named
@ViewScoped
public class HistoricoController implements Serializable {

	// ========================================================= VARIABLES
	// ==================================================================================//

	private static final long serialVersionUID = 1L;
	private boolean lgMatriculaAluno = false;
	private boolean lgNomeAluno = false;
	private boolean lgAluno = true;
	private Aluno aluno = new Aluno();
	private List<Historico> listaHistorico = new ArrayList<Historico>();
	private List<Historico> listaHistoricoFiltrada;
	private String classeEscolhida;
	private Float ira;
	private Integer periodo;
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private Curso curso = new Curso();

	private Logger logger = Logger.getLogger(HistoricoController.class);
	
	@Inject
	private AlunoRepository alunos;

	// ========================================================= METODOS
	// ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		try {
			estruturaArvore = EstruturaArvore.getInstance();
			// cursos = estruturaArvore.getCursoDAO();
			usuarioController.atualizarPessoaLogada();

			if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")) {
				
				lgMatriculaAluno = true;
				lgNomeAluno = true;
				lgAluno = false;
				
				aluno = alunos.buscarPorMatricula(usuarioController.getAutenticacao().getSelecaoIdentificador());
				
				if (aluno == null || aluno.getMatricula() == null) {
					FacesMessage msg = new FacesMessage("Matrícula não cadastrada na base!");
					FacesContext.getCurrentInstance().addMessage(null, msg);

					return;
				}
				curso = aluno.getCurso();
				onItemSelectMatriculaAluno();
			} else {
				curso = usuarioController.getAutenticacao().getCursoSelecionado();
				if (curso.getGrupoAlunos().size() == 0) {
					logger.warn("O curso " + curso.getCodigo() + " ainda não possui alunos cadastrados");
					FacesMessage msg = new FacesMessage("Nenhum aluno cadastrado no curso!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}

			}
		} catch (Exception e) {
			logger.error("Exceção ao carregar o histórico", e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um problema", e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public List<String> alunoMatricula(String codigo) {
		codigo = codigo.toUpperCase();
		List<String> todos = new ArrayList<String>();
		for (Aluno alunoQuestao : curso.getGrupoAlunos()) {
			if (alunoQuestao.getMatricula().contains(codigo)) {
				todos.add(alunoQuestao.getMatricula());
			}
		}
		return todos;
	}

	public List<Aluno> alunoNome(String codigo) {
		codigo = codigo.toUpperCase();
		List<Aluno> todos = new ArrayList<Aluno>();
		for (Aluno alunoQuestao : curso.getGrupoAlunos()) {
			
			//Desconsiderando acentuação
			String nome = alunoQuestao.getNome();
			String nomeNormalizado = Normalizer.normalize(nome, Normalizer.Form.NFD);
			String nomeAscii = nomeNormalizado.replaceAll("[^\\p{ASCII}]", "");
			String codigoNormalizado = Normalizer.normalize(codigo, Normalizer.Form.NFD);
			String codigoAscii = codigoNormalizado.replaceAll("[^\\p{ASCII}]", "");
			
			if (nomeAscii.contains(codigoAscii)) {
				todos.add(alunoQuestao);
			}
		}
		return todos;
	}

	public void onItemSelectMatriculaAluno() {
		lgMatriculaAluno = true;
		lgNomeAluno = true;
		
		try {
			logger.info("Buscando dados para o aluno " + aluno.getMatricula());
			aluno = alunos.buscarPorMatricula(aluno.getMatricula());
		} catch (Exception e) {
			logger.error("Erro ao buscar o aluno", e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível carregar os dados", e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if(aluno == null)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível carregar os dados", "O matrícula inexistente na base de dados");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		listaHistorico = aluno.getGrupoHistorico();
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(), true);
		StudentsHistory sh = importador.getSh();
		Student st = sh.getStudents().get(aluno.getMatricula());

		if (st == null) {
			logger.info("O aluno " + aluno.getMatricula() + " não possui histórico de matricula");
			FacesMessage msg = new FacesMessage(
					"O aluno:" + aluno.getMatricula() + " não possui nenhum histórico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			lgMatriculaAluno = true;
			lgNomeAluno = true;

			return;
		}

		ira = aluno.getIra();
		if (ira == -1) {
			ira = (float) 0;
		}
		periodo = aluno.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
	
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("form:gridHistorico");
		dataTable.clearInitialState();
		dataTable.reset();
	}

	public void limpaAluno() {
		aluno = new Aluno();
		lgMatriculaAluno = false;
		lgNomeAluno = false;
		ira = (float) 0;
		periodo = 0;
		listaHistorico = new ArrayList<Historico>();
	}

	public int periodoCorrente(String ingresso) {
		Calendar now = Calendar.getInstance();
		int anoAtual = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		int i = 0;
		int periodoAtual = 0;
		if (mes >= 1 && mes <= 6) {
			periodoAtual = 1;
		} else {
			periodoAtual = 3;
		}
		int anoIngresso = Integer.parseInt(ingresso.substring(0, 4));
		int periodoIngresso = Integer.parseInt(ingresso.substring(4, 5));
		while (anoIngresso != anoAtual || periodoAtual != periodoIngresso) {
			i++;
			if (periodoIngresso == 3) {
				anoIngresso++;
				periodoIngresso = 1;
			} else {
				periodoIngresso = 3;
			}
		}
		return i;
	}

	// ========================================================= GET - SET
	// ==================================================================================//

	public boolean isLgMatriculaAluno() {
		return lgMatriculaAluno;
	}

	public void setLgMatriculaAluno(boolean lgMatriculaAluno) {
		this.lgMatriculaAluno = lgMatriculaAluno;
	}

	public boolean isLgNomeAluno() {
		return lgNomeAluno;
	}

	public void setLgNomeAluno(boolean lgNomeAluno) {
		this.lgNomeAluno = lgNomeAluno;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Historico> getListaHistorico() {
		return listaHistorico;
	}

	public void setListaHistorico(List<Historico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getClasseEscolhida() {
		return classeEscolhida;
	}

	public void setClasseEscolhida(String classeEscolhida) {
		this.classeEscolhida = classeEscolhida;
	}

	public Float getIra() {
		return ira;
	}

	public void setIra(Float ira) {
		this.ira = ira;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public ImportarArvore getImportador() {
		return importador;
	}

	public void setImportador(ImportarArvore importador) {
		this.importador = importador;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public List<Historico> getListaHistoricoFiltrada() {
		return listaHistoricoFiltrada;
	}

	public void setListaHistoricoFiltrada(List<Historico> listaHistoricoFiltrada) {
		this.listaHistoricoFiltrada = listaHistoricoFiltrada;
	}

	public boolean isLgAluno() {
		return lgAluno;
	}

	public void setLgAluno(boolean lgAluno) {
		this.lgAluno = lgAluno;
	}
}
