package model.integra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
@XmlSeeAlso({AlunoDisciplina.class})
public class AlunoCurso implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String matricula;
    private String nome;
    private String curso;
    private String curriculo;
    private AlunoDisciplina disciplinaList;

    public AlunoCurso()
    {
        
    }
    
    public AlunoCurso(String matricula, String nome, String curso, String curriculo, AlunoDisciplina disciplinaList)
    {
        this.matricula = matricula;
        this.nome = nome;
        this.curso = curso;
        this.curriculo = curriculo;
        this.disciplinaList = disciplinaList;
    }

    /**
     * @return the curso
     */
    public String getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }
    
    /**
     * @return the curriculo
     */
    public String getCurriculo() {
        return curriculo;
    }

    /**
     * @param curriculo the curriculo to set
     */
    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the disciplinaList
     */
    public AlunoDisciplina getDisciplinas() {
        return disciplinaList;
    }

    /**
     * @param disciplinaList the disciplinaList to set
     */
    public void setDisciplinas(AlunoDisciplina disciplinaList) {
        this.disciplinaList = disciplinaList;
    }
}
