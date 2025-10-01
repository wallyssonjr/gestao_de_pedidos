package br.com.gestaopedidos.config;

import br.com.gestaopedidos.core.util.Constantes;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MensagemConfig {

    @Bean
    public MessageSource mensagens() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(Constantes.Mensagem.ARQUIVO_MENSAGENS);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
