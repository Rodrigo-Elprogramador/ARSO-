package arso.productos.modelo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"arso.productos", "repositorio", "utils", "servicio"})
@EntityScan(basePackages = {"arso.productos.modelo"})
@EnableJpaRepositories(basePackages = {"arso.productos.repositorio"})  // AÑADIR ESTA LÍNEA
public class ProductosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductosApplication.class, args);
    }
}
