package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Pago {
	
	private Long   idCuenta;
	private Long   idFactura;
	private String idPago;
	private String idRubro;	
	private Date   fechaPago;
	private Double valorPago;
	private String valorIva;
	private String observacion;
	private Date   fechaModificacionAUD;
	private String usuarioAUD;
	private String maquinaAUD;
	private String operacionAUD;
	private String estadoAUD;
	private Date   fechaEnvio;
	private Date   fechaSolPago;
	private String idEnvio;
	private String estado;
	private String tipoPago;
	private String idPagoSISE;
	private String ReconReserva;
	private String valorPagoSISE;
	private String NROOPSISEANT;
	private String retornoSISE;
	private Long NROOPSISE;
	private String INDProceso;
	private String INDReserva;
	private Date   fechaCalculo;
	private String idSiarp;
	//private String idConsecutivo;
	//private Date   fechaGenerado;
	private Date   fechaProcesado;
	//private Date   fechaAnulado;
	private String codError;
	//private String descError;
	//private String docAnula;
	private String NROOPSAP;
	//private String solpagoSAP;
	private String retornoSAP;
	 
}
