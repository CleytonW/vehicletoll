package com.br.cleytonmelo.vehicletoll.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.cleytonmelo.vehicletoll.model.TabelaPedagio;

@Repository
public interface TabelaPedagioRepository extends JpaRepository<TabelaPedagio, Long> {    
    
    // No mercado, usamos muito as "Derived Queries" do Spring.
    // O simples fato de nomearmos o método seguindo esse padrão (findBy + NomeDoAtributo) 
    // faz o Spring automaticamente criar o SQL "SELECT * FROM tabelas_pedagio WHERE data_vigencia = ?"
    boolean existsByDataVigencia(LocalDate dataVigente);

    // Custom query que buscaremos depois para calcular o pedágio:
    // "Buscar a tabela cuja data de vigência seja a mais recente, mas não maior que a data atual."
    Optional<TabelaPedagio> findFirstByDataVigenciaLessThanEqualOrderByDataVigenciaDesc(LocalDate dataAtual);
}
