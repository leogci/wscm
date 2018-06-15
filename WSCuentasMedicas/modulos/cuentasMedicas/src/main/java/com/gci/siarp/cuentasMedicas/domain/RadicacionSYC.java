package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;
import lombok.Data;

@Data
public class RadicacionSYC {
	
	private Long 	idRadicado;
	private Long 	idFactura;
	private Long 	consecutivo;
	private String 	tipoMOV;
	private Date 	fechaMOV;
	private Long 	multiusuario;
	private String 	concurrente;
	private String 	XMLRecibido;   
	private String 	retornoSIARP;
	private Date 	fechaModificacionAUD;
	private String 	usuarioAUD;
	private String 	maquinaAUD;
	private String 	operacionAUD;
	private String 	estadoAUD;

}
