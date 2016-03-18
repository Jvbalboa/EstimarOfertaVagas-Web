package dao.Interface;



import model.Aluno;

public interface AlunoDAO extends GenericDAO<Aluno, Long> {
	public Aluno buscarPorMatricula(String variavel);
	//public ArrayList<String> buscarTodosAlunoMatricula(String variavel,Long idcurso);
	//public ArrayList<String> buscarTodosAlunoNome(String variavel,Long idcurso);
	//public Aluno buscarPorNome(String variavel);
	//public ArrayList<Aluno> buscarTodosAlunoNomeObjeto(String variavel,Long idcurso);
	//public ArrayList<Aluno> buscarTodosAlunoCursoGradeObjeto(Long idCurso,Long IdGrade );
	//public ArrayList<Aluno> buscarTodosAlunoCursoObjeto(Long idCurso);
}