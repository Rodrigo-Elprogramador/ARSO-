package arso.compraventas.rest.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public JwtRequestFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        SecurityContextHolder.clearContext();
        String jwt = getToken(request);

        if (jwt != null) {
            try {
                Map<String, Object> claims = readClaims(jwt);
                String username = (String) claims.get("sub");

                if (username != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, getAuthorities(claims.get("roles")));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring("Bearer ".length()).trim();
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private Map<String, Object> readClaims(String jwt) throws IOException {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token JWT incorrecto");
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        return objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {
        });
    }

    private List<SimpleGrantedAuthority> getAuthorities(Object rolesObj) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (rolesObj instanceof List) {
            authorities = ((List<?>) rolesObj).stream()
                    .map(this::toAuthority)
                    .collect(Collectors.toList());
        } else if (rolesObj instanceof String) {
            for (String role : ((String) rolesObj).split(",")) {
                authorities.add(toAuthority(role));
            }
        }
        return authorities;
    }

    private SimpleGrantedAuthority toAuthority(Object role) {
        String value = String.valueOf(role).trim();
        if (!value.startsWith("ROLE_")) {
            value = "ROLE_" + value;
        }
        return new SimpleGrantedAuthority(value);
    }
}
