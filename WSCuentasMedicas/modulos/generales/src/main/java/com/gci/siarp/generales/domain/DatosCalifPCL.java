package com.gci.siarp.generales.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DatosCalifPCL {
	private Long idSiniestro;
	private Long idCalifPCL;
	private BigDecimal PCL;
	private Date fechaDictamen;
	private Date fechaEstructura;
	private String observacion; 
}
