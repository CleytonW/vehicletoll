package com.br.cleytonmelo.vehicletoll.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.br.cleytonmelo.vehicletoll.dto.CategoriaVeiculoRequestDTO;
import com.br.cleytonmelo.vehicletoll.dto.FaixaHorarioRequestDTO;
import com.br.cleytonmelo.vehicletoll.dto.TabelaPedagioRequestDTO;
import com.br.cleytonmelo.vehicletoll.exception.RegraNegocioException;
import com.br.cleytonmelo.vehicletoll.model.CategoriaVeiculo;
import com.br.cleytonmelo.vehicletoll.model.FaixaHorario;
import com.br.cleytonmelo.vehicletoll.model.TabelaPedagio;
import com.br.cleytonmelo.vehicletoll.repository.TabelaPedagioRepository;

import jakarta.transaction.Transactional;

@Service
public class TabelaPedagioService {

    private final TabelaPedagioRepository repository;

    // Injeção de dependência oficial via Construtor
    public TabelaPedagioService(TabelaPedagioRepository repository) {
        this.repository = repository;
    }

    @Transactional// Garante que se uma parte do save falhar, ele faz um "rollback" em tudo e não salva pela metade
    public TabelaPedagio criarTabela(TabelaPedagioRequestDTO request) {

        // Regra de Negócio: Não duplicar tabelas na mesma vigência
        if (repository.existsByDataVigencia(request.dataVigencia())) {
            // Em vez de retornar um erro feio padrão quebrando tela (stacktrace), jogamos uma Exception Mapeada nossa.
            // O tratamento de erro dela faremos no próximo passo.
            throw new RegraNegocioException(
                    "Já exite uma tabela de pedágio com vigência em " + request.dataVigencia()
            );
        }

        // Validação complexa das faixas horárias
        request.categorias().forEach(this::validarFaixasHorarias);

        // Conversão do DTO para a Entidade que vai pro banco de dados
        TabelaPedagio tabela = new TabelaPedagio();
        tabela.setNome(request.nome());
        tabela.setDataVigente(request.dataVigencia());

        List<CategoriaVeiculo> categorias = request.categorias().stream().map(catDTO -> {
            CategoriaVeiculo cat = new CategoriaVeiculo();
            cat.setTipoVeiculo(catDTO.tipoVeiculo());
            cat.setTabelaPedagio(tabela);

            List<FaixaHorario> faixas = catDTO.faixasHorarios().stream().map(faixaDTO -> {
                FaixaHorario f = new FaixaHorario();
                f.setHoraInicio(faixaDTO.horaInicio());
                f.setHoraFinal(faixaDTO.horaFim());
                f.setValorUnitario(faixaDTO.valorUnitario());
                f.setCategoria(cat);
                return f;
            }).toList();

            cat.setFaixasHorarios(faixas); // Set explícito dependendo dos Setters gerados
            return cat;
        }).toList();

        tabela.setCategorias(categorias);
        return repository.save(tabela);
    }

    // Todas as outras lógicas menores, isolamos em métodos "private" para manter a classe legível (Clean Code)
    private void validarFaixasHorarias(CategoriaVeiculoRequestDTO categoria) {

        List<FaixaHorarioRequestDTO> faixas = categoria.faixasHorarios().stream().sorted(Comparator.comparingInt(FaixaHorarioRequestDTO::horaInicio)).toList();

        var tipo = categoria.tipoVeiculo().name();

        // Valida hora inicial 0
        if (faixas.get(0).horaInicio() != 0) {
            throw new RegraNegocioException(
                    "As faixas horárias da categoria " + tipo + " devem iniciar à meia-noite (hora 0)."
            );
        }

        // Valida sobreposicao ou gap
        for (int i = 0; i < faixas.size() - 1; i++) {
            FaixaHorarioRequestDTO atual = faixas.get(i);
            FaixaHorarioRequestDTO proxima = faixas.get(i + 1);

            // Regra forte: se a faixa vai de 0 a 6, a próxima DEVE começar EXATAMENTE em 6.
            if (!atual.horaFim().equals(proxima.horaInicio())) {
                throw new RegraNegocioException(
                        "As faixas horárias da categoria " + tipo + " possuem lacuna ou sobreposição. A faixa que inicia em "
                        + proxima.horaInicio() + " deveria iniciar em " + atual.horaFim() + "."
                );
            }
        }

        // Valida preenchimento até hora 24
        if (faixas.get(faixas.size() - 1).horaFim() != 24) {
            throw new RegraNegocioException(
                "As faixas horárias da categoria " + tipo + " devem cobrir até a hora 24."
            );
        }
    }
}
