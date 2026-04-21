package arso.productos.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import arso.productos.modelo.Estado;
import arso.productos.modelo.Producto;

public interface RepositorioProductos extends JpaRepository<Producto, String> {

    @Query("SELECT p FROM Producto p WHERE MONTH(p.fechaPublicacion) = :mes AND YEAR(p.fechaPublicacion) = :year")
    List<Producto> getByMonthYear(@Param("mes") int mes, @Param("year") int year);

    @Query("SELECT p FROM Producto p WHERE " +
           "(:categoria IS NULL OR p.categoria.id = :categoria) AND " +
           "(:descripcion IS NULL OR p.descripcion LIKE %:descripcion%) AND " +
           "(:estado IS NULL OR p.estado = :estado) AND " +
           "(:precioMaximo <= 0.0 OR p.precio <= :precioMaximo)")
    List<Producto> getByFiltrado(@Param("categoria") String categoria, 
                                 @Param("descripcion") String descripcion, 
                                 @Param("estado") Estado estado, 
                                 @Param("precioMaximo") double precioMaximo);
}