package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.EventoAce;

import java.util.List;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class EventoAceRepository implements Serializable {

	@Inject
	private EntityManager manager;
	
	public EventoAceRepository(){ };
	
	public EventoAceRepository(EntityManager manager)
	{
		this.manager = manager;
	}
	
	private static final long serialVersionUID = 1L;

	public EventoAce porid(long id) {
		return manager.find(EventoAce.class, id);
	}

	public EventoAce persistir(EventoAce objeto) {
		return manager.merge(objeto);
	}

	public void remover(EventoAce objeto) {
		manager.remove(manager.contains(objeto) ? objeto : manager.merge(objeto));
	}

	public List<EventoAce> listarTodos() {
		return manager.createQuery("FROM EventoAce", EventoAce.class).getResultList();
	}

	public List<EventoAce> buscarPorAluno(long codigo) {
		return manager.createQuery("FROM EventoAce WHERE id_aluno = :codigo order by periodo", EventoAce.class)
				.setParameter("codigo", codigo).getResultList();

	}

	public List<EventoAce> buscarPorMatricula(String matricula) {
		return manager.createQuery("FROM EventoAce WHERE matricula = :matricula", EventoAce.class)
				.setParameter("matricula", matricula).getResultList();
	}

	public int recuperarHorasConcluidasPorMatricula(String matricula) {
		BigDecimal result = ((BigDecimal) manager
				.createNativeQuery("SELECT SUM( HORAS ) AS horas_totais FROM `EventoAce` WHERE MATRICULA = :matricula")
				.setParameter("matricula", matricula).getSingleResult());

		if (result != null)
			return result.intValue();
		else
			return 0;
	}

}