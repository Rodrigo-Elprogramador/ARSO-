package arso.productos.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import arso.productos.modelo.Categoria;
import arso.productos.modelo.Estado;
import arso.productos.modelo.LugarRecogida;
import arso.productos.modelo.Producto;
import arso.productos.modelo.ProductoDTO;
import arso.productos.modelo.UsuarioResumen;
import arso.productos.repositorio.RepositorioProductoAdHoc;

public class ServicioProductos implements IServicioProductos{
	
	private RepositorioProductoAdHoc repositorio = FactoriaRepositorios.getRepositorio(Producto.class);

	private Repositorio<Categoria, String> repositorioCategoria = FactoriaRepositorios.getRepositorio(Categoria.class);

	private Repositorio<UsuarioResumen, String> repositorioUsuario = FactoriaRepositorios.getRepositorio(UsuarioResumen.class);

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
		
		UsuarioResumen vendedor = repositorioUsuario.getById(idVendedor);
		
		Categoria categoria = repositorioCategoria.getById(idCategoria);
		
		LocalDateTime fecha = LocalDateTime.now();
		
		Producto producto = new Producto(titulo, descripcion, precio, estado, fecha, categoria, 0, disponible, null, vendedor);
		
		String idProducto = repositorio.add(producto);
		
		return idProducto;
		


	}
	@Override
	public void asignarPuntoRecogida(String id, double longitud, double latitud, String descripcion) throws RepositorioException, EntidadNoEncontrada{
		
		if(id == null || id.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		
		Producto producto = repositorio.getById(id);
		
		if(descripcion == null || descripcion.isEmpty())
			throw new IllegalArgumentException("descripcion: no debe ser nulo ni vacio");
		if(longitud < 0.0)
			throw new IllegalArgumentException("longitud: no debe ser menor a 0.0");
		
		LugarRecogida lugar = new LugarRecogida(descripcion, longitud, latitud);
		producto.setRecogida(lugar);
		
		repositorio.update(producto);
		
	}
	

	@Override
	public void recuperarProducto(String id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void modificarProducto(String id, double precio, String descripcion) throws RepositorioException, EntidadNoEncontrada{
		
		if(id == null || id.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		
		Producto producto = repositorio.getById(id);
		boolean actualizar = false;
		if(descripcion != null && !descripcion.isEmpty())
			producto.setDescripcion(descripcion);
			actualizar = true;
		if(precio > 0.0)
			producto.setPrecio(precio);
			actualizar = true;
		if(actualizar) {
			repositorio.update(producto);
			System.out.println("Se ha modificado el producto: " + producto.getDescripcion());
		}
	}

	@Override
	public void addVisualizacion(String id) throws RepositorioException, EntidadNoEncontrada{
		if(id == null || id.isBlank())
			throw new IllegalArgumentException("identificador: no debe ser nulo ni vacio o solo espacios en blanco");
		
		Producto producto = repositorio.getById(id);
		
		producto.setIncremento();
		
		repositorio.update(producto);
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
