package controller.util;

import model.Aluno;
import model.Curso;
import model.Disciplina;
import model.Grade;
import model.Historico;
import br.ufjf.ice.integra3.rs.restclient.model.v2.AlunoCurso;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.HistoricoDAO;

public class IntergraControler {
	
	public void importadorDados (AlunoCurso alunoCurso,CursoDAO cursoDAO,AlunoDAO alunoDAO,DisciplinaDAO disciplinaDAO,HistoricoDAO historicoDAO,GradeDAO gradeDAO){
		Curso curso = new Curso();		
		if ( cursoDAO.buscarPorCodigo(alunoCurso.getCurso()) == null){	
			curso.setCodigo(alunoCurso.getCurso());
			cursoDAO.persistir(curso);
		}
		else {
			curso = cursoDAO.buscarPorCodigo(alunoCurso.getCurso());
		}		 
		Grade grade = null;
		for(Grade gradeQuestao : curso.getGrupoGrades()){
			if (gradeQuestao.getCodigo().equals(alunoCurso.getCurriculo())){
				grade = gradeQuestao;
				break;
			}
		}
		if (grade == null){
			grade = new Grade();
			grade.setCodigo(alunoCurso.getCurriculo());
			grade.setCurso(curso);
			grade.setHorasAce(0);
			grade.setHorasEletivas(0);
			grade.setHorasOpcionais(0);
			grade.setNumeroMaximoPeriodos(0);
			grade.setPeriodoInicio(1);
			gradeDAO.persistir(grade);
		}
		Aluno aluno = null;
		//aluno = alunoDAO.buscarPorMatricula(alunoCurso.getMatricula());
		
		for (Aluno alunoQuestao : grade.getGrupoAlunos()){
			if(alunoQuestao.getMatricula().equals(alunoCurso.getMatricula())){
				aluno = alunoQuestao;
				break;
			}
		}
		
		
		
		if ( aluno == null){
			aluno = new Aluno();
			aluno.setMatricula(alunoCurso.getMatricula());
			aluno.setNome(alunoCurso.getNome());
			aluno.setGrade(grade);
			aluno.setCurso(curso);
			alunoDAO.persistir(aluno);
		} 

		for (br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegra : alunoCurso.getDisciplinas().getDisciplina()) {
			Disciplina disciplina = new Disciplina();
			if ( disciplinaDAO.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina()) == null){
				disciplina.setCodigo(disciplinaIntegra.getDisciplina());
				disciplina.setCargaHoraria(Integer.parseInt(disciplinaIntegra.getHorasAula()));
				disciplinaDAO.persistir(disciplina);
			}
			else {
				disciplina = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina());
			}
			Historico historico = new Historico();
			historico.setAluno(aluno);
			historico.setDisciplina(disciplina);
			if (disciplinaIntegra.getNota() == null || disciplinaIntegra.getNota().trim().equals("") ){
				historico.setNota("0");
			}
			else {
				historico.setNota(disciplinaIntegra.getNota().trim());
			}
			historico.setSemestreCursado(disciplinaIntegra.getAnoSemestre());
			historico.setStatusDisciplina(disciplinaIntegra.getSituacao());
			historicoDAO.persistir(historico); 	
		}
	}

}
