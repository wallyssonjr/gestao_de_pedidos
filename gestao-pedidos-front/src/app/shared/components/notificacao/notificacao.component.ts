import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBarRef} from '@angular/material/snack-bar';

@Component({
  selector: 'app-notificacao',
  standalone: false,
  templateUrl: './notificacao.component.html',
  styleUrls: ['./notificacao.component.scss']
})
export class NotificacaoComponent {
  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: { message: string, type: 'success' | 'error' },
    public snackBarRef: MatSnackBarRef<NotificacaoComponent>
  ) {
  }
}
