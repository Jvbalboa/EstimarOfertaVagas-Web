package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.ufjf.coordenacao.sistemagestaocurso.model.Disciplina;

public class DisciplinaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public DisciplinaRepository() 	{ }
	
	public DisciplinaRepository(EntityManager manager)
	{
		this.manager = manager;
	}
	
	public Disciplina porid(long id) {
		return manager.find(Disciplina.class, id);
	}

	public Disciplina persistir(Disciplina objeto) {
		return manager.merge(objeto);
	}

	public void remover(Disciplina objeto) {
		manager.remove(manager.contains(objeto) ? objeto : manager.merge(objeto));
	}

	public List<Disciplina> listarTodos() {
		return manager.createQuery("FROM Disciplina", Disciplina.class).getResultList();
	}
	
	public List<Disciplina> buscarTodosDisciplinaCodigo(String codigo) {
		return manager.createQuery("FROM Disciplina WHERE codigo like :codigo", Disciplina.class)
				.setParameter("codigo", "%" + codigo + "%").getResultList();
	}

	public List<String> buscarTodosCodigosDisciplina(String variavel) {
		return manager
				.createQuery("Select codigo FROM Disciplina WHERE codigo like :codigo order by codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();

	}

	public Disciplina buscarPorCodigoDisciplina(String variavel) {
		try {
			return manager.createQuery("FROM Disciplina WHERE codigo = :codigo", Disciplina.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<String> buscarTodosNomesDisciplina(String variavel) {
		return manager.createQuery("Select nome FROM Disciplina WHERE nome like :codigo", String.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();
	}

	public List<Disciplina> buscarTodosNomesDisciplinaObjeto(String variavel) {
		return manager.createQuery("FROM Disciplina WHERE nome like :codigo", Disciplina.class)
				.setParameter("codigo", "%" + variavel + "%").getResultList();
	}

	public Disciplina buscarPorNomeDisciplina(String variavel) {
		try {
			return manager.createQuery("FROM Disciplina WHERE nome = :codigo", Disciplina.class)
					.setParameter("codigo", variavel).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
