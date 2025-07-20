package com.deliverytech.delivery.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//import com.deliverytech.delivery.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    // Mudança 16/07
    private BigDecimal valorTotal;
    private String numeroPedido;
    private BigDecimal subtotal;
    private String observacoes;


    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private LocalDateTime dataPedido = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @Embedded
    private Endereco enderecoEntrega;
}


 