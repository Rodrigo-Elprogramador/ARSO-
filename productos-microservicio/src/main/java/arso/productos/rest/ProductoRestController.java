package arso.productos.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import arso.productos.modelo.Estado;
import arso.productos.modelo.ProductoDTO;
import arso.productos.servicio.IServicioProductos;


@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private IServicioProductos servicio;

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Estado estado,
            @RequestParam(required = false, defaultValue = "0") double precioMaximo) throws Exception {
    	
    	List<ProductoDTO> productos = servicio.buscarProductos(categoria, descripcion, estado, precioMaximo);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/historial/{mes}/{year}")
    public ResponseEntity<List<ProductoDTO>> historialMes(@PathVariable int mes, @PathVariable int year) throws Exception {
        
        List<ProductoDTO> productosDTO = servicio.historialMes(mes, year).stream()
            .map(p -> new ProductoDTO(
                p.getId(), 
                p.getTitulo(), 
                p.getPrecio(), 
                p.getEstado(), 
                p.getFechaPublicacion(), 
                p.getCategoria() != null ? p.getCategoria().getNombre() : null, 
                p.getVisualizaciones()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(productosDTO);
    }

    @PutMapping("/{id}/visualizacion")
    public ResponseEntity<Void> addVisualizacion(@PathVariable String id) throws Exception{
    	servicio.addVisualizacion(id);
        return ResponseEntity.noContent().build();
    }
}
