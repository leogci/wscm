package com.gci.siarp.generales.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ParametroGen {
	private Integer idParamtro;
	private String descripcion;
	private String tipo;
	private String cadena;
	private Integer entero;
	private Date fecha;
	private String estadoAud;

}
