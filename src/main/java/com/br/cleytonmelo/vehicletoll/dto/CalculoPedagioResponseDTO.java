package com.br.cleytonmelo.vehicletoll.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.br.cleytonmelo.vehicletoll.model.enums.TipoVeiculo;

public record CalculoPedagioResponseDTO(
    TipoVeiculo tipoVeiculo,
    LocalTime horarioPassagem,
    FaixaAplicadaDTO faixaAplicadaDTO,
    BigDecimal valorCobrado,
    String descricaoFaixa
) {
    public record FaixaAplicadaDTO(Integer horaInicio, Integer horaFim) {}
}
