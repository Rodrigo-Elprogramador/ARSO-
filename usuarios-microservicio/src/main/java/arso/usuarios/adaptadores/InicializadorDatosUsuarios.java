package arso.usuarios.adaptadores;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import utils.EntityManagerHelper;

@WebListener
public class InicializadorDatosUsuarios implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            Long total = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            if (total > 0) {
                return;
            }

            em.getTransaction().begin();
            persistir(em, "1", "Juan", "Perez", "juan@example.com", "1234", false);
            persistir(em, "2", "Maria", "Lopez", "maria@example.com", "1234", false);
            persistir(em, "3", "Admin", "ARSO", "admin@example.com", "admin", true);
            em.getTransaction().commit();
            System.out.println("[Datos] Usuarios iniciales creados.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[Datos] Error inicializando usuarios: " + e.getMessage());
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    private void persistir(EntityManager em, String id, String nombre, String apellidos, String email, String clave,
            boolean administrador) {
        em.createNativeQuery("INSERT INTO usuario "
                + "(id, nombre, apellidos, email, clave, fecha_nacimiento, telefono, administrador, contador_compras, contador_ventas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 0)")
                .setParameter(1, id)
                .setParameter(2, nombre)
                .setParameter(3, apellidos)
                .setParameter(4, email)
                .setParameter(5, clave)
                .setParameter(6, java.sql.Date.valueOf(LocalDate.of(1990, 1, 1)))
                .setParameter(7, "600000000")
                .setParameter(8, administrador)
                .executeUpdate();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
