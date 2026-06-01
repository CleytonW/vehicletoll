package com.br.cleytonmelo.vehicletoll.model;

import java.util.ArrayList;
import java.util.List;

import com.br.cleytonmelo.vehicletoll.model.enums.TipoVeiculo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "categoria_veiculos")
public class CategoriaVeiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) //Força o banco a salvar o carro como String e não um número 0 ou 1;
    private TipoVeiculo tipoVeiculo;

    @ManyToOne
    @JoinColumn(name = "tabela_pedagio_id")
    private TabelaPedagio tabelaPedagio;

    // O cascade=ALL salva as faixas automaticamente quando salvamos a categoria
    // O orphanRemoval=true deleta as faixas do banco se removermos elas da lista
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaixaHorario> faixasHorarios = new ArrayList<>();
}
