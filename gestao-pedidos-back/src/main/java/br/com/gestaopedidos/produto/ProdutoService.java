package br.com.gestaopedidos.produto;

import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import br.com.gestaopedidos.produto.dto.ProdutoMapper;

import java.util.List;

public interface ProdutoService {

    List<ProdutoDTO> listarTodos();

    ProdutoDTO buscarPorId(Long id);

    ProdutoDTO salvar(ProdutoDTO productDTO);

    void remover(Long id);

    void removerTodos();

    default ProdutoMapper getProdutoMapper() {
        return ProdutoMapper.PRODUTO_MAPPER_INSTANCE;
    }

}
