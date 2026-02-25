package arso.usuarios.servicio;

import java.time.LocalDate;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;
import arso.usuarios.modelo.Usuario;
import arso.usuarios.repositorio.RepositorioUsuarioAdHocJPA;

public class ServicioUsuario implements IServicioUsuario{
	
	private RepositorioUsuarioAdHocJPA repositorio = FactoriaRepositorios.getRepositorio(Usuario.class);

	@Override
	public String altaUsuario(String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada {
		
		if(nombre == null || nombre.isBlank())
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio o solo espacios en blanco");
		
		if(apellidos == null || apellidos.isBlank())
			throw new IllegalArgumentException("apellidos: no debe ser nulo ni vacio o solo espacios en blanco");
		
		if(email == null || email.isBlank())
			throw new IllegalArgumentException("email: no debe ser nulo ni vacio o solo espacios en blanco");
		
		if(clave == null || clave.isBlank())
			throw new IllegalArgumentException("clave: no debe ser nulo ni vacio o solo espacios en blanco");
		
		if(nacimiento == null || LocalDate.now().isBefore(nacimiento))
			throw new IllegalArgumentException("Fecha: no debe ser nulo ni anterior a la fecha actual");
		if(repositorio.getInstanciaEmail(email))
			throw new IllegalArgumentException("Ya existe una cuenta con este email");
		Usuario usuario = new Usuario(nombre, email, apellidos, clave, nacimiento, telefono, false);
		
		String idUsuario = repositorio.add(usuario);
		
		System.out.println("Se ha creado el usuario con id " + "\""+ usuario.getId() +"\"");
		
		return idUsuario;
	}
	
	//Aquí el equipo ha decidido que si los parámetros son nullos o cadenas sin información no se modificará el parámetro correspondiente
	@Override
	public void	modificarUsuario(String identificador, String nombre, String apellidos, String email, String clave, LocalDate nacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada{
		if(identificador == null || identificador.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		//Recuperamos el usario
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
		
		repositorio.update(usuario);
		System.out.println("Se ha modificado el usuario con id " + "\""+ usuario.getId() +"\"");
	}
	
	@Override
	public Usuario comprobarUsuario(String email, String clave) throws EntidadNoEncontrada, RepositorioException{

		if(email == null || email.isBlank())
			throw new IllegalArgumentException("email: no debe ser nulo ni vacio o solo espacios en blanco");
		
		if(clave == null || clave.isBlank())
			throw new IllegalArgumentException("clave: no debe ser nulo ni vacio o solo espacios en blanco");
		Usuario user = repositorio.getByIdentificación(email, clave);
		return user;
	}
}
