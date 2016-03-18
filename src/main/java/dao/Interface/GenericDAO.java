/*ATUALIZAÇÕES*/
/*DATA = 15/05/2016   MÉTODO = completeText 					ANALISTA = Tliner Friaça Castro			*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/


package dao.Interface;

import java.io.Serializable;
import java.util.Collection;

public interface GenericDAO<T, ID extends Serializable> {	
	public void persistir(T entity);
	public void editar(T entity);
	public void remover(T entity);
	public void removePeloId(ID id);
	public T recuperarPorId(ID id);
	public Collection<T> recuperarTodos();
}
