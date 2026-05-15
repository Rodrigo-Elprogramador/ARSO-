package auth;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import arso.usuarios.modelo.Usuario;
import arso.usuarios.servicio.IServicioUsuario;
import servicio.FactoriaServicios;

@Path("auth")
public class ControladorAuth {

    private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);

    // POST: http://localhost:8080/api/auth/login
    // curl -X POST -d "username=email@x.com&password=clave" http://localhost:8080/api/auth/login
    @POST
    @Path("/login")
    @PermitAll
    public Response login(@FormParam("username") String username,
                          @FormParam("password") String password) {
        try {
            Usuario usuario = servicio.comprobarUsuario(username, password);
            if (usuario != null) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", usuario.getId());       // ID del usuario como subject
                claims.put("email", usuario.getEmail());
                claims.put("roles", "USUARIO");           // Todos tienen rol USUARIO
                String token = JwtUtils.generateToken(claims);
                return Response.ok(token).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciales inválidas").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Credenciales inválidas").build();
        }
    }
}