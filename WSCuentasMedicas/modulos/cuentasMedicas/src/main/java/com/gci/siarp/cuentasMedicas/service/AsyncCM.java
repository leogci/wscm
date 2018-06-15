package com.gci.siarp.cuentasMedicas.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.AjustarReservaDao;
import com.gci.siarp.cuentasMedicas.dao.FacturaDao;
import com.gci.siarp.cuentasMedicas.dao.SAPDao;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.generales.domain.sap.ArchivoAnulado;
import com.gci.siarp.generales.service.SAPService;
import com.gci.siarp.generales.service.SAPService.SAPDirectorio;

@Log4j
@SiarpService
@PropertySource("file:${SIARP_CONF}/WSCM.properties")
public class AsyncCM {

	@Autowired
	CalcularVrCertificado cvr;

	@Autowired
	PagarCuentasMedicas cf;

	@Autowired
	SAPServiceCM SAPServiceCM;

	@Autowired
	AjustarReserva AJ;

	@Autowired
	AjustarReservaDao AjustarDAO;

	@Autowired
	FacturaDao facturaDao;

	@Autowired
	SAPDao SAPDAO;

	@Autowired
	SAPService SAPService;

	Long delay = 30000l;

	@Scheduled(initialDelayString = "${siarp.initialDelay}", fixedDelayString = "${siarp.delayCM}")
	public void valorCertificado() {
		
		if (valorPropiedad("siarp.valorCertificado")) {
			System.out.println("-- Valor Certificado " + LocalDateTime.now() + "--");
			try {
				List<Factura> calcularVR = facturaDao.consultarFacturasVR();
				if (!(calcularVR.isEmpty())) {
					for (Factura factura : calcularVR) {
						try {
							System.out.println("VrCertificado " + factura.getIdFactura() + " " + factura.getIdCuenta());
							cvr.vrCertificado("GCI", "GCI", "I", factura);
						} catch (Exception e) {
							log.error(e.getClass());
						}
					}
				} else {
					System.out.println("Sin registros VRcertificado");
				}
			} catch (Exception e) {
				log.error(e.getClass());
			}
		} else {
			System.out.println("-- Valor Certificado OFF--");
		}
	}

	@Scheduled(initialDelayString = "${siarp.initialDelay}", fixedDelayString = "${siarp.delayCM}")
	public void pagarCuentaSAP() {

		if (valorPropiedad("siarp.pagarCuentaSAP")) {
			System.out.println("-- Pagar SAP " + LocalDateTime.now() + "--");
			try {
				List<Long> listadoCuentasPago = SAPDAO.CuentasPorPagarSAP();
				if (!(listadoCuentasPago.isEmpty())) {
					for (Long cuenta : listadoCuentasPago) {
						try {
							cf.PagarCM(cuenta);
						} catch (Exception e) {
							log.error(e.getClass());
						}
					}
				} else {
					System.out.println("Sin registros Pagar SAP");
				}
			} catch (Exception e) {
				log.error(e.getClass());
			}
			System.out.println("Termina Async Pagar SAP" + LocalDateTime.now());
		} else {
			System.out.println("-- Pagar SAP OFF--");
		}

	}

	@Scheduled(initialDelayString = "${siarp.initialDelay}", fixedDelayString = "${siarp.delaySAP}")
	public void procesarErroresSAP() {

		if (valorPropiedad("siarp.procesarErroresSAP")) {
			System.out.println("-- ERRORES_SAP " + LocalDateTime.now()+ "--");
			try {
				SAPServiceCM.procesarErroresSAP();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getClass());
			}
		} else {

			System.out.println("-- ERRORES_SAP SAP OFF--");
		}
	}

	@Scheduled(initialDelayString = "${siarp.initialDelay}", fixedDelayString = "${siarp.delaySAP}")
	public void procesarAnulacionesSAP() {

		if (valorPropiedad("siarp.procesarAnulacionesSAP")) {
			System.out.println("-- ANULACIONES_SAP" + LocalDateTime.now() + "--");
			try {
				List<ArchivoAnulado> anulados = SAPService.buscarArchivosAnulados("20");
				for (ArchivoAnulado archivo : anulados) {
					try {
						SAPServiceCM.procesarAnuladosSAP(archivo);
						SAPService.moverArchivo(SAPDirectorio.ANULADOS, SAPDirectorio.PROCESADOS_ANULADOS, archivo.getNombre());
					} catch (Exception e) {
						log.error(e.getClass());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getClass());
			}
		} else {
			System.out.println("-- ANULACIONES_SAP OFF--");
		}

	}

	@Scheduled(initialDelayString = "${siarp.initialDelay}", fixedDelayString = "${siarp.delaySAP}")
	public void procesarPagadosSAP() {

		if (valorPropiedad("siarp.procesarPagadosSAP")) {
			System.out.println("-- PAGADOS_SAP " + LocalDateTime.now() + "--");
			try {

				SAPServiceCM.procesarPagadosSAP();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getClass());
			}
		} else {
			System.out.println("-- PAGADOS_SAP OFF --");
		}

	}

	@Scheduled(initialDelayString = "${siarp.initialDelay}", fixedDelayString = "${siarp.delayCM}")
	public void ajustarReserva() { 
		if (valorPropiedad("siarp.ajustarReserva")) {
			System.out.println("-- Ajustar Reserva " + LocalDateTime.now() + "--");
			try {
				List<Factura> listadoAjustar = AjustarDAO.listadoFacturasR();
				if (!(listadoAjustar.isEmpty())) {
					for (Factura factura : listadoAjustar) {
						try {
							System.out.println("Aj Re " + factura.getIdFactura() + " " + factura.getIdCuenta());
							AJ.Reserva(factura);
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getClass());
						}
					}
				} else {
					System.out.println("Sin registros AjustarReserva");
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getClass());
			}
		} else {
			System.out.println("-- Ajustar Reserva OFF --");
		}

	}

	public boolean valorPropiedad(String propiedad) {

		boolean estado = false;

		try {
			String siarpConfDirectory = System.getenv("SIARP_CONF");

			if (siarpConfDirectory == null) {
				throw new RuntimeException("No se pudo establecer el valor de la variable de entorno 'SIARP_CONF'. Verifique que la variable ha sido definida en el sistema operativo.");
			}

			Properties properties = new Properties();
			properties.load(new FileInputStream(siarpConfDirectory + "/WSCM.properties"));
			String siarpLogsDirectory;

			siarpLogsDirectory = properties.getProperty(propiedad);

			estado = Boolean.valueOf(siarpLogsDirectory);

			if (siarpLogsDirectory == null) {
				throw new RuntimeException("No se encontró la propiedad '"+propiedad+"' en el archivo 'WSCM.properties'. Verifique que la propiedad existe en el archivo.");
			}

		} catch (IOException e) {
			log.error("Error cargando el archivo 'WSCM.properties'. Verifique que el archivo existe en el directorio de configuración.", e);
			return false;

		} catch (Exception e) {
			log.error("Problemas al cargar el delay. ", e);
			return false;
		}

		return estado;
	}

}
