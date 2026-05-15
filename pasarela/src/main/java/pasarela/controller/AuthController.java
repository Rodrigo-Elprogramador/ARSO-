package pasarela.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pasarela.retrofit.UsuariosRestClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${usuarios.base-url:http://localhost:8081/api/}")
    private String usuariosBaseUrl;

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

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credenciales, HttpServletResponse response) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        try {
            Map<String, Object> usuario = usuariosClient.verificarCredenciales(email, password).execute().body();

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String idUsuario = usuario.get("id").toString();
            String nombreCompleto = usuario.get("nombre").toString();
            Object roles = usuario.get("roles");
            String token = crearToken(idUsuario, nombreCompleto, roles);

            response.addCookie(crearCookie(token, JWT_TIEMPO_VALIDEZ));

            return ResponseEntity.ok(crearRespuesta(token, idUsuario, nombreCompleto, roles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(crearCookie(null, 0));
        return ResponseEntity.noContent().build();
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

    private Cookie crearCookie(String token, int maxAge) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private Map<String, Object> crearRespuesta(String token, String idUsuario, String nombreCompleto, Object roles) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("identificador", idUsuario);
        responseBody.put("nombreCompleto", nombreCompleto);
        responseBody.put("roles", roles);
        return responseBody;
    }
}
