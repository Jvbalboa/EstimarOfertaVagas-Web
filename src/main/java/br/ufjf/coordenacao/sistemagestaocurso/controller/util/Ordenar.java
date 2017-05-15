package br.ufjf.coordenacao.sistemagestaocurso.controller.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.*;
import br.ufjf.coordenacao.sistemagestaocurso.model.estrutura.*;


public class Ordenar {

	
	public class EventoAcePeriodo implements Comparator<EventoAce> {
		@Override
		public int compare(EventoAce o1, EventoAce o2) {
			//Verificacao necessaria para manter o EXCEDENTE sempre no fim
			if(o1.getPeriodo() == null)
				return 1;
			
			if(o2.getPeriodo() == null)
				return -1;
			
			return o1.getPeriodo().compareTo(o2.getPeriodo());
		}
	}
	
	public class CursoCodigo implements Comparator<Curso> {
		@Override
		public int compare(Curso o1, Curso o2) {
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
	}
	
	public class DisciplinaCodigo implements Comparator<Disciplina> {
	    @Override
	    public int compare(Disciplina o1, Disciplina o2) {
	        return o1.getCodigo().compareTo(o2.getCodigo());
	    }
	}
	
	public class DisciplinaGradeDisciplinaCodigo implements Comparator<DisciplinaGradeDisciplina> {
		@Override
		public int compare(DisciplinaGradeDisciplina o1, DisciplinaGradeDisciplina o2) {
			return o1.getDisciplina().getCodigo().compareTo(o2.getDisciplina().getCodigo());
		}
	}
	
	public class DisciplinaGradeDisciplinaPeriodo implements Comparator<DisciplinaGradeDisciplina> {
		@Override
		public int compare(DisciplinaGradeDisciplina o1, DisciplinaGradeDisciplina o2) {
			return o1.getGradeDisciplina().getPeriodo().compareTo(o2.getGradeDisciplina().getPeriodo());
		}
	}	
	
	public class PeriodoAlunoOrdenarPeriodo implements Comparator<PeriodoAluno> {
		@Override
		public int compare(PeriodoAluno o1, PeriodoAluno o2) {
			return o1.getPeriodoReal().compareTo(o2.getPeriodoReal());
		}
	}	
	
	public class ListaPeriodoAlunoIngresso implements Comparator<ListaPeriodoAluno> {
		@Override
		public int compare(ListaPeriodoAluno o1, ListaPeriodoAluno o2) {
			return o1.getIngressoAlunos().compareTo(o2.getIngressoAlunos());
		}
	}	
	
	public class TurmaAlunosAlunoIngresso implements Comparator<TurmaAlunos> {
		@Override
		public int compare(TurmaAlunos o1, TurmaAlunos o2) {
			return o1.getIngressoAlunos().compareTo(o2.getIngressoAlunos());
		}
	}	
	
	public class ListaPeriodoAlunoGradeInvertido implements Comparator<TotalizadorCurso> {
		@Override
		public int compare(TotalizadorCurso o1, TotalizadorCurso o2) {
			return o2.getGradeIngresso().compareTo(o1.getGradeIngresso());
		}
	}
			
	public class PeriodoAlunoPeriodo implements Comparator<PeriodoAluno> {
		@Override
		public int compare(PeriodoAluno o1, PeriodoAluno o2) {
			return o1.getPeriodoReal().compareTo(o2.getPeriodoReal());
		}
	}
	
	public class AprovacoesQuantidadePeriodo implements Comparator<AprovacoesQuantidade> {
		@Override
		public int compare(AprovacoesQuantidade o1, AprovacoesQuantidade o2) {
			return o1.getQuantidadeAprovacoes().compareTo(o2.getQuantidadeAprovacoes());
		}
	}
	
	public class ElementoGraficoNumero implements Comparator<ElementoGrafico> {
		@Override
		public int compare(ElementoGrafico o1, ElementoGrafico o2) {
			return o1.getNumero().compareTo(o2.getNumero());
		}
	}
	
	public class AlunoIra implements Comparator<Aluno> {
		@Override
		public int compare(Aluno o1, Aluno o2) {
			return o1.getIra().compareTo(o2.getIra());
		}
	}
	
	public class OrdenarGrupoElementoGrafico implements Comparator<GrupoElementoGrafico> {
		@Override
		public int compare(GrupoElementoGrafico o1, GrupoElementoGrafico o2) {
			return o1.getGrade().compareTo(o2.getGrade());
		}
	}
	
	public class OrdenarElementoGrafico implements Comparator<ElementoGrafico> {
		@Override
		public int compare(ElementoGrafico o1, ElementoGrafico o2) {
			return o1.getNumero().compareTo(o2.getNumero());
		}
	}
	
	public class OrdenarElementoGraficoIra implements Comparator<ElementoGrafico> {
		@Override
		public int compare(ElementoGrafico o1, ElementoGrafico o2) {
			return o1.getIra().compareTo(o2.getIra());
		}
	}
	
	
	
	
	//GrupoElementoGrafico
	//AlunoPanoramico
	
	
	public List<EventoAce> EventoAceOrdenarPeriodo(List<EventoAce> listaEventosAce){

		Collections.sort(listaEventosAce, new EventoAcePeriodo());

		return listaEventosAce;
	}

	public List<Curso> CursoOrdenarCodigo(List<Curso> listaCurso){

		Collections.sort(listaCurso, new CursoCodigo());

		return listaCurso;
	}
	
	public List<Disciplina> DisciplinaOrdenarCodigo(List<Disciplina> listaDisciplina){

		Collections.sort(listaDisciplina, new DisciplinaCodigo());

		return listaDisciplina;
	}
	
	public List<DisciplinaGradeDisciplina> DisciplinaGradeDisciplinaOrdenarCodigo(List<DisciplinaGradeDisciplina> listaDisciplinaGradeDisciplina){

		Collections.sort(listaDisciplinaGradeDisciplina, new DisciplinaGradeDisciplinaCodigo());

		return listaDisciplinaGradeDisciplina;
	}
	
	public List<DisciplinaGradeDisciplina> DisciplinaGradeDisciplinaOrdenarPeriodo(List<DisciplinaGradeDisciplina> listaDisciplinaGradeDisciplina){

		Collections.sort(listaDisciplinaGradeDisciplina, new DisciplinaGradeDisciplinaPeriodo());

		return listaDisciplinaGradeDisciplina;
	}
	
	
	public List<PeriodoAluno> PeriodoAlunoPorPeriodo(List<PeriodoAluno> listaPeriodoAluno){

		Collections.sort(listaPeriodoAluno, new PeriodoAlunoOrdenarPeriodo());

		return listaPeriodoAluno;
	}
	
	public List<ListaPeriodoAluno> PeriodoAlunoPorIngressoGeral(List<ListaPeriodoAluno> listaPeriodoAluno){

		Collections.sort(listaPeriodoAluno, new ListaPeriodoAlunoIngresso());

		return listaPeriodoAluno;
	}	
	
	public List<TurmaAlunos> TurmaAlunosPorIngressoGeral(List<TurmaAlunos> listaPeriodoAluno){

		Collections.sort(listaPeriodoAluno, new TurmaAlunosAlunoIngresso());

		return listaPeriodoAluno;
	}	
	
	public List<TotalizadorCurso> PeriodoAlunoPorGradeGeralInv(List<TotalizadorCurso> listaTotalizadorCurso){

		Collections.sort(listaTotalizadorCurso, new ListaPeriodoAlunoGradeInvertido());

		return listaTotalizadorCurso;
	}
	
	public List<PeriodoAluno> PeriodoAlunoPorPeriodoGeral(List<PeriodoAluno> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new PeriodoAlunoPeriodo());

		return lPeriodoAluno;
	}
	
	public List<AprovacoesQuantidade> AprovacoesQuantidadePeriodoGeral(List<AprovacoesQuantidade> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new AprovacoesQuantidadePeriodo());

		return lPeriodoAluno;
	}
	
	public List<ElementoGrafico> numeroElementoGrafico(List<ElementoGrafico> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new ElementoGraficoNumero());

		return lPeriodoAluno;
	}
	
	public List<Aluno> numeroAluno(List<Aluno> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new AlunoIra());

		return lPeriodoAluno;
	}
	
	public List<GrupoElementoGrafico> periodoGrupoElementoGrafico(List<GrupoElementoGrafico> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new OrdenarGrupoElementoGrafico());

		return lPeriodoAluno;
	}
	
	public List<ElementoGrafico> periodoElementoGrafico(List<ElementoGrafico> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new OrdenarElementoGrafico());

		return lPeriodoAluno;
	}
	
	public List<ElementoGrafico> periodoElementoGraficoIra(List<ElementoGrafico> lPeriodoAluno){

		Collections.sort(lPeriodoAluno, new OrdenarElementoGraficoIra());

		return lPeriodoAluno;
	}
	
	//ElementoGrafico
	
	
	
	
	
	//GrupoElementoGrafico
	
}