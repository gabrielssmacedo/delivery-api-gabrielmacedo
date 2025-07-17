package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByRestauranteId(Long restauranteId);
    List<Produto> findByDisponivelTrue();
    List<Produto> findByCategoria(String categoria);

    @Query(value = "SELECT p.nome, COUNT(ip.produto_id) as quantidade_vendida " +
            "FROM produto p " +
            "LEFT JOIN item_pedido ip ON p.id = ip.produto_id " +
            "GROUP BY p.id, p.nome " +
            "ORDER BY quantidade_vendida DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> produtosMaisVendidos();
}
