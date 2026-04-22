package arso.compraventas.puertos;
import arso.compraventas.adaptadores.dto.ProductoInfoDTO;

public interface ProductosPuerto {
    ProductoInfoDTO recuperarInfoProducto(String idProducto) throws Exception;
}