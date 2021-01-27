/*    */ package br.ufjf.ice.integra3.ws.login;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlType;
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
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name = "isValidTokenResponse", propOrder = {"_return"})
/*    */ public class IsValidTokenResponse
/*    */ {
/*    */   @XmlElement(name = "return")
/*    */   protected boolean _return;
/*    */   
/* 43 */   public boolean isReturn() { return this._return; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void setReturn(boolean value) { this._return = value; }
/*    */ }


/* Location:              C:\Users\victo\Documents\UFJF\TP\Gestão\IntegraAPI-2018.jar!/br/ufjf/ice/integra3/ws/login/IsValidTokenResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */