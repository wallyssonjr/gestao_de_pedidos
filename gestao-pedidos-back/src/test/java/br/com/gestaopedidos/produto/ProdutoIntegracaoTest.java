package br.com.gestaopedidos.produto;

import br.com.gestaopedidos.core.util.JsonUtil;
import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import br.com.gestaopedidos.produto.entidade.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
public class ProdutoIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepo produtoRepo;

    @Autowired
    private JsonUtil jsonUtil;

    @BeforeEach
    void configurar() {
        produtoRepo.deleteAll();
    }

    @Test
    void listarTodosProdutos200Test() throws Exception {
        Produto produto = montarProdutoPadrao();

        produtoRepo.save(produto);

        mockMvc.perform(get("/produto/listar-todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].nome", is("Monster")))
                .andExpect(jsonPath("$[0].quantidade", is(50)));
    }

    @Test
    void buscarProdutoPorId200Test() throws Exception {
        Produto produto = montarProdutoPadrao();

        Produto produtoSalvo = produtoRepo.save(produto);

        mockMvc.perform(get("/produto/{id}", produtoSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(produtoSalvo.getId().intValue())))
                .andExpect(jsonPath("$.nome", is("Monster")));
    }

    @Test
    void buscarProdutoPorId404Test() throws Exception {
        mockMvc.perform(get("/produto/{id}", 20L)).andExpect(status().isNotFound());
    }

    @Test
    void salvarNovoProduto200Test() throws Exception {
        ProdutoDTO novoProdutoDTO = montarProdutoDTOPradrao();

        mockMvc.perform(post("/produto/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objetoParaJsonString(novoProdutoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome", is("Monster")));
    }

    @Test
    void removerProduto200Test() throws Exception {
        Produto produto = montarProdutoPadrao();

        Produto novoProduto = produtoRepo.save(produto);

        mockMvc.perform(delete("/produto/{id}", novoProduto.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/produto/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    private Produto montarProdutoPadrao() {
        return Produto.builder()
                .nome("Monster")
                .preco(new BigDecimal("11.90"))
                .descricao("Energético")
                .quantidade(50)
                .build();
    }

    private ProdutoDTO montarProdutoDTOPradrao() {
        return ProdutoDTO.builder()
                .nome("Monster")
                .preco(new BigDecimal("11.90"))
                .descricao("Energético")
                .quantidade(50)
                .build();
    }

}
