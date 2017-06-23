package br.ufjf.coordenacao.sistemagestaocurso.util.http;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@Named
public class SessionUtil
{
  @Inject
  private HttpServletRequest request;
  
  private Logger logger = Logger.getLogger(SessionUtil.class);
  
  public void invalidateSession()
  {
	  logger.info("Invalidating session.");
	  HttpSession session = this.request.getSession();
	  session.invalidate();
  }
}