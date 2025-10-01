import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CadastroPedidoComponent} from './cadastro-pedido/cadastro-pedido.component';
import {RelatorioPedidosComponent} from './relatorio-pedidos/relatorio-pedidos.component';

const routes: Routes = [
  {path: '', redirectTo: 'novo', pathMatch: 'full'},
  {path: 'novo', component: CadastroPedidoComponent},
  {path: 'relatorio', component: RelatorioPedidosComponent}
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PedidoRoutingModule {
}
