package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.pedido.dto.PedidoDTO;
import br.com.gestaopedidos.pedido.dto.PedidoItemProdutoDTO;
import br.com.gestaopedidos.pedido.dto.PedidoMapper;

import java.util.List;

public interface PedidoService {

    List<PedidoDTO> listarTodos();

    PedidoDTO buscarPorId(Long id);

    PedidoDTO salvar(PedidoDTO pedidoDTO);

    List<PedidoDTO> listarUltimosXDias(Integer dias);

    List<PedidoItemProdutoDTO> pedidosAgrupadoProduto(Integer dias);

    default PedidoMapper getPedidoMapper() {
        return PedidoMapper.PEDIDO_MAPPER_INSTANCE;
    }
}
