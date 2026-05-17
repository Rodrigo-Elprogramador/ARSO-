package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerHelper {
	private static EntityManagerFactory entityManagerFactory;

	private static final ThreadLocal<EntityManager> entityManagerHolder;

	static {

		entityManagerFactory = Persistence.createEntityManagerFactory("arsousuarios", getJpaProperties());

		entityManagerHolder = new ThreadLocal<EntityManager>();

	}

	private static Map<String, String> getJpaProperties() {
		Map<String, String> properties = new HashMap<>();
		putEnv(properties, "javax.persistence.jdbc.url", "JPA_JDBC_URL");
		putEnv(properties, "javax.persistence.jdbc.user", "JPA_JDBC_USER");
		putEnv(properties, "javax.persistence.jdbc.password", "JPA_JDBC_PASSWORD");
		putEnv(properties, "javax.persistence.jdbc.driver", "JPA_JDBC_DRIVER");
		return properties;
	}

	private static void putEnv(Map<String, String> properties, String property, String env) {
		String value = System.getenv(env);
		if (value != null && !value.trim().isEmpty()) {
			properties.put(property, value);
		}
	}

	public static EntityManager getEntityManager() {

		EntityManager entityManager = entityManagerHolder.get();

		if (entityManager == null || !entityManager.isOpen()) {

			entityManager = entityManagerFactory.createEntityManager();

			entityManagerHolder.set(entityManager);

		}

		return entityManager;

	}

	public static void closeEntityManager() {

		EntityManager entityManager = entityManagerHolder.get();

		if (entityManager != null) {

			entityManagerHolder.set(null);

			entityManager.close();

		}

	}	
}
