package arso.productos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import arso.productos.modelo.UsuarioResumen;

public interface RepositorioUsuariosResumen extends JpaRepository<UsuarioResumen, String> {
}