import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Produto, ProdutoService} from '../produto.service';
import {NotificacaoService} from '../../core/services/notificacao.service';
import {ProdutoStoreService} from '../produto-store.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-product-form',
  standalone: false,
  templateUrl: './cadastro-produto.component.html',
  styleUrls: ['./cadastro-produto.component.scss']
})
export class CadastroProdutoComponent implements OnInit {
  form: FormGroup;
  isEdicao = false;
  private idProduto: number | null = null;

  constructor(
    private fb: FormBuilder,
    private produtoService: ProdutoService,
    private router: Router,
    private route: ActivatedRoute,
    private notificacaoService: NotificacaoService,
    private produtoStoreService: ProdutoStoreService
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      descricao: [''],
      preco: [null, [Validators.required, Validators.min(0.01)]],
      quantidade: [null, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEdicao = true;
        this.idProduto = +id;

        this.produtoService.buscarPorId(this.idProduto).subscribe(produto => {
          this.form.patchValue(produto);
        });
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    const produtoForm: Produto = this.form.value;
    let acaoProduto$: Observable<Produto>;

    if (this.isEdicao) {
      produtoForm.id = this.idProduto!;
      acaoProduto$ = this.produtoStoreService.atualizarProduto(produtoForm);
    } else {
      acaoProduto$ = this.produtoStoreService.adicionarProduto(produtoForm);
    }

    acaoProduto$.subscribe(() => {
      const mensagemSucesso = this.isEdicao ? 'Produto atualizado com sucesso!' : 'Produto criado com sucesso!';
      this.notificacaoService.msgSucesso(mensagemSucesso);
      this.router.navigate(['/produto']);
    });
  }

  onCancel(): void {
    this.router.navigate(['/produto']);
  }
}
