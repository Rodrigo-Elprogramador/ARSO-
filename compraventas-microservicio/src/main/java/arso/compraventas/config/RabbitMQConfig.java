// TAREA 7
package arso.compraventas.config;

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
    public Queue colaCompraventas() {
        return new Queue("compraventas", true);
    }

    @Bean
    public Binding bindingCompraventas(Queue colaCompraventas, TopicExchange busExchange) {
        // Compraventas escucha sus propios eventos (por si necesita en el futuro)
        return BindingBuilder.bind(colaCompraventas).to(busExchange).with("bus.compraventas.#");
    }
}
