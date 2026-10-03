package br.edu.cs.poo.ac.seguro.entidades;
import java.math.BigDecimal;
import java.time.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class Sinistro implements Serializable{
	private String numero;
	private Veiculo veiculo;
	private LocalDateTime dataHoraSinistro;
	private LocalDateTime dataHoraRegistro;
	private String usuarioRegistro;
	private BigDecimal valorSinistro;
	private TipoSinistro tipo;
}
