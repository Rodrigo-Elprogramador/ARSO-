package pasarela.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pasarela.retrofit.UsuariosRestClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${usuarios.base-url:http://localhost:8081/api/}")
    private String usuariosBaseUrl;

    @Autowired
    private ObjectMapper objectMapper;

    private final int JWT_TIEMPO_VALIDEZ = 3600;
    private UsuariosRestClient usuariosClient;

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(usuariosBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.usuariosClient = retrofit.create(UsuariosRestClient.class);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String githubId = oauthUser.getAttributes().get("id").toString();

        try {
            Map<String, Object> usuario = usuariosClient.recuperarPorGithubId(githubId).execute().body();

            if (usuario == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario de GitHub no registrado en el sistema");
                return;
            }

            String idUsuario = usuario.get("id").toString();
            String nombreCompleto = usuario.get("nombre").toString();
            Object roles = usuario.get("roles");
            String token = crearToken(idUsuario, nombreCompleto, roles);

            response.addCookie(crearCookie(token));

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("identificador", idUsuario);
            responseBody.put("nombreCompleto", nombreCompleto);
            responseBody.put("roles", roles);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String crearToken(String idUsuario, String nombreCompleto, Object roles) {
        return Jwts.builder()
                .setSubject(idUsuario)
                .claim("nombre", nombreCompleto)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (JWT_TIEMPO_VALIDEZ * 1000L)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }

    private Cookie crearCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(JWT_TIEMPO_VALIDEZ);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
