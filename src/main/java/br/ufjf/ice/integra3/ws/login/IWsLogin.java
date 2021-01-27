package br.ufjf.ice.integra3.ws.login;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "IWsLogin", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/")
@XmlSeeAlso({ObjectFactory.class})
public interface IWsLogin {
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "getUserInformation", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.GetUserInformation")
  @ResponseWrapper(localName = "getUserInformationResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.GetUserInformationResponse")
  WsUserInfoResponse getUserInformation(@WebParam(name = "token", targetNamespace = "") String paramString) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @RequestWrapper(localName = "logout", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.Logout")
  @ResponseWrapper(localName = "logoutResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.LogoutResponse")
  void logout(@WebParam(name = "token", targetNamespace = "") String paramString) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "isValidProfile", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.IsValidProfile")
  @ResponseWrapper(localName = "isValidProfileResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.IsValidProfileResponse")
  boolean isValidProfile(@WebParam(name = "token", targetNamespace = "") String paramString1, @WebParam(name = "profile", targetNamespace = "") String paramString2) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "getPermissions", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.GetPermissions")
  @ResponseWrapper(localName = "getPermissionsResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.GetPermissionsResponse")
  WsPermissionResponse getPermissions(@WebParam(name = "token", targetNamespace = "") String paramString) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "loginByCurso", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.LoginByCurso")
  @ResponseWrapper(localName = "loginByCursoResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.LoginByCursoResponse")
  WsLoginResponse loginByCurso(@WebParam(name = "cpf", targetNamespace = "") String paramString1, @WebParam(name = "senha", targetNamespace = "") String paramString2, @WebParam(name = "curso", targetNamespace = "") String paramString3, @WebParam(name = "appToken", targetNamespace = "") String paramString4) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "getTokenExpiration", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.GetTokenExpiration")
  @ResponseWrapper(localName = "getTokenExpirationResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.GetTokenExpirationResponse")
  XMLGregorianCalendar getTokenExpiration(@WebParam(name = "token", targetNamespace = "") String paramString) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "login", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.Login")
  @ResponseWrapper(localName = "loginResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.LoginResponse")
  WsLoginResponse login(@WebParam(name = "cpf", targetNamespace = "") String paramString1, @WebParam(name = "senha", targetNamespace = "") String paramString2, @WebParam(name = "appToken", targetNamespace = "") String paramString3) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "isValidToken", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.IsValidToken")
  @ResponseWrapper(localName = "isValidTokenResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.IsValidTokenResponse")
  boolean isValidToken(@WebParam(name = "token", targetNamespace = "") String paramString) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "loginByMatricula", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.LoginByMatricula")
  @ResponseWrapper(localName = "loginByMatriculaResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.LoginByMatriculaResponse")
  WsLoginResponse loginByMatricula(@WebParam(name = "matricula", targetNamespace = "") String paramString1, @WebParam(name = "senha", targetNamespace = "") String paramString2, @WebParam(name = "appToken", targetNamespace = "") String paramString3) throws IntegraSoapServiceException_Exception;
  
  @WebMethod
  @WebResult(targetNamespace = "")
  @RequestWrapper(localName = "updateUserGroup", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.UpdateUserGroup")
  @ResponseWrapper(localName = "updateUserGroupResponse", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", className = "br.ufjf.ice.integra3.ws.login.UpdateUserGroupResponse")
  boolean updateUserGroup(@WebParam(name = "idUser", targetNamespace = "") int paramInt, @WebParam(name = "token", targetNamespace = "") String paramString);
}


/* Location:              C:\Users\victo\Documents\UFJF\TP\Gestão\IntegraAPI-2018.jar!/br/ufjf/ice/integra3/ws/login/IWsLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */