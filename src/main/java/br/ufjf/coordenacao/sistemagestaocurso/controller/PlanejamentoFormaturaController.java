package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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

import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.report.StudentCoursePlan;
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.GradeDisciplina;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.DisciplinaPlanejamento;
import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.CursoRepository;
import br.ufjf.coordenacao.sistemagestaocurso.repository.DisciplinaRepository;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;


@Named
@ViewScoped
public class PlanejamentoFormaturaController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private boolean lgMatriculaAluno = false;
	private boolean lgNomeAluno  = false;
	private boolean lgSelecionado  = false;	
	private boolean lgCampoHrsPeriodo  = true;	
	private boolean matriculados  = true;	
	private Aluno aluno = new Aluno();
	private DisciplinaPlanejamento disciplinaSelecionada = new DisciplinaPlanejamento();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();
	private boolean lgTabelaUm = false;
	private boolean lgTabelaDois = false;
	private boolean lgTabelaTres = false;
	private boolean lgTabelaQuatro = false;
	private boolean lgTabelaCinco = false;
	private boolean lgTabelaSeis = false;
	private boolean lgTabelaSete = false;
	private boolean lgTabelaOito = false;
	private boolean lgTabelaNove = false;
	private boolean lgTabelaDez = false;
	private boolean lgTabelaOnze = false;
	private boolean lgTabelaDoze = false;
	private boolean lgAluno = true;
	private boolean periodoDesejado;
	private List< String > listaCargaHorariaPeriodo = new ArrayList<String>();
	private int horasEletivasConcluidas;
	private int horasOpcionaisConcluidas;
	private int horasObrigatoriasConcluidas;
	private int horasObrigatorias;
	private int horasFaltamEletivas ;
	private int horasFaltamOpcionais ;
	private int contadorPorPeriodo;
	private Integer periodoInicio;
	private Student st;
	private Integer qtdHorasPeriodo;
	private Curriculum curriculum;
	private Curriculum curriculumAluno;
	private float ira;
	private int periodo;
	private Curso curso = new Curso();
	private EstruturaArvore estruturaArvore;
	
	@Inject
	private AlunoRepository alunos;
	@Inject
	private CursoRepository cursoDAO ;
	@Inject
	private DisciplinaRepository disciplinaDAO ;
	@Inject
	private ImportarArvore importador;



	//========================================================= METODOS ==================================================================================//


	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		try {
		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();

		Calendar now = Calendar.getInstance();
		int mes = now.get(Calendar.MONTH) + 1;
		if (mes > 6){
			periodoInicio = 3;
		}
		else {
			periodoInicio = 1;
		}

		Grade grade = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);
		aluno.setGrade(grade);
		qtdHorasPeriodo= 300;
	
		if (usuarioController.getAutenticacao().getTipoAcesso().equals("aluno")){	
			aluno = alunos.buscarPorMatricula(usuarioController.getAutenticacao().getSelecaoIdentificador());
			
			lgMatriculaAluno = true;
			lgNomeAluno = true;
			lgAluno = false;
			
			if (aluno == null || aluno.getMatricula() == null){
				FacesMessage msg = new FacesMessage("Matrcula no cadastrada na base!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}			
			curso = aluno.getCurso();
			onItemSelectAluno();
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
		}


	}

	public boolean calculaPeriodo(int semestre){
		if(aluno.getGrade().getPeriodoInicio() == 1 && semestre == 3 ){
			return true;
		}
		else if(aluno.getGrade().getPeriodoInicio() == 3 && semestre == 3 ){
			return false;
		}
		else if(aluno.getGrade().getPeriodoInicio() == 1 && semestre == 1 ){
			return false;
		}
		else if(aluno.getGrade().getPeriodoInicio() == 3 && semestre == 1 ){
			return true;
		}
		return true;
	}

	public String corDisciplina(int periodoDisciplina,int periodoInclusao){
		periodoDisciplina = periodoDisciplina % 2;
		periodoInclusao = periodoInclusao % 2;
		if ( periodoInclusao ==  0 && periodoDisciplina == 1 && aluno.getGrade().getPeriodoInicio() == 3 && periodoInicio == 3){
			return "black";
		}
		else if ( periodoInclusao == 1 &&  periodoDisciplina == 1 && aluno.getGrade().getPeriodoInicio() == 3 && periodoInicio == 1){
			return "black";
		}
		else if ( periodoInclusao == 0 &&  periodoDisciplina == 0 && aluno.getGrade().getPeriodoInicio() == 3 && periodoInicio == 1){
			return "black";
		}
		else if ( periodoInclusao == 1 &&  periodoDisciplina == 0 && aluno.getGrade().getPeriodoInicio() == 3 && periodoInicio == 3){
			return "black";
		}
		else if(aluno.getGrade().getPeriodoInicio() == 3){
			return "orange";
		}
		if ( periodoInclusao ==  0 && periodoDisciplina == 0 && aluno.getGrade().getPeriodoInicio() == 1 && periodoInicio == 3){
			return "black";
		}
		else if ( periodoInclusao == 1 &&  periodoDisciplina == 1 && aluno.getGrade().getPeriodoInicio() == 1 && periodoInicio == 3){
			return "black";
		}
		else if ( periodoInclusao == 0 &&  periodoDisciplina == 1 && aluno.getGrade().getPeriodoInicio() == 1 && periodoInicio == 1){
			return "black";
		}
		else if ( periodoInclusao == 1 &&  periodoDisciplina == 0 && aluno.getGrade().getPeriodoInicio() == 1 && periodoInicio == 1){
			return "black";
		}
		else if(aluno.getGrade().getPeriodoInicio() == 1){
			return "orange";
		}
		return null;
	}

	public String bordaPeriodo(int variavel){
		if (aluno.getGrade().getCodigo() != null){
			if (periodo + variavel > aluno.getGrade().getNumeroMaximoPeriodos()){
				return "border-style: solid; border-color: red;";
			}
		}
		return "";
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
		return i + 1;
	}

	public String periodo(int numero){
		Calendar now = Calendar.getInstance();
		int ano = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		int i;
		String periodo = "";
		if(mes > 6){
			if (periodoInicio != 3){
				numero = numero + 1;
			}
		}
		else {
			if (periodoInicio != 1){
				numero = numero + 1;
			}
		}
		for (i=0;i<numero;i++){
			if(mes >= 1 && mes <= 6){
				periodo = "1 - " + ano;
			}
			else {
				periodo = "3 - " + ano;
				ano++;
			}
			mes = mes + 6;
			if (mes > 12){
				mes = mes - 12;
			}
		}
		return periodo;
	}

	public boolean moverDireita(int posicao,DisciplinaPlanejamento disciplinaSelecionadaIntera ){
		if (disciplinaSelecionadaIntera == null){
			disciplinaSelecionadaIntera = disciplinaSelecionada;
		}
		boolean funcionou = true;
		String cor = "";
		if (!disciplinaSelecionadaIntera.getCodigo().equals("ELETIVA") && !disciplinaSelecionadaIntera.getCodigo().equals("OPCIONAL")){
			for(int i: curriculumAluno.getMandatories().keySet()){
				for(Class c: curriculumAluno.getMandatories().get(i)){
					for(Class cl: c.getCorequisite()){
						if(cl.getId().equals(disciplinaSelecionadaIntera.getCodigo())){
							DisciplinaPlanejamento preRequisitoPlanejamento = new DisciplinaPlanejamento();
							preRequisitoPlanejamento.setCodigo(c.getId());
							preRequisitoPlanejamento.setCargaHoraria(c.getWorkload());
							for(DisciplinaPlanejamento disciplinaPrincipal :recuperarLista(posicao) ){
								if (disciplinaPrincipal.getCodigo().equals( preRequisitoPlanejamento.getCodigo())){
									recuperarLista(posicao).remove(disciplinaSelecionadaIntera);
									funcionou = moverDireita (posicao,disciplinaPrincipal);
									break;
								}
							}
						}
					}
					for(Class cl: c.getPrerequisite()){
						if(cl.getId() == disciplinaSelecionadaIntera.getCodigo()){
							DisciplinaPlanejamento preRequisitoPlanejamento = new DisciplinaPlanejamento();
							preRequisitoPlanejamento.setCodigo(c.getId());
							preRequisitoPlanejamento.setCargaHoraria(c.getWorkload());
							if (posicao < 12 ){
								for(DisciplinaPlanejamento disciplinaPrincipal :recuperarLista(posicao + 1) ){
									if (disciplinaPrincipal.getCodigo() == preRequisitoPlanejamento.getCodigo()){
										funcionou = moverDireita (posicao + 1,disciplinaPrincipal);
										break;
									}
								}
							}
						}
					}
					if (funcionou == true){
						if (disciplinaSelecionadaIntera.getCor().equals("black")){
							cor = "orange"; 
						}
						else {
							cor = "black"; 
						}
					}
				}
			}
		}
		if (recuperarLista(posicao + 1) == null){
			FacesMessage msg = new FacesMessage("No  possvel realizar esta ao,verifique os requisitos desta disciplina!");	
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}
		if (funcionou == false){
			return false;
		}
		if (funcionou == true){
			recuperarLista(posicao).remove(disciplinaSelecionadaIntera);
			if (recuperarLista(posicao + 1).size() == 0){
				recuperarLg(posicao + 1, true);
			}
			disciplinaSelecionadaIntera.setCor(cor);
			recuperarLista(posicao + 1).add(disciplinaSelecionadaIntera);
		}
		return true;
	}

	public boolean moverEsquerda(int posicao,DisciplinaPlanejamento disciplinaSelecionadaIntera ){
		if (disciplinaSelecionadaIntera == null){
			disciplinaSelecionadaIntera = disciplinaSelecionada;
		}
		boolean funcionou = true;
		String cor = "";
		if (!disciplinaSelecionadaIntera.getCodigo().equals("ELETIVA") && !disciplinaSelecionadaIntera.getCodigo().equals("OPCIONAL")){
			for(int i: curriculumAluno.getMandatories().keySet()){
				for(Class c: curriculumAluno.getMandatories().get(i)) {	
					if (c.getId() == disciplinaSelecionadaIntera.getCodigo()){
						for(Class cl: c.getCorequisite()){
							for(DisciplinaPlanejamento disciplinaPrincipal :recuperarLista(posicao) ){
								if (disciplinaPrincipal.getCodigo().equals( cl.getId())){
									recuperarLista(posicao).remove(disciplinaSelecionadaIntera);
									funcionou = moverEsquerda (posicao,disciplinaPrincipal);
									break;
								}
							}
						}
						for(Class cl: c.getPrerequisite()){
							if (posicao > 1 ){
								for(DisciplinaPlanejamento disciplinaPrincipal :recuperarLista(posicao - 1)  ){
									if (disciplinaPrincipal.getCodigo() == cl.getId()){
										funcionou = moverEsquerda (posicao - 1,disciplinaPrincipal);
										break;
									}
								}
							}
						}
					}
					if (disciplinaSelecionadaIntera.getCor().equals("black")){
						cor = "orange"; 
					}
					else {
						cor = "black"; 
					}
				}
			}
		}
		if (recuperarLista(posicao - 1) == null || recuperarLista(posicao) == null){
			FacesMessage msg = new FacesMessage("No  possvel realizar esta ao,verifique os requisitos desta disciplina!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}
		if (funcionou == false){
			return false;
		}
		if (funcionou == true){
			recuperarLista(posicao).remove(disciplinaSelecionadaIntera);
			disciplinaSelecionadaIntera.setCor(cor);
			recuperarLista(posicao - 1).add(disciplinaSelecionadaIntera);
			recuperarLg(posicao - 1, true);
			for(int x = 12; x >= posicao; x-- ){
				if (recuperarLista(x).size() == 0){
					recuperarLg(x, false);
				}
				else {
					break;
				}
			}
		}
		return true;
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

	public void onItemSelectAluno() {
		aluno = alunos.buscarPorMatricula(aluno.getMatricula());
		
		lgMatriculaAluno = true;
		lgNomeAluno = true;	
		lgCampoHrsPeriodo = false;
		listaCargaHorariaPeriodo = new ArrayList<String>();
		importador = estruturaArvore.recuperarArvore(aluno.getGrade(),true);
		StudentsHistory sh = importador.getSh();
		st = sh.getStudents().get(aluno.getMatricula());

		if (st == null){

			FacesMessage msg = new FacesMessage("O aluno:" + aluno.getMatricula() + " não tem nenhum histórico de matricula cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			lgCampoHrsPeriodo = true;
			return;

		}


		ira = st.getIRA();
		if(ira == -1) {
			ira = 0;
		}
		periodo = aluno.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
		importador.considerarCo();
		curriculum = importador.get_cur();		

		/*if (!aluno.getGrade().estaCompleta()) {
			FacesMessage msg = new FacesMessage("Grade Incompleta verifique o menu Cadastros > Grade!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}*/
		
		gerarExpectativa();
		
	}

	public void gerarExpectativa(){

		if (!aluno.getGrade().estaCompleta()) {
			FacesMessage msg = new FacesMessage("Não foi possível gerar o planejamento. Motivo: a grade está incompleta");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}


		if(qtdHorasPeriodo < 60){
			FacesMessage msg = new FacesMessage("A quantidade de horas por período deve ser maior que 60h!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();
		lgTabelaUm = false;
		lgTabelaDois = false;
		lgTabelaTres = false;
		lgTabelaQuatro = false;
		lgTabelaCinco = false;
		lgTabelaSeis = false;
		lgTabelaSete = false;
		lgTabelaOito = false;
		lgTabelaNove = false;
		lgTabelaDez = false;
		lgTabelaOnze = false;
		lgTabelaDoze = false;
		try {
		gerarDadosAluno(st,curriculum);

		periodoDesejado = calculaPeriodo(periodoInicio);
		StudentCoursePlan g = new StudentCoursePlan(st, curriculum, qtdHorasPeriodo,periodoDesejado,!matriculados);
		
			curriculumAluno = g.generate();

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Verificar gerador de planejamento");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		int ultimoPeriodoPreenchido = 0;
		if (curriculumAluno.getMandatories().keySet().size() > 12){
			FacesMessage msg = new FacesMessage("A quantidade de horas por período deve ser maior pois o número de períodos excedeu o limite de 12 períodos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		horasFaltamEletivas =     aluno.getGrade().getHorasEletivas() - aluno.getHorasEletivasCompletadas();
		horasFaltamOpcionais =   aluno.getGrade().getHorasOpcionais() - horasOpcionaisConcluidas;
		int contadorPeriodosGerados = 0;
		for(int i : curriculumAluno.getMandatories().keySet()){			
			contadorPorPeriodo = 0;
			while(contadorPeriodosGerados != i){
				gerarEletivasObrigatorias (recuperarLista(contadorPeriodosGerados + 1),(contadorPeriodosGerados + 1));
				contadorPeriodosGerados++;
				contadorPorPeriodo = 0;
			}
			for(Class cl: curriculumAluno.getMandatories().get(i)){				
				contadorPorPeriodo =  contadorPorPeriodo + cl.getWorkload();				
				DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
				disciplinaPlanejamento.setCargaHoraria(cl.getWorkload());
				disciplinaPlanejamento.setCodigo(cl.getId());
				Disciplina disciplina = disciplinaDAO.buscarPorCodigoDisciplina(cl.getId());
				GradeDisciplina gradeDisciplina  = null;
				for(GradeDisciplina gradeDisciplinaSelecionada : aluno.getGrade().getGrupoGradeDisciplina()){
					if (gradeDisciplinaSelecionada.getDisciplina().getCodigo().equals(disciplina.getCodigo())){

						gradeDisciplina = gradeDisciplinaSelecionada;	
						break;
					}
				}
				int  periodo = 0;
				if (gradeDisciplina != null){
					periodo = (int) (long) gradeDisciplina.getPeriodo();
					disciplinaPlanejamento.setCor(corDisciplina(periodo,((i))));
					if(gradeDisciplina.getTipoDisciplina().equals("Eletiva")){}
				}
				else {	disciplinaPlanejamento.setCor("black");
				}
				recuperarLista(i+1).add(disciplinaPlanejamento);
				recuperarLg(i+1, true);
			}
			gerarEletivasObrigatorias (recuperarLista(i+1),(i+1));
			ultimoPeriodoPreenchido = i;
			listaCargaHorariaPeriodo.add(Integer.toString(contadorPorPeriodo));
			contadorPeriodosGerados ++;
		}
		
		if (ultimoPeriodoPreenchido == 0){
			ultimoPeriodoPreenchido = -1;
			
		}
		
		while(horasFaltamEletivas > 0 || horasFaltamOpcionais > 0){
			contadorPorPeriodo = 0;
			
				ultimoPeriodoPreenchido = ultimoPeriodoPreenchido + 1;
			
			if (ultimoPeriodoPreenchido >= 12){
				FacesMessage msgs = new FacesMessage("A Quantidade de Horas Período deve ser maior pois o número de períodos excedeu 12 períodos!");
				FacesContext.getCurrentInstance().addMessage(null, msgs);
				listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
				listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();
				lgTabelaUm = false;
				lgTabelaDois = false;
				lgTabelaTres = false;
				lgTabelaQuatro = false;
				lgTabelaCinco = false;
				lgTabelaSeis = false;
				lgTabelaSete = false;
				lgTabelaOito = false;
				lgTabelaNove = false;
				lgTabelaDez = false;
				lgTabelaOnze = false;
				lgTabelaDoze = false;
				return;
			}
			recuperarLg(ultimoPeriodoPreenchido+ 1, true);
			gerarEletivasObrigatorias (recuperarLista(ultimoPeriodoPreenchido+1),(ultimoPeriodoPreenchido+1));
			listaCargaHorariaPeriodo.add(Integer.toString(contadorPorPeriodo));
		}
		
		if(this.listaDisciplinaSelecionadas.isEmpty() && this.listaDisciplinaSelecionadasDois.isEmpty()
				&& this.listaDisciplinaSelecionadasTres.isEmpty()
				&& this.listaDisciplinaSelecionadasQuatro.isEmpty()
				&& this.listaDisciplinaSelecionadasCinco.isEmpty()
				&& this.listaDisciplinaSelecionadasSeis.isEmpty()
				&& this.listaDisciplinaSelecionadasSete.isEmpty()
				&& this.listaDisciplinaSelecionadasOito.isEmpty()
				&& this.listaDisciplinaSelecionadasNove.isEmpty()
				&& this.listaDisciplinaSelecionadasDez.isEmpty()
				&& this.listaDisciplinaSelecionadasOnze.isEmpty()
				&& this.listaDisciplinaSelecionadasDoze.isEmpty())
		{
			FacesMessage msg = new FacesMessage("Não há disciplinas para serem exibidas!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String cargaHorariaPeriodo(int periodo){
		if (listaCargaHorariaPeriodo.size() == 0) {
			return "";
		}
		return listaCargaHorariaPeriodo.get(periodo);
	}

	public void gerarEletivasObrigatorias (List<DisciplinaPlanejamento> listaDisciplinas,int numeroLista){
		boolean continuar = true; 
		while(continuar){
			if ( (qtdHorasPeriodo - contadorPorPeriodo) > 0){
				if (horasFaltamEletivas >= 60 && (qtdHorasPeriodo - contadorPorPeriodo)>= 60){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria(60);
					disciplinaPlanejamento.setCodigo("ELETIVA" );
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamEletivas = horasFaltamEletivas - 60;
					contadorPorPeriodo = contadorPorPeriodo + 60;
				}
				else if (horasFaltamOpcionais >= 60 && (qtdHorasPeriodo - contadorPorPeriodo) >= 60){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria(60);
					disciplinaPlanejamento.setCodigo("OPCIONAL" );
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamOpcionais = horasFaltamOpcionais - 60;
					contadorPorPeriodo = contadorPorPeriodo + 60;
				}
				else if (horasFaltamEletivas >= 30 && (qtdHorasPeriodo - contadorPorPeriodo) >= 30){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria(30);
					disciplinaPlanejamento.setCodigo("ELETIVA");
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamEletivas = horasFaltamEletivas - 30;
					contadorPorPeriodo = contadorPorPeriodo + 30;
				}
				else if (horasFaltamOpcionais >= 30 && (qtdHorasPeriodo - contadorPorPeriodo) >= 30){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria(30);
					disciplinaPlanejamento.setCodigo("OPCIONAL");
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamOpcionais = horasFaltamOpcionais - 30;
					contadorPorPeriodo = contadorPorPeriodo + 30;
				}
				else  if (horasFaltamEletivas > 0 && (qtdHorasPeriodo - contadorPorPeriodo) >= horasFaltamEletivas){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria(horasFaltamEletivas);
					disciplinaPlanejamento.setCodigo("ELETIVA");
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamEletivas = 0;
					contadorPorPeriodo = contadorPorPeriodo + horasFaltamEletivas;
				}
				else if (horasFaltamOpcionais > 0 && (qtdHorasPeriodo - contadorPorPeriodo) >= horasFaltamOpcionais){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria(horasFaltamOpcionais);
					disciplinaPlanejamento.setCodigo("OPCIONAL");
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamOpcionais = 0;
					contadorPorPeriodo = contadorPorPeriodo + horasFaltamOpcionais;
				}
				else if((qtdHorasPeriodo - contadorPorPeriodo) < horasFaltamOpcionais){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria((qtdHorasPeriodo - contadorPorPeriodo));
					disciplinaPlanejamento.setCodigo("OPCIONAL");
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamOpcionais = horasFaltamOpcionais - (qtdHorasPeriodo - contadorPorPeriodo);
					contadorPorPeriodo = contadorPorPeriodo + (qtdHorasPeriodo - contadorPorPeriodo);
				}
				else if((qtdHorasPeriodo - contadorPorPeriodo) < horasFaltamEletivas){
					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
					disciplinaPlanejamento.setCargaHoraria((qtdHorasPeriodo - contadorPorPeriodo));
					disciplinaPlanejamento.setCodigo("ELETIVA");
					listaDisciplinas.add(disciplinaPlanejamento);
					recuperarLg(numeroLista, true);
					horasFaltamEletivas = horasFaltamEletivas - (qtdHorasPeriodo - contadorPorPeriodo);
					contadorPorPeriodo = contadorPorPeriodo + (qtdHorasPeriodo - contadorPorPeriodo);
				}

				else if ((qtdHorasPeriodo - contadorPorPeriodo) < horasFaltamOpcionais && (qtdHorasPeriodo - contadorPorPeriodo) < horasFaltamEletivas){
					continuar = false;
				}

				else if (horasFaltamOpcionais <= 0 && horasFaltamEletivas <= 0){
					continuar = false;
				}
			} 
			else {
				continuar = false;
			}
		}
	}

	public List<DisciplinaPlanejamento> recuperarLista(int i){
		if(i== 1) return listaDisciplinaSelecionadas;
		if(i== 2) return listaDisciplinaSelecionadasDois;
		if(i== 3) return listaDisciplinaSelecionadasTres;
		if(i== 4) return listaDisciplinaSelecionadasQuatro;
		if(i== 5) return listaDisciplinaSelecionadasCinco;
		if(i== 6) return listaDisciplinaSelecionadasSeis;
		if(i== 7) return listaDisciplinaSelecionadasSete;
		if(i== 8) return listaDisciplinaSelecionadasOito;
		if(i== 9) return listaDisciplinaSelecionadasNove;
		if(i== 10) return listaDisciplinaSelecionadasDez;
		if(i== 11) return listaDisciplinaSelecionadasOnze;
		if(i== 12) return listaDisciplinaSelecionadasDoze;
		return null;
	}

	public void recuperarLg(int i,boolean valor){
		if(i== 1)  lgTabelaUm = valor;
		if(i== 2)  lgTabelaDois = valor;
		if(i== 3)  lgTabelaTres = valor;
		if(i== 4)  lgTabelaQuatro = valor;
		if(i== 5)  lgTabelaCinco = valor;
		if(i== 6)  lgTabelaSeis = valor;
		if(i== 7)  lgTabelaSete = valor;
		if(i== 8)  lgTabelaOito = valor;
		if(i== 9)  lgTabelaNove = valor;
		if(i== 10)  lgTabelaDez = valor;
		if(i== 11)  lgTabelaOnze = valor;
		if(i== 12)  lgTabelaDoze = valor;
	}

	public void limpaAluno(){
		aluno = new Aluno();
		lgMatriculaAluno = false;
		lgNomeAluno  = false;
		ira = 0;
		periodo = 0;	
		horasObrigatorias = 0;
		horasObrigatoriasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasEletivasConcluidas = 0;
		qtdHorasPeriodo = 300;
		listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();
		lgTabelaUm = false;
		lgTabelaDois = false;
		lgTabelaTres = false;
		lgTabelaQuatro = false;
		lgTabelaCinco = false;
		lgTabelaSeis = false;
		lgTabelaSete = false;
		lgTabelaOito = false;
		lgTabelaNove = false;
		lgTabelaDez = false;
		lgTabelaOnze = false;
		lgTabelaDoze = false;
		Grade grade = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);
		lgCampoHrsPeriodo = true;
		aluno.setGrade(grade);
	}

	public void gerarDadosAluno(Student st, Curriculum cur)
	{
		HashMap<Class, ArrayList<String[]>> aprovado;
		HashMap<Class, ArrayList<String[]>> matriculadosGrade;
		horasObrigatorias = 0;
		horasObrigatoriasConcluidas = aluno.getHorasObrigatoriasCompletadas();
		horasOpcionaisConcluidas = aluno.getHorasOpcionaisCompletadas();
		horasEletivasConcluidas = aluno.getHorasEletivasCompletadas();
		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED)); 
		matriculadosGrade = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.ENROLLED)); 
		TreeSet<String> naocompletado = new TreeSet<String>();
		for(int i: cur.getMandatories().keySet()){
			for(Class c: cur.getMandatories().get(i)){
				horasObrigatorias = horasObrigatorias + c.getWorkload();
				if (matriculadosGrade.containsKey(c) && matriculados){
					//horasObrigatoriasConcluidas = horasObrigatoriasConcluidas + c.getWorkload();
					matriculadosGrade.remove(c);
				}
				if(!aprovado.containsKey(c)) {
					naocompletado.add(c.getId());
				}
				else{
					//horasObrigatoriasConcluidas = horasObrigatoriasConcluidas + c.getWorkload();
					aprovado.remove(c);
				}
			}	
		}
		//int creditos = 0;
		for(Class c: cur.getElectives()){
			if(aprovado.containsKey(c))	{
				//creditos += c.getWorkload();
				aprovado.remove(c);
			}
			if (matriculadosGrade.containsKey(c) && matriculados){
				//creditos += c.getWorkload();
				matriculadosGrade.remove(c);
			}
		}
		//horasEletivasConcluidas = creditos;
		//creditos = 0;
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext()){
			Class c = i.next();
			//creditos += c.getWorkload();
		}
		if (matriculados){
			Set<Class> apm = matriculadosGrade.keySet();
			Iterator<Class> im = apm.iterator();
			while(im.hasNext())	{
				Class c = im.next();
				//creditos += c.getWorkload();
			}
		}
		//horasOpcionaisConcluidas = creditos;
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



	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public ImportarArvore getImportador() {
		return importador;
	}

	public void setImportador(ImportarArvore importador) {
		this.importador = importador;
	}

	public boolean isLgSelecionado() {
		return lgSelecionado;
	}

	public void setLgSelecionado(boolean lgSelecionado) {
		this.lgSelecionado = lgSelecionado;
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

	public DisciplinaPlanejamento getDisciplinaSelecionada() {
		return disciplinaSelecionada;
	}

	public void setDisciplinaSelecionada(
			DisciplinaPlanejamento disciplinaSelecionada) {
		this.disciplinaSelecionada = disciplinaSelecionada;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadas() {
		return listaDisciplinaSelecionadas;
	}

	public void setListaDisciplinaSelecionadas(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadas) {
		this.listaDisciplinaSelecionadas = listaDisciplinaSelecionadas;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasDois() {
		return listaDisciplinaSelecionadasDois;
	}

	public void setListaDisciplinaSelecionadasDois(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDois) {
		this.listaDisciplinaSelecionadasDois = listaDisciplinaSelecionadasDois;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasTres() {
		return listaDisciplinaSelecionadasTres;
	}

	public void setListaDisciplinaSelecionadasTres(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasTres) {
		this.listaDisciplinaSelecionadasTres = listaDisciplinaSelecionadasTres;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasQuatro() {
		return listaDisciplinaSelecionadasQuatro;
	}

	public void setListaDisciplinaSelecionadasQuatro(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasQuatro) {
		this.listaDisciplinaSelecionadasQuatro = listaDisciplinaSelecionadasQuatro;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasCinco() {
		return listaDisciplinaSelecionadasCinco;
	}

	public void setListaDisciplinaSelecionadasCinco(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasCinco) {
		this.listaDisciplinaSelecionadasCinco = listaDisciplinaSelecionadasCinco;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasSeis() {
		return listaDisciplinaSelecionadasSeis;
	}

	public void setListaDisciplinaSelecionadasSeis(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSeis) {
		this.listaDisciplinaSelecionadasSeis = listaDisciplinaSelecionadasSeis;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasSete() {
		return listaDisciplinaSelecionadasSete;
	}

	public void setListaDisciplinaSelecionadasSete(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSete) {
		this.listaDisciplinaSelecionadasSete = listaDisciplinaSelecionadasSete;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasOito() {
		return listaDisciplinaSelecionadasOito;
	}

	public void setListaDisciplinaSelecionadasOito(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOito) {
		this.listaDisciplinaSelecionadasOito = listaDisciplinaSelecionadasOito;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasNove() {
		return listaDisciplinaSelecionadasNove;
	}

	public void setListaDisciplinaSelecionadasNove(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasNove) {
		this.listaDisciplinaSelecionadasNove = listaDisciplinaSelecionadasNove;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasDez() {
		return listaDisciplinaSelecionadasDez;
	}

	public void setListaDisciplinaSelecionadasDez(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDez) {
		this.listaDisciplinaSelecionadasDez = listaDisciplinaSelecionadasDez;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasOnze() {
		return listaDisciplinaSelecionadasOnze;
	}

	public void setListaDisciplinaSelecionadasOnze(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOnze) {
		this.listaDisciplinaSelecionadasOnze = listaDisciplinaSelecionadasOnze;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasDoze() {
		return listaDisciplinaSelecionadasDoze;
	}

	public void setListaDisciplinaSelecionadasDoze(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDoze) {
		this.listaDisciplinaSelecionadasDoze = listaDisciplinaSelecionadasDoze;
	}

	public float getIra() {
		return ira;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public List<String> getListaCargaHorariaPeriodo() {
		return listaCargaHorariaPeriodo;
	}

	public void setListaCargaHorariaPeriodo(List<String> listaCargaHorariaPeriodo) {
		this.listaCargaHorariaPeriodo = listaCargaHorariaPeriodo;
	}

	public int getHorasFaltamEletivas() {
		return horasFaltamEletivas;
	}

	public void setHorasFaltamEletivas(int horasFaltamEletivas) {
		this.horasFaltamEletivas = horasFaltamEletivas;
	}

	public int getHorasFaltamOpcionais() {
		return horasFaltamOpcionais;
	}

	public void setHorasFaltamOpcionais(int horasFaltamOpcionais) {
		this.horasFaltamOpcionais = horasFaltamOpcionais;
	}

	public Curriculum getCurriculumAluno() {
		return curriculumAluno;
	}

	public void setCurriculumAluno(Curriculum curriculumAluno) {
		this.curriculumAluno = curriculumAluno;
	}

	public int getContadorPorPeriodo() {
		return contadorPorPeriodo;
	}

	public void setContadorPorPeriodo(int contadorPorPeriodo) {
		this.contadorPorPeriodo = contadorPorPeriodo;
	}

	public Integer getQtdHorasPeriodo() {
		return qtdHorasPeriodo;
	}

	public void setQtdHorasPeriodo(Integer qtdHorasPeriodo) {
		this.qtdHorasPeriodo = qtdHorasPeriodo;
	}

	public boolean isLgCampoHrsPeriodo() {
		return lgCampoHrsPeriodo;
	}

	public void setLgCampoHrsPeriodo(boolean lgCampoHrsPeriodo) {
		this.lgCampoHrsPeriodo = lgCampoHrsPeriodo;
	}

	public Student getSt() {
		return st;
	}

	public void setSt(Student st) {
		this.st = st;
	}

	public boolean isLgTabelaUm() {
		return lgTabelaUm;
	}

	public void setLgTabelaUm(boolean lgTabelaUm) {
		this.lgTabelaUm = lgTabelaUm;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public boolean isLgTabelaDois() {
		return lgTabelaDois;
	}

	public void setLgTabelaDois(boolean lgTabelaDois) {
		this.lgTabelaDois = lgTabelaDois;
	}

	public boolean isLgTabelaTres() {
		return lgTabelaTres;
	}

	public void setLgTabelaTres(boolean lgTabelaTres) {
		this.lgTabelaTres = lgTabelaTres;
	}

	public boolean isLgTabelaQuatro() {
		return lgTabelaQuatro;
	}

	public void setLgTabelaQuatro(boolean lgTabelaQuatro) {
		this.lgTabelaQuatro = lgTabelaQuatro;
	}

	public boolean isLgTabelaCinco() {
		return lgTabelaCinco;
	}

	public void setLgTabelaCinco(boolean lgTabelaCinco) {
		this.lgTabelaCinco = lgTabelaCinco;
	}

	public boolean isLgTabelaSeis() {
		return lgTabelaSeis;
	}

	public void setLgTabelaSeis(boolean lgTabelaSeis) {
		this.lgTabelaSeis = lgTabelaSeis;
	}

	public boolean isLgTabelaSete() {
		return lgTabelaSete;
	}

	public void setLgTabelaSete(boolean lgTabelaSete) {
		this.lgTabelaSete = lgTabelaSete;
	}

	public boolean isLgTabelaOito() {
		return lgTabelaOito;
	}

	public void setLgTabelaOito(boolean lgTabelaOito) {
		this.lgTabelaOito = lgTabelaOito;
	}

	public boolean isLgTabelaNove() {
		return lgTabelaNove;
	}

	public void setLgTabelaNove(boolean lgTabelaNove) {
		this.lgTabelaNove = lgTabelaNove;
	}

	public boolean isLgTabelaDez() {
		return lgTabelaDez;
	}

	public void setLgTabelaDez(boolean lgTabelaDez) {
		this.lgTabelaDez = lgTabelaDez;
	}

	public boolean isLgTabelaOnze() {
		return lgTabelaOnze;
	}

	public void setLgTabelaOnze(boolean lgTabelaOnze) {
		this.lgTabelaOnze = lgTabelaOnze;
	}

	public boolean isLgTabelaDoze() {
		return lgTabelaDoze;
	}

	public void setLgTabelaDoze(boolean lgTabelaDoze) {
		this.lgTabelaDoze = lgTabelaDoze;
	}

	public boolean isLgAluno() {
		return lgAluno;
	}

	public void setLgAluno(boolean lgAluno) {
		this.lgAluno = lgAluno;
	}

	public boolean isPeriodoDesejado() {
		return periodoDesejado;
	}

	public void setPeriodoDesejado(boolean periodoDesejado) {
		this.periodoDesejado = periodoDesejado;
	}

	public Integer getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Integer periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public boolean isMatriculados() {
		return matriculados;
	}

	public void setMatriculados(boolean matriculados) {
		this.matriculados = matriculados;
	}
}