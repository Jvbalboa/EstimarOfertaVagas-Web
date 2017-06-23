package br.ufjf.coordenacao.sistemagestaocurso.repository;

import br.ufjf.coordenacao.sistemagestaocurso.model.Historico;
import java.util.List;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class HistoricoRepository implements Serializable {

	@Inject
	private EntityManager manager;

	private static final long serialVersionUID = 1L;

	public Historico porid(long id) {
		return manager.find(Historico.class, id);
	}

	public Historico persistir(Historico objeto) {
		return manager.merge(objeto);
	}

	public void remover(Historico objeto) {
		manager.remove(manager.contains(objeto) ? objeto : manager.merge(objeto));
	}

	public List<Historico> listarTodos() {
		return manager.createQuery("FROM Historico", Historico.class).getResultList();
	}
	
	public List<Historico> buscarTodosHistoricosPorMatricula(long codigo){
		return manager.createQuery("FROM Historico WHERE id_matricula = :codigo", Historico.class)
					.setParameter("codigo",  codigo )
					.getResultList();
	}

	public List<Historico> buscarTodosHistoricosPorSemestre(String codigo){
		return manager.createQuery("FROM Historico WHERE semestre_cursado = :codigo order by id_matricula", Historico.class)
					.setParameter("codigo",  codigo )
					.getResultList();
	}

}