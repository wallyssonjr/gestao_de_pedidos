package br.com.gestaopedidos.core.relatorio;

import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Relatorio {

    String getArquivoRelatorio();

    void setArquivoRelatorio(String path);

    Object montarRelatorioComLista(Map<String, Object> parametros, List<?> listaDeDados) throws JRException, IOException;

    Object montarRelatorioApenasComParametros(Map<String, Object> parametros) throws JRException, IOException;

}
