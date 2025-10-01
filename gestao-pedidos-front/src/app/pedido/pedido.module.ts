import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

import {PedidoRoutingModule} from './pedido-routing.module';
import {CadastroPedidoComponent} from './cadastro-pedido/cadastro-pedido.component';
import {PedidoComponent} from './pedido.component';
import {RelatorioPedidosComponent} from './relatorio-pedidos/relatorio-pedidos.component';

import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatTableModule} from '@angular/material/table';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule, MAT_DATE_LOCALE, MAT_DATE_FORMATS} from '@angular/material/core';
import {MatMomentDateModule} from '@angular/material-moment-adapter';

export const DATA_FORMATO_BR = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@NgModule({
  declarations: [
    CadastroPedidoComponent,
    RelatorioPedidosComponent,
    PedidoComponent
  ],
  imports: [
    CommonModule,
    PedidoRoutingModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatAutocompleteModule,
    MatTableModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatMomentDateModule
  ],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'pt-BR'},
    {provide: MAT_DATE_FORMATS, useValue: DATA_FORMATO_BR},
  ]
})
export class PedidoModule {
}
