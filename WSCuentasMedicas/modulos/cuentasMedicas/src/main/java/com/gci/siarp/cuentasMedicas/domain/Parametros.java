package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Parametros {

	private String 	tipo;
	private Long 	consecutivo;
	private String 	descripcion;
	private String 	valor;
	private Date 	fechaModificacionAUD;
	private String 	usuarioAUD;
	private String 	maquinaAUD;
	private String 	operacionAUD;
	private String 	estadoAUD;
	private String 	valor2;
	private String 	indVisible;
	
	
}
