package arso.productos.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import arso.productos.modelo.Categoria;
import arso.productos.modelo.Estado;
import arso.productos.modelo.LugarRecogida;
import arso.productos.modelo.Producto;
import arso.productos.modelo.ProductoDTO;
import arso.productos.modelo.UsuarioResumen;
import arso.productos.repositorio.RepositorioCategorias;
import arso.productos.repositorio.RepositorioProductos;
import arso.productos.repositorio.RepositorioUsuariosResumen;

@Service
@Transactional
public class ServicioProductos implements IServicioProductos {
	
	@Autowired
	private RepositorioProductos repositorio;

	@Autowired
	private RepositorioCategorias repositorioCategoria;

	@Autowired
	private RepositorioUsuariosResumen repositorioUsuario;


	@Override
	public String altaProducto(String titulo, String descripcion, double precio, Estado estado, String idCategoria, boolean disponible, String idVendedor) throws RepositorioException, EntidadNoEncontrada{
		if(titulo == null || titulo.isEmpty())
			throw new IllegalArgumentException("titulo: no debe ser nulo ni vacio");
		if(descripcion == null || descripcion.isEmpty())
			throw new IllegalArgumentException("descripcion: no debe ser nulo ni vacio");
		if(precio < 0.0)
			throw new IllegalArgumentException("precio: no debe ser menor a 0.0");
		if(estado==null)
			throw new IllegalArgumentException("estado: no debe ser nulo");
		if(idVendedor == null || idVendedor.isEmpty())
			throw new IllegalArgumentException("idVendedor: no debe ser nulo o vacio");
		if(idCategoria == null || idCategoria.isEmpty())
			throw new IllegalArgumentException("idCategoria: no debe ser nulo o vacio");
		
		UsuarioResumen vendedor = repositorioUsuario.findById(idVendedor).orElseThrow(() -> new EntidadNoEncontrada("Vendedor no encontrado id: "+ idVendedor));
		
		Categoria categoria = repositorioCategoria.findById(idCategoria).orElseThrow(() -> new EntidadNoEncontrada("Categoría no encontrada id: "+ idCategoria));
		
		LocalDateTime fecha = LocalDateTime.now();
		
		Producto producto = new Producto(titulo, descripcion, precio, estado, fecha, categoria, 0, disponible, null, vendedor);
		
		Producto guardado = repositorio.save(producto);
		
		return guardado.getId();

	}
	
	@Override
	public void asignarPuntoRecogida(String id, double longitud, double latitud, String descripcion) throws RepositorioException, EntidadNoEncontrada{
		
		if(id == null || id.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		if(descripcion == null || descripcion.isEmpty())
			throw new IllegalArgumentException("descripcion: no debe ser nulo ni vacio");
		if(longitud < 0.0)
			throw new IllegalArgumentException("longitud: no debe ser menor a 0.0");
		
		Producto producto = repositorio.findById(id).orElseThrow(() -> new EntidadNoEncontrada("Producto no encontrado"));
		
		LugarRecogida lugar = new LugarRecogida(descripcion, longitud, latitud);
		producto.setRecogida(lugar);
		
		repositorio.save(producto);
		
	}
	

	@Override
	public void recuperarProducto(String id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void modificarProducto(String id, double precio, String descripcion) throws RepositorioException, EntidadNoEncontrada{
		
		if(id == null || id.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		
		Producto producto = repositorio.findById(id).orElseThrow(() -> new EntidadNoEncontrada("Producto no encontrado"));
		boolean actualizar = false;
		if(descripcion != null && !descripcion.isEmpty())
			producto.setDescripcion(descripcion);
			actualizar = true;
		if(precio > 0.0)
			producto.setPrecio(precio);
			actualizar = true;
		if(actualizar) {
			repositorio.save(producto);
			System.out.println("Se ha modificado el producto: " + producto.getDescripcion());
		}
	}

	@Override
	public void addVisualizacion(String id) throws RepositorioException, EntidadNoEncontrada{
		if(id == null || id.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		
		Producto producto = repositorio.findById(id).orElseThrow(() -> new EntidadNoEncontrada("Producto no encontrado"));
		
		producto.setIncremento();
		
		repositorio.save(producto);
	}
	
	@Override
	public List<Producto> historialMes(int mes, int year) throws RepositorioException, EntidadNoEncontrada{
		if(mes<0 || mes>12)
			throw new IllegalArgumentException("mes: Debe de ser un valor entre 1 y 12");
		if(year<0 || year >2026)
			throw new IllegalArgumentException("year: Debe de ser un valor entre 1 y 2026");
		return repositorio.getByMonthYear(mes, year);
	}

	@Override
	public List<ProductoDTO> buscarProductos(String categoria, String descripcion, Estado estado, double precioMaximo) throws RepositorioException, EntidadNoEncontrada{
		//No se hace control de parametros ya que se realiza en el repositorio antes de hacer la petición
		return repositorio.getByFiltrado(categoria, descripcion, estado, precioMaximo).stream().map(this::transformToDTO).collect(Collectors.toList());
	}
	
	
	private ProductoDTO transformToDTO(Producto producto) {
		ProductoDTO productoDTO = new ProductoDTO(producto.getId(), producto.getTitulo(), producto.getPrecio(), producto.getEstado(), producto.getFechaPublicacion(), producto.getCategoria().getNombre(), producto.getVisualizaciones());
		return productoDTO;
	}
}
