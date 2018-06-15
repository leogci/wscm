package com.gci.siarp.generales.domain;

import java.util.Date;
import lombok.Data;

@Data
public class DetallePagados {
	private Double id_siarp;
	private Date fecha;
	private Double valor;
	private Long orden_pago;
	private Long solicitud_pago;
	private String id_origen_pago;
	private String zzid_cta;
}
