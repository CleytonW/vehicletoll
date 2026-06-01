package com.br.cleytonmelo.vehicletoll.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "faixa_horarios")
public class FaixaHorario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer horaInicia;
    private Integer horaFinal;
    private BigDecimal valorUnitario;

    @ManyToOne
    @JoinColumn(name = "categoria_id") //Criando a FK no banco
    private CategoriaVeiculo categoriaVeiculo;
}
