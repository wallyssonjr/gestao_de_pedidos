package br.com.gestaopedidos.config;

import br.com.gestaopedidos.core.relatorio.Relatorio;
import br.com.gestaopedidos.core.relatorio.RelatorioFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RelatorioConfig {

    @Bean
    @Qualifier("relatorioPDFService")
    public Relatorio relatorioPDFService() {
        return RelatorioFactory.getRelatorioPDFService();
    }

}
