package com.deliverytech.delivery.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    @NotBlank
    private String endereco;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal taxaEntrega;

    @NotNull
    @Min(1)
    private Integer tempoEntregaMinutos;

    private String telefone;
    private String email;
}
