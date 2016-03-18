package model.arvore;

import java.util.HashMap;
public class StudentsHistory {
	private HashMap<String, Student> _students = new HashMap<String, Student>();
	
	public StudentsHistory() {}
	
	public StudentsHistory(HashMap<String, Student> students){
		this._students = students;
	}
	
	public void add(
			String id,             //matricula aluno
			String nome,           //nome aluno
			String course,         //codigo curso
			String curriculum,     //codigo grade
			String semester,       //semetre cursado
			String _class,         //codigo disciplina
			ClassStatus status,    //status disciplina
			String grade,          //nota disciplina
			String weight         //carga hor‡ria disciplina			
			) {
		Student st = this._students.get(id);
		if (st == null) {
			st = new Student(id);
			st.setNome(nome);
			st.setCurriculum(curriculum);
			st.setCourse(course);
			this._students.put(id, st);
		}
		st.addClass(_class, semester, status, grade, weight);
	}
	
	public HashMap<String, Student> getStudents() {
		return this._students;
	}

	@Override
	public String toString() {
		String output = "";
		for (Student student : this._students.values()) {
			output += student.toString() + "\n";
		}
		return output;
	}
}
