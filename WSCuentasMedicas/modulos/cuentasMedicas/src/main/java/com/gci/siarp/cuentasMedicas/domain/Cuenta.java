package com.gci.siarp.cuentasMedicas.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;


@Data
public class Cuenta {

	private Long 		idCuenta;
	private Integer 	idTipoSol;
	private Date 		fechaRadicado;
	private String 		idTipoDOC;
	private String 		idProveedor;
	private Integer		dvEmpresa;
	private String 		tipoIngresoCuenta;
	private Integer 	idSeccional;
	private Integer 	numeroFolios;
	private Integer 	numeroFacturas;
	private BigDecimal 	valorCuenta;	
	private String  	ordenPago;
	private Date 		fechaOP;
	private String 		indEstadoCuenta;
	private String 		CDP;
	private Date 		fechaCDP;
	private Integer 	RP;
	private Date 		fechaRP;
	private Date 		fechaModAud;
	private String 		usuarioAud;
	private String 		maquinaAud;
	private String 		operacionAud;
	private String 		estadoAud;
	private Date		fechaEstado;
	private String		idProfesional;
	private Integer		indReembolso;
	private Long 		idReembolso;
	private String 		tipoSolicitante;
	private String 		anoRadicacion;
	private Integer 	codigoSeccional;
	private Long 		codigoPAT;
	private Long		numeroRadicacion;
	private String 		razonSocial;
	private String 		tipoDocumento;
	private Long 		documento;
	private Integer 	codigoBanco;
	private String 		naturalezaCuenta;
	private String 		nombreBanco;
	private String 		tipoCuenta;
	private String 		numeroCuenta;
	private String 		sucursalBancaria;
	private String 		tipoPersona;
	private String 		direccion;
	private String 		correo;
	private String 		celular;
	private String 		telefono;
		
}
