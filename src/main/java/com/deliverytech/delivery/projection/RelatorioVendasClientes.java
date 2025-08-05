package com.deliverytech.delivery.projection;

import java.math.BigDecimal;

public interface RelatorioVendasClientes {
    Long getIdCliente();
    String getNomeCliente();
    BigDecimal getTotalCompras();
    Long getQuantidadePedidos();
}