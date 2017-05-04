package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.ufjf.coordenacao.OfertaVagas.model.Student;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.Ordenar;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Curso;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.*;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.EstruturaArvore;
import br.ufjf.coordenacao.sistemagestaocurso.util.arvore.ImportarArvore;

@Named
@ViewScoped
public class GraficosIraController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private float ira;
	private Curso curso = new Curso();
	private LineChartModel  bubbleModel1;
	private List<GrupoElementoGrafico> listaGrupoElementoGrafico = new ArrayList<GrupoElementoGrafico>();
	private Ordenar ordenar = new Ordenar();

	//========================================================= METODOS ==================================================================================//


	@Inject
	private UsuarioController usuarioController;	

	@PostConstruct
	public void init() {
		estruturaArvore = EstruturaArvore.getInstance();
		usuarioController.atualizarPessoaLogada();
		
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		createBubbleModels();
	}

	private void createBubbleModels(){
		bubbleModel1 = initBubbleModel();
		bubbleModel1.setTitle("Grfico de Distribuio IRA do Curso");
		Axis xAxis = bubbleModel1.getAxis(AxisType.X);
		xAxis.setMin(0);
		xAxis.setMax(100);
		bubbleModel1.setLegendPosition("ne");
		
		Axis yAxis = bubbleModel1.getAxis(AxisType.Y);
		yAxis.setTickFormat("%d");

		xAxis.setTickFormat("%.2f");
		bubbleModel1.setDatatipFormat("%1$.2f");
		bubbleModel1.setZoom(true);
		xAxis.setLabel("IRA");
		yAxis.setLabel("Turmas");
		
				
	}


	private LineChartModel  initBubbleModel(){
		listaGrupoElementoGrafico = new ArrayList<GrupoElementoGrafico>();
		LineChartModel  model = new LineChartModel ();
		for(Grade grade : curso.getGrupoGrades()){
			importador = estruturaArvore.recuperarArvore(grade,true);
			for(Aluno aluno : grade.getGrupoAlunos()){
								
				StudentsHistory sh = importador.getSh();
				Student st = sh.getStudents().get(aluno.getMatricula());
				
				if (st == null){

					System.out.println("Historico no encontrado para o aluno :" + aluno.getMatricula());				
					continue;

				}				
				
				ira = aluno.getIra();			
				
				retornaListaElemento(Integer.valueOf(aluno.getMatricula().substring(0,4)),ira).getListaAluno().add(aluno);				
				
			}
		
		}
		ordenar.periodoGrupoElementoGrafico(listaGrupoElementoGrafico);
		
		
		for (GrupoElementoGrafico grupoElementoGraficoQuestao : listaGrupoElementoGrafico){
			
			ordenar.periodoElementoGraficoIra(grupoElementoGraficoQuestao.getListaElementoGrafico());			
			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel(String.valueOf(grupoElementoGraficoQuestao.getGrade()));
			
			
			
			
			for (ElementoGrafico elementoGraficoQuestao : grupoElementoGraficoQuestao.getListaElementoGrafico()){
				
				
							
				series1.set(elementoGraficoQuestao.getIra(), grupoElementoGraficoQuestao.getGrade()      );
				series1.setShowLine(false);
			}
			model.addSeries(series1);
			
		}
		return model;
	}

	public ElementoGrafico retornaListaElemento(Integer grade,Float qtdHoras){

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

	public ElementoGrafico buscaElementoGrafico (GrupoElementoGrafico grupoElementoGrafico , Float qtdHoras){

		for (ElementoGrafico elementoGraficoQuestao : grupoElementoGrafico.getListaElementoGrafico()){
			if (elementoGraficoQuestao.getIra().equals(qtdHoras)){
				return elementoGraficoQuestao;			
			}			
		}

		ElementoGrafico elementoGraficoNovo = new ElementoGrafico();
		elementoGraficoNovo.setIra(qtdHoras);
		grupoElementoGrafico.getListaElementoGrafico().add(elementoGraficoNovo);
		return elementoGraficoNovo;

	}

	public void itemSelect(ItemSelectEvent event) {
		if (listaGrupoElementoGrafico.size() > event.getSeriesIndex()){

			for (Aluno alunoSelecionado :listaGrupoElementoGrafico.get(event.getSeriesIndex()).getListaElementoGrafico().get(event.getItemIndex() ).getListaAluno()){

				FacesMessage msg = new FacesMessage("Aluno Selecionado:" + alunoSelecionado.getMatricula() + " - " + alunoSelecionado.getNome());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
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

	public LineChartModel getBubbleModel1() {
		return bubbleModel1;
	}

	public void setBubbleModel1(LineChartModel bubbleModel1) {
		this.bubbleModel1 = bubbleModel1;
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

	

	public List<GrupoElementoGrafico> getListaGrupoElementoGrafico() {
		return listaGrupoElementoGrafico;
	}

	public void setListaGrupoElementoGrafico(
			List<GrupoElementoGrafico> listaGrupoElementoGrafico) {
		this.listaGrupoElementoGrafico = listaGrupoElementoGrafico;
	}

	public Ordenar getOrdenar() {
		return ordenar;
	}

	public void setOrdenar(Ordenar ordenar) {
		this.ordenar = ordenar;
	}
}