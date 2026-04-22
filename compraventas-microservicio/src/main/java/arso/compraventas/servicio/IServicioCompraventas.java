package arso.compraventas.servicio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import arso.compraventas.modelo.Compraventa;
import repositorio.EntidadNoEncontrada;

public interface IServicioCompraventas {
    
    public String realizarCompraventa(String idProducto, String idComprador) throws Exception;
    
    public Page<Compraventa> recuperarComprasUsuario(String idComprador, Pageable pageable);
    
    public Page<Compraventa> recuperarVentasUsuario(String idVendedor, Pageable pageable);
    
    public Page<Compraventa> recuperarTransacciones(String idComprador, String idVendedor, Pageable pageable);
    
    public Compraventa recuperarCompraventa(String id) throws EntidadNoEncontrada;
}