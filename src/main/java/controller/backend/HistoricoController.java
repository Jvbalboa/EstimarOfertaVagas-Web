package controller.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Aluno;
import model.Curso;
import model.Historico;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;

import org.primefaces.component.datatable.DataTable;

import controller.util.EstruturaArvore;
import controller.util.ImportarArvore;
import controller.util.UsuarioController;
import dao.Interface.CursoDAO;



@Named
@ViewScoped
public class HistoricoController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private boolean lgMatriculaAluno = false;
	private boolean lgNomeAluno  = false;
	private boolean lgAluno = true;
	private Aluno aluno = new Aluno();
	private List<Historico> listaHistorico = new ArrayList<Historico>();
	private List<Historico> listaHistoricoFiltrada ;
	private String classeEscolhida;
	private Float ira;
	private Integer periodo;
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private Curso curso = new Curso();
	private CursoDAO cursoDAO ;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		estruturaArvore = EstruturaArvore.getInstance();
		cursoDAO = estruturaArvore.getCursoDAO();
		usuarioController.atualizarPessoaLogada();
		
		if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")){	
			List<Curso> listaCurso = (List<Curso>) cursoDAO.recuperarTodos();	
			for (Curso cursoQuestao : listaCurso){
				for (Aluno alunoQuestao : cursoQuestao.getGrupoAlunos()){
					if(alunoQuestao.getMatricula().contains(usuarioController.getAutenticacao().getSelecaoIdentificador())){
						aluno = alunoQuestao;
						break;
					}
				}	
			}		
			if (aluno.getMatricula() == null){
				FacesMessage msg = new FacesMessage("Matr’cula n‹o cadastrada na base!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				lgMatriculaAluno = true;
				lgNomeAluno = true;	
				return;
			}			
			curso = aluno.getCurso();
			onItemSelectMatriculaAluno();
			lgAluno = false;
		}
		else{
			curso = usuarioController.getAutenticacao().getCursoSelecionado();
			if (curso.getGrupoAlunos().size() == 0){
				
				FacesMessage msg = new FacesMessage("Nenhum aluno cadastrado no curso!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
			
		}
	}	

	public List<String> alunoMatricula(String codigo) {		
		codigo = codigo.toUpperCase();
		List<String> todos = new ArrayList<String>();
		for (Aluno alunoQuestao : curso.getGrupoAlunos()){
			if(alunoQuestao.getMatricula().contains(codigo)){
				todos.add(alunoQuestao.getMatricula()); 
			}
		}
		return todos;
	}	

	public List<Aluno> alunoNome(String codigo) {		
		codigo = codigo.toUpperCase();
		List<Aluno> todos = new ArrayList<Aluno>();
		for (Aluno alunoQuestao : curso.getGrupoAlunos()){
			if(alunoQuestao.getNome().contains(codigo)){
				todos.add(alunoQuestao); 
			}
		}
		return todos;
	}

	public void onItemSelectMatriculaAluno() {		
		for (Aluno alunoQuestao : curso.getGrupoAlunos()){
			if(alunoQuestao.getMatricula().contains(aluno.getMatricula())){
				aluno = alunoQuestao;
				break;
			}
		}
		
		lgMatriculaAluno = true;
		lgNomeAluno = true;	
		listaHistorico = aluno.getGrupoHistorico();
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(),false);
		StudentsHistory sh = importador.getSh();
		Student st = sh.getStudents().get(aluno.getMatricula());
		
		if (st == null){
			
			FacesMessage msg = new FacesMessage("O aluno:" + aluno.getMatricula() + " n‹o tem nenhum hist—rico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
		}
		
		
		ira = st.getIRA();
		if(ira == -1) {
			ira = (float) 0;
		}
		periodo = aluno.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
	}	

	public void limpaAluno()  {
		aluno = new Aluno();
		lgMatriculaAluno = false;
		lgNomeAluno  = false;
		ira = (float) 0;
		periodo = 0;
		listaHistorico = new ArrayList<Historico>();		
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridHistorico");			
		dataTable.clearInitialState();		 
		dataTable.reset();
	}

	public int periodoCorrente(String ingresso){
		Calendar now = Calendar.getInstance();
		int anoAtual = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		int i = 0;
		int periodoAtual = 0;
		if(mes >= 1 && mes <= 6){
			periodoAtual = 1;
		}
		else {
			periodoAtual = 3;
		}
		int anoIngresso = Integer.parseInt(ingresso.substring(0, 4));
		int periodoIngresso = Integer.parseInt(ingresso.substring(4, 5));
		while( anoIngresso != anoAtual || periodoAtual != periodoIngresso  ){
			i++;
			if (periodoIngresso == 3){
				anoIngresso++;
				periodoIngresso = 1;
			}
			else{
				periodoIngresso = 3;
			}
		}
		return i;
	}

	//========================================================= GET - SET ==================================================================================//

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