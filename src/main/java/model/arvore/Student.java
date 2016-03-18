package model.arvore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Student implements Comparable<Student>{

	private HashMap<ClassStatus, HashMap<Class, ArrayList<String[]>>> classes = new HashMap<ClassStatus, HashMap<Class, ArrayList<String[]>>>();
	private String _id;
	private String _nome;
	private String _curriculum;
	private String _course;
	private TreeSet<Integer> _coursedSemesters;

	public Student(String id) {
		this(id, "");
	}

	public Student(String id, String nome) {
		this._id = id;
		this._nome = nome;
		this._coursedSemesters = new TreeSet<Integer>();
	}	

	private float IRA(int firstSemester, int lastSemester){
		HashMap<Class, ArrayList<String[]>> cl;
		int grade = 0;
		int weight = 0;
		TreeSet<String> _IRA_skipClasses = CurriculumFactory.getCurriculum(_course, _curriculum).getIRA_skipClasses();
		cl = this.classes.get(ClassStatus.APPROVED);
		if(cl != null) for(Class c: cl.keySet()){
			if(_IRA_skipClasses.contains(c.getId()))
				continue;
			String[] exceptions = {"APR", "DISP", "A", "B", "C", "D", "E"};
			List<String> Exceptions = Arrays.asList(exceptions);			
			ArrayList<String[]> classdata = cl.get(c);
			for(String[] s2: classdata)	{				
				if(Exceptions.contains(s2[1]))
					continue;
				if(Integer.valueOf(s2[0]) < firstSemester || Integer.valueOf(s2[0]) > lastSemester)
					continue;
				grade += Integer.valueOf(s2[1]) * c.getWorkload();
				weight += c.getWorkload();
			}
		}
		cl = this.classes.get(ClassStatus.REPROVED_GRADE);
		if(cl != null) for(Class c:cl.keySet())	{
			if(_IRA_skipClasses.contains(c.getId()))
				continue;
			ArrayList<String[]> classdata = cl.get(c);
			for(String[] s2: classdata){
				if(s2[1].equals(""))
					s2[1] = "0";
				if(s2[1].equals("NC"))
					continue;
				
				try {
					Integer.valueOf(s2[1]);
				} catch (Exception e) {
					s2[1] = "0";					
					continue;
				}
				
				if(Integer.valueOf(s2[0]) < firstSemester || Integer.valueOf(s2[0]) > lastSemester)
					continue;
				grade += Integer.valueOf(s2[1]) * c.getWorkload();
				weight += c.getWorkload();
			}
		}
		cl = this.classes.get(ClassStatus.REPROVED_FREQUENCY);
		if(cl != null) for(Class c: cl.keySet()){	
			if(_IRA_skipClasses.contains(c.getId()))
				continue;
			ArrayList<String[]> classdata = cl.get(c);
			for(String[] s2: classdata){
				if(Integer.valueOf(s2[0]) < firstSemester || Integer.valueOf(s2[0]) > lastSemester)
					continue;
				weight += c.getWorkload();
			}
		}
		if(weight == 0){
			return -1;
		}
		

		if(grade == 0)
			return new BigDecimal(0.0f).setScale(2, RoundingMode.HALF_EVEN).floatValue();

		float ira = (float) grade / weight;
		return new BigDecimal(ira).setScale(2, RoundingMode.HALF_EVEN).floatValue();
	}

	public void addClass(String _class, String semester, ClassStatus status, String grade, String weight) {

		Class _class2 = ClassFactory.getClass(_course, _curriculum, _class,weight);
		if (!this.classes.containsKey(status))
			this.classes.put(status, new HashMap<Class, ArrayList<String[]>>());		
		ArrayList<String[]> a = this.classes.get(status).get(_class2);
		if(a == null) {
			a = new ArrayList<String[]>();
		}		
		String[] o = new String[2]; //Array que guarda as informacoes de cada vez que o aluno fez a disciplina (Semestre, Disciplina...)		
		o[0] = semester;//adiciona o semestre cursado
		o[1] = grade; //adiciona a nota da disciplina
		a.add(o);
		this.classes.get(status).put(_class2, a);		
		this._coursedSemesters.add(Integer.valueOf(semester));
	}

	public String getId() { return this._id; }

	public int getFirstSemester() { return this._coursedSemesters.first(); }

	@Override
	public String toString() {
		String output = "Student " + this._id;
		for (ClassStatus status : this.classes.keySet()) {
			output += ", "+status.name()+"=";
			for (Object string : this.classes.get(status).keySet().toArray()) 
				output += "," + string;
		}
		return output;
	}

	public HashMap<Class, ArrayList<String[]>> getClasses(ClassStatus cs) {
		if (!this.classes.containsKey(cs))
			this.classes.put(cs, new HashMap<Class, ArrayList<String[]>>());
		return classes.get(cs);
	}	

	public String get_ingresso() {
		String periodoInicioLocal = "";
		if (_id != null){
			periodoInicioLocal =  _id.substring(0,4) ;
		}
		return periodoInicioLocal;
	}

	@Override
	public int compareTo(Student s)	{
		return s._id.compareTo(this._id);
	}

	public TreeSet<Integer> getCoursedSemesters()	{
		return this._coursedSemesters;
	}		

	public String getNome() {
		return _nome;
	}

	public void setNome(String _nome) {
		this._nome = _nome;
	}

	public String getCurriculum() {
		return _curriculum;
	}

	public void setCurriculum(String curriculum) {
		this._curriculum = curriculum;
	}

	public void setCourse(String course)	{
		this._course = course;
	}

	public String getCourse()	{
		return this._course;
	}

	public float getIRA() { return IRA(0, 99999); }
	public float getIRA(int semester) { return IRA(this._coursedSemesters.first(), semester); }
	public float getSemesterIRA(int semester) { return IRA(semester, semester); }

}
