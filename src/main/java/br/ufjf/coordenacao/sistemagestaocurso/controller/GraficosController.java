package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.AlunoSelecionado;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.ListaPeriodoAluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.PeriodoAluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.TotalizadorCurso;

import org.apache.log4j.Logger;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartModelRow;
import org.primefaces.extensions.component.gchart.model.GChartType;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.*;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.Ordenar;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
//import br.ufjf.coordenacao.sistemagestaocurso.repository.AlunoRepository;

@Named
@ViewScoped
public class GraficosController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;	
	
	private Curso curso = new Curso();
	private int maximoEncontrado;
	private int quantidadeSelecionados;
	private int periodoSelecionados;
	private int quantidadeTotal;
	private HorizontalBarChartModel animatedModel2;
	private Ordenar ordenar = new Ordenar();
	private GChartType chartType = GChartType.PIE;
	private GChartModel chartModel = null;
	private List<ListaPeriodoAluno> listaDados = new ArrayList<ListaPeriodoAluno>();
	private List<TotalizadorCurso> listaTotalizada = new ArrayList<TotalizadorCurso>();
	private List<AlunoSelecionado> listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
	private List<AlunoSelecionado> listaAlunoSelecionadoFiltrados ;
	private List<ColumnModel> columns;
	private boolean semColuna = true;
	
	private static final Logger logger = Logger.getLogger(GraficosController.class);

	/*@Inject
	private AlunoRepository alunoDAO ;*/
	
	//========================================================= METODOS ==================================================================================//

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init()  {
		
		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();
		//alunoDAO =  estruturaArvore.getAlunoDAO();
		curso = usuarioController.getAutenticacao().getCursoSelecionado();

		buscarDados();
		createAnimatedModels();
	}

	public void buscar(){
		init();		
	}



	private void createAnimatedModels() {
		animatedModel2 = initBarModel();
		animatedModel2.setTitle("Gráfico - Periodização do Aluno por Turma");
		animatedModel2.setLegendPosition("se");
		animatedModel2.setStacked(true);
		animatedModel2.setZoom(true);
		animatedModel2.setDatatipFormat("%1$d");
		Axis xAxis = animatedModel2.getAxis(AxisType.X);
		xAxis.setLabel("Quantidade Alunos");
		Axis yAxis = animatedModel2.getAxis(AxisType.Y);
		yAxis.setLabel("Turmas");
		yAxis.setMin(0);
		yAxis.setTickInterval("10");
		yAxis.setMax(20);
	}

	private HorizontalBarChartModel initBarModel() {
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		GChartModelBuilder gChartModelBuilder = new GChartModelBuilder();
		gChartModelBuilder.setChartType(getChartType()).addColumns("Periodo Incompleto", "Quantidade");
		listaDados = ordenar.PeriodoAlunoPorIngressoGeral(listaDados);
		columns = new ArrayList<ColumnModel>();
		int i;
		for (i=1;i<=maximoEncontrado;i++){
			columns.add(new ColumnModel(String.valueOf(i),(i-1)));
		}
		
		if (columns.size() == 0){
			
			semColuna = false;
		}
		
		
		columns.add(new ColumnModel("Total",(i-1)));
		ArrayList<Integer> listaTotalizadosAcumulados = new ArrayList<Integer>(5);
		i = 1;
		boolean naoEntrouPrimeira = false;
		while(i != (maximoEncontrado + 1)){
			int contadorAluno = 0;
			ChartSeries f = new ChartSeries();
			f.setLabel(String.valueOf(i) + "° - Período Incompleto");
			for(ListaPeriodoAluno listaPeriodoAlunoSelecionado : listaDados){
				ordenar.PeriodoAlunoPorPeriodoGeral(listaPeriodoAlunoSelecionado.getListaPeriodoAluno());
				boolean naoEntrou = false;
				List<Integer> listaTotalizados = new ArrayList<Integer>();
				int quantidadeAlunos = 0;
				int contador = 1;
				for (PeriodoAluno periodoAlunoSelecionado : listaPeriodoAlunoSelecionado.getListaPeriodoAluno()){
					if(periodoAlunoSelecionado.getPeriodoReal() == i ){
						contadorAluno = contadorAluno + periodoAlunoSelecionado.getListaAlunosPeriodo().size();
						f.set((listaPeriodoAlunoSelecionado.getIngressoAlunos()), periodoAlunoSelecionado.getListaAlunosPeriodo().size());
						naoEntrou = true;
						if(naoEntrouPrimeira == true) break;
					}
					if(naoEntrouPrimeira == false){
						while(contador != periodoAlunoSelecionado.getPeriodoReal()){
							listaTotalizados.add(0);
							contador++;
						}
						listaTotalizados.add(periodoAlunoSelecionado.getListaAlunosPeriodo().size());
						quantidadeAlunos = quantidadeAlunos + periodoAlunoSelecionado.getListaAlunosPeriodo().size();
					}
					contador ++ ;
				}
				if(naoEntrouPrimeira == false){
					int contadorTam;
					if (listaTotalizados.size() != maximoEncontrado){
						contadorTam = listaTotalizados.size();
						while(contadorTam != (maximoEncontrado)){
							listaTotalizados.add(0);
							contadorTam++;
						}
					}
					listaTotalizados.add(quantidadeAlunos);
					TotalizadorCurso totalizadorCurso = new TotalizadorCurso();
					totalizadorCurso.setListaTotalizados(listaTotalizados);
					totalizadorCurso.setGradeIngresso((listaPeriodoAlunoSelecionado.getIngressoAlunos()));
					int contTotais = 0;
					for (Integer inteiro : totalizadorCurso.getListaTotalizados()){
						int soma;
						if (listaTotalizadosAcumulados.size() == 0 || listaTotalizadosAcumulados.size() == contTotais) {
							soma = inteiro;
							listaTotalizadosAcumulados.add(soma);								
						}
						else{
							soma = inteiro + listaTotalizadosAcumulados.get(contTotais);
							listaTotalizadosAcumulados.set(contTotais, soma);
						}
						contTotais ++;
					}
					listaTotalizada.add(totalizadorCurso);
				}
				if (naoEntrou == false){
					f.set((listaPeriodoAlunoSelecionado.getIngressoAlunos()), 0);
				}
			}
			naoEntrouPrimeira = true;
			
			//if(i != curso.gr)
			gChartModelBuilder.addRow(String.valueOf(i) + "° - Período Incompleto", contadorAluno);
			model.addSeries(f);
			i++;
		}
		chartModel = gChartModelBuilder.build();
		ordenar.PeriodoAlunoPorGradeGeralInv(listaTotalizada);
		TotalizadorCurso totalizadorCurso = new TotalizadorCurso();
		totalizadorCurso.setListaTotalizados(listaTotalizadosAcumulados);
		totalizadorCurso.setGradeIngresso("Total");
		listaTotalizada.add(totalizadorCurso);
		return model;
	}


	public void buscarDados (){
		for (Grade gradeSelecionado : curso.getGrupoGrades()){

			if (!gradeSelecionado.estaCompleta()) continue;

			//List<Aluno> listaAlunos = alunoDAO.buscarTodosAlunoCursoGradeObjeto(curso.getId(), gradeSelecionado.getId());
			
			List<Aluno> listaAlunos = gradeSelecionado.getGrupoAlunos();
			
			
			
			importador = estruturaArvore.recuperarArvore(gradeSelecionado,true);
			for (Aluno alunoSelecionado : listaAlunos){
				curriculum = importador.get_cur();
				StudentsHistory sh = importador.getSh();		
				Student st = sh.getStudents().get(alunoSelecionado.getMatricula());
				
				if (st == null){

					logger.warn("Historico não encontrado para o aluno: " + alunoSelecionado.getMatricula());				
					continue;

				}	
				
				
				
				alunoSelecionado.setPeriodoReal(gerarDadosAluno(st,curriculum));
				ListaPeriodoAluno listaPeriodoAlunoManipulada = buscarListaIngresso(alunoSelecionado.getPeriodoIngresso(),gradeSelecionado); // lista global
				int periodoRealNovo = gerarDadosAluno(st,curriculum);
				if (periodoRealNovo > maximoEncontrado)  maximoEncontrado = periodoRealNovo;
				if (listaPeriodoAlunoManipulada.getMaximoEncontrado() == null || listaPeriodoAlunoManipulada.getMaximoEncontrado() < periodoRealNovo ) listaPeriodoAlunoManipulada.setMaximoEncontrado(periodoRealNovo);
				PeriodoAluno periodoAlunoManipulado = buscarListaPeriodoReal(listaPeriodoAlunoManipulada,periodoRealNovo); //lista por periodo
				periodoAlunoManipulado.getListaAlunosPeriodo().add(alunoSelecionado);
			}
		}
	}

	public ListaPeriodoAluno buscarListaIngresso(String ingresso,Grade grade ){
		for (ListaPeriodoAluno listaPeriodoAlunoSelecionado : listaDados){
			if (listaPeriodoAlunoSelecionado.getIngressoAlunos().equals(ingresso)){	
				return listaPeriodoAlunoSelecionado;	
			}		
		}
		ListaPeriodoAluno listaPeriodoAlunoNova = new ListaPeriodoAluno();
		listaPeriodoAlunoNova.setIngressoAlunos(ingresso);
		listaDados.add(listaPeriodoAlunoNova);
		return listaPeriodoAlunoNova;
	}

	public PeriodoAluno buscarListaPeriodoReal(ListaPeriodoAluno listaPeriodoAluno,Integer periodoReal){
		for(PeriodoAluno periodoAlunoSelecionado : listaPeriodoAluno.getListaPeriodoAluno()){
			if(periodoAlunoSelecionado.getPeriodoReal() == periodoReal){
				return periodoAlunoSelecionado;												
			}					
		}
		PeriodoAluno periodoAlunoNova = new PeriodoAluno();
		periodoAlunoNova.setPeriodoReal(periodoReal);
		listaPeriodoAluno.getListaPeriodoAluno().add(periodoAlunoNova);
		return periodoAlunoNova;
	}

	public int gerarDadosAluno(Student st, Curriculum cur)	{
		HashMap<Class, ArrayList<String[]>> aprovado;
		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED)); 
		for(int i: cur.getMandatories().keySet()){
			for(Class c: cur.getMandatories().get(i)){
				if(!aprovado.containsKey(c)) {
					return i;
				}
			}	
		}
		//return cur.getMandatories().keySet().size()+1;
		
		return cur.getMandatories().keySet().size();
	}

	public void itemSelect(ItemSelectEvent event) {
		quantidadeTotal = 0;
		listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
		for (PeriodoAluno periodoAluno : listaDados.get(event.getItemIndex()).getListaPeriodoAluno()){
			quantidadeTotal = quantidadeTotal + periodoAluno.getListaAlunosPeriodo().size();
			if (periodoAluno.getPeriodoReal() == (event.getSeriesIndex() + 1)){
				for(Aluno alunoQuestao : periodoAluno.getListaAlunosPeriodo()){
					AlunoSelecionado alunoSelecionado = new AlunoSelecionado();
					alunoSelecionado.setGradeIngresso((listaDados.get(event.getItemIndex()).getIngressoAlunos()));
					alunoSelecionado.setMatricula(alunoQuestao.getMatricula());
					alunoSelecionado.setNomeAluno(alunoQuestao.getNome());
					alunoSelecionado.setPeriodoCorrente(alunoQuestao.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado()));
					listaAlunoSelecionado.add(alunoSelecionado);
				}
				quantidadeSelecionados = listaAlunoSelecionado.size();
				periodoSelecionados = periodoAluno.getPeriodoReal();
			}
		}		
	}


	public void onSelectPizzaE(SelectEvent event){
		int selecionado = 0;
		JsonArray value = (JsonArray) event.getObject();
		if(value.size() > 0){
			JsonElement element = value.get(0);
			String label = new ArrayList<GChartModelRow>(this.getChart().getRows()).get(element.getAsJsonObject().get("row").getAsInt()).getLabel();
			selecionado = Integer.valueOf(label.substring(0,1));
		}
		listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
		for(ListaPeriodoAluno listaPeriodoAlunoSelecionado : listaDados){
			for (PeriodoAluno periodoAluno : listaPeriodoAlunoSelecionado.getListaPeriodoAluno()){
				if (periodoAluno.getPeriodoReal() == (selecionado)){
					for(Aluno alunoQuestao : periodoAluno.getListaAlunosPeriodo()){
						AlunoSelecionado alunoSelecionado = new AlunoSelecionado();
						alunoSelecionado.setGradeIngresso((listaPeriodoAlunoSelecionado.getIngressoAlunos()));
						alunoSelecionado.setMatricula(alunoQuestao.getMatricula());
						alunoSelecionado.setNomeAluno(alunoQuestao.getNome());
						/*String periodo;
						if (alunoQuestao.getGrade().getPeriodoInicio() == 0){
							periodo = alunoQuestao.getMatricula().substring(0,4)   + "3";
						}
						else {
							periodo = alunoQuestao.getMatricula().substring(0,4)  + String.valueOf(alunoQuestao.getGrade().getPeriodoInicio());
						}*/
						alunoSelecionado.setPeriodoCorrente( alunoQuestao.getPeriodoCorrente(usuarioController.getAutenticacao().getSemestreSelecionado()));
						listaAlunoSelecionado.add(alunoSelecionado);
					}
					break;
				}
			}	
			
			TotalizadorCurso totalizadorCursoFinal;
			totalizadorCursoFinal = listaTotalizada.get(listaTotalizada.size() - 1);
			quantidadeTotal = totalizadorCursoFinal.getListaTotalizados().get(totalizadorCursoFinal.getListaTotalizados().size() - 1);
			
			
			quantidadeSelecionados = listaAlunoSelecionado.size();
			periodoSelecionados = (selecionado);
		}
	}

	static public class ColumnModel implements Serializable {  
		private static final long serialVersionUID = 1L;
		private String header;  
		private int mes;  
		public ColumnModel(String header, int mes) {  
			this.header = header;  
			this.mes = mes;
		}  

		public String getHeader() {  
			return header;  
		}

		public Integer retornaProperty(TotalizadorCurso c) {  

			return c.getListaTotalizados().get(mes);
		}
	}

	//========================================================= GET - SET ==================================================================================//






	public GChartType getChartType() {
		return chartType;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<ListaPeriodoAluno> getListaDados() {
		return listaDados;
	}

	public void setListaDados(List<ListaPeriodoAluno> listaDados) {
		this.listaDados = listaDados;
	}

	public HorizontalBarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	public List<TotalizadorCurso> getListaTotalizada() {
		return listaTotalizada;
	}

	public void setListaTotalizada(List<TotalizadorCurso> listaTotalizada) {
		this.listaTotalizada = listaTotalizada;
	}

	public List<AlunoSelecionado> getListaAlunoSelecionado() {
		return listaAlunoSelecionado;
	}

	public void setListaAlunoSelecionado(
			List<AlunoSelecionado> listaAlunoSelecionado) {
		this.listaAlunoSelecionado = listaAlunoSelecionado;
	}

	public int getQuantidadeSelecionados() {
		return quantidadeSelecionados;
	}

	public void setQuantidadeSelecionados(int quantidadeSelecionados) {
		this.quantidadeSelecionados = quantidadeSelecionados;
	}

	public int getPeriodoSelecionados() {
		return periodoSelecionados;
	}

	public void setPeriodoSelecionados(int periodoSelecionados) {
		this.periodoSelecionados = periodoSelecionados;
	}

	public List<AlunoSelecionado> getListaAlunoSelecionadoFiltrados() {
		return listaAlunoSelecionadoFiltrados;
	}

	public void setListaAlunoSelecionadoFiltrados(
			List<AlunoSelecionado> listaAlunoSelecionadoFiltrados) {
		this.listaAlunoSelecionadoFiltrados = listaAlunoSelecionadoFiltrados;
	}

	public GChartModel getChart(){
		return chartModel;
	}

	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(int quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	public boolean isSemColuna() {
		return semColuna;
	}

	public void setSemColuna(boolean semColuna) {
		this.semColuna = semColuna;
	}
	
	

}
