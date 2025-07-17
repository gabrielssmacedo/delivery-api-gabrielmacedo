package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    Produto cadastrar(Produto produto);
    Optional<Produto> buscarPorId(Long id);
    List<Produto> listarTodos();
    Produto atualizar(Long id, Produto produtoAtualizado);
    void inativar(Long id);


    List<Produto> buscarPorRestaurante(Long restauranteId);
    List<Produto> buscarPorCategoria(String categoria);
    List<Produto> listarDisponiveis();


    void alterarDisponibilidade(Long id, boolean disponivel);
    void validarPreco(BigDecimal preco);
}
