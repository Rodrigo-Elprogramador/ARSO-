package arso.productos.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConexionBD {
	public static void main(String[] args) {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		
		try {
			System.out.println("Intentando conectar con la base de datos productos_db...");
			
			emf = Persistence.createEntityManagerFactory("productos-db");
			em = emf.createEntityManager();
			
			System.out.println("✓ Conexión exitosa con la base de datos!");
			System.out.println("✓ El esquema de las tablas debería haberse creado.");
			
		} catch (Exception e) {
			System.err.println("✗ Error de conexión:");
			e.printStackTrace();
		} finally {
			if (em != null) em.close();
			if (emf != null) emf.close();
		}
	}
}
