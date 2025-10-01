package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.core.relatorio.dto.RelatorioDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;


public interface PedidoRelatorioService {

    RelatorioDTO gerarRelatorioPedidos(LocalDate dataInicial, LocalDate dataFinal) throws JRException, IOException;

    RelatorioDTO gerarRelatorioPedidosAgrupadoPorProduto(LocalDate dataInicial, LocalDate dataFinal) throws JRException, IOException;

}
