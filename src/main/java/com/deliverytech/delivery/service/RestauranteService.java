package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Restaurante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RestauranteService {

    Restaurante cadastrar(Restaurante restaurante);

    Optional<Restaurante> buscarPorId(Long id);

    List<Restaurante> listarTodos();

    List<Restaurante> listarAtivos();

    List<Restaurante> buscarPorCategoria(String categoria);

    List<Restaurante> buscarPorAvaliacao(BigDecimal minAvaliacao);

    List<Restaurante> buscarPorTaxaEntrega(BigDecimal maxTaxa);

    Restaurante atualizar(Long id, Restaurante restauranteAtualizado);

    void inativar(Long id);
}
