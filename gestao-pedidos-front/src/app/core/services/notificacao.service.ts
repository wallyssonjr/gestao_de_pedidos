import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {NotificacaoComponent} from '../../shared/components/notificacao/notificacao.component';

@Injectable({
  providedIn: 'root'
})
export class NotificacaoService {

  constructor(private snackBar: MatSnackBar) {
  }

  msgSucesso(message: string): void {
    this.snackBar.openFromComponent(NotificacaoComponent, {
      data: {message, type: 'success'},
      duration: 4000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: 'custom-snackbar'
    });
  }

  msgErro(message: string): void {
    this.snackBar.openFromComponent(NotificacaoComponent, {
      data: {message, type: 'error'},
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: 'custom-snackbar'
    });
  }

  msgAlerta(message: string): void {
    this.snackBar.openFromComponent(NotificacaoComponent, {
      data: {message, type: 'alert'},
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: 'custom-snackbar'
    });
  }
}
