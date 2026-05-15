// TAREA 7
package arso.usuarios.adaptadores;

import arso.usuarios.puertos.IEventosCompraventas;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.util.Map;

public class ConsumidorEventosUsuarios {

    private static final String HOST = "localhost";
    private static final String EXCHANGE = "bus";
    private static final String COLA = "usuarios";
    private static final String BINDING = "bus.compraventas.#";

    private final IEventosCompraventas eventosCompraventas;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Connection connection;
    private Channel channel;

    public ConsumidorEventosUsuarios(IEventosCompraventas eventosCompraventas) {
        this.eventosCompraventas = eventosCompraventas;
    }

    public void iniciar() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();

        // Declaramos el exchange y la cola (idempotente)
        channel.exchangeDeclare(EXCHANGE, "topic", true);
        channel.queueDeclare(COLA, true, false, false, null);
        channel.queueBind(COLA, EXCHANGE, BINDING);

        DeliverCallback callback = (consumerTag, delivery) -> {
            try {
                String routingKey = delivery.getEnvelope().getRoutingKey();
                String mensaje = new String(delivery.getBody(), "UTF-8");
                Map<String, Object> datos = objectMapper.readValue(mensaje, Map.class);

                if ("bus.compraventas.compraventa-creada".equals(routingKey)) {
                    String idVendedor = (String) datos.get("idVendedor");
                    String idComprador = (String) datos.get("idComprador");
                    eventosCompraventas.onCompraventaCreada(idVendedor, idComprador);
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        channel.basicConsume(COLA, false, callback, ct -> {});
    }

    public void detener() throws Exception {
        if (channel != null) channel.close();
        if (connection != null) connection.close();
    }
}
