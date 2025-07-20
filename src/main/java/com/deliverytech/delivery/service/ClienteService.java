package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.ClienteRequest;
import com.deliverytech.delivery.model.Cliente;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviços para gerenciamento de clientes
 * Define todas as operações de negócio relacionadas a clientes
 */
public interface ClienteService {

    /**
     * Cadastrar novo cliente com validações completas
     */
    Cliente cadastrar(Cliente cliente);

    /**
     * Buscar cliente por ID
     */
    Optional<Cliente> buscarPorId(Long id);

    /**
     * Buscar cliente por email
     */
    Optional<Cliente> buscarPorEmail(String email);

    /**
     * Listar todos os clientes ativos
     */
    List<Cliente> listarAtivos();

    /**
     * Buscar clientes por nome (contendo)
     */
    List<Cliente> buscarPorNome(String nome);

    /**
     * Atualizar dados do cliente
     */
    Cliente atualizar(Long id, Cliente clienteAtualizado);

    /**
     * Inativar cliente (soft delete)
     */
    void inativar(Long id);

    Cliente cadastrar(ClienteRequest clienteRequest);
}

