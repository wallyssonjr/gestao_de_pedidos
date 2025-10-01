package br.com.gestaopedidos.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constantes {

    private final String CLASSPTAH_STR = "classpath:";

    @UtilityClass
    public class Relatorios {
        public final String ARQUIVO_RELATORIO_PEDIDOS = "relatorios/pedidos/pedidos_report.jrxml";
        public final String ARQUIVO_RELATORIO_PEDIDOS_PRODUTO_AGRUPADO = "relatorios/pedidos/produto_agrupado_report.jrxml";
    }

    @UtilityClass
    public class Mensagem {
        public final String ARQUIVO_MENSAGENS = CLASSPTAH_STR + "mensagens";
    }
}
