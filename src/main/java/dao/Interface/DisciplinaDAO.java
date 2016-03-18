package dao.Interface;



import java.util.ArrayList;

import model.Disciplina;

public interface DisciplinaDAO extends GenericDAO<Disciplina, Long> {
	
	public ArrayList<String> buscarTodosCodigosDisciplina(String variavel);
	public Disciplina buscarPorCodigoDisciplina(String variavel) ;
	public ArrayList<String> buscarTodosNomesDisciplina(String variavel);
	public Disciplina buscarPorNomeDisciplina(String variavel) ;
	public ArrayList<Disciplina> buscarTodosNomesDisciplinaObjeto(String variavel);
	
}