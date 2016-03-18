package controller.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Curso;
import model.Disciplina;
import model.Equivalencia;
import model.Grade;
import model.GradeDisciplina;
import model.PreRequisito;
import model.arvore.Class;
import model.arvore.ClassFactory;
import model.estrutura.DisciplinaGradeDisciplina;

import org.primefaces.component.datatable.DataTable;

import controller.util.EstruturaArvore;
import controller.util.Ordenar;
import controller.util.UsuarioController;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.EquivalenciaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.PreRequisitoDAO;

@Named 
@ViewScoped
public class CadastroGradeController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private boolean lgHorasAoe = true;
	private boolean lgMaxPeriodo = true;
	private boolean lgExcluirGrade = true;
	private boolean lgIncluirGrade = true;	
	private boolean lgCodigoGrade = false;
	private boolean lgHorasEletivas = true;
	private boolean lgNomeDisciplina = true;	
	private boolean lgTipoDisciplina = true;
	private boolean lgHorasOpcionais  = true;		
	private boolean lgCodigoDisciplina = true;	
	private boolean lgLimparDisciplina = true;
	private boolean lgIncluirDisciplina = true;
	private boolean lgPeriodoDisciplina = true;
	private boolean lgLimparEquivalencia = true;
	private boolean lgIncluirEquivalencia = true;
	private boolean lgCargaHorariaDisciplina = true;
	private boolean lgNomeDisciplinaEquivalenciaUm = true;
	private boolean lgNomeDisciplinaEquivalenciaDois = true;
	private boolean lgCodigoDisciplinaEquivalenciaUm = true;
	private boolean lgCodigoDisciplinaEquivalenciaDois = true;
	private boolean lgDisciplinaIra = true;

	private List<Equivalencia> listaEquivalenciaSelecionada ;
	private List<Equivalencia> listaEquivalencia = new ArrayList<Equivalencia>();	
	private List<DisciplinaGradeDisciplina> listaEletivasSelecionada ;
	private List<DisciplinaGradeDisciplina> listaObrigatoriasSelecionada ;
	private List<DisciplinaGradeDisciplina> listaEletivas = new ArrayList<DisciplinaGradeDisciplina>();
	private List<DisciplinaGradeDisciplina> listaObrigatorias = new ArrayList<DisciplinaGradeDisciplina>();		
	private List<GradeDisciplina> listaIra = new ArrayList<GradeDisciplina>();	
	private List<GradeDisciplina> listaIraSelecionados ;
	private List<PreRequisito> listaPreRequisitos = new ArrayList<PreRequisito>();	

	private Grade grade = new Grade();	
	private Curso curso = new Curso();
	private Ordenar ordenar = new Ordenar();
	private Disciplina disciplina = new Disciplina();	
	private Disciplina disciplinaPre = new Disciplina();
	private Disciplina disciplinaNova = new Disciplina();
	private Disciplina disciplinaIra = new Disciplina();
	private Disciplina disciplinaEquivalenciaUm = new Disciplina();
	private Disciplina disciplinaEquivalenciaDois = new Disciplina();
	private GradeDisciplina gradeDisciplina = new GradeDisciplina();	
	private PreRequisito linhaSelecionadaPreRequisto = new PreRequisito();
	private Equivalencia linhaSelecionadaEquivalencia = new Equivalencia();
	private DisciplinaGradeDisciplina linhaSelecionada = new DisciplinaGradeDisciplina();	
	private GradeDisciplina gradeDisciplinaIraSelecionada = new GradeDisciplina();

	private GradeDAO gradeDAO ;	
	private DisciplinaDAO disciplinaDAO ;
	private PreRequisitoDAO preRequisitoDAO ;
	private EquivalenciaDAO equivalenciaDAO ;
	private GradeDisciplinaDAO gradeDisciplinaDAO ;
	private CursoDAO cursoDAO ;

	private String tipoPre;
	private EstruturaArvore estruturaArvore;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();
		gradeDAO = estruturaArvore.getGradeDAO();
		cursoDAO = estruturaArvore.getCursoDAO();
		disciplinaDAO = estruturaArvore.getDisciplinaDAO();
		equivalenciaDAO = estruturaArvore.getEquivalenciaDAO();
		preRequisitoDAO =  estruturaArvore.getPreRequisitoDAO();		
		gradeDisciplinaDAO = estruturaArvore.getGradeDisciplinaDAO();
		curso = cursoDAO.buscarPorCodigo(usuarioController.getAutenticacao().getCursoSelecionado().getCodigo());	
	}

	public List<String> gradeCodigos(String codigo) {	
		
		codigo = codigo.toUpperCase();
		List<String> todos = new ArrayList<String>();
		for (Grade gradeQuestao : curso.getGrupoGrades()){
			if(gradeQuestao.getCodigo().contains(codigo)){
				todos.add(gradeQuestao.getCodigo()); 
			}
		}
		return todos;


	}

	public void buscarGrade(){

		Grade gradeAuxiliar = new Grade();
		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();
		grade.setCodigo(grade.getCodigo().trim());
		grade.setCodigo(grade.getCodigo().toUpperCase());		
		boolean achouGrade = false;

		for(Grade gradeSelecionada : curso.getGrupoGrades()){
			if (gradeSelecionada.getCodigo().equals(grade.getCodigo())){
				gradeAuxiliar = gradeSelecionada;
				achouGrade = true;
				break;
			}
		}

		if (achouGrade == true) {
			grade = gradeAuxiliar;
			FacesMessage msg = new FacesMessage("Grade encontrada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			lgCodigoGrade = true;
			lgHorasEletivas = false;
			lgHorasOpcionais  = false;
			lgHorasAoe = false;
			lgMaxPeriodo = false;
			lgCodigoDisciplina = false;
			lgNomeDisciplina = false;	
			lgCargaHorariaDisciplina = false;
			lgTipoDisciplina = false;
			lgIncluirDisciplina = false;
			lgLimparDisciplina = false;
			lgCodigoDisciplinaEquivalenciaUm = false;
			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaDois = false;
			lgIncluirEquivalencia = false;
			lgLimparEquivalencia = false;			
			lgDisciplinaIra = false;
			lgExcluirGrade = false;
			atualizaGrids();
		}

		else{	

			if(grade.getCodigo() != null && !grade.getCodigo().equals("")){
				grade.setCurso(curso);
				grade.setHorasAce( 0);
				grade.setHorasEletivas( 0);
				grade.setHorasOpcionais( 0);
				gradeDAO.persistir(grade);
				
				usuarioController.setReseta(true);
				usuarioController.atualizarPessoaLogada();
				
				FacesMessage msg = new FacesMessage("Nova grade cadastrada!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				

			}
			else {
				FacesMessage msg = new FacesMessage("Preencha o campo Código Grade!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}

			lgCodigoGrade = true;
			lgHorasEletivas = false;
			lgHorasOpcionais  = false;
			lgHorasAoe = false;
			lgMaxPeriodo = false;
			lgCodigoDisciplina = false;
			lgNomeDisciplina = false;	
			lgCargaHorariaDisciplina = false;
			lgTipoDisciplina = false;
			lgIncluirDisciplina = false;
			lgLimparDisciplina = false;
			lgCodigoDisciplinaEquivalenciaUm = false;
			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaDois = false;
			lgIncluirEquivalencia = false;
			lgLimparEquivalencia = false;
			lgDisciplinaIra = false;
			lgExcluirGrade = false;
			estruturaArvore.resetarCursoDAO();
		}
	}

	public void excluirGrade(){

		/*for(Grade gradeSelecionada : curso.getGrupoGrades()){
			if (gradeSelecionada.getCodigo().equals(grade.getCodigo())){
				grade = gradeSelecionada;
				break;
			}
		}

		List<GradeDisciplina> listaGradeDisciplina = grade.getGrupoGradeDisciplina();

		if (listaGradeDisciplina != null){
			for (GradeDisciplina gradeDisiplinaDeleta : listaGradeDisciplina){
				List<PreRequisito> listaPre = gradeDisiplinaDeleta.getPreRequisito();
				if (listaPre != null){
					for (PreRequisito preReq:listaPre){
						preRequisitoDAO.removePeloId(preReq.getId());
					}
				}
				gradeDisciplinaDAO.removePeloId(gradeDisiplinaDeleta.getId());
			}
		}

		ArrayList<Equivalencia> listaEquivalencia = equivalenciaDAO.buscarTodasEquivalenciasPorGrade(grade.getId());		
		if (listaEquivalencia != null){			
			for (Equivalencia equivalenciaDeletar :listaEquivalencia ){				
				equivalenciaDAO.removePeloId(equivalenciaDeletar.getId());			

			}			
		}*/
		curso.getGrupoGrades().remove(grade);
		gradeDAO.removePeloId(grade.getId());
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		usuarioController.atualizarPessoaLogada();
		
		//estruturaArvore.resetarCursoDAO();
		limpaGrade();

	}

	public void limpaGrade(){

		grade = new Grade();
		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();
		listaEquivalencia = new ArrayList<Equivalencia>();
		listaIra = new ArrayList<GradeDisciplina>();
		lgCodigoGrade = false;
		lgHorasEletivas = true;
		lgHorasOpcionais  = true;
		lgHorasAoe = true;
		lgMaxPeriodo = true;
		lgIncluirGrade = true;
		lgCodigoDisciplina = true;
		lgNomeDisciplina = true;	
		lgPeriodoDisciplina = true;
		lgCargaHorariaDisciplina = true;
		lgTipoDisciplina = true;
		lgIncluirDisciplina = true;
		lgLimparDisciplina = true;
		lgCodigoDisciplinaEquivalenciaUm = true;
		lgCodigoDisciplinaEquivalenciaDois = true;
		lgNomeDisciplinaEquivalenciaUm = true;
		lgNomeDisciplinaEquivalenciaDois = true;
		lgIncluirEquivalencia = true;
		lgLimparEquivalencia = true;
		lgDisciplinaIra = true;
		lgExcluirGrade = true;
		listaObrigatorias = new ArrayList<DisciplinaGradeDisciplina>();
		listaEletivas = new ArrayList<DisciplinaGradeDisciplina>();

		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridObrigatorias");
		dataTable.clearInitialState();
		dataTable.reset();

		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEletivas");
		dataTable.clearInitialState();
		dataTable.reset();

		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEquivalencias");
		dataTable.clearInitialState();
		dataTable.reset();		

		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridIra");
		dataTable.clearInitialState();
		dataTable.reset();
		
		init();

	}

	public void alterarGrade(){
		
		
		
		gradeDAO.editar(grade);
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		//usuarioController.atualizarPessoaLogada();
	}

	public void alteraCampoPeriodo(){

		if(gradeDisciplina.getTipoDisciplina().equals("Obrigatoria")){
			lgPeriodoDisciplina = false;
		}
		else{
			lgPeriodoDisciplina = true;
		}
	}

	public List<String> disciplinaCodigos(String codigo) {	
		codigo = codigo.toUpperCase();
		List<String> todos = disciplinaDAO.buscarTodosCodigosDisciplina(codigo);
		return todos;
	}

	public List<Disciplina> disciplinaNomes(String codigo) {	
		codigo = codigo.toUpperCase();
		List<Disciplina> todos = disciplinaDAO.buscarTodosNomesDisciplinaObjeto(codigo);
		return todos;
	}

	public List<Disciplina> disciplinaNomesEquivalencia(String codigo) {	

		List<Disciplina> listaNomesSelecionados = new ArrayList<Disciplina>();
		List<DisciplinaGradeDisciplina> newList = new ArrayList<DisciplinaGradeDisciplina>(listaObrigatorias);
		newList.addAll(listaEletivas);
		for (DisciplinaGradeDisciplina disciplina:newList ){
			if (disciplina.getDisciplina().getNome().indexOf(codigo.toUpperCase()) >= 0){
				listaNomesSelecionados.add(disciplina.getDisciplina());
			}
		}
		codigo = codigo.toUpperCase();
		return listaNomesSelecionados;
	}

	public List<Disciplina> disciplinaCodigoEquivalencia(String codigo) {	

		List<Disciplina> listaNomesSelecionados = new ArrayList<Disciplina>();
		List<DisciplinaGradeDisciplina> newList = new ArrayList<DisciplinaGradeDisciplina>(listaObrigatorias);
		newList.addAll(listaEletivas);
		for (DisciplinaGradeDisciplina disciplina:newList ){
			if (disciplina.getDisciplina().getCodigo().indexOf(codigo.toUpperCase()) >= 0){
				listaNomesSelecionados.add(disciplina.getDisciplina());
			}
		}
		codigo = codigo.toUpperCase();
		return listaNomesSelecionados;
	}

	public void limparDisciplina(){

		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();
		lgCodigoDisciplina = false;
		lgNomeDisciplina = false;	
		lgCargaHorariaDisciplina = false;

	}

	public void incluirDisciplinaNova(){

		disciplinaNova.setCodigo(disciplinaNova.getCodigo().toUpperCase());
		disciplinaNova.setNome(disciplinaNova.getNome().toUpperCase());
		disciplinaNova.setCodigo(disciplinaNova.getCodigo().trim());
		disciplinaNova.setNome(disciplinaNova.getNome().trim());
		Disciplina disciplinaExiste = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaNova.getCodigo());
		if (disciplinaNova.getCodigo() == ""){
			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		if (disciplinaNova.getNome() == ""){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		if (disciplinaNova.getCargaHoraria() == null){
			FacesMessage msg = new FacesMessage("Preencha o campo Carga Horária!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		if (disciplinaExiste != null){
			FacesMessage msg = new FacesMessage("Disciplina já existe");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		else {
			disciplinaDAO.persistir(disciplinaNova);
			FacesMessage msg = new FacesMessage("Disciplina cadastrada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaNova =  new Disciplina();
		}
	}

	public void onItemSelectCodigoDisciplina() {
		disciplina = disciplinaDAO.buscarPorCodigoDisciplina(disciplina.getCodigo());
		lgCodigoDisciplina = true;
		lgNomeDisciplina = true;	
		lgCargaHorariaDisciplina = true;
	}

	public void onItemSelectCodigoNome() {
		Disciplina disciplinaAux = disciplinaDAO.buscarPorCodigoDisciplina(disciplina.getCodigo());
		if (disciplinaAux != null){
			disciplina = disciplinaAux;
		}
		lgCodigoDisciplina = true;
	}

	public void onItemSelectNomeDisciplina() {

		lgCodigoDisciplina = true;
		lgNomeDisciplina = true;	
		lgCargaHorariaDisciplina = true;	
	}

	public void onItemSelectCodigoDisciplinaEquivalenciaUm() {

		lgCodigoDisciplinaEquivalenciaUm = true;
		lgNomeDisciplinaEquivalenciaUm = true;	
	}

	public void onItemSelectNomeDisciplinaEquivalenciaUm() {

		lgCodigoDisciplinaEquivalenciaUm = true;
		lgNomeDisciplinaEquivalenciaUm = true;	
	}

	public void onItemSelectDisciplinaIra() {

		lgDisciplinaIra = true;
	}

	public void onItemSelectCodigoDisciplinaEquivalenciaDois() {

		disciplinaEquivalenciaDois = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaEquivalenciaDois.getCodigo());
		lgCodigoDisciplinaEquivalenciaDois = true;
		lgNomeDisciplinaEquivalenciaDois = true;	
	}

	public void onItemSelectNomeDisciplinaEquivalenciaDois() {

		lgCodigoDisciplinaEquivalenciaDois = true;
		lgNomeDisciplinaEquivalenciaDois = true;	
	}

	public void incluirGradeDisciplina(){

		if (disciplina.getNome() == null){

			disciplina.setNome("");

		}

		if(disciplina.getCodigo().equals("")){

			FacesMessage msg = new FacesMessage("Preencha o campo Código Disciplina !");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getId() == null){

			if (disciplina.getNome() == null || disciplina.getNome().equals("")){

				FacesMessage msg = new FacesMessage("Preencha o campo Nome Disciplina !");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}

		if(gradeDisciplina.getTipoDisciplina() == ""){

			FacesMessage msg = new FacesMessage("Preencha o campo Tipo Disciplina!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if(gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") && gradeDisciplina.getPeriodo() == 0){

			FacesMessage msg = new FacesMessage("Preencha o campo Período!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}

		gradeDisciplina.setDisciplina(disciplina);
		gradeDisciplina.setGrade(grade);	
		List<GradeDisciplina> todos = gradeDisciplinaDAO.buscarTodasGradeDisciplinaPorGrade(grade.getId());

		for (GradeDisciplina g:todos){
			if(g.getDisciplina().getCodigo().equals(disciplina.getCodigo()) ){
				FacesMessage msg = new FacesMessage("Disciplina já cadastrada nesta grade!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}

		if (!gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") ){			
			gradeDisciplina.setPeriodo((long) 0);
		}

		gradeDisciplina.setExcluirIra(false);
		gradeDisciplinaDAO.persistir(gradeDisciplina);	
		DisciplinaGradeDisciplina disciplinaGradeDisciplina  = new DisciplinaGradeDisciplina();
		disciplinaGradeDisciplina.setDisciplina(disciplina);
		disciplinaGradeDisciplina.setGradeDisciplina(gradeDisciplina);	

		if (gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") ){
			listaObrigatorias.add(disciplinaGradeDisciplina);
		}

		else {
			listaEletivas.add(disciplinaGradeDisciplina);
		}

		ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaObrigatorias);
		ordenar.DisciplinaGradeDisciplinaOrdenarPeriodo(listaObrigatorias);
		ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaEletivas);

		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();

		lgCodigoDisciplina = false;
		lgNomeDisciplina = false;	
		lgCargaHorariaDisciplina = false;	
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridObrigatorias");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEletivas");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEquivalencias");
		dataTable.clearInitialState();
		dataTable.reset();	
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridIra");
		dataTable.clearInitialState();
		dataTable.reset();
		
		//usuarioController.atualizarPessoaLogada();
	}	

	public void deletarGradeDisciplina(){

		gradeDisciplinaDAO.removePeloId(linhaSelecionada.getGradeDisciplina().getId());
		listaEletivas.remove(linhaSelecionada);
		listaObrigatorias.remove(linhaSelecionada);
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		
		//usuarioController.atualizarPessoaLogada();
		atualizaGrids();
	}

	public void atualizaGrids(){

		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridObrigatorias");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEletivas");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEquivalencias");
		dataTable.clearInitialState();
		dataTable.reset();	
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridIra");
		dataTable.clearInitialState();
		dataTable.reset();
		listaEquivalencia = new ArrayList<Equivalencia>();			
		listaEletivas = new ArrayList<DisciplinaGradeDisciplina>();
		listaObrigatorias = new ArrayList<DisciplinaGradeDisciplina>();
		//listaEquivalencia = equivalenciaDAO.buscarTodasEquivalenciasPorGrade(grade.getId());
		listaEquivalencia = equivalenciaDAO.buscarTodasEquivalenciasPorGrade(grade.getId());
		listaIra = new ArrayList<GradeDisciplina>();

		ArrayList<GradeDisciplina> todos = gradeDisciplinaDAO.buscarTodasGradeDisciplinaPorGrade(grade.getId());
		//List<GradeDisciplina> todos = grade.getGrupoGradeDisciplina();	
		for(GradeDisciplina gradeDisciplinaSelecionada : todos){
			if(gradeDisciplinaSelecionada.getExcluirIra() != null && gradeDisciplinaSelecionada.getExcluirIra() == true){		
				listaIra.add(gradeDisciplinaSelecionada);
			}
		}
		//listaIra = gradeDisciplinaDAO.buscarPorIra(grade.getId(), true);
		while(!todos.isEmpty()){ 
			DisciplinaGradeDisciplina disciplinaGradeDisciplina  = new DisciplinaGradeDisciplina();
			disciplinaGradeDisciplina.setDisciplina(todos.get(0).getDisciplina());
			disciplinaGradeDisciplina.setGradeDisciplina(todos.get(0));	
			if (todos.get(0).getTipoDisciplina().equals("Obrigatoria")){
				listaObrigatorias.add(disciplinaGradeDisciplina);
			}
			else{
				listaEletivas.add(disciplinaGradeDisciplina);
			}
			ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaObrigatorias);
			ordenar.DisciplinaGradeDisciplinaOrdenarPeriodo(listaObrigatorias);
			ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaEletivas);
			todos.remove(0);
		}
		
		listaEletivasSelecionada =  null;
	    listaObrigatoriasSelecionada  =  null;
	    listaIraSelecionados = null;
		
	}

	public void incluiPreRequisitos (){
		
		
		PreRequisito preRequisito  = new PreRequisito();

		preRequisito  = new PreRequisito();

		disciplinaPre = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaPre.getCodigo());

		preRequisito.setDisciplina(disciplinaPre);

		preRequisito.setGradeDisciplina(linhaSelecionada.getGradeDisciplina());

		preRequisito.setTipo(tipoPre);

		if ( disciplinaPre == null || disciplinaPre.getCodigo() == "" ){

			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;



		}

		if (tipoPre == ""){

			FacesMessage msg = new FacesMessage("Selecione o Tipo de Pré-Requsito!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		long idPreprocurado = preRequisitoDAO.buscarPorDisciplanaGradeId(linhaSelecionada.getGradeDisciplina().getId(), disciplinaPre.getId());

		if(idPreprocurado != 0){

			FacesMessage msg = new FacesMessage("Pre-Requisito já cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaPre = new Disciplina();
			tipoPre = "";
			return;
		}


		if(disciplinaPre.getId() == linhaSelecionada.getDisciplina().getId()){

			FacesMessage msg = new FacesMessage("Não é possível incluir como pré-requisito a própria disciplina!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaPre = new Disciplina();
			tipoPre = "";
			return;
		}

		preRequisitoDAO.persistir(preRequisito);

		listaPreRequisitos.add(preRequisito);

		disciplinaPre = new Disciplina();
		
		carregaPreRequisitos();
		
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);

		
		


	}

	public void carregaPreRequisitos(){

		listaPreRequisitos = new ArrayList<PreRequisito>();
		String pre = "";		
		
		ArrayList<PreRequisito> todos = preRequisitoDAO.buscarPorTodosCodigoGradeDisc(linhaSelecionada.getGradeDisciplina().getId());
		while(!todos.isEmpty()){  
			pre = pre + todos.get(0).getDisciplina().getCodigo() + " : ";
			listaPreRequisitos.add(todos.remove(0));
		}
		linhaSelecionada.getGradeDisciplina().setPreRequisitos(pre);


	}

	public void deletarPreRequisito(){

		preRequisitoDAO.removePeloId(linhaSelecionadaPreRequisto.getId());
		List<PreRequisito> todos = linhaSelecionada.getGradeDisciplina().getPreRequisito();
		listaPreRequisitos.clear();
		for(PreRequisito p:todos){
			listaPreRequisitos.add(p);
		}

		carregaPreRequisitos();
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		//usuarioController.atualizarPessoaLogada();
		
	}

	public void incluirDisciplinaIra() {
		GradeDisciplina gradeDiscIra = null;		
		boolean achouDisciplinaIra = false;
		
		if (disciplinaIra.getCodigo() == null || disciplinaIra.getCodigo().equals("")){
			
			FacesMessage msg = new FacesMessage("Insira uma disciplina para ser ignorada no calculo do IRA!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
		}
		
		for(GradeDisciplina gradeDisciplinaSelecionada : grade.getGrupoGradeDisciplina()){				
			if (gradeDisciplinaSelecionada.getDisciplina().getCodigo().equals(disciplinaIra.getCodigo()) && gradeDisciplinaSelecionada.getExcluirIra() == true){					
				achouDisciplinaIra = true;					
			}
			else if (gradeDisciplinaSelecionada.getDisciplina().getCodigo().equals(disciplinaIra.getCodigo()) && gradeDisciplinaSelecionada.getExcluirIra() == false){					
				gradeDiscIra = gradeDisciplinaSelecionada;		

			}			
		}		

		
		
		if (achouDisciplinaIra == true){
			FacesMessage msg = new FacesMessage("Esta Disciplina já foi incluída!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		gradeDiscIra.setExcluirIra(true);
		gradeDisciplinaDAO.editar(gradeDiscIra);
		listaIra.add(gradeDiscIra);
		disciplinaIra = new Disciplina();
		lgDisciplinaIra = false;
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		//usuarioController.atualizarPessoaLogada();
		
	}

	public void incluiEquivalencia(){
		Equivalencia equivalencia = new Equivalencia();
		equivalencia.setDisciplinaGrade(disciplinaEquivalenciaUm);
		equivalencia.setDisciplinaEquivalente(disciplinaEquivalenciaDois);
		equivalencia.setGrade(grade);
		Equivalencia equivalenciaAuxiliar;
		equivalenciaAuxiliar = null;
		
		if (disciplinaEquivalenciaUm.getCodigo() == null || disciplinaEquivalenciaUm.getCodigo().equals("") || disciplinaEquivalenciaDois.getCodigo() == null || disciplinaEquivalenciaDois.getCodigo().equals("")){
			FacesMessage msg = new FacesMessage("Complete os dados da equivalência!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
			
		}
		
		
		for (Equivalencia equivalenciaQuestao : grade.getGrupoEquivalencia()){
			if (equivalenciaQuestao.getDisciplinaEquivalente().getCodigo().equals( disciplinaEquivalenciaUm.getCodigo())  && equivalenciaQuestao.getDisciplinaGrade().getCodigo().equals(disciplinaEquivalenciaDois.getCodigo())){
				equivalenciaAuxiliar = equivalenciaQuestao;
				break;
			}
		}
				
		if (equivalenciaAuxiliar != null){
			FacesMessage msg = new FacesMessage("Equivalncia já existe!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaEquivalenciaUm = new Disciplina();
			disciplinaEquivalenciaDois = new Disciplina();
			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaDois = false;	
			lgCodigoDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaUm = false;	
			return;
		}

		if(disciplinaEquivalenciaDois.getCodigo().equals(disciplinaEquivalenciaUm.getCodigo()) ){
			FacesMessage msg = new FacesMessage("Não possível incluir equivalncia da própria disciplina!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaEquivalenciaUm = new Disciplina();
			disciplinaEquivalenciaDois = new Disciplina();
			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaDois = false;	
			lgCodigoDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaUm = false;	
			return;
		}

		
		equivalenciaDAO.persistir(equivalencia);	
		listaEquivalencia.add(equivalencia);
		disciplinaEquivalenciaUm = new Disciplina();
		disciplinaEquivalenciaDois = new Disciplina();
		lgCodigoDisciplinaEquivalenciaDois = false;
		lgNomeDisciplinaEquivalenciaDois = false;	
		lgCodigoDisciplinaEquivalenciaUm = false;
		lgNomeDisciplinaEquivalenciaUm = false;	
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		//usuarioController.atualizarPessoaLogada();
		

	}

	public void limpaDisciplinaIra(){
		disciplinaIra = new Disciplina();
		lgDisciplinaIra = false;
	}

	public void limpaEquivalencia(){

		disciplinaEquivalenciaUm = new Disciplina();
		disciplinaEquivalenciaDois = new Disciplina();
		lgCodigoDisciplinaEquivalenciaDois = false;
		lgNomeDisciplinaEquivalenciaDois = false;	
		lgCodigoDisciplinaEquivalenciaUm = false;
		lgNomeDisciplinaEquivalenciaUm = false;	

	}

	public void deletarEquivalencia(){		

		equivalenciaDAO.removePeloId(linhaSelecionadaEquivalencia.getId());
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		listaEquivalencia.remove(linhaSelecionadaEquivalencia);
		//usuarioController.atualizarPessoaLogada();
		
		Class c = null;
		c = ClassFactory.getClass(grade.getCurso().getCodigo(),grade.getCodigo(), linhaSelecionadaEquivalencia.getDisciplinaGrade().getCodigo(),linhaSelecionadaEquivalencia.getDisciplinaGrade().getCargaHoraria().toString());
		ClassFactory.removeClass(grade.getCurso().getCodigo(),grade.getCodigo(), linhaSelecionadaEquivalencia.getDisciplinaEquivalente().getCodigo(), c);
		
		
		
		
		atualizaGrids();		

	}
	
	public void deletarDisciplinaIra(){		
		listaIra.remove(gradeDisciplinaIraSelecionada);
		gradeDisciplinaIraSelecionada.setExcluirIra(false);
		gradeDisciplinaDAO.editar(gradeDisciplinaIraSelecionada);
		estruturaArvore.removerEstrutura(grade);
		usuarioController.setReseta(true);
		//usuarioController.atualizarPessoaLogada();
		
		
	}

	//========================================================= GET - SET ==================================================================================//

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public boolean isLgHorasEletivas() {
		return lgHorasEletivas;
	}

	public void setLgHorasEletivas(boolean lgHorasEletivas) {
		this.lgHorasEletivas = lgHorasEletivas;
	}

	public boolean isLgHorasOpcionais() {
		return lgHorasOpcionais;
	}

	public void setLgHorasOpcionais(boolean lgHorasOpcionais) {
		this.lgHorasOpcionais = lgHorasOpcionais;
	}

	public boolean isLgHorasAoe() {
		return lgHorasAoe;
	}

	public void setLgHorasAoe(boolean lgHorasAoe) {
		this.lgHorasAoe = lgHorasAoe;
	}

	public boolean isLgIncluirGrade() {
		return lgIncluirGrade;
	}

	public void setLgIncluirGrade(boolean lgIncluirGrade) {
		this.lgIncluirGrade = lgIncluirGrade;
	}

	public boolean isLgCodigoGrade() {
		return lgCodigoGrade;
	}

	public void setLgCodigoGrade(boolean lgCodigoGrade) {
		this.lgCodigoGrade = lgCodigoGrade;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public boolean isLgCodigoDisciplina() {
		return lgCodigoDisciplina;
	}

	public void setLgCodigoDisciplina(boolean lgCodigoDisciplina) {
		this.lgCodigoDisciplina = lgCodigoDisciplina;
	}

	public boolean isLgNomeDisciplina() {
		return lgNomeDisciplina;
	}

	public void setLgNomeDisciplina(boolean lgNomeDisciplina) {
		this.lgNomeDisciplina = lgNomeDisciplina;
	}

	public boolean isLgPeriodoDisciplina() {
		return lgPeriodoDisciplina;
	}

	public void setLgPeriodoDisciplina(boolean lgPeriodoDisciplina) {
		this.lgPeriodoDisciplina = lgPeriodoDisciplina;
	}

	public boolean isLgCargaHorariaDisciplina() {
		return lgCargaHorariaDisciplina;
	}

	public void setLgCargaHorariaDisciplina(boolean lgCargaHorariaDisciplina) {
		this.lgCargaHorariaDisciplina = lgCargaHorariaDisciplina;
	}

	public boolean isLgTipoDisciplina() {
		return lgTipoDisciplina;
	}

	public void setLgTipoDisciplina(boolean lgTipoDisciplina) {
		this.lgTipoDisciplina = lgTipoDisciplina;
	}

	public GradeDisciplina getGradeDisciplina() {
		return gradeDisciplina;
	}

	public void setGradeDisciplina(GradeDisciplina gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isLgIncluirDisciplina() {
		return lgIncluirDisciplina;
	}

	public void setLgIncluirDisciplina(boolean lgIncluirDisciplina) {
		this.lgIncluirDisciplina = lgIncluirDisciplina;
	}

	public boolean isLgLimparDisciplina() {
		return lgLimparDisciplina;
	}

	public void setLgLimparDisciplina(boolean lgLimparDisciplina) {
		this.lgLimparDisciplina = lgLimparDisciplina;
	}

	public List<DisciplinaGradeDisciplina> getListaObrigatorias() {
		return listaObrigatorias;
	}

	public void setListaObrigatorias(
			List<DisciplinaGradeDisciplina> listaObrigatorias) {
		this.listaObrigatorias = listaObrigatorias;
	}

	public List<DisciplinaGradeDisciplina> getListaEletivas() {
		return listaEletivas;
	}

	public void setListaEletivas(List<DisciplinaGradeDisciplina> listaEletivas) {
		this.listaEletivas = listaEletivas;
	}

	public DisciplinaGradeDisciplina getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public void setLinhaSelecionada(DisciplinaGradeDisciplina linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}

	public Disciplina getDisciplinaPre() {
		return disciplinaPre;
	}

	public void setDisciplinaPre(Disciplina disciplinaPre) {
		this.disciplinaPre = disciplinaPre;
	}

	public List<PreRequisito> getListaPreRequisitos() {
		return listaPreRequisitos;
	}

	public void setListaPreRequisitos(List<PreRequisito> listaPreRequisitos) {
		this.listaPreRequisitos = listaPreRequisitos;
	}

	public List<Equivalencia> getListaEquivalencia() {
		return listaEquivalencia;
	}

	public void setListaEquivalencia(List<Equivalencia> listaEquivalencia) {
		this.listaEquivalencia = listaEquivalencia;
	}

	public Disciplina getDisciplinaEquivalenciaUm() {
		return disciplinaEquivalenciaUm;
	}

	public void setDisciplinaEquivalenciaUm(Disciplina disciplinaEquivalenciaUm) {
		this.disciplinaEquivalenciaUm = disciplinaEquivalenciaUm;
	}

	public Disciplina getDisciplinaEquivalenciaDois() {
		return disciplinaEquivalenciaDois;
	}

	public void setDisciplinaEquivalenciaDois(Disciplina disciplinaEquivalenciaDois) {
		this.disciplinaEquivalenciaDois = disciplinaEquivalenciaDois;
	}

	public boolean isLgCodigoDisciplinaEquivalenciaUm() {
		return lgCodigoDisciplinaEquivalenciaUm;
	}

	public void setLgCodigoDisciplinaEquivalenciaUm(
			boolean lgCodigoDisciplinaEquivalenciaUm) {
		this.lgCodigoDisciplinaEquivalenciaUm = lgCodigoDisciplinaEquivalenciaUm;
	}

	public boolean isLgCodigoDisciplinaEquivalenciaDois() {
		return lgCodigoDisciplinaEquivalenciaDois;
	}

	public void setLgCodigoDisciplinaEquivalenciaDois(
			boolean lgCodigoDisciplinaEquivalenciaDois) {
		this.lgCodigoDisciplinaEquivalenciaDois = lgCodigoDisciplinaEquivalenciaDois;
	}

	public boolean isLgNomeDisciplinaEquivalenciaUm() {
		return lgNomeDisciplinaEquivalenciaUm;
	}

	public void setLgNomeDisciplinaEquivalenciaUm(
			boolean lgNomeDisciplinaEquivalenciaUm) {
		this.lgNomeDisciplinaEquivalenciaUm = lgNomeDisciplinaEquivalenciaUm;
	}

	public boolean isLgNomeDisciplinaEquivalenciaDois() {
		return lgNomeDisciplinaEquivalenciaDois;
	}

	public void setLgNomeDisciplinaEquivalenciaDois(
			boolean lgNomeDisciplinaEquivalenciaDois) {
		this.lgNomeDisciplinaEquivalenciaDois = lgNomeDisciplinaEquivalenciaDois;
	}

	public boolean isLgIncluirEquivalencia() {
		return lgIncluirEquivalencia;
	}

	public void setLgIncluirEquivalencia(boolean lgIncluirEquivalencia) {
		this.lgIncluirEquivalencia = lgIncluirEquivalencia;
	}

	public boolean isLgLimparEquivalencia() {
		return lgLimparEquivalencia;
	}

	public void setLgLimparEquivalencia(boolean lgLimparEquivalencia) {
		this.lgLimparEquivalencia = lgLimparEquivalencia;
	}

	public PreRequisito getLinhaSelecionadaPreRequisto() {
		return linhaSelecionadaPreRequisto;
	}

	public void setLinhaSelecionadaPreRequisto(
			PreRequisito linhaSelecionadaPreRequisto) {
		this.linhaSelecionadaPreRequisto = linhaSelecionadaPreRequisto;
	}

	public GradeDAO getGradeDAO() {
		return gradeDAO;
	}

	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	public Equivalencia getLinhaSelecionadaEquivalencia() {
		return linhaSelecionadaEquivalencia;
	}

	public void setLinhaSelecionadaEquivalencia(
			Equivalencia linhaSelecionadaEquivalencia) {
		this.linhaSelecionadaEquivalencia = linhaSelecionadaEquivalencia;
	}

	public GradeDisciplinaDAO getGradeDisciplinaDAO() {
		return gradeDisciplinaDAO;
	}

	public void setGradeDisciplinaDAO(GradeDisciplinaDAO gradeDisciplinaDAO) {
		this.gradeDisciplinaDAO = gradeDisciplinaDAO;
	}

	public DisciplinaDAO getDisciplinaDAO() {
		return disciplinaDAO;
	}

	public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
		this.disciplinaDAO = disciplinaDAO;
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

	public UsuarioController getUsuarioController() {
		return usuarioController;
	}

	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}


	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getTipoPre() {
		return tipoPre;
	}

	public void setTipoPre(String tipoPre) {
		this.tipoPre = tipoPre;
	}

	public boolean isLgExcluirGrade() {
		return lgExcluirGrade;
	}

	public void setLgExcluirGrade(boolean lgExcluirGrade) {
		this.lgExcluirGrade = lgExcluirGrade;
	}

	public boolean isLgMaxPeriodo() {
		return lgMaxPeriodo;
	}

	public void setLgMaxPeriodo(boolean lgMaxPeriodo) {
		this.lgMaxPeriodo = lgMaxPeriodo;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public Disciplina getDisciplinaNova() {
		return disciplinaNova;
	}

	public void setDisciplinaNova(Disciplina disciplinaNova) {
		this.disciplinaNova = disciplinaNova;
	}

	public List<Equivalencia> getListaEquivalenciaSelecionada() {
		return listaEquivalenciaSelecionada;
	}

	public void setListaEquivalenciaSelecionada(
			List<Equivalencia> listaEquivalenciaSelecionada) {
		this.listaEquivalenciaSelecionada = listaEquivalenciaSelecionada;
	}

	public List<DisciplinaGradeDisciplina> getListaObrigatoriasSelecionada() {
		return listaObrigatoriasSelecionada;
	}

	public void setListaObrigatoriasSelecionada(
			List<DisciplinaGradeDisciplina> listaObrigatoriasSelecionada) {
		this.listaObrigatoriasSelecionada = listaObrigatoriasSelecionada;
	}

	public List<DisciplinaGradeDisciplina> getListaEletivasSelecionada() {
		return listaEletivasSelecionada;
	}

	public void setListaEletivasSelecionada(
			List<DisciplinaGradeDisciplina> listaEletivasSelecionada) {
		this.listaEletivasSelecionada = listaEletivasSelecionada;
	}
	public Disciplina getDisciplinaIra() {
		return disciplinaIra;
	}
	public void setDisciplinaIra(Disciplina disciplinaIra) {
		this.disciplinaIra = disciplinaIra;
	}
	public boolean isLgDisciplinaIra() {
		return lgDisciplinaIra;
	}
	public void setLgDisciplinaIra(boolean lgDisciplinaIra) {
		this.lgDisciplinaIra = lgDisciplinaIra;
	}
	public List<GradeDisciplina> getListaIra() {
		return listaIra;
	}
	public void setListaIra(List<GradeDisciplina> listaIra) {
		this.listaIra = listaIra;
	}
	public List<GradeDisciplina> getListaIraSelecionados() {
		return listaIraSelecionados;
	}
	public void setListaIraSelecionados(List<GradeDisciplina> listaIraSelecionados) {
		this.listaIraSelecionados = listaIraSelecionados;
	}
	public GradeDisciplina getGradeDisciplinaIraSelecionada() {
		return gradeDisciplinaIraSelecionada;
	}
	public void setGradeDisciplinaIraSelecionada(
			GradeDisciplina gradeDisciplinaIraSelecionada) {
		this.gradeDisciplinaIraSelecionada = gradeDisciplinaIraSelecionada;
	}




}
