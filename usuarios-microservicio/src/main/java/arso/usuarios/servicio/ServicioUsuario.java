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
		if(nombre == null || nombre.isBlank())
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio o solo espacios en blanco");

		if(apellidos == null || apellidos.isBlank())
			throw new IllegalArgumentException("apellidos: no debe ser nulo ni vacio o solo espacios en blanco");

		if(email == null || email.isBlank())
			throw new IllegalArgumentException("email: no debe ser nulo ni vacio o solo espacios en blanco");

		if(clave == null || clave.isBlank())
			throw new IllegalArgumentException("clave: no debe ser nulo ni vacio o solo espacios en blanco");

		if(nacimiento == null || LocalDate.now().isAfter(LocalDate.now()))
			throw new IllegalArgumentException("Fecha: no debe ser nulo ni anterior a la fecha actual");
		if(repositorio.getInstanciaEmail(email))
			throw new IllegalArgumentException("Ya existe una cuenta con este email");

		Usuario usuario = new Usuario(nombre, email, apellidos, clave, nacimiento, telefono, false);
		if (githubId != null && !githubId.isBlank()) {
			usuario.setGithubId(githubId);
		}

		String idUsuario = repositorio.add(usuario);
		System.out.println("Se ha creado el usuario con id " + "\"" + usuario.getId() + "\"");

		return idUsuario;
	}

	@Override
	public void modificarUsuario(String identificador, String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono, String githubId) throws RepositorioException, EntidadNoEncontrada {
		if(identificador == null || identificador.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");

		Usuario usuario = repositorio.getById(identificador);

		if(nombre != null && !nombre.isBlank())
			usuario.setNombre(nombre);
		if(apellidos != null && !apellidos.isBlank())
			usuario.setApellidos(apellidos);
		if(email != null && !email.isBlank())
			usuario.setEmail(email);
		if(clave != null && !clave.isBlank())
			usuario.setClave(clave);
		if(nacimiento != null)
			if(LocalDate.now().isAfter(nacimiento))
				usuario.setFecha_nacimiento(nacimiento);
		if(telefono != null && !telefono.isBlank())
			usuario.setTelefono(telefono);
		if(githubId != null && !githubId.isBlank())
			usuario.setGithubId(githubId);

		repositorio.update(usuario);
		System.out.println("Se ha modificado el usuario con id " + "\"" + usuario.getId() + "\"");
	}

	@Override
	public Usuario comprobarUsuario(String email, String clave) throws EntidadNoEncontrada, RepositorioException {
		if(email == null || email.isBlank())
			throw new IllegalArgumentException("email: no debe ser nulo ni vacio o solo espacios en blanco");

		if(clave == null || clave.isBlank())
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
}
