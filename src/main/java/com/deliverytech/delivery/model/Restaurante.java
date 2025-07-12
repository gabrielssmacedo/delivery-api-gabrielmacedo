package com.deliverytech.delivery.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal taxaEntrega;
    private Integer tempoEntregaMinutos;
    private boolean ativo;

    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos;

    @OneToMany(mappedBy = "restaurante")
    private List<Pedido> pedidos;
}
 