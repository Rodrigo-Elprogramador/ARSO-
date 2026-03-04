package arso.usuarios.rest;

import java.net.URI;
import java.time.LocalDate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import arso.usuarios.modelo.Usuario;
import arso.usuarios.servicio.IServicioUsuario;
import servicio.FactoriaServicios;

@Path("usuarios")
public class ControladorUsuarios {

	private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);

	@Context
	private UriInfo uriInfo;

	// Alta de un Usuario
	// POST: http://localhost:8080/api/usuarios
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response altaUsuario(UsuarioDTO dto) throws Exception {
	    
	    // Convertimos el String a LocalDate manualmente
	    LocalDate fechaParsed = null;
	    if (dto.getFechaNacimiento() != null && !dto.getFechaNacimiento().isBlank()) {
	        fechaParsed = LocalDate.parse(dto.getFechaNacimiento());
	    }

	    // Llamamos al servicio con los datos del DTO
	    String id = servicio.altaUsuario(
	            dto.getNombre(), 
	            dto.getApellidos(), 
	            dto.getEmail(), 
	            dto.getClave(), 
	            fechaParsed, 
	            dto.getTelefono());
	    
	    URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();
	    return Response.created(nuevaURL).build();
	}

	// Recuperación de Usuario (NUEVA)
	// GET: http://localhost:8080/api/usuarios/{id}
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuario(@PathParam("id") String id) throws Exception {
		
		Usuario usuario = servicio.getUsuario(id);
		
		return Response.status(Response.Status.OK).entity(usuario).build();
	}

	// Modificar un Usuario
	// PUT: http://localhost:8080/api/usuarios/{id}
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modificarUsuario(@PathParam("id") String id, Usuario usuario) throws Exception {
		
		servicio.modificarUsuario(
				id, 
				usuario.getNombre(), 
				usuario.getApellidos(), 
				usuario.getEmail(), 
				usuario.getClave(), 
				usuario.getFecha_nacimiento(), 
				usuario.getTelefono());
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}
}