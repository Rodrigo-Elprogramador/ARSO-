package arso.productos.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import arso.productos.modelo.Categoria;
import arso.productos.modelo.Estado;
import arso.productos.modelo.LugarRecogida;
import arso.productos.modelo.Producto;
import arso.productos.modelo.UsuarioResumen;
import arso.productos.repositorio.RepositorioCategorias;
import arso.productos.repositorio.RepositorioProductos;
import arso.productos.repositorio.RepositorioUsuariosResumen;

@Configuration
public class DatosInicialesConfig {

    @Bean
    public CommandLineRunner inicializarDatosProductos(RepositorioCategorias categorias,
            RepositorioUsuariosResumen usuarios,
            RepositorioProductos productos) {
        return args -> {
            if (!usuarios.existsById("1")) {
                usuarios.save(new UsuarioResumen("1", "juan@example.com", "Juan", "Perez"));
            }
            if (!usuarios.existsById("2")) {
                usuarios.save(new UsuarioResumen("2", "maria@example.com", "Maria", "Lopez"));
            }
            if (!usuarios.existsById("3")) {
                usuarios.save(new UsuarioResumen("3", "admin@example.com", "Admin", "ARSO"));
            }

            Categoria electronica = categorias.findById("cat-electronica")
                    .orElseGet(() -> categorias.save(new Categoria("cat-electronica", "Electronica", "/electronica")));
            if (!categorias.existsById("cat-libros")) {
                categorias.save(new Categoria("cat-libros", "Libros", "/libros"));
            }

            if (productos.count() == 0) {
                UsuarioResumen vendedor = usuarios.findById("1").orElseThrow(IllegalStateException::new);
                Producto producto = new Producto("Camara analogica", "Camara de prueba para Docker y Postman",
                        45.0, Estado.BUEN_ESTADO, LocalDateTime.now(), electronica, 0, true,
                        new LugarRecogida("Campus Espinardo", -1.165, 38.016), vendedor);
                productos.save(producto);
            }
        };
    }
}
