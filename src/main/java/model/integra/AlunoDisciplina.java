package model.integra;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
@XmlSeeAlso({DisciplinaIntegra.class})
public class AlunoDisciplina implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DisciplinaIntegra> disciplinaList;
    
    public AlunoDisciplina()
    {
        
    }
    
    public AlunoDisciplina(List<DisciplinaIntegra> disciplinaList)
    {
        this.disciplinaList = disciplinaList;
    }

    /**
     * @return the disciplinaList
     */
    public List<DisciplinaIntegra> getDisciplina() {
        return disciplinaList;
    }

    /**
     * @param disciplinaList the disciplinaList to set
     */
    public void setDisciplina(List<DisciplinaIntegra> disciplinaList) {
        this.disciplinaList = disciplinaList;
    }
}
