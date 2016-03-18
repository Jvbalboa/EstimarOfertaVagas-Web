package dao.Interface;



import java.util.ArrayList;

import model.Grade;

public interface GradeDAO extends GenericDAO<Grade, Long> {		
	public ArrayList<String> buscarTodosCodigosGrade(String variavel,long idCurso);
	public Grade buscarPorCodigoGrade(String variavel);
	public ArrayList<Grade> buscarTodosCodigosGradePorCurso(long idCurso);	
}