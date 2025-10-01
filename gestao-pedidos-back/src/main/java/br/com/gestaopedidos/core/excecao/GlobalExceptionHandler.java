package br.com.gestaopedidos.core.excecao;

import br.com.gestaopedidos.core.AmbienteService;
import br.com.gestaopedidos.core.MensagemService;
import br.com.gestaopedidos.core.util.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final JsonUtil jsonUtil;

    private final MensagemService mensagemService;
    private final AmbienteService ambienteService;

    @Autowired(required = false)
    private ErrorEventPublisher errorEventPublisher;


    private final String CHAVE_STATUS_MAP = "status";
    private final String VALOR_MENSAGEM_MAP = "mensagem";
    private final String VALOR_MENSAGENS_MAP = "mensagens";

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<?> handleRuntimeException(RuntimeException e, WebRequest w) {
        Map<String, String> nodes = Map.of(
                VALOR_MENSAGEM_MAP, mensagemService.getMensagem("exception.erro.1", ExceptionUtils.getStackTrace(e)),
                CHAVE_STATUS_MAP, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        String response = jsonUtil.converterParaJsonString(nodes);

        log.error(response, e);
        publishGeneralErrorEvent(response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e, WebRequest w) {
        Map<String, String> errosMap = new HashMap<>();

        e.getConstraintViolations().forEach(
                erro -> errosMap.put(erro.getPropertyPath().toString(), mensagemService.getMensagem(erro.getMessage()))
        );

        ObjectNode arrayNode = jsonUtil.criarArrayNode(VALOR_MENSAGENS_MAP, Stream.of(errosMap).toArray());
        ObjectNode node = jsonUtil.criarNode(CHAVE_STATUS_MAP, String.valueOf(HttpStatus.BAD_REQUEST.value()));

        String response = jsonUtil.converterParaJsonString(arrayNode, node);
        log.info(response, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {NegocioException.class, RecursoNaoEncontradoException.class})
    protected ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest w) {
        Map<String, String> nodes = Map.of(
                VALOR_MENSAGEM_MAP, e.getMessage(),
                CHAVE_STATUS_MAP, String.valueOf(e.getHttpStatus().value())
        );

        String response = jsonUtil.converterParaJsonString(nodes);
        log.info(response, e);

        return ResponseEntity.status(e.getHttpStatus().value()).body(response);
    }

    @ExceptionHandler(value = {org.springframework.dao.DataIntegrityViolationException.class})
    protected ResponseEntity<?> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e, WebRequest w) {
        Map<String, String> nodes = Map.of(
                VALOR_MENSAGEM_MAP, mensagemService.getMensagem("dataIntegrityViolationException"),
                CHAVE_STATUS_MAP, String.valueOf(HttpStatus.BAD_REQUEST.value())
        );

        String response = jsonUtil.converterParaJsonString(nodes);
        log.info(response, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<?> handleNegocioException(NoSuchElementException e, WebRequest w) {
        Map<String, String> nodes = Map.of(
                VALOR_MENSAGEM_MAP, e.getMessage(),
                CHAVE_STATUS_MAP, String.valueOf(HttpStatus.BAD_REQUEST.value())
        );

        String response = jsonUtil.converterParaJsonString(nodes);
        log.info(response, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(value = {GlobalException.class})
    protected ResponseEntity<?> handleGlobalException(GlobalException e, WebRequest w) {
        Map<String, String> nodes = Map.of(
                VALOR_MENSAGEM_MAP, mensagemService.getMensagem("exception.erro.2", ExceptionUtils.getStackTrace(e)),
                CHAVE_STATUS_MAP, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );

        String response = jsonUtil.converterParaJsonString(nodes);
        log.error(response, e);
        publishGeneralErrorEvent(response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private void publishGeneralErrorEvent(String jsonError) {
        if (ambienteService.isProfileDev() && Objects.nonNull(errorEventPublisher)) {
            errorEventPublisher.publishErrorEvent(jsonError);
        }
    }
}
