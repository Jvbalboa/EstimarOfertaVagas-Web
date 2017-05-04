package br.ufjf.coordenacao.sistemagestaocurso.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufjf.coordenacao.sistemagestaocurso.controller.util.Ordenar;
import br.ufjf.coordenacao.sistemagestaocurso.controller.util.UsuarioController;
import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.*;

import org.primefaces.component.datatable.DataTable;
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



@Named
@ViewScoped
public class GraficosDisciplinasAprovadaController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private Curso curso = new Curso();
	private List<Historico> listaHistorico ;
	private String semestreSelecionado;
	private int maximoAprovacoes;
	private int quantidadeTotal;
	private HorizontalBarChartModel animatedModel2;
	private Ordenar ordenar = new Ordenar();
	private int quantidadeSelecionados;
	private int quantidadeAprovacoesSelecionados;
	private GChartType chartType = GChartType.PIE;
	private GChartModel chartModel = null;
	private List<TurmaAlunos> listaTurmaAlunos = new ArrayList<TurmaAlunos>();
	private List<AlunoSelecionado> listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
	private List<AlunoSelecionado> listaAlunoSelecionadoFiltrados ;
	private List<Historico> listaHistoricoSelecionado = new ArrayList<Historico>();
	private List<Historico> listaHistoricoSelecionadoFiltrados ;
	private AlunoSelecionado alunoSelecionadoGrid;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init()  {
		usuarioController.atualizarPessoaLogada();
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		periodoIniciar();
		carregaHistoricoSemestre();
		createAnimatedModels();
	}

	private void createAnimatedModels() {
		
		

		animatedModel2 = initBarModel();
		animatedModel2.setTitle("Gráfico - Quantidade de aprovações por turma no período selecionado");
		animatedModel2.setLegendPosition("se");
		animatedModel2.setStacked(true);
		animatedModel2.setZoom(true);
		Axis yAxis = animatedModel2.getAxis(AxisType.Y);
		animatedModel2.setDatatipFormat("%1$d");
		Axis xAxis = animatedModel2.getAxis(AxisType.X);
		yAxis.setLabel("Turmas");
		xAxis.setLabel("Quantidade Alunos");
		yAxis.setMin(0);
		yAxis.setTickInterval("10");
		yAxis.setMax(20);
	}

	public void periodoIniciar(){
		/*Calendar now = Calendar.getInstance();
		int anoAtual = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		int periodoAtual = 0;
		if(mes >= 1 && mes <= 6){
			periodoAtual = 1;
		}
		else {
			periodoAtual = 3;
		}
		if (periodoAtual == 3){
			semestreSelecionado = Integer.toString(anoAtual) + "1";
		}
		else {
			semestreSelecionado = Integer.toString(anoAtual-1) + "3";
		}*/
		semestreSelecionado = usuarioController.getAutenticacao().getSemestreSelecionado();
	}

	public void buscar(){
		
		
		
		carregaHistoricoSemestre();
		createAnimatedModels();
		listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
		quantidadeTotal = 0;
		quantidadeSelecionados = 0;
		quantidadeAprovacoesSelecionados = 0;
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:dadosConsolidadosDetalhe");
		dataTable.clearInitialState();
		dataTable.reset();
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:dadosConsolidadosDetalheAluno");
		dataTable.clearInitialState();
		dataTable.reset();
	}


	private HorizontalBarChartModel initBarModel() {
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		GChartModelBuilder gChartModelBuilder = new GChartModelBuilder();
		gChartModelBuilder.setChartType(getChartType()).addColumns("Quantidade de Disciplinas", "Quantidade");
		listaTurmaAlunos = ordenar.TurmaAlunosPorIngressoGeral(listaTurmaAlunos);
		int i = 0;
		boolean naoEntrouPrimeira = false;
		while(i != (maximoAprovacoes + 1)){
			int contadorAluno = 0;
			ChartSeries f = new ChartSeries();
			if(i == 0){
				f.setLabel("Sem aprovação");
			}else if(i == 1){
				f.setLabel(String.valueOf(i) + " aprovação");
			}else {
				f.setLabel(String.valueOf(i) + " aprovações");
			}
			for(TurmaAlunos turmaAlunos : listaTurmaAlunos){
				ordenar.AprovacoesQuantidadePeriodoGeral(turmaAlunos.getListaAprovacoesQuantidade());
				boolean naoEntrou = false;
				if (turmaAlunos.getIngressoAlunos().substring(0,4).compareTo(semestreSelecionado.substring(0,4)) > 0){
					continue;
				}
				turmaAlunos.setQuantidadeTotal(0);
				for (AprovacoesQuantidade aprovacoesQuantidadeSelecionado : turmaAlunos.getListaAprovacoesQuantidade()){
					turmaAlunos.setQuantidadeTotal(turmaAlunos.getQuantidadeTotal() + aprovacoesQuantidadeSelecionado.getListaAlunoQuantidade().size());
					if(aprovacoesQuantidadeSelecionado.getQuantidadeAprovacoes() == i ){
						contadorAluno = contadorAluno + aprovacoesQuantidadeSelecionado.getListaAlunoQuantidade().size();
						f.set((turmaAlunos.getIngressoAlunos()),aprovacoesQuantidadeSelecionado.getListaAlunoQuantidade().size());
						naoEntrou = true;
						if(naoEntrouPrimeira == true) break;
					}
				}
				if (naoEntrou == false){
					f.set((turmaAlunos.getIngressoAlunos()), 0);
				}
			}
			naoEntrouPrimeira = true;
			if(i == 0){
				gChartModelBuilder.addRow("Sem aprovação", contadorAluno);

			}else if(i == 1){
				gChartModelBuilder.addRow(String.valueOf(i) + " aprovação", contadorAluno);
			}else {
				gChartModelBuilder.addRow(String.valueOf(i) + " aprovações", contadorAluno);
			}
			model.addSeries(f);
			i++;
		}
		chartModel = gChartModelBuilder.build();
		return model;
	}

	public void carregaHistoricoSemestre(){
		listaTurmaAlunos = new ArrayList<TurmaAlunos>();
		AlunoQuantidade alunoQuantidade = null;
		TurmaAlunos turmaAlunos= null;
		AprovacoesQuantidade aprovacoesQuantidade = null;
		for (Grade gradeSelecionado : curso.getGrupoGrades()){
			List<Aluno> listaAlunos = gradeSelecionado.getGrupoAlunos(); 
			//importador = estruturaArvore.recuperarArvore(gradeSelecionado,false);
			for (Aluno alunoSelecionadoGrade : listaAlunos){
				alunoQuantidade = new AlunoQuantidade();
				turmaAlunos = buscarTurmaAlunos(alunoSelecionadoGrade.getGrade().getCodigo(),alunoSelecionadoGrade.getPeriodoIngresso());
				alunoQuantidade.setAluno(alunoSelecionadoGrade);
				boolean achouHistorico = false;
				//Hibernate.initialize(alunoSelecionadoGrade.getGrupoHistorico());
				for (Historico historico : alunoSelecionadoGrade.getGrupoHistorico()){
					if (historico.getSemestreCursado().equals(semestreSelecionado)){
						achouHistorico = true;
						if (historico.getStatusDisciplina().equals("Aprovado") ){
							alunoQuantidade.getListaDisciplina().add(historico.getDisciplina());
						}
					}
				}
				if (achouHistorico == false){
					continue;
				}
				if (alunoQuantidade.getListaDisciplina().size() > maximoAprovacoes){
					maximoAprovacoes = alunoQuantidade.getListaDisciplina().size();
				}
				aprovacoesQuantidade = buscarAprovacoesQuantidade(turmaAlunos,alunoQuantidade.getListaDisciplina().size());
				aprovacoesQuantidade.getListaAlunoQuantidade().add(alunoQuantidade);	
			}
		}
	}

	public TurmaAlunos buscarTurmaAlunos (String grade,String ingresso){
		for (TurmaAlunos turmaAlunos : listaTurmaAlunos){
			if (turmaAlunos.getIngressoAlunos().equals(ingresso)){
				return turmaAlunos;
			}
		}
		TurmaAlunos turmaAlunos = new TurmaAlunos();

		turmaAlunos.setIngressoAlunos(ingresso);
		listaTurmaAlunos.add(turmaAlunos);
		return turmaAlunos;
	} 

	public AprovacoesQuantidade buscarAprovacoesQuantidade (TurmaAlunos turmaAlunos,int quantidadeAprovacoes){
		for (AprovacoesQuantidade aprovacoesQuantidade : turmaAlunos.getListaAprovacoesQuantidade()){
			if (aprovacoesQuantidade.getQuantidadeAprovacoes() == quantidadeAprovacoes){
				return aprovacoesQuantidade;
			}
		}
		AprovacoesQuantidade aprovacoesQuantidade = new AprovacoesQuantidade();
		aprovacoesQuantidade.setQuantidadeAprovacoes(quantidadeAprovacoes);
		turmaAlunos.getListaAprovacoesQuantidade().add(aprovacoesQuantidade);
		return aprovacoesQuantidade;
	}

	public void itemSelect(ItemSelectEvent event) {
		listaHistoricoSelecionado = new ArrayList<Historico>();
		listaHistoricoSelecionadoFiltrados = null;
		alunoSelecionadoGrid = new AlunoSelecionado();
		listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
		quantidadeTotal = listaTurmaAlunos.get(event.getItemIndex()).getQuantidadeTotal();
		for (AprovacoesQuantidade aprovacoesQuantidade : listaTurmaAlunos.get(event.getItemIndex()).getListaAprovacoesQuantidade()){
			if (aprovacoesQuantidade.getQuantidadeAprovacoes() == (event.getSeriesIndex())){
				for(AlunoQuantidade alunoQuantidade : aprovacoesQuantidade.getListaAlunoQuantidade()){
					AlunoSelecionado alunoSelecionado = new AlunoSelecionado();
					alunoSelecionado.setGradeIngresso((listaTurmaAlunos.get(event.getItemIndex()).getIngressoAlunos()));
					alunoSelecionado.setMatricula(alunoQuantidade.getAluno().getMatricula());
					alunoSelecionado.setNomeAluno(alunoQuantidade.getAluno().getNome());
					alunoSelecionado.setAluno(alunoQuantidade.getAluno());
					listaAlunoSelecionado.add(alunoSelecionado);
				}
				quantidadeSelecionados = listaAlunoSelecionado.size();
				quantidadeAprovacoesSelecionados = aprovacoesQuantidade.getQuantidadeAprovacoes();
				break;
			}
		}		
	}

	public void onRowSelect(SelectEvent event) {
		alunoSelecionadoGrid = (AlunoSelecionado) event.getObject();
		listaHistoricoSelecionado = new ArrayList<Historico>();
		for(Historico hitorico : alunoSelecionadoGrid.getAluno().getGrupoHistorico()){
			if (hitorico.getSemestreCursado().equals(semestreSelecionado)){
				listaHistoricoSelecionado.add(hitorico);
			}
		}
	}

	public void onSelectPizzaE(SelectEvent event){
		int selecionado = 0;
		quantidadeTotal = 0;
		listaHistoricoSelecionado = new ArrayList<Historico>();
		listaHistoricoSelecionadoFiltrados = null;
		alunoSelecionadoGrid = new AlunoSelecionado();
		JsonArray value = (JsonArray) event.getObject();
		if(value.size() > 0){
			JsonElement element = value.get(0);
			String label = new ArrayList<GChartModelRow>(this.getChart().getRows()).get(element.getAsJsonObject().get("row").getAsInt()).getLabel();
			if (label.substring(0,1).equals("S")){
				selecionado = 0;
			}
			else {
				selecionado = Integer.valueOf(label.substring(0,1));
			}
		}

		listaAlunoSelecionado = new ArrayList<AlunoSelecionado>();
		for(TurmaAlunos turmaAlunos : listaTurmaAlunos){
			if (turmaAlunos.getIngressoAlunos().compareTo(semestreSelecionado.substring(0,4)) > 0){
				continue;
			}
			quantidadeTotal = quantidadeTotal + turmaAlunos.getQuantidadeTotal();
			for (AprovacoesQuantidade aprovacoesQuantidade : turmaAlunos.getListaAprovacoesQuantidade()){
				if (aprovacoesQuantidade.getQuantidadeAprovacoes() == (selecionado)){
					for(AlunoQuantidade alunoQuantidade : aprovacoesQuantidade.getListaAlunoQuantidade()){
						AlunoSelecionado alunoSelecionado = new AlunoSelecionado();
						alunoSelecionado.setGradeIngresso((turmaAlunos.getIngressoAlunos()));
						alunoSelecionado.setMatricula(alunoQuantidade.getAluno().getMatricula());
						alunoSelecionado.setNomeAluno(alunoQuantidade.getAluno().getNome());
						alunoSelecionado.setAluno(alunoQuantidade.getAluno());
						listaAlunoSelecionado.add(alunoSelecionado);
					}
					break;
				}
			}	
		}
		quantidadeSelecionados = listaAlunoSelecionado.size();
		quantidadeAprovacoesSelecionados = (selecionado);
	}

	/*---------------------------------------------------------------------------*/

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Historico> getListaHistorico() {
		return listaHistorico;
	}

	public void setListaHistorico(List<Historico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	public String getSemestreSelecionado() {
		return semestreSelecionado;
	}

	public void setSemestreSelecionado(String semestreSelecionado) {
		this.semestreSelecionado = semestreSelecionado;
	}

	public int getMaximoAprovacoes() {
		return maximoAprovacoes;
	}

	public void setMaximoAprovacoes(int maximoAprovacoes) {
		this.maximoAprovacoes = maximoAprovacoes;
	}

	public List<TurmaAlunos> getListaTurmaAlunos() {
		return listaTurmaAlunos;
	}

	public void setListaTurmaAlunos(List<TurmaAlunos> listaTurmaAlunos) {
		this.listaTurmaAlunos = listaTurmaAlunos;
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

	public GChartModel getChart(){
		return chartModel;
	}

	public HorizontalBarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	public void setAnimatedModel2(HorizontalBarChartModel animatedModel2) {
		this.animatedModel2 = animatedModel2;
	}

	public Ordenar getOrdenar() {
		return ordenar;
	}

	public void setOrdenar(Ordenar ordenar) {
		this.ordenar = ordenar;
	}

	public GChartType getChartType() {
		return chartType;
	}

	public void setChartType(GChartType chartType) {
		this.chartType = chartType;
	}

	public GChartModel getChartModel() {
		return chartModel;
	}

	public void setChartModel(GChartModel chartModel) {
		this.chartModel = chartModel;
	}

	public List<AlunoSelecionado> getListaAlunoSelecionado() {
		return listaAlunoSelecionado;
	}

	public void setListaAlunoSelecionado(
			List<AlunoSelecionado> listaAlunoSelecionado) {
		this.listaAlunoSelecionado = listaAlunoSelecionado;
	}

	public List<AlunoSelecionado> getListaAlunoSelecionadoFiltrados() {
		return listaAlunoSelecionadoFiltrados;
	}

	public void setListaAlunoSelecionadoFiltrados(
			List<AlunoSelecionado> listaAlunoSelecionadoFiltrados) {
		this.listaAlunoSelecionadoFiltrados = listaAlunoSelecionadoFiltrados;
	}

	public int getQuantidadeSelecionados() {
		return quantidadeSelecionados;
	}

	public void setQuantidadeSelecionados(int quantidadeSelecionados) {
		this.quantidadeSelecionados = quantidadeSelecionados;
	}

	public int getQuantidadeAprovacoesSelecionados() {
		return quantidadeAprovacoesSelecionados;
	}


	public void setQuantidadeAprovacoesSelecionados(
			int quantidadeAprovacoesSelecionados) {
		this.quantidadeAprovacoesSelecionados = quantidadeAprovacoesSelecionados;
	}


	public List<Historico> getListaHistoricoSelecionado() {
		return listaHistoricoSelecionado;
	}


	public void setListaHistoricoSelecionado(
			List<Historico> listaHistoricoSelecionado) {
		this.listaHistoricoSelecionado = listaHistoricoSelecionado;
	}


	public List<Historico> getListaHistoricoSelecionadoFiltrados() {
		return listaHistoricoSelecionadoFiltrados;
	}


	public void setListaHistoricoSelecionadoFiltrados(
			List<Historico> listaHistoricoSelecionadoFiltrados) {
		this.listaHistoricoSelecionadoFiltrados = listaHistoricoSelecionadoFiltrados;
	}


	public AlunoSelecionado getAlunoSelecionadoGrid() {
		return alunoSelecionadoGrid;
	}


	public void setAlunoSelecionadoGrid(AlunoSelecionado alunoSelecionadoGrid) {
		this.alunoSelecionadoGrid = alunoSelecionadoGrid;
	}


	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(int quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}
}
