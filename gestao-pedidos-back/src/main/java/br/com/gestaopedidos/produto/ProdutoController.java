package br.com.gestaopedidos.produto;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.produto.dto.ProdutoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
@RequiredArgsConstructor
@Tag(name = "Produto", description = "Serviços disponíveis para a tabela de Produto.")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final MensagemService mensagemService;

    @GetMapping("/listar-todos")
    @Operation(summary = "Lista todos os produtos.", description = "Lista todos os produtos do sistema sem filtros.")
    public ResponseEntity<List<ProdutoDTO>> listarTodosProdutos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca produto por id.", description = "Retorna os dados do produto buscando pelo id")
    public ResponseEntity<ProdutoDTO> buscarPorIdProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping("/salvar")
    @Operation(summary = "Salvar produto.", description = "Salva um novo ou altera um produto a depender se foi passado um id como parâmetro.")
    public ResponseEntity<ProdutoDTO> salvar(@RequestBody ProdutoDTO productDTO) {
        return ResponseEntity.ok(produtoService.salvar(productDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto.", description = "Deleta um produto por id seguindo a regra que o produto não pode conter relacionamentos.")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        produtoService.remover(id);
        return ResponseEntity.ok(mensagemService.getMensagem("produto.sucesso.1"));
    }

}
