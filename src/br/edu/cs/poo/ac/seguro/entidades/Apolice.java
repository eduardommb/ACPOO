package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Getter
@Setter
public class Apolice implements Serializable{
	private Veiculo veiculo;
	private BigDecimal valorFranquia;
	private BigDecimal valorPremio;
	private BigDecimal valorMaximoSegurado;
	private String numero;
	
	public Apolice(Veiculo veiculo, BigDecimal valorFranquia, BigDecimal valorPremio, BigDecimal valorMaximoSegurado) {                                                             
        this.veiculo = veiculo;                                                                                                                                                     
        this.valorFranquia = valorFranquia;                                                                                                                                         
        this.valorPremio = valorPremio;                                                                                                                                             
        this.valorMaximoSegurado = valorMaximoSegurado;                                                                                                                             
    } 
}

