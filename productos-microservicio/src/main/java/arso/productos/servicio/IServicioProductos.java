package arso.productos.servicio;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;
import arso.productos.modelo.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServicioProductos {
	
	public String altaProducto(String titulo, String descripcion, double precio, Estado estado, String idCategoria, boolean disponible, String idVendedor) throws RepositorioException, EntidadNoEncontrada;
	
	public void asignarPuntoRecogida(String id, double longitud, double latitud, String descripcion) throws RepositorioException, EntidadNoEncontrada;
	
	public Producto recuperarProducto(String id) throws RepositorioException, EntidadNoEncontrada;
	
	public void modificarProducto(String id, double precio, String descripcion) throws RepositorioException, EntidadNoEncontrada;

	public void addVisualizacion(String id) throws RepositorioException, EntidadNoEncontrada;
	
	public Page<ProductoDTO> historialMes(int mes, int year, Pageable pageable) throws RepositorioException, EntidadNoEncontrada;
	
	public Page<ProductoDTO> buscarProductos(String categoria, String descripcion, Estado estado, double precioMaximo, Pageable pageable) throws RepositorioException, EntidadNoEncontrada;
	
}
