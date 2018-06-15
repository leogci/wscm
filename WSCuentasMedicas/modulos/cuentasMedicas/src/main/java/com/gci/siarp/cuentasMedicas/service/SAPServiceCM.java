package com.gci.siarp.cuentasMedicas.service;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.SAPDao;
import com.gci.siarp.generales.domain.sap.ArchivoAnulado;
import com.gci.siarp.generales.domain.sap.ArchivoAnulado.CamposAnulado;
import com.gci.siarp.generales.domain.sap.ArchivoError;
import com.gci.siarp.generales.domain.sap.ArchivoError.CamposError;
import com.gci.siarp.generales.domain.sap.ArchivoPago;
import com.gci.siarp.generales.domain.sap.ArchivoPago.CamposPago;
import com.gci.siarp.generales.domain.sap.ArchivoPagoDetalle;
import com.gci.siarp.generales.domain.sap.ArchivoPagoDetalle.CamposPagoDetalle;
import com.gci.siarp.generales.domain.sap.ArchivoSAPF;
import com.gci.siarp.generales.service.SAPService;
import com.gci.siarp.generales.service.SAPService.SAPDirectorio;

@Log4j
@SiarpService
public class SAPServiceCM {

	@Autowired
	SAPService SAPService;

	@Autowired
	SAPDao SAPDao;

	DateTimeFormatter zonaHoraria = DateTimeFormatter.ofPattern("yyyy-LL-d h:mm:ss");

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void procesarAnuladosSAP(ArchivoAnulado archivo){
		String retornoSap = "\n" + (ZonedDateTime.now().format(zonaHoraria));
		Long idSiarp = SAPService.extraerIdSiarp(archivo.getNombre());
		boolean reprocesar = false;
		for (HashMap<CamposAnulado, String> r : archivo.getRegistros()) {
			retornoSap = retornoSap + "\n" + r.get(CamposAnulado.ID_SIARP) + ";" + r.get(CamposAnulado.DOC_SAP) + ";" + r.get(CamposAnulado.MOT_ANULACION) + ";" + r.get(CamposAnulado.DESC_ANULACION);
			if (!reprocesar) {
				String a = SAPDao.accionReprocesar(r.get(CamposAnulado.MOT_ANULACION));
				if (a != null && a.equals("REPROCESAR")) {
					reprocesar = true;
				}
			}
		}
		String historicoSAP = SAPDao.recuperarRetornoSAPPago(idSiarp);
		if (historicoSAP == null) {
			historicoSAP = "";
		}
		historicoSAP += retornoSap;

		int affectedRows = SAPDao.actualizarErrorPago(idSiarp, "ANULADO", historicoSAP);
		SAPDao.actualizarPagosSAP(idSiarp, "A", retornoSap, "GCI", "GCI");
		SAPDao.actualizarSolPagoFatura(idSiarp);

		if (affectedRows == 0) {
			log.error("El pago con ID_SIARP" + idSiarp + "no existe en la tabla PA_MV_PAGO, se procesa el archivo ANULADO [" + archivo.getNombre() + "] sin afectar ningun pago!");
		}
		
		if(reprocesar){
			SAPDao.marcarReprocesoSAP(idSiarp);
		}		
	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void procesarPagadosSAP() {
		
		for (ArchivoPago archivo : SAPService.buscarArchivosPagados("20")) {
			ArchivoPagoDetalle detallePago = null;
			String retornoSap = "\n" + (ZonedDateTime.now().format(zonaHoraria));
			Long idSiarp = SAPService.extraerIdSiarp(archivo.getNombre());
			for (HashMap<CamposPago, String> r : archivo.getRegistros()) {
				retornoSap = retornoSap + "\n" + r;
				if (r.get(CamposPago.ID_ORIGEN_PAGO).equals("YQ")) {
					File f = SAPService.buscarArchivo(idSiarp + "_Detalle.txt", SAPDirectorio.PAGADOS);
					detallePago = new ArchivoSAPF<ArchivoPagoDetalle>().crear(f, ArchivoPagoDetalle.class);
				}
			}
			String historicoSAP = SAPDao.recuperarRetornoSAPPago(idSiarp);
			if (historicoSAP == null) {
				historicoSAP = "";
			}
			historicoSAP += retornoSap;

			try {				
				transaccionPagados(idSiarp, archivo.getRegistros().get(0).get(CamposPago.ORDEN_PAGO), historicoSAP, retornoSap, archivo.getRegistros().get(0).get(CamposPago.FECHA_PAGO));
				SAPService.moverArchivo(SAPDirectorio.PAGADOS, SAPDirectorio.PROCESADOS_PAGADOS, archivo.getNombre());
				System.out.println("--------" + archivo);
				if (detallePago != null) {
					try {
						transaccionDetallePago(detallePago.getRegistros(), idSiarp);
						SAPService.moverArchivo(SAPDirectorio.PAGADOS, SAPDirectorio.PROCESADOS_PAGADOS, detallePago.getNombre());
						System.out.println("--------" + detallePago);
					} catch (Exception e) {
						log.error(e);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName , propagation = Propagation.REQUIRES_NEW)
	public void procesarErroresSAP() {
		
		List<ArchivoError> archivos = SAPService.buscarArchivosErroneos("20");
		
		for (ArchivoError archivo : archivos) {
			String retornoSap = "\n" + (ZonedDateTime.now().format(zonaHoraria));
			Long idSiarp = SAPService.extraerIdSiarp(archivo.getNombre());
			for (HashMap<CamposError, String> r : archivo.getRegistros()) {
				retornoSap = retornoSap + "\n" + r.get(CamposError.ID_SIARP) + ";" + r.get(CamposError.COD_ERROR) + ";" + r.get(CamposError.DESC_ERROR);
			}
			String historicoSAP = SAPDao.recuperarRetornoSAPPago(idSiarp);
			if (historicoSAP == null) {
				historicoSAP = "";
			}
			historicoSAP += retornoSap;
			try {
				transaccionErroneos(idSiarp, historicoSAP, retornoSap, archivo.getNombre());
				SAPService.moverArchivo(SAPDirectorio.ERRONEOS, SAPDirectorio.PROCESADOS_ERRONEOS, archivo.getNombre());
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void transaccionErroneos(Long idSiarp, String historicoSAP, String retornoSap, String nombreArchivo) {
		int affectedRows = SAPDao.actualizarErrorPago(idSiarp, "ERROR", historicoSAP);
		SAPDao.actualizarPagosSAP(idSiarp, "E", retornoSap, "GCI", "GCI");
		SAPDao.actualizarSolPagoFatura(idSiarp);
		if (affectedRows == 0) {
			log.error("El pago con ID_SIARP" + idSiarp + "no existe en la tabla PA_MV_PAGO, se procesa el archivo de ERROR [" + nombreArchivo + "] sin afectar ningun pago!");
		}
	}

	

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void transaccionPagados(Long idSiarp, String numOP, String historicoSAP, String retornoSap, String fechaPago) {
		Long numOPSise = Long.valueOf(numOP);
		
		SAPDao.actualizarRespuestaPago(idSiarp, numOP, numOPSise, historicoSAP, "GCI", "GCI", fechaPago);
		
		
		SAPDao.actualizarPagosSAP(idSiarp, "P", retornoSap, "GCI", "GCI");
		
	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void transaccionDetallePago(List<HashMap<CamposPagoDetalle, String>> Registros, Long idSiarp) {
		for (HashMap<CamposPagoDetalle, String> r : Registros) {
			String valorMov = r.get(CamposPagoDetalle.VALOR_MOV);
			valorMov = valorMov.replace("-", "");
			valorMov = valorMov.replace(".", "");
			valorMov = valorMov.replace(",", ".");
			SAPDao.insertarDetallePago(idSiarp, r.get(CamposPagoDetalle.NUM_CUENTA), r.get(CamposPagoDetalle.TIPO_RET), r.get(CamposPagoDetalle.IND_RET), r.get(CamposPagoDetalle.IND_IVA),
					Double.valueOf(valorMov), "GCI", "GCI");
		}
	}

}
