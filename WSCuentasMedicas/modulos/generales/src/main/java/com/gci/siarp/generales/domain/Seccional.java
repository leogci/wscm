package com.gci.siarp.generales.domain;

import lombok.Data;

@Data
public class Seccional {
	private Integer idSeccional;
	private String nombreSeccional;
	private String estado;
	private String direccion;
	private Integer idJunta;
	private String telefonos;
	private Integer idDepartamento;
	private Integer idMunicipio;
	private String codigoSAP;
	private String descripcionSAP;
	private Integer codigoSISE;
	
}
