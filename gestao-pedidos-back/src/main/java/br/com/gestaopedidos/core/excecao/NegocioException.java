package br.com.gestaopedidos.core.excecao;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NegocioException extends IllegalArgumentException {

    private final HttpStatus httpStatus;

    public NegocioException() {
        super();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NegocioException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NegocioException(HttpStatus status, String message) {
        super(message);
        this.httpStatus = status;
    }

    public NegocioException(Throwable e) {
        super(e);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NegocioException(HttpStatus status, Throwable e) {
        super(e);
        this.httpStatus = status;
    }

    public NegocioException(String message, Throwable e) {
        super(message, e);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NegocioException(HttpStatus status, String message, Throwable e) {
        super(message, e);
        this.httpStatus = status;
    }

}