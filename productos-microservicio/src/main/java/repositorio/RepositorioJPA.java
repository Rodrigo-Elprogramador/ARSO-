package repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import utils.EntityManagerHelper;

@Repository
@Transactional
public abstract class RepositorioJPA<T extends Identificable> implements RepositorioString<T> {

	public abstract Class<T> getClase();

	@Override
	public String add(T entity) throws RepositorioException {
	    EntityManager em = EntityManagerHelper.getEntityManager();
	    try {
	        em.persist(entity);
	        em.flush(); // Fuerza la generación del ID
	        return entity.getId();
	    } catch (Exception e) {
	        throw new RepositorioException("Error al guardar la entidad", e);
	    }
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
	    EntityManager em = EntityManagerHelper.getEntityManager();
	    try {
	        T instancia = em.find(getClase(), entity.getId());
	        if (instancia == null) {
	            throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
	        }
	        em.merge(entity);
	    } catch (RuntimeException e) {
	        throw new RepositorioException("Error al actualizar la entidad con id " + entity.getId(), e);
	    }
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
	    EntityManager em = EntityManagerHelper.getEntityManager();
	    try {
	        T instancia = em.find(getClase(), entity.getId());
	        if (instancia == null) {
	            throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
	        }
	        em.remove(instancia);
	    } catch (RuntimeException e) {
	        throw new RepositorioException("Error al borrar la entidad con id " + entity.getId(), e);
	    }
	}

	@Override
	public T getById(String id) throws EntidadNoEncontrada, RepositorioException {
	    EntityManager em = EntityManagerHelper.getEntityManager();
	    try {
	        T instancia = em.find(getClase(), id);
	        if (instancia == null) {
	            throw new EntidadNoEncontrada(id + " no existe en el repositorio");
	        }
	        em.refresh(instancia);
	        return instancia;
	    } catch (RuntimeException e) {
	        throw new RepositorioException("Error al recuperar la entidad con id " + id, e);
	    }
	}

	@Override
	public List<T> getAll() throws RepositorioException {
	    EntityManager em = EntityManagerHelper.getEntityManager();
	    try {
	        final String queryString = "SELECT t FROM " + getClase().getSimpleName() + " t";
	        Query query = em.createQuery(queryString);
	        return query.getResultList();
	    } catch (RuntimeException e) {
	        throw new RepositorioException("Error buscando todas las entidades de " + getClase().getSimpleName(), e);
	    }
	}

	@Override
	public List<String> getIds() throws RepositorioException {
	    EntityManager em = EntityManagerHelper.getEntityManager();
	    try {
	        final String queryString = "SELECT t.id FROM " + getClase().getSimpleName() + " t";
	        Query query = em.createQuery(queryString);
	        return query.getResultList();
	    } catch (RuntimeException e) {
	        throw new RepositorioException("Error buscando todos los ids de " + getClase().getSimpleName(), e);
	    }
	}

}
