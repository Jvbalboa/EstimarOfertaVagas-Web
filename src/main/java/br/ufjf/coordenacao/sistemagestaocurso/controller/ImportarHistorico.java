package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
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
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.HistoricoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.Importador;
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
public class ImportarHistorico implements Serializable{

	private Curso curso = new Curso();

	private Logger logger = Logger.getLogger(ImportarHistorico.class);
	
	@Inject
	private UsuarioController usuarioController;
	private static final long serialVersionUID = 1L;
	private Date d ;
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@Inject
	private CursoRepository cursos ;
	@Inject
	private AlunoRepository alunos;
	@Inject
	private DisciplinaRepository disciplinas;
	@Inject
	private HistoricoRepository historicos;
	@Inject
	private EntityManager manager;
	
	@Inject
	private Importador importador;

	@Inject
	private GradeRepository grades;

	@PostConstruct
	public void init() throws IOException {
		
		
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		usuarioController.setReseta(true);

	}
	
	private Grade criarGrade(String codigo)
	{
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

	@Transactional
	private void atulizaDataImportação()
	{
		d = new Date();
		String data = format.format(d).toString();
		curso = cursos.porid(curso.getId());
		curso.setDataAtualizacao(data);
		cursos.persistir(curso);
	}
	
	@Transactional
	private Disciplina criarDisciplina(String codigo, String horasAula)
	{
		Disciplina disciplina = new Disciplina();
		disciplina.setCodigo(codigo);
		disciplina.setCargaHoraria(Integer.parseInt(horasAula));
		disciplina = disciplinas.persistir(disciplina);
		
		return disciplina;
	}
	
	@Transactional
	public void chamarTudo() throws Exception {
		//manager.cl
		if (usuarioController.getAutenticacao().getTipoAcesso().equals("externo")){
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		try {
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			
			WsLoginResponse user;
			

				user = integra.login(usuarioController.getAutenticacao().getLogin(), usuarioController.getAutenticacao().getSenha(), usuarioController.getAutenticacao().getToken());
			
			logger.info("Recuperando dados do curso "+ curso.getCodigo() +"...");
			RSCursoAlunosDiscSituacao rsClient = new RSCursoAlunosDiscSituacao(user.getToken(), ServiceVersion.V2);
			EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();

			if (curso == null){
				FacesMessage msg = new FacesMessage("Curso Inválido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				logger.warn("Curso inválido");
				return;
			}
			else {
				int numAlunosRemovidos = cursos.removerTodosAlunos(curso);
				logger.info(numAlunosRemovidos + " alunos removidos");
				for (Grade grade:curso.getGrupoGrades()){
					logger.info("Removendo alunos da grade " + grade.getCodigo());
					/*for (Aluno alunoQuestao : grade.getGrupoAlunos()){
						alunos.remover(alunoQuestao);
						logger.info("Removido aluno " + alunoQuestao.getMatricula());
					}*/
					grade.setGrupoAlunos(new ArrayList<Aluno>());
				}
			}

			CursoAlunosSituacaoResponse rsResponse = (CursoAlunosSituacaoResponse) rsClient.get(curso.getCodigo());
			if (rsResponse.getResponseStatus() != null) 
				throw new Exception (rsResponse.getResponseStatus());

			List<Grade> listaGrade = new ArrayList<Grade>();			
			int contador = 0;
			int total = rsResponse.getAluno().size();

			listaGrade.addAll(curso.getGrupoGrades());
			
			logger.info("Processando alunos de " + curso.getCodigo());

			for(AlunoCurso alunoCurso : rsResponse.getAluno()) {

				contador ++;
				logger.info("Recuperando " + String.valueOf(contador) + " de " + String.valueOf(total));
				Grade grade = null;
				for(Grade gradeQuestao : listaGrade){
					if (gradeQuestao.getCodigo().equals(alunoCurso.getCurriculo())){
						grade = gradeQuestao;
						break;
					}
				}
				
				if (grade == null){
					grade = criarGrade(alunoCurso.getCurriculo());
					listaGrade.add(grade);
				}

				Aluno aluno = alunos.buscarPorMatricula(alunoCurso.getMatricula());

				if ( aluno == null){
					aluno = new Aluno();
					aluno.setMatricula(alunoCurso.getMatricula());
					aluno.setNome(alunoCurso.getNome());
					aluno.setGrade(grade);
					aluno.setCurso(curso);
					aluno = alunos.persistir(aluno);
					grade.getGrupoAlunos().add(aluno);

				} 

				logger.info("Processando aluno " + alunoCurso.getMatricula() + " - " + alunoCurso.getCurriculo());
				for (br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegra : alunoCurso.getDisciplinas().getDisciplina()) {
					Disciplina disciplina = disciplinas.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina());
					
					if (disciplina == null){
						disciplina = criarDisciplina(disciplinaIntegra.getDisciplina(), disciplinaIntegra.getHorasAula());
					}

					Historico historico = new Historico();
					historico.setAluno(aluno);
					historico.setDisciplina(disciplina);
					if (disciplinaIntegra.getNota() == null || disciplinaIntegra.getNota().trim().equals("")){
						historico.setNota("0");
					}
					else {
						historico.setNota(disciplinaIntegra.getNota().trim());
					}
					historico.setSemestreCursado(disciplinaIntegra.getAnoSemestre());
					historico.setStatusDisciplina(disciplinaIntegra.getSituacao());
					if (aluno.getGrupoHistorico() == null){

						aluno.setGrupoHistorico(new ArrayList<Historico>());

					}
					historico = historicos.persistir(historico);
					aluno.getGrupoHistorico().add(historico);
				}
			}
			/*logger.info("OK. Enviando dados para o importador");
			importador.gravarRegistros(listaGrade);
			logger.info("Importador terminado. Atualizando dados");*/
			

			List<Grade> listaGrades = curso.getGrupoGrades();
			for (Grade grade:listaGrades){
				estruturaArvore.removerEstrutura(grade);
				grades.persistir(grade);
				logger.info("Removendo " + grade.getCodigo() + " da estrutura");
			}
			logger.info("Atualizando Data de importação");
			atulizaDataImportação();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Finalizada!", "Importação concluída com sucesso"));
			logger.info("OK");

		} catch (NotAuthorizedException e) {
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			logger.warn("Erro de autorização");
			} 
		
		//Exception removida na nova versao da API do Integra
		/*catch (WsException_Exception e) {
			FacesMessage msg = new FacesMessage("Ocorreu um problema ao importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		}*/
		catch (Exception e) {
			FacesMessage msg = new FacesMessage("Ocorreu um problema ao importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			throw e;
		}
		logger.info("Importação terminada");
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