package model.integra;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Augusto
 */
@XmlRootElement
public class CursoAlunosSituacaoResponse extends model.integra.CursoAlunosSituacaoResponse2 implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String responseStatus;
    private List<AlunoCurso> alunosList;
    
    public CursoAlunosSituacaoResponse()
    {
        
    }
    
    public CursoAlunosSituacaoResponse(String responseStatus)
    {
        this.responseStatus = responseStatus;
    }
    
    public CursoAlunosSituacaoResponse(List<AlunoCurso> alunosCursoList)
    {
        this.alunosList = alunosCursoList;
    }

    /**
     * @return the responseStatus
     */
    public String getResponseStatus() {
        return responseStatus;
    }

    /**
     * @param responseStatus the responseStatus to set
     */
    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
    
    /**
     * @return the alunosList
     */
    public List<AlunoCurso> getAluno() {
        return alunosList;
    }

    /**
     * @param alunosList the alunosList to set
     */
    public void setAluno(List<AlunoCurso> alunosList) {
        this.alunosList = alunosList;
    }
}
