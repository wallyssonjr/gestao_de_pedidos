package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.excecao.NegocioException;
import br.com.gestaopedidos.core.excecao.RecursoNaoEncontradoException;
import br.com.gestaopedidos.pedido.dto.PedidoDTO;
import br.com.gestaopedidos.pedido.dto.PedidoItemDTO;
import br.com.gestaopedidos.pedido.dto.PedidoItemProdutoDTO;
import br.com.gestaopedidos.pedido.entidade.Pedido;
import br.com.gestaopedidos.pedido.entidade.PedidoItem;
import br.com.gestaopedidos.produto.ProdutoService;
import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import br.com.gestaopedidos.produto.dto.ProdutoMapperImpl;
import br.com.gestaopedidos.produto.entidade.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceImplTest {

    @Mock
    private PedidoRepo pedidoRepo;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private MensagemService mensagemService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Produto produto;
    private ProdutoDTO produtoDTO;
    private PedidoItemDTO pedidoItemDTO;
    private Pedido pedido;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void configurar() {
        pedido = Pedido.builder()
                .id(1L)
                .dataPedido(LocalDateTime.now())
                .build();

        produto = Produto.builder()
                .id(1L)
                .nome("Monster")
                .descricao("Energético")
                .preco(new BigDecimal("10"))
                .quantidade(20)
                .build();

        produtoDTO = ProdutoDTO.builder()
                .id(1L)
                .nome("Monster")
                .descricao("Energético")
                .preco(new BigDecimal("10"))
                .quantidade(20)
                .build();

        pedidoItemDTO = criarPedidoItemQuantidade2DoEstoque();

        pedidoDTO = PedidoDTO.builder()
                .itensDoPedido(List.of(pedidoItemDTO))
                .build();
    }

    @Test
    void listarTodosPedidosDTOSucessoTest() {
        when(pedidoRepo.findAll()).thenReturn(List.of(pedido));

        List<PedidoDTO> resultado = pedidoService.listarTodos();

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(1);
        assertThat(resultado.get(0).getId()).isEqualTo(pedido.getId());
        verify(pedidoRepo, times(1)).findAll();
    }

    @Test
    void buscarPedidoPorIdDTOSucessoTest() {
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoDTO resultado = pedidoService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(pedidoRepo, times(1)).findById(1L);
    }

    @Test
    void buscarPedidoPorIdDTONaoEncontradoTest() {
        final long idPedidoInexistente = 20L;

        when(pedidoRepo.findById(idPedidoInexistente)).thenReturn(Optional.empty());
        when(mensagemService.getMensagem("geral.erro.1")).thenReturn("Recurso não encontrado.");

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            pedidoService.buscarPorId(idPedidoInexistente);
        });
        verify(pedidoRepo, times(1)).findById(idPedidoInexistente);
    }

    @Test
    void buscarProdutoPorIdDTOValidacaoTest() {
        when(mensagemService.getMensagem("pedido.erro.1")).thenReturn("Id do pedido inválido.");

        assertThrows(NegocioException.class, () -> {
            pedidoService.buscarPorId(0L);
        });
        verify(pedidoRepo, never()).findById(anyLong());
    }

    @Test
    void salvarPedidoSuccessoTest() {
        when(produtoService.buscarPorId(1L)).thenReturn(produtoDTO);
        when(produtoService.getProdutoMapper()).thenReturn(new ProdutoMapperImpl());

        when(pedidoRepo.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido novoPedido = invocation.getArgument(0);
            novoPedido.setId(1L);
            return novoPedido;
        });

        PedidoDTO novoPedido = pedidoService.salvar(pedidoDTO);

        assertThat(novoPedido).isNotNull();
        assertThat(novoPedido.getPrecoTotal()).isEqualTo(new BigDecimal("20"));

        verify(produtoService, times(1)).buscarPorId(1L);

        ArgumentCaptor<ProdutoDTO> produtoCaptor = ArgumentCaptor.forClass(ProdutoDTO.class);
        verify(produtoService, times(1)).salvar(produtoCaptor.capture());
        assertThat(produtoCaptor.getValue().getQuantidade()).isEqualTo(18);

        verify(pedidoRepo, times(1)).save(any(Pedido.class));
    }

    @Test
    void salvarPedidoSemEstoqueExceptionTest() {
        produtoDTO.setQuantidade(1);
        pedidoItemDTO.setQuantidade(2);

        final String erroEsperado = "Não há estoque o suficiente para o produto: " + produtoDTO.getId() + ". Quantidade disponível: " + produtoDTO.getQuantidade();

        when(produtoService.buscarPorId(1L)).thenReturn(produtoDTO);
        when(mensagemService.getMensagem(anyString(), any(), any())).thenReturn(erroEsperado);

        assertThatThrownBy(() -> {
            pedidoService.salvar(pedidoDTO);
        }).isInstanceOf(NegocioException.class).hasMessage(erroEsperado);

        verify(pedidoRepo, never()).save(any());
        verify(produtoService, never()).salvar(any());
    }

    @Test
    void listarUltimosXDiasSucessoTest() {
        when(pedidoRepo.findByDataPedidoGreaterThanEqual(any(LocalDateTime.class))).thenReturn(List.of(pedido));

        List<PedidoDTO> resultado = pedidoService.listarUltimosXDias(7);

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);

        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(pedidoRepo, times(1)).findByDataPedidoGreaterThanEqual(dateCaptor.capture());
        assertThat(dateCaptor.getValue()).isBefore(LocalDateTime.now().minusDays(6));
    }

    @Test
    void listarUltimosXDiasVazioSucessoTest() {
        when(pedidoRepo.findByDataPedidoGreaterThanEqual(any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        List<PedidoDTO> resultado = pedidoService.listarUltimosXDias(1);
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    void listarPedidosAgrupadoProdutoSucessoTest() {
        Produto monsterP = Produto.builder().id(1L).nome("Monster").preco(new BigDecimal("10.00")).build();
        Produto chocolateP = Produto.builder().id(2L).nome("Chocolate").preco(new BigDecimal("25.00")).build();

        PedidoItem pedidoItem1 = PedidoItem.builder().id(101L).produto(monsterP).quantidade(2).build();
        PedidoItem pedidoItem2 = PedidoItem.builder().id(102L).produto(chocolateP).quantidade(3).build();
        PedidoItem pedidoItem3 = PedidoItem.builder().id(103L).produto(monsterP).quantidade(5).build();

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setItensDoPedido(List.of(pedidoItem1, pedidoItem2, pedidoItem3));

        try (MockedStatic<PedidoItemProdutoDTO> pedidoItemProdutoDTOMock = Mockito.mockStatic(PedidoItemProdutoDTO.class)) {
            when(pedidoRepo.findByDataPedidoGreaterThanEqual(any(LocalDateTime.class))).thenReturn(List.of(pedido));

            pedidoService.pedidosAgrupadoProduto(7);

            ArgumentCaptor<Map<Produto, List<PedidoItem>>> mapProdutoPedidoItem = ArgumentCaptor.forClass(Map.class);
            pedidoItemProdutoDTOMock.verify(() -> PedidoItemProdutoDTO.calcularTotaisItensProduto(mapProdutoPedidoItem.capture()), times(1));

            Map<Produto, List<PedidoItem>> mapaGerado = mapProdutoPedidoItem.getValue();

            assertThat(mapaGerado).hasSize(2);
            assertThat(mapaGerado.get(monsterP)).hasSize(2);
            assertThat(mapaGerado.get(chocolateP)).hasSize(1);
        }
    }

    private PedidoItemDTO criarPedidoItemQuantidade2DoEstoque() {
        return PedidoItemDTO.builder()
                .idProduto(1L)
                .quantidade(2)
                .build();
    }

}
