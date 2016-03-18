package dao.Interface;



import java.util.ArrayList;

import model.Curso;

public interface CursoDAO extends GenericDAO<Curso, Long> {
	public Curso buscarPorCodigo(String variave);
	public Curso buscarPorNome(String variavel);
	public ArrayList<String> buscarTodosCodigosCurso(String variavel);
	public ArrayList<String> buscarTodosNomesCurso(String variavel);
	public ArrayList<Curso> buscarTodosNomesObjetoCurso(String variavel);	
}