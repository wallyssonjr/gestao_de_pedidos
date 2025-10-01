package br.com.gestaopedidos.pedido.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItemDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String nomeProduto;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precoProduto;

    @NotNull(message = "pedido.validacao.produto")
    private Long idProduto;

    @NotNull(message = "pedido.validacao.quantidade")
    @Positive(message = "pedido.validacao.quantidadePositiva")
    private Integer quantidade;

}
