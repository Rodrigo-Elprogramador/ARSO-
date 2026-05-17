package arso.usuarios.servicio;

import java.time.LocalDate;
import java.util.List;

import arso.usuarios.modelo.Usuario;
import arso.usuarios.repositorio.RepositorioUsuarioAdHocJPA;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;

public class ServicioUsuario implements IServicioUsuario {

	private RepositorioUsuarioAdHocJPA repositorio = FactoriaRepositorios.getRepositorio(Usuario.class);

	@Override
	public void onCompraventaCreada(String idVendedor, String idComprador) throws Exception {
	    Usuario vendedor = repositorio.getById(idVendedor);
	    vendedor.setContadorVentas(vendedor.getContadorVentas() + 1);
	    repositorio.update(vendedor);

	    Usuario comprador = repositorio.getById(idComprador);
	    comprador.setContadorCompras(comprador.getContadorCompras() + 1);
	    repositorio.update(comprador);
	}

	@Override
	public String altaUsuario(String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono, String githubId) throws RepositorioException, EntidadNoEncontrada {
		if(isBlank(nombre))
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio o solo espacios en blanco");

		if(isBlank(apellidos))
			throw new IllegalArgumentException("apellidos: no debe ser nulo ni vacio o solo espacios en blanco");

		if(isBlank(email))
			throw new IllegalArgumentException("email: no debe ser nulo ni vacio o solo espacios en blanco");

		if(isBlank(clave))
			throw new IllegalArgumentException("clave: no debe ser nulo ni vacio o solo espacios en blanco");

		if(nacimiento == null || LocalDate.now().isAfter(LocalDate.now()))
			throw new IllegalArgumentException("Fecha: no debe ser nulo ni anterior a la fecha actual");
		if(repositorio.getInstanciaEmail(email))
			throw new IllegalArgumentException("Ya existe una cuenta con este email");

		Usuario usuario = new Usuario(nombre, email, apellidos, clave, nacimiento, telefono, false);
		if (!isBlank(githubId)) {
			usuario.setGithubId(githubId);
		}

		String idUsuario = repositorio.add(usuario);
		System.out.println("Se ha creado el usuario con id " + "\"" + usuario.getId() + "\"");

		return idUsuario;
	}

	@Override
	public void modificarUsuario(String identificador, String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono, String githubId) throws RepositorioException, EntidadNoEncontrada {
		if(isBlank(identificador))
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");

		Usuario usuario = repositorio.getById(identificador);

		if(!isBlank(nombre))
			usuario.setNombre(nombre);
		if(!isBlank(apellidos))
			usuario.setApellidos(apellidos);
		if(!isBlank(email))
			usuario.setEmail(email);
		if(!isBlank(clave))
			usuario.setClave(clave);
		if(nacimiento != null)
			if(LocalDate.now().isAfter(nacimiento))
				usuario.setFecha_nacimiento(nacimiento);
		if(!isBlank(telefono))
			usuario.setTelefono(telefono);
		if(!isBlank(githubId))
			usuario.setGithubId(githubId);

		repositorio.update(usuario);
		System.out.println("Se ha modificado el usuario con id " + "\"" + usuario.getId() + "\"");
	}

	@Override
	public Usuario comprobarUsuario(String email, String clave) throws EntidadNoEncontrada, RepositorioException {
		if(isBlank(email))
			throw new IllegalArgumentException("email: no debe ser nulo ni vacio o solo espacios en blanco");

		if(isBlank(clave))
			throw new IllegalArgumentException("clave: no debe ser nulo ni vacio o solo espacios en blanco");

		return repositorio.getByIdentificación(email, clave);
	}

	@Override
	public Usuario getUsuario(String id) throws EntidadNoEncontrada, RepositorioException {
		return repositorio.getById(id);
	}

	@Override
	public Usuario getUsuarioByGithubId(String githubId) throws EntidadNoEncontrada, RepositorioException {
		return repositorio.getByGithubId(githubId);
	}

	@Override
	public List<Usuario> listarUsuarios() throws RepositorioException {
	    return repositorio.getAll();
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
