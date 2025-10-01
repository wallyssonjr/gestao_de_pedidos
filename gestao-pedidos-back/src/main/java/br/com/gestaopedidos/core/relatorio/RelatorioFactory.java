package br.com.gestaopedidos.core.relatorio;

import java.util.Objects;

public class RelatorioFactory {

    private static RelatorioPDFService relatorioPDFService;

    public static RelatorioPDFService getRelatorioPDFService() {
        if (Objects.isNull(relatorioPDFService)) {
            relatorioPDFService = new RelatorioPDFService();
        }
        return relatorioPDFService;
    }

}
