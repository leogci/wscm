package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;
import lombok.Data;


@Data
public class Rubro {
	
	private	Long	idCuenta	;
	private	Long	idFactura	;
	private	Integer	consecutivo	;
	private	String	idRubro	;
	private	String	nombreRubro	;
	private	Double	valorRubro	;
	private	Double	valorIva	;
	private	Long	idGlosa	;
	private	String	nombreGlosa	;
	private	Double	valorGlosa	;
	private	Double	valorNC	;
	private	String	observacion	;
	private	Date	fechaModificacionAud	;
	private	String	usuarioAud	;
	private	String	maquinaAud	;
	private	String	operacionAud	;
	private	String	estadoAud	;
	private String  idRubroSAP;

}
