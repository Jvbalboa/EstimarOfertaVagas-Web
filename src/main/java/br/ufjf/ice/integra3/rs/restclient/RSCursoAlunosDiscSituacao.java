package br.ufjf.ice.integra3.rs.restclient;

import org.apache.cxf.jaxrs.client.WebClient;
import javax.ws.rs.core.MediaType;

/**
 *  Cliente para o serviço RSCursoAlunosDiscSituacao
 * 
 * @author Luis Augusto
 */
public class RSCursoAlunosDiscSituacao 
{
    // URL do serviço
    private static final String REST_URL_V1 = "http://integra.ufjf.br/integra/services/rs/alunos/curso/situacao/v1/getcursoalunosdiscsituacao";
    private static final String REST_URL_V2 = "http://integra.ufjf.br/integra/services/rs/alunos/curso/situacao/v2/getcursoalunosdiscsituacao";

    // Versão do serviço
    public enum ServiceVersion { V1, V2 };
    
    private String authToken;
    private WebClient rsClient;
    private ServiceVersion rsVersion;
    
    /**
     * 
     * @param authToken Token do usuário
     * @param version Versão do serviço
     */
    public RSCursoAlunosDiscSituacao(String authToken, ServiceVersion version)
    {
        this.authToken = authToken;
        this.rsVersion = version;
        this.createServiceClient();
    }

    /**
     * Retorna o token do usuário que está sendo usado
     * 
     * @return String
     */
    public String getAuthToken() 
    {
        return authToken;
    }

    public void setAuthToken(String authToken) 
    {
        this.authToken = authToken;
    }
    
    /**
     * Retorna a versão do serviço que está sendo usada
     * 
     * @return ServiceVersion
     */
    public ServiceVersion getServiceVersion() 
    {
        return rsVersion;
    }
    
    public void setServiceVersion(ServiceVersion version) 
    {
        this.rsVersion = version;
        this.createServiceClient();
    }
    
    /**
     * Obtém a resposta do serviço para o curso informado
     * 
     * @param curso Curso desejado. Ex: 65A
     * @return br.ufjf.ice.integra3.rs.restclient.model.CursoAlunosSituacaoResponse
     */
    public br.ufjf.ice.integra3.rs.restclient.model.CursoAlunosSituacaoResponse get(String curso)
    {
        switch (rsVersion) 
        {
            case V1: 
            {
                return this.get_v1(curso);
            }
            case V2: 
            {
                return this.get_v2(curso);
            }
            default:
                return null;
        }
    }
    
    private void createServiceClient()
    {
        if(rsVersion != null)
        {
            if (rsClient != null) 
            {
                this.rsClient.close();
            }
            
            // Criando um cliente para o serviço de acordo com a versão
            switch(rsVersion)
            {
                case V1:
                {
                    this.rsClient = WebClient.create(REST_URL_V1);
                    break;
                }
                case V2:
                {
                    this.rsClient = WebClient.create(REST_URL_V2);
                    break;
                }
            }
            
            // Cabeçalho com o token de acesso
            this.rsClient.header("IntegraToken", authToken);
        }
        else
        {
            throw new IllegalArgumentException("Uma versão do serviço deve ser escolhida");
        }
    }
    
    private br.ufjf.ice.integra3.rs.restclient.model.v1.CursoAlunosSituacaoResponse get_v1(String curso)
    {
        return this.rsClient.path(String.format("adm/%s", curso)).accept(MediaType.APPLICATION_XML).get(br.ufjf.ice.integra3.rs.restclient.model.v1.CursoAlunosSituacaoResponse.class);
    }
    
    private br.ufjf.ice.integra3.rs.restclient.model.v2.CursoAlunosSituacaoResponse get_v2(String curso)
    {
        return this.rsClient.path(String.format("adm/%s", curso)).accept(MediaType.APPLICATION_XML).get(br.ufjf.ice.integra3.rs.restclient.model.v2.CursoAlunosSituacaoResponse.class);
    }
}
