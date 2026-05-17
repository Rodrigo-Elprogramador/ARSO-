package auth;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenFilter implements ContainerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                Claims claims = readClaims(token);
                
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

    private Claims readClaims(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token JWT incorrecto");
        }
        byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
        Map<String, Object> values = objectMapper.readValue(payload, Map.class);
        return new DefaultClaims(values);
    }
}
