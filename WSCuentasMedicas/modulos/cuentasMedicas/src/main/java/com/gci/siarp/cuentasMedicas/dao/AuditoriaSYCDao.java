package com.gci.siarp.cuentasMedicas.dao;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.cuentasMedicas.domain.AuditoriaSYC;


@SiarpDatabase
public interface AuditoriaSYCDao {

	/*public void insertarAuditoriaSYC(@Param("idCuenta") Long idCuenta, @Param("fechaAud") Date fechaAud, @Param("ps_archivo") String ps_archivo, @Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina, @Param("idProveedor") String idProveedor, @Param("idFactura") Long idFactura);
*/
	
	public void insertarAuditoriaSYC(AuditoriaSYC auditoriaSYC);

	/* public Glosa consultarGlosa(@Param("idGlosa") Long idGlosa); */
    //public void actualizarAuditoriaSYC(@Param("idCuenta") Long idCuenta, @Param("retorno") String retorno, @Param("auditor") String auditor, @Param("idFactura") Long idFactura, @Param("idAuditoriaSYC") Long idAuditoriaSYC);
	
	public void actualizarAuditoriaSYC(AuditoriaSYC auditoriaSYC);

	public String obtenerEstadoGlosa(@Param("id_cuenta") Long id_cuenta, @Param("id_factura") Long id_factura);
	
}
