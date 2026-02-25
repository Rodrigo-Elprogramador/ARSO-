package arso.usuarios.repositorio;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import arso.usuarios.modelo.Usuario;
import utils.EntityManagerHelper;

public class RepositorioUsuarioAdHocJPA extends RepositorioUsuarioJPA implements RepositorioUsuarioAdHoc{

	@Override
	public Usuario getByIdentificación(String email, String clave) throws RepositorioException, EntidadNoEncontrada{
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();

			
			String queryString = "SELECT e " +
	                			 "FROM Usuario e " + 
								 "WHERE e.email = :email AND e.clave = :clave";
			TypedQuery<Usuario> query = em.createQuery(queryString, Usuario.class);
			query.setParameter("email", email);
			query.setParameter("clave", clave);
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			
			return query.getSingleResult();
		} catch (RuntimeException e) {
			throw new RepositorioException("Error buscando Usuario", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
	
	@Override
	public boolean getInstanciaEmail(String email) throws RepositorioException, EntidadNoEncontrada{
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();

			
			String queryString = "SELECT COUNT(e) " +
	                			 "FROM Usuario e " + 
								 "WHERE e.email = :email";
			TypedQuery<Long> query = em.createQuery(queryString, Long.class);
			query.setParameter("email", email);
			Long instancia = query.getSingleResult();
			return  instancia > 0;
		} catch (RuntimeException e) {
			throw new RepositorioException("Error buscando Usuario", e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
}
