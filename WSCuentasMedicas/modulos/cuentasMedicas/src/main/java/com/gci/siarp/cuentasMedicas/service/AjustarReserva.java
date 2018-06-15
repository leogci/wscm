package com.gci.siarp.cuentasMedicas.service;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.AjustarReservaDao;
import com.gci.siarp.cuentasMedicas.dao.FacturaDao;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.generales.domain.StructRetornos;
import com.gci.siarp.generales.service.ReservasService;
import com.vaadin.server.ServiceException;

@Log4j
@SiarpService
public class AjustarReserva {
		
	@Autowired
	AjustarReservaDao AjustarDAO;
	
	@Autowired 
	ReservasService reservaService;
	
	@Autowired
	FacturaDao fact;
	
	@Transactional(value = SiarpDatabase.transactionManagerBeanName)
	public void Reserva(Factura factura) {
		StructRetornos struct;
		Double valorAntiguo;
		try {
			Long numeroAutorizacion = factura.getNumeroAutorizacion() != null ? Long.valueOf(factura.getNumeroAutorizacion()) : null;
			//Regla creada a partir de FK con autorizaciones
			if(numeroAutorizacion == 0)
				numeroAutorizacion = null;
			reservaService.ajustarAS(factura.getIdFuratFurep(),	factura.getValorBruto(), "AS", numeroAutorizacion, factura.getIdCuenta(), factura.getIdFactura(), "CM", "GCI", "GCI");
			AjustarDAO.actualizarReconRESERVA(factura.getIdCuenta(), factura.getIdFactura());
		} catch (NumberFormatException | ServiceException e) {
			e.printStackTrace();
		}
		/*valorAntiguo = AjustarDAO.consultarAjusteSALDO(factura.getIdCuenta(), factura.getIdFactura(), factura.getIdFuratFurep());
		if (valorAntiguo != null) {
			if (valorAntiguo != factura.getValorBruto()) {
				/*struct = reservaService.ajustarAsistencialCM(factura.getIdFuratFurep(), factura.getIdCuenta(), factura.getIdFactura(), (factura.getValorBruto() - valorAntiguo),
						"GCI", "GCI");
				if (struct.getICodigo() == -1) {
					log.error("Se presento un error Ajustando la Reserva CM.");
					throw new RuntimeException(struct.getSMensaje()); 
				} else {
					AjustarDAO.actualizarReconRESERVA(factura.getIdCuenta(), factura.getIdFactura());
				}
				
			}
		}
			/*else {
		}
			struct = reservaService.ajustarAsistencialCM(factura.getIdFuratFurep(), factura.getIdCuenta(), factura.getIdFactura(), factura.getValorBruto(), "GCI", "GCI");
			if (struct.getICodigo() == -1) {
				log.error("Se presento un error Ajustando la Reserva CM.");
				throw new RuntimeException(struct.getSMensaje());
			} else {
				AjustarDAO.actualizarReconRESERVA(factura.getIdCuenta(), factura.getIdFactura());				
			}
		}*/
	}
}

