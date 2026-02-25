package arso.productos.repositorio;


import java.util.List;

import repositorio.RepositorioException;
import repositorio.RepositorioString;
import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;

public interface RepositorioProductoAdHoc extends RepositorioString<Producto> {

	public List<Producto> getByMonthYear(int month, int year) throws RepositorioException;
	
	public List<Producto> getByFiltrado(String categoria, String descripcion, Estado estado, double precioMaximo) throws RepositorioException;

	//Devuelve los productos que contengan a un vendedor concreto (uso en eliminarUsuario)
	public List<Producto> getByVendedor(String idVendedor) throws RepositorioException;

	//Devuelve las categorias que contengan a un vendedor concreto (uso en eliminarCategoria)
	public List<Producto> getByCategoria(String idCategoria) throws RepositorioException;

}
