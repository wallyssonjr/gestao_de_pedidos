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
@Tag(name = "Pedido", description = "Serviços disponíveis para a tabela de Pedido e PedidoItem.")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRelatorioService pedidoRelatorioService;
    private final MensagemService mensagemService;

    @GetMapping("/listar-todos")
    @Operation(summary = "Lista todos os pedidos.", description = "Lista todos os pedidos do sistema sem filtros.")
    public ResponseEntity<List<PedidoDTO>> listarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca pedido por id.", description = "Retorna os dados do pedido buscando pelo id")
    public ResponseEntity<PedidoDTO> buscarPorIdPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/salvar")
    @Operation(summary = "Salvar produto.", description = "Realiza um novo pedido.")
    public ResponseEntity<PedidoDTO> salvar(@RequestBody @Valid PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.salvar(pedidoDTO));
    }

    @GetMapping("/pedidos-relatorio-pdf")
    @Operation(summary = "Relatório PDF de pedidos.", description = "Retorna um relatório PDF de pedidos filtrados por período.")
    public ResponseEntity<byte[]> gerarRelatorioPedidoPDF(@RequestParam LocalDate dataInicial, LocalDate dataFinal) {
        try {
            RelatorioDTO relatorioPDF = pedidoRelatorioService.gerarRelatorioPedidos(dataInicial, dataFinal);

            return ResponseEntity.ok().headers(relatorioPDF.getHttpHeader()).body(relatorioPDF.getRelatorioPDF());
        } catch (JRException | IOException e) {
            log.error(mensagemService.getMensagem("pedido.relatorio.erro.1", ExceptionUtils.getStackTrace(e)), e);
            throw new GlobalException("Erro ao gerar o relatório", e);
        }
    }

    @GetMapping("/pedidos-produto-relatorio-pdf")
    @Operation(summary = "Relatório PDF de pedidos agrupados por produto.", description = "Relatório PDF de pedidos agrupados e sumarizados por produto filtrados por período.")
    public ResponseEntity<byte[]> gerarRelatorioPedidosProdutoAgrupadoPDF(@RequestParam LocalDate dataInicial, LocalDate dataFinal) {
        try {
            RelatorioDTO relatorioPDF = pedidoRelatorioService.gerarRelatorioPedidosAgrupadoPorProduto(dataInicial, dataFinal);

            return ResponseEntity.ok().headers(relatorioPDF.getHttpHeader()).body(relatorioPDF.getRelatorioPDF());
        } catch (JRException | IOException e) {
            log.error(mensagemService.getMensagem("pedido.relatorio.erro.1", ExceptionUtils.getStackTrace(e)), e);
            throw new GlobalException("Erro ao gerar o relatório", e);
        }
    }

    @GetMapping("/ultimos-x-dias")
    @Operation(summary = "Lista os pedidos por filtro de dias.", description = "Lista os pedidos realizados nos últimos X dias, onde X é o parâmetro passado.")
    public ResponseEntity<List<PedidoDTO>> listarUltimosXDias(@RequestParam(defaultValue = "7", required = false) Integer dias) {
        return ResponseEntity.ok(pedidoService.listarUltimosXDias(dias));
    }

    @GetMapping("/pedidos-agrupado-produto")
    @Operation(summary = "Lista os pedidos agrupados por produto com filtro de dias.", description = "Lista os pedidos agrupados e sumarizados por produto realizados nos últimos X dias, onde X é o parâmetro passado.")
    public ResponseEntity<List<PedidoItemProdutoDTO>> listarPedidosAgrupadoProduto(@RequestParam(defaultValue = "7", required = false) Integer dias) {
        return ResponseEntity.ok(pedidoService.pedidosAgrupadoProduto(dias));
    }

}
