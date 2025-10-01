import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ambiente} from '../../ambiente/ambiente';

export interface Produto {
  id?: number;
  nome: string;
  descricao: string;
  preco: number;
  quantidade: number;
}

@Injectable({providedIn: 'root'})
export class ProdutoService {
  private apiUrl = `${ambiente.baseUrl}/produto`;

  constructor(private http: HttpClient) {
  }

  listarTodos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.apiUrl}/listar-todos`);
  }

  buscarPorId(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/${id}`);
  }

  salvar(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(`${this.apiUrl}/salvar`, produto);
  }

  remover(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {responseType: 'text'});
  }
}
