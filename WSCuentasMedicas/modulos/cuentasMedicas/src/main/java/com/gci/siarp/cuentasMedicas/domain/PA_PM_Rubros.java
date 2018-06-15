package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class PA_PM_Rubros {
	
	
	private		String		idRubro		;
	private		String		descripcion		;
	private		Integer		comision		;
	private		String		debitoSAP		;
	private		String		creditoSAP		;
	private		String		estadoAUD		;
	private		String		operacionAUD		;
	private		String		usuarioAUD		;
	private		String		maquinaAUD		;
	private		Date		fechaModificacionAUD		;


}
