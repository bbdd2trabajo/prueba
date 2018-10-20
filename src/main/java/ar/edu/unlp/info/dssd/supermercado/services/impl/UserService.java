package ar.edu.unlp.info.dssd.supermercado.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unlp.info.dssd.supermercado.entities.Employee;
import ar.edu.unlp.info.dssd.supermercado.repositories.EmpleadoRepository;


@Service
public class UserService {
	
	@Autowired
	private EmpleadoRepository emp;

	public void logout(String user) {
		
	}
	
	public Employee login(Employee usuario) {
		Employee u = emp.getEmployeeByEmail(usuario.getEmail());
		if(!u.equals(null)){
			if(u.getPassword().equals(usuario.getPassword())){
				return u;
			}
		}
		
		throw new IllegalArgumentException("Usuario, rol o password invalido");
	}
}

