//TAREA 7
package arso.compraventas.adaptadores;

import arso.compraventas.puertos.IBusEventos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BusEventosRabbitMQ implements IBusEventos {

    private static final String EXCHANGE = "bus";
    private static final String PREFIJO = "bus.compraventas";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void publicarEvento(String tipoEvento, Map<String, Object> datos) throws Exception {
        String routingKey = PREFIJO + "." + tipoEvento;
        String mensaje = objectMapper.writeValueAsString(datos);
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, mensaje);
    }
}
