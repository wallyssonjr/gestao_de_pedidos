package br.com.gestaopedidos.pedido.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Date time of order", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataPedido;

    @NotEmpty(message = "pedido.validacao.1")
    private List<PedidoItemDTO> itensDoPedido;

    @Schema(description = "Total value of this order", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precoTotal;

    public BigDecimal getPrecoTotal() {
        precoTotal = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(itensDoPedido)) {
            BigDecimal precoTotal = itensDoPedido.stream()
                    .map(item -> Optional.ofNullable(item.getPrecoProduto()).orElse(BigDecimal.ZERO).multiply(new BigDecimal(Optional.ofNullable(item.getQuantidade()).orElse(0))))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            setPrecoTotal(precoTotal);
        }
        return precoTotal;
    }
}
