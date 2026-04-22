package arso.compraventas.rest;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import arso.compraventas.modelo.Compraventa;
import arso.compraventas.servicio.IServicioCompraventas;

@RestController
@RequestMapping("/api/compraventas")
public class CompraventaRestController implements CompraventasApi {

	private final IServicioCompraventas servicio;

	@Autowired
	private CompraventaDTOAssembler assembler;

	@Autowired
	private PagedResourcesAssembler<CompraventaDTO> pagedResourcesAssembler;

	@Autowired
	public CompraventaRestController(IServicioCompraventas servicio) {
		this.servicio = servicio;
	}

	@Override
	public ResponseEntity<Void> comprar(@Valid @RequestBody CompraventaAltaDTO dto) throws Exception {
		String id = servicio.realizarCompraventa(dto.getIdProducto(), dto.getIdComprador());
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(location).build();
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<CompraventaDTO>> getCompraventa(@PathVariable String id) throws Exception {
		Compraventa cv = servicio.recuperarCompraventa(id);
		CompraventaDTO dto = transformToDTO(cv);
		return ResponseEntity.ok(assembler.toModel(dto));
	}

	@Override
	@GetMapping("/compras/{idUsuario}")
	public ResponseEntity<PagedModel<EntityModel<CompraventaDTO>>> getCompras(@PathVariable String idUsuario, Pageable pageable) {
		Page<Compraventa> pagina = servicio.recuperarComprasUsuario(idUsuario, pageable);
		Page<CompraventaDTO> paginaDTO = pagina.map(this::transformToDTO);
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(paginaDTO, assembler));
	}

	@Override
	@GetMapping("/ventas/{idUsuario}")
	public ResponseEntity<PagedModel<EntityModel<CompraventaDTO>>> getVentas(@PathVariable String idUsuario, Pageable pageable) {
		Page<Compraventa> pagina = servicio.recuperarVentasUsuario(idUsuario, pageable);
		Page<CompraventaDTO> paginaDTO = pagina.map(this::transformToDTO);
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(paginaDTO, assembler));
	}

	@Override
	@GetMapping("/compraventas")
	public ResponseEntity<PagedModel<EntityModel<CompraventaDTO>>> getCompraventas(
			@RequestParam String idComprador, 
			@RequestParam String idVendedor, 
			Pageable pageable) {
		Page<Compraventa> pagina = servicio.recuperarTransacciones(idComprador, idVendedor, pageable);
		Page<CompraventaDTO> paginaDTO = pagina.map(this::transformToDTO);
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(paginaDTO, assembler));
	}

	private CompraventaDTO transformToDTO(Compraventa cv) {
		CompraventaDTO dto = new CompraventaDTO();
		dto.setId(cv.getId());
		dto.setIdProducto(cv.getIdProducto());
		dto.setTitulo(cv.getTitulo());
		dto.setPrecio(cv.getPrecio());
		dto.setNombreVendedor(cv.getNombreVendedor());
		dto.setNombreComprador(cv.getNombreComprador());
		dto.setFecha(cv.getFecha());
		return dto;
	}
}