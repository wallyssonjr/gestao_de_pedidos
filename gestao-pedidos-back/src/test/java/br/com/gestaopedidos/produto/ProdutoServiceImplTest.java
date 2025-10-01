package br.com.gestaopedidos.produto;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.excecao.NegocioException;
import br.com.gestaopedidos.core.excecao.RecursoNaoEncontradoException;
import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import br.com.gestaopedidos.produto.entidade.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepo produtoRepo;

    @Mock
    private MensagemService mensagemService;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private Produto produto;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void configurar() {
        criarProduto();
        criarProdutoDTO();
    }

    private void criarProdutoDTO() {
        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Monster");
        produtoDTO.setDescricao("Bebida energético");
        produtoDTO.setPreco(new BigDecimal("11.90"));
        produtoDTO.setQuantidade(10);
    }

    private void criarProduto() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Monster");
        produto.setDescricao("Bebida energético");
        produto.setPreco(new BigDecimal("11.90"));
        produto.setQuantidade(10);
    }

    @Test
    void lostarTodosProdutosDTOSucessoTest() {
        when(produtoRepo.findAll()).thenReturn(List.of(produto));

        List<ProdutoDTO> resultado = produtoService.listarTodos();

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(1);
        assertThat(resultado.get(0).getId()).isEqualTo(produto.getId());
        assertThat(resultado.get(0).getNome()).isEqualTo(produto.getNome());
        verify(produtoRepo, times(1)).findAll();
    }

    @Test
    void buscarProdutoPorIdDTOSucessoTest() {
        when(produtoRepo.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO resultado = produtoService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(produtoRepo, times(1)).findById(1L);
    }

    @Test
    void buscarProdutoPorIdDTONaoEncontradoTest() {
        final long idProdutoInexistente = 20L;

        when(produtoRepo.findById(idProdutoInexistente)).thenReturn(Optional.empty());
        when(mensagemService.getMensagem("geral.erro.1")).thenReturn("Recurso não encontrado.");

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            produtoService.buscarPorId(idProdutoInexistente);
        });
        verify(produtoRepo, times(1)).findById(idProdutoInexistente);
    }

    @Test
    void buscarProdutoPorIdDTOValidacaoTest() {
        when(mensagemService.getMensagem("produto.erro.3")).thenReturn("ID de produto inválido.");

        assertThrows(NegocioException.class, () -> {
            produtoService.buscarPorId(0L);
        });
        verify(produtoRepo, never()).findById(anyLong());
    }

    @Test
    void salvarNovoProdutoSucessoTest() {
        produtoDTO.setId(null);
        when(produtoRepo.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO novoProdutoDTO = produtoService.salvar(produtoDTO);

        assertThat(novoProdutoDTO).isNotNull();
        assertThat(novoProdutoDTO.getId()).isEqualTo(produto.getId());
        verify(produtoRepo, times(1)).save(any(Produto.class));
        verify(produtoRepo, never()).findById(anyLong());
    }

    @Test
    void salvarProdutoExistenteSucessoTest() {
        ProdutoDTO produtoExistenteDTO = new ProdutoDTO();
        produtoExistenteDTO.setId(1L);
        produtoExistenteDTO.setNome("RedBull");
        produtoExistenteDTO.setPreco(new BigDecimal("15.00"));

        when(produtoRepo.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepo.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoDTO produtoDTOAlterado = produtoService.salvar(produtoExistenteDTO);

        assertThat(produtoDTOAlterado).isNotNull();
        assertThat(produtoDTOAlterado.getNome()).isEqualTo("RedBull");
        assertThat(produtoDTOAlterado.getPreco()).isEqualTo(new BigDecimal("15.00"));
        verify(produtoRepo, times(1)).findById(1L);
        verify(produtoRepo, times(1)).save(any(Produto.class));
    }

    @Test
    void removerProdutoSucessoTest() {
        when(produtoRepo.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepo).deleteById(1L);

        produtoService.remover(1L);

        verify(produtoRepo, times(1)).existsById(1L);
        verify(produtoRepo, times(1)).deleteById(1L);
    }

    @Test
    void removerProdutoNaoEncontradoTest() {
        final long idProdutoInexistente = 20L;

        when(produtoRepo.existsById(idProdutoInexistente)).thenReturn(false);
        when(mensagemService.getMensagem("geral.erro.1")).thenReturn("Recurso não encontrado.");

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            produtoService.remover(idProdutoInexistente);
        });
        verify(produtoRepo, times(1)).existsById(idProdutoInexistente);
        verify(produtoRepo, never()).deleteById(anyLong());
    }

    @Test
    void removerProdutoPorIdValidacaoTest() {
        when(mensagemService.getMensagem("produto.erro.3")).thenReturn("ID de produto inválido.");

        assertThrows(NegocioException.class, () -> {
            produtoService.buscarPorId(0L);
        });
        verify(produtoRepo, never()).deleteById(anyLong());
    }

    @Test
    void salvarNovoProdutoValidacaoTest() {
        produtoDTO.setId(null);
        produtoDTO.setQuantidade(0);

        when(mensagemService.getMensagem("produto.validacao.quantidadePositiva")).thenReturn("Quantidade no estoque deve ser maior que zero");

        assertThrows(NegocioException.class, () -> {
            produtoService.salvar(produtoDTO);
        });
        verify(produtoRepo, never()).save(any());
    }

}
