package model.arvore;

import java.util.ArrayList;
import java.util.List;

public class Class implements Comparable<Class> {

	protected String id;
	protected ArrayList<Class> prerequisite = new ArrayList<Class>();
	private int workload;
	protected List<Class> corequ;
	
	public Class(String id, int weight){
		this.id = id;
		this.workload = weight;
		this.corequ = new ArrayList<Class>();
	}	
	
	public Class(String id) {
		this(id, 4);
	}
	
	public void addPrerequisite(Class c) {
		this.prerequisite.add(c);
	}

	public void addCorequisite(Class c)	{
		this.corequ.add(c);
	}
	
	public List<Class> getCorequisite()	{
		return this.corequ;
	}
	public ArrayList<Class> getPrerequisite() {
		return this.prerequisite;
	}
	
	public String getId() { return id; }
	
	public int getWorkload() { return this.workload; }
	public void setWorkload(int workload) { this.workload = workload; }
	
	
	@Override
	public String toString() {
		String o = id + "(" + workload + ")";
		if (!prerequisite.isEmpty()) {
			o += " -> [pre  ";
			for (Class _class : prerequisite) o += _class.getId() +",";
			o = o.substring(0,o.length()-1) + "]";
		}
		return o;
	}

	@Override
	public int compareTo(Class o) {
		return o.getId().compareTo(id);
	}
	
}
