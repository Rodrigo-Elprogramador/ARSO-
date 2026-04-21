package arso.productos.rest;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;
import arso.productos.modelo.ProductoAltaDTO;
import arso.productos.modelo.ProductoDTO;
import arso.productos.servicio.IServicioProductos;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController implements ProductosApi {

	private IServicioProductos servicio;

	@Autowired
	private PagedResourcesAssembler<ProductoDTO> pagedResourcesAssembler;

	@Autowired
	private ProductoDTOAssembler assembler;

	@Autowired
	public ProductoRestController(IServicioProductos servicio) {
		this.servicio = servicio;
	}

	@Override
	@PostMapping
	public ResponseEntity<Void> altaProducto(@Valid @RequestBody ProductoAltaDTO dto) throws Exception {

		String id = this.servicio.altaProducto(dto.getTitulo(), dto.getDescripcion(), dto.getPrecio(), dto.getEstado(),
				dto.getIdCategoria(), dto.isDisponible(), dto.getIdVendedor());

		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(nuevaURL).build();
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<ProductoDTO>> getProducto(@PathVariable String id) throws Exception {

		Producto producto = this.servicio.recuperarProducto(id);

		ProductoDTO dto = new ProductoDTO(producto.getId(), producto.getTitulo(), producto.getPrecio(),
				producto.getEstado(), producto.getFechaPublicacion(),
				producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
				producto.getVisualizaciones());

		EntityModel<ProductoDTO> model = EntityModel.of(dto);
		model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoRestController.class).getProducto(id))
				.withSelfRel());

		return ResponseEntity.ok(model);
	}

	@Override
	@GetMapping("/buscar")
	public ResponseEntity<PagedModel<EntityModel<ProductoDTO>>> buscarProductos(
			@RequestParam(required = false) String categoria, @RequestParam(required = false) String descripcion,
			@RequestParam(required = false) Estado estado,
			@RequestParam(required = false, defaultValue = "0") double precioMaximo, Pageable pageable)
			throws Exception {

		Page<ProductoDTO> resultado = this.servicio.buscarProductos(categoria, descripcion, estado, precioMaximo,
				pageable);

		PagedModel<EntityModel<ProductoDTO>> pagedModel = this.pagedResourcesAssembler.toModel(resultado, assembler);

		return ResponseEntity.ok(pagedModel);
	}

	@Override
	@GetMapping("/historial/{mes}/{year}")
	public ResponseEntity<PagedModel<EntityModel<ProductoDTO>>> historialMes(@PathVariable int mes,
			@PathVariable int year, Pageable pageable) throws Exception {

		Page<ProductoDTO> resultado = this.servicio.historialMes(mes, year, pageable);

		PagedModel<EntityModel<ProductoDTO>> pagedModel = this.pagedResourcesAssembler.toModel(resultado, assembler);

		return ResponseEntity.ok(pagedModel);
	}

	@Override
	@PutMapping("/{id}/visualizacion")
	public ResponseEntity<Void> addVisualizacion(@PathVariable String id) throws Exception {
		this.servicio.addVisualizacion(id);
		return ResponseEntity.noContent().build();
	}
}