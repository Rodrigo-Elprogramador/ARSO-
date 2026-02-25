package arso.productos.repositorio;

import repositorio.RepositorioJPA;
import arso.productos.modelo.Producto;

public class RepositorioProductoJPA extends RepositorioJPA<Producto> {

	@Override
	public Class<Producto> getClase() {

		return Producto.class;

	}

}