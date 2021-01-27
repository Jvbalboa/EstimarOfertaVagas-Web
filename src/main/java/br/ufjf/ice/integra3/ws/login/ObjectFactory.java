/*     */ package br.ufjf.ice.integra3.ws.login;
/*     */ 
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlElementDecl;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRegistry
/*     */ public class ObjectFactory
/*     */ {
/*  27 */   private static final QName _LoginResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "loginResponse");
/*  28 */   private static final QName _IsValidProfile_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "isValidProfile");
/*  29 */   private static final QName _GetPermissions_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "getPermissions");
/*  30 */   private static final QName _Profile_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "profile");
/*  31 */   private static final QName _WsLoginResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "wsLoginResponse");
/*  32 */   private static final QName _LoginByCurso_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "loginByCurso");
/*  33 */   private static final QName _GetTokenExpiration_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "getTokenExpiration");
/*  34 */   private static final QName _WsPermissionResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "wsPermissionResponse");
/*  35 */   private static final QName _UpdateUserGroup_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "updateUserGroup");
/*  36 */   private static final QName _IntegraSoapServiceException_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "IntegraSoapServiceException");
/*  37 */   private static final QName _GetUserInformation_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "getUserInformation");
/*  38 */   private static final QName _IsValidToken_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "isValidToken");
/*  39 */   private static final QName _ProfileResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "profileResponse");
/*  40 */   private static final QName _UpdateUserGroupResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "updateUserGroupResponse");
/*  41 */   private static final QName _LoginByMatriculaResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "loginByMatriculaResponse");
/*  42 */   private static final QName _LogoutResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "logoutResponse");
/*  43 */   private static final QName _GetPermissionsResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "getPermissionsResponse");
/*  44 */   private static final QName _IsValidTokenResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "isValidTokenResponse");
/*  45 */   private static final QName _Login_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "login");
/*  46 */   private static final QName _GetTokenExpirationResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "getTokenExpirationResponse");
/*  47 */   private static final QName _GetUserInformationResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "getUserInformationResponse");
/*  48 */   private static final QName _IsValidProfileResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "isValidProfileResponse");
/*  49 */   private static final QName _WsUserInfoResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "wsUserInfoResponse");
/*  50 */   private static final QName _Logout_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "logout");
/*  51 */   private static final QName _LoginByCursoResponse_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "loginByCursoResponse");
/*  52 */   private static final QName _LoginByMatricula_QNAME = new QName("http://interfaces.login.ws.integra3.ice.ufjf.br/", "loginByMatricula");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public LoginByMatriculaResponse createLoginByMatriculaResponse() { return new LoginByMatriculaResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public LogoutResponse createLogoutResponse() { return new LogoutResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public GetPermissionsResponse createGetPermissionsResponse() { return new GetPermissionsResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public IsValidTokenResponse createIsValidTokenResponse() { return new IsValidTokenResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public Login createLogin() { return new Login(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public GetUserInformationResponse createGetUserInformationResponse() { return new GetUserInformationResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public IsValidProfileResponse createIsValidProfileResponse() { return new IsValidProfileResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public GetTokenExpirationResponse createGetTokenExpirationResponse() { return new GetTokenExpirationResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public Logout createLogout() { return new Logout(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public WsUserInfoResponse createWsUserInfoResponse() { return new WsUserInfoResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public LoginByCursoResponse createLoginByCursoResponse() { return new LoginByCursoResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public LoginByMatricula createLoginByMatricula() { return new LoginByMatricula(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public LoginResponse createLoginResponse() { return new LoginResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public IsValidProfile createIsValidProfile() { return new IsValidProfile(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public GetPermissions createGetPermissions() { return new GetPermissions(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public Profile createProfile() { return new Profile(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public WsLoginResponse createWsLoginResponse() { return new WsLoginResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public LoginByCurso createLoginByCurso() { return new LoginByCurso(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public GetTokenExpiration createGetTokenExpiration() { return new GetTokenExpiration(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public WsPermissionResponse createWsPermissionResponse() { return new WsPermissionResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public UpdateUserGroup createUpdateUserGroup() { return new UpdateUserGroup(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public IntegraSoapServiceException createIntegraSoapServiceException() { return new IntegraSoapServiceException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   public GetUserInformation createGetUserInformation() { return new GetUserInformation(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public ProfileResponse createProfileResponse() { return new ProfileResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public UpdateUserGroupResponse createUpdateUserGroupResponse() { return new UpdateUserGroupResponse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 266 */   public IsValidToken createIsValidToken() { return new IsValidToken(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "loginResponse")
/* 275 */   public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) { return new JAXBElement(_LoginResponse_QNAME, LoginResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "isValidProfile")
/* 284 */   public JAXBElement<IsValidProfile> createIsValidProfile(IsValidProfile value) { return new JAXBElement(_IsValidProfile_QNAME, IsValidProfile.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "getPermissions")
/* 293 */   public JAXBElement<GetPermissions> createGetPermissions(GetPermissions value) { return new JAXBElement(_GetPermissions_QNAME, GetPermissions.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "profile")
/* 302 */   public JAXBElement<Profile> createProfile(Profile value) { return new JAXBElement(_Profile_QNAME, Profile.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "wsLoginResponse")
/* 311 */   public JAXBElement<WsLoginResponse> createWsLoginResponse(WsLoginResponse value) { return new JAXBElement(_WsLoginResponse_QNAME, WsLoginResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "loginByCurso")
/* 320 */   public JAXBElement<LoginByCurso> createLoginByCurso(LoginByCurso value) { return new JAXBElement(_LoginByCurso_QNAME, LoginByCurso.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "getTokenExpiration")
/* 329 */   public JAXBElement<GetTokenExpiration> createGetTokenExpiration(GetTokenExpiration value) { return new JAXBElement(_GetTokenExpiration_QNAME, GetTokenExpiration.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "wsPermissionResponse")
/* 338 */   public JAXBElement<WsPermissionResponse> createWsPermissionResponse(WsPermissionResponse value) { return new JAXBElement(_WsPermissionResponse_QNAME, WsPermissionResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "updateUserGroup")
/* 347 */   public JAXBElement<UpdateUserGroup> createUpdateUserGroup(UpdateUserGroup value) { return new JAXBElement(_UpdateUserGroup_QNAME, UpdateUserGroup.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "IntegraSoapServiceException")
/* 356 */   public JAXBElement<IntegraSoapServiceException> createIntegraSoapServiceException(IntegraSoapServiceException value) { return new JAXBElement(_IntegraSoapServiceException_QNAME, IntegraSoapServiceException.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "getUserInformation")
/* 365 */   public JAXBElement<GetUserInformation> createGetUserInformation(GetUserInformation value) { return new JAXBElement(_GetUserInformation_QNAME, GetUserInformation.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "isValidToken")
/* 374 */   public JAXBElement<IsValidToken> createIsValidToken(IsValidToken value) { return new JAXBElement(_IsValidToken_QNAME, IsValidToken.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "profileResponse")
/* 383 */   public JAXBElement<ProfileResponse> createProfileResponse(ProfileResponse value) { return new JAXBElement(_ProfileResponse_QNAME, ProfileResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "updateUserGroupResponse")
/* 392 */   public JAXBElement<UpdateUserGroupResponse> createUpdateUserGroupResponse(UpdateUserGroupResponse value) { return new JAXBElement(_UpdateUserGroupResponse_QNAME, UpdateUserGroupResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "loginByMatriculaResponse")
/* 401 */   public JAXBElement<LoginByMatriculaResponse> createLoginByMatriculaResponse(LoginByMatriculaResponse value) { return new JAXBElement(_LoginByMatriculaResponse_QNAME, LoginByMatriculaResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "logoutResponse")
/* 410 */   public JAXBElement<LogoutResponse> createLogoutResponse(LogoutResponse value) { return new JAXBElement(_LogoutResponse_QNAME, LogoutResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "getPermissionsResponse")
/* 419 */   public JAXBElement<GetPermissionsResponse> createGetPermissionsResponse(GetPermissionsResponse value) { return new JAXBElement(_GetPermissionsResponse_QNAME, GetPermissionsResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "isValidTokenResponse")
/* 428 */   public JAXBElement<IsValidTokenResponse> createIsValidTokenResponse(IsValidTokenResponse value) { return new JAXBElement(_IsValidTokenResponse_QNAME, IsValidTokenResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "login")
/* 437 */   public JAXBElement<Login> createLogin(Login value) { return new JAXBElement(_Login_QNAME, Login.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "getTokenExpirationResponse")
/* 446 */   public JAXBElement<GetTokenExpirationResponse> createGetTokenExpirationResponse(GetTokenExpirationResponse value) { return new JAXBElement(_GetTokenExpirationResponse_QNAME, GetTokenExpirationResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "getUserInformationResponse")
/* 455 */   public JAXBElement<GetUserInformationResponse> createGetUserInformationResponse(GetUserInformationResponse value) { return new JAXBElement(_GetUserInformationResponse_QNAME, GetUserInformationResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "isValidProfileResponse")
/* 464 */   public JAXBElement<IsValidProfileResponse> createIsValidProfileResponse(IsValidProfileResponse value) { return new JAXBElement(_IsValidProfileResponse_QNAME, IsValidProfileResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "wsUserInfoResponse")
/* 473 */   public JAXBElement<WsUserInfoResponse> createWsUserInfoResponse(WsUserInfoResponse value) { return new JAXBElement(_WsUserInfoResponse_QNAME, WsUserInfoResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "logout")
/* 482 */   public JAXBElement<Logout> createLogout(Logout value) { return new JAXBElement(_Logout_QNAME, Logout.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "loginByCursoResponse")
/* 491 */   public JAXBElement<LoginByCursoResponse> createLoginByCursoResponse(LoginByCursoResponse value) { return new JAXBElement(_LoginByCursoResponse_QNAME, LoginByCursoResponse.class, null, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/", name = "loginByMatricula")
/* 500 */   public JAXBElement<LoginByMatricula> createLoginByMatricula(LoginByMatricula value) { return new JAXBElement(_LoginByMatricula_QNAME, LoginByMatricula.class, null, value); }
/*     */ }


/* Location:              C:\Users\victo\Documents\UFJF\TP\Gestão\IntegraAPI-2018.jar!/br/ufjf/ice/integra3/ws/login/ObjectFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */