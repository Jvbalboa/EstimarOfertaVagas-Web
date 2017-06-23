package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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
		return manager.createQuery("DELETE FROM IRA WHERE ID_CURSO = :curso")
				.setParameter("curso", c.getId())
				.executeUpdate();

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
		return manager.merge(ira);
	}
	
	public IRA porId(long id)
	{
		return manager.find(IRA.class, id);
	}

}
