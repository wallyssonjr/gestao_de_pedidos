import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {from, Observable, throwError} from 'rxjs';
import {catchError, switchMap} from 'rxjs/operators';
import {NotificacaoService} from '../services/notificacao.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private notificacaoService: NotificacaoService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((err: HttpErrorResponse) => {
        let mensagemErro = 'Ocorreu um erro no sistema.';

        if (err.status === 404) {
          if (err.error instanceof Blob && err.error.type === "application/json") {
            return from(this.parseErrorBlob(err.error)).pipe(
              switchMap(errorMessage => {
                this.notificacaoService.msgAlerta(errorMessage);
                const newError = new HttpErrorResponse({
                  ...err,
                  url: err.url || undefined,
                  error: errorMessage
                });
                return throwError(() => newError);
              })
            );
          }
          mensagemErro = err.error.mensagem;
          this.notificacaoService.msgAlerta(mensagemErro);
        } else if (err.status === 400) {
          let erroBody = err.error;
          if (typeof erroBody === 'string') {
            try {
              erroBody = JSON.parse(erroBody);
            } catch (e) {
              mensagemErro = erroBody;
            }
          }
          if (typeof erroBody.mensagem === 'string') {
            mensagemErro = erroBody.mensagem;
          }
          this.notificacaoService.msgErro(mensagemErro);
        } else if (err.error && typeof err.error === 'string') {
          mensagemErro = err.error;
          this.notificacaoService.msgErro(mensagemErro);
        } else if (err.status === 0) {
          mensagemErro = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
          this.notificacaoService.msgErro(mensagemErro);
        } else {
          mensagemErro = `Status ${err.error.status}: ` + err.error.mensagem;
          this.notificacaoService.msgErro(mensagemErro);
        }
        return throwError(() => err);
      })
    );
  }

  private async parseErrorBlob(blob: Blob): Promise<string> {
    const errorText = await blob.text();
    try {
      const errorJson = JSON.parse(errorText);
      return errorJson.mensagem || 'Erro interno do servidor.';
    } catch (e) {
      return errorText;
    }
  }
}
