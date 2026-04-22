package arso.compraventas.adaptadores;

import org.springframework.stereotype.Component;
import arso.compraventas.adaptadores.dto.ProductoInfoDTO;
import arso.compraventas.adaptadores.retrofit.ProductosRetrofitAPI;
import arso.compraventas.puertos.ProductosPuerto;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component
public class ProductosAdaptador implements ProductosPuerto {

    private final ProductosRetrofitAPI api;

    public ProductosAdaptador() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/") 
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        
        this.api = retrofit.create(ProductosRetrofitAPI.class);
    }

    @Override
    public ProductoInfoDTO recuperarInfoProducto(String idProducto) throws Exception {
        Response<ProductoInfoDTO> response = api.getProducto(idProducto).execute();
        
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new Exception("Error al comunicar con Productos. HTTP: " + response.code());
        }
    }
}