package controller.backend;

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

import model.Aluno;
import model.Curso;
import model.EventoAce;
import model.Grade;
import model.arvore.Class;
import model.arvore.ClassStatus;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;
import model.estrutura.ElementoGrafico;
import model.estrutura.GrupoElementoGrafico;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import controller.util.EstruturaArvore;
import controller.util.ImportarArvore;
import controller.util.Ordenar;
import controller.util.UsuarioController;
import dao.Interface.AlunoDAO;



@Named
@ViewScoped
public class GraficosPanoramicoController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private float ira;
	private AlunoDAO alunoDAO ;
	private Curso curso = new Curso();
	private int horasObrigatorias;
	private int horasAceConcluidas;	
	private int horasEletivasConcluidas;
	private int horasOpcionaisConcluidas;
	private int horasObrigatoriasConcluidas;
	private List<String> listaCores = new ArrayList<String>();
	private Aluno alunoSelecionado = new Aluno();
	private boolean lgNomeAluno  = false;
	private boolean lgMatriculaAluno = false;
	private String semestreSelecionado;
	private boolean lgObrigatorias = true;
	private boolean lgOEletivas = true;
	private boolean lgOpcionais = true;
	private boolean lgAce = true;
	private LineChartModel  lineChartModel;
	private List<GrupoElementoGrafico> listaGrupoElementoGrafico = new ArrayList<GrupoElementoGrafico>();
	private List<String> listaGrades = new ArrayList<String>();
	private String cores = "";
	private String coreSelecionada = "";
	private Ordenar ordenar = new Ordenar();
	private int periodoAtual;
	private boolean liberaSelecao = false;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();
		
		alunoDAO =  estruturaArvore.getAlunoDAO();
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		
		listaCores.add("58BA27");
		listaCores.add("FFCC33");
		listaCores.add("F74A4A");
		listaCores.add("F52F2F");
		listaCores.add("A30303");
		listaCores.add("7B5858");
		listaCores.add("F1A413");
		listaCores.add("8E7E7E");		
		createBubbleModels();

		//#FF0000
	}

	public void onItemSelectMatriculaAluno()  {
		//alunoSelecionado = alunoDAO.buscarPorMatricula(alunoSelecionado.getMatricula());
		
		for (Aluno alunoQuestao : curso.getGrupoAlunos()){
			if(alunoQuestao.getMatricula().contains(alunoSelecionado.getMatricula())){
				alunoSelecionado = alunoQuestao;
				break;
			}
		}
		
		
	}	

	public void limpaAluno(){		
		lgNomeAluno  = false;
		lgMatriculaAluno = false;
		alunoSelecionado = new Aluno();
	}

	public void createBubbleModels(){
		lineChartModel = initBubbleModel();
		lineChartModel.setTitle("Gráfico Panorâmico por Grade");
		lineChartModel.getAxis(AxisType.X).setLabel("Quantidade Horas Concluídas");
		lineChartModel.setZoom(true);
		lineChartModel.setLegendPosition("ne");
		lineChartModel.setSeriesColors(cores);
		lineChartModel.setDatatipFormat("%1$d");
		
		Axis xAxis = lineChartModel.getAxis(AxisType.X);
		xAxis.setMin(0);
		Axis yAxis = lineChartModel.getAxis(AxisType.Y);
		yAxis.setLabel("Período");
		yAxis.setTickFormat("%d");
		xAxis.setTickFormat("%d");
		yAxis.setMin(0);
	}

	public ElementoGrafico retornaListaElemento(Integer grade,Integer qtdHoras){

		for(GrupoElementoGrafico grupoElementoGraficoQuestao : listaGrupoElementoGrafico){
			if (grupoElementoGraficoQuestao.getGrade().equals(grade)){
				return buscaElementoGrafico(grupoElementoGraficoQuestao,qtdHoras);			
			}		
		}

		GrupoElementoGrafico grupoElementoGraficoNovo = new GrupoElementoGrafico();
		grupoElementoGraficoNovo.setGrade(grade);
		listaGrupoElementoGrafico.add(grupoElementoGraficoNovo);
		return buscaElementoGrafico(grupoElementoGraficoNovo,qtdHoras);	
	}

	public ElementoGrafico buscaElementoGrafico (GrupoElementoGrafico grupoElementoGrafico , Integer qtdHoras){

		for (ElementoGrafico elementoGraficoQuestao : grupoElementoGrafico.getListaElementoGrafico()){
			if (elementoGraficoQuestao.getNumero().equals(qtdHoras)){
				return elementoGraficoQuestao;			
			}			
		}

		ElementoGrafico elementoGraficoNovo = new ElementoGrafico();
		elementoGraficoNovo.setNumero(qtdHoras);
		grupoElementoGrafico.getListaElementoGrafico().add(elementoGraficoNovo);
		return elementoGraficoNovo;

	}

	private void GerarTodosDados(){

		listaGrupoElementoGrafico = new ArrayList<GrupoElementoGrafico>();
		listaGrades = new ArrayList<String>();
		for(Grade grade : curso.getGrupoGrades()){
			if (!grade.estaCompleta()) continue;

			listaGrades.add(grade.getCodigo().substring(1,5));
			importador = estruturaArvore.recuperarArvore(grade,true);
			for(Aluno aluno : grade.getGrupoAlunos()){

				horasAceConcluidas = 0;
				horasObrigatorias = 0;
				horasObrigatoriasConcluidas = 0;
				horasOpcionaisConcluidas = 0;
				horasEletivasConcluidas = 0;

				/*listaEventosAce = estruturaArvore.getEventosAceDAO().buscarPorMatricula(aluno.getMatricula());//aluno.getListaEventosAce();

				if (listaEventosAce != null){
					for (EventoAce evento :listaEventosAce){
						horasAceConcluidas = (int) (horasAceConcluidas + evento.getHoras());
					}
				}*/
				
				horasAceConcluidas = estruturaArvore.getEventosAceDAO().recuperarHorasConcluidasPorMatricula(aluno.getMatricula());

				/**************************** DADOS ALUNO *******************/
				StudentsHistory sh = importador.getSh();
				curriculum = importador.get_cur();
				Student st = sh.getStudents().get(aluno.getMatricula());
				
				if (st == null){

					System.out.println("Historico não encontrado para o aluno :" + aluno.getMatricula());				
					continue;

				}		
				
				//gerarDadosAluno(st,curriculum);
				horasAceConcluidas += aluno.getHorasAceConcluidas() + aluno.getSobraHorasOpcionais();
				horasObrigatoriasConcluidas = aluno.getHorasObrigatoriasCompletadas();
				horasEletivasConcluidas = aluno.getHorasEletivasCompletadas();
				horasOpcionaisConcluidas = aluno.getHorasOpcionaisCompletadas();
				horasObrigatorias = aluno.getGrade().getHorasObrigatorias();

				ira = st.getIRA();
				
				if (alunoSelecionado.getMatricula()== null || alunoSelecionado.getMatricula().equals("")){					
					liberaSelecao = true;					
				}
				

				if (aluno.getMatricula().equals(alunoSelecionado.getMatricula()) && ira == -1){
					FacesMessage msg = new FacesMessage("Aluno Selecionado não tem horas concluídas");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					liberaSelecao = true;
					continue;
				}


				if (ira == -1) continue;

				/*************************************** *******************/


				int totalConcluido = 0;
				if(lgObrigatorias == false){
					horasObrigatoriasConcluidas = 0;
				}
				if(lgOEletivas == false){
					horasEletivasConcluidas = 0;
				}
				if(lgOpcionais == false){
					horasOpcionaisConcluidas = 0;
				}
				if(lgAce == false){
					horasAceConcluidas = 0;
				}

				//int periodoAluno = aluno.getPeriodoCorrente(usuarioController.getSemestreSelecionado());
				totalConcluido = horasObrigatoriasConcluidas + horasOpcionaisConcluidas + horasEletivasConcluidas + horasAceConcluidas;	

				if (aluno.getMatricula().equals(alunoSelecionado.getMatricula()) && liberaSelecao == false){
					periodoAtual = alunoSelecionado.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());
					alunoSelecionado.setPeriodoReal(totalConcluido);
					continue;
				}

				retornaListaElemento(Integer.valueOf(aluno.getMatricula().substring(0,4)),totalConcluido).getListaAluno().add(aluno);
			}
		}
		
		java.util.Collections.sort(listaGrades);
		ordenar.periodoGrupoElementoGrafico(listaGrupoElementoGrafico);
	}

	private LineChartModel initBubbleModel(){

		LineChartModel  model = new LineChartModel ();
		
		liberaSelecao = false;

		GerarTodosDados();

		int corAtual = 0;
		cores = "";

		for (GrupoElementoGrafico grupoElementoGraficoQuestao : listaGrupoElementoGrafico){

			ordenar.periodoElementoGrafico(grupoElementoGraficoQuestao.getListaElementoGrafico());

			coreSelecionada = "";
			int interador = 0;
			for (String gradeEstrutura : listaGrades){

				int gradeQuestao = Integer.valueOf(gradeEstrutura);
				int gradeTurma = grupoElementoGraficoQuestao.getGrade(); 

				if (gradeTurma >= gradeQuestao){
					coreSelecionada = listaCores.get(interador);
					interador ++ ;
				}
				else {	
					break;
				}				
			}

			if (coreSelecionada.equals("")){

				coreSelecionada = listaCores.get(0);

			}

			if (corAtual == 0){
				cores = coreSelecionada ;
				corAtual = 1;
			}

			else{
				cores = cores + "," + coreSelecionada ;
			}

			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel("Turma - " + String.valueOf(grupoElementoGraficoQuestao.getGrade()));


			for (ElementoGrafico elementoGraficoQuestao : grupoElementoGraficoQuestao.getListaElementoGrafico()){

				int periodoCorrente = elementoGraficoQuestao.getListaAluno().get(0).getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado());				
				series1.set(elementoGraficoQuestao.getNumero(), periodoCorrente);
				series1.setShowLine(false);
			}

			model.addSeries(series1);

		}


		//aki
		if (alunoSelecionado.getMatricula() != null && liberaSelecao == false){
			retornaListaElemento(Integer.valueOf(alunoSelecionado.getMatricula()),alunoSelecionado.getPeriodoReal()).getListaAluno().add(alunoSelecionado);

			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel("Aluno " + alunoSelecionado.getMatricula());
			series1.set( alunoSelecionado.getPeriodoReal(),periodoAtual);
			model.addSeries(series1);

			cores =  cores + ",FF0000" ;
		}



		int contadorGrade = 0;
		for(String gradeSelecao : listaGrades){

			for(Grade grade : curso.getGrupoGrades()){

				if(grade.getCodigo().length() > 4 && grade.getCodigo().substring(1,5).equals(gradeSelecao)){


					int horasObrigatoriasFinal = horasObrigatorias;
					int horasEletivas = grade.getHorasEletivas();
					int hotasOpcionais = grade.getHorasOpcionais();
					int horasAce = grade.getHorasAce();
					if(lgObrigatorias == false){
						horasObrigatoriasFinal = 0;
					}
					if(lgOEletivas == false){
						horasEletivas = 0;
					}
					if(lgOpcionais == false){
						hotasOpcionais = 0;
					}
					if(lgAce == false){
						horasAce = 0;
					}

					int valorTotalHorasGrade = horasObrigatoriasFinal + horasEletivas + hotasOpcionais + horasAce;
					int taxaAumento = valorTotalHorasGrade / grade.getNumeroMaximoPeriodos();

					LineChartSeries series1 = new LineChartSeries();
					series1.setLabel("Tendência Grade " + grade.getCodigo());
					series1.set( 0,1);
					series1.setShowMarker(false);
					series1.setDisableStack(true);

					int i;
					int valorAumento = taxaAumento;

					for(i=2;i<= grade.getNumeroMaximoPeriodos() + 1 ;i++){
						series1.set( valorAumento,i);	
						valorAumento = valorAumento + taxaAumento;
					}

					cores = cores + "," + listaCores.get(contadorGrade);
					contadorGrade ++;
					model.addSeries(series1);

				}
			}
		}


		return model;
	}

	public void gerarDadosAluno(Student st, Curriculum cur)	{
		HashMap<Class, ArrayList<String[]>> aprovado;
		horasObrigatorias = 0;
		horasObrigatoriasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasEletivasConcluidas = 0;
		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED)); 
		for(int i: cur.getMandatories().keySet()){
			for(Class c: cur.getMandatories().get(i)){
				horasObrigatorias = horasObrigatorias + c.getWorkload();
				if(aprovado.containsKey(c)) {
					horasObrigatoriasConcluidas = horasObrigatoriasConcluidas + c.getWorkload();
					aprovado.remove(c);
				}
			}	
		}
		int creditos = 0;
		for(Class c: cur.getElectives()){
			if(aprovado.containsKey(c))	{
				creditos += c.getWorkload();
				aprovado.remove(c);
			} 	
		}
		horasEletivasConcluidas = creditos;
		creditos = 0;
		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext()){
			Class c = i.next();
			ArrayList<String[]> classdata = aprovado.get(c);
			for(String[] s2: classdata)
			{
				if (s2[1].equals("APR") || s2[1].equals("A")){
					horasAceConcluidas = horasAceConcluidas + c.getWorkload();
				}
				else{
					creditos += c.getWorkload();
				}
			}
		}
		horasOpcionaisConcluidas = creditos;
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

	public void itemSelect(ItemSelectEvent event) {
		if (listaGrupoElementoGrafico.size() > event.getSeriesIndex()){

			for (Aluno alunoSelecionado :listaGrupoElementoGrafico.get(event.getSeriesIndex()).getListaElementoGrafico().get(event.getItemIndex() ).getListaAluno()){

				FacesMessage msg = new FacesMessage("Aluno Selecionado:" + alunoSelecionado.getMatricula() + " - " + alunoSelecionado.getNome());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
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

	public float getIra() {
		return ira;
	}

	public void setIra(float ira) {
		this.ira = ira;
	}

	public AlunoDAO getAlunoDAO() {
		return alunoDAO;
	}

	public void setAlunoDAO(AlunoDAO alunoDAO) {
		this.alunoDAO = alunoDAO;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int getHorasObrigatorias() {
		return horasObrigatorias;
	}

	public void setHorasObrigatorias(int horasObrigatorias) {
		this.horasObrigatorias = horasObrigatorias;
	}

	public int getHorasAceConcluidas() {
		return horasAceConcluidas;
	}

	public void setHorasAceConcluidas(int horasAceConcluidas) {
		this.horasAceConcluidas = horasAceConcluidas;
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

	public LineChartModel getLineChartModel() {
		return lineChartModel;
	}

	public void setLineChartModel(LineChartModel lineChartModel) {
		this.lineChartModel = lineChartModel;
	}

	public UsuarioController getUsuarioController() {
		return usuarioController;
	}

	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public List<String> getListaCores() {
		return listaCores;
	}

	public void setListaCores(List<String> listaCores) {
		this.listaCores = listaCores;
	}

	public Aluno getAlunoSelecionado() {
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Aluno alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}

	public boolean isLgNomeAluno() {
		return lgNomeAluno;
	}

	public void setLgNomeAluno(boolean lgNomeAluno) {
		this.lgNomeAluno = lgNomeAluno;
	}

	public boolean isLgMatriculaAluno() {
		return lgMatriculaAluno;
	}

	public void setLgMatriculaAluno(boolean lgMatriculaAluno) {
		this.lgMatriculaAluno = lgMatriculaAluno;
	}

	public String getSemestreSelecionado() {
		return semestreSelecionado;
	}

	public void setSemestreSelecionado(String semestreSelecionado) {
		this.semestreSelecionado = semestreSelecionado;
	}

	public boolean isLgObrigatorias() {
		return lgObrigatorias;
	}

	public void setLgObrigatorias(boolean lgObrigatorias) {
		this.lgObrigatorias = lgObrigatorias;
	}

	public boolean isLgOEletivas() {
		return lgOEletivas;
	}

	public void setLgOEletivas(boolean lgOEletivas) {
		this.lgOEletivas = lgOEletivas;
	}

	public boolean isLgOpcionais() {
		return lgOpcionais;
	}

	public void setLgOpcionais(boolean lgOpcionais) {
		this.lgOpcionais = lgOpcionais;
	}

	public boolean isLgAce() {
		return lgAce;
	}

	public void setLgAce(boolean lgAce) {
		this.lgAce = lgAce;
	}
}