package arso.usuarios.modelo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import repositorio.Identificable;

@Entity
@Table(name = "usuario")
public class Usuario implements Identificable{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE) //anotación JPA
	private String id;
	@Column(unique = true)
	private String email;
	private String nombre;
	private String apellidos;
	private String clave;
	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;
	private String telefono;
	private boolean administrador;
	
	public Usuario() {
		
	}
	
	public Usuario(String nombre,String email,String apellidos, String clave, LocalDate fecha_nacimiento,String telefono,boolean administrador) {
		this.nombre=nombre;
		this.email=email;
		this.apellidos=apellidos;
		this.clave=clave;
		this.fechaNacimiento=fecha_nacimiento;
		this.telefono=telefono;
		this.administrador=administrador;
	}

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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public LocalDate getFecha_nacimiento() {
		return fechaNacimiento;
	}

	public void setFecha_nacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", nombre=" + nombre + ", apellidos=" + apellidos + ", clave="
				+ clave + ", fechaNacimiento=" + fechaNacimiento + ", telefono=" + telefono + ", administrador="
				+ administrador + "]";
	}

	
}
