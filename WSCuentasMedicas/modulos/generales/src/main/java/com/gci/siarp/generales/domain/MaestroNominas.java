package com.gci.siarp.generales.domain;

import java.util.Date;
import lombok.Data;

@Data
public class MaestroNominas {
	private Long id_solicitud_it;
	private Integer id_secuencial_it;
	private Long id_cuenta;
	private Integer id_factura;
	private Long id_solicitud_pe;
	private Integer consec;
	private String estado_sap;
	private String id_tipo_doc;
	private String id_persona;
	private Integer nro_nomina;
	private Date fecha_cierre_nomina;
	private Double id_siarp;
}
