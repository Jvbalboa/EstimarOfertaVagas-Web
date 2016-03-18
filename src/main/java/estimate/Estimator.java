package estimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import model.arvore.Class;
import model.arvore.ClassStatus;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;

public class Estimator {
	
	private Curriculum curriculum;
	private StudentsHistory history;
	private EstimativesResult result;

	public Estimator(Curriculum c, StudentsHistory sh) {
			this.curriculum = c;
			this.history = sh;
	}
	
/**
 * 
 * 		//Passos:
		// para cada disciplina da grade,
		    // pegar quais os seus pr�-requisitos
		      // verificar quantos alunos j� fizeram os pr�-requisitos
		      // verificar quantos alunos est�o fazendo os prerequisitos (1 ou mais)
		      
		     // para cada aluno, verificar se ele j� cursou a disciplina (o aluno pode ter feito a disciplina mas n�o o seu pr�-requisito)
		       // descontar do numero total
		
		    // permitir uma taxa de reprova��o. Ou seja, daqueles que estao cursando, quantos podem se reprovar.

			// o aluno pode estar cursando a disciplina neste semestre...
 * 
 * 	
 */
		
	public EstimativesResult populateData() {		
		this.result = new EstimativesResult(); // Fazendo com disciplinas obrigat�rias
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
		
		for (Class _class : this.curriculum.getElectives()) { //Fazendo com disciplinas eletivas
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
	 * @return -1 = nao pode cursar; 0 = aprovado; 1 = matriculado; 2 = reprovado por nota; 3 = reprovado por infrequencia; 4 = possui prerequisitos; 5 = est� matriculado nos prerequisitos
	 */
	public static int processStudentCourseStatus(Class discipline, Student student) {
		
		int retorno = -1;		
		if (student.getClasses(ClassStatus.APPROVED).containsKey(discipline)) // Se o aluno j� passou na disciplina, n�o entra na contagem
			retorno = 0;		
		else if (student.getClasses(ClassStatus.ENROLLED).containsKey(discipline)) // Se o aluno est� fazendo a disciplina, n�o precisa checar pr�-requisitos
			retorno = 1;		
		else if (student.getClasses(ClassStatus.REPROVED_GRADE).containsKey(discipline) || student.getClasses(ClassStatus.REPROVED_FREQUENCY).containsKey(discipline)) { // Se o aluno j� foi reprovado por nota ou infrequencia na disciplina, ent�o ele pode faz�-la novamente
		    						
			ArrayList<String[]> array1 = student.getClasses(ClassStatus.REPROVED_GRADE).get(discipline);
			String s1 = (array1 != null ? array1.get(array1.size()-1)[0] : "");			
			ArrayList<String[]> array2 = student.getClasses(ClassStatus.REPROVED_FREQUENCY).get(discipline);
			String s2 = (String) (array2 != null ? array2.get(array2.size()-1)[0] : "");			
			if (s1.compareTo(s2) > 1) retorno = 2; // compara qual o maior semestre
			else retorno = 3;
		}
		
		else {
			int qtdPrereqCompleted = 0;
			int qtdPrereqEnrolled = 0;
			for(Class pre : discipline.getPrerequisite()) {
				if (student.getClasses(ClassStatus.APPROVED).containsKey(pre)) 
					qtdPrereqCompleted++;				
				if (student.getClasses(ClassStatus.ENROLLED).containsKey(pre)) 
					qtdPrereqEnrolled++;
			}			
			if(qtdPrereqCompleted == discipline.getPrerequisite().size()) retorno = 4;
			else if (qtdPrereqCompleted + qtdPrereqEnrolled == discipline.getPrerequisite().size())	retorno = 5;	
		}
		return retorno;
	}	
}
