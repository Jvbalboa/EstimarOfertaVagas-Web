package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.ufjf.coordenacao.sistemagestaocurso.model.*;

public class IraRepository implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	public List<IRA> porAluno(Aluno a)
	{
		try {
			return (List<IRA>) manager.createQuery("FROM IRA where ID_ALUNO = :matricula").setParameter(0, a.getId())
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public int deletarTodosCurso(Curso c)
	{
		EntityTransaction transaction = null;
		int deletar;
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			deletar = manager.createQuery("DELETE FROM IRA WHERE ID_CURSO = :curso")
					.setParameter("curso", c.getId())
					.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return deletar;

	}
	
	public int deletarTodosCursoSemCommit(Curso c)
	{
		EntityTransaction transaction = null;
		int deletar;
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			deletar = manager.createQuery("DELETE FROM IRA WHERE ID_CURSO = :curso")
					.setParameter("curso", c.getId())
					.executeUpdate();
			//transaction.commit();
		} catch (Exception e) {
			//transaction.rollback();
			throw e;
		}
		
		return deletar;

	}
	
	@SuppressWarnings("unchecked")
	public List<IRA> deletarTodosCursoV2(Curso c) throws Exception
	{
		EntityTransaction transaction = null;
		List<IRA> listaIras = new  ArrayList<IRA>();
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			
			listaIras = (List<IRA>) manager.createQuery("FROM IRA WHERE ID_CURSO = :curso")
						.setParameter("curso", c.getId())
						.getResultList();
			
			for(int i = 0; i < listaIras.size(); i++) {
				listaIras.set(i,(IRA)listaIras.get(i).clone());
			}
			
			manager.createQuery("DELETE FROM IRA WHERE ID_CURSO = :curso")
					.setParameter("curso", c.getId())
					.executeUpdate();
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return listaIras;

	}
	
	public int deletarTodosGrade(Grade g)
	{
		return manager.createQuery("DELETE FROM IRA WHERE ID_GRADE = :grade")
				.setParameter("grade", g.getId())
				.executeUpdate();
	}
	
	public int deletarTodosAluno(Aluno a)
	{
		return manager.createQuery("DELETE FROM IRA WHERE ID_ALUNO = :aluno")
				.setParameter("aluno", a.getId())
				.executeUpdate();
	}
	
	public IRA persistir(IRA ira)
	{
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			ira = manager.merge(ira);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
		
		return ira;
	}
	
	public void persistir(List<IRA> iras)
	{
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			//iras = manager.merge(iras);
			for(IRA ira : iras) {
				ira = manager.merge(ira);
				//manager.flush();
				//manager.clear();
			}
			transaction.commit();
		} catch (Exception e) {
			//transaction.rollback();
			throw e;
		}
		
	}
	
	public void persistirSemCommit(List<IRA> iras)
	{
		EntityTransaction transaction = null;
		
		try {
			transaction = manager.getTransaction();
			if (!transaction.isActive())
				transaction.begin();
			//iras = manager.merge(iras);
			for(IRA ira : iras) {
				ira = manager.merge(ira);
				//manager.flush();
				//manager.clear();
			}
			//transaction.commit();
		} catch (Exception e) {
			//transaction.rollback();
			throw e;
		}
		
	}
	
	public IRA porId(long id)
	{
		return manager.find(IRA.class, id);
	}

}
