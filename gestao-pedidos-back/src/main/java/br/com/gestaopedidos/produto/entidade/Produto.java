package br.com.gestaopedidos.produto.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "produto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "produto.validacao.nome")
    @Size(min = 3, max = 100, message = "produto.validacao.nomeTamanho")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 255, message = "produto.validacao.descricaoTamanho")
    private String descricao;

    @NotNull(message = "produto.validacao.preco")
    @Positive(message = "produto.validacao.precoPositivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Min(value = 0, message = "produto.validacao.quantidadePositiva")
    @NotNull(message = "produto.validacao.quantidade")
    @Column(nullable = false)
    private Integer quantidade;

}
