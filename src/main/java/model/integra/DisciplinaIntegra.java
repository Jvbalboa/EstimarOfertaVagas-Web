package model.integra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
public class DisciplinaIntegra implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String disciplina;
    private String anoSemestre;
    private String nota;
    private String situacao;
    private String horasAula;
    
    public DisciplinaIntegra()
    {
        
    }
    
    public DisciplinaIntegra(String disciplina, String anoSemestre, String nota, String situacao, String horasAula)
    {
        this.disciplina = disciplina;
        this.anoSemestre = anoSemestre;
        this.nota = nota;
        this.situacao = situacao;
        this.horasAula = horasAula;
    }

    /**
     * @return the disciplina
     */
    public String getDisciplina() {
        return disciplina;
    }

    /**
     * @param disciplina the disciplina to set
     */
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * @return the anoSemestre
     */
    public String getAnoSemestre() {
        return anoSemestre;
    }

    /**
     * @param anoSemestre the anoSemestre to set
     */
    public void setAnoSemestre(String anoSemestre) {
        this.anoSemestre = anoSemestre;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param nota the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the horasAula
     */
    public String getHorasAula() {
        return horasAula;
    }

    /**
     * @param horasAula the horasAula to set
     */
    public void setHorasAula(String horasAula) {
        this.horasAula = horasAula;
    }
}