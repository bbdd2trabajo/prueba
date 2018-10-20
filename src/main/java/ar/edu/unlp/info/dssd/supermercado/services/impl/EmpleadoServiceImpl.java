package ar.edu.unlp.info.dssd.supermercado.services.impl;

import ar.edu.unlp.info.dssd.supermercado.entities.Employee;
import ar.edu.unlp.info.dssd.supermercado.entities.EmployeeType;
import ar.edu.unlp.info.dssd.supermercado.repositories.EmpleadoRepository;
import ar.edu.unlp.info.dssd.supermercado.repositories.EmpleadoTypeRepository;
import ar.edu.unlp.info.dssd.supermercado.services.EmpleadoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {
	
	@Autowired
	private transient EmpleadoRepository empleadoRepository;
	
	@Autowired
	private transient EmpleadoTypeRepository empleadoTypeRepository;

	public Employee getEmployeeByEmail(String email){
		return empleadoRepository.getEmployeeByEmail(email);
	}
	
	public boolean save(Employee employee) throws Exception {
		try {
			empleadoRepository.saveEmployee(employee);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
	public List<Employee> getAll() throws Exception {
		try {
			return empleadoRepository.getAllEmployee();
		}catch(Exception e){
			e.getMessage();
			return null;
		}
	}
	

	public boolean delete(Employee employee) throws Exception {
		try {
			 empleadoRepository.removeEmployee(employee);
			 return true; 
		}catch(Exception e){
			e.getMessage();
			return false;
		}
	}
	
	public boolean deleteType(EmployeeType employee) throws Exception {
		empleadoTypeRepository.removeEmployee(employee);
		try {
			 empleadoTypeRepository.removeEmployee(employee);
			 return true; 
		}catch(Exception e){
			e.getMessage();
			return false;
		}
	}

	public boolean update(Employee employeeUpdate) throws Exception {
		try {
			empleadoRepository.updateEmployee(employeeUpdate);
			 return true; 
		}catch(Exception e){
			System.out.println("ERRORRR:" + e.getMessage());
			return false;
		}
	}
	
	public boolean saveEmployeeType(EmployeeType employee) {
		try {
			empleadoTypeRepository.saveEmployee(employee);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
	public EmployeeType getEmployeeByInitials(String initials) {
	    return empleadoTypeRepository.getEmployeeByInitials(initials);
	}
	
	@Override
	public EmployeeType getCreateEmployeeType(EmployeeType employee) {
		try{
			return empleadoTypeRepository.getEmployeeByInitials(employee.getInitials());
		}catch (NoResultException ex){
			empleadoTypeRepository.saveEmployee(employee);
			return empleadoTypeRepository.getEmployeeByInitials(employee.getInitials());
		}catch (Exception e) {
			return null;
		}
	}
	
	public boolean generateData() {
		try {
			ArrayList<String> nombres = new ArrayList<String>(); 
			nombres.add("Pablo");nombres.add("Pepe");nombres.add("Pedro");nombres.add("Maria");nombres.add("Ana");nombres.add("Claudia");nombres.add("Teresa");nombres.add("Seba");nombres.add("Joaquin");nombres.add("Miño");nombres.add("Tito");nombres.add("Sofia");
			ArrayList<String> mails = new ArrayList<String>();
			mails.add("@hotmail.com");mails.add("@yahoo.com");mails.add("@gmail.com");mails.add("@outlook.com");
			ArrayList<String> initials = new ArrayList<String>();
			initials.add("admin");initials.add("dev");initials.add("rrhh");initials.add("lid");initials.add("ing");initials.add("PM");
			ArrayList<String> desc = new ArrayList<String>();
			desc.add("Administrador");desc.add("Desarrollador");desc.add("Recursos Humanos");desc.add("Team Lider");desc.add("Ingeniero en Software");desc.add("Proyect Manager");
			Random r = new Random();
			for (int i=0; i <= 5; i++) {
				EmployeeType em = new EmployeeType();
				em.setInitials(initials.get(Math.abs(r.nextInt()) % 6));
				em.setDescriptions(desc.get(Math.abs(r.nextInt()) % 6));
				em = this.getCreateEmployeeType(em);
				Employee e = new Employee(nombres.get(Math.abs(r.nextInt()) % 12),nombres.get(Math.abs(r.nextInt()) % 12),nombres.get(Math.abs(r.nextInt()) % 12)+mails.get(Math.abs(r.nextInt()) % 4),nombres.get(Math.abs(r.nextInt()) % 12));
				e.setEmployeetype(em);
				this.save(e);
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public EmpleadoRepository getEmpleadoRepository() {
		return empleadoRepository;
	}

	public void setEmpleadoRepository(EmpleadoRepository empleadoRepository) {
		this.empleadoRepository = empleadoRepository;
	}

}
