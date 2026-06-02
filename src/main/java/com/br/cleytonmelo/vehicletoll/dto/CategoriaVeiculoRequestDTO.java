package com.br.cleytonmelo.vehicletoll.dto;

import java.util.List;

import com.br.cleytonmelo.vehicletoll.model.enums.TipoVeiculo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CategoriaVeiculoRequestDTO(
    
    @NotNull(message = "O tipo do veículo é obrigatório")
    TipoVeiculo tipoVeiculo,

    // A anotação @Valid obriga que o Spring valide as regras (NotNull, Min) do que está DENTRO da lista também.
    @NotEmpty(message = "A categoria deve ter ao menos uma faixa de horário")
    @Valid
    List<FaixaHorarioRequestDTO> faixasHorarios
) {
    
}
