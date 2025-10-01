package br.com.gestaopedidos.produto;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.excecao.NegocioException;
import br.com.gestaopedidos.core.excecao.RecursoNaoEncontradoException;
import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import br.com.gestaopedidos.produto.entidade.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepo produtoRepo;

    private final MensagemService mensagemService;

    public List<ProdutoDTO> listarTodos() {
        return getProdutoMapper().paraDTOList(produtoRepo.findAll());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public ProdutoDTO buscarPorId(Long id) {
        validarIdProdutoValido(id);
        return produtoRepo.findById(id).map(getProdutoMapper()::produtoParaDTO).orElseThrow(() -> new RecursoNaoEncontradoException(mensagemService.getMensagem("geral.erro.1")));
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO productDTO) {
        Produto produtoSalvo;

        if (Objects.nonNull(productDTO.getId())) {
            produtoSalvo = alterarProduto(productDTO);
        } else {
            validarSalvarProduto(productDTO);
            produtoSalvo = produtoRepo.save(getProdutoMapper().dtoParaProduto(productDTO));
        }
        return getProdutoMapper().produtoParaDTO(produtoSalvo);
    }

    @Transactional
    public void remover(Long id) {
        validarIdProdutoValido(id);

        if (!produtoRepo.existsById(id)) {
            throw new RecursoNaoEncontradoException(mensagemService.getMensagem("geral.erro.1"));
        }
        produtoRepo.deleteById(id);
    }

    @Override
    public void removerTodos() {
        produtoRepo.deleteAll();
    }

    private void validarIdProdutoValido(Long id) {
        if (Objects.isNull(id) || id.intValue() <= 0) {
            throw new NegocioException(mensagemService.getMensagem("produto.erro.3"));
        }
    }

    private Produto alterarProduto(ProdutoDTO productDTO) {
        ProdutoDTO produtoAlterarDTO = buscarPorId(productDTO.getId());

        produtoAlterarDTO.setNome(productDTO.getNome());
        produtoAlterarDTO.setDescricao(productDTO.getDescricao());
        produtoAlterarDTO.setPreco(productDTO.getPreco());
        produtoAlterarDTO.setQuantidade(productDTO.getQuantidade());

        return produtoRepo.save(getProdutoMapper().dtoParaProduto(produtoAlterarDTO));
    }

    private void validarSalvarProduto(ProdutoDTO productDTO) {
        if (productDTO.getQuantidade() <= 0) {
            throw new NegocioException(mensagemService.getMensagem("produto.validacao.quantidadePositiva"));
        }
    }
}
