package com.gci.siarp.generales.domain;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CalculoPension {
	private BigDecimal 	valor;
	private BigDecimal 	mesada;
	private Integer 	mesadas;
	private String		mensaje;
}
