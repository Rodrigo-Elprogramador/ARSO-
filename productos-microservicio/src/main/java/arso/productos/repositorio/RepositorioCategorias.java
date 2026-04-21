package arso.productos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import arso.productos.modelo.Categoria;

public interface RepositorioCategorias extends JpaRepository<Categoria, String> {
}