package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoService {


    Pedido criar(Pedido pedido);
    Optional<Pedido> buscarPorId(Long id);


    List<Pedido> buscarPorCliente(Long clienteId);
    List<Pedido> buscarPorRestaurante(Long restauranteId);
    List<Pedido> buscarPorStatus(StatusPedido status);


    Pedido atualizarStatus(Long id, StatusPedido status);
    Pedido confirmar(Long id);
    void cancelar(Long id);


    Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade);

    BigDecimal calcularTotal(Pedido pedido);

    List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

}
