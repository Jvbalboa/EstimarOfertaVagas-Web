package dao;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;  
import org.hibernate.SessionFactory;  
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.GradeDisciplina;

public class CadastrarDisciplinaDAO { 
	
	  
	public void Inserir(GradeDisciplina gradeDisciplina ) {  
	      
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("EstimarOfertaVagas-Web");
		EntityManager manager = factory.createEntityManager();

		manager.getTransaction().begin();    
		manager.persist(gradeDisciplina);
		manager.getTransaction().commit();  

		//System.out.println("ID da tarefa: " + tarefa.getId());

		manager.close();
	      
	  
	}  



}
