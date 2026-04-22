package arso.compraventas.adaptadores.retrofit;

import arso.compraventas.adaptadores.dto.ProductoInfoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductosRetrofitAPI {
    @GET("api/productos/{id}")
    Call<ProductoInfoDTO> getProducto(@Path("id") String id);
}