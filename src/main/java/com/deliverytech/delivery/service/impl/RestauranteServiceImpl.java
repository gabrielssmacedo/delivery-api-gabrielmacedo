package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.dto.request.RestauranteRequest; // ✅ ADICIONAR
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // ✅ ADICIONAR
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ ADICIONAR

import java.math.BigDecimal; // ✅ ADICIONAR
import java.util.List;
import java.util.Optional;

@Slf4j // ✅ ADICIONAR
@Service
@Transactional // ✅ ADICIONAR
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;

    @Override
    public Restaurante cadastrar(RestauranteRequest restauranteRequest) { // ✅ MUDAR PARÂMETRO
        log.info("Iniciando cadastro de restaurante: {}", restauranteRequest.getNome());

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteRequest.getNome());
        restaurante.setCategoria(restauranteRequest.getCategoria());
        /*         restaurante.setEndereco(restauranteRequest.getEndereco()); */
        restaurante.setTaxaEntrega(restauranteRequest.getTaxaEntrega());
        restaurante.setTelefone(restauranteRequest.getTelefone());
        /*         restaurante.setEmail(restauranteRequest.getEmail()); */
        restaurante.setTempoEntregaMinutos(restauranteRequest.getTempoEntregaMinutos()); // ✅ ADICIONAR
        restaurante.setAtivo(true);

        Restaurante salvo = restauranteRepository.save(restaurante);
        log.info("Restaurante cadastrado com sucesso - ID: {}", salvo.getId());

        return salvo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    // ✅ IMPLEMENTAR MÉTODO FALTANTE
    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    // ✅ IMPLEMENTAR MÉTODO FALTANTE
    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorAvaliacao(BigDecimal minAvaliacao) {
        return restauranteRepository.findByAvaliacaoGreaterThanEqual(minAvaliacao);
    }

    // ✅ IMPLEMENTAR MÉTODO FALTANTE
    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorTaxaEntrega(BigDecimal maxTaxa) {
        return restauranteRepository.findByTaxaEntregaLessThanEqual(maxTaxa);
    }

    @Override
    public Restaurante atualizar(Long id, RestauranteRequest atualizado) {
        return restauranteRepository.findById(id)
                .map(r -> {
                    r.setNome(atualizado.getNome());
                    r.setTelefone(atualizado.getTelefone());
                    r.setCategoria(atualizado.getCategoria());
                    r.setTaxaEntrega(atualizado.getTaxaEntrega());
                    r.setTempoEntregaMinutos(atualizado.getTempoEntregaMinutos());
                    return restauranteRepository.save(r);
                }).orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
    }

    // ✅ IMPLEMENTAR MÉTODO FALTANTE
    @Override
    public void inativar(Long id) {
        restauranteRepository.findById(id)
                .ifPresentOrElse(
                        restaurante -> {
                            restaurante.setAtivo(false);
                            restauranteRepository.save(restaurante);
                            log.info("Restaurante inativado - ID: {}", id);
                        },
                        () -> {
                            throw new RuntimeException("Restaurante não encontrado - ID: " + id);
                        }
                );
    }
}
