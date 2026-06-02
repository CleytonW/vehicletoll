package com.br.cleytonmelo.vehicletoll.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FaixaHorarioRequestDTO(
        @NotNull(message ="A hora de início é obrigatória")
        @Min(value = 0, message = "A hora de início mínima é 0")
        @Max(value = 24, message= "A hora de início máxima é 24")
        Integer horaInicio,

        @NotNull(message="A hora do fim é obrigatória")
        @Min(value = 0, message = "A hora de início mínima é 0")
        @Max(value = 24, message = "A hora de início máxima é 24")
        Integer horaFim,

        @NotNull(message="O valor é obrigatório")
        @Min(value = 0, message = "O valor não pode ser negativo")
        BigDecimal valorUnitario
        ) {

}
