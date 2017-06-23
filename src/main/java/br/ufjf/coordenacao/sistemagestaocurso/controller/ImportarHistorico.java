package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
//import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.ws.rs.NotAuthorizedException;

import org.apache.log4j.Logger;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.CursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeDisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.HistoricoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.IraRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;
import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao;
import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao.ServiceVersion;
import br.ufjf.ice.integra3.rs.restclient.model.v2.AlunoCurso;
import br.ufjf.ice.integra3.rs.restclient.model.v2.CursoAlunosSituacaoResponse;
import br.ufjf.ice.integra3.ws.login.interfaces.IWsLogin;
import br.ufjf.ice.integra3.ws.login.interfaces.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.service.WSLogin;

@Named
@ViewScoped
public class ImportarHistorico implements Serializable {

	// Comparador utilizado para ordenar o histórico no cálculo do IRA
	public class HistoricoComparador implements Comparator<Historico> {

		@Override
		public int compare(Historico o1, Historico o2) {
			// retorna >0 se o1 for maior que o2, <0 se o1 for menor que o2 e 0
			// se o1 == o2
			// Os historicos que são "trancado", "dispensado" e "matriculado"
			// sempre ficarão no final da lista
			// Os outros serao ordenados pela ordem dos periodos

			if (o1.getStatusDisciplina().equals("Trancado") || o1.getStatusDisciplina().equals("Dispensado")
					|| o1.getStatusDisciplina().equals("Matriculado")) {
				if (o2.getStatusDisciplina().equals("Trancado") || o2.getStatusDisciplina().equals("Dispensado")
						|| o2.getStatusDisciplina().equals("Matriculado")) {
					return 0; // Os dois são iguais
				} else {
					return 1;
				}
			} else {
				if (o2.getStatusDisciplina().equals("Trancado") || o2.getStatusDisciplina().equals("Dispensado")
						|| o2.getStatusDisciplina().equals("Matriculado")) {
					return -1; // O primeiro deve vir antes do ultimo
				} else {
					try {
						Integer s1 = Integer.parseInt(o1.getSemestreCursado());
						Integer s2 = Integer.parseInt(o2.getSemestreCursado());

						return s1.compareTo(s2);
					} catch (NumberFormatException ex) {
						return 0;
					}
				}
			}

		}

	}

	private Curso curso = new Curso();

	private Logger logger = Logger.getLogger(ImportarHistorico.class);

	@Inject
	private UsuarioController usuarioController;
	private static final long serialVersionUID = 1L;
	private Date d;
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private long tempoExecucaoIra = 0;
	private HashMap<Long, List<Disciplina>> discplinasExcluirIra = new HashMap<>();

	@Inject
	private CursoRepository cursos;
	@Inject
	private GradeDisciplinaRepository gradeDisciplinas;
	@Inject
	private AlunoRepository alunos;
	@Inject
	private DisciplinaRepository disciplinas;
	@Inject
	private HistoricoRepository historicos;
	@Inject
	private IraRepository iras;
	@Inject
	private EntityManager manager;
	/*
	 * @Inject private Importador importador;
	 */
	@Inject
	private GradeRepository grades;

	@PostConstruct
	public void init() throws IOException {

		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		usuarioController.setReseta(true);
		logger.info("Abrindo página de importação");

	}

	private Grade criarGrade(String codigo) {
		Grade grade = new Grade();
		grade.setCodigo(codigo);
		grade.setCurso(curso);
		grade.setHorasAce(0);
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);
		grade.setNumeroMaximoPeriodos(0);
		grade.setPeriodoInicio(1);
		return grade;
	}

	private void atulizaDataImportação() {
		d = new Date();
		String data = format.format(d).toString();
		curso = cursos.porid(curso.getId());
		curso.setDataAtualizacao(data);
		cursos.persistir(curso);
	}

	private Disciplina criarDisciplina(String codigo, String horasAula) {
		Disciplina disciplina = new Disciplina();
		disciplina.setCodigo(codigo);
		disciplina.setCargaHoraria(Integer.parseInt(horasAula));
		disciplina = disciplinas.persistir(disciplina);

		return disciplina;
	}

	@Transactional
	public void importar() throws Exception {
		if (usuarioController.getAutenticacao().getTipoAcesso().equals("externo")) {
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		try {
			IWsLogin integra = new WSLogin().getWsLoginServicePort();

			WsLoginResponse user;

			
			 user = integra.login(usuarioController.getAutenticacao().getLogin(),
			 usuarioController.getAutenticacao().getSenha(),
			 usuarioController.getAutenticacao().getToken());
			 
			// if(usuarioController.getAutenticacao().getLogin().equals("admin"))
			

			logger.info("Recuperando dados do curso " + curso.getCodigo() + "...");
			RSCursoAlunosDiscSituacao rsClient = new RSCursoAlunosDiscSituacao(user.getToken(), ServiceVersion.V2);
			EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();

			if (curso == null) {
				FacesMessage msg = new FacesMessage("Curso Inválido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				logger.warn("Curso inválido. Importacao cancelada");
				return;
			} else {
				if (!curso.getGrupoAlunos().isEmpty()) {

					iras.deletarTodosCurso(curso);

					logger.info("Removendo alunos do curso");
					int numAlunosRemovidos = cursos.removerTodosAlunos(curso);

					logger.info(numAlunosRemovidos + " alunos removidos");
					for (Grade grade : curso.getGrupoGrades()) {
						logger.info("Removendo alunos da grade " + grade.getCodigo());

						grade.setGrupoAlunos(new ArrayList<Aluno>());
					}
				}

				else {
					logger.info("O curso não há alunos a serem removidos");
				}
			}

			CursoAlunosSituacaoResponse rsResponse = (CursoAlunosSituacaoResponse) rsClient.get(curso.getCodigo());
			if (rsResponse.getResponseStatus() != null)
				throw new Exception(rsResponse.getResponseStatus());

			List<Grade> listaGrade = new ArrayList<Grade>();
			int contador = 0;
			int total = rsResponse.getAluno().size();

			listaGrade.addAll(curso.getGrupoGrades());

			logger.info("Processando alunos de " + curso.getCodigo());

			for (AlunoCurso alunoCurso : rsResponse.getAluno()) {

				contador++;
				logger.info("Recuperando " + String.valueOf(contador) + " de " + String.valueOf(total));
				Grade grade = null;
				for (Grade gradeQuestao : listaGrade) {
					if (gradeQuestao.getCodigo().equals(alunoCurso.getCurriculo())) {
						grade = gradeQuestao;
						break;
					}
				}

				if (grade == null) {
					grade = criarGrade(alunoCurso.getCurriculo());
					listaGrade.add(grade);
				}

				Aluno aluno;

				aluno = new Aluno();
				aluno.setMatricula(alunoCurso.getMatricula());
				aluno.setNome(alunoCurso.getNome());
				aluno.setGrade(grade);
				aluno.setCurso(curso);
				aluno = alunos.persistir(aluno);
				grade.getGrupoAlunos().add(aluno);

				logger.info("Processando aluno " + alunoCurso.getMatricula() + " - " + alunoCurso.getCurriculo());
				for (br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegra : alunoCurso
						.getDisciplinas().getDisciplina()) {
					Disciplina disciplina = disciplinas.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina());

					if (disciplina == null) {
						disciplina = criarDisciplina(disciplinaIntegra.getDisciplina(),
								disciplinaIntegra.getHorasAula());
					}

					Historico historico = new Historico();
					historico.setAluno(aluno);
					historico.setDisciplina(disciplina);
					if (disciplinaIntegra.getNota() == null || disciplinaIntegra.getNota().trim().equals("")) {
						historico.setNota("0");
					} else {
						historico.setNota(disciplinaIntegra.getNota().trim());
					}
					historico.setSemestreCursado(disciplinaIntegra.getAnoSemestre());
					historico.setStatusDisciplina(disciplinaIntegra.getSituacao());
					if (aluno.getGrupoHistorico() == null) {

						aluno.setGrupoHistorico(new ArrayList<Historico>());

					}
					historico = historicos.persistir(historico);
					aluno.getGrupoHistorico().add(historico);
				}

				calcularIra(aluno);
				logger.info(tempoExecucaoIra + "ms");

			}

			List<Grade> listaGrades = curso.getGrupoGrades();
			for (Grade grade : listaGrades) {
				estruturaArvore.removerEstrutura(grade);
				grades.persistir(grade);
				logger.info("Removendo " + grade.getCodigo() + " da estrutura");

			}
			logger.info("Atualizando Data de importação");
			atulizaDataImportação();

			logger.info("OK");

		} catch (NotAuthorizedException e) {
			throw e;
		}

		catch (Exception e) {
			logger.error("Problema na importação: " + e.getMessage());
			throw e;
		}
		logger.info("Importação terminada");
		logger.info("Cálculo do IRA: " + tempoExecucaoIra + "ms");
	}

	// TODO talvez isso aqui possa ser organizado em outro lugar
	// Método que gera os dados do IRA do aluno e os salva no BD
	@Transactional
	public void calcularIra(Aluno a) {
		long inicio = System.currentTimeMillis();

		if (!discplinasExcluirIra.containsKey(a.getGrade().getId())) {
			List<GradeDisciplina> gDisciplinas = gradeDisciplinas.buscarPorIra(a.getGrade().getId(), true);
			List<Disciplina> disc = new ArrayList<>();
			if (gDisciplinas == null)
				gDisciplinas = new ArrayList<>();

			for (GradeDisciplina gd : gDisciplinas)
				disc.add(gd.getDisciplina());

			discplinasExcluirIra.put(a.getGrade().getId(), disc);
		}

		List<Disciplina> excluir = discplinasExcluirIra.get(a.getGrade().getId());

		// Ordena a lista
		Collections.sort(a.getGrupoHistorico(), new HistoricoComparador());

		int notaPonderadaAcumulada = 0, notaPonderada = 0;
		float cargaHorariaAcumulada = 0, cargaHoraria = 0;
		String periodoCorrente = a.getGrupoHistorico().get(0).getSemestreCursado();

		for (Historico h : a.getGrupoHistorico()) {
			// Pela ordenação, as disciplinas que nao entram no calculo sempre
			// ficam no final,
			// entao ao encontrar uma delas, sabemos que nao tem mais nada para
			// entrar no calculo
			if (h.getStatusDisciplina().equals("Trancado") || h.getStatusDisciplina().equals("Dispensado")
					|| h.getStatusDisciplina().equals("Matriculado")) {
				break;
			}

			// TODO verificar se essa verificação é eficaz
			if (excluir.contains(h.getDisciplina())) {
				logger.debug(h.getDisciplina().getCodigo() + " foi excluida do IRA pelo coordenador");
				continue;
			}

			// Se mudou o periodo, entao salva os dados do anterior e limpa
			if (!periodoCorrente.equals(h.getSemestreCursado())) {
				IRA ira = new IRA();
				ira.setAluno(a);
				ira.setCurso(a.getCurso());
				ira.setGrade(a.getGrade());
				ira.setSemestre(periodoCorrente);
				ira.setIraAcumulado((cargaHorariaAcumulada > 0 ? notaPonderadaAcumulada / cargaHorariaAcumulada : 0));
				ira.setIraSemestre((cargaHoraria > 0 ? notaPonderada / cargaHoraria : 0));

				cargaHoraria = 0;
				notaPonderada = 0;

				// System.out.println(ira.getSemestre() + " -> " +
				// ira.getIraAcumulado() + " - "+ ira.getIraSemestre());
				iras.persistir(ira);

				periodoCorrente = h.getSemestreCursado();
			}

			float nota = 0;
			try {
				nota = Integer.parseInt(h.getNota());
			} catch (NumberFormatException e) {
				// Se a nota nao for valida, desconsidera, a menos que a
				// situacao seja RI
				if (!h.getStatusDisciplina().equals("Rep Freq"))
					continue;
			}

			if (!h.getStatusDisciplina().equals("Rep Freq")) {
				nota = nota * h.getDisciplina().getCargaHoraria();
				notaPonderada += nota;
				notaPonderadaAcumulada += nota;
			}

			cargaHoraria += h.getDisciplina().getCargaHoraria();
			cargaHorariaAcumulada += h.getDisciplina().getCargaHoraria();
		}

		// O último semestre que foi calculado nao foi persistido, entao ele
		// sera persistido agora

		if (cargaHoraria > 0 || cargaHorariaAcumulada > 0) {
			IRA ira = new IRA();
			ira.setAluno(a);
			ira.setCurso(a.getCurso());
			ira.setGrade(a.getGrade());
			ira.setSemestre(periodoCorrente);
			ira.setIraAcumulado((cargaHorariaAcumulada > 0 ? notaPonderadaAcumulada / cargaHorariaAcumulada : 0));
			ira.setIraSemestre((cargaHoraria > 0 ? notaPonderada / cargaHoraria : 0));

			// System.out.println(ira.getSemestre() + " -> " +
			// ira.getIraAcumulado() + " - "+ ira.getIraSemestre());
			iras.persistir(ira);
		}

		a.setIra((cargaHorariaAcumulada > 0 ? notaPonderadaAcumulada / cargaHorariaAcumulada : 0));
		long tempo = (System.currentTimeMillis() - inicio);
		tempoExecucaoIra += tempo;
	}

	public void chamarTudo() {
		try {
			if (!manager.getTransaction().isActive())
				manager.getTransaction().begin();

			importar();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Concluído",
					"Importação concluída com sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			manager.getTransaction().commit();
		} catch (NotAuthorizedException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não autorizado",
					"Você não possui permissão de importar dados");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
				logger.error("Falha. Realizado rollback da transação", e);
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao importar",
					"Ocorreu algum problema e a importação não foi realizada.\n\n" + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			logger.error("", e);
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
				logger.error("Falha. Realizado rollback da transação");
			}
		}

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
}