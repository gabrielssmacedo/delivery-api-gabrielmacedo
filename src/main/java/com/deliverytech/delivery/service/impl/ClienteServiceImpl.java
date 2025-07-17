package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Cadastrar novo cliente com validações completas
     */
    @Override
    public Cliente cadastrar(Cliente cliente) {
        log.info("Iniciando cadastro de cliente: {}", cliente.getEmail());

        // Validar email único
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + cliente.getEmail());
        }

        // Validações de negócio
        validarDadosCliente(cliente);

        // Definir como ativo por padrão
        cliente.setAtivo(true);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        log.info("Cliente cadastrado com sucesso - ID: {}", clienteSalvo.getId());

        return clienteSalvo;
    }

    /**
     * Buscar cliente por ID
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        log.debug("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id);
    }

    /**
     * Buscar cliente por email
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        log.debug("Buscando cliente por email: {}", email);
        return clienteRepository.findByEmail(email);
    }

    /**
     * Listar todos os clientes ativos
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarAtivos() {
        log.debug("Listando clientes ativos");
        return clienteRepository.findByAtivoTrue();
    }

    /**
     * Buscar clientes por nome
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        log.debug("Buscando clientes por nome: {}", nome);
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Atualizar dados do cliente COM TODAS AS VALIDAÇÕES
     */
    @Override
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        log.info("Atualizando cliente ID: {}", id);

        Cliente cliente = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        // Verificar se email não está sendo usado por outro cliente
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + clienteAtualizado.getEmail());
        }

        // Validar dados atualizados
        validarDadosCliente(clienteAtualizado);

        // Atualizar campos
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());

        // Atualizar telefone e endereco (se implementados na entidade)
        try {
            cliente.setTelefone(clienteAtualizado.getTelefone());
        } catch (UnsupportedOperationException e) {
            log.warn("Campo telefone não implementado na entidade Cliente");
        }

        try {
            cliente.setEndereco(clienteAtualizado.getEndereco());
        } catch (UnsupportedOperationException e) {
            log.warn("Campo endereco não implementado na entidade Cliente");
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso - ID: {}", clienteSalvo.getId());

        return clienteSalvo;
    }

    /**
     * Inativar cliente (soft delete) COM VALIDAÇÃO
     */
    @Override
    public void inativar(Long id) {
        log.info("Inativando cliente ID: {}", id);

        Cliente cliente = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        // Verificar se cliente já está inativo
        if (!cliente.getAtivo()) {
            throw new IllegalArgumentException("Cliente já está inativo: " + id);
        }

        // Usar método da entidade se disponível, senão usar setter
        try {
            cliente.inativar();
        } catch (Exception e) {
            cliente.setAtivo(false);
        }

        clienteRepository.save(cliente);
        log.info("Cliente inativado com sucesso - ID: {}", id);
    }

    /**
     * VALIDAÇÕES DE NEGÓCIO COMPLETAS (método privado)
     */
    private void validarDadosCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (cliente.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }

        if (cliente.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome não pode ter mais de 100 caracteres");
        }

        if (!cliente.getEmail().contains("@") || !cliente.getEmail().contains(".")) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }

        if (cliente.getEmail().length() > 150) {
            throw new IllegalArgumentException("Email não pode ter mais de 150 caracteres");
        }

        log.debug("Validações de negócio aprovadas para cliente: {}", cliente.getEmail());
    }
}
