import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProdutoComponent} from './produto.component';
import {ListaProdutoComponent} from './lista-produto/lista-produto.component';
import {CadastroProdutoComponent} from './cadastro-produto/cadastro-produto.component';

const routes: Routes = [
  {path: '', component: ListaProdutoComponent},
  {path: 'novo', component: CadastroProdutoComponent},
  {path: 'editar/:id', component: CadastroProdutoComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProdutoRoutingModule {
}
