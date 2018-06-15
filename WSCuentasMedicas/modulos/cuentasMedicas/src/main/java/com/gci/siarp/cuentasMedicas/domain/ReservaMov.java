package com.gci.siarp.cuentasMedicas.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ReservaMov {
	private Integer nroMovimiento;
	private String tipoMovimiento;
	private Date fechaMovimiento;
	private Double valor;
	private String usuario;
}

