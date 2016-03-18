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
import model.Disciplina;
import model.Grade;
import model.Historico;
import model.estrutura.AlunoSituacao;
import model.estrutura.DisciplinaReprovada;

import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartModelRow;
import org.primefaces.extensions.component.gchart.model.GChartType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import controller.util.UsuarioController;



@Named
@ViewScoped
public class GraficosDisciplinasReprovadasController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;
	private Curso curso = new Curso();
	private List<DisciplinaReprovada> listaTotal = new ArrayList<DisciplinaReprovada>();
	private List<DisciplinaReprovada> listaTotalFiltrados;
	private List<Historico> listaHistorico = new ArrayList<Historico>();
	private List<Historico> listaHistoricoSelecionado;
	private DisciplinaReprovada disciplinaReprovadaSelecionada;
	private List<AlunoSituacao> listaAlunoSituacaoSelecionado = new ArrayList<AlunoSituacao>();
	private List<AlunoSituacao> listaAlunoSituacaoFiltrado ;
	private GChartType chartTypeAluno = GChartType.PIE;
	private GChartModel chartModelAluno = null;
	private GChartType chartTypeHist = GChartType.PIE;
	private GChartModel chartModelHist = null;

	//========================================================= METODOS ==================================================================================//


	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {
		usuarioController.atualizarPessoaLogada();
		curso = usuarioController.getAutenticacao().getCursoSelecionado();
		gerarDados();
	}

	private void  gerarDados(){

		chartModelAluno = new GChartModelBuilder().setChartType(getChartTypeAluno())
				.addColumns("Matriculados", "Descri‹o")
				.addRow("Dados", 1)
				.build();

		chartModelHist = new GChartModelBuilder().setChartType(getChartTypeHist())
				.addColumns("Atividade", "Descri‹o")
				.addRow("Dados", 1)
				.build();
		
		
		for(Grade grade : curso.getGrupoGrades()){
			
			for(Aluno aluno : grade.getGrupoAlunos()){
				for(Historico historicoQuestao : aluno.getGrupoHistorico()){
					DisciplinaReprovada disciplinaReprovadaQuestao = buscaDisciplinaReprovada(historicoQuestao.getDisciplina());
					disciplinaReprovadaQuestao.getListaTotal().add(historicoQuestao);
					if(historicoQuestao.getStatusDisciplina().equals("Rep Nota")){						
						disciplinaReprovadaQuestao.getListaReprovadosNota().add(historicoQuestao);
					}
					else if(historicoQuestao.getStatusDisciplina().equals("Rep Freq")){						
						disciplinaReprovadaQuestao.getListaReprovadosFreq().add(historicoQuestao);
					}
					else if(historicoQuestao.getStatusDisciplina().equals("Aprovado")){
						disciplinaReprovadaQuestao.getListaAprov().add(historicoQuestao);						
					}
					else {
						disciplinaReprovadaQuestao.getListaMatriculados().add(historicoQuestao);
					}
				}
			}
		}	
	}


	public DisciplinaReprovada buscaDisciplinaReprovada(Disciplina disciplinaBuscada){
		for (DisciplinaReprovada disciplinaReprovadaQuestao : listaTotal){
			if (disciplinaReprovadaQuestao.getDisciplinaSelecionada() == disciplinaBuscada){
				return disciplinaReprovadaQuestao;
			}
		}
		DisciplinaReprovada disciplinaReprovada = new DisciplinaReprovada();
		disciplinaReprovada.setDisciplinaSelecionada(disciplinaBuscada);
		listaTotal.add(disciplinaReprovada);
		return disciplinaReprovada;
	}

	public void onRowSelect(SelectEvent event) {
		listaAlunoSituacaoSelecionado = disciplinaReprovadaSelecionada.getListaAlunoSituacao();
		listaHistorico = disciplinaReprovadaSelecionada.getListaTotal();
		chartModelAluno = new GChartModelBuilder().setChartType(getChartTypeAluno())
				.addColumns("Situa‹o", "Quantidade")
				.addRow("Rep. Nota", disciplinaReprovadaSelecionada.getQuantidadeAlunosReprovadosNota())
				.addRow("Rep. Freq", disciplinaReprovadaSelecionada.getQuantidadeAlunosReprovadosFreq())
				.addRow("Aprovados", disciplinaReprovadaSelecionada.getQuantidadeAlunosAprovados())
				.addRow("Outros", ( disciplinaReprovadaSelecionada.getQuantidadeAlunos() - disciplinaReprovadaSelecionada.getQuantidadeAlunosReprovadosFreq() - disciplinaReprovadaSelecionada.getQuantidadeAlunosReprovadosNota() - disciplinaReprovadaSelecionada.getQuantidadeAlunosAprovados()) )
				.build();
		chartModelHist = new GChartModelBuilder().setChartType(getChartTypeHist())
				.addColumns("Situa‹o", "Quantidade")
				.addRow("Rep. Nota", disciplinaReprovadaSelecionada.getQuantidadeReprovacoesNota())
				.addRow("Rep. Freq", disciplinaReprovadaSelecionada.getQuantidadeReprovacoesFreq())
				.addRow("Aprovados", disciplinaReprovadaSelecionada.getQuantidadeAprovados())
				.addRow("Outros", ( disciplinaReprovadaSelecionada.getQuantidadeTentativas() - disciplinaReprovadaSelecionada.getQuantidadeReprovacoesFreq() - disciplinaReprovadaSelecionada.getQuantidadeReprovacoesNota() - disciplinaReprovadaSelecionada.getQuantidadeAprovados()) )
				.build();
	}


	public void onSelectPizzaAluno(SelectEvent event){
		JsonArray value = (JsonArray) event.getObject();
		if(value.size() > 0){
			JsonElement element = value.get(0);
			String label = new ArrayList<GChartModelRow>(this.getChartModelAluno().getRows()).get(element.getAsJsonObject().get("row").getAsInt()).getLabel();
			if (label.equals("Rep. Nota")){
				List<AlunoSituacao> listaAlunoSituacaoSelecionadoAux = new ArrayList<AlunoSituacao>();
				for (AlunoSituacao alunoSituacaoQuestao : disciplinaReprovadaSelecionada.getListaAlunoSituacao()){
					if (alunoSituacaoQuestao.getQuantidadeReprovacoesNota() > 0){
						listaAlunoSituacaoSelecionadoAux.add(alunoSituacaoQuestao);
					}
				}
				listaAlunoSituacaoSelecionado = listaAlunoSituacaoSelecionadoAux;
			}
			else if (label.equals("Rep. Freq")){
				List<AlunoSituacao> listaAlunoSituacaoSelecionadoAux = new ArrayList<AlunoSituacao>();
				for (AlunoSituacao alunoSituacaoQuestao : disciplinaReprovadaSelecionada.getListaAlunoSituacao()){
					if (alunoSituacaoQuestao.getQuantidadeReprovacoesFreq() > 0){
						listaAlunoSituacaoSelecionadoAux.add(alunoSituacaoQuestao);
					}
				}
				listaAlunoSituacaoSelecionado = listaAlunoSituacaoSelecionadoAux;
			}
			else {
				List<AlunoSituacao> listaAlunoSituacaoSelecionadoAux = new ArrayList<AlunoSituacao>();
				for (AlunoSituacao alunoSituacaoQuestao : disciplinaReprovadaSelecionada.getListaAlunoSituacao()){
					if (alunoSituacaoQuestao.getQuantidadeMatriculas() - alunoSituacaoQuestao.getQuantidadeReprovacoesFreq() - alunoSituacaoQuestao.getQuantidadeReprovacoesNota() > 0){
						listaAlunoSituacaoSelecionadoAux.add(alunoSituacaoQuestao);
					}
				}
				listaAlunoSituacaoSelecionado = listaAlunoSituacaoSelecionadoAux;
			}
		}
	}

	public void onSelectPizzaHist(SelectEvent event){
		JsonArray value = (JsonArray) event.getObject();
		if(value.size() > 0){
			JsonElement element = value.get(0);
			String label = new ArrayList<GChartModelRow>(this.getChartModelHist().getRows()).get(element.getAsJsonObject().get("row").getAsInt()).getLabel();
			if (label.equals("Rep. Nota")){
				listaHistorico = disciplinaReprovadaSelecionada.getListaReprovadosNota();
			}
			else if (label.equals("Rep. Freq")){
				listaHistorico = disciplinaReprovadaSelecionada.getListaReprovadosFreq();
			}
			else if (label.equals("Aprovados")){
				listaHistorico = disciplinaReprovadaSelecionada.getListaAprov();
			}
			else {
				listaHistorico = disciplinaReprovadaSelecionada.getListaMatriculados();
			}
		}
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

	public List<DisciplinaReprovada> getListaTotal() {
		return listaTotal;
	}

	public void setListaTotal(List<DisciplinaReprovada> listaTotal) {
		this.listaTotal = listaTotal;
	}

	public List<DisciplinaReprovada> getListaTotalFiltrados() {
		return listaTotalFiltrados;
	}

	public void setListaTotalFiltrados(List<DisciplinaReprovada> listaTotalFiltrados) {
		this.listaTotalFiltrados = listaTotalFiltrados;
	}

	public List<Historico> getListaHistorico() {
		return listaHistorico;
	}

	public void setListaHistorico(List<Historico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	public List<Historico> getListaHistoricoSelecionado() {
		return listaHistoricoSelecionado;
	}

	public void setListaHistoricoSelecionado(
			List<Historico> listaHistoricoSelecionado) {
		this.listaHistoricoSelecionado = listaHistoricoSelecionado;
	}

	public DisciplinaReprovada getDisciplinaReprovadaSelecionada() {
		return disciplinaReprovadaSelecionada;
	}

	public void setDisciplinaReprovadaSelecionada(
			DisciplinaReprovada disciplinaReprovadaSelecionada) {
		this.disciplinaReprovadaSelecionada = disciplinaReprovadaSelecionada;
	}

	public List<AlunoSituacao> getListaAlunoSituacaoSelecionado() {
		return listaAlunoSituacaoSelecionado;
	}

	public void setListaAlunoSituacaoSelecionado(
			List<AlunoSituacao> listaAlunoSituacaoSelecionado) {
		this.listaAlunoSituacaoSelecionado = listaAlunoSituacaoSelecionado;
	}

	public List<AlunoSituacao> getListaAlunoSituacaoFiltrado() {
		return listaAlunoSituacaoFiltrado;
	}

	public void setListaAlunoSituacaoFiltrado(
			List<AlunoSituacao> listaAlunoSituacaoFiltrado) {
		this.listaAlunoSituacaoFiltrado = listaAlunoSituacaoFiltrado;
	}

	public GChartType getChartTypeAluno() {
		return chartTypeAluno;
	}

	public void setChartTypeAluno(GChartType chartTypeAluno) {
		this.chartTypeAluno = chartTypeAluno;
	}

	public GChartModel getChartModelAluno() {
		return chartModelAluno;
	}

	public void setChartModelAluno(GChartModel chartModelAluno) {
		this.chartModelAluno = chartModelAluno;
	}

	public GChartType getChartTypeHist() {
		return chartTypeHist;
	}

	public void setChartTypeHist(GChartType chartTypeHist) {
		this.chartTypeHist = chartTypeHist;
	}

	public GChartModel getChartModelHist() {
		return chartModelHist;
	}

	public void setChartModelHist(GChartModel chartModelHist) {
		this.chartModelHist = chartModelHist;
	}

}

