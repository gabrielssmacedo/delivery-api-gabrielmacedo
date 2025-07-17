package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente cadastrar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorEmail(String email);

    List<Cliente> listarAtivos();

    List<Cliente> buscarPorNome(String nome);

    Cliente atualizar(Long id, Cliente clienteAtualizado);

    void inativar(Long id);
}

