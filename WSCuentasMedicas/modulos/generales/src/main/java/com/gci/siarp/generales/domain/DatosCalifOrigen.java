package com.gci.siarp.generales.domain;

import java.util.Date;
import lombok.Data;

@Data
public class DatosCalifOrigen {
	private Long 		idSiniestro;
	private Long 		idCalifDTO;
	private Integer 	origen;
	private Date 		fechaDictamen;
	private Integer 	origenCalifDTO;
	private Long 		numeroDictamen;
	private String		sustentacion;
	private String		observaciones;
}
