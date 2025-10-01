import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {debounceTime, switchMap, startWith, map} from 'rxjs/operators';

import {ProdutoService, Produto} from '../../produto/produto.service';
import {PedidoService, PedidoDTO, PedidoItemDTO, SalvarPedidoDTO, SalvarPedidoItemDTO} from '../pedido.service';
import {NotificacaoService} from '../../core/services/notificacao.service';
import {ProdutoStoreService} from '../../produto/produto-store.service';

interface ItemCarrinho {
  idProduto: number;
  nomeProduto: string;
  quantidade: number;
  precoUnitario: number;
  estoque: number;
}

@Component({
  selector: 'app-pedido-form',
  standalone: false,
  templateUrl: './cadastro-pedido.component.html',
  styleUrl: './cadastro-pedido.component.scss'
})
export class CadastroPedidoComponent implements OnInit {
  formBuscaProduto: FormGroup;
  produtosFiltrados$!: Observable<Produto[]>;

  itensCarrinho: ItemCarrinho[] = [];
  colunasProdutoCarrinho: string[] = ['nome', 'quantidade', 'preco', 'subtotal', 'acoes'];
  totalCarrinho = 0;

  constructor(
    private fb: FormBuilder,
    private produtoService: ProdutoService,
    private pedidoService: PedidoService,
    private router: Router,
    private notificacaoService: NotificacaoService,
    private produtoStoreService: ProdutoStoreService
  ) {
    this.formBuscaProduto = this.fb.group({
      produtoInput: [null, Validators.required],
      quantidade: [1, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.produtoStoreService.carregarProdutos().subscribe();

    this.produtosFiltrados$ = this.formBuscaProduto.get('produtoInput')!.valueChanges.pipe(
      startWith(''),
      debounceTime(300),
      switchMap(valor => this.produtoStoreService.produtosObs$.pipe(
        map(produtos => this.filtrarProdutos(produtos, valor))
      ))
    );
  }

  displayProduto(produto: Produto): string {
    return produto && produto.nome ? produto.nome : '';
  }

  private filtrarProdutos(produtos: Produto[], valor: string): Produto[] {
    const valorBusca = valor.toLowerCase();
    return produtos.filter(p => p.nome.toLowerCase().includes(valorBusca));
  }

  adicionarAoCarrinho(): void {
    if (this.formBuscaProduto.invalid)
      return;

    const produtoSelecionado: Produto = this.formBuscaProduto.get('produtoInput')?.value;
    const quantidade = this.formBuscaProduto.get('quantidade')?.value;

    if (!produtoSelecionado || !produtoSelecionado.id) {
      this.notificacaoService.msgAlerta('Selecione um produto.');
      return;
    }

    if (quantidade > produtoSelecionado.quantidade) {
      this.notificacaoService.msgAlerta(`Estoque insuficiente. Disponível: ${produtoSelecionado.quantidade}`);
      return;
    }

    const produtoExistente = this.itensCarrinho.find(item => item.idProduto === produtoSelecionado.id);

    if (produtoExistente) {
      produtoExistente.quantidade += quantidade;
    } else {
      this.itensCarrinho.push({
        idProduto: produtoSelecionado.id,
        nomeProduto: produtoSelecionado.nome,
        quantidade: quantidade,
        precoUnitario: produtoSelecionado.preco,
        estoque: produtoSelecionado.quantidade
      });
    }

    this.itensCarrinho = [...this.itensCarrinho];
    this.calcularTotalCarrinho();
    this.formBuscaProduto.reset(
      {quantidade: 1}
    );
  }

  removerItem(idProduto: number): void {
    this.itensCarrinho = this.itensCarrinho.filter(item => item.idProduto !== idProduto);
    this.calcularTotalCarrinho();
  }

  calcularTotalCarrinho(): void {
    this.totalCarrinho = this.itensCarrinho.reduce((total, item) => {
      return total + (item.precoUnitario * item.quantidade);
    }, 0);
  }

  realizarPedido(): void {
    if (this.itensCarrinho.length === 0) {
      this.notificacaoService.msgAlerta('Adicione pelo menos um produto ao pedido.');
      return;
    }

    const itensDoPedido: SalvarPedidoItemDTO[] = this.itensCarrinho.map(item => ({
      idProduto: item.idProduto,
      quantidade: item.quantidade
    }));

    const pedidoDTO: SalvarPedidoDTO = {itensDoPedido};

    this.pedidoService.salvar(pedidoDTO).subscribe((resposta) => {
      this.notificacaoService.msgSucesso(`Pedido #${resposta.id} feito com sucesso!`)
      this.router.navigate(['/dashboard']);
    });
  }
}
