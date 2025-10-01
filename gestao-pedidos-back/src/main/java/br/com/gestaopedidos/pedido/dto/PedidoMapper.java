package br.com.gestaopedidos.pedido.dto;

import br.com.gestaopedidos.pedido.entidade.Pedido;
import br.com.gestaopedidos.pedido.entidade.PedidoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PedidoMapper {

    PedidoMapper PEDIDO_MAPPER_INSTANCE = Mappers.getMapper(PedidoMapper.class);

    PedidoDTO pedidoParaDTO(Pedido pedido);

    @Mapping(source = "produto.nome", target = "nomeProduto")
    @Mapping(source = "produto.preco", target = "precoProduto")
    @Mapping(source = "produto.id", target = "idProduto")
    PedidoItemDTO pedidoItemParaDTO(PedidoItem pedidoItem);

    List<PedidoDTO> listaPedidosParaListaDTO(List<Pedido> pedidoList);
}
