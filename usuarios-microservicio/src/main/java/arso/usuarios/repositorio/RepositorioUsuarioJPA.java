package arso.usuarios.repositorio;

import repositorio.RepositorioJPA;
import arso.usuarios.modelo.Usuario;

public class RepositorioUsuarioJPA extends RepositorioJPA<Usuario>{
	
	@Override
	public Class<Usuario> getClase() {

		return Usuario.class;

	}
}
