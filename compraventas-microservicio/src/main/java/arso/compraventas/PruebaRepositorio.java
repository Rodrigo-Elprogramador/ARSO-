package arso.compraventas;

import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import arso.compraventas.modelo.Compraventa;
import arso.compraventas.repositorio.RepositorioCompraventas;

@SpringBootApplication
public class PruebaRepositorio {

    public static void main(String[] args) {
        ConfigurableApplicationContext contexto = SpringApplication.run(PruebaRepositorio.class, args);

        RepositorioCompraventas repo = contexto.getBean(RepositorioCompraventas.class);

        Compraventa cv = new Compraventa();
        cv.setIdProducto("prod-123");
        cv.setTitulo("Bicicleta de montaña");
        cv.setPrecio(150.0);
        cv.setRecogida("Madrid, Centro");
        cv.setIdVendedor("vend-456");
        cv.setNombreVendedor("María");
        cv.setIdComprador("comp-789");
        cv.setNombreComprador("Juan");
        cv.setFecha(LocalDateTime.now());

        repo.save(cv);
        System.out.println("Compraventa guardada con ID: " + cv.getId());

        long total = repo.count();
        System.out.println("Total de documentos en la colección: " + total);

        contexto.close();
    }
}