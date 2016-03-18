package dao.Interface;



import java.util.ArrayList;

import model.PreRequisito;

public interface PreRequisitoDAO extends GenericDAO<PreRequisito, Long> {
	public ArrayList<PreRequisito> buscarPorTodosCodigoGradeDisc(Long codigo);
	public Integer buscarPorDisciplanaGradeId(Long idGrade ,Long idDisciplina) ;
	public ArrayList<PreRequisito> buscarPorTodosCodigoDisciplina(Long codigo);	
}