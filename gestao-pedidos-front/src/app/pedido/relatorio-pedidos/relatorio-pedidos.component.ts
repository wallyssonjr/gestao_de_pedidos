import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PedidoService} from '../pedido.service';
import {NotificacaoService} from '../../core/services/notificacao.service';

@Component({
  selector: 'app-pedido-relatorio-form',
  standalone: false,
  templateUrl: './relatorio-pedidos.component.html',
  styleUrls: ['./relatorio-pedidos.component.scss']
})
export class RelatorioPedidosComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private pedidoService: PedidoService,
    private notificacaoService: NotificacaoService
  ) {
    this.form = this.fb.group({
      dataInicial: [null, Validators.required],
      dataFinal: [null, Validators.required]
    });
  }

  private formatarData(data: Date): string {
    return data.toISOString().split('T')[0];
  }

  gerarRelatorio(tipo: 'simples' | 'agrupado'): void {
    if (this.form.invalid) {
      this.notificacaoService.msgAlerta('Please select a valid date range.');
      return;
    }

    const {dataInicial, dataFinal} = this.form.value;
    const dataInicialFormatada = this.formatarData(dataInicial);
    const dataFinalFormatada = this.formatarData(dataFinal);

    const relatorioServico = tipo === 'simples'
      ? this.pedidoService.gerarRelatorioPedidos(dataInicialFormatada, dataFinalFormatada)
      : this.pedidoService.gerarRelatorioPedidosAgrupado(dataInicialFormatada, dataFinalFormatada);

    relatorioServico.subscribe((resposta) => {
      const url = window.URL.createObjectURL(resposta);
      const a = document.createElement('a');
      a.href = url;
      a.download = `report_orders_${tipo}_${dataInicialFormatada}_a_${dataFinalFormatada}.pdf`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove();
    });
  }
}
