package arso.productos.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import arso.productos.modelo.Categoria;

public interface RepositorioCategorias extends JpaRepository<Categoria, String> {
	
	List<Categoria> findByCategoriaPadreIsNull();
	
	List<Categoria> findByCategoriaPadre(Categoria padre);
}