package arso.productos.repositorio;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import repositorio.RepositorioException;
import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;
import utils.EntityManagerHelper;

public class RepositorioProductoAdHocJPA extends RepositorioProductoJPA implements RepositorioProductoAdHoc{
	
	@Override
	public List<Producto> getByMonthYear(int month, int year) throws RepositorioException{
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();

			LocalDateTime inicioMes = LocalDateTime.of(year, month, 1, 0, 0);
			LocalDateTime finMes = inicioMes.plusMonths(1);
			
			String queryString = "SELECT e " +
	                			 "FROM Producto e " + 
								 "WHERE e.fechaPublicacion >= :inicioMes AND e.fechaPublicacion < :finMes";
			TypedQuery<Producto> query = em.createQuery(queryString, Producto.class);
			
			query.setParameter("inicioMes", inicioMes);
		    query.setParameter("finMes", finMes);
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			
			return query.getResultList();
		} catch (RuntimeException e) {
			throw new RepositorioException("Error buscando by sin votos", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
			
	}
	
	@Override
	public List<Producto> getByFiltrado(String categoria, String descripcion, Estado estado, double precioMaximo) throws RepositorioException{
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			
			//Se establece la petición mínima
			String queryString = "SELECT e FROM Producto e WHERE 1 = 1";
			//Se crea un mapa para los parámetros
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			if (categoria != null && !categoria.isEmpty()) {
		        queryString += " AND e.categoria.id = :categoria";
		        parameters.put("categoria",  categoria );
		    }

		    if (descripcion != null && !descripcion.isEmpty()) {
		        queryString += " AND e.descripcion LIKE :descripcion";
		        parameters.put("descripcion", "%" + descripcion + "%");
		    }

		    if (estado != null) {
		        queryString += " AND e.estado = :estado";
		        parameters.put("estado", estado);
		    }

		    if (precioMaximo > 0) {
		        queryString += " AND e.precio <= :precioMaximo";
		        parameters.put("precioMaximo", precioMaximo);
		    }
			
			TypedQuery<Producto> query = em.createQuery(queryString, Producto.class);
			//Pasamos los parametros que se han añadido
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
		        query.setParameter(entry.getKey(), entry.getValue());
		    }
		    
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);

			return query.getResultList();
		} catch (RuntimeException e) {
			throw new RepositorioException("Error buscando by sin votos", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
	
	@Override
	public List<Producto> getByVendedor(String idVendedor) throws RepositorioException{
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();

			String queryString = "SELECT e " +
	                			 "FROM Producto e " + 
								 "WHERE e.vendedor.id = :idVendedor";
			TypedQuery<Producto> query = em.createQuery(queryString, Producto.class);
			
			query.setParameter("idVendedor", idVendedor);
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			
			return query.getResultList();
		} catch (RuntimeException e) {
			throw new RepositorioException("Error buscando by sin votos", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
	
	
	@Override
	public List<Producto> getByCategoria(String idCategoria) throws RepositorioException{
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();

			String queryString = "SELECT e " +
	                			 "FROM Producto e " + 
								 "WHERE e.categoria.id = :idCategoria";
			TypedQuery<Producto> query = em.createQuery(queryString, Producto.class);
			
			query.setParameter("idCategoria", idCategoria);
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			
			return query.getResultList();
		} catch (RuntimeException e) {
			throw new RepositorioException("Error buscando by sin votos", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

}
