package com.br.cleytonmelo.vehicletoll.dto;

import java.time.LocalTime;

import com.br.cleytonmelo.vehicletoll.model.enums.TipoVeiculo;

import jakarta.validation.constraints.NotBlank;

public record CalculoPedagioRequestDTO(

    @NotBlank(message = "O tipo do veículo é obrigatório")
    TipoVeiculo tipoVeiculo,

    @NotBlank(message = "O horário da passagem é obrigatório")
    LocalTime horarioPassagem
) {
    
}
