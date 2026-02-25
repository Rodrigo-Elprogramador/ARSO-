package arso.productos.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import repositorio.Identificable;

/**
 * Versión simplificada de Usuario para el microservicio de Productos.
 * Mantiene solo la información básica necesaria para la autonomía del microservicio.
 * La información completa está en el microservicio de Usuarios.
 */
@Entity
@Table(name = "usuario_resumen")
public class UsuarioResumen implements Identificable {
	
	@Id
	private String id;
	
	@Column(unique = true)
	private String email;
	
	private String nombre;
	
	private String apellidos;
	
	// Constructor vacío requerido por JPA
	public UsuarioResumen() {
	}
	
	// Constructor con parámetros
	public UsuarioResumen(String id, String email, String nombre, String apellidos) {
		this.id = id;
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	// Getters y Setters
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Override
	public String toString() {
		return "UsuarioResumen [id=" + id + ", email=" + email + ", nombre=" + nombre + ", apellidos=" + apellidos + "]";
	}
}