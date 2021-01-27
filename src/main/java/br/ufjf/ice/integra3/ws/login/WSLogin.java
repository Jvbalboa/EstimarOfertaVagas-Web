/*    */ package br.ufjf.ice.integra3.ws.login;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.Service;
/*    */ import javax.xml.ws.WebEndpoint;
/*    */ import javax.xml.ws.WebServiceClient;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @WebServiceClient(name = "WSLogin", targetNamespace = "http://service.login.ws.integra3.ice.ufjf.br/", wsdlLocation = "http://login.integra.nrc.ice.ufjf.br:80/integra/services/soap/login/wslogin?wsdl")
/*    */ public class WSLogin
/*    */   extends Service
/*    */ {
/*    */   private static final URL WSLOGIN_WSDL_LOCATION;
/*    */   private static final WebServiceException WSLOGIN_EXCEPTION;
/* 27 */   private static final QName WSLOGIN_QNAME = new QName("http://service.login.ws.integra3.ice.ufjf.br/", "WSLogin");
/*    */   
/*    */   static  {
/* 30 */     URL url = null;
/* 31 */     WebServiceException e = null;
/*    */     try {
/* 33 */       url = new URL("http://login.integra.nrc.ice.ufjf.br:80/integra/services/soap/login/wslogin?wsdl");
/* 34 */     } catch (MalformedURLException ex) {
/* 35 */       e = new WebServiceException(ex);
/*    */     } 
/* 37 */     WSLOGIN_WSDL_LOCATION = url;
/* 38 */     WSLOGIN_EXCEPTION = e;
/*    */   }
/*    */ 
/*    */   
/* 42 */   public WSLogin() { super(__getWsdlLocation(), WSLOGIN_QNAME); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public WSLogin(WebServiceFeature... features) { super(__getWsdlLocation(), WSLOGIN_QNAME, features); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public WSLogin(URL wsdlLocation) { super(wsdlLocation, WSLOGIN_QNAME); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public WSLogin(URL wsdlLocation, WebServiceFeature... features) { super(wsdlLocation, WSLOGIN_QNAME, features); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public WSLogin(URL wsdlLocation, QName serviceName) { super(wsdlLocation, serviceName); }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public WSLogin(URL wsdlLocation, QName serviceName, WebServiceFeature... features) { super(wsdlLocation, serviceName, features); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @WebEndpoint(name = "WsLoginServicePort")
/* 72 */   public IWsLogin getWsLoginServicePort() { return (IWsLogin)getPort(new QName("http://service.login.ws.integra3.ice.ufjf.br/", "WsLoginServicePort"), IWsLogin.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @WebEndpoint(name = "WsLoginServicePort")
/* 84 */   public IWsLogin getWsLoginServicePort(WebServiceFeature... features) { return (IWsLogin)getPort(new QName("http://service.login.ws.integra3.ice.ufjf.br/", "WsLoginServicePort"), IWsLogin.class, features); }
/*    */ 
/*    */   
/*    */   private static URL __getWsdlLocation() {
/* 88 */     if (WSLOGIN_EXCEPTION != null) {
/* 89 */       throw WSLOGIN_EXCEPTION;
/*    */     }
/* 91 */     return WSLOGIN_WSDL_LOCATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\victo\Documents\UFJF\TP\Gestão\IntegraAPI-2018.jar!/br/ufjf/ice/integra3/ws/login/WSLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */