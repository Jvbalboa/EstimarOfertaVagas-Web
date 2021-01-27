/*    */ package br.ufjf.ice.integra3.ws.login;
/*    */ 
/*    */ import javax.xml.ws.WebFault;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @WebFault(name = "IntegraSoapServiceException", targetNamespace = "http://interfaces.login.ws.integra3.ice.ufjf.br/")
/*    */ public class IntegraSoapServiceException_Exception
/*    */   extends Exception
/*    */ {
/*    */   private IntegraSoapServiceException faultInfo;
/*    */   
/*    */   public IntegraSoapServiceException_Exception(String message, IntegraSoapServiceException faultInfo) {
/* 30 */     super(message);
/* 31 */     this.faultInfo = faultInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IntegraSoapServiceException_Exception(String message, IntegraSoapServiceException faultInfo, Throwable cause) {
/* 41 */     super(message, cause);
/* 42 */     this.faultInfo = faultInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public IntegraSoapServiceException getFaultInfo() { return this.faultInfo; }
/*    */ }


/* Location:              C:\Users\victo\Documents\UFJF\TP\Gestão\IntegraAPI-2018.jar!/br/ufjf/ice/integra3/ws/login/IntegraSoapServiceException_Exception.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */