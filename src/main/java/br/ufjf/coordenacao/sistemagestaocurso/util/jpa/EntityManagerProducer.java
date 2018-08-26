package br.ufjf.coordenacao.sistemagestaocurso.util.jpa;

import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.ufjf.coordenacao.sistemagestaocurso.util.config.ConfHandler;

@ApplicationScoped
public class EntityManagerProducer {
	
	private HashMap<String, String> properties;

	private EntityManagerFactory factory;
	
	public EntityManagerProducer() {
		//factory = Persistence.createEntityManagerFactory("sgcPU");
		setProperties();
		factory = Persistence.createEntityManagerFactory("sgcPU", properties);
	}
	
	private void setProperties() {
		properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.url", ConfHandler.getConf("JDBC.URL"));
		properties.put("javax.persistence.jdbc.user", ConfHandler.getConf("JDBC.USER"));
		properties.put("javax.persistence.jdbc.password", ConfHandler.getConf("JDBC.PASSWORD"));
		properties.put("javax.persistence.jdbc.driver", ConfHandler.getConf("JDBC.DRIVER"));
	}
	
	@Produces @RequestScoped
	public EntityManager createEntityManager()
	{
		return factory.createEntityManager();
	}
	
	public void closeEntityManager(@Disposes EntityManager manager)
	{
		manager.close();
	}
}
