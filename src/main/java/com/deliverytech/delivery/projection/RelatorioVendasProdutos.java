package com.deliverytech.delivery.projection;

import java.math.BigDecimal;

public interface RelatorioVendasProdutos {
    Long getIdProduto();
    String getNomeProduto();
    BigDecimal getTotalVendas();
    Long getQuantidadeItemPedido();
}