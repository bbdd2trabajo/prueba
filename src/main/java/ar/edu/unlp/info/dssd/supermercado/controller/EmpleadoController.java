package ar.edu.unlp.info.dssd.supermercado.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ar.edu.unlp.info.dssd.supermercado.services.EmpleadoService;
import ar.edu.unlp.info.dssd.supermercado.services.impl.UserService;
import ar.edu.unlp.info.dssd.supermercado.entities.Employee;
import ar.edu.unlp.info.dssd.supermercado.entities.EmployeeType;
import ar.edu.unlp.info.dssd.supermercado.entities.Token;
import ar.edu.unlp.info.dssd.supermercado.jwt.TokenManagerSecurity;

@Controller
@RequestMapping(value = "/api/empleado")
public class EmpleadoController {
	
	@Autowired
	private transient EmpleadoService empleadoService;
	
	@Autowired
	private UserService userService;
	
	@Inject
	private TokenManagerSecurity tokenManagerSecurity;
 
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json",produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> login(HttpServletRequest request, @RequestBody Employee usuario, @RequestParam("token") String token) {
		try {
			HttpSession httpSession = request.getSession(); 
			if (httpSession.getAttribute("token") == null || !httpSession.getAttribute("token").equals(token)) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			userService.login(usuario);
			return ResponseEntity.ok(request.getAttribute("user"));
		} catch (Exception e) {
			return new ResponseEntity<>(Collections.singletonMap("AuthenticationException", e.getMessage()),
				HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value = "/mail", // /{email:.+}
			method = RequestMethod.POST,
			headers="Accept=application/json",produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getByEmail(HttpServletRequest request, @RequestBody Map<String, Object> mail, @RequestParam("token") String token) throws Exception {
		try {
			HttpSession httpSession = request.getSession(); 
			if (httpSession.getAttribute("token") == null || !httpSession.getAttribute("token").equals(token)) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Employee empleado = empleadoService.getEmployeeByEmail(mail.get("email").toString());
			return new ResponseEntity<Employee>(empleado, HttpStatus.OK);
		}catch(NoResultException e) {
			return new ResponseEntity<>("No se encontró el empleado con mail: " + mail, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error al consultar el empleado con mail: " + mail, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, headers="Accept=application/json",
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> save(HttpServletRequest request, @RequestBody Employee employee, @RequestParam("token") String token) throws Exception {
		try{
			HttpSession httpSession = request.getSession(); 
			if (httpSession.getAttribute("token") == null || !httpSession.getAttribute("token").equals(token)) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            employee.setEmployeetype(empleadoService.getCreateEmployeeType((employee.getEmployeetype())));
			return ResponseEntity.ok(empleadoService.save(employee));
		}catch(NoResultException e) {
			return new ResponseEntity<>("No se encontró el empleado con mail: " + employee.getEmail(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error al guardar el empleado con mail: " + employee.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/all", 
			method = RequestMethod.GET, 
			headers="Accept=application/json",produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getAll(HttpServletRequest request, @RequestParam("token") String token) throws Exception {
		List<Employee> empleados = new ArrayList<Employee>();
		try {
			HttpSession httpSession = request.getSession();
			if (httpSession.getAttribute("token") == null) {
				Token newToken = new Token(tokenManagerSecurity.createJWT(request.getSession().getId()));
				httpSession.setAttribute("token", newToken.getToken());
			}else if(!httpSession.getAttribute("token").equals(token)){
				return new ResponseEntity<> (HttpStatus.FORBIDDEN);
			}
			empleados = empleadoService.getAll();
			if(empleados.isEmpty())
				return new ResponseEntity<>("No se encontraron empleados ", HttpStatus.NOT_FOUND);
			return new ResponseEntity<List<Employee>>(empleados, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error consultar los empleados", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> delete(HttpServletRequest request, @RequestBody Map<String, Object> mail, @RequestParam("token") String token) throws Exception {
		try{
			HttpSession httpSession = request.getSession(); 
			if (httpSession.getAttribute("token") == null || !httpSession.getAttribute("token").equals(token)) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Employee employee = empleadoService.getEmployeeByEmail(mail.get("email").toString());
			return ResponseEntity.ok(empleadoService.delete(employee));
		}catch(NoResultException e) {
			return new ResponseEntity<>("No se encontró el empleado con mail: " + mail, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error al eliminar el empleado con mail: " + mail, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers="Accept=application/json",
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> update(HttpServletRequest request, @RequestBody Employee employee, @RequestParam("token") String token) throws Exception {
		try{
			HttpSession httpSession = request.getSession(); 
			if (httpSession.getAttribute("token") == null || !httpSession.getAttribute("token").equals(token)) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Employee employeeBD = empleadoService.getEmployeeByEmail(employee.getEmail());
			employeeBD.setFirstname(employee.getFirstname());
			employeeBD.setPassword(employee.getPassword());
			employeeBD.setSurname(employee.getSurname());
			employeeBD.setEmployeetype(empleadoService.getCreateEmployeeType((employee.getEmployeetype())));
			return ResponseEntity.ok(empleadoService.update(employeeBD));
		}catch(NoResultException e) {
			return new ResponseEntity<>("No se encontró el empleado ID: " + employee.getId(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error al eliminar el empleado ID: " + employee.getId(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getCreate", method = RequestMethod.POST, headers="Accept=application/json",
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createEmployeeType(HttpServletRequest request, @RequestBody EmployeeType employee, @RequestParam("token") String token) throws Exception {
		try{
			return ResponseEntity.ok(empleadoService.getCreateEmployeeType(employee));
		}catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error al crear tipo de empleado: " + employee.getId(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = "/deleteType", method = RequestMethod.POST, headers="Accept=application/json",
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteEmployeeType(HttpServletRequest request, @RequestBody Map<String, Object> init, @RequestParam("token") String token) throws Exception {
		try{
			HttpSession httpSession = request.getSession(); 
			if (httpSession.getAttribute("token") == null || !httpSession.getAttribute("token").equals(token)) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			EmployeeType employee = empleadoService.getEmployeeByInitials(init.get("initials").toString());
			for (Employee e : employee.getEmployees()) {
					e.setEmployeetype(null);
					empleadoService.update(e);
		    }
			return ResponseEntity.ok(empleadoService.deleteType(employee));
		}catch(NoResultException e) {
			return new ResponseEntity<>("No se encontró el tipo de empleado: " + init.get("initials").toString(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<> ("Ocurrió un error al consultar tipo de empleado: " + init.get("initials").toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getToken", method = RequestMethod.GET, headers = "Accept=application/json",produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getToken(HttpServletRequest request) {
		try {
			HttpSession httpSession = request.getSession();
			if (httpSession.getAttribute("token") == null) {
				return new ResponseEntity<> (HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<> (httpSession.getAttribute("token"),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();	
			return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/generador", method = RequestMethod.GET, headers="Accept=application/json",
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> generator(HttpServletRequest request, @RequestParam("token") String token) throws Exception {
		try{
			return ResponseEntity.ok(empleadoService.generateData());
		}catch (Exception e) {
			System.out.println("ERRORRR:" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}

	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}

	
}
