package arso.compraventas.servicio;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import arso.compraventas.adaptadores.dto.ProductoInfoDTO;
import arso.compraventas.modelo.Compraventa;
import arso.compraventas.puertos.ProductosPuerto;
import arso.compraventas.puertos.UsuariosPuerto;
import arso.compraventas.repositorio.RepositorioCompraventas;
import repositorio.EntidadNoEncontrada;

@Service
public class ServicioCompraventas implements IServicioCompraventas {

    private final RepositorioCompraventas repositorio;
    private final ProductosPuerto productosPuerto;
    private final UsuariosPuerto usuariosPuerto;

    @Autowired
    public ServicioCompraventas(RepositorioCompraventas repositorio, ProductosPuerto productosPuerto, UsuariosPuerto usuariosPuerto) {
        this.repositorio = repositorio;
        this.productosPuerto = productosPuerto;
        this.usuariosPuerto = usuariosPuerto;
    }

    @Override
    public String realizarCompraventa(String idProducto, String idComprador) throws Exception {
        
        ProductoInfoDTO producto = productosPuerto.recuperarInfoProducto(idProducto);

        String nombreComprador = usuariosPuerto.recuperarNombreUsuario(idComprador);
        String nombreVendedor = usuariosPuerto.recuperarNombreUsuario(producto.getIdVendedor());

        Compraventa cv = new Compraventa();
        cv.setIdProducto(idProducto);
        cv.setTitulo(producto.getTitulo());
        cv.setPrecio(producto.getPrecio());
        cv.setRecogida(producto.getRecogida());
        
        cv.setIdVendedor(producto.getIdVendedor());
        cv.setNombreVendedor(nombreVendedor);
        
        cv.setIdComprador(idComprador);
        cv.setNombreComprador(nombreComprador);
        
        cv.setFecha(LocalDateTime.now());

        repositorio.save(cv);
        
        return cv.getId();
    }

    @Override
    public Page<Compraventa> recuperarComprasUsuario(String idComprador, Pageable pageable) {
        return repositorio.findByIdComprador(idComprador, pageable);
    }

    @Override
    public Page<Compraventa> recuperarVentasUsuario(String idVendedor, Pageable pageable) {
        return repositorio.findByIdVendedor(idVendedor, pageable);
    }

    @Override
    public Page<Compraventa> recuperarTransacciones(String idComprador, String idVendedor, Pageable pageable) {
        return repositorio.findByIdCompradorAndIdVendedor(idComprador, idVendedor, pageable);
    }

    @Override
    public Compraventa recuperarCompraventa(String id) throws EntidadNoEncontrada {
        return repositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontrada("No existe la compraventa con ID: " + id));
    }
}