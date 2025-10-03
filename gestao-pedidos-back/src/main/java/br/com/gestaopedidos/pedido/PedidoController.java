package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.excecao.GlobalException;
import br.com.gestaopedidos.core.relatorio.dto.RelatorioDTO;
import br.com.gestaopedidos.pedido.dto.PedidoDTO;
import br.com.gestaopedidos.pedido.dto.PedidoItemProdutoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
@Tag(name = "Pedido", description = "Services available for the Order and OrderItem table.")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRelatorioService pedidoRelatorioService;
    private final MensagemService mensagemService;

    @GetMapping("/listar-todos")
    @Operation(summary = "List all orders.", description = "Lists all system orders without filters.")
    public ResponseEntity<List<PedidoDTO>> listarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search order by id.", description = "Retorna os dados do pedido buscando pelo id")
    public ResponseEntity<PedidoDTO> buscarPorIdPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/salvar")
    @Operation(summary = "Save product.", description = "Place a new order.")
    public ResponseEntity<PedidoDTO> salvar(@RequestBody @Valid PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.salvar(pedidoDTO));
    }

    @GetMapping("/pedidos-relatorio-pdf")
    @Operation(summary = "PDF order report.", description = "Returns a PDF report of orders filtered by period.")
    public ResponseEntity<byte[]> gerarRelatorioPedidoPDF(@RequestParam LocalDate dataInicial, LocalDate dataFinal) {
        try {
            RelatorioDTO relatorioPDF = pedidoRelatorioService.gerarRelatorioPedidos(dataInicial, dataFinal);

            return ResponseEntity.ok().headers(relatorioPDF.getHttpHeader()).body(relatorioPDF.getRelatorioPDF());
        } catch (JRException | IOException e) {
            log.error(mensagemService.getMensagem("pedido.relatorio.erro.1", ExceptionUtils.getStackTrace(e)), e);
            throw new GlobalException("Error generating report", e);
        }
    }

    @GetMapping("/pedidos-produto-relatorio-pdf")
    @Operation(summary = "PDF report of orders grouped by product.", description = "PDF report of orders grouped and summarized by product filtered by period.")
    public ResponseEntity<byte[]> gerarRelatorioPedidosProdutoAgrupadoPDF(@RequestParam LocalDate dataInicial, LocalDate dataFinal) {
        try {
            RelatorioDTO relatorioPDF = pedidoRelatorioService.gerarRelatorioPedidosAgrupadoPorProduto(dataInicial, dataFinal);

            return ResponseEntity.ok().headers(relatorioPDF.getHttpHeader()).body(relatorioPDF.getRelatorioPDF());
        } catch (JRException | IOException e) {
            log.error(mensagemService.getMensagem("pedido.relatorio.erro.1", ExceptionUtils.getStackTrace(e)), e);
            throw new GlobalException("Error generating report", e);
        }
    }

    @GetMapping("/ultimos-x-dias")
    @Operation(summary = "List orders by day filter.", description = "Lists the orders placed in the last X days, where X is the parameter passed.")
    public ResponseEntity<List<PedidoDTO>> listarUltimosXDias(@RequestParam(defaultValue = "7", required = false) Integer dias) {
        return ResponseEntity.ok(pedidoService.listarUltimosXDias(dias));
    }

    @GetMapping("/pedidos-agrupado-produto")
    @Operation(summary = "Lists orders grouped by product with a day filter.", description = "Lists orders grouped and summarized by product made in the last X days, where X is the parameter passed.")
    public ResponseEntity<List<PedidoItemProdutoDTO>> listarPedidosAgrupadoProduto(@RequestParam(defaultValue = "7", required = false) Integer dias) {
        return ResponseEntity.ok(pedidoService.pedidosAgrupadoProduto(dias));
    }

}
