package arso.compraventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import arso.compraventas.servicio.IServicioCompraventas;

@SpringBootApplication
public class ProgramaServicio {

    public static void main(String[] args) {
        
        ConfigurableApplicationContext contexto = SpringApplication.run(ProgramaServicio.class, args);
        IServicioCompraventas servicio = contexto.getBean(IServicioCompraventas.class);

        try {
            String idProductoReal = "1"; 
            String idCompradorReal = "1";

            System.out.println("Intentando comprar el producto " + idProductoReal + " para el usuario " + idCompradorReal);
            
            String idCompraventa = servicio.realizarCompraventa(idProductoReal, idCompradorReal);
            
            System.out.println("¡ÉXITO! Compraventa realizada y guardada en Mongo con ID: " + idCompraventa);

        } catch (Exception e) {
            System.err.println("¡ERROR en la prueba!: " + e.getMessage());
            e.printStackTrace();
        } finally {
            contexto.close();
        }
    }
}