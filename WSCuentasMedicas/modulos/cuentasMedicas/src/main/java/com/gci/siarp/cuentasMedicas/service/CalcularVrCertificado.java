package com.gci.siarp.cuentasMedicas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.FacturaDao;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.cuentasMedicas.domain.Rubro;

@SiarpService
public class CalcularVrCertificado{

	@Autowired
	FacturaDao facturaDao;
	
	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void vrCertificado(String ps_usuario,String ps_maquina, String ps_operacion,Factura factura){
		
		Double vrPagar, vrGlosado, vrSustentado, vrPagado;
		Long consecutivoP = 1l;
		String rubroComision = facturaDao.valorRubroComision();
		List<Rubro> rubros = facturaDao.buscarRubrosSAP(factura.getIdCuenta(), factura.getIdFactura());
		for (Rubro rubroA : rubros) {
			
			consecutivoP = (facturaDao.registrosPago(factura.getIdCuenta(), factura.getIdFactura()) + 1);
			vrGlosado = facturaDao.sumGlosa(factura.getIdCuenta(), factura.getIdFactura(), rubroA.getIdRubro());
			vrGlosado = vrGlosado == null ? 0.0 : vrGlosado;

			vrSustentado = facturaDao.sumSustentado(factura.getIdCuenta(), factura.getIdFactura(), rubroA.getIdRubro());
			vrSustentado = vrSustentado == null ? 0.0 : vrSustentado;

			vrPagado = facturaDao.sumVRpagado(factura.getIdCuenta(), factura.getIdFactura(), rubroA.getIdRubro());
			vrPagado = (vrPagado == null) ? 0.0 : vrPagado;

			if (rubroA.getIdRubroSAP().equals(rubroComision)) {
					vrPagar = (rubroA.getValorRubro() - vrGlosado + vrSustentado - vrPagado);
				} else {
					vrPagar = (rubroA.getValorRubro() - factura.getNotaCredito() - vrGlosado + vrSustentado - vrPagado);
				}			

			if (vrPagar > 0) {
					facturaDao.insertarPago(factura.getIdCuenta(), factura.getIdFactura(), consecutivoP, vrPagar, ps_usuario, ps_maquina, ps_operacion,rubroA.getIdRubro());				
					facturaDao.actualizarFlagVRcertificado(factura.getIdCuenta(), factura.getIdFactura(),-2);
			}
			else if (vrPagar == 0){
				facturaDao.actualizarFlagVRcertificado(factura.getIdCuenta(), factura.getIdFactura(),-2);
			}
			else{
				facturaDao.actualizarFlagVRcertificado(factura.getIdCuenta(), factura.getIdFactura(),-3);
			}
		}
	}
}

