//TAREA 7
package arso.productos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange busExchange() {
        return new TopicExchange("bus");
    }

    @Bean
    public Queue colaProductos() {
        return new Queue("productos", true);
    }

    @Bean
    public Binding bindingProductos(Queue colaProductos, TopicExchange busExchange) {
        // Escucha eventos de compraventas
        return BindingBuilder.bind(colaProductos).to(busExchange).with("bus.compraventas.#");
    }
}
