package controller.util;

import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import model.Grade;
import model.Historico;
import dao.AlunoDAOImpl;
import dao.CursoDAOImpl;
import dao.DisciplinaDAOimpl;
import dao.EquivalenciaDAOImpl;
import dao.EventoAceDAOImpl;
import dao.GradeDAOImpl;
import dao.GradeDisciplinaDAOImpl;
import dao.HistoricoDAOImpl;
import dao.PessoaCursoDAOImpl;
import dao.PessoaDAOImpl;
import dao.PreRequisitoDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.EquivalenciaDAO;
import dao.Interface.EventoAceDAO;
import dao.Interface.GradeDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.HistoricoDAO;
import dao.Interface.PessoaCursoDAO;
import dao.Interface.PessoaDAO;
import dao.Interface.PreRequisitoDAO;

public class EstruturaArvore {

	private static EstruturaArvore instancia;
	private AlunoDAO alunoDAO = new AlunoDAOImpl();	
	private GradeDAO gradeDAO = new GradeDAOImpl();
	private HistoricoDAO historicoDAO = new HistoricoDAOImpl();
	private EventoAceDAO eventosAceDAO = new EventoAceDAOImpl();
	private DisciplinaDAO disciplinaDAO = new DisciplinaDAOimpl();
	private PreRequisitoDAO preRequisitoDAO = new PreRequisitoDAOImpl();
	private EquivalenciaDAO equivalenciaDAO = new EquivalenciaDAOImpl();
	private GradeDisciplinaDAO gradeDisciplinaDAO = new GradeDisciplinaDAOImpl();
	private PessoaDAO pessoaDAO = new PessoaDAOImpl();
	private CursoDAO cursoDAO = new CursoDAOImpl();	
	private PessoaCursoDAO pessoaCursoDAO = new PessoaCursoDAOImpl();
	private String loginUtilizado;
	private List<ImportarArvore> todasArvores = new ArrayList<ImportarArvore>();

	public static synchronized EstruturaArvore getInstance(){

		if (instancia == null){			
			instancia = new EstruturaArvore();
		}
		return instancia;
	}

	public void resetStanciaPessoa(){
		pessoaCursoDAO.resetDAO();
		pessoaDAO.resetDAO();
	}

	public void resetarCursoDAO(){		

		cursoDAO = new CursoDAOImpl();
		//gradeDAO = new GradeDAOImpl();
		//gradeDisciplinaDAO = new GradeDisciplinaDAOImpl();	

	}

	public void resetarPessoaDAO(){		

		pessoaDAO = new PessoaDAOImpl();
		//gradeDAO = new GradeDAOImpl();
		//gradeDisciplinaDAO = new GradeDisciplinaDAOImpl();	

	}



	public ImportarArvore recuperarArvoreSemProcessar(Grade grade){
		for(ImportarArvore importarArvore:todasArvores){
			if(importarArvore.getGrade().getId() == grade.getId()){
				return importarArvore;
			}
		}
		return null;
	}

	public ImportarArvore recuperarArvore (Grade grade,boolean consideraCo){	






		for(ImportarArvore importarArvore:todasArvores){
			if(importarArvore.getGrade().getId() == grade.getId()){
				importarArvore.importarDisciplinas(grade,consideraCo);
				return importarArvore;
			}
		}	



		ImportarArvore importador = new ImportarArvore(); 
		importador.setGrade(grade);

		/*if (reseta == true){
			grade = gradeDAO.recuperarPorId(grade.getId());
			reseta = false;
			//importador.setResetarStance(true);
			gradeDAO = new GradeDAOImpl();

		}*/

		todasArvores.add(importador);
		importador.importarDisciplinas(grade,consideraCo);

		List<Aluno> listaAluno = grade.getGrupoAlunos();

		//List<Aluno> listaAluno = alunoDAO.buscarTodosAlunoCursoGradeObjeto(grade.getCurso().getId(), grade.getId());
		for(Aluno aluno:listaAluno){
			List<Historico> listaHistorico = aluno.getGrupoHistorico();
			importador.importarHistorico(listaHistorico);
		}
		return importador;
	}

	public void removerEstrutura (Grade grade){

		

		for(ImportarArvore importarArvore:todasArvores){
			if(importarArvore.getGrade().getId() == grade.getId()){
				todasArvores.remove(importarArvore);
				return;
			}
		}
	}

	public static EstruturaArvore getInstancia() {
		return instancia;
	}

	public static void setInstancia(EstruturaArvore instancia) {
		EstruturaArvore.instancia = instancia;
	}

	public AlunoDAO getAlunoDAO() {
		return alunoDAO;
	}

	public void setAlunoDAO(AlunoDAO alunoDAO) {
		this.alunoDAO = alunoDAO;
	}

	public GradeDAO getGradeDAO() {
		return gradeDAO;
	}

	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	public HistoricoDAO getHistoricoDAO() {
		return historicoDAO;
	}

	public void setHistoricoDAO(HistoricoDAO historicoDAO) {
		this.historicoDAO = historicoDAO;
	}

	public DisciplinaDAO getDisciplinaDAO() {
		return disciplinaDAO;
	}

	public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
		this.disciplinaDAO = disciplinaDAO;
	}

	public GradeDisciplinaDAO getGradeDisciplinaDAO() {
		return gradeDisciplinaDAO;
	}

	public void setGradeDisciplinaDAO(GradeDisciplinaDAO gradeDisciplinaDAO) {
		this.gradeDisciplinaDAO = gradeDisciplinaDAO;
	}

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}

	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}

	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
	}

	public PessoaCursoDAO getPessoaCursoDAO() {
		return pessoaCursoDAO;
	}

	public void setPessoaCursoDAO(PessoaCursoDAO pessoaCursoDAO) {
		this.pessoaCursoDAO = pessoaCursoDAO;
	}

	public String getLoginUtilizado() {
		return loginUtilizado;
	}

	public void setLoginUtilizado(String loginUtilizado) {
		this.loginUtilizado = loginUtilizado;
	}

	public List<ImportarArvore> getTodasArvores() {
		return todasArvores;
	}

	public void setTodasArvores(List<ImportarArvore> todasArvores) {
		this.todasArvores = todasArvores;
	}

	public EventoAceDAO getEventosAceDAO() {
		return eventosAceDAO;
	}

	public void setEventosAceDAO(EventoAceDAO eventosAceDAO) {
		this.eventosAceDAO = eventosAceDAO;
	}

	public PreRequisitoDAO getPreRequisitoDAO() {
		return preRequisitoDAO;
	}

	public void setPreRequisitoDAO(PreRequisitoDAO preRequisitoDAO) {
		this.preRequisitoDAO = preRequisitoDAO;
	}

	public EquivalenciaDAO getEquivalenciaDAO() {
		return equivalenciaDAO;
	}

	public void setEquivalenciaDAO(EquivalenciaDAO equivalenciaDAO) {
		this.equivalenciaDAO = equivalenciaDAO;
	}


}
