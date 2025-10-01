import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Produto, ProdutoService} from '../produto.service';
import {Router} from '@angular/router';
import {NotificacaoService} from '../../core/services/notificacao.service';
import {ProdutoStoreService} from '../produto-store.service';

@Component({
  selector: 'app-lista-produto',
  standalone: false,
  templateUrl: './lista-produto.component.html',
  styleUrl: './lista-produto.component.scss'
})

export class ListaProdutoComponent implements OnInit {

  colunasProduto: string[] = ['id', 'nome', 'preco', 'quantidade', 'acoes'];
  listaProdutos$: Observable<Produto[]>;

  constructor(
    private produtoService: ProdutoService,
    private router: Router,
    private notificacaoService: NotificacaoService,
    private produtoStoreService: ProdutoStoreService
  ) {
    this.listaProdutos$ = this.produtoStoreService.produtosObs$;
  }

  ngOnInit(): void {
    this.produtoStoreService.carregarProdutos().subscribe();
  }

  novoProduto(): void {
    this.router.navigate(['/produto/novo']);
  }

  editarProduto(id: number | undefined): void {
    if (id) {
      this.router.navigate(['/produto/editar', id]);
    }
  }

  removerProduto(id: number | undefined): void {
    if (id && confirm('Are you sure you want to delete this product?')) {

      this.produtoStoreService.removerProduto(id).subscribe((mensagem) => {
        this.notificacaoService.msgSucesso(mensagem);
      });
    }
  }
}
