package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Acuerdo {

	private Integer idAcuerdo;
	private String idTipoDoc;
	private String idProveedor;
	private Date fechaAcuerdo;
	private Long valorAcuerdo;
	private Long saldoAcuerdo;
	private String observacion;
	private String estado;
	private String estadoAUD;
	private Date fechaModificacionAUD;
	private String maquinaAUD;
	private String usuarioAUD;
	private String operacionAUD;

}
