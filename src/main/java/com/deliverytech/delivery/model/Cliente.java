package main.java.com.deliverytech.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private boolean ativo;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    public void inativar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inativar'");
    }
}