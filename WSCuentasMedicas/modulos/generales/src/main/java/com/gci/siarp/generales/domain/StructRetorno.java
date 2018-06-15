package com.gci.siarp.generales.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StructRetorno {
	private Integer 		iCodigo;
	private String 			sMensaje;
	private BigDecimal 		valor;
	private String			estado;
}
