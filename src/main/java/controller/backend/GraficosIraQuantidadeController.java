package controller.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Aluno;
import model.Curso;
import model.Grade;
import model.arvore.Student;
import model.arvore.StudentsHistory;
import model.estrutura.ElementoGrafico;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import controller.util.EstruturaArvore;
import controller.util.ImportarArvore;
import controller.util.Ordenar;
import controller.util.UsuarioController;

@Named
@ViewScoped
public class GraficosIraQuantidadeController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private float ira;
	private Curso curso = new Curso();
	private LineChartModel  bubbleModel2;
	private List<ElementoGrafico> listaElementoGrafico = new ArrayList<ElementoGrafico>();
	private Ordenar ordenar = new Ordenar();
	private List<Aluno> listaAlunoSelecionado = new ArrayList<Aluno>();
	private List<Aluno> listaAlunoSelecionadoFiltrados ;
	private int quantidadeSelecionados;
	private String periodoSelecionados;
	private int quantidadeTotal;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;	

	@PostConstruct
	public void init() {
		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();
		
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		createBubbleModelsAcumulado();
	}

	private void createBubbleModelsAcumulado(){
		bubbleModel2 = initBubbleModel();
		bubbleModel2.setTitle("Gráfico de Distribuição de Alunos por faixa de IRA");
		bubbleModel2.setShowPointLabels(true);
		bubbleModel2.setDatatipFormat("%2$d");
        Axis xAxis = new CategoryAxis("Faixas de IRA");
        bubbleModel2.getAxes().put(AxisType.X, xAxis);
        Axis yAxis = bubbleModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade de Alunos");
        yAxis.setMin(0);
        yAxis.setTickFormat("%d");
        yAxis.setTickInterval("1");
	}

	private LineChartModel  initBubbleModel(){
		LineChartModel  model = new LineChartModel ();
		for(Grade grade : curso.getGrupoGrades()){
			importador = estruturaArvore.recuperarArvore(grade,true);
			for(Aluno aluno : grade.getGrupoAlunos()){
				StudentsHistory sh = importador.getSh();
				Student st = sh.getStudents().get(aluno.getMatricula());
				
				if (st == null){

					System.out.println("Historico não encontrado para o aluno :" + aluno.getMatricula());				
					continue;

				}		
				
				
				ira = st.getIRA(); 
				if(ira == -1) continue;
				int index = (int)ira / 5;
				ElementoGrafico elementoGrafico = buscaElemento(index);
				aluno.setIra(ira);
				elementoGrafico.getListaAluno().add(aluno);
			}
		}
		ordenar.numeroElementoGrafico(listaElementoGrafico);
		LineChartSeries series1 = new LineChartSeries();
		series1.setFill(true);
		series1.set( 0,0);
		quantidadeTotal = 0;
		for(ElementoGrafico elementoGraficoQuestao : listaElementoGrafico){
				series1.set( ((elementoGraficoQuestao.getNumero()+1) * 5),elementoGraficoQuestao.getListaAluno().size());
				quantidadeTotal	= elementoGraficoQuestao.getListaAluno().size() + quantidadeTotal;
		}
		quantidadeTotal = quantidadeTotal - 1;	
		model.addSeries(series1);
		return model;
	}

	public ElementoGrafico buscaElemento(int numero){
		for (ElementoGrafico elementoGraficoQuestao : listaElementoGrafico){
			if (elementoGraficoQuestao.getNumero() == numero){
				return elementoGraficoQuestao;
			}
		}
		ElementoGrafico elementoGrafico = new ElementoGrafico();
		elementoGrafico.setNumero(numero);
		listaElementoGrafico.add(elementoGrafico);
		return elementoGrafico;
	}

	public void itemSelect(ItemSelectEvent event) {
		listaAlunoSelecionado = new ArrayList<Aluno>();
		quantidadeSelecionados = 0;
		if (event.getItemIndex() == 0){
			return;
		}
		listaAlunoSelecionado = listaElementoGrafico.get(event.getItemIndex() - 1).getListaAluno();
		quantidadeSelecionados = listaAlunoSelecionado.size();
		periodoSelecionados = String.valueOf((event.getItemIndex() - 1) * 5) + " - " + String.valueOf((event.getItemIndex() ) * 5);
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

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}	

	public LineChartModel getBubbleModel2() {
		return bubbleModel2;
	}

	public void setBubbleModel2(LineChartModel bubbleModel2) {
		this.bubbleModel2 = bubbleModel2;
	}

	public Ordenar getOrdenar() {
		return ordenar;
	}

	public void setOrdenar(Ordenar ordenar) {
		this.ordenar = ordenar;
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

	public List<ElementoGrafico> getListaElementoGrafico() {
		return listaElementoGrafico;
	}

	public void setListaElementoGrafico(List<ElementoGrafico> listaElementoGrafico) {
		this.listaElementoGrafico = listaElementoGrafico;
	}

	public List<Aluno> getListaAlunoSelecionado() {
		return listaAlunoSelecionado;
	}

	public void setListaAlunoSelecionado(List<Aluno> listaAlunoSelecionado) {
		this.listaAlunoSelecionado = listaAlunoSelecionado;
	}

	public List<Aluno> getListaAlunoSelecionadoFiltrados() {
		return listaAlunoSelecionadoFiltrados;
	}

	public void setListaAlunoSelecionadoFiltrados(
			List<Aluno> listaAlunoSelecionadoFiltrados) {
		this.listaAlunoSelecionadoFiltrados = listaAlunoSelecionadoFiltrados;
	}

	public int getQuantidadeSelecionados() {
		return quantidadeSelecionados;
	}

	public void setQuantidadeSelecionados(int quantidadeSelecionados) {
		this.quantidadeSelecionados = quantidadeSelecionados;
	}

	public String getPeriodoSelecionados() {
		return periodoSelecionados;
	}

	public void setPeriodoSelecionados(String periodoSelecionados) {
		this.periodoSelecionados = periodoSelecionados;
	}

	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(int quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}
}