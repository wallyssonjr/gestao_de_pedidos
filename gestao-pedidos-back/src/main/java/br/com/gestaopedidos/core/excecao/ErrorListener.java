package br.com.gestaopedidos.core.excecao;

import br.com.gestaopedidos.config.RabbitMQErrorConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Log4j2
@Component
public class ErrorListener {

    @RabbitListener(queues = RabbitMQErrorConfig.QUEUE_NAME)
    public void onError(String errorMessage) {
        log.error("=================================================");
        log.error("ERRO RECEBIDO DA FILA RABBITMQ:");
        log.error(errorMessage);
        log.error("=================================================");
    }

}
