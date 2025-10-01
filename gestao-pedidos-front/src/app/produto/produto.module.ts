import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ProdutoRoutingModule} from './produto-routing.module';
import {ProdutoComponent} from './produto.component';
import {ListaProdutoComponent} from './lista-produto/lista-produto.component';
import {CadastroProdutoComponent} from './cadastro-produto/cadastro-produto.component';

import {MatTableModule} from '@angular/material/table';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {ReactiveFormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    ProdutoComponent,
    ListaProdutoComponent,
    CadastroProdutoComponent
  ],
  imports: [
    CommonModule,
    ProdutoRoutingModule,
    ReactiveFormsModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule
  ]
})
export class ProdutoModule {
}
