package com.gci.siarp.cuentasMedicas.domain;

import lombok.Data;


@Data
public class CuentaAcuerdo {

	private String idCuenta;
	private String prefijoFactura;
	private String factura;
	private String valorFactura;
	private String idTipoDoc;
	private String idProveedor;
	private String razonSocial;
	private String totalNeto;
	private String totalCredito;
	
}
