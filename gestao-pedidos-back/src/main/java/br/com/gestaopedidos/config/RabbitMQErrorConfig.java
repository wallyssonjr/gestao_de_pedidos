package br.com.gestaopedidos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class RabbitMQErrorConfig {

    public static final String EXCHANGE_NAME = "errors.exchange";
    public static final String QUEUE_NAME = "errors.log.queue";
    public static final String ROUTING_KEY = "errors.#";

    @Bean
    public TopicExchange errorExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding errorBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

}
