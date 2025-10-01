package br.com.gestaopedidos.core.relatorio;

import br.com.gestaopedidos.core.excecao.NegocioException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioPDFService implements Relatorio {

    private String arquivoRelatorio;

    @Override
    public String getArquivoRelatorio() {
        if (!StringUtils.hasLength(arquivoRelatorio)) {
            throw new NegocioException("Não foi definido o caminho do jrxml do relatório a ser gerado.");
        }
        return arquivoRelatorio;
    }

    @Override
    public void setArquivoRelatorio(String path) {
        this.arquivoRelatorio = path;
    }

    @Override
    public Object montarRelatorioComLista(Map<String, Object> parametros, List<?> listaDeDados) throws JRException, IOException {
        return montarRelatorio(parametros, listaDeDados);
    }

    @Override
    public Object montarRelatorioApenasComParametros(Map<String, Object> parametros) throws JRException, IOException {
        return montarRelatorio(parametros, Collections.emptyList());
    }

    private byte[] montarRelatorio(Map<String, Object> parametros, List<?> listaDeDados) throws IOException, JRException {
        InputStream reportStream = new ClassPathResource(this.getArquivoRelatorio()).getInputStream();
        JasperReport relatorioJasper = JasperCompileManager.compileReport(reportStream);

        JRDataSource dataSource;
        if (listaDeDados != null && !listaDeDados.isEmpty()) {
            dataSource = new JRBeanCollectionDataSource(listaDeDados);
        } else {
            dataSource = new JREmptyDataSource();
        }
        if (parametros == null) {
            parametros = new HashMap<>();
        }
        parametros.put("Criador", "Gestão de Pedidos");
        parametros.put("Data/hora criação", LocalDateTime.now());

        JasperPrint relatorioPreenchido = JasperFillManager.fillReport(relatorioJasper, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(relatorioPreenchido);
    }
}
