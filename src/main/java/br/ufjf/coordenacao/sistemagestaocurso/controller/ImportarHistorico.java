package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotAuthorizedException;

import org.apache.log4j.Logger;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.CursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
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
	AlunoRepository alunos;
	@Inject
	DisciplinaRepository disciplinas;
	@Inject
	Importador importador;

	@PostConstruct
	public void init() throws IOException {
		
		
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		usuarioController.setReseta(true);

	}

	@Transactional
	public void chamarTudo()  {

		if (usuarioController.getAutenticacao().getTipoAcesso().equals("externo")){
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		try {
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			WsLoginResponse user = integra.login(usuarioController.getAutenticacao().getLogin(), usuarioController.getAutenticacao().getSenha(), usuarioController.getAutenticacao().getToken());
			logger.info("Recuperando dados do curso "+ curso.getCodigo() +"...");
			RSCursoAlunosDiscSituacao rsClient = new RSCursoAlunosDiscSituacao(user.getToken(), ServiceVersion.V2);
			EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();
			

			d = new Date();
			String data = format.format(d).toString();
			curso.setDataAtualizacao(data);
			cursos.persistir(curso);
			
			

			if (curso == null){
				FacesMessage msg = new FacesMessage("Curso Inválido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			else {

				for (Grade grade:curso.getGrupoGrades()){
					for (Aluno alunoQuestao : grade.getGrupoAlunos()){
						alunos.remover(alunoQuestao);
					}
					grade.setGrupoAlunos(new ArrayList<Aluno>());
				}
			}

			CursoAlunosSituacaoResponse rsResponse = (CursoAlunosSituacaoResponse) rsClient.get(curso.getCodigo());
			if (rsResponse.getResponseStatus() != null) 
				throw new Exception (rsResponse.getResponseStatus());

			List<Grade> listaGrade = new ArrayList<Grade>();			
			int contador = 0;
			int total = rsResponse.getAluno().size();

			/**/
			listaGrade.addAll(curso.getGrupoGrades());
			
			logger.info(curso.getCodigo());

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
					grade = new Grade();
					grade.setCodigo(alunoCurso.getCurriculo());
					grade.setCurso(curso);
					grade.setHorasAce(0);
					grade.setHorasEletivas(0);
					grade.setHorasOpcionais(0);
					grade.setNumeroMaximoPeriodos(0);
					grade.setPeriodoInicio(1);
					listaGrade.add(grade);
				}

				Aluno aluno = alunos.buscarPorMatricula(alunoCurso.getMatricula());

				if ( aluno == null){
					aluno = new Aluno();
					aluno.setMatricula(alunoCurso.getMatricula());
					aluno.setNome(alunoCurso.getNome());
					aluno.setGrade(grade);
					aluno.setCurso(curso);
					grade.getGrupoAlunos().add(aluno);

				} 

				
				for (br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegra : alunoCurso.getDisciplinas().getDisciplina()) {
					Disciplina disciplina = disciplinas.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina());
					
					if (disciplina == null){
						disciplina = new Disciplina();
						disciplina.setCodigo(disciplinaIntegra.getDisciplina());
						disciplina.setCargaHoraria(Integer.parseInt(disciplinaIntegra.getHorasAula()));
						disciplinas.persistir(disciplina);
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
					aluno.getGrupoHistorico().add(historico);
				}
			}

			importador.gravarRegistros(listaGrade);
			
			

			List<Grade> listaGrades = curso.getGrupoGrades();
			for (Grade grade:listaGrades){
				estruturaArvore.removerEstrutura(grade);
			}


		} catch (NotAuthorizedException e) {
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			} 
		
		//Exception removida na nova versao da API do Integra
		/*catch (WsException_Exception e) {
			FacesMessage msg = new FacesMessage("Ocorreu um problema ao importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		}*/
		catch (Exception e) {
			FacesMessage msg = new FacesMessage("Ocorreu um problema ao importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
						
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