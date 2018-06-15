package com.gci.siarp.generales.domain;

import lombok.Data;

@Data
public class DetalleAnulados {
	private Double id_siarp;
	private String doc_sap;
	private String motivo_anulacion;
	private String desc_anulacion;
	private String zzid_cta;
}
