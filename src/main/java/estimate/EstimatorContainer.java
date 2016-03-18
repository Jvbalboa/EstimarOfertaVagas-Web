package estimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import estimate.Estimative;
import estimate.EstimativesResult;
import estimate.Estimator;
import estimate.EstimatorContainerSource;
import model.arvore.Class;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;

public class EstimatorContainer {
	private ArrayList<EstimatorContainerSource> _listCurriculum;
	private HashMap<String, EstimativesResult> _listEstimatives;
	
	public EstimatorContainer(ArrayList<EstimatorContainerSource> list)	{
		this._listCurriculum = list;
		this._listEstimatives = new HashMap<String, EstimativesResult>();
	}
	
	public EstimativesResult populateData()	{
		HashMap<String, Estimative> estimatives = new HashMap<String, Estimative>();
		
		for(EstimatorContainerSource ecs : _listCurriculum)
		{
			Estimator estimator = new Estimator(ecs.getCurriculumForMultiple(), ecs.getSh());
			EstimativesResult result = estimator.populateData();
			
			this._listEstimatives.put(ecs.getCurriculumId(), result);
			
			for(Estimative e: result.getEstimatives())
			{
				if(estimatives.containsKey(e.getClassId()))
				{
					estimatives.get(e.getClassId()).joinEstimative(e);
				}
				else
					estimatives.put(e.getClassId(), e);
			}			
		}
		
		EstimativesResult er = new EstimativesResult();
		er.getEstimatives().addAll(estimatives.values());
		
		return er;
	}
	
	public EstimativesResult getEstimativesByCurriculum(String curriculumId)	{
		return this._listEstimatives.get(curriculumId);
	}
	
	public StudentsHistory getStudentsHistory()	{
		HashMap<String, Student> sh = new HashMap<String, Student>();
		
		for(EstimatorContainerSource ecs: this._listCurriculum)
		{
			sh.putAll(ecs.getSh().getStudents());
		}
		
		return new StudentsHistory(sh);
	}
	
	public Curriculum getCurriculum(){
		TreeSet<Class> mandatories = new TreeSet<Class>();
		TreeSet<Class> electives = new TreeSet<Class>();
		
		for(EstimatorContainerSource ecs : this._listCurriculum)
		{
			for(Class c: ecs.getCurriculumForMultiple().getMandatories().get(1))
			{
				if(!mandatories.contains(c))
					mandatories.add(c);				
				if(electives.contains(c))
					electives.remove(c);
			}
			
			for(Class c: ecs.getCurriculum().getElectives())
			{
				if(!mandatories.contains(c) && !electives.contains(c))
					electives.add(c);
			}
		}
		
		return new Curriculum(mandatories, electives);
	}
	
}