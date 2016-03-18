package dao.Interface;


import java.util.ArrayList;

import model.GradeDisciplina;

public interface GradeDisciplinaDAO extends GenericDAO<GradeDisciplina, Long> {	
	public ArrayList<GradeDisciplina> buscarTodasGradeDisciplinaPorGrade(long codigo);
	//public GradeDisciplina buscarPorPK(String tipo,Long periodo,Long idGrade,Long idDisciplina) ;
	public GradeDisciplina buscarPorDisciplinaGrade(Long idGrade,Long idDisciplina) ;
	//public ArrayList<GradeDisciplina> buscarPorTipoGrade(Long idGrade,String tipo);
	//public ArrayList<GradeDisciplina> buscarTodasGradeDisciplinaPorGradeId(long idGrade);
	public ArrayList<GradeDisciplina> buscarPorIra(Long idGrade,boolean ira);
	//public GradeDisciplina buscarPorDisciplinaGradeIra(Long idGrade,Long idDisciplina);	
}