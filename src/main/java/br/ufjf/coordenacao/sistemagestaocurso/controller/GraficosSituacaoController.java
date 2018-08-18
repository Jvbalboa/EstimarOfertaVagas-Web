package br.ufjf.coordenacao.sistemagestaocurso.controller;

import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.SituacaoDisciplina;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.EventoAceRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartModelRow;
import org.primefaces.extensions.component.gchart.model.GChartType;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

@Named
@ViewScoped
public class GraficosSituacaoController implements Serializable {

	// ========================================================= VARIABLES
	// ==================================================================================//

	private static final long serialVersionUID = 1L;
	private boolean lgNomeAluno = false;
	private boolean lgMatriculaAluno = false;
	private boolean lgAce = true;
	private boolean lgAluno = true;
	private Aluno aluno = new Aluno();
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

	private List<EventoAce> listaEventosAce = new ArrayList<EventoAce>();
	private List<SituacaoDisciplina> listaDisciplinaEletivasSelecionadas;
	private List<SituacaoDisciplina> listaDisciplinaOpcionaisSelecionadas;
	private List<SituacaoDisciplina> listaDisciplinaObrigatoriasSelecionadas;
	private List<SituacaoDisciplina> listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
	private List<SituacaoDisciplina> listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
	private List<SituacaoDisciplina> listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
	private List<SituacaoDisciplina> listaDisciplinaSelecionadas = new ArrayList<SituacaoDisciplina>();
	private HorizontalBarChartModel animatedModel2;
	private GChartType chartTypePie = GChartType.PIE;
	private GChartModel chartModelPie = null;
	private Curso curso = new Curso();
	private String selecao;
	private String status;

	@Inject
	private AlunoRepository alunoDAO;
	@Inject
	private DisciplinaRepository disciplinaDAO;
	@Inject
	private EventoAceRepository eventosace;
	// ========================================================= METODOS
	// ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	public void preencheSobraHorasEletivas() {
		if (this.aluno.getSobraHorasEletivas() > 0) {
			SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
			disciplinaSituacao.setCodigo("");
			disciplinaSituacao.setSituacao("");
			disciplinaSituacao.setCargaHoraria(this.aluno.getSobraHorasEletivas() + "");
			disciplinaSituacao.setNome("EXCEDENTE EM DISCIPLINAS ELETIVAS");
			listaDisciplinaOpcionais.add(disciplinaSituacao);
		}
	}
	
	public void preencheSobraHorasOpcionais() {
		if(this.aluno.getSobraHorasOpcionais() > 0)
		{
			horasAceConcluidas += this.aluno.getSobraHorasOpcionais();
			EventoAce evento = new EventoAce();
			evento.setDescricao("EXCEDENTE EM DISCIPLINAS OPCIONAIS");
			evento.setHoras((long)this.aluno.getSobraHorasOpcionais());
			evento.setExcluir(false);
			listaEventosAce.add(evento);
		}
	}

	@PostConstruct
	public void init() {
		try {
			animatedModel2 = initBarModel();
			animatedModel2.setTitle("Gráfico - Quantidade de Horas por Atividades");
			animatedModel2.setLegendPosition("se");
			animatedModel2.setStacked(true);
			animatedModel2.setZoom(true);
			animatedModel2.setDatatipFormat("%1$d");
			Axis xAxis = animatedModel2.getAxis(AxisType.X);
			xAxis.setLabel("Quantidade Horas");
			Axis yAxis = animatedModel2.getAxis(AxisType.Y);
			yAxis.setLabel("Tipo Atividade");
			yAxis.setMin(0);
			yAxis.setTickInterval("10");
			yAxis.setMax(20);
			chartModelPie = new GChartModelBuilder().setChartType(getChartTypePie())
					.addColumns("Atividade", "Descrição").addRow("Dados", 1).build();
			estruturaArvore = EstruturaArvore.getInstance();
			usuarioController.atualizarPessoaLogada();

			Grade grade = new Grade();
			grade.setHorasEletivas(0);
			grade.setHorasOpcionais(0);
			grade.setHorasAce(0);
			aluno.setGrade(grade);

			if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")) {
				aluno = alunoDAO.buscarPorMatricula(usuarioController.getAutenticacao().getSelecaoIdentificador());
				lgMatriculaAluno = true;
				lgNomeAluno = true;
				lgAluno = false;
				if (aluno.getMatricula() == null) {
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
			// TODO: handle exception
		}
	}

	private HorizontalBarChartModel initBarModel() {
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		ChartSeries f = new ChartSeries();
		f.setLabel("Horas Completas");
		f.set("ACE", 1);
		f.set("Opcionais", 1);
		f.set("Eletivas", 1);
		f.set("Obrigatórias", 1);
		model.addSeries(f);
		ChartSeries fi = new ChartSeries();
		fi.setLabel("Horas Incompletas");
		fi.set("ACE", 1);
		fi.set("Opcionais", 1);
		fi.set("Eletivas", 1);
		fi.set("Obrigatórias", 1);
		model.addSeries(fi);
		return model;
	}

	private void dadosGraficoAluno() {
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		ChartSeries f = new ChartSeries();
		f.setLabel("Horas Completas");
		f.set("ACE", horasAceConcluidas);
		f.set("Opcionais", horasOpcionaisConcluidas);
		f.set("Eletivas", horasEletivasConcluidas);
		f.set("Obrigatórias", horasObrigatoriasConcluidas);
		model.addSeries(f);
		ChartSeries fi = new ChartSeries();
		fi.setLabel("Horas Incompletas");
		fi.set("ACE", horasIncompletasAce);
		fi.set("Opcionais", horasIncompletasOpcionais);
		fi.set("Eletivas", horasIncompletasEletivas);
		fi.set("Obrigatórias", (horasObrigatorias - horasObrigatoriasConcluidas));
		model.addSeries(fi);
		animatedModel2 = model;
		animatedModel2.setTitle("Gráfico - Atividades por Tipo");
		animatedModel2.setLegendPosition("se");
		animatedModel2.setStacked(true);
		animatedModel2.setZoom(true);
		animatedModel2.setDatatipFormat("%1$d");
		Axis xAxis = animatedModel2.getAxis(AxisType.X);
		xAxis.setLabel("Quantidade Horas");
		Axis yAxis = animatedModel2.getAxis(AxisType.Y);
		yAxis.setLabel("Tipo Atividade");
		yAxis.setMin(0);
		yAxis.setTickInterval("10");
		yAxis.setMax(20);

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

		aluno = alunoDAO.buscarPorMatricula(aluno.getMatricula());
		aluno.setDisciplinaRepository(disciplinaDAO);
		aluno.setEventoAceRepository(eventosace);

		if (aluno == null) {
			FacesMessage msg = new FacesMessage("Matrícula não cadastrada na base!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			if (lgAluno) {
				lgNomeAluno = false;
				lgMatriculaAluno = false;
			}
			return;
		}
		lgAce = false;
		lgNomeAluno = true;
		lgMatriculaAluno = true;
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(), true);
		curriculum = importador.get_cur();
		StudentsHistory sh = importador.getSh();
		Student st = sh.getStudents().get(aluno.getMatricula());

		if (st == null) {

			FacesMessage msg = new FacesMessage(
					"O aluno:" + aluno.getMatricula() + " não tem nenhum histórico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}

		listaEventosAce = this.eventosace.buscarPorMatricula(this.aluno.getMatricula());
		if (listaEventosAce != null) {
			for (EventoAce evento : listaEventosAce) {
				horasAceConcluidas = (int) (horasAceConcluidas + evento.getHoras());
			}
		} else {
			listaEventosAce = new ArrayList<EventoAce>();
		}
		/*if (horasAceConcluidas > aluno.getGrade().getHorasAce()) {

			horasAceConcluidas = aluno.getGrade().getHorasAce();
		}*/
		
		if (this.aluno.getSobraHorasEletivas() > 0) {
			SituacaoDisciplina disciplinaSituacao = this.aluno.getExcedenteEletivas();
			listaDisciplinaOpcionais.add(disciplinaSituacao);
		}
		
		if(this.aluno.getSobraHorasOpcionais() > 0)
		{
			horasAceConcluidas += this.aluno.getSobraHorasOpcionais();
			EventoAce evento = this.aluno.getExcedenteOpcionais();
			listaEventosAce.add(evento);
		}
		
		gerarDadosAluno(st, curriculum);
		ira = aluno.getIra();
		periodo = aluno.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
		horasIncompletasEletivas = 0;
		if (horasEletivasConcluidas > aluno.getGrade().getHorasEletivas()) {
			horasIncompletasEletivas = 0;
		} else {
			horasIncompletasEletivas = aluno.getGrade().getHorasEletivas() - horasEletivasConcluidas;
		}
		horasIncompletasOpcionais = 0;
		if (horasOpcionaisConcluidas > aluno.getGrade().getHorasOpcionais()) {
			horasIncompletasOpcionais = 0;
		} else {
			horasIncompletasOpcionais = aluno.getGrade().getHorasOpcionais() - horasOpcionaisConcluidas;
		}
		horasIncompletasAce = 0;
		if (horasAceConcluidas > aluno.getGrade().getHorasAce()) {
			horasIncompletasAce = 0;
		} else {
			horasIncompletasAce = aluno.getGrade().getHorasAce() - horasAceConcluidas;
		}
		chartModelPie = new GChartModelBuilder().setChartType(getChartTypePie()).addColumns("Atividade", "Descrição")
				.addRow("Horas Obrigatórias Completas", horasObrigatoriasConcluidas)
				.addRow("Horas Obrigatórias Incompletas", (horasObrigatorias - horasObrigatoriasConcluidas))
				.addRow("Horas Eletivas Completas", horasEletivasConcluidas)
				.addRow("Horas Eletivas Incompletas", (horasIncompletasEletivas))
				.addRow("Horas Opcionais Completas", horasOpcionaisConcluidas)
				.addRow("Horas Opcionais Incompletas", (horasIncompletasOpcionais))
				.addRow("Horas Ace Completas", horasAceConcluidas)
				.addRow("Horas Ace Incompletas", (horasIncompletasAce)).build();
		dadosGraficoAluno();
	}

	public void limpaAluno() {
		lgAce = true;
		lgNomeAluno = false;
		lgMatriculaAluno = false;
		listaDisciplinaSelecionadas = new ArrayList<SituacaoDisciplina>();
		listaEventosAce = new ArrayList<EventoAce>();
		listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
		selecao = "";
		status = "";
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
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("form:gridObrigatorias");
		dataTable.clearInitialState();
		dataTable.reset();
		init();
		chartModelPie = new GChartModelBuilder().setChartType(getChartTypePie()).addColumns("Atividade", "Descrição")
				.addRow("Dados", 1).build();
	}

	public void gerarDadosAluno(Student st, Curriculum cur) {
		HashMap<Class, ArrayList<String[]>> aprovado;
		listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
		horasObrigatorias = 0;
		horasObrigatoriasConcluidas = aluno.getHorasObrigatoriasCompletadas();
		horasOpcionaisConcluidas = aluno.getHorasOpcionaisCompletadas();
		horasEletivasConcluidas = aluno.getHorasEletivasCompletadas();
		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED));
		TreeSet<String> naocompletado = new TreeSet<String>();
		boolean lgPeriodoAtual = false;
		for (int i : cur.getMandatories().keySet()) {
			for (Class c : cur.getMandatories().get(i)) {
				horasObrigatorias = horasObrigatorias + c.getWorkload();
				if (!aprovado.containsKey(c)) {
					if (lgPeriodoAtual == false) {
						aluno.setPeriodoReal(i);
						lgPeriodoAtual = true;
					}
					naocompletado.add(c.getId());
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setSituacao("NAO APROVADO");
					disciplinaSituacao.setCodigo(c.getId());
					disciplinaSituacao.setPeriodo(Integer.toString(i));
					disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));
					disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					listaDisciplinaObrigatorias.add(disciplinaSituacao);
				} else {
					// horasObrigatoriasConcluidas = horasObrigatoriasConcluidas
					// + c.getWorkload();
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCodigo(c.getId());
					disciplinaSituacao.setSituacao("APROVADO");
					disciplinaSituacao.setPeriodo(Integer.toString(i));
					disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));
					disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					listaDisciplinaObrigatorias.add(disciplinaSituacao);
					aprovado.remove(c);
				}
			}
		}
		// int creditos = 0;
		for (Class c : cur.getElectives()) {
			if (aprovado.containsKey(c)) {
				SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
				disciplinaSituacao.setCodigo(c.getId());
				disciplinaSituacao.setSituacao("APROVADO");
				disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));
				disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
				listaDisciplinaEletivas.add(disciplinaSituacao);
				// creditos += c.getWorkload();
				aprovado.remove(c);
			}
		}
		// horasEletivasConcluidas = creditos;
		if (horasEletivasConcluidas <= aluno.getGrade().getHorasEletivas()) {

		} else {
			horasEletivasConcluidas = aluno.getGrade().getHorasEletivas();
		}

		// creditos = 0;
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while (i.hasNext()) {
			Class c = i.next();
			ArrayList<String[]> classdata = aprovado.get(c);
			for (String[] s2 : classdata) {
				if (s2[1].equals("APR") || s2[1].equals("A")) {
					EventoAce evento = new EventoAce();
					horasAceConcluidas = horasAceConcluidas + c.getWorkload();
					evento.setDescricao(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					evento.setHoras((long) c.getWorkload());
					String periodo = s2[0];
					evento.setPeriodo(Integer.parseInt(periodo));
					evento.setExcluir(false);
					listaEventosAce.add(evento);
				} else {

					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCodigo(c.getId());
					disciplinaSituacao.setSituacao("APROVADO");
					disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));
					disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					listaDisciplinaOpcionais.add(disciplinaSituacao);
					// creditos += c.getWorkload();
				}
			}
		}
		// horasOpcionaisConcluidas = creditos;

		if (horasOpcionaisConcluidas <= aluno.getGrade().getHorasOpcionais()) {

		} else {
			horasOpcionaisConcluidas = aluno.getGrade().getHorasOpcionais();
		}
	}

	public void onSelectPizza(SelectEvent event) {
		listaDisciplinaSelecionadas = new ArrayList<SituacaoDisciplina>();
		JsonArray value = (JsonArray) event.getObject();
		if (value.size() > 0) {
			JsonElement element = value.get(0);
			String label = new ArrayList<GChartModelRow>(this.getChartModelPie().getRows())
					.get(element.getAsJsonObject().get("row").getAsInt()).getLabel();
			if (label.equals("Horas Obrigatrias Completas")) {
				selecao = "Horas Obrigatrias Completas";
				status = Integer.toString(horasObrigatoriasConcluidas) + " / " + Integer.toString(horasObrigatorias);
				for (SituacaoDisciplina disciplinaSituacao : listaDisciplinaObrigatorias) {
					if (disciplinaSituacao.getSituacao().equals("APROVADO")) {
						listaDisciplinaSelecionadas.add(disciplinaSituacao);
					}
				}
			} else if (label.equals("Horas Obrigatrias Incompletas")) {
				selecao = "Horas Obrigatrias Incompletas";
				status = Integer.toString(horasObrigatorias - horasObrigatoriasConcluidas) + " / "
						+ Integer.toString(horasObrigatorias);
				for (SituacaoDisciplina disciplinaSituacao : listaDisciplinaObrigatorias) {
					if (!disciplinaSituacao.getSituacao().equals("APROVADO")) {
						listaDisciplinaSelecionadas.add(disciplinaSituacao);
					}
				}
			} else if (label.equals("Horas Eletivas Completas")) {
				status = Integer.toString(horasEletivasConcluidas) + " / "
						+ Integer.toString(aluno.getGrade().getHorasEletivas());
				selecao = "Horas Eletivas Completas";
				listaDisciplinaSelecionadas = listaDisciplinaEletivas;
			} else if (label.equals("Horas Opcionais Completas")) {
				status = Integer.toString(horasOpcionaisConcluidas) + " / "
						+ Integer.toString(aluno.getGrade().getHorasOpcionais());
				selecao = "Horas Opcionais Completas";
				listaDisciplinaSelecionadas = listaDisciplinaOpcionais;
			} else if (label.equals("Horas Ace Completas")) {
				status = Integer.toString(horasAceConcluidas) + " / "
						+ Integer.toString(aluno.getGrade().getHorasAce());
				selecao = "Horas Ace Completas";
				listaDisciplinaSelecionadas = new ArrayList<SituacaoDisciplina>();
				for (EventoAce eventoAce : listaEventosAce) {
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCargaHoraria(Long.toString((eventoAce.getHoras())));
					disciplinaSituacao.setNome(eventoAce.getDescricao());
					disciplinaSituacao.setPeriodo(Integer.toString(eventoAce.getPeriodo()));
					listaDisciplinaSelecionadas.add(disciplinaSituacao);
				}
			} else if (label.equals("Horas Eletivas Incompletas")) {
				selecao = label;
				int horasIncompletas = 0;
				if (horasEletivasConcluidas > aluno.getGrade().getHorasEletivas()) {
					horasIncompletas = 0;
				} else {
					horasIncompletas = aluno.getGrade().getHorasEletivas() - horasEletivasConcluidas;
				}
				status = Integer.toString(horasIncompletas) + " / "
						+ Integer.toString(aluno.getGrade().getHorasEletivas());
			} else if (label.equals("Horas Opcionais Incompletas")) {
				selecao = label;
				int horasIncompletas = 0;
				if (horasOpcionaisConcluidas > aluno.getGrade().getHorasOpcionais()) {
					horasIncompletas = 0;
				} else {
					horasIncompletas = aluno.getGrade().getHorasOpcionais() - horasOpcionaisConcluidas;
				}
				status = Integer.toString(horasIncompletas) + " / "
						+ Integer.toString(aluno.getGrade().getHorasOpcionais());
			} else if (label.equals("Horas Ace Incompletas")) {
				selecao = label;
				int horasIncompletas = 0;
				if (horasAceConcluidas > aluno.getGrade().getHorasAce()) {
					horasIncompletas = 0;
				} else {
					horasIncompletas = aluno.getGrade().getHorasAce() - horasAceConcluidas;
				}
				status = Integer.toString(horasIncompletas) + " / " + Integer.toString(aluno.getGrade().getHorasAce());
			}
		}
	}

	public void itemSelect(ItemSelectEvent event) {
		listaDisciplinaSelecionadas = new ArrayList<SituacaoDisciplina>();
		int linha = event.getItemIndex();
		int coluna = event.getSeriesIndex() + 1;
		if (linha == 3 && coluna == 1) {
			selecao = "Horas Obrigatórias Completas";
			status = Integer.toString(horasObrigatoriasConcluidas) + " / " + Integer.toString(horasObrigatorias);
			for (SituacaoDisciplina disciplinaSituacao : listaDisciplinaObrigatorias) {
				if (disciplinaSituacao.getSituacao().equals("APROVADO")) {
					listaDisciplinaSelecionadas.add(disciplinaSituacao);
				}
			}
		} else if (linha == 3 && coluna == 2) {
			selecao = "Horas Obrigatrias Incompletas";
			status = Integer.toString(horasObrigatorias - horasObrigatoriasConcluidas) + " / "
					+ Integer.toString(horasObrigatorias);
			for (SituacaoDisciplina disciplinaSituacao : listaDisciplinaObrigatorias) {
				if (!disciplinaSituacao.getSituacao().equals("APROVADO")) {
					listaDisciplinaSelecionadas.add(disciplinaSituacao);
				}
			}
		} else if (linha == 2 && coluna == 1) {

			selecao = "Horas Eletivas Completas";
			status = Integer.toString(horasEletivasConcluidas) + " / "
					+ Integer.toString(aluno.getGrade().getHorasEletivas());
			listaDisciplinaSelecionadas = listaDisciplinaEletivas;
		} else if (linha == 2 && coluna == 2) {
			selecao = "Horas Eletivas Incompletas";
			int horasIncompletas = 0;
			if (horasEletivasConcluidas > aluno.getGrade().getHorasEletivas()) {
				horasIncompletas = 0;
			} else {
				horasIncompletas = aluno.getGrade().getHorasEletivas() - horasEletivasConcluidas;
			}
			status = Integer.toString(horasIncompletas) + " / " + Integer.toString(aluno.getGrade().getHorasEletivas());
		} else if (linha == 1 && coluna == 1) {

			selecao = "Horas Opcionais Completas";
			listaDisciplinaSelecionadas = listaDisciplinaOpcionais;
			status = Integer.toString(horasOpcionaisConcluidas) + " / "
					+ Integer.toString(aluno.getGrade().getHorasOpcionais());
		} else if (linha == 1 && coluna == 2) {
			selecao = "Horas Opcionais Incompletas";
			int horasIncompletas = 0;
			if (horasOpcionaisConcluidas > aluno.getGrade().getHorasOpcionais()) {
				horasIncompletas = 0;
			} else {
				horasIncompletas = aluno.getGrade().getHorasOpcionais() - horasOpcionaisConcluidas;
			}
			status = Integer.toString(horasIncompletas) + " / "
					+ Integer.toString(aluno.getGrade().getHorasOpcionais());
		} else if (linha == 0 && coluna == 1) {
			selecao = "Horas Ace Completas";
			status = Integer.toString(horasAceConcluidas) + " / " + Integer.toString(aluno.getGrade().getHorasAce());
			listaDisciplinaSelecionadas = new ArrayList<SituacaoDisciplina>();
			for (EventoAce eventoAce : listaEventosAce) {
				SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
				disciplinaSituacao.setCargaHoraria(Long.toString((eventoAce.getHoras())));
				disciplinaSituacao.setNome(eventoAce.getDescricao());
				disciplinaSituacao.setPeriodo(Integer.toString(eventoAce.getPeriodo()));
				listaDisciplinaSelecionadas.add(disciplinaSituacao);
			}
		} else if (linha == 0 && coluna == 2) {
			selecao = "Horas Ace Incompletas";
			int horasIncompletas = 0;
			if (horasAceConcluidas > aluno.getGrade().getHorasAce()) {
				horasIncompletas = 0;
			} else {
				horasIncompletas = aluno.getGrade().getHorasAce() - horasAceConcluidas;
			}
			status = Integer.toString(horasIncompletas) + " / " + Integer.toString(aluno.getGrade().getHorasAce());

		}
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

	public List<SituacaoDisciplina> getListaDisciplinaObrigatorias() {
		return listaDisciplinaObrigatorias;
	}

	public void setListaDisciplinaObrigatorias(List<SituacaoDisciplina> listaDisciplinaObrigatorias) {
		this.listaDisciplinaObrigatorias = listaDisciplinaObrigatorias;
	}

	public List<SituacaoDisciplina> getListaDisciplinaEletivas() {
		return listaDisciplinaEletivas;
	}

	public void setListaDisciplinaEletivas(List<SituacaoDisciplina> listaDisciplinaEletivas) {
		this.listaDisciplinaEletivas = listaDisciplinaEletivas;
	}

	public List<SituacaoDisciplina> getListaDisciplinaOpcionais() {
		return listaDisciplinaOpcionais;
	}

	public void setListaDisciplinaOpcionais(List<SituacaoDisciplina> listaDisciplinaOpcionais) {
		this.listaDisciplinaOpcionais = listaDisciplinaOpcionais;
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

	public List<EventoAce> getListaEventosAce() {
		return listaEventosAce;
	}

	public void setListaEventosAce(List<EventoAce> listaEventosAce) {
		this.listaEventosAce = listaEventosAce;
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

	public GChartType getChartTypePie() {
		return chartTypePie;
	}

	public void setChartTypePie(GChartType chartTypePie) {
		this.chartTypePie = chartTypePie;
	}

	public GChartModel getChartModelPie() {
		return chartModelPie;
	}

	public void setChartModelPie(GChartModel chartModelPie) {
		this.chartModelPie = chartModelPie;
	}

	public List<SituacaoDisciplina> getListaDisciplinaSelecionadas() {
		return listaDisciplinaSelecionadas;
	}

	public void setListaDisciplinaSelecionadas(List<SituacaoDisciplina> listaDisciplinaSelecionadas) {
		this.listaDisciplinaSelecionadas = listaDisciplinaSelecionadas;
	}

	public String getSelecao() {
		return selecao;
	}

	public void setSelecao(String selecao) {
		this.selecao = selecao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public HorizontalBarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	public void setAnimatedModel2(HorizontalBarChartModel animatedModel2) {
		this.animatedModel2 = animatedModel2;
	}

	public List<SituacaoDisciplina> getListaDisciplinaEletivasSelecionadas() {
		return listaDisciplinaEletivasSelecionadas;
	}

	public void setListaDisciplinaEletivasSelecionadas(List<SituacaoDisciplina> listaDisciplinaEletivasSelecionadas) {
		this.listaDisciplinaEletivasSelecionadas = listaDisciplinaEletivasSelecionadas;
	}

	public List<SituacaoDisciplina> getListaDisciplinaOpcionaisSelecionadas() {
		return listaDisciplinaOpcionaisSelecionadas;
	}

	public void setListaDisciplinaOpcionaisSelecionadas(List<SituacaoDisciplina> listaDisciplinaOpcionaisSelecionadas) {
		this.listaDisciplinaOpcionaisSelecionadas = listaDisciplinaOpcionaisSelecionadas;
	}

	public List<SituacaoDisciplina> getListaDisciplinaObrigatoriasSelecionadas() {
		return listaDisciplinaObrigatoriasSelecionadas;
	}

	public void setListaDisciplinaObrigatoriasSelecionadas(
			List<SituacaoDisciplina> listaDisciplinaObrigatoriasSelecionadas) {
		this.listaDisciplinaObrigatoriasSelecionadas = listaDisciplinaObrigatoriasSelecionadas;
	}

}
