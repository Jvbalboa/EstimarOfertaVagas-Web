/*ATUALIZAÇÕES*/
/*DATA = 15/05/2016   MÉTODO = completeText 					ANALISTA = Tliner Friaça Castro			*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/


package dao;



import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.Interface.GenericDAO;



public class HibernateGenericDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {
	
//========================================================= VARIABLES ==================================================================================//
	
	 EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaGestaoCurso");
	 EntityManager em = emf.createEntityManager();
	 
	
//======================================================================================================================================================//
//============================================================ METHODS =================================================================================//
		
	 public void persistir(T entity) {
		try {		
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editar(T entity) {
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remover(T entity) {
		try {
			em.getTransaction().begin();
			em.remove(em.merge(entity));
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removePeloId(ID id) {
		try {
			T entity = recuperarPorId(id);
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public T recuperarPorId(ID id) {
		try {
			em.clear();
			em.getTransaction().begin();
			T obj = em.find(recuperarTipoClasse(), id);
			em.getTransaction().commit();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<T> recuperarTodos() {
		List<T> list = null;
		try {
			em.getTransaction().begin();
			 list = em.createQuery("FROM " + recuperarTipoClasse().getName()).getResultList();
			em.getTransaction().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}


	@SuppressWarnings("unchecked")
	private Class<T> recuperarTipoClasse() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
//======================================================================================================================================================//
	
}
