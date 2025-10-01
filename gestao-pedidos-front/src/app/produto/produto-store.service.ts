import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Produto, ProdutoService} from './produto.service';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProdutoStoreService {

  private readonly listaProdutos = new BehaviorSubject<Produto[]>([]);

  readonly produtosObs$: Observable<Produto[]> = this.listaProdutos.asObservable();

  constructor(private produtoService: ProdutoService) {
  }

  carregarProdutos(): Observable<Produto[]> {
    return this.produtoService.listarTodos().pipe(
      tap(produtos => {
        this.listaProdutos.next(produtos);
      })
    );
  }

  adicionarProduto(produto: Produto): Observable<Produto> {
    return this.produtoService.salvar(produto).pipe(
      tap(produtoSalvo => {
        const produtos = this.listaProdutos.getValue();
        this.listaProdutos.next([...produtos, produtoSalvo]);
      })
    );
  }

  atualizarProduto(produto: Produto): Observable<Produto> {
    return this.produtoService.salvar(produto).pipe(
      tap(produtoAtualizado => {
        const produtos = this.listaProdutos.getValue();
        const index = produtos.findIndex(p => p.id === produtoAtualizado.id);
        if (index > -1) {
          produtos[index] = produtoAtualizado;
          this.listaProdutos.next([...produtos]);
        }
      })
    );
  }

  removerProduto(id: number): Observable<any> {
    return this.produtoService.remover(id).pipe(
      tap(() => {
        const produtos = this.listaProdutos.getValue();
        const produtosAtualizado = produtos.filter(p => p.id !== id);
        this.listaProdutos.next(produtosAtualizado);
      })
    );
  }
}
