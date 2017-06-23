package br.ufjf.coordenacao.sistemagestaocurso.util.jpa;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

@Transactional @Interceptor
public class TransactionInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(TransactionInterceptor.class);
	
	private @Inject EntityManager manager;
	
	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {

		boolean criador = false;
		//Session session = (Session) manager;
		EntityTransaction trx = manager.getTransaction();
		try {
			//Session session = (Session) manager;

			if (!trx.isActive()) {
				// truque para fazer rollback no que já passou
				// (senão, um futuro commit, confirmaria até mesmo operações sem transação)
				logger.info("Iniciado transacao dummy");
				trx.begin();
				logger.info("Rollback na transacao dummy");
				trx.rollback();
				logger.info("Dummy OK. Iniciando transcacao");
				// agora sim inicia a transação
				trx.begin();
				logger.info("Transação iniciada. Retornando a execucao");
				
				criador = true;
			}

			return context.proceed();
		} catch (Exception e) {
			if (trx != null && criador) {
				trx.rollback();
				logger.error("Falha. Realizado rollback da transação");
			}

			throw e;
		} finally {
			if (trx != null && trx.isActive() && criador) {
				trx.commit();
				logger.info("Transação fechada");
			}
		}
	}
	
}