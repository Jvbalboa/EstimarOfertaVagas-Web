package controller.backend;

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

import model.Aluno;
import model.Curso;
import model.EventoAce;
import model.Grade;
import model.Historico;
import model.arvore.Class;
import model.arvore.ClassStatus;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;
import model.estrutura.SituacaoDisciplina;

import org.primefaces.component.datatable.DataTable;

import controller.util.EstruturaArvore;
import controller.util.ImportarArvore;
import controller.util.Ordenar;
import controller.util.UsuarioController;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.EventoAceDAO;



@Named
@ViewScoped
public class AlunoSituacaoController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//


	private static final long serialVersionUID = 1L;

	private boolean lgNomeAluno  = false;
	private boolean lgMatriculaAluno = false;
	private boolean lgAce  = true;
	private boolean lgAluno = true;
	private Aluno aluno = new Aluno();
	private EventoAce eventosAce =  new EventoAce();
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private float ira;
	private String classeEscolhida;
	private int periodo;
	private int horasObrigatorias;
	private int horasAceConcluidas;	
	private int horasEletivasConcluidas;
	private int horasOpcionaisConcluidas;
	private int horasObrigatoriasConcluidas;
	private int percentualObrigatorias;
	private int percentualEletivas;
	private int percentualOpcionais;
	private int percentualAce;
	private int horasEletivas;
	private int horasOpcionais;
	private int horasACE;
	private AlunoDAO alunoDAO ;
	private DisciplinaDAO disciplinaDAO ;
	private EventoAceDAO eventosAceDAO ;
	private EventoAce eventoAceSelecionado;
	private List<EventoAce> listaEventosAceSelecionadas ;	
	private List<SituacaoDisciplina> listaDisciplinaEletivasSelecionadas;
	private List<SituacaoDisciplina> listaDisciplinaOpcionaisSelecionadas;
	private List<SituacaoDisciplina> listaDisciplinaObrigatoriasSelecionadas;
	private List<Historico> listaHistorico = new ArrayList<Historico>();
	private List<EventoAce> listaEventosAce = new ArrayList<EventoAce>();
	private List<SituacaoDisciplina> listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
	private List<SituacaoDisciplina> listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
	private List<SituacaoDisciplina> listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
	private CursoDAO cursoDAO ;


	private Curso curso = new Curso();

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	public void preencheSobraHoras()
	{
		if(this.aluno.getSobraHorasEletivas() > 0)
		{
			SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
			disciplinaSituacao.setCodigo("");
			disciplinaSituacao.setSituacao("");
			disciplinaSituacao.setCargaHoraria(this.aluno.getSobraHorasEletivas() + "");
			disciplinaSituacao.setNome("EXCEDENTE EM DISCIPLINAS ELETIVAS");
			listaDisciplinaOpcionais.add(disciplinaSituacao);
		}
		
	}
	
	@PostConstruct
	public void init()  {
		try {
			estruturaArvore = EstruturaArvore.getInstance();		
			Grade grade = new Grade();
			grade.setHorasEletivas(0);
			grade.setHorasOpcionais(0);
			grade.setHorasAce(0);
			aluno.setGrade(grade);
			disciplinaDAO = estruturaArvore.getDisciplinaDAO();
			cursoDAO = estruturaArvore.getCursoDAO();
			alunoDAO = estruturaArvore.getAlunoDAO();
			eventosAceDAO = estruturaArvore.getEventosAceDAO();	
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
				//curso = usuarioController.getAutenticacao().getCursoSelecionado().getCodigo();		
				curso = usuarioController.getAutenticacao().getCursoSelecionado();
				if (curso.getGrupoAlunos().size() == 0){

					FacesMessage msg = new FacesMessage("Nenhum aluno cadastrado no curso!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
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

	public void onItemSelectMatriculaAluno()  {



		for (Aluno alunoQuestao : curso.getGrupoAlunos()){
			if(alunoQuestao.getMatricula().contains(aluno.getMatricula())){
				aluno = alunoQuestao;
				break;
			}
		}
		lgAce = false;
		lgNomeAluno = true;	
		lgMatriculaAluno = true;
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(),true);

		/*if (importador.getResetarStance() == true){


			//importador.setResetarStance(false);
		   // usuarioController.atualizarPessoaLogada();


		}*/

		System.out.println(aluno.getMatricula());

		Grade gradeAluno = aluno.getGrade();
		horasEletivas = gradeAluno.getHorasEletivas();
		horasOpcionais = gradeAluno.getHorasOpcionais();
		horasACE = gradeAluno.getHorasAce();
		curriculum = importador.get_cur();
		StudentsHistory sh = importador.getSh();		
		Student st = sh.getStudents().get(aluno.getMatricula());

		if (st == null){

			FacesMessage msg = new FacesMessage("O aluno:" + aluno.getMatricula() + " n‹o tem nenhum hist—rico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}
		
		//listaEventosAce = new ArrayList<EventoAce>();

		listaEventosAce = new ArrayList<EventoAce>(aluno.getListaEventosAce());		
		if (listaEventosAce != null){
			for (EventoAce evento :listaEventosAce){
				horasAceConcluidas = (int) (horasAceConcluidas + evento.getHoras());
			}
		}
		else {
			listaEventosAce = new ArrayList<EventoAce>();
		}
		
		horasObrigatoriasConcluidas = aluno.getHorasObrigatoriasCompletadas();
		horasEletivasConcluidas = aluno.getHorasEletivasCompletadas();
		horasOpcionaisConcluidas = aluno.getHorasOpcionaisCompletadas();
		
		gerarDadosAluno(st,curriculum);
		this.preencheSobraHoras();
		
		ira = st.getIRA();
		if(ira == -1) {
			ira = 0;
		}
		periodo = aluno.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
		int SomaInt = 0;
		if (horasObrigatorias != 0){
			percentualObrigatorias =   (horasObrigatoriasConcluidas * 100 / horasObrigatorias);
		}
		SomaInt =  aluno.getGrade().getHorasEletivas();
		if (aluno.getGrade().getHorasEletivas() != 0){
			percentualEletivas = ((horasEletivasConcluidas* 100 / SomaInt));
		}	
		SomaInt = aluno.getGrade().getHorasOpcionais();
		if (aluno.getGrade().getHorasOpcionais() != 0  ){
			percentualOpcionais = ((horasOpcionaisConcluidas* 100 / SomaInt) );
		}
		
		
		if(this.aluno.getSobraHorasOpcionais() > 0)
		{
			horasAceConcluidas += this.aluno.getSobraHorasOpcionais();
			EventoAce evento = new EventoAce();
			evento.setDescricao("EXCEDENTE EM DISCIPLINAS OPCIONAIS");
			evento.setHoras((long)this.aluno.getSobraHorasOpcionais());
			//evento.setPeriodo(this.aluno.getPeriodoReal());
			evento.setExcluir(false);
			listaEventosAce.add(evento);
		}
		
		SomaInt = aluno.getGrade().getHorasAce();
		if (aluno.getGrade().getHorasAce() != 0){
			percentualAce = (horasAceConcluidas* 100 / SomaInt) ;
		}
	}

	public void limpaAluno(){		

		lgAce = true;
		lgNomeAluno  = false;
		lgMatriculaAluno = false;
		eventosAce =  new EventoAce();
		listaEventosAce = new ArrayList<EventoAce>();
		listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();	
		ira = 0;
		periodo = 0;
		horasObrigatorias = 0;
		horasAceConcluidas = 0;
		horasEletivasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasObrigatoriasConcluidas = 0;
		percentualObrigatorias = 0;
		percentualEletivas = 0;
		percentualOpcionais = 0;
		percentualAce = 0;
		Grade grade = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);
		grade.setHorasAce(0);
		aluno = new Aluno();
		aluno.setGrade(grade);	
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridObrigatorias");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridEletivas");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridOpcionais");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridAce");
		dataTable.clearInitialState();
		dataTable.reset();
	}

	public void gerarDadosAluno(Student st, Curriculum cur)	{
		HashMap<Class, ArrayList<String[]>> aprovado;
		listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
		horasObrigatorias = 0;
		//horasObrigatoriasConcluidas = 0;
		//horasOpcionaisConcluidas = 0;
		//horasEletivasConcluidas = 0;
		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED));
		TreeSet<String> naocompletado = new TreeSet<String>();		
		boolean lgPeriodoAtual = false;
		for(int i: cur.getMandatories().keySet()){
			for(Class c: cur.getMandatories().get(i)){
				horasObrigatorias = horasObrigatorias + c.getWorkload();
				if(!aprovado.containsKey(c)){					
					if (lgPeriodoAtual == false){						
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
					String preRequisito = "";
					for(Class cl: c.getPrerequisite()){
						preRequisito =  cl.getId() + " : " + preRequisito;
					}
					for(Class cl: c.getCorequisite()){
						preRequisito =  cl.getId() + " : " + preRequisito;
					}
					disciplinaSituacao.setListaPreRequisitos(preRequisito);
					listaDisciplinaObrigatorias.add(disciplinaSituacao);
				}
				else{
					//horasObrigatoriasConcluidas = horasObrigatoriasConcluidas + c.getWorkload();
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCodigo(c.getId());
					disciplinaSituacao.setSituacao("APROVADO");
					disciplinaSituacao.setPeriodo(Integer.toString(i));
					disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));					
					disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					String preRequisito = "";
					for(Class cl: c.getPrerequisite()){
						if (!preRequisito.contains(cl.getId())){
							preRequisito =  cl.getId() + " : " + preRequisito;
						}
					}
					disciplinaSituacao.setListaPreRequisitos(preRequisito);
					listaDisciplinaObrigatorias.add(disciplinaSituacao);
					aprovado.remove(c);
				}
			}	
		}
		//int creditos = 0;
		for(Class c: cur.getElectives()){
			if(aprovado.containsKey(c))	{
				SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
				disciplinaSituacao.setCodigo(c.getId());
				disciplinaSituacao.setSituacao("APROVADO");
				disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));
				disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
				listaDisciplinaEletivas.add(disciplinaSituacao);
				//creditos += c.getWorkload();
				aprovado.remove(c);
			} 	
		}

		//horasEletivasConcluidas = creditos;
		//creditos = 0;
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext()){
			Class c = i.next();
			ArrayList<String[]> classdata = aprovado.get(c);
			for(String[] s2: classdata)	{
				if (s2[1].equals("APR") || s2[1].equals("A")){
					EventoAce evento =  new EventoAce();
					horasAceConcluidas = horasAceConcluidas + c.getWorkload();
					evento.setDescricao(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					evento.setHoras((long) c.getWorkload());
					String periodo = s2[0];
					evento.setPeriodo(Integer.parseInt(periodo));
					evento.setExcluir(false);
					listaEventosAce.add(evento);
				}
				else{
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCodigo(c.getId());			
					disciplinaSituacao.setSituacao("APROVADO");
					disciplinaSituacao.setCargaHoraria(Integer.toString(c.getWorkload()));
					disciplinaSituacao.setNome(disciplinaDAO.buscarPorCodigoDisciplina(c.getId()).getNome());
					listaDisciplinaOpcionais.add(disciplinaSituacao);
					//creditos += c.getWorkload();
				}
			}
		}
		
		
		
		//horasOpcionaisConcluidas = creditos;
	}

	public void adicionarAce(){

		if (eventosAce.getPeriodo() == 0) {
			FacesMessage msg = new FacesMessage("Preencha o campo Per’odo!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		eventosAce.setDescricao(eventosAce.getDescricao().trim());
		if (eventosAce.getDescricao().isEmpty()) {
			FacesMessage msg = new FacesMessage("Preencha o campo Descri‹o!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		if (eventosAce.getHoras() == 0) {
			FacesMessage msg = new FacesMessage("Preencha o campo Carga Hor‡ria!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		eventosAce.setAluno(aluno);
		eventosAce.setDescricao(eventosAce.getDescricao().toUpperCase());

		System.out.println(eventosAce.getDescricao().toUpperCase() + ";" +  eventosAce.getHoras() + ";" + eventosAce.getPeriodo());

		eventosAce.setExcluir(true);



		eventosAceDAO.persistir(eventosAce);
		aluno.getListaEventosAce().add(eventosAce);
		listaEventosAce.add(eventosAce);
		
		Ordenar ordenar = new Ordenar();
		ordenar.EventoAceOrdenarPeriodo(listaEventosAce);
		horasAceConcluidas = (int) (horasAceConcluidas + eventosAce.getHoras());
		if (aluno.getGrade().getHorasAce() != 0){
			percentualAce = (horasAceConcluidas* 100 / aluno.getGrade().getHorasAce()) ;
		}		
		eventosAce = new EventoAce();
	}

	public void limparAce(){
		eventosAce =  new EventoAce();
	}

	public void deletarAce(){
		eventosAceDAO.removePeloId(eventoAceSelecionado.getId());
		horasAceConcluidas = (int) (horasAceConcluidas - eventoAceSelecionado.getHoras());
		if (aluno.getGrade().getHorasAce() != 0){
			percentualAce = (horasAceConcluidas* 100 / aluno.getGrade().getHorasAce()) ;
		}		
		listaEventosAce.remove(eventoAceSelecionado);
		aluno.getListaEventosAce().remove(eventoAceSelecionado);
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

	public AlunoDAO getAlunoDAO() {
		return alunoDAO;
	}

	public void setAlunoDAO(AlunoDAO alunoDAO) {
		this.alunoDAO = alunoDAO;
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

	public void setListaDisciplinaObrigatorias(
			List<SituacaoDisciplina> listaDisciplinaObrigatorias) {
		this.listaDisciplinaObrigatorias = listaDisciplinaObrigatorias;
	}

	public List<SituacaoDisciplina> getListaDisciplinaEletivas() {
		return listaDisciplinaEletivas;
	}

	public void setListaDisciplinaEletivas(
			List<SituacaoDisciplina> listaDisciplinaEletivas) {
		this.listaDisciplinaEletivas = listaDisciplinaEletivas;
	}

	public List<SituacaoDisciplina> getListaDisciplinaOpcionais() {
		return listaDisciplinaOpcionais;
	}

	public void setListaDisciplinaOpcionais(
			List<SituacaoDisciplina> listaDisciplinaOpcionais) {
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

	public DisciplinaDAO getDisciplinaDAO() {
		return disciplinaDAO;
	}

	public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
		this.disciplinaDAO = disciplinaDAO;
	}

	public List<SituacaoDisciplina> getListaDisciplinaObrigatoriasSelecionadas() {
		return listaDisciplinaObrigatoriasSelecionadas;
	}

	public void setListaDisciplinaObrigatoriasSelecionadas(
			List<SituacaoDisciplina> listaDisciplinaObrigatoriasSelecionadas) {
		this.listaDisciplinaObrigatoriasSelecionadas = listaDisciplinaObrigatoriasSelecionadas;
	}

	public List<SituacaoDisciplina> getListaDisciplinaEletivasSelecionadas() {
		return listaDisciplinaEletivasSelecionadas;
	}

	public void setListaDisciplinaEletivasSelecionadas(
			List<SituacaoDisciplina> listaDisciplinaEletivasSelecionadas) {
		this.listaDisciplinaEletivasSelecionadas = listaDisciplinaEletivasSelecionadas;
	}

	public List<SituacaoDisciplina> getListaDisciplinaOpcionaisSelecionadas() {
		return listaDisciplinaOpcionaisSelecionadas;
	}

	public void setListaDisciplinaOpcionaisSelecionadas(
			List<SituacaoDisciplina> listaDisciplinaOpcionaisSelecionadas) {
		this.listaDisciplinaOpcionaisSelecionadas = listaDisciplinaOpcionaisSelecionadas;
	}

	public List<EventoAce> getListaEventosAce() {
		return this.listaEventosAce;
	}

	public void setListaEventosAce(List<EventoAce> listaEventosAce) {
		this.listaEventosAce = listaEventosAce;
	}

	public List<EventoAce> getListaEventosAceSelecionadas() {
		return listaEventosAceSelecionadas;
	}

	public void setListaEventosAceSelecionadas(
			List<EventoAce> listaEventosAceSelecionadas) {
		this.listaEventosAceSelecionadas = listaEventosAceSelecionadas;
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

	public EventoAceDAO getEventosAceDAO() {
		return eventosAceDAO;
	}

	public void setEventosAceDAO(EventoAceDAO eventosAceDAO) {
		this.eventosAceDAO = eventosAceDAO;
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

	public float getPercentualObrigatorias() {
		return percentualObrigatorias;
	}

	public int getPercentualEletivas() {
		return percentualEletivas;
	}

	public void setPercentualEletivas(int percentualEletivas) {
		this.percentualEletivas = percentualEletivas;
	}

	public int getPercentualOpcionais() {
		return percentualOpcionais;
	}

	public void setPercentualOpcionais(int percentualOpcionais) {
		this.percentualOpcionais = percentualOpcionais;
	}

	public int getPercentualAce() {
		return percentualAce;
	}

	public void setPercentualAce(int percentualAce) {
		this.percentualAce = percentualAce;
	}

	public void setPercentualObrigatorias(int percentualObrigatorias) {
		this.percentualObrigatorias = percentualObrigatorias;
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

	public int getHorasEletivas() {
		return horasEletivas;
	}

	public void setHorasEletivas(int horasEletivas) {
		this.horasEletivas = horasEletivas;
	}

	public int getHorasOpcionais() {
		return horasOpcionais;
	}

	public void setHorasOpcionais(int horasOpcionais) {
		this.horasOpcionais = horasOpcionais;
	}

	public int getHorasACE() {
		return horasACE;
	}

	public void setHorasACE(int horasACE) {
		this.horasACE = horasACE;
	}
}
