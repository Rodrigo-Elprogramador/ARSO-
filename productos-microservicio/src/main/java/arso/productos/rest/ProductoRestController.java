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

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import arso.productos.modelo.Categoria;
import arso.productos.modelo.CategoriaDTO;

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
	@GetMapping("/categorias")
	public ResponseEntity<List<CategoriaDTO>> getCategoriasRaiz() throws Exception {
		List<Categoria> raiz = this.servicio.getCategoriasRaiz();
		return ResponseEntity.ok(raiz.stream()
				.map(c -> new CategoriaDTO(c.getId(), c.getNombre(), c.getDescripcion()))
				.collect(Collectors.toList()));
	}

	@Override
	@GetMapping("/categorias/{id}/descendientes")
	public ResponseEntity<List<CategoriaDTO>> getSubcategorias(@PathVariable String id) throws Exception {
		List<Categoria> subs = this.servicio.getSubcategorias(id);
		return ResponseEntity.ok(subs.stream()
				.map(c -> new CategoriaDTO(c.getId(), c.getNombre(), c.getDescripcion()))
				.collect(Collectors.toList()));
	}

	@Override
	@PostMapping
	public ResponseEntity<Void> altaProducto(@Valid @RequestBody ProductoAltaDTO dto) throws Exception {
		String usuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!dto.getIdVendedor().equals(usuarioAutenticado)) {
			throw new AccessDeniedException("Solo el propietario puede dar de alta el producto");
		}

		String id = this.servicio.altaProducto(dto.getTitulo(), dto.getDescripcion(), dto.getPrecio(), dto.getEstado(),
				dto.getIdCategoria(), dto.isDisponible(), usuarioAutenticado);

		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(nuevaURL).build();
	}

	@Override
	@PutMapping("/{id}/recogida")
	public ResponseEntity<Void> asignarPuntoRecogida(@PathVariable String id, @RequestParam double longitud, @RequestParam double latitud, @RequestParam String descripcion) throws Exception {
		checkPropiedad(id);
		this.servicio.asignarPuntoRecogida(id, longitud, latitud, descripcion);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PutMapping("/{id}")
	public ResponseEntity<Void> modificarProducto(@PathVariable String id, @RequestParam(required = false) Double precio, @RequestParam(required = false) String descripcion) throws Exception {
		checkPropiedad(id);
		this.servicio.modificarProducto(id, precio != null ? precio : 0.0, descripcion);
		return ResponseEntity.noContent().build();
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

	private void checkPropiedad(String idProducto) throws Exception {
		String usuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
		Producto producto = this.servicio.recuperarProducto(idProducto);
		if (producto.getVendedor() == null || !producto.getVendedor().getId().equals(usuarioAutenticado)) {
			throw new AccessDeniedException("No es el propietario del producto");
		}
	}
}
