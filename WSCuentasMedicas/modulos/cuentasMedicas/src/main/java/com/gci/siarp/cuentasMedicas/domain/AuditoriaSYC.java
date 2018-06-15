package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class AuditoriaSYC {

	
	private	Long	idAuditoria			;
	private	Long	consecutivo			;
	private	Long	idRadicado			;
	private	Date	fechaAuditoria		;
	private	String	xmlRecibido			;
	private	String	retornoSiarp		;
	private	Date	fechaModificacionAud;
	private	String	usuarioAud			;
	private	String	maquinaAud			;
	private	String	operacionAud		;
	private	String	estadoAud			;
	private	String	idProveedor			;
	private	Long	idFactura			;
	private	Long	idAuditoriaSYC		;
	
}