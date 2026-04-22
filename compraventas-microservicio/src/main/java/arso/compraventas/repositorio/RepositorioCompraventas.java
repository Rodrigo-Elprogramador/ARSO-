package arso.compraventas.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import arso.compraventas.modelo.Compraventa;

public interface RepositorioCompraventas extends MongoRepository<Compraventa, String> {
    
    Page<Compraventa> findByIdComprador(String idComprador, Pageable pageable);
    
    Page<Compraventa> findByIdVendedor(String idVendedor, Pageable pageable);

    Page<Compraventa> findByIdCompradorAndIdVendedor(String idComprador, String idVendedor, Pageable pageable);
}