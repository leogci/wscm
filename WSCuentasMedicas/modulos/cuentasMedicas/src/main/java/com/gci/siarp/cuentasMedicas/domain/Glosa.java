package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Glosa {
	
	private	Long	idCuenta	;
	private	Long	idFactura	;
	private	Integer	consecutivo	;
	private	Integer	idGlosa	;
	private	String	idRubro	;
	private	Date	fechaGlosa	;
	private	Double	valorGlosa	;
	private	String	observacion	;
	private	String	estado	;
	private	String	tipoGlosa	;
	private	Integer		tipoRespuesta	;
	private	Date	fechaRespuesta	;
	private	Double	valorSustentado	;
	private	String	observacionRespuesta	;
	private	Date	fechaModificacionAud	;
	private	String	usuarioAud	;
	private	String	maquinaAud	;
	private	String	operacionAud	;
	private	String	estadoAud	;
	private	Long	idSiarp	;
	private	Long	idSolglosa	;
	private	String	retornoSISE	;
	private	Long	idSiarpLev	;
	private	Long	idSolglosaLev	;
	private	String	retornoSISELev	;
	private	Long	indProceso	;


}
