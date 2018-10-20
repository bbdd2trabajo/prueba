package ar.edu.unlp.info.dssd.supermercado.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.unlp.info.dssd.supermercado.entities.EmployeeType;

@Repository
public class EmpleadoTypeRepository {
	  
	  @Autowired
	  private SessionFactory sessionFactory;
	  
	  @Transactional
	  public EmployeeType getEmployeeByInitials(String initials){
		 Session session = sessionFactory.getCurrentSession();
		 return (EmployeeType) session.createQuery(
		 	      "SELECT u FROM EmployeeType u WHERE u.initials = :initials").setParameter("initials", initials).getSingleResult();	
	  }
	  
	  @Transactional
	  public void saveEmployee(EmployeeType e) {
		  Session session= sessionFactory.getCurrentSession();
		  session.persist(e); 
	  }
	  
	  @Transactional
		public void removeEmployee(EmployeeType e) {
			Session session = sessionFactory.getCurrentSession();
			session.delete(e);
		}
	  
	  @Transactional
	  public void updateEmployee(EmployeeType employee) {
		Session session = sessionFactory.getCurrentSession();
		session.update(employee);
	  }
}
