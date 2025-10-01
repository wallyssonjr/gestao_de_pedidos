package br.com.gestaopedidos.produto;

import br.com.gestaopedidos.produto.entidade.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProdutoRepo extends JpaRepository<Produto, Long> {

}
