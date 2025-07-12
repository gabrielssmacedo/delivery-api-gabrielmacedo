package com.deliverytech.delivery.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;
}

