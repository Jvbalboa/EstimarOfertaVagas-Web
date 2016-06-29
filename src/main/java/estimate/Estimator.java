package estimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import model.arvore.*;
import model.arvore.Class;

public class Estimator {
	
	private Curriculum curriculum;
	private StudentsHistory history;
	private EstimativesResult result;
	private ArrayList<ClassContainer> corequisites;
	
	public Estimator(Curriculum c, StudentsHistory sh) {
			this.curriculum = c;
			this.history = sh;
	}
	
/**
 * 
 * 		//Passos:
		// para cada disciplina da grade,
		    // pegar quais os seus pr-requisitos
		      // verificar quantos alunos j fizeram os pr-requisitos
		      // verificar quantos alunos esto fazendo os prerequisitos (1 ou mais)
		      
		     // para cada aluno, verificar se ele j cursou a disciplina (o aluno pode ter feito a disciplina mas no o seu pr-requisito)
		       // descontar do numero total
		
		    // permitir uma taxa de reprovao. Ou seja, daqueles que estao cursando, quantos podem se reprovar.

			// o aluno pode estar cursando a disciplina neste semestre...
 * 
 * 	
 */
		
	public EstimativesResult populateData() {

		// Fazendo com disciplinas obrigatrias
		this.result = new EstimativesResult();
		HashMap<Integer, TreeSet<Class>> mantadories = this.curriculum.getMandatories();
		Collection<Student> students = this.history.getStudents().values();
		for (Integer semester : mantadories.keySet()) {
			for (Class _class : mantadories.get(semester)) {
				int countCanEnroll = 0;
				int countIsEnrolled = 0;
				int countCompletePrereq = 0;
				int countReprovedGrade = 0;
				int countReprovedRI = 0;
								
				for(Student st : students) {
					int retorno = processStudentCourseStatus(_class, st);
					
					if (retorno == 1) countIsEnrolled++;
					else if (retorno == 2) countReprovedGrade++;
					else if (retorno == 3) countReprovedRI++;
					else if (retorno == 4) countCompletePrereq++;
					else if (retorno == 5) countCanEnroll++;
				}
				result.addEstimative(_class, countCompletePrereq, countCanEnroll, countIsEnrolled, countReprovedGrade, countReprovedRI);
			}
		}

		//Fazendo com disciplinas eletivas
		for (Class _class : this.curriculum.getElectives()) {
			int countCanEnroll = 0;
			int countIsEnrolled = 0;
			int countCompletePrereq = 0;
			int countReprovedGrade = 0;
			int countReprovedRI = 0;
							
			for(Student st : students) {
				int retorno = processStudentCourseStatus(_class, st);
				
				if (retorno == 1) countIsEnrolled++;
				else if (retorno == 2) countReprovedGrade++;
				else if (retorno == 3) countReprovedRI++;
				else if (retorno == 4) countCompletePrereq++;
				else if (retorno == 5) countCanEnroll++;
			}
			result.addEstimative(_class, countCompletePrereq, countCanEnroll, countIsEnrolled, countReprovedGrade, countReprovedRI);
		}
	

		return this.result;	
	}

	/**
	 * 
	 * @param discipline
	 * @param student
	 * @return -1 = nao pode cursar; 0 = aprovado; 1 = matriculado; 2 = reprovado por nota; 3 = reprovado por infrequencia; 4 = possui prerequisitos; 5 = est matriculado nos prerequisitos
	 */
	public static int processStudentCourseStatus(Class discipline, Student student) {
		int retorno = -1;
		
		// Se o aluno j passou na disciplina, no entra na contagem
		if (student.getClasses(ClassStatus.APPROVED).containsKey(discipline))
			retorno = 0;
		
		// Se o aluno est fazendo a disciplina, no precisa checar pr-requisitos
		else if (student.getClasses(ClassStatus.ENROLLED).containsKey(discipline))
			retorno = 1;
	
		// Se o aluno j foi reprovado por nota ou infrequencia na disciplina, ento ele pode faz-la novamente
		else if (student.getClasses(ClassStatus.REPROVED_GRADE).containsKey(discipline) || student.getClasses(ClassStatus.REPROVED_FREQUENCY).containsKey(discipline)) {
		    						
			ArrayList<String[]> array1 = student.getClasses(ClassStatus.REPROVED_GRADE).get(discipline);
			String s1 = (array1 != null ? array1.get(array1.size()-1)[0] : "");
			
			ArrayList<String[]> array2 = student.getClasses(ClassStatus.REPROVED_FREQUENCY).get(discipline);
			String s2 = (String) (array2 != null ? array2.get(array2.size()-1)[0] : "");
				
			// compara qual o maior semestre
			if (s1.compareTo(s2) > 1) retorno = 2;
			else retorno = 3;
		}
		
		// Se o aluno no fez ou no est fazendo a disciplina, talvez ele possa fazer no prximo perodo
		else {
			int qtdPrereqCompleted = 0;
			int qtdPrereqEnrolled = 0;
			
			TreeSet<Class> prerequisites = new TreeSet<Class>();
			
			prerequisites.addAll(discipline.getPrerequisite());
			
			//Se a disciplina possuir co-requisitos, os prerequisitos dela tambem serao levados em consideracao
			if(!discipline.getCorequisite().isEmpty())
			{
				for(Class c: discipline.getCorequisite())
					prerequisites.addAll(c.getPrerequisite());
			}
				
			
			for(Class pre : prerequisites) {
				if (student.getClasses(ClassStatus.APPROVED).containsKey(pre)) 
					qtdPrereqCompleted++;
				
				if (student.getClasses(ClassStatus.ENROLLED).containsKey(pre)) 
					qtdPrereqEnrolled++;
			}
			
			System.out.println(prerequisites);
			
			if(qtdPrereqCompleted == prerequisites.size()) retorno = 4;
			else if (qtdPrereqCompleted + qtdPrereqEnrolled == prerequisites.size())	retorno = 5;
	
		}
		
		return retorno;
	}
	
	
}