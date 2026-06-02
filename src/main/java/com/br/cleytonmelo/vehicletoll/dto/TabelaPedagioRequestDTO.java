package com.br.cleytonmelo.vehicletoll.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record TabelaPedagioRequestDTO(

    @NotBlank(message = "O nome da tabela é obrigatório")
    String nome,
    
    @NotBlank(message = "A data de vigência é obrigatório")
    LocalDate dataVigencia,

    @NotBlank(message = "A tabela deve ter pelo menos uma categoria de veículo")
    List<CategoriaVeiculoRequestDTO> categorias
) {
    
}
