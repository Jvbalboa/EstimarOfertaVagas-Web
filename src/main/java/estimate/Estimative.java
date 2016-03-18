package estimate;

public class Estimative {
	
	/**
	 * Esse valor guarda quantos alunos possuem todos os pré-requisitos para a disciplina
	 */
	private int qtdHasAllPrereq;
	private float qtdHasAllPrereqWeight;
	
	/**
	 * Esse valor guarda quantos alunos podem, ao final do período, ter todos os pré-requisitos para a disciplina
	 * Ou seja, o aluno está cursando ainda algum pré-requisito
	 */
	private int qtdCanHaveAllPreq;
	private float qtdCanHaveAllPreqWeight;

	/**
	 * Código da disciplina
	 */
	private String classId;
	
	/**
	 * Esse valor guarda quantos alunos estão matriculados atualmente nesta disciplina
	 */
	private int qtdEnrolled;
	private float qtdEnrolledWeight;
	
	public int getQtdTotalWeighted() {
		return getQtdCanHaveAllPreqWeighted()+
				getQtdEnrolledWeighted()+
				getQtdHasAllPrereqWeighted()+
				getQtdReprovedGradeWeighted()+
				getQtdReprovedFreqWeighted();
	}
	
	public void joinEstimative(Estimative e)
	{
		this.qtdCanHaveAllPreq += e.qtdCanHaveAllPreq;
		this.qtdEnrolled += e.qtdEnrolled;
		this.qtdHasAllPrereq += e.getQtdHasAllPrereq();
		this.qtdReprovedFreq += e.getQtdReprovedFreq();
		this.qtdReprovedGrade += e.getQtdReprovedGrade();
	}
	
	/**
	 * Esses valores guardam quantos alunos podem fazer a disciplina mas foram reprovados nela
	 */
	private int qtdReprovedGrade;
	private int qtdReprovedFreq;
	private float qtdReprovedGradeWeight;
	private float qtdReprovedFreqWeight;
	
	public int getQtdReprovedGradeWeighted() {
		return (int) Math.ceil(qtdReprovedGradeWeight*qtdReprovedGrade);
	}
	public float getQtdReprovedGradeWeight() {
		return qtdReprovedGradeWeight;
	}
	public void setQtdReprovedGradeWeight(float qtdReprovedGradeWeight) {
		this.qtdReprovedGradeWeight = qtdReprovedGradeWeight;
	}
	public int getQtdReprovedFreqWeighted() {
		return (int) Math.ceil(qtdReprovedFreqWeight*qtdReprovedFreq);
	}
	public float getQtdReprovedFreqWeight() {
		return qtdReprovedFreqWeight;
	}
	public void setQtdReprovedFreqWeight(float qtdReprovedFreqWeight) {
		this.qtdReprovedFreqWeight = qtdReprovedFreqWeight;
	}
	public int getQtdReprovedGrade() {
		return qtdReprovedGrade;
	}
	public int getQtdReprovedFreq() {
		return qtdReprovedFreq;
	}
	public String getClassId() {
		return classId;
	}
	public int getQtdEnrolled() {
		return qtdEnrolled;
	}
	public int getQtdHasAllPrereq() {
		return qtdHasAllPrereq;
	}
	public int getQtdCanHaveAllPreq() {
		return qtdCanHaveAllPreq;
	}

	public int getQtdHasAllPrereqWeighted() {
		return (int) Math.ceil(qtdHasAllPrereqWeight*qtdHasAllPrereq);
	}
	public float getQtdHasAllPrereqWeight() {
		return qtdHasAllPrereqWeight;
	}
	public void setQtdHasAllPrereqWeight(float qtdHasAllPrereqWeighted) {
		this.qtdHasAllPrereqWeight = qtdHasAllPrereqWeighted;
	}
	public int getQtdCanHaveAllPreqWeighted() {
		return (int) Math.ceil(qtdCanHaveAllPreq*qtdCanHaveAllPreqWeight);
	}
	public float getQtdCanHaveAllPreqWeight() {
		return qtdCanHaveAllPreqWeight;
	}
	public void setQtdCanHaveAllPreqWeight(float qdtCanHaveAllPreqWeighted) {
		this.qtdCanHaveAllPreqWeight = qdtCanHaveAllPreqWeighted;
	}
	public int getQtdEnrolledWeighted() {
		return (int) Math.ceil(qtdEnrolledWeight*qtdEnrolled);
	}
	public float getQtdEnrolledWeight() {
		return qtdEnrolledWeight;
	}
	public void setQtdEnrolledWeight(float qtdEnrolledWeighted) {
		this.qtdEnrolledWeight = qtdEnrolledWeighted;
	}
	public int getQtdTotal() {
		return getQtdCanHaveAllPreq()+
				getQtdEnrolled()+
				getQtdHasAllPrereq()+
				getQtdReprovedFreq()+
				getQtdReprovedGrade();
	}

	public Estimative(String c, int hasPrereq, int qdtCanHaveAllPreq, int isEnrolled, int reprovG, int reprovF) {
		this.qtdHasAllPrereq = hasPrereq;
		this.classId = c;
		this.qtdEnrolled = isEnrolled;
		this.qtdCanHaveAllPreq = qdtCanHaveAllPreq;
		this.qtdReprovedGrade = reprovG;
		this.qtdReprovedFreq = reprovF;
	}
	
	public String toString()
	{
		String estimative = getClassId() + ": "
						+ getQtdCanHaveAllPreq() + " "
						+ getQtdEnrolled() + " "
						+ getQtdHasAllPrereq() + " "
						+ getQtdReprovedFreq() + " "
						+ getQtdReprovedGrade();
		
		return estimative;
	}
	
	public void updateEstimative(int hasPrereq, int isEnrolled, int qdtCanHaveAllPreq, int reprovG, int reprovF)
	{
		this.qtdHasAllPrereq = hasPrereq;
		this.qtdEnrolled = isEnrolled;
		this.qtdCanHaveAllPreq = qdtCanHaveAllPreq;
		this.qtdReprovedGrade = reprovG;
		this.qtdReprovedFreq = reprovF;
	}
	
}
