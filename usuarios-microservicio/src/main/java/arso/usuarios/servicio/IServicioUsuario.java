package arso.usuarios.servicio;

import java.time.LocalDate;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import arso.usuarios.modelo.Usuario;

public interface IServicioUsuario {

	public String altaUsuario(String nombre, String apellidos, String email, String clave, LocalDate naciemiento, String telefono) throws RepositorioException, EntidadNoEncontrada;

	public void	modificarUsuario(String identificador, String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada;

	public Usuario comprobarUsuario(String email, String clave) throws EntidadNoEncontrada, RepositorioException;
}
