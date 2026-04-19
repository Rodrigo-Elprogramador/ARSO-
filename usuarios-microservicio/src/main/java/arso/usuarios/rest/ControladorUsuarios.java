package arso.usuarios.rest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import arso.usuarios.modelo.Usuario;
import arso.usuarios.servicio.IServicioUsuario;
import io.jsonwebtoken.Claims;
import servicio.FactoriaServicios;

@Path("usuarios")
public class ControladorUsuarios {

    private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    // -----------------------------------------------
    // Alta de usuario: PÚBLICA (@PermitAll)
    // POST http://localhost:8080/api/usuarios
    // -----------------------------------------------
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response altaUsuario(UsuarioDTO dto) throws Exception {
        LocalDate fechaParsed = null;
        if (dto.getFechaNacimiento() != null && !dto.getFechaNacimiento().isBlank()) {
            fechaParsed = LocalDate.parse(dto.getFechaNacimiento());
        }
        String id = servicio.altaUsuario(
                dto.getNombre(), dto.getApellidos(), dto.getEmail(),
                dto.getClave(), fechaParsed, dto.getTelefono());
        URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();
        return Response.created(nuevaURL).build();
    }

    // -----------------------------------------------
    // Recuperación de usuario: autenticado
    // GET http://localhost:8080/api/usuarios/{id}
    // -----------------------------------------------
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USUARIO")
    public Response getUsuario(@PathParam("id") String id) throws Exception {
        Usuario usuario = servicio.getUsuario(id);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setFechaNacimiento(usuario.getFecha_nacimiento().toString());
        dto.setTelefono(usuario.getTelefono());
        return Response.ok(dto).build();
    }

    // -----------------------------------------------
    // Listado de usuarios: autenticado
    // GET http://localhost:8080/api/usuarios
    // -----------------------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USUARIO")
    public Response listarUsuarios() throws Exception {
        List<Usuario> usuarios = servicio.listarUsuarios();
        return Response.ok(usuarios).build();
    }

    // -----------------------------------------------
    // Modificar usuario: autenticado + solo el propio usuario
    // PUT http://localhost:8080/api/usuarios/{id}
    // -----------------------------------------------
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("USUARIO")
    public Response modificarUsuario(@PathParam("id") String id, UsuarioDTO dto) throws Exception {
        
        // Control: solo el usuario autenticado puede modificar sus propios datos
        Claims claims = (Claims) request.getAttribute("claims");
        String usuarioAutenticadoId = claims.getSubject(); // "sub" = id del usuario

        if (!usuarioAutenticadoId.equals(id)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("No puede modificar datos de otro usuario").build();
        }

        LocalDate fecha = null;
        if (dto.getFechaNacimiento() != null && !dto.getFechaNacimiento().isBlank()) {
            fecha = LocalDate.parse(dto.getFechaNacimiento());
        }
        servicio.modificarUsuario(id, dto.getNombre(), dto.getApellidos(),
                dto.getEmail(), dto.getClave(), fecha, dto.getTelefono());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}