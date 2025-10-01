package br.com.gestaopedidos.core.excecao;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus httpStatus;

    public GlobalException() {
        super();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GlobalException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GlobalException(Throwable e) {
        super(e);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GlobalException(String message, Throwable e) {
        super(message, e);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GlobalException(HttpStatus status, String message) {
        super(message);
        this.httpStatus = status;
    }

    public GlobalException(HttpStatus status, Throwable e) {
        super(e);
        this.httpStatus = status;
    }

    public GlobalException(HttpStatus status, String message, Throwable e) {
        super(message, e);
        this.httpStatus = status;
    }
}
