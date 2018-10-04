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
	
	public List<Historico> buscarHistoricosAprovadosUltimosTresSemestres(long idAluno) {
		List<Historico> historicosAgrupadosUltimosTresSemestres = manager.createQuery("FROM Historico WHERE id_matricula = :idAluno AND status_disciplina <> 'Matriculado' GROUP BY semestre_cursado ORDER BY semestre_cursado DESC", Historico.class)
				.setParameter("idAluno", idAluno).setMaxResults(3).getResultList();
		
		final int INDEX_ANTEPENULTIMO_SEMESTRE = historicosAgrupadosUltimosTresSemestres.size() - 1;
		String antepenultimoSemestre = historicosAgrupadosUltimosTresSemestres.get(INDEX_ANTEPENULTIMO_SEMESTRE).getSemestreCursado();
		
		return manager.createQuery("FROM Historico WHERE id_matricula = :idAluno AND status_disciplina = 'Aprovado' AND semestre_cursado >= :antepenultimoSemestre", Historico.class)
				.setParameter("idAluno", idAluno).setParameter("antepenultimoSemestre", antepenultimoSemestre).getResultList();
	}
}