package ar.edu.unlp.info.dssd.supermercado.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity (name="Employee")
public class Employee {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String firstname;
	private String surname;
	private String email;
	private String password;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn (name = "employeetype")
	private EmployeeType employeeType;
	
	public Employee() {
		
	}
	
	public Employee(String firstname, String surname, String email, String password/*, EmployeeType employeetype*/) {
		super();
		this.firstname = firstname;
		this.surname = surname;
		this.email = email;
		this.password = password;
		//this.employeetype = employeetype;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public EmployeeType getEmployeetype() {
		return employeeType;
	}

	public void setEmployeetype(EmployeeType employeetype) {
		this.employeeType = employeetype;

	}

}
