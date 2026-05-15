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

        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            return;
        }

        String authorization = requestContext.getHeaderString("Authorization");
        String token = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring("Bearer ".length()).trim();
        } else if (servletRequest.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : servletRequest.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No se adjunta el token correctamente").build());
        } else {
            try {
                String[] parts = token.split("\\.");
                Claims claims;
                if (parts.length == 3) {
                     claims = io.jsonwebtoken.Jwts.parser().parseClaimsJwt(parts[0] + "." + parts[1] + ".").getBody();
                } else {
                     claims = JwtUtils.validateToken(token);
                }
                
                this.servletRequest.setAttribute("claims", claims);

                Set<String> roles = new HashSet<>();
                Object rolesObj = claims.get("roles");
                if (rolesObj instanceof java.util.List) {
                    roles.addAll((java.util.List<String>) rolesObj);
                } else if (rolesObj instanceof String) {
                    roles.addAll(Arrays.asList(((String) rolesObj).split(",")));
                }

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
