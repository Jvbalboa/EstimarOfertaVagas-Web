package dao.Interface;



import java.util.List;

import model.PessoaCurso;

public interface PessoaCursoDAO extends GenericDAO<PessoaCurso, Long> {
	public List<PessoaCurso> buscarTodasPessoaCursoPorPessoa(long codigo);	
	public void resetDAO();
}