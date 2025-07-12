package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByRestauranteId(Long restauranteId);
    List<Produto> findByDisponivelTrue();
    List<Produto> findByCategoria(String categoria);
}
