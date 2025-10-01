import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ambiente} from '../../ambiente/ambiente';

export interface SalvarPedidoItemDTO {
  idProduto: number;
  quantidade: number;
}

export interface SalvarPedidoDTO {
  itensDoPedido: SalvarPedidoItemDTO[];
}

export interface PedidoItemDTO {
  idProduto: number;
  nomeProduto: string;
  quantidade: number;
  precoProduto: number;
}

export interface PedidoDTO {
  id: number;
  dataPedido: string;
  precoTotal: number;
  itensDoPedido: PedidoItemDTO[];
}

export interface PedidoResponse {
  id: number;
  precoTotal: number;
}


//renomear para um nome melhor
export interface PedidoItemProdutoDTO {
  nomeProduto: string;
  precoUnitarioProduto: number;
  quantidadeTotal: number;
  valorTotal: number;
}

@Injectable({
  providedIn: 'root'
})


export class PedidoService {
  private apiUrl = `${ambiente.baseUrl}/pedido`;

  constructor(private http: HttpClient) {
  }

  salvar(pedido: SalvarPedidoDTO): Observable<PedidoResponse> {
    return this.http.post<PedidoResponse>(`${this.apiUrl}/salvar`, pedido);
  }

  gerarRelatorioPedidos(dataInicial: string, dataFinal: string): Observable<Blob> {
    const params = new HttpParams()
      .set('dataInicial', dataInicial)
      .set('dataFinal', dataFinal);

    return this.http.get(`${this.apiUrl}/pedidos-relatorio-pdf`, {
      params: params,
      responseType: 'blob'
    });
  }

  gerarRelatorioPedidosAgrupado(dataInicial: string, dataFinal: string): Observable<Blob> {
    const params = new HttpParams()
      .set('dataInicial', dataInicial)
      .set('dataFinal', dataFinal);

    return this.http.get(`${this.apiUrl}/pedidos-produto-relatorio-pdf`, {
      params: params,
      responseType: 'blob'
    });
  }

  listarUltimosXDias(dias: number): Observable<PedidoDTO[]> {
    const params = new HttpParams().set('dias', dias.toString());
    return this.http.get<PedidoDTO[]>(`${this.apiUrl}/ultimos-x-dias`, {params});
  }

  listarPedidosAgrupadoPorProduto(dias: number): Observable<PedidoItemProdutoDTO[]> {
    const params = new HttpParams().set('dias', dias.toString());
    return this.http.get<PedidoItemProdutoDTO[]>(`${this.apiUrl}/pedidos-agrupado-produto`, {params});
  }

}
