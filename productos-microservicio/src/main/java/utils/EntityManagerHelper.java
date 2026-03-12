package utils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class EntityManagerHelper {

    private static EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        EntityManagerHelper.entityManager = em;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void closeEntityManager() {
        // En Spring Boot con @Transactional no es necesario cerrar manualmente
        // El contenedor gestiona el ciclo de vida del EntityManager
    }
}
