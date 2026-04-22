package arso.compraventas.adaptadores.retrofit;

import arso.compraventas.adaptadores.dto.UsuarioNombreDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuariosRetrofitAPI {
    @GET("api/usuarios/{id}/nombre")
    Call<UsuarioNombreDTO> getNombreUsuario(@Path("id") String id);
}