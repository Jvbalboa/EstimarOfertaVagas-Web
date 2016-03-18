package dao.Interface;



import java.util.ArrayList;

import model.Equivalencia;

public interface EquivalenciaDAO extends GenericDAO<Equivalencia, Long> {	
	public ArrayList<Equivalencia> buscarTodasEquivalenciasPorGrade(long codigo);
	public Equivalencia buscarPorEquivalencia(long idDisciplinaEsquerda,long idDisciplinaDireita,long idGrade);
}