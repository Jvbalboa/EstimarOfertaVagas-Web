package br.ufjf.coordenacao.sistemagestaocurso.util.arvore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.ufjf.coordenacao.sistemagestaocurso.model.*;


public class EstruturaArvore implements Serializable{

	private static final long serialVersionUID = 1L;

	private static EstruturaArvore instancia;
	
	private String loginUtilizado;
	private List<ImportarArvore> todasArvores = new ArrayList<ImportarArvore>();

	public static synchronized EstruturaArvore getInstance(){

		if (instancia == null){			
			instancia = new EstruturaArvore();
		}
		return instancia;
	}

	
	public ImportarArvore recuperarArvoreSemProcessar(Grade grade){
		for(ImportarArvore importarArvore:todasArvores){
			if(importarArvore.getGrade().getId() == grade.getId()){
				return importarArvore;
			}
		}
		return null;
	}

	public ImportarArvore recuperarArvore (Grade grade,boolean consideraCo){	






		for(ImportarArvore importarArvore:todasArvores){
			if(importarArvore.getGrade().getId() == grade.getId()){
				importarArvore.importarDisciplinas(grade,consideraCo);
				return importarArvore;
			}
		}	



		ImportarArvore importador = new ImportarArvore(); 
		importador.setGrade(grade);

		/*if (reseta == true){
			grade = gradeDAO.recuperarPorId(grade.getId());
			reseta = false;
			//importador.setResetarStance(true);
			gradeDAO = new GradeDAOImpl();

		}*/

		todasArvores.add(importador);
		importador.importarDisciplinas(grade,consideraCo);

		List<Aluno> listaAluno = grade.getGrupoAlunos();

		//List<Aluno> listaAluno = alunoDAO.buscarTodosAlunoCursoGradeObjeto(grade.getCurso().getId(), grade.getId());
		for(Aluno aluno:listaAluno){
			List<Historico> listaHistorico = aluno.getGrupoHistorico();
			importador.importarHistorico(listaHistorico);
		}
		return importador;
	}

	public void removerEstrutura (Grade grade){

		

		for(ImportarArvore importarArvore:todasArvores){
			if(importarArvore.getGrade().getId() == grade.getId()){
				todasArvores.remove(importarArvore);
				return;
			}
		}
	}

	public static EstruturaArvore getInstancia() {
		return instancia;
	}

	public static void setInstancia(EstruturaArvore instancia) {
		EstruturaArvore.instancia = instancia;
	}

	

	public String getLoginUtilizado() {
		return loginUtilizado;
	}

	public void setLoginUtilizado(String loginUtilizado) {
		this.loginUtilizado = loginUtilizado;
	}

	public List<ImportarArvore> getTodasArvores() {
		return todasArvores;
	}

	public void setTodasArvores(List<ImportarArvore> todasArvores) {
		this.todasArvores = todasArvores;
	}
}
