package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.core.relatorio.dto.RelatorioDTO;
import br.com.gestaopedidos.core.util.JsonUtil;
import br.com.gestaopedidos.pedido.dto.PedidoDTO;
import br.com.gestaopedidos.pedido.dto.PedidoItemDTO;
import br.com.gestaopedidos.pedido.entidade.Pedido;
import br.com.gestaopedidos.pedido.entidade.PedidoItem;
import br.com.gestaopedidos.produto.ProdutoService;
import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import br.com.gestaopedidos.produto.entidade.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
public class PedidoIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoRepo pedidoRepo;

    @Autowired
    private JsonUtil jsonUtil;

    @MockBean
    private PedidoRelatorioService pedidoRelatorioService;

    private ProdutoDTO produtoDTO;
    private ProdutoDTO produtoDTOAlternativo;
    private Produto produtoMonster;
    private Produto produtoChocolate;

    @BeforeEach
    void configurar() {
        pedidoRepo.deleteAll();
        produtoService.removerTodos();

        produtoDTO = produtoService.salvar(montarProdutoDTOPradrao());
        produtoDTOAlternativo = produtoService.salvar(montarProdutoDTOAlternativo());

        produtoMonster = produtoService.getProdutoMapper().dtoParaProduto(produtoDTO);
        produtoChocolate = produtoService.getProdutoMapper().dtoParaProduto(produtoDTOAlternativo);
    }

    @Test
    void salvarPedido200Test() throws Exception {
        PedidoItemDTO pedidoItemDTO = PedidoItemDTO.builder().idProduto(produtoDTO.getId()).precoProduto(new BigDecimal(10)).quantidade(2).build();
        PedidoDTO novoPedidoDTO = PedidoDTO.builder().itensDoPedido(List.of(pedidoItemDTO)).build();

        mockMvc.perform(post("/pedido/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objetoParaJsonString(novoPedidoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.precoTotal", is(20)));
    }

    @Test
    void salvarPedidoSemEstoque400Test() throws Exception {
        PedidoItemDTO pedidoItemDTO = PedidoItemDTO.builder().idProduto(produtoDTO.getId()).precoProduto(new BigDecimal(10)).quantidade(11).build();
        PedidoDTO novoPedidoDTO = PedidoDTO.builder().itensDoPedido(List.of(pedidoItemDTO)).build();

        mockMvc.perform(post("/pedido/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objetoParaJsonString(novoPedidoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void gerarRelatorioPedidosReportPDF200Test() throws Exception {
        byte[] pdf = "Conteudo PDF qualquer".getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"relatorio.pdf\"");

        RelatorioDTO relatorioDTO = RelatorioDTO.builder()
                .relatorioPDF(pdf)
                .httpHeader(headers)
                .build();

        when(pedidoRelatorioService.gerarRelatorioPedidos(any(), any())).thenReturn(relatorioDTO);

        mockMvc.perform(get("/pedido/pedidos-relatorio-pdf")
                        .param("dataInicial", LocalDate.now().toString())
                        .param("dataFinal", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"relatorio.pdf\""))
                .andExpect(content().bytes(pdf));
        ;
    }

    @Test
    void gerarRelatorioPedidosProdutoAgrupadoPDF200Test() throws Exception {
        byte[] pdf = "Conteudo PDF qualquer".getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"relatorio.pdf\"");

        RelatorioDTO relatorioDTO = RelatorioDTO.builder()
                .relatorioPDF(pdf)
                .httpHeader(headers)
                .build();

        when(pedidoRelatorioService.gerarRelatorioPedidosAgrupadoPorProduto(any(LocalDate.class), any(LocalDate.class))).thenReturn(relatorioDTO);

        mockMvc.perform(get("/pedido/pedidos-produto-relatorio-pdf")
                        .param("dataInicial", LocalDate.now().toString())
                        .param("dataFinal", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"relatorio.pdf\""))
                .andExpect(content().bytes(pdf));
    }

    @Test
    void listarUltimosXDias200Test() throws Exception {
        montarPedidoPadrao();

        mockMvc.perform(get("/pedido/ultimos-x-dias")
                        .param("dias", "7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].itensDoPedido", hasSize(2)));
    }

    @Test
    void listarPedidosAgrupadoProduto200Test() throws Exception {
        montarPedidoPadrao();

        mockMvc.perform(get("/pedido/pedidos-agrupado-produto")
                        .param("dias", "7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private void montarPedidoPadrao() {
        PedidoItem pedidoItem1 = PedidoItem.builder().produto(produtoMonster).quantidade(2).build();
        PedidoItem pedidoItem2 = PedidoItem.builder().produto(produtoChocolate).quantidade(1).build();

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now().minusDays(1));
        pedido.setItensDoPedido(List.of(pedidoItem1, pedidoItem2));

        pedidoItem1.setPedido(pedido);
        pedidoItem2.setPedido(pedido);

        pedidoRepo.save(pedido);
    }

    private ProdutoDTO montarProdutoDTOPradrao() {
        return ProdutoDTO.builder()
                .nome("Monster")
                .preco(new BigDecimal("10"))
                .descricao("Energético")
                .quantidade(10)
                .build();
    }

    private ProdutoDTO montarProdutoDTOAlternativo() {
        return ProdutoDTO.builder()
                .nome("Chocolate")
                .preco(new BigDecimal("5"))
                .descricao("Doce")
                .quantidade(10)
                .build();
    }

}
