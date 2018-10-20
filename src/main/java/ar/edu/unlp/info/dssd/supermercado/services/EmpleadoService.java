package ar.edu.unlp.info.dssd.supermercado.services;

import java.util.List;

import ar.edu.unlp.info.dssd.supermercado.entities.Employee;
import ar.edu.unlp.info.dssd.supermercado.entities.EmployeeType;


public interface EmpleadoService {
	
	public Employee getEmployeeByEmail(String email) throws Exception;
	public boolean save(Employee employee) throws Exception;
	public boolean saveEmployeeType(EmployeeType employee);
	public EmployeeType getEmployeeByInitials(String initials);
	public List<Employee> getAll() throws Exception;
	public boolean delete(Employee employee)throws Exception;
	public boolean deleteType(EmployeeType employee)throws Exception;
	public boolean update(Employee employee)throws Exception;
	public EmployeeType getCreateEmployeeType (EmployeeType employee);
	public boolean generateData();

}
