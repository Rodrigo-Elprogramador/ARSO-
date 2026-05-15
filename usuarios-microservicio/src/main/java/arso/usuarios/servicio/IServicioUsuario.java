package arso.usuarios.servicio;

import java.time.LocalDate;
import java.util.List;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import arso.usuarios.modelo.Usuario;
import arso.usuarios.puertos.IEventosCompraventas;

public interface IServicioUsuario extends IEventosCompraventas {

	public String altaUsuario(String nombre, String apellidos, String email, String clave, LocalDate naciemiento, String telefono) throws RepositorioException, EntidadNoEncontrada;

	public void	modificarUsuario(String identificador, String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada;

	public Usuario comprobarUsuario(String email, String clave) throws EntidadNoEncontrada, RepositorioException;
	
	public Usuario getUsuario(String id) throws EntidadNoEncontrada, RepositorioException;
	
	//TAREA 5
	public List<Usuario> listarUsuarios() throws RepositorioException;
	//TAREA 7
	void onCompraventaCreada(String idVendedor, String idComprador) throws Exception;
}
