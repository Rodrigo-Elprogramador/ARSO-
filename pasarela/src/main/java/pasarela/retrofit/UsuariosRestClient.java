package pasarela.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Map;

public interface UsuariosRestClient {

    @GET("usuarios/verificar")
    Call<Map<String, Object>> verificarCredenciales(@Query("email") String email, @Query("password") String password);

    @GET("usuarios/github/{githubId}")
    Call<Map<String, Object>> recuperarPorGithubId(@Path("githubId") String githubId);
}
