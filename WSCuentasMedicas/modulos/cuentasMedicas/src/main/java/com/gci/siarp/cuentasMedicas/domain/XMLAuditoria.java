package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;


@Data
public class XMLAuditoria {
	
	private	String		cod_prestador	;
	private	String		id_tipo_doc_prov	;
	private	String		id_proveedor	;
	private	String		prefijo_factura	;
	private	String		factura	;
	private	String		fecha_servicio	;
	private	String		fecha_factura	;
	private	String		numero_autorizacion	;
	private	String		fecha_autorizacion	;
	private	String		id_tipo_doc_emp	;
	private	String		id_empresa	;
	private	String		id_tipo_doc	;
	private	String		id_persona	;
	private	String		fecha_atep	;
	private	String		evento	;
	private	String		id_dx	;
	private	Integer		valor_neto	;
	private	Integer		valor_iva	;
	private	Integer		valor_nc	;
	private	String		id_rubro	;
	private	String		id_siniestro	;
	private	Long		id_cuenta	;
	private	Long		id_factura	;
	private	String		cod_stikker	;
	private	String		cod_departamento	;
	private	String		cod_municipio	;
	private	String		fecha_radicacion_factura	;
	private	Double		valor_aprobado	;
	private	Double		valor_glosado	;
	private	Integer		codigo_glosa	;
	private	String		ind_glosa	;
	private	Integer		cod_respuesta	;
	private	Double		valor_sustentado	;
	private	Date		fecha_auditoria	;
	private	Date		fecha_respuesta	;
	private	String		firma_auditora	;
	private String 		estado_auditoria;


}