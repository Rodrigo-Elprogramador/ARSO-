package arso.usuarios.repositorio;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.RepositorioString;
import arso.usuarios.modelo.Usuario;

public interface RepositorioUsuarioAdHoc extends RepositorioString<Usuario>{

	public Usuario getByIdentificación(String email, String clave) throws RepositorioException, EntidadNoEncontrada;

	public boolean getInstanciaEmail(String email) throws RepositorioException, EntidadNoEncontrada;
}
