/*package controller.backend;

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

import model.Aluno;
import model.Curso;
import model.Disciplina;
import model.Grade;
import model.Historico;
import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao;
import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao.ServiceVersion;
import br.ufjf.ice.integra3.rs.restclient.model.v2.AlunoCurso;
import br.ufjf.ice.integra3.rs.restclient.model.v2.CursoAlunosSituacaoResponse;
import br.ufjf.ice.integra3.ws.login.interfaces.IWsLogin;
import br.ufjf.ice.integra3.ws.login.interfaces.WsException_Exception;
import br.ufjf.ice.integra3.ws.login.interfaces.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.service.WSLogin;
import controller.util.EstruturaArvore;
import controller.util.UsuarioController;
import dao.ImportaDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;

@Named
@ViewScoped
public class ImportarHistorico implements Serializable{

	private Curso curso = new Curso();

	@Inject
	private UsuarioController usuarioController;
	private static final long serialVersionUID = 1L;
	private Date d ;
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private CursoDAO cursoDAO ;

	@PostConstruct
	public void init() throws IOException {
		
		
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		usuarioController.setReseta(true);

	}

	public void chamarTudo()  {
		
		
		
		


		
		
		if (usuarioController.getAutenticacao().getTipoAcesso().equals("externo")){
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		try {
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			//WsLoginResponse user = integra.login("***REMOVED***", "***REMOVED***","***REMOVED***");
			WsLoginResponse user = integra.login(usuarioController.getAutenticacao().getLogin(), usuarioController.getAutenticacao().getSenha(), usuarioController.getAutenticacao().getToken());
			System.out.println("Recuperando dados do curso "+ curso.getCodigo() +"...");
			RSCursoAlunosDiscSituacao rsClient = new RSCursoAlunosDiscSituacao(user.getToken(), ServiceVersion.V2);
			EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();
			
			cursoDAO = estruturaArvore.getCursoDAO();
			d = new Date();
			String data = format.format(d).toString();
			curso.setDataAtualizacao(data);
			cursoDAO.editar(curso);
			
			
			
			AlunoDAO alunoDAO = estruturaArvore.getAlunoDAO();
			DisciplinaDAO disciplinaDAO = estruturaArvore.getDisciplinaDAO();
			ImportaDAOImpl importaDAO = new ImportaDAOImpl();
			if (curso == null){
				FacesMessage msg = new FacesMessage("Curso Inválido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			else {

				for (Grade grade:curso.getGrupoGrades()){
					for (Aluno alunoQuestao : grade.getGrupoAlunos()){
						alunoDAO.removePeloId(alunoQuestao.getId());
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

			/*List<AlunoCurso> listaAlunoCurso = new ArrayList<AlunoCurso>();
			AlunoCurso alunoCursoNovo;
			br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegraNovo;
			List<br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina> listaDisciplinaIntegraNovo;
			AlunoDisciplina disciplinaList;
			alunoCursoNovo = new AlunoCurso();			
			alunoCursoNovo.setCurriculo("12009");
			alunoCursoNovo.setCurso("35A");
			alunoCursoNovo.setMatricula("209195001");
			alunoCursoNovo.setNome("JOAO PEDRO");			
			disciplinaIntegraNovo = new br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina();			
			disciplinaIntegraNovo.setAnoSemestre("20091");
			disciplinaIntegraNovo.setDisciplina("DCC120");
			disciplinaIntegraNovo.setHorasAula("60");
			disciplinaIntegraNovo.setNota("60");
			disciplinaIntegraNovo.setSituacao("Aprovado");			
			listaDisciplinaIntegraNovo = new ArrayList<br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina>();
			listaDisciplinaIntegraNovo.add(disciplinaIntegraNovo);
			disciplinaList = new AlunoDisciplina();
			disciplinaList.setDisciplina(listaDisciplinaIntegraNovo);			
			alunoCursoNovo.setDisciplinas(disciplinaList);
			listaAlunoCurso.add(alunoCursoNovo);
			alunoCursoNovo = new AlunoCurso();			
			alunoCursoNovo.setCurriculo("12009");
			alunoCursoNovo.setCurso("35A");
			alunoCursoNovo.setMatricula("208195001");
			alunoCursoNovo.setNome("JOAO PEDRO");			
			disciplinaIntegraNovo = new br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina();			
			disciplinaIntegraNovo.setAnoSemestre("20091");
			disciplinaIntegraNovo.setDisciplina("DCC120");
			disciplinaIntegraNovo.setHorasAula("60");
			disciplinaIntegraNovo.setNota("60");
			disciplinaIntegraNovo.setSituacao("Aprovado");			
			listaDisciplinaIntegraNovo = new ArrayList<br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina>();
			listaDisciplinaIntegraNovo.add(disciplinaIntegraNovo);
			disciplinaList = new AlunoDisciplina();
			disciplinaList.setDisciplina(listaDisciplinaIntegraNovo);			
			alunoCursoNovo.setDisciplinas(disciplinaList);	
			listaAlunoCurso.add(alunoCursoNovo);*/
/*
			listaGrade.addAll(curso.getGrupoGrades());
			
			System.out.println(curso.getCodigo());

			for(AlunoCurso alunoCurso : rsResponse.getAluno()) {

				contador ++;
				System.out.println(String.valueOf(contador) + "/" + String.valueOf(total));
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

				Aluno aluno = null;

				for (Aluno alunoQuestao : grade.getGrupoAlunos()){
					if(alunoQuestao.getMatricula().equals(alunoCurso.getMatricula())){
						aluno = alunoQuestao;
						break;
					}
				}

				if ( aluno == null){
					aluno = new Aluno();
					aluno.setMatricula(alunoCurso.getMatricula());
					aluno.setNome(alunoCurso.getNome());
					aluno.setGrade(grade);
					aluno.setCurso(curso);
					grade.getGrupoAlunos().add(aluno);

				} 

				
				for (br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegra : alunoCurso.getDisciplinas().getDisciplina()) {
					Disciplina disciplina = new Disciplina();
					
					if ( disciplinaDAO.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina()) == null){
						disciplina.setCodigo(disciplinaIntegra.getDisciplina());
						disciplina.setCargaHoraria(Integer.parseInt(disciplinaIntegra.getHorasAula()));
						disciplinaDAO.persistir(disciplina);
					}
					else {
						disciplina = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina());
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

			importaDAO.gravarRegistros(listaGrade);
			
			

			List<Grade> listaGrades = curso.getGrupoGrades();
			for (Grade grade:listaGrades){
				estruturaArvore.removerEstrutura(grade);
			}


		} catch (NotAuthorizedException e) {
			FacesMessage msg = new FacesMessage("Voce não tem permissão para importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			} catch (WsException_Exception e) {
			FacesMessage msg = new FacesMessage("Ocorreu um problema ao importartar dados!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		} catch (Exception e) {
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
}*/