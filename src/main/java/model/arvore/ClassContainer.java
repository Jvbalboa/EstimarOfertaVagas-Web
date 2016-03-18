package model.arvore;

import java.util.ArrayList;

public class ClassContainer extends Class {

	private ArrayList<Class> classes;
	
	public ClassContainer(String containerId) {
		super(containerId, 0);
		this.classes = new ArrayList<Class>();
	}
	
	public void addClass(Class c){
		this.classes.add(c);
		this.setWorkload(this.getWorkload() + c.getWorkload());
		
		for(Class cl: c.getPrerequisite()){
			if(!this.prerequisite.contains(cl))
				this.prerequisite.add(cl);
		}		
		this.id += " " + c.getId();
	}
	
	@Override
	public String getId()
	{
		String s = "[";		
		for(Class c: classes){
			s += " " + c.getId();
		}
		s += "]";		
		return s;
	}	
	public ArrayList<Class> getClasses()
	{
		return this.classes;
	}	
}
