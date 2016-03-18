package model.arvore;

import java.util.HashMap;

public class CurriculumFactory {

	private static HashMap<String, Curriculum> _map = new HashMap<String, Curriculum>();
	
	public static Curriculum getCurriculum(String course, String curriculumId)	{
		return _map.get(course + ";" + curriculumId);
	}
	
	public static void putCurriculum(String course, String curriculumId, Curriculum c)	{
		_map.put(course + ";" + curriculumId, c);
	}
}
