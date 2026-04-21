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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos", description = "Operaciones sobre la gestión de productos de segunda mano")
public interface ProductosApi {

    @Operation(summary = "Dar de alta un producto", description = "Registra un producto validando los datos de entrada.")
    @PostMapping
    ResponseEntity<Void> altaProducto(ProductoAltaDTO dto) throws Exception;

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