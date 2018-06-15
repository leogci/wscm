package com.gci.siarp.cuentasMedicas.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.AuditoriaSYCDao;
import com.gci.siarp.cuentasMedicas.dao.RadicacionSYCDao;
import com.gci.siarp.cuentasMedicas.domain.AuditoriaSYC;


@SiarpService
public class LogSYC {
	
	@Autowired 
	RadicacionSYCDao radicacionSYC;
	@Autowired
	AuditoriaSYCDao auditoriaSYCDao;
	
	public Long consultarConsecutivoRadicacion (Long cuenta, Long factura){
		return radicacionSYC.consultarConsecutivoRadicacion(cuenta,factura);
	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void insertarRadicacionSYS(Long cuenta, Long factura, Long consecutivo, String tipoMov, Integer numFacturas, String visado,String xml, String usuario, String maquina ){
		radicacionSYC.insertarRadicacionSYS(cuenta, factura, consecutivo, tipoMov, numFacturas, visado, xml, usuario, maquina);
	}
	
	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void actualizarSYC(String retorno, Long cuenta, Long factura, Long consecutivo){
		radicacionSYC.actualizarSYC(retorno,cuenta,factura,consecutivo);
	}
	
	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void insertarAuditoriaSYC(AuditoriaSYC auditoriaSYC) {
		auditoriaSYCDao.insertarAuditoriaSYC(auditoriaSYC);
	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void actualizarAuditoriaSYC(AuditoriaSYC auditoriaSYC) {
		auditoriaSYCDao.actualizarAuditoriaSYC(auditoriaSYC);
	}
}
