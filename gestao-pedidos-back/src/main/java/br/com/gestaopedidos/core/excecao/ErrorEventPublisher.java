package br.com.gestaopedidos.core.excecao;

import br.com.gestaopedidos.config.RabbitMQErrorConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Log4j2
@Profile("dev")
@Service
@RequiredArgsConstructor
public class ErrorEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishErrorEvent(String errorMessage) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQErrorConfig.EXCHANGE_NAME,
                    "errors.log",
                    errorMessage
            );
        } catch (Exception e) {
            log.warn("Tentativa de publicar no rabbitmq um erro, mas falou. Fluxo segue sem interrupções: {}", e.getMessage());
        }
    }
}
