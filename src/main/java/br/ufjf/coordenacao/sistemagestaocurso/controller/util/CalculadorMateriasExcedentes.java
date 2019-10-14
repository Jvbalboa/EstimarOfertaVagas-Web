package br.ufjf.coordenacao.sistemagestaocurso.controller.util;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.EventoAce;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.SituacaoDisciplina;

public abstract class CalculadorMateriasExcedentes {
	
	public static List<SituacaoDisciplina> getExcedentesEletivas(int horasEletivasObrigatorias, List<SituacaoDisciplina> disciplinasEletivasCompletadas) {
		int horasExcedentes = 0, horasTotais = 0;
		for(SituacaoDisciplina sd : disciplinasEletivasCompletadas) {
			horasTotais += Integer.parseInt(sd.getCargaHoraria());
		}
		horasExcedentes = horasTotais - horasEletivasObrigatorias;
		ArrayList<SituacaoDisciplina> disciplinasExcedentes = new ArrayList<>();
		ArrayList<String> codigoDisciplinasExcedentes = selecionaExcedentes(horasExcedentes, disciplinasEletivasCompletadas);
		for(String disciplinaExcedente : codigoDisciplinasExcedentes) {
			for(SituacaoDisciplina sd : disciplinasEletivasCompletadas) {
				if(sd.getCodigo().equals(disciplinaExcedente)) {
					sd.setNome(sd.getNome() + " : Excedente Eletiva");
					disciplinasExcedentes.add(sd);
					break;
				}
			}
		}
		return disciplinasExcedentes;
	}
	
	public static List<EventoAce> getExcedentesOpcionais(int horasOpcionaisObrigatorias, List<SituacaoDisciplina> disciplinasOpcionaisCompletadas) {
		int horasExcedentes = 0, horasTotais = 0;
		for(SituacaoDisciplina sd : disciplinasOpcionaisCompletadas) {
			horasTotais += Integer.parseInt(sd.getCargaHoraria());
		}
		horasExcedentes = horasTotais - horasOpcionaisObrigatorias;
		ArrayList<EventoAce> disciplinasExcedentes = new ArrayList<>();
		ArrayList<String> codigoDisciplinasExcedentes = selecionaExcedentes(horasExcedentes, disciplinasOpcionaisCompletadas);
		for(String disciplinaExcedente :  codigoDisciplinasExcedentes) {
			for(SituacaoDisciplina sd :disciplinasOpcionaisCompletadas) {
				if(sd.getCodigo().equals(disciplinaExcedente)) {
					EventoAce eventoAce = new EventoAce();
					eventoAce.setMatricula(sd.getCodigo());
					eventoAce.setDescricao(sd.getCodigo() + " " + sd.getNome() + " : Disciplina Excedente Opcional");
					eventoAce.setHoras((long) Integer.parseInt(sd.getCargaHoraria()));
					eventoAce.setExcluir(false);
					disciplinasExcedentes.add(eventoAce);
					break;
				}
			}
		}
		return disciplinasExcedentes;
	}
	
	private static ArrayList<String> selecionaExcedentes(int horasExcedentes, List<SituacaoDisciplina> disciplinasCompletadas){
		ArrayList<String> solucao = new ArrayList<String>(); // lista com codigo das disciplinas que vao ser selecionadas como excedentes
		int tamanhoConjunto = disciplinasCompletadas.size(); // tamanho do conjunto das disciplinas
		boolean[][] tabela = new boolean[tamanhoConjunto + 1][horasExcedentes+1]; // tabela auxiliar
		int[] pesos = new int[tamanhoConjunto + 1]; // vetor contendo as horas de cada disciplina
		
		
		// atribuindo os valores dos pesos de acordo com a carga horária da disciplina
		for(int i = 1; i <= tamanhoConjunto; i++) {
			//pesos[i] = disciplinasCompletadas.get(i-1).getCargaHoraria();
			pesos[i] = Integer.parseInt(disciplinasCompletadas.get(i-1).getCargaHoraria());
			// inserindo a primeira coluna
			tabela[i][0] = true;
		}
		tabela[0][0] = true;
		
		//inicio do subset sum
		boolean s;
		for(int j = 1; j < horasExcedentes+1; j++) {
			//zerando a primeira linha
			tabela[0][j] = false;
			
			for(int i = 1; i < tamanhoConjunto+1; i++) {
				//se já tiver conseguido essa subsoma s == 1
				s = tabela[i-1][j];
				
				//se ainda nao tiver conseguido a subsoma e este elemento for viavel
				if(s == false && pesos[i] <= j) {
					s = tabela[i-1][j-pesos[i]];
				}
				
				tabela[i][j] = s;
			}
		}
		
		// encontra melhor solução
		for(int i = horasExcedentes; i > 0; i--) {
			if(tabela[tamanhoConjunto][i]) {
				solucao = encontraSolucao(tabela, pesos, tamanhoConjunto, i, disciplinasCompletadas, solucao);
				break;
			}
		}
		return solucao;
	}
	
	private static ArrayList<String> encontraSolucao(boolean tabela[][], int pesos[], int tamanhoConjunto, int horasExcedentes, List<SituacaoDisciplina> disciplinasCompletadas, ArrayList<String> solucao){
		if(horasExcedentes == 0) {
			return solucao;
		}
		else {
			for(int i = 0; i < tamanhoConjunto; i++) {
				if(tabela[i][horasExcedentes]) {
					solucao.add(disciplinasCompletadas.get(i-1).getCodigo());
					return encontraSolucao(tabela, pesos, tamanhoConjunto, horasExcedentes - pesos[i], disciplinasCompletadas, solucao);
				}
			}
		}
		return solucao;
	}

}
