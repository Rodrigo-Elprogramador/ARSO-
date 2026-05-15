package arso.usuarios.servicio;

import java.time.LocalDate;
import java.util.List;

import arso.usuarios.modelo.Usuario;
import arso.usuarios.puertos.IEventosCompraventas;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioUsuario extends IEventosCompraventas {

	public String altaUsuario(String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono, String githubId) throws RepositorioException, EntidadNoEncontrada;

	public void modificarUsuario(String identificador, String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono, String githubId) throws RepositorioException, EntidadNoEncontrada;

	public Usuario comprobarUsuario(String email, String clave) throws EntidadNoEncontrada, RepositorioException;
	
	public Usuario getUsuario(String id) throws EntidadNoEncontrada, RepositorioException;
	
	public Usuario getUsuarioByGithubId(String githubId) throws EntidadNoEncontrada, RepositorioException;
	
	public List<Usuario> listarUsuarios() throws RepositorioException;

	void onCompraventaCreada(String idVendedor, String idComprador) throws Exception;
}
