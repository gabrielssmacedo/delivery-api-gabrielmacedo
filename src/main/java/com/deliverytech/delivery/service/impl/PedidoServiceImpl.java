package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public Pedido criar(Pedido pedido) {
        // ✅ CORRIGIDO: Usar status do gabarito
        pedido.setStatus(StatusPedido.PENDENTE);  // ← Era CRIADO, agora PENDENTE
        pedido.setDataPedido(LocalDateTime.now());

        // ✅ ADICIONADO: Calcular valor total
        BigDecimal valorTotal = calcularTotal(pedido);
        pedido.setValorTotal(valorTotal);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> buscarPorCliente(Long clienteId) {
        // ✅ CORRIGIDO: Removida duplicação, mantido apenas este método
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Pedido> buscarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        // ✅ ADICIONADO: Método obrigatório
        return pedidoRepository.findByStatus(status);
    }

    @Override
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        // ✅ ADICIONADO: Método obrigatório
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido status) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    // ✅ ADICIONADO: Validação de transição de status
                    validarTransicaoStatus(pedido.getStatus(), status);
                    pedido.setStatus(status);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Override
    public Pedido confirmar(Long id) {
        // ✅ ADICIONADO: Método obrigatório para endpoint PUT /pedidos/{id}/confirmar
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (pedido.getStatus() != StatusPedido.PENDENTE) {
                        throw new RuntimeException("Apenas pedidos PENDENTES podem ser confirmados");
                    }
                    pedido.setStatus(StatusPedido.CONFIRMADO);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Override
    public void cancelar(Long id) {
        pedidoRepository.findById(id).ifPresentOrElse(pedido -> {
            // ✅ MELHORADO: Validação antes de cancelar
            if (pedido.getStatus() == StatusPedido.ENTREGUE) {
                throw new RuntimeException("Não é possível cancelar pedido já entregue");
            }
            pedido.setStatus(StatusPedido.CANCELADO);
            pedidoRepository.save(pedido);
        }, () -> {
            throw new RuntimeException("Pedido não encontrado");
        });
    }

    @Override
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        // ✅ ADICIONADO: Método obrigatório para POST /pedidos/{id}/itens
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new RuntimeException("Só é possível adicionar itens em pedidos PENDENTES");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.isDisponivel()) {
            throw new RuntimeException("Produto não está disponível");
        }

        // Criar novo item do pedido
        ItemPedido item = ItemPedido.builder()
                .pedido(pedido)
                .produto(produto)
                .quantidade(quantidade)
                .precoUnitario(produto.getPreco())
                .subtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)))
                .build();

        pedido.getItens().add(item);

        // Recalcular valor total
        BigDecimal novoTotal = calcularTotal(pedido);
        pedido.setValorTotal(novoTotal);

        return pedidoRepository.save(pedido);
    }

    @Override
    public BigDecimal calcularTotal(Pedido pedido) {
        // ✅ ADICIONADO: Método obrigatório para cálculo de valores
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return pedido.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ ADICIONADO: Método auxiliar para validações
    private void validarTransicaoStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Regras de negócio para transição de status
        switch (statusAtual) {
            case PENDENTE:
                if (novoStatus != StatusPedido.CONFIRMADO && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Status inválido para pedido PENDENTE");
                }
                break;
            case CONFIRMADO:
                if (novoStatus != StatusPedido.PREPARANDO && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Status inválido para pedido CONFIRMADO");
                }
                break;
            case PREPARANDO:
                if (novoStatus != StatusPedido.SAIU_PARA_ENTREGA && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Status inválido para pedido PREPARANDO");
                }
                break;
            case SAIU_PARA_ENTREGA:
                if (novoStatus != StatusPedido.ENTREGUE) {
                    throw new RuntimeException("Status inválido para pedido SAIU_PARA_ENTREGA");
                }
                break;
            case ENTREGUE:
            case CANCELADO:
                throw new RuntimeException("Não é possível alterar status de pedido " + statusAtual);
            case CRIADO:
                break;
            default:
                break;
        }
    }
}