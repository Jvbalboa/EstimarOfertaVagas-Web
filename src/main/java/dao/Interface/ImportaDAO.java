package dao.Interface;



import java.util.List;

import model.Grade;

public interface ImportaDAO extends GenericDAO<Grade, Long> {
	public void gravarRegistros(List<Grade> gradesGravada);
	
}