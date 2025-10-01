package br.com.gestaopedidos.produto.dto;

import br.com.gestaopedidos.produto.entidade.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper PRODUTO_MAPPER_INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    ProdutoDTO produtoParaDTO(Produto product);
    Produto dtoParaProduto(ProdutoDTO productDTO);
    List<ProdutoDTO> paraDTOList(List<Produto> products);

}
