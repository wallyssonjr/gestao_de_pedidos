package br.com.gestaopedidos.core.excecao;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RecursoNaoEncontradoException extends NegocioException {

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public RecursoNaoEncontradoException() {
        super();
    }

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }

    public RecursoNaoEncontradoException(Throwable e) {
        super(e);
    }

    public RecursoNaoEncontradoException(String message, Throwable e) {
        super(message, e);
    }

}