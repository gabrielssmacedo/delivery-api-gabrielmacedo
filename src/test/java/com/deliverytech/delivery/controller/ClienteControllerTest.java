package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ClienteRequest;
import com.deliverytech.delivery.dto.response.ClienteResponse;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteRequest clienteRequestDTO;
    private Cliente clienteResponseDTO;

    @BeforeEach
    void setUp() {
        clienteRequestDTO = new ClienteRequest();
        clienteRequestDTO.setNome("João Silva");
        clienteRequestDTO.setTelefone("11999999999");
        clienteRequestDTO.setEmail("joao@email.com");
        clienteRequestDTO.setEndereco("Av Joao da Silva");

        clienteResponseDTO = new Cliente();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setNome(clienteRequestDTO.getNome());
        clienteResponseDTO.setEmail(clienteRequestDTO.getEmail());
    }

    @Test
    @WithMockUser
    void deveCadastrarClienteComSucesso() throws Exception {
        when(clienteService.cadastrar(clienteRequestDTO)).thenReturn(clienteResponseDTO);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nome").value("João Silva"))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser
    void deveBuscarClientePorId() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(clienteResponseDTO));

        mockMvc.perform(get("/api/clientes/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void deveAtualizarClienteComSucesso() throws Exception {
        when(clienteService.atualizar(eq(1L), any(ClienteRequest.class)))
                .thenReturn(clienteResponseDTO);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("João Silva"));
    }
}
