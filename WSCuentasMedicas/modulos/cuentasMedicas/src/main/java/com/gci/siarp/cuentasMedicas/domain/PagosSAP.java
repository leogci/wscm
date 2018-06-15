package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class PagosSAP {

	private Long idSiarp;
	private Long idCuenta;
	private Double valorPago;
	private Double valorComision;
	private Date fechaEnvio;
	private Date fechaRespuesta;
	private String tipo;
	private String respuestaSAP;
	private String estadoAUD;
	private String operacionAUD;
	private String usuarioAUD;
	private String maquinaAUD;
	private Date fechaModificacionAUD;
}
