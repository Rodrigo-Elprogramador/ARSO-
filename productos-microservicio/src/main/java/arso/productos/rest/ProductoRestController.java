package arso.productos.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;
import arso.productos.modelo.ProductoDTO;
import arso.productos.servicio.IServicioProductos;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

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
            @RequestParam(required = false, defaultValue = "0") double precioMaximo) {
        try {
            List<ProductoDTO> productos = servicio.buscarProductos(categoria, descripcion, estado, precioMaximo);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/historial/{mes}/{year}")
    public ResponseEntity<List<Producto>> historialMes(@PathVariable int mes, @PathVariable int year) {
        try {
            List<Producto> productos = servicio.historialMes(mes, year);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/visualizacion")
    public ResponseEntity<Void> addVisualizacion(@PathVariable String id) {
        try {
            servicio.addVisualizacion(id);
            return ResponseEntity.ok().build();
        } catch (EntidadNoEncontrada e) {
            return ResponseEntity.notFound().build();
        } catch (RepositorioException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
