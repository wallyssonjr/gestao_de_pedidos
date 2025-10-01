package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.excecao.NegocioException;
import br.com.gestaopedidos.core.excecao.RecursoNaoEncontradoException;
import br.com.gestaopedidos.core.relatorio.ExtensaoRelatorioEnum;
import br.com.gestaopedidos.core.relatorio.Relatorio;
import br.com.gestaopedidos.core.relatorio.dto.RelatorioDTO;
import br.com.gestaopedidos.core.util.Constantes;
import br.com.gestaopedidos.pedido.dto.PedidoItemProdutoDTO;
import br.com.gestaopedidos.pedido.entidade.Pedido;
import br.com.gestaopedidos.pedido.entidade.PedidoItem;
import br.com.gestaopedidos.produto.entidade.Produto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PedidoRelatorioServiceImpl implements PedidoRelatorioService {

    private final Relatorio relatorioPedidosPDF;

    private final PedidoRepo pedidoRepo;
    private final MensagemService mensagemService;

    public PedidoRelatorioServiceImpl(@Qualifier("relatorioPDFService") Relatorio relatorioPedidosPDF, PedidoRepo pedidoRepo, MensagemService mensagemService) {
        this.relatorioPedidosPDF = relatorioPedidosPDF;
        this.pedidoRepo = pedidoRepo;
        this.mensagemService = mensagemService;
    }

    public RelatorioDTO gerarRelatorioPedidos(LocalDate dataInicial, LocalDate dataFinal) throws JRException, IOException {
        List<PedidoItem> pedidosItemNoPeriodo = buscarPedidoItemsNoPeriodo(dataInicial, dataFinal);

        relatorioPedidosPDF.setArquivoRelatorio(Constantes.Relatorios.ARQUIVO_RELATORIO_PEDIDOS);
        byte[] pdf = (byte[]) relatorioPedidosPDF.montarRelatorioComLista(null, pedidosItemNoPeriodo);

        final String nomeRelatorio = "relatorio_pedidos_" + dataInicial + "_a_" + dataFinal;

        return RelatorioDTO.builder().relatorioPDF(pdf).nomeRelatorio(nomeRelatorio).extensao(ExtensaoRelatorioEnum.PDF).build();
    }

    public RelatorioDTO gerarRelatorioPedidosAgrupadoPorProduto(LocalDate dataInicial, LocalDate dataFinal) throws JRException, IOException {
        List<PedidoItem> pedidosItemNoPeriodo = buscarPedidoItemsNoPeriodo(dataInicial, dataFinal);

        Map<Produto, List<PedidoItem>> itensPorProduto = pedidosItemNoPeriodo.stream().collect(Collectors.groupingBy(PedidoItem::getProduto));
        List<PedidoItemProdutoDTO> pedidoItemProdutoDTOs = PedidoItemProdutoDTO.calcularTotaisItensProduto(itensPorProduto);

        relatorioPedidosPDF.setArquivoRelatorio(Constantes.Relatorios.ARQUIVO_RELATORIO_PEDIDOS_PRODUTO_AGRUPADO);
        byte[] pdf = (byte[]) relatorioPedidosPDF.montarRelatorioComLista(null, pedidoItemProdutoDTOs);

        final String nomeRelatorio = "relatorio_pedidos_produtos_agrupado_" + dataInicial + "_a_" + dataFinal;

        return RelatorioDTO.builder().relatorioPDF(pdf).nomeRelatorio(nomeRelatorio).extensao(ExtensaoRelatorioEnum.PDF).build();
    }

    private List<PedidoItem> buscarPedidoItemsNoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataFinal.isBefore(dataInicial)) {
            throw new NegocioException("pedido.relatorio.erro.3");
        }

        LocalDateTime dataHoraInicial = dataInicial.atStartOfDay();
        LocalDateTime dataHoraFinal = dataFinal.atTime(LocalTime.MAX);

        List<Pedido> pedidosNoPeriodo = pedidoRepo.findByDataPedidoBetween(dataHoraInicial, dataHoraFinal);
        if (CollectionUtils.isEmpty(pedidosNoPeriodo)) {
            throw new RecursoNaoEncontradoException(mensagemService.getMensagem("pedido.relatorio.erro.2"));
        }

        return pedidosNoPeriodo.stream()
                .map(Pedido::getItensDoPedido)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

}
