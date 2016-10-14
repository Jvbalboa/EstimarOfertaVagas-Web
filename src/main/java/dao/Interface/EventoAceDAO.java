

package dao.Interface;



import java.util.List;

import model.EventoAce;


public interface EventoAceDAO extends GenericDAO<EventoAce, Long> {
	//public ArrayList<EventoAce> buscarPorAluno(long codigo);	
	public List<EventoAce> buscarPorMatricula(String matricula);
	public int recuperarHorasConcluidasPorMatricula(String matricula);
}