package arso.productos.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import arso.productos.modelo.Estado;
import arso.productos.modelo.ProductoAltaDTO;
import arso.productos.modelo.ProductoDTO;
import arso.productos.modelo.CategoriaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Productos", description = "Operaciones sobre la gestión de productos de segunda mano")
public interface ProductosApi {

    @Operation(summary = "Recuperar categorías raíz", description = "Obtiene las categorías que no tienen padre.")
    @GetMapping("/categorias")
    ResponseEntity<List<CategoriaDTO>> getCategoriasRaiz() throws Exception;

    @Operation(summary = "Recuperar descendientes de una categoría", description = "Obtiene las subcategorías de una categoría dada.")
    @GetMapping("/categorias/{id}/descendientes")
    ResponseEntity<List<CategoriaDTO>> getSubcategorias(@PathVariable String id) throws Exception;

    @Operation(summary = "Dar de alta un producto", description = "Registra un producto validando los datos de entrada.")
    @PostMapping
    ResponseEntity<Void> altaProducto(ProductoAltaDTO dto) throws Exception;

    @Operation(summary = "Asignar lugar de recogida", description = "Asigna las coordenadas y descripción del lugar de recogida.")
    @PutMapping("/{id}/recogida")
    ResponseEntity<Void> asignarPuntoRecogida(@PathVariable String id, @RequestParam double longitud, @RequestParam double latitud, @RequestParam String descripcion) throws Exception;

    @Operation(summary = "Modificar datos de un producto", description = "Permite modificar el precio y la descripción de un producto.")
    @PutMapping("/{id}")
    ResponseEntity<Void> modificarProducto(@PathVariable String id, @RequestParam(required = false) Double precio, @RequestParam(required = false) String descripcion) throws Exception;

    @Operation(summary = "Recuperar un producto", description = "Obtiene los detalles de un producto por su ID.")
    @GetMapping("/{id}")
    ResponseEntity<EntityModel<ProductoDTO>> getProducto(@PathVariable String id) throws Exception;

    @Operation(summary = "Buscar productos", description = "Busca productos con filtros y devuelve una lista paginada con HATEOAS.")
    @GetMapping("/buscar")
    ResponseEntity<PagedModel<EntityModel<ProductoDTO>>> buscarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Estado estado,
            @RequestParam(required = false, defaultValue = "0") double precioMaximo,
            Pageable pageable) throws Exception;

    @Operation(summary = "Historial del mes", description = "Obtiene los productos de un mes/año de forma paginada.")
    @GetMapping("/historial/{mes}/{year}")
    ResponseEntity<PagedModel<EntityModel<ProductoDTO>>> historialMes(
            @PathVariable int mes, 
            @PathVariable int year, 
            Pageable pageable) throws Exception;

    @Operation(summary = "Añadir visualización", description = "Incrementa el contador de visualizaciones de un producto.")
    @PutMapping("/{id}/visualizacion")
    ResponseEntity<Void> addVisualizacion(@PathVariable String id) throws Exception;
}