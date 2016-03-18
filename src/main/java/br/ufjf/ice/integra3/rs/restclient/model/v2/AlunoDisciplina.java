package br.ufjf.ice.integra3.rs.restclient.model.v2;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
@XmlSeeAlso({Disciplina.class})
public class AlunoDisciplina implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Disciplina> disciplinaList;
    
    public AlunoDisciplina()
    {
        
    }
    
    public AlunoDisciplina(List<Disciplina> disciplinaList)
    {
        this.disciplinaList = disciplinaList;
    }

    /**
     * @return the disciplinaList
     */
    public List<Disciplina> getDisciplina() {
        return disciplinaList;
    }

    /**
     * @param disciplinaList the disciplinaList to set
     */
    public void setDisciplina(List<Disciplina> disciplinaList) {
        this.disciplinaList = disciplinaList;
    }
}
