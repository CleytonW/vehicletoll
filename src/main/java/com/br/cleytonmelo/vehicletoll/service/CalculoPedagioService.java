package com.br.cleytonmelo.vehicletoll.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.br.cleytonmelo.vehicletoll.dto.CalculoPedagioRequestDTO;
import com.br.cleytonmelo.vehicletoll.dto.CalculoPedagioResponseDTO;
import com.br.cleytonmelo.vehicletoll.exception.RecursoNaoEncontradoException;
import com.br.cleytonmelo.vehicletoll.model.CategoriaVeiculo;
import com.br.cleytonmelo.vehicletoll.model.FaixaHorario;
import com.br.cleytonmelo.vehicletoll.model.TabelaPedagio;
import com.br.cleytonmelo.vehicletoll.repository.TabelaPedagioRepository;

@Service
public class CalculoPedagioService {
    
    private final TabelaPedagioRepository repository;

    public CalculoPedagioService(TabelaPedagioRepository repository) {
        this.repository = repository;
    }

    public CalculoPedagioResponseDTO calcular(CalculoPedagioRequestDTO request) {
        
        // 1. Busca a tabela de pedágio ativa aplicável no dia de hoje
        LocalDate hoje = LocalDate.now();
        TabelaPedagio tabelaAtiva = repository.findFirstByDataVigenciaLessThanEqualOrderByDataVigenciaDesc(hoje).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma tabela de pedágio ativa encontrada."));

        // 2. Busca a categoria do veículo dentro da tabela ativa
        CategoriaVeiculo categoriaEncontrada = tabelaAtiva.getCategorias().stream().filter(cat -> cat.getTipoVeiculo().equals(request.tipoVeiculo())).findFirst().orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma tabela tarifária ativa encontrada para esse tipo de veículo " + request.tipoVeiculo()));

        // 3. Busca a faixa horária onde a hora de passagem se encaixa. 
        // Observação: usamos apenas a "hora", ex: 08:30 pertence à faixa 6h às 9h, pois o 8 está entre 6 e < 9.
        int horaPassagem = request.horarioPassagem().getHour();
        FaixaHorario faixaAplicada = categoriaEncontrada.getFaixasHorarios().stream().filter(faixa -> horaPassagem >= faixa.getHoraInicio() && horaPassagem < faixa.getHoraFinal()).findFirst().orElseThrow(() -> new IllegalStateException("Nenhuma faixa horário correspondente encontrada(falha de integridade da tabela)"));

        // 4. Monta e retorna o DTO como resposta 
        // Dica de negócio: Em cenários reais, as tabelas têm nomes amigáveis em banco para faixas. 
        // Aqui simulamos uma string baseada na hora para entregar o que o desafio pede no JSON.
        String descricao = gerarDescricaoFaixa(faixaAplicada.getHoraInicio(), faixaAplicada.getHoraFinal());

        return new CalculoPedagioResponseDTO(
            request.tipoVeiculo(),
            request.horarioPassagem(),
            new CalculoPedagioResponseDTO.FaixaAplicadaDTO(faixaAplicada.getHoraInicio(), faixaAplicada.getHoraFinal()),
            faixaAplicada.getValorUnitario(),
            descricao
        );

    }

    // Método auxiliar para criar uma descrição textual como "Horário de pico matinal" (Exigência do output do projeto)
    public String gerarDescricaoFaixa(int de, int ate) {
        if (de == 6 && ate <= 10) {
            return "Horário de pico matinal";
        }
        if (de >= 17 && ate <= 20) {
            return "Horário de pico vespertino";
        }
        
        return "Horário normal (" + de + "h às " + ate + "h)";
    }
}
