package arso.compraventas.adaptadores;

import org.springframework.stereotype.Component;
import arso.compraventas.adaptadores.dto.UsuarioNombreDTO;
import arso.compraventas.adaptadores.retrofit.UsuariosRetrofitAPI;
import arso.compraventas.puertos.UsuariosPuerto;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component
public class UsuariosAdaptador implements UsuariosPuerto {

    private final UsuariosRetrofitAPI api;

    public UsuariosAdaptador() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081/") 
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        
        this.api = retrofit.create(UsuariosRetrofitAPI.class);
    }

    @Override
    public String recuperarNombreUsuario(String idUsuario) throws Exception {
        Response<UsuarioNombreDTO> response = api.getNombreUsuario(idUsuario).execute();
        
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getNombre();
        } else {
            throw new Exception("Error al comunicar con Usuarios. HTTP: " + response.code());
        }
    }
}