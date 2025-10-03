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
@Tag(name = "Produto", description = "Services available for the Product table.")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final MensagemService mensagemService;

    @GetMapping("/listar-todos")
    @Operation(summary = "List all products.", description = "Lists all products in the system without filters.")
    public ResponseEntity<List<ProdutoDTO>> listarTodosProdutos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search product by id.", description = "Returns product data by searching for the id")
    public ResponseEntity<ProdutoDTO> buscarPorIdProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping("/salvar")
    @Operation(summary = "Save product.", description = "Saves a new or changes a product depending on whether an id was passed as a parameter.")
    public ResponseEntity<ProdutoDTO> salvar(@RequestBody ProdutoDTO productDTO) {
        return ResponseEntity.ok(produtoService.salvar(productDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product.", description = "Deletes a product by id following the rule that the product cannot contain relationships.")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        produtoService.remover(id);
        return ResponseEntity.ok(mensagemService.getMensagem("produto.sucesso.1"));
    }

}
