// Tarea 7
package arso.productos.adaptadores;

import arso.productos.puertos.IEventosCompraventas;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumidorEventosProductos {

    @Autowired
    private IEventosCompraventas eventosCompraventas;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "productos")
    public void onMensaje(String mensaje,
                          @Header("amqp_receivedRoutingKey") String routingKey) throws Exception {
        Map<String, Object> datos = objectMapper.readValue(mensaje, Map.class);

        if (routingKey.equals("bus.compraventas.compraventa-creada")) {
            String idProducto = (String) datos.get("idProducto");
            eventosCompraventas.onCompraventaCreada(idProducto);
        }
        
    }
}
