package arso.usuarios.rest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response altaUsuario(UsuarioDTO dto) throws Exception {
        LocalDate fechaParsed = null;
        if (!isBlank(dto.getFechaNacimiento())) {
            fechaParsed = LocalDate.parse(dto.getFechaNacimiento());
        }
        String id = servicio.altaUsuario(
                dto.getNombre(), dto.getApellidos(), dto.getEmail(),
                dto.getClave(), fechaParsed, dto.getTelefono(), dto.getGithubId());
        URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();
        return Response.created(nuevaURL).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USUARIO")
    public Response getUsuario(@PathParam("id") String id) throws Exception {
        Usuario usuario = servicio.getUsuario(id);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setFechaNacimiento(usuario.getFecha_nacimiento() != null ? usuario.getFecha_nacimiento().toString() : null);
        dto.setTelefono(usuario.getTelefono());
        dto.setGithubId(usuario.getGithubId());
        return Response.ok(dto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USUARIO")
    public Response listarUsuarios() throws Exception {
        List<Usuario> usuarios = servicio.listarUsuarios();
        
        List<UsuarioResumenDTO> resumen = usuarios.stream().map(u -> {
            UsuarioResumenDTO dto = new UsuarioResumenDTO();
            dto.setId(u.getId());
            dto.setNombre(u.getNombre() + " " + u.getApellidos());
            String url = uriInfo.getAbsolutePathBuilder().path(u.getId()).build().toString();
            dto.setUrl(url);
            return dto;
        }).collect(Collectors.toList());

        return Response.ok(resumen).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("USUARIO")
    public Response modificarUsuario(@PathParam("id") String id, UsuarioDTO dto) throws Exception {
        Claims claims = (Claims) request.getAttribute("claims");
        String usuarioAutenticadoId = claims.getSubject();

        if (!usuarioAutenticadoId.equals(id)) {
        	throw new ForbiddenException("No puede modificar datos de otro usuario");
        }

        LocalDate fecha = null;
        if (!isBlank(dto.getFechaNacimiento())) {
            fecha = LocalDate.parse(dto.getFechaNacimiento());
        }
        servicio.modificarUsuario(id, dto.getNombre(), dto.getApellidos(),
                dto.getEmail(), dto.getClave(), fecha, dto.getTelefono(), dto.getGithubId());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @GET
    @Path("/{id}/nombre")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getNombreUsuario(@PathParam("id") String id) throws Exception {
        Usuario usuario = servicio.getUsuario(id);
        return Response.ok("{\"nombre\": \"" + usuario.getNombre() + "\"}").build();
    }
    
    @GET
    @Path("/verificar")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response verificarCredenciales(@QueryParam("email") String email, @QueryParam("password") String password) throws Exception {
        try {
            Usuario usuario = servicio.comprobarUsuario(email, password);
            return Response.ok(transformToMap(usuario)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/github/{githubId}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getUsuarioByGithubId(@PathParam("githubId") String githubId) throws Exception {
        try {
            Usuario usuario = servicio.getUsuarioByGithubId(githubId);
            return Response.ok(transformToMap(usuario)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private java.util.Map<String, Object> transformToMap(Usuario usuario) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", usuario.getId());
        map.put("nombre", usuario.getNombre() + " " + usuario.getApellidos());
        map.put("roles", usuario.isAdministrador() ? new String[]{"USUARIO", "ADMINISTRADOR"} : new String[]{"USUARIO"});
        return map;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
