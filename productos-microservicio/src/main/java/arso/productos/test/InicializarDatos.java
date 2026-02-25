package arso.productos.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import arso.productos.modelo.UsuarioResumen;

/**
 * Clase para inicializar la base de datos de Productos con usuarios provisionales.
 * Estos usuarios permitirán dar de alta productos hasta que el microservicio 
 * esté integrado con eventos del microservicio de Usuarios.
 */
public class InicializarDatos {
	
	public static void main(String[] args) {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		
		try {
			System.out.println("=== Inicializando base de datos de Productos ===\n");
			
			emf = Persistence.createEntityManagerFactory("productos-db");
			em = emf.createEntityManager();
			
			em.getTransaction().begin();
			
			// Crear usuarios provisionales
			UsuarioResumen usuario1 = new UsuarioResumen(
				"user-001", 
				"juan.perez@email.com", 
				"Juan", 
				"Pérez García"
			);
			
			UsuarioResumen usuario2 = new UsuarioResumen(
				"user-002", 
				"maria.lopez@email.com", 
				"María", 
				"López Martínez"
			);
			
			UsuarioResumen usuario3 = new UsuarioResumen(
				"user-003", 
				"carlos.ruiz@email.com", 
				"Carlos", 
				"Ruiz Sánchez"
			);
			
			// Persistir usuarios
			em.persist(usuario1);
			em.persist(usuario2);
			em.persist(usuario3);
			
			em.getTransaction().commit();
			
			System.out.println("✓ Base de datos inicializada correctamente");
			System.out.println("✓ Se han creado 3 usuarios provisionales:");
			System.out.println("  - " + usuario1.getNombre() + " " + usuario1.getApellidos() + " (" + usuario1.getEmail() + ")");
			System.out.println("  - " + usuario2.getNombre() + " " + usuario2.getApellidos() + " (" + usuario2.getEmail() + ")");
			System.out.println("  - " + usuario3.getNombre() + " " + usuario3.getApellidos() + " (" + usuario3.getEmail() + ")");
			
		} catch (Exception e) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.err.println("✗ Error al inicializar datos:");
			e.printStackTrace();
		} finally {
			if (em != null) em.close();
			if (emf != null) emf.close();
		}
	}
}
