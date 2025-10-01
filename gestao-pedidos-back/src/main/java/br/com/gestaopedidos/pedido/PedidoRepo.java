package br.com.gestaopedidos.pedido;

import br.com.gestaopedidos.pedido.entidade.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
interface PedidoRepo extends JpaRepository<Pedido, Long> {

    List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

    List<Pedido> findByDataPedidoGreaterThanEqual(LocalDateTime dataInicio);

}