package estimate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import model.arvore.Class;
import model.arvore.*;

public class StudentCoursePlan {

	private HashMap<Integer, TreeSet<Class>> _period;
	private int[] _periodWorkload;
	private int _maxWorkload;
	private int _currentPeriod;
	private Student _student;
	private HashMap<Integer, TreeSet<Class>> _notCompleted;
	private boolean _invert;
	private boolean _enrolled;

	// Construtor recebe o aluno, a grade de disciplinas e o maximo de creditos
	// por periodo, um parametro que server para inverter periodos impares e pares, e outro parametro para mostrar 
	// disciplinas matriculadas.
	public StudentCoursePlan(Student st, Curriculum cur, int maxWorkload, boolean invert, boolean showEnrolled) {
		_maxWorkload = maxWorkload;
		_currentPeriod = 0;
		_student = st;
		_period = new HashMap<Integer, TreeSet<Class>>();
		_notCompleted = new HashMap<Integer, TreeSet<Class>>();
		_invert = invert;
		_enrolled = showEnrolled;
		//_skipClasses = new ArrayList<Class>();
		
		//Cria os containers de disciplinas que s‹o corequisitos
		//TODO verificar se h‡ alguma incompatibilidade e evitar 2 "for"
		for(int i: cur.getMandatories().keySet())
		{
			//for(Class c: _cur.getMandatories().get(i))
			Iterator<Class> it = cur.getMandatories().get(i).iterator();
			
			ArrayList<Class> duplicated = new ArrayList<Class>();
			ArrayList<Class> insert = new ArrayList<Class>();
			while(it.hasNext())
			{			
				Class c = it.next();
				if(!c.getCorequisite().isEmpty())
				{
					ClassContainer cc = new ClassContainer("Co-requisitos");
					cc.addClass(c);
					
					for(Class cr: c.getCorequisite())
					{
						//_cur.getMandatories().get(i).remove(cr);
						//it.remove();
						duplicated.add(cr);
						cc.addClass(cr);
					}
					
					//_cur.getMandatories().get(i).remove(c);
					it.remove();
					insert.add(cc);
				}
			}
			
			cur.getMandatories().get(i).removeAll(duplicated);
			cur.getMandatories().get(i).addAll(insert);
		}
		
		
		// Retira do curriculum as disciplinas que ainda nao forma cursadas
		int courseCount = 1;
		
		
		for (int i : cur.getMandatories().keySet()) 
		{
			
			TreeSet<Class> t = new TreeSet<Class>();
			for (Class c : cur.getMandatories().get(i)) {
				if (!c.getCorequisite().isEmpty()) {
					ClassContainer cc = new ClassContainer(c.getId());
					cc.addClass(c);

					for (Class cr : c.getCorequisite()) {
						cur.getMandatories().get(i).remove(cr);
						cc.addClass(cr);
					}
				}

				if (c instanceof ClassContainer) {
					ClassContainer cc = (ClassContainer) c;

					for (Iterator<Class> iterator = cc.getClasses().iterator(); iterator.hasNext();) {

						Class cl = iterator.next();

						if (st.getClasses(ClassStatus.APPROVED).containsKey(cl)
								|| st.getClasses(ClassStatus.ENROLLED).containsKey(cl)) {

							cc.setWorkload(cc.getWorkload() - cl.getWorkload());
							for (Class clp : cl.getPrerequisite()) {
								cc.getPrerequisite().remove(clp);
							}

							iterator.remove();
						}
					}

					/*if (cc.getClasses().size() == 0)
						continue;
					*/
					if (cc.getClasses().size() == 1) {
						t.add(cc.getClasses().get(0));
						courseCount++;
					} else {
						t.add(cc);
						courseCount++;
					}

				}

				else if ((!st.getClasses(ClassStatus.APPROVED).containsKey(c) && !st.getClasses(ClassStatus.ENROLLED).containsKey(c))   	) {
					t.add(c);
					courseCount++;
				}
			}
			if (!t.isEmpty()) {
				_notCompleted.put(i, t);
			}
		}

		_periodWorkload = new int[courseCount];

		//Trata as disciplinas matriculadas
		
		if (_enrolled) {
			TreeSet<Class> enrolledClasses = new TreeSet<Class>();

			for (Class cl : st.getClasses(ClassStatus.ENROLLED).keySet()) {
				enrolledClasses.add(cl);
				_periodWorkload[0] += cl.getWorkload();
				//_skipClasses.addAll(cl.getPrerequisite());
				
			}
			
			_period.put(0, enrolledClasses);
		}
		
			

		if (_periodWorkload[0] >= _maxWorkload)
			_currentPeriod++;
	}

	// Verifica qual o periodo minimo que o aluno pode cursar aquela disciplina
	private int checkPrereq(Class c) {
		if (c instanceof ClassContainer)
			return checkPrereq((ClassContainer) c);

		// Se ele ja esteve matriculado nela antes, entao ele pode cursar no
		// primeiro periodo livre
		if (_student.getClasses(ClassStatus.REPROVED_GRADE).containsKey(c)
				|| _student.getClasses(ClassStatus.REPROVED_FREQUENCY).containsKey(c))
			return _currentPeriod;

		// Caso nao, ele verifica o maior periodo dos prerequisitos e compara
		// com o primeiro periodo livre, e retorna o maior.\
		int i = 0;
		for (Class cl : c.getPrerequisite()) {
			if (!_student.getClasses(ClassStatus.APPROVED).containsKey(cl) && (!_student.getClasses(ClassStatus.ENROLLED).containsKey(cl))
					|| (_student.getClasses(ClassStatus.ENROLLED).containsKey(cl) && _enrolled)) {
				for (int p : _period.keySet()) {
					if (_period.get(p).contains(cl))
						if (p >= i) {
							i = p;
							if (i >= _currentPeriod)
								i++;
						}
				}
			}
		}
		return (_currentPeriod > i ? _currentPeriod : i);
	}

	private int checkPrereq(ClassContainer c) {
		int period = 0;
		for (Class cl : c.getClasses()) {
			period = Math.max(period, checkPrereq(cl));
		}
		return Math.max(period, _currentPeriod);
	}

	public void add(Class c, boolean par) {
		int period = checkPrereq(c);

		if (par) {
			if (period % 2 != 0)
				period++;
		} else {
			if (period % 2 == 0)
				period++;
		}

		while ((_periodWorkload[period] + c.getWorkload() > _maxWorkload) && (_periodWorkload[period] != 0))
			period += 2;

		_periodWorkload[period] += c.getWorkload();

		if (_period.containsKey(period)) {
			if (c instanceof ClassContainer) {
				for (Class cr : ((ClassContainer) c).getClasses())
					_period.get(period).add(cr);
			} else
				_period.get(period).add(c);
		} else {
			TreeSet<Class> t = new TreeSet<Class>();

			if (c instanceof ClassContainer) {
				for (Class cr : ((ClassContainer) c).getClasses())
					t.add(cr);
			} else
				t.add(c);

			_period.put(period, t);
		}

		if (_periodWorkload[_currentPeriod] == _maxWorkload)
			_currentPeriod++;
	}

	public Curriculum generate() {
		
		for (int i : _notCompleted.keySet()) {

			boolean par = (_invert == (i % 2 == 0));

			for (Class cl : _notCompleted.get(i))
			{
				add(cl, par);
				
			}
		}

		Curriculum cur = new Curriculum(_period, new TreeSet<Class>());

		return cur;
	}

	public void pulaPeriodo() {
		_currentPeriod++;
	}

}