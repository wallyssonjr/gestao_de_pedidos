import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {PedidoDTO, PedidoService, PedidoItemProdutoDTO} from '../../pedido/pedido.service';
import {FormControl} from '@angular/forms';
import {map, startWith, switchMap} from 'rxjs/operators';

interface ChartData {
  name: string;
  value: number;
}

@Component({
  selector: 'app-dashboard-view',
  standalone: false,
  templateUrl: './dashboard-view.component.html',
  styleUrls: ['./dashboard-view.component.scss']
})
export class DashboardViewComponent implements OnInit {

  periodoControl = new FormControl(7);
  periodoOpcoes = [7, 30, 365];
  pedidos$!: Observable<PedidoDTO[]>;
  pedidosAgrupadosProduto$!: Observable<ChartData[]>;

  constructor(private pedidoService: PedidoService) {
  }

  public formatarPorcentagem = (value: number): string => {
    return `${value.toFixed(2)}`;
  };

  public formatarValor = (value: number): string => {
    return this.formatacaoMoedaBR(value);
  };

  ngOnInit(): void {
    const periodo$ = this.periodoControl.valueChanges.pipe(
      startWith(this.periodoControl.value)
    );

    this.pedidos$ = periodo$.pipe(
      switchMap(dias => this.pedidoService.listarUltimosXDias(dias!))
    );

    this.pedidosAgrupadosProduto$ = periodo$.pipe(
      switchMap(dias => this.pedidoService.listarPedidosAgrupadoPorProduto(dias!)),
      map(vendas => this.preencherGrafico(vendas))
    );
  }

  private preencherGrafico(vendas: PedidoItemProdutoDTO[]): ChartData[] {
    return vendas.map(venda => ({
      name: `${venda.nomeProduto} (${this.formatacaoMoedaBR(venda.valorTotal)})`,
      value: venda.valorTotal
    }));
  }

  private formatacaoMoedaBR(value: number): string {
    return new Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(value);
  }

}
