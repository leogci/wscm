package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Proveedor {
	

	private		String		idTipoDoc	;
	private		String		idProveedor	;
	private		Integer		idDepartamento	;
	private		Integer		idMunicipio	;
	private		Integer		idTipoSol	;
	private		Integer		dvEmpresa	;
	private		String		razonSocial	;
	private		String		direccionProveedor	;
	private		String		telefonoProveedor	;
	private		String		faxProveedor	;
	private		String		emailProveedor	;
	private		String		indZona	;
	private		Date		fechaIngreso	;
	private		String		representanteLegal	;
	private		String		tipoCuentaBancaria	;
	private		String		cuentaBancaria	;
	private		String		oficinaBanco	;
	private		Integer		idBanco	;
	private		Date		fechaModificacionAUD	;
	private		String		usuarioAUD	;
	private		String		maquinaAUD	;
	private		String		operacionAUD	;
	private		String		estadoAUD	;
	private		String		tipoProveedor	;
	private		String		codigoPrestador	;
	private		String		apellido1	;
	private		String		apellido2	;
	private		Double		porcentajePago	;
	private		String		nombreSISE	;
	private		String		apellido1SISE	;
	private		String		apellido2SISE	;
	private		Integer		prioridad	;
	private		String		servicios	;
	private		Integer		codTipoMedioPago	;
	private		Integer		codTipoDir	;
	private		Integer		codTipoTelef	;
	private		String		xmlSISE	;
	private		String		xmlRetornoSISE	;
	private		Integer		terceroSISE	;
	private		Integer		indCalidad	;
	private		Integer		indAliado	;
	private		Integer		indDsctoCond	;
	private 	String 		tipoPersona;

}


