package auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        // Rutas con @PermitAll: públicas (alta de usuario, login)
        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            return;
        }

        String authorization = requestContext.getHeaderString("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No se adjunta el token correctamente").build());
        } else {
            String token = authorization.substring("Bearer ".length()).trim();
            try {
                Claims claims = JwtUtils.validateToken(token);
                // Guardamos los claims en el request para usarlos en el controlador
                this.servletRequest.setAttribute("claims", claims);

                Set<String> roles = new HashSet<>(Arrays.asList(claims.get("roles", String.class).split(",")));

                if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {
                    String[] allowedRoles = resourceInfo.getResourceMethod()
                            .getAnnotation(RolesAllowed.class).value();
                    if (roles.stream().noneMatch(r -> Arrays.asList(allowedRoles).contains(r))) {
                        requestContext.abortWith(
                                Response.status(Response.Status.FORBIDDEN)
                                        .entity("No tiene rol de acceso").build());
                    }
                }
            } catch (Exception e) {
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
            }
        }
    }
}
