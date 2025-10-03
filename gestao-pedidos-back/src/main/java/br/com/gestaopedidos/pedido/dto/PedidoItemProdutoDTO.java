package br.com.gestaopedidos.pedido.dto;

import br.com.gestaopedidos.pedido.entidade.PedidoItem;
import br.com.gestaopedidos.produto.entidade.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemProdutoDTO {

    private String nomeProduto;
    private BigDecimal precoUnitarioProduto;
    private Integer quantidadeTotal;
    private BigDecimal valorTotal;

    /**
     * Method used to calculate the total value and quantity of products across all sales
     *
     * @param itemsPerProduct Map<Product, List<OrderItem>> For the product key, contains the list of all orders included in it.
     * @return List<OrderItemProductDTO> with all calculated totals.
     */
    @JsonIgnore
    public static List<PedidoItemProdutoDTO> calcularTotaisItensProduto(Map<Produto, List<PedidoItem>> itensPorProduto) {
        return itensPorProduto.entrySet().stream()
                .map(itemProduto -> {
                    Produto produto = itemProduto.getKey();
                    List<PedidoItem> itensDoProduto = itemProduto.getValue();

                    final int quantidadeTotalItemProduto = itensDoProduto.stream()
                            .mapToInt(PedidoItem::getQuantidade)
                            .sum();

                    final BigDecimal valorTotalItemProduto = itensDoProduto.stream()
                            .map(item -> item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new PedidoItemProdutoDTO(
                            produto.getNome(),
                            produto.getPreco(),
                            quantidadeTotalItemProduto,
                            valorTotalItemProduto
                    );
                }).collect(Collectors.toList());
    }

}
