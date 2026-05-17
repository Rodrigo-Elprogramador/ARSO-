package arso.usuarios.adaptadores;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import arso.usuarios.modelo.Usuario;
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
            persistir(em, "Juan", "Perez", "juan@example.com", "1234", false);
            persistir(em, "Maria", "Lopez", "maria@example.com", "1234", false);
            persistir(em, "Admin", "ARSO", "admin@example.com", "admin", true);
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

    private void persistir(EntityManager em, String nombre, String apellidos, String email, String clave,
            boolean administrador) {
        Usuario usuario = new Usuario(nombre, email, apellidos, clave, LocalDate.of(1990, 1, 1), "600000000",
                administrador);
        em.persist(usuario);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
