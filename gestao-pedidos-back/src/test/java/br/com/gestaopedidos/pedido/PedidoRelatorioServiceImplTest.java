package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.excecao.NegocioException;
import br.com.gestaopedidos.core.excecao.RecursoNaoEncontradoException;
import br.com.gestaopedidos.core.relatorio.RelatorioFactory;
import br.com.gestaopedidos.core.relatorio.RelatorioPDFService;
import br.com.gestaopedidos.core.relatorio.dto.RelatorioDTO;
import br.com.gestaopedidos.pedido.dto.PedidoItemProdutoDTO;
import br.com.gestaopedidos.pedido.entidade.Pedido;
import br.com.gestaopedidos.pedido.entidade.PedidoItem;
import br.com.gestaopedidos.produto.entidade.Produto;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoRelatorioServiceImplTest {

    @Mock
    private PedidoRepo pedidoRepo;

    @Mock
    private MensagemService mensagemService;

    @Mock
    private RelatorioPDFService relatorioPDFService;

    @InjectMocks
    private PedidoRelatorioServiceImpl pedidoRelatorioService;

    private Pedido pedido;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    @BeforeEach
    void configurar() {
        Produto produtoMonster = Produto.builder().id(1L).nome("Monster").preco(new BigDecimal("10.00")).build();
        Produto produtoRedbull = Produto.builder().id(2L).nome("Redbull").preco(new BigDecimal("20.00")).build();

        PedidoItem pedidoItemMonster = PedidoItem.builder().id(1L).produto(produtoMonster).quantidade(2).build();
        PedidoItem pedidoItemRedbull = PedidoItem.builder().id(2L).produto(produtoRedbull).quantidade(3).build();

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setItensDoPedido(List.of(pedidoItemMonster, pedidoItemRedbull));

        dataInicial = LocalDate.of(2025, 8, 1);
        dataFinal = LocalDate.of(2025, 8, 15);
    }

    @Test
    void gerarRelatorioPedidosSucessoTest() throws JRException, IOException {
        final byte[] pdf = "Any PDF content".getBytes();
        final String nomeRelatorio = "report_orders_" + dataInicial + "_a_" + dataFinal + ".pdf";

        try (MockedStatic<RelatorioFactory> relatorioFactoryMock = Mockito.mockStatic(RelatorioFactory.class)) {
            relatorioFactoryMock.when(RelatorioFactory::getRelatorioPDFService).thenReturn(relatorioPDFService);


            when(pedidoRepo.findByDataPedidoBetween(any(), any())).thenReturn(List.of(pedido));
            when(relatorioPDFService.montarRelatorioComLista(any(), anyList())).thenReturn(pdf);

            RelatorioDTO relatorioDTO = pedidoRelatorioService.gerarRelatorioPedidos(dataInicial, dataFinal);

            assertThat(relatorioDTO).isNotNull();
            assertThat(relatorioDTO.getNomeRelatorio()).isEqualTo(nomeRelatorio);
            assertThat(relatorioDTO.getRelatorioPDF()).isNotEmpty();
            verify(pedidoRepo, times(1)).findByDataPedidoBetween(any(), any());
            verify(relatorioPDFService, times(1)).montarRelatorioComLista(any(), anyList());
        }
    }

    @Test
    @SneakyThrows
    void buscarPedidosSemDadosNoPeriodoSucessoTest() {
        when(pedidoRepo.findByDataPedidoBetween(any(), any())).thenReturn(Collections.emptyList());
        when(mensagemService.getMensagem("pedido.relatorio.erro.2")).thenReturn("No orders found for the past period.");

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            pedidoRelatorioService.gerarRelatorioPedidos(dataInicial, dataFinal);
        });
        verify(relatorioPDFService, never()).montarRelatorioComLista(any(), anyList());
    }

    @Test
    void buscarPedidosValidacaoTest() {
        LocalDate dataFinalInvalida = dataInicial.minusDays(1);

        assertThrows(NegocioException.class, () -> {
            pedidoRelatorioService.gerarRelatorioPedidos(dataInicial, dataFinalInvalida);
        });
        verify(pedidoRepo, never()).findByDataPedidoBetween(any(), any());
    }

    @Test
    void gerarRelatorioAgrupadoPorProdutoSucessoTest() throws JRException, IOException {
        byte[] pdf = "Any PDF content".getBytes();
        final String nomeRelatorio = "grouped_product_orders_report_" + dataInicial + "_a_" + dataFinal + ".pdf";

        try (MockedStatic<RelatorioFactory> relatorioFactoryMock = Mockito.mockStatic(RelatorioFactory.class)) {
            relatorioFactoryMock.when(RelatorioFactory::getRelatorioPDFService).thenReturn(relatorioPDFService);

            when(pedidoRepo.findByDataPedidoBetween(any(), any())).thenReturn(List.of(pedido));
            when(relatorioPDFService.montarRelatorioComLista(any(), anyList())).thenReturn(pdf);

            RelatorioDTO relatorioDTO = pedidoRelatorioService.gerarRelatorioPedidosAgrupadoPorProduto(dataInicial, dataFinal);

            assertThat(relatorioDTO).isNotNull();
            assertThat(relatorioDTO.getNomeRelatorio()).isEqualTo(nomeRelatorio);

            ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
            verify(relatorioPDFService).montarRelatorioComLista(any(), captor.capture());

            List<PedidoItemProdutoDTO> listaAgrupada = captor.getValue();
            assertThat(listaAgrupada).hasSize(2);

            PedidoItemProdutoDTO pedidoItemMonster = listaAgrupada.stream().filter(p -> p.getNomeProduto().equals("Monster")).findFirst().get();
            assertThat(pedidoItemMonster.getQuantidadeTotal()).isEqualTo(2);
            assertThat(pedidoItemMonster.getValorTotal()).isEqualByComparingTo("20.00");

            PedidoItemProdutoDTO pedidoItemRedbull = listaAgrupada.stream().filter(p -> p.getNomeProduto().equals("Redbull")).findFirst().get();
            assertThat(pedidoItemRedbull.getQuantidadeTotal()).isEqualTo(3);
            assertThat(pedidoItemRedbull.getValorTotal()).isEqualByComparingTo("60.00");
        }
    }

}
