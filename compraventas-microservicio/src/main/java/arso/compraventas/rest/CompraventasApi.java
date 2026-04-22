package arso.compraventas.rest;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Compraventas", description = "Gestión de transacciones entre usuarios")
public interface CompraventasApi {

    @Operation(summary = "Realizar una compra")
    @PostMapping
    ResponseEntity<Void> comprar(@Valid @RequestBody CompraventaAltaDTO dto) throws Exception;

    @Operation(summary = "Recuperar una compraventa por ID")
    @GetMapping("/{id}")
    ResponseEntity<EntityModel<CompraventaDTO>> getCompraventa(@PathVariable String id) throws Exception;

    @Operation(summary = "Obtener compras de un usuario")
    @GetMapping("/compras/{idUsuario}")
    ResponseEntity<PagedModel<EntityModel<CompraventaDTO>>> getCompras(
            @PathVariable String idUsuario, 
            Pageable pageable);

    @Operation(summary = "Obtener ventas de un usuario")
    @GetMapping("/ventas/{idUsuario}")
    ResponseEntity<PagedModel<EntityModel<CompraventaDTO>>> getVentas(
            @PathVariable String idUsuario, 
            Pageable pageable);

    @Operation(summary = "Filtrar compraventas entre dos usuarios", 
               description = "Busca transacciones específicas cruzando comprador y vendedor.")
    @GetMapping("/compraventas")
    ResponseEntity<PagedModel<EntityModel<CompraventaDTO>>> getCompraventas(
            @RequestParam String idComprador, 
            @RequestParam String idVendedor, 
            Pageable pageable);
}