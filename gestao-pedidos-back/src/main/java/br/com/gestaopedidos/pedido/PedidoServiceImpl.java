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
import br.com.gestaopedidos.produto.entidade.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepo pedidoRepo;

    private final MensagemService mensagemService;
    private final ProdutoService produtoService;

    public List<PedidoDTO> listarTodos() {
        return getPedidoMapper().listaPedidosParaListaDTO(pedidoRepo.findAll());
    }

    public PedidoDTO buscarPorId(Long id) {
        validarIdPedidoValido(id);
        return pedidoRepo.findById(id).map(getPedidoMapper()::pedidoParaDTO).orElseThrow(() -> new RecursoNaoEncontradoException(mensagemService.getMensagem("geral.erro.1")));
    }

    @Transactional
    public PedidoDTO salvar(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();

        List<PedidoItem> itensDoPedido = montarPedidoItem(pedidoDTO, pedido);
        pedido.setItensDoPedido(itensDoPedido);

        return getPedidoMapper().pedidoParaDTO(pedidoRepo.save(pedido));
    }

    @Override
    public List<PedidoDTO> listarUltimosXDias(Integer dias) {
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(dias);
        return getPedidoMapper().listaPedidosParaListaDTO(pedidoRepo.findByDataPedidoGreaterThanEqual(dataInicio));
    }

    @Override
    public List<PedidoItemProdutoDTO> pedidosAgrupadoProduto(Integer dias) {
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(dias);
        List<Pedido> pedidosNoPeriodo = pedidoRepo.findByDataPedidoGreaterThanEqual(dataInicio);

        List<PedidoItem> pedidosItemNoPeriodo = pedidosNoPeriodo.stream()
                .map(Pedido::getItensDoPedido)
                .flatMap(Collection::stream).toList();

        Map<Produto, List<PedidoItem>> itensPorProduto = pedidosItemNoPeriodo.stream().collect(Collectors.groupingBy(PedidoItem::getProduto));

        return PedidoItemProdutoDTO.calcularTotaisItensProduto(itensPorProduto);
    }

    private List<PedidoItem> montarPedidoItem(PedidoDTO pedidoDTO, Pedido pedido) {
        return pedidoDTO.getItensDoPedido().stream().map(pedidoItemDTO -> {
            Produto produto = ajustarQuantidadeProduto(pedidoItemDTO);
            return PedidoItem.builder().pedido(pedido).produto(produto).quantidade(pedidoItemDTO.getQuantidade()).build();
        }).collect(Collectors.toList());
    }

    private Produto ajustarQuantidadeProduto(PedidoItemDTO pedidoItemDTO) {
        ProdutoDTO produto = produtoService.buscarPorId(pedidoItemDTO.getIdProduto());

        if (produto.getQuantidade() < pedidoItemDTO.getQuantidade()) {
            throw new NegocioException(mensagemService.getMensagem("produto.erro.2", produto.getNome(), produto.getQuantidade()));
        }

        produto.setQuantidade(produto.getQuantidade() - pedidoItemDTO.getQuantidade());
        produtoService.salvar(produto);

        return produtoService.getProdutoMapper().dtoParaProduto(produto);
    }

    private void validarIdPedidoValido(Long id) {
        if (Objects.isNull(id) || id.intValue() <= 0) {
            throw new NegocioException(mensagemService.getMensagem("pedido.erro.1"));
        }
    }
}
