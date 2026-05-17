package arso.compraventas.adaptadores;

import org.springframework.beans.factory.annotation.Value;
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

    public ProductosAdaptador(@Value("${servicios.productos.base-url:http://localhost:8082/}") String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
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
