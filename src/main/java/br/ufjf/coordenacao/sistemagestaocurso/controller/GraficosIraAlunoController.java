package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.EventoAce;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.IRA;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;

@Named
@ViewScoped
public class GraficosIraAlunoController implements Serializable {

	// ========================================================= VARIABLES
	// ==================================================================================//

	private static final long serialVersionUID = 1L;
	private boolean lgNomeAluno = false;
	private boolean lgMatriculaAluno = false;
	private boolean lgAce = true;
	private boolean lgAluno = true;
	private Aluno aluno = new Aluno();
	private EventoAce eventosAce = new EventoAce();
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private int horasIncompletasOpcionais;
	private int horasIncompletasAce;
	private int horasIncompletasEletivas;
	private float ira;
	private int periodo;
	private int horasObrigatorias;
	private int horasAceConcluidas;
	private int horasEletivasConcluidas;
	private int horasOpcionaisConcluidas;
	private int horasObrigatoriasConcluidas;
	private EventoAce eventoAceSelecionado;
	private LineChartModel lineChartModel;
	private Curso curso = new Curso();
	
	@Inject
	private AlunoRepository alunos;

	// ========================================================= METODOS
	// ==================================================================================//

	@Inject
	private UsuarioController usuarioController;
	
	private Logger logger;

	@PostConstruct
	public void init() {
		try {
			logger = Logger.getLogger(GraficosIraAlunoController.class);
		
			
			lineChartModel = initBarModel();
			lineChartModel.setTitle("Gráfico de Evolução do IRA por Período");
			Axis yAxis = lineChartModel.getAxis(AxisType.Y);
			yAxis.setMin(0);
			yAxis.setMax(100);

			yAxis.setTickFormat("%d");
			estruturaArvore = EstruturaArvore.getInstance();
			//usuarioController.atualizarPessoaLogada();
			Grade grade = new Grade();
			grade.setHorasEletivas(0);
			grade.setHorasOpcionais(0);
			grade.setHorasAce(0);
			aluno.setGrade(grade);
			// cursoDAO = estruturaArvore.getCursoDAO();
			if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")) {
				
				aluno = alunos.buscarPorMatricula(usuarioController.getAutenticacao().getSelecaoIdentificador());
			
				logger.info("Aluno: " + aluno);
				
				lgMatriculaAluno = true;
				lgNomeAluno = true;
				lgAluno = false;

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

					FacesMessage msg = new FacesMessage("Nenhum aluno cadastrado no curso!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR ,"Ocorreu um problema", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			//trava os botoes da tela
			lgMatriculaAluno = true;
			lgNomeAluno = true;
			lgAluno = false;

		}
	}

	private LineChartModel initBarModel() {
		LineChartModel model = new LineChartModel();
		ChartSeries f = new ChartSeries();
		f.set("Semestre", 1);
		model.addSeries(f);
		return model;
	}

	private LineChartModel gerarDados() {
		LineChartModel model = new LineChartModel();
		ChartSeries fPeriodo = new ChartSeries();
		ChartSeries fAcumulado = new ChartSeries();
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(), false);
		curriculum = importador.get_cur();
		StudentsHistory sh = importador.getSh();
		Student st = sh.getStudents().get(aluno.getMatricula());

		//TreeSet<Integer> semestres = st.getCoursedSemesters();
		fPeriodo.setLabel("IRA Período");
		fAcumulado.setLabel("IRA Acumulado");
		ira = aluno.getIra();
		logger.info("Gerando IRA. Aluno " + st.getNome() + ": IRA=" +ira);
		
		periodo = aluno.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
		if (ira == -1) {
			ChartSeries f = new ChartSeries();
			ira = 0;
			f.setLabel("IRA");
			f.set("Semestre", 1);
			model.addSeries(f);
			return model;

		}

		for (IRA ira : aluno.getIras()) {
			logger.info("(" + ira.getSemestre() + ") IRA Semestre: " + ira.getIraSemestre() + "| IRA Acumulado: "
					+ ira.getIraAcumulado());
			fPeriodo.set(ira.getSemestre(), ira.getIraSemestre());
			fAcumulado.set(ira.getSemestre(), ira.getIraAcumulado());

		}

		model.addSeries(fPeriodo);
		model.addSeries(fAcumulado);
		return model;
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
			if (alunoQuestao.getNome().contains(codigo)) {
				todos.add(alunoQuestao);
			}
		}
		return todos;
	}

	public void onItemSelectMatriculaAluno() {
		
		if (aluno == null) {
			FacesMessage msg = new FacesMessage("Matrícula não cadastrada na base!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			if (lgAluno) {
				lgNomeAluno = false;
				lgMatriculaAluno = false;
			}
			return;
		}
		
		aluno = alunos.buscarPorMatricula(aluno.getMatricula());



		lgAce = false;
		lgNomeAluno = true;
		lgMatriculaAluno = true;

		if (aluno.getGrupoHistorico() == null || aluno.getGrupoHistorico().size() == 0) {

			FacesMessage msg = new FacesMessage(
					"O aluno " + aluno.getMatricula() + " não tem nenhum histórico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}

		lineChartModel = gerarDados();
		lineChartModel.setTitle("Gráfico de Evolução do IRA por Período");
		lineChartModel.setLegendPosition("se");
		Axis yAxis = lineChartModel.getAxis(AxisType.Y);
		Axis xAxis = new CategoryAxis();
		lineChartModel.getAxes().put(AxisType.X, xAxis);
		lineChartModel.setDatatipFormat("%2$.2f");
		yAxis.setLabel("IRA");
		yAxis.setTickFormat("%.2f");
		yAxis.setMin(0);
		yAxis.setMax(100);
	}

	public void limpaAluno() {
		lgAce = true;
		lgNomeAluno = false;
		lgMatriculaAluno = false;
		eventosAce = new EventoAce();
		ira = 0;
		periodo = 0;
		horasObrigatorias = 0;
		horasAceConcluidas = 0;
		horasEletivasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasObrigatoriasConcluidas = 0;
		Grade grade = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);
		grade.setHorasAce(0);
		aluno = new Aluno();
		aluno.setGrade(grade);
		init();
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public float getIra() {
		return ira;
	}

	public void setIra(float ira) {
		this.ira = ira;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
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

	public int getHorasEletivasConcluidas() {
		return horasEletivasConcluidas;
	}

	public void setHorasEletivasConcluidas(int horasEletivasConcluidas) {
		this.horasEletivasConcluidas = horasEletivasConcluidas;
	}

	public int getHorasOpcionaisConcluidas() {
		return horasOpcionaisConcluidas;
	}

	public void setHorasOpcionaisConcluidas(int horasOpcionaisConcluidas) {
		this.horasOpcionaisConcluidas = horasOpcionaisConcluidas;
	}

	public int getHorasObrigatoriasConcluidas() {
		return horasObrigatoriasConcluidas;
	}

	public void setHorasObrigatoriasConcluidas(int horasObrigatoriasConcluidas) {
		this.horasObrigatoriasConcluidas = horasObrigatoriasConcluidas;
	}

	public int getHorasObrigatorias() {
		return horasObrigatorias;
	}

	public void setHorasObrigatorias(int horasObrigatorias) {
		this.horasObrigatorias = horasObrigatorias;
	}

	public EventoAce getEventosAce() {
		return eventosAce;
	}

	public void setEventosAce(EventoAce eventosAce) {
		this.eventosAce = eventosAce;
	}

	public int getHorasAceConcluidas() {
		return horasAceConcluidas;
	}

	public void setHorasAceConcluidas(int horasAceConcluidas) {
		this.horasAceConcluidas = horasAceConcluidas;
	}

	public boolean isLgAce() {
		return lgAce;
	}

	public void setLgAce(boolean lgAce) {
		this.lgAce = lgAce;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public EventoAce getEventoAceSelecionado() {
		return eventoAceSelecionado;
	}

	public void setEventoAceSelecionado(EventoAce eventoAceSelecionado) {
		this.eventoAceSelecionado = eventoAceSelecionado;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public UsuarioController getUsuarioController() {
		return usuarioController;
	}

	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}

	public boolean isLgAluno() {
		return lgAluno;
	}

	public void setLgAluno(boolean lgAluno) {
		this.lgAluno = lgAluno;
	}

	public int getHorasIncompletasOpcionais() {
		return horasIncompletasOpcionais;
	}

	public void setHorasIncompletasOpcionais(int horasIncompletasOpcionais) {
		this.horasIncompletasOpcionais = horasIncompletasOpcionais;
	}

	public int getHorasIncompletasAce() {
		return horasIncompletasAce;
	}

	public void setHorasIncompletasAce(int horasIncompletasAce) {
		this.horasIncompletasAce = horasIncompletasAce;
	}

	public int getHorasIncompletasEletivas() {
		return horasIncompletasEletivas;
	}

	public void setHorasIncompletasEletivas(int horasIncompletasEletivas) {
		this.horasIncompletasEletivas = horasIncompletasEletivas;
	}

	public LineChartModel getLineChartModel() {
		return lineChartModel;
	}

	public void setLineChartModel(LineChartModel lineChartModel) {
		this.lineChartModel = lineChartModel;
	}
}