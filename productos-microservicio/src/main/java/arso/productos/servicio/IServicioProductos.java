package arso.productos.servicio;

import java.util.List;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;
import arso.productos.modelo.ProductoDTO;

public interface IServicioProductos {
	
	public String altaProducto(String titulo, String descripcion, double precio, Estado estado, String idCategoria, boolean disponible, String idVendedor) throws RepositorioException, EntidadNoEncontrada;
	
	public void asignarPuntoRecogida(String id, double longitud, double latitud, String descripcion) throws RepositorioException, EntidadNoEncontrada;
	
	public void recuperarProducto(String id);
	
	public void modificarProducto(String id, double precio, String descripcion) throws RepositorioException, EntidadNoEncontrada;

	public void addVisualizacion(String id) throws RepositorioException, EntidadNoEncontrada;
	
	public List<Producto> historialMes(int mes, int year) throws RepositorioException, EntidadNoEncontrada;
	
	public List<ProductoDTO> buscarProductos(String categoria, String descripcion, Estado estado, double precioMaximo) throws RepositorioException, EntidadNoEncontrada;
	
}
