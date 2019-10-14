package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.CalculadorMateriasExcedentes;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.Ordenar;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.EventoAce;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.Historico;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.SituacaoDisciplina;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.EventoAceRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.GradeRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.jpa.Transactional;

@Named
@ViewScoped
public class SimulacaoGradeController implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Inject
  private DisciplinaRepository disciplinas;
  
  @Inject
  private EventoAceRepository eventosAceRepository;
  
  @Inject
  private EventoAce eventoAceSelecionado;
  
  @Inject
  private AlunoRepository alunos;
  
  @Inject
  private GradeRepository grades;
  
  @Inject
  private UsuarioController usuarioController;
  private boolean lgNomeAluno = false;
  private boolean lgGradeAluno = false;
  private boolean lgMatriculaAluno = false;
  private boolean lgAce = true;
  private boolean lgAluno = true;
  private Aluno aluno = new Aluno();
  private EventoAce eventosAce = new EventoAce();
  
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
  
  private Grade gradeSimulada;
  
  
  private List<EventoAce> listaEventosAceSelecionadas;
  private List<SituacaoDisciplina> listaDisciplinaEletivasSelecionadas;
  private List<SituacaoDisciplina> listaDisciplinaOpcionaisSelecionadas;
  private List<SituacaoDisciplina> listaDisciplinaObrigatoriasSelecionadas;
  private List<Historico> listaHistorico = new ArrayList<Historico>();
  private List<EventoAce> listaEventosAce = new ArrayList<EventoAce>();
  private List<SituacaoDisciplina> listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
  private List<SituacaoDisciplina> listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
  private List<SituacaoDisciplina> listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();

  private Curso curso = new Curso();
  private static final Logger logger = Logger.getLogger(AlunoSituacaoController.class);
  
	@PostConstruct
	public void init()  {
		try {
			
			
			estruturaArvore = EstruturaArvore.getInstance();		
			Grade grade = new Grade();
			grade.setHorasEletivas(0);
			grade.setHorasOpcionais(0);
			grade.setHorasAce(0);
			aluno.setGrade(grade);

			usuarioController.atualizarPessoaLogada();

			if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")){	
				aluno = alunos.buscarPorMatricula(usuarioController.getAutenticacao().getSelecaoIdentificador());
				lgMatriculaAluno = true;
				lgNomeAluno = true;
				lgGradeAluno = false;
				lgAluno = false;
				
				if (aluno == null || aluno.getMatricula() == null){
					FacesMessage msg = new FacesMessage("Matrícula não cadastrada na base!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return;
				}			
				curso = aluno.getCurso();
				onItemSelectMatriculaAluno();
				
			}
			else{	
				curso = usuarioController.getAutenticacao().getCursoSelecionado();
				if (curso.getGrupoAlunos().size() == 0){

					FacesMessage msg = new FacesMessage("Nenhum aluno cadastrado no curso!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception SimulacaoGradeController.init()");
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
	
	public List<String> codigoGrades(String codigo) {
		
		codigo = codigo.toUpperCase();
		List<String> todos = new ArrayList<String>();
		for (Grade grade : curso.getGrupoGrades()) {
			if(grade.getCodigo().contains(codigo) && Integer.valueOf(grade.getCodigo()) >= Integer.valueOf(aluno.getGrade().getCodigo()))
				todos.add(grade.getCodigo());
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
		this.gradeSimulada = null;
		for (Aluno alunoQuestao : curso.getGrupoAlunos()){
			if(alunoQuestao.getMatricula().contains(aluno.getMatricula())){
				aluno = alunoQuestao;
				break;
			}
		}
		this.gradeSimulada = aluno.getGrade();
		this.calculaSituacaoAluno();
	}
	
	public void onItemSelectGradeSimular() {		
		EstruturaArvore estruturaArvoreOriginal = new EstruturaArvore();
		ImportarArvore importadorGradeOriginal = new ImportarArvore();

		importadorGradeOriginal = estruturaArvoreOriginal.recuperarArvore(this.aluno.getGrade(),true);
		StudentsHistory studentHistory = importadorGradeOriginal.getSh();
		Student student = studentHistory.getStudents().get(aluno.getMatricula());
		
		/*
		//busca entre todas as grades, e através do código da grade, a grade que deve ser simulada
		 * 	 .isso é devido ao fato de, quando o usuário escolhe o codigo da grade a ser simulada, ele na verdade está apenas mudando o codigo
		 * 	da gradeSimulada, deve-se trocar todo o objeto grade de acordo com o codigo informado. 
		 */
		for(Grade grade : grades.listarTodos()) {
			if(grade.getCodigo().equals(this.gradeSimulada.getCodigo())) {
				this.gradeSimulada = grade;
				break;
			}
		}
		
		ImportarArvore importadorGradeAlternativa = new ImportarArvore();
		EstruturaArvore estruturaArvoreAlternativa = new EstruturaArvore();  //importador da grade alternativa do aluno
		importadorGradeAlternativa = estruturaArvoreAlternativa.recuperarArvore(this.gradeSimulada,true);
		Curriculum curriculumGradeAlternativa = new Curriculum();
		curriculumGradeAlternativa = importadorGradeAlternativa.get_cur();
		
		horasEletivas = this.gradeSimulada.getHorasEletivas();
		horasOpcionais = this.gradeSimulada.getHorasOpcionais();
		horasACE = this.gradeSimulada.getHorasAce();
		
		horasObrigatoriasConcluidas = 0;
		horasEletivasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasAceConcluidas = 0;
		
		gerarDadosAluno(student, curriculumGradeAlternativa);
		
		if (horasEletivasConcluidas > this.gradeSimulada.getHorasEletivas()) {
			List<SituacaoDisciplina> disciplinaSituacao = CalculadorMateriasExcedentes.getExcedentesEletivas(this.gradeSimulada.getHorasEletivas(), this.listaDisciplinaEletivas);
			for(SituacaoDisciplina eletivaExtra : disciplinaSituacao) {
				horasOpcionaisConcluidas += Integer.valueOf(eletivaExtra.getCargaHoraria());
				horasEletivasConcluidas -= Integer.valueOf(eletivaExtra.getCargaHoraria());
				listaDisciplinaOpcionais.add(eletivaExtra);
				listaDisciplinaEletivas.remove(eletivaExtra);
			}
		}
		
		if(horasOpcionaisConcluidas > this.gradeSimulada.getHorasOpcionais())
		{
			List<EventoAce> ExcedentesOpcionais = CalculadorMateriasExcedentes.getExcedentesOpcionais(this.gradeSimulada.getHorasOpcionais(), this.listaDisciplinaOpcionais);
			for(EventoAce eventoAceExtra : ExcedentesOpcionais) {
				horasAceConcluidas += eventoAceExtra.getHoras();
				horasOpcionaisConcluidas -= eventoAceExtra.getHoras();
				listaEventosAce.add(eventoAceExtra);
				for(SituacaoDisciplina d : listaDisciplinaOpcionais) {
					if(d.getCodigo().equals(eventoAceExtra.getMatricula())){
						listaDisciplinaOpcionais.remove(d);
						break;
					}
				}
			}
		}

		if (horasObrigatorias != 0){
			percentualObrigatorias = (horasObrigatoriasConcluidas * 100 / horasObrigatorias);
		}
		if (horasEletivas != 0){
			percentualEletivas = (horasEletivasConcluidas* 100 / horasEletivas);
		}
		if (horasOpcionais != 0) {
			percentualOpcionais = (horasOpcionaisConcluidas * 100 / horasOpcionais);
		}
		
		this.resetaDataTables();
	}
	
	public void gerarDadosAluno(Student historicoAluno, Curriculum curriculumGrade) {
		EstruturaArvore estruturaArvoreOriginal = new EstruturaArvore();
		ImportarArvore importadorGradeOriginal = new ImportarArvore();
		//importador da grade original do aluno
		importadorGradeOriginal = estruturaArvoreOriginal.recuperarArvore(this.aluno.getGrade(),true);

		StudentsHistory studentHistory = importadorGradeOriginal.getSh();
		Student student = studentHistory.getStudents().get(aluno.getMatricula());
		HashMap<Class, ArrayList<String[]>> disciplinasAprovadas; //HashMap das disciplinas em que o aluno está aprovado
		disciplinasAprovadas = new HashMap<Class, ArrayList<String[]>>(student.getClasses(ClassStatus.APPROVED));
		
		listaDisciplinaObrigatorias = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaEletivas = new ArrayList<SituacaoDisciplina>();
		listaDisciplinaOpcionais = new ArrayList<SituacaoDisciplina>();
		listaEventosAce = new ArrayList<EventoAce>();
		
		horasObrigatorias = 0;
		
		for(int i : curriculumGrade.getMandatories().keySet()) {
			for(Class disciplinaGrade : curriculumGrade.getMandatories().get(i)) {
				boolean isAprovadoNaDisciplina = false;
				Class disciplinaAprovada = null; //irá guardar a disciplina que o aluno foi aprovado
				horasObrigatorias = horasObrigatorias + disciplinaGrade.getWorkload();
				//vendo se o aluno foi aprovado na disciplina
				for(Class disciplina : disciplinasAprovadas.keySet()) {
					if(disciplina.getId().equals(disciplinaGrade.getId())) {
						isAprovadoNaDisciplina = true;
						disciplinaAprovada = disciplina;
					}
				}
				
				if(!isAprovadoNaDisciplina){									
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setSituacao("NAO APROVADO");
					disciplinaSituacao.setCodigo(disciplinaGrade.getId());
					disciplinaSituacao.setPeriodo(Integer.toString(i));
					disciplinaSituacao.setCargaHoraria(Integer.toString(disciplinaGrade.getWorkload()));					
					disciplinaSituacao.setNome(disciplinas.buscarPorCodigoDisciplina(disciplinaGrade.getId()).getNome());
					String preRequisito = "";
					for(Class cl: disciplinaGrade.getPrerequisite()){
						preRequisito =  cl.getId() + " : " + preRequisito;
					}
					for(Class cl: disciplinaGrade.getCorequisite()){
						preRequisito =  cl.getId() + " : " + preRequisito;
					}
					disciplinaSituacao.setListaPreRequisitos(preRequisito);
					listaDisciplinaObrigatorias.add(disciplinaSituacao);
				}
				else{
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCodigo(disciplinaGrade.getId());
					disciplinaSituacao.setSituacao("APROVADO");
					disciplinaSituacao.setPeriodo(Integer.toString(i));
					disciplinaSituacao.setCargaHoraria(Integer.toString(disciplinaGrade.getWorkload()));					
					disciplinaSituacao.setNome(disciplinas.buscarPorCodigoDisciplina(disciplinaGrade.getId()).getNome());
					String preRequisito = "";
					for(Class cl: disciplinaGrade.getPrerequisite()){
						if (!preRequisito.contains(cl.getId())){
							preRequisito =  cl.getId() + " : " + preRequisito;
						}
					}
					disciplinaSituacao.setListaPreRequisitos(preRequisito);
					listaDisciplinaObrigatorias.add(disciplinaSituacao);
					horasObrigatoriasConcluidas += Integer.valueOf(disciplinaSituacao.getCargaHoraria());
					disciplinasAprovadas.remove(disciplinaAprovada);
				}
				
			}
		}
		
		for(Class disciplinaEletiva: curriculumGrade.getElectives()){
			boolean isAprovadoNaDisciplina = false;
			Class aprovada = null;
			for(Class classe : disciplinasAprovadas.keySet()) {
				if(classe.getId().equals(disciplinaEletiva.getId())) {
					isAprovadoNaDisciplina = true;
					aprovada = classe;
				}
			}
			if(isAprovadoNaDisciplina)	{
				SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
				disciplinaSituacao.setCodigo(disciplinaEletiva.getId());
				disciplinaSituacao.setSituacao("APROVADO");
				logger.info(disciplinaEletiva.getId() + " eletiva");
				disciplinaSituacao.setCargaHoraria(Integer.toString(disciplinaEletiva.getWorkload()));
				disciplinaSituacao.setNome(disciplinas.buscarPorCodigoDisciplina(disciplinaEletiva.getId()).getNome());
				listaDisciplinaEletivas.add(disciplinaSituacao);
				horasEletivasConcluidas += Integer.valueOf(disciplinaSituacao.getCargaHoraria());
				disciplinasAprovadas.remove(aprovada);
			} 	
		}
		
		Set<Class> ap = disciplinasAprovadas.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext()){
			Class c = i.next();
			ArrayList<String[]> classdata = disciplinasAprovadas.get(c);
			for(String[] s2: classdata)	{
				if (s2[1].equals("APR") || s2[1].equals("A")){
					EventoAce evento =  new EventoAce();
					horasAceConcluidas = horasAceConcluidas + c.getWorkload();
					evento.setDescricao(disciplinas.buscarPorCodigoDisciplina(c.getId()).getNome());
					
					Disciplina d = disciplinas.buscarPorCodigoDisciplina(c.getId());
					
					if(d != null)
						c.setWorkload(d.getCargaHoraria());
					
					evento.setHoras((long) c.getWorkload());
					String periodo = s2[0];
					evento.setPeriodo(Integer.parseInt(periodo));
					evento.setExcluir(false);
					listaEventosAce.add(evento);
				}
				else{
					Disciplina opcional = disciplinas.buscarPorCodigoDisciplina(c.getId());
					SituacaoDisciplina disciplinaSituacao = new SituacaoDisciplina();
					disciplinaSituacao.setCodigo(c.getId());			
					disciplinaSituacao.setSituacao("APROVADO");
					disciplinaSituacao.setCargaHoraria(opcional.getCargaHoraria().toString());
					disciplinaSituacao.setNome(opcional.getNome());
					horasOpcionaisConcluidas += Integer.valueOf(disciplinaSituacao.getCargaHoraria());
					listaDisciplinaOpcionais.add(disciplinaSituacao);
					//creditos += c.getWorkload();
				}
			}
		}
	}
	
	
	
	public void calculaSituacaoAluno() {
		lgAce = false;
		lgNomeAluno = true;	
		lgGradeAluno = false;
		lgMatriculaAluno = true;
		//grade_aluno
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(),true);

		logger.info("Aluno: " + aluno.getMatricula());

		Grade gradeAluno = aluno.getGrade();
		
		horasEletivas = gradeAluno.getHorasEletivas();
		horasOpcionais = gradeAluno.getHorasOpcionais();
		horasACE = gradeAluno.getHorasAce();
		
		aluno.setDisciplinaRepository(disciplinas);
		aluno.setEventoAceRepository(eventosAceRepository);
		
		curriculum = importador.get_cur();
		StudentsHistory sh = importador.getSh();		
		Student st = sh.getStudents().get(aluno.getMatricula());

		if (st == null){

			FacesMessage msg = new FacesMessage("O aluno:" + aluno.getMatricula() + " não tem nenhum histórico de matrícula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}

		listaEventosAce = new ArrayList<EventoAce>(eventosAceRepository.buscarPorMatricula(aluno.getMatricula()));
		if (listaEventosAce != null){
			horasAceConcluidas = eventosAceRepository.recuperarHorasConcluidasPorMatricula(aluno.getMatricula());
		}
		else {
			listaEventosAce = new ArrayList<EventoAce>();
		}
		
		gerarDadosAluno(st, curriculum);
		
		if (this.aluno.getSobraHorasEletivas() > 0) {
			List<SituacaoDisciplina> disciplinaSituacao = CalculadorMateriasExcedentes.getExcedentesEletivas(this.aluno.getGrade().getHorasEletivas(), this.listaDisciplinaEletivas);
			for(SituacaoDisciplina eletivaExtra : disciplinaSituacao) {
				listaDisciplinaOpcionais.add(eletivaExtra);
				listaDisciplinaEletivas.remove(eletivaExtra);
			}
		}
		
		ira = aluno.getIra();
		
		
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
			List<EventoAce> ExcedentesOpcionais = CalculadorMateriasExcedentes.getExcedentesOpcionais(this.aluno.getGrade().getHorasOpcionais(), this.listaDisciplinaOpcionais);
			for(EventoAce eventoAceExtra : ExcedentesOpcionais) {
				listaEventosAce.add(eventoAceExtra);
				for(SituacaoDisciplina d : listaDisciplinaOpcionais) {
					if(d.getCodigo().equals(eventoAceExtra.getMatricula())){
						listaDisciplinaOpcionais.remove(d);
						break;
					}
				}
			}
			horasAceConcluidas += this.aluno.getSobraHorasOpcionais();
		}
		
		SomaInt = aluno.getGrade().getHorasAce();
		if (aluno.getGrade().getHorasAce() != 0){
			percentualAce = (horasAceConcluidas* 100 / SomaInt) ;
		}
		this.resetaDataTables();
	}
	

	public void limpaAluno(){		

		lgAce = true;
		lgNomeAluno  = false;
		lgGradeAluno = true;
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
		this.gradeSimulada = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);
		grade.setHorasAce(0);
		aluno = new Aluno();
		aluno.setGrade(grade);
		this.resetaDataTables();
	}
	
	public void resetaDataTables() {
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

	@Transactional
	public void adicionarAce(){

		if (eventosAce.getPeriodo() == 0) {
			FacesMessage msg = new FacesMessage("Preencha o campo \"Período\"");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		eventosAce.setDescricao(eventosAce.getDescricao().trim());
		if (eventosAce.getDescricao().isEmpty()) {
			FacesMessage msg = new FacesMessage("Preencha o campo Descrição!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		if (eventosAce.getHoras() == 0) {
			FacesMessage msg = new FacesMessage("Preencha o campo Carga Horária!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		//eventosAce.setAluno(aluno);
		eventosAce.setDescricao(eventosAce.getDescricao().toUpperCase());
		eventosAce.setMatricula(aluno.getMatricula());

		logger.info(eventosAce.getDescricao().toUpperCase() + ";" +  eventosAce.getHoras() + ";" + eventosAce.getPeriodo());

		eventosAce.setExcluir(true);



		eventosAceRepository.persistir(eventosAce);
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

	@Transactional
	public void deletarAce(){
		eventosAceRepository.remover(eventoAceSelecionado);
		horasAceConcluidas = (int) (horasAceConcluidas - eventoAceSelecionado.getHoras());
		if (aluno.getGrade().getHorasAce() != 0){
			percentualAce = (horasAceConcluidas* 100 / aluno.getGrade().getHorasAce()) ;
		}		
		listaEventosAce.remove(eventoAceSelecionado);
	}


	//========================================================= GET - SET ==================================================================================//
	public boolean isLgGradeAluno() {
		return lgGradeAluno;
	}
	
	public void setLgGradeAluno(boolean lgGradeAluno) {
		this.lgGradeAluno = lgGradeAluno;
	}
	
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
	
	public Grade getGradeSimulada() {
		return this.gradeSimulada;
	}
	
	public void setGradeSimulada(Grade gradeSimulada) {
		this.gradeSimulada = gradeSimulada;
	}
}
