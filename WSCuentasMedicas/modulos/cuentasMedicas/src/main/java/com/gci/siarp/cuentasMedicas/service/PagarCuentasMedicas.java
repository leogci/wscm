package com.gci.siarp.cuentasMedicas.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.AjustarReservaDao;
import com.gci.siarp.cuentasMedicas.dao.FacturaDao;
import com.gci.siarp.cuentasMedicas.dao.SAPDao;
import com.gci.siarp.cuentasMedicas.domain.Cuenta;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.cuentasMedicas.domain.InfoEmbargo;
import com.gci.siarp.cuentasMedicas.domain.PA_PM_Rubros;
import com.gci.siarp.cuentasMedicas.domain.Pago;
import com.gci.siarp.cuentasMedicas.domain.Proveedor;
import com.gci.siarp.generales.domain.StructRetornos;
import com.gci.siarp.generales.domain.sap.SAPCabecera;
import com.gci.siarp.generales.domain.sap.SAPDetalle;
import com.gci.siarp.generales.domain.sap.SAPIdentificacion;
import com.gci.siarp.generales.service.ReservasService;
import com.gci.siarp.generales.service.SAPService;
import com.gci.siarp.generales.service.UtilidadesFechas;
import com.vaadin.server.ServiceException;

@Log4j
@SiarpService
public class PagarCuentasMedicas {
	
	@Autowired
	FacturaDao facturaDao;
	
	@Autowired
	SAPService SAPservice;
	
	@Autowired
	AcuerdoService acuerdoService;

	@Autowired
	UtilidadesFechas fechasUTIL;
	
	@Autowired
	SAPDao SAPDAO;
	
	@Autowired 
	ReservasService reservaService;
	
	@Autowired
	AjustarReservaDao AjustarDAO;
	
	private SAPIdentificacion lineaSAP1;
	private SAPCabecera lineaSAP2;
	private List<SAPDetalle> lineaSAP3;
	private DateTimeFormatter zonaHoraria = DateTimeFormatter.ofPattern("yyyy-LL-d h:mm;");
	private Cuenta objcuenta;
	String tipoDocumento;
		
	@Transactional(value = SiarpDatabase.transactionManagerBeanName, propagation = Propagation.REQUIRES_NEW)
	public void PagarCM(Long cuenta){
		System.out.println("Cuenta para pago: "+cuenta);
		boolean cuentaB = false;
		List<Factura> listFact = SAPDAO.pagossinreconocer(cuenta);
		if (!(listFact.isEmpty())) {
			for (Factura fact : listFact) {
				/*StructRetornos struc = reservaService.reconocerCMedicas(fact.getIdFuratFurep(), "AS", fact.getValorNeto(), "AS", "CM", fact.getIdCuenta(), fact.getIdFactura(),
						"GCI", "GCI");
				System.out.println("Retorno struc "+struc.getICodigo());
				
				if (struc.getICodigo() == -1) {
					log.error(struc.getSMensaje());
					cuentaB = false;
					break;
				}*/
				try {
					reservaService.reconocerAS(fact.getIdFuratFurep(), fact.getValorNeto(), "AS", 0, fact.getNumeroAutorizacion() != null ? Long.valueOf(fact.getNumeroAutorizacion()) : null, fact.getIdCuenta(), fact.getIdFactura(), "CM", 	"GCI", "GCI");
				} catch (NumberFormatException | ServiceException e) {
					e.printStackTrace();
				}
				
				AjustarDAO.actualizarReconocimiento(fact.getIdCuenta(), fact.getIdFactura());
				//cuentaB = true;
				cuentaB = false;
			}
		}else{
			//cuentaB = true;
			cuentaB = false;
		}
	
		if(cuentaB){			
			objcuenta = facturaDao.cuentaReembolso(cuenta);
			if(objcuenta.getIdReembolso() != null ){
				 tipoDocumento = "YU";
				 generarArchivoSAP(cuenta, tipoDocumento);
			}else{
				StructRetornos retorno = acuerdoService.registrarAcuerdo(cuenta, "GCI", "GCI");
				if (retorno.getICodigo() == 0 || retorno.getICodigo() == -8) {
					tipoDocumento = "ZE";
					generarArchivoSAP(cuenta, tipoDocumento);				
				} else if (retorno.getICodigo() == -9) {
					log.error(retorno.getSMensaje());						
				}
				else {
					tipoDocumento = "YQ";
					generarArchivoSAP(cuenta, tipoDocumento);
				}
			}
		}		
	} 
	
	public void generarArchivoSAP(Long cuenta, String acuerdo){
		System.out.println("ARCHIVO");
		InfoEmbargo embargo = null;
		String ciudad = null;
		Double valorPagar = 0.0, valorComision =0.0;
		Boolean generarArchivo= true, contraSAP = false;
		String contrSAP="2357102499";
		Factura factura = SAPDAO.pagarSAP(cuenta);
		Long SIARP = Long.valueOf(SAPDAO.consultaridSIARP());
		SIARP += 1;
		Boolean codSAP=true;
		SAPDAO.actualizaridSIARP(SIARP.toString());
		if (factura.getFechaFactura() == null || factura.getFechaRadicacion() == null || factura.getPrefijoFactura() == null || factura.getFactura() == null
				|| factura.getIdDepartamento() == null || factura.getIdMunicipio() == null) {
			errorSAP(cuenta,"La informacion en Factura esta incompleta.",SIARP);
		} else {
			if (factura.getCodSAP() == null) {
				factura.setCodSAP(SAPDAO.consultarCodigoSAP(factura.getIdDepartamento(), factura.getIdMunicipio()));
				if (factura.getCodSAP() == null) {
					errorSAP(cuenta, "El codigo sucursal SAP es NULL.", SIARP);
					codSAP=false;
				} 
			}			
			if (codSAP) {
					ciudad = facturaDao.consultarCiudad(factura.getIdDepartamento(), factura.getIdMunicipio());
					if (ciudad == null) {
						errorSAP(cuenta,"La ciudad es NULL.",SIARP);
					} else {
						Proveedor proveedor = null;
						if(objcuenta.getIdReembolso() != null ){
							proveedor = new Proveedor();
							informacionProveedor(proveedor);
						}else{
							proveedor = facturaDao.consultarProveedor(factura.getIdTipoDocProv(), factura.getIdProveedor());
							embargo = facturaDao.inforEmbargo(factura.getIdTipoDocProv(), factura.getIdProveedor());
							if(embargo != null){
								acuerdo = "ZF";
							}
						}
						
					if (proveedor.getIdProveedor() == null || proveedor.getIdTipoDoc() == null || proveedor.getRazonSocial() == null || proveedor.getDvEmpresa() == null
							|| proveedor.getIdBanco() == null || proveedor.getTipoCuentaBancaria() == null || proveedor.getCuentaBancaria() == null
							|| proveedor.getTipoPersona() == null) {
							if (objcuenta.getIdReembolso() != null) {
								errorSAP(cuenta,"La informacion de Reembolso esta incompleta.",SIARP);
							}
							errorSAP(cuenta,"La informacion de proveedor esta incompleta.",SIARP);
						}else{							
							lineaSAP3 = new ArrayList<SAPDetalle>();
							int consecutivo = 1;
							List<Map<String,?>>  listadoRubrosPago = SAPDAO.valoresPagoSAP(cuenta);	
							
							
							
							
							if(!listadoRubrosPago.isEmpty()){
								
								int numRubros=0;
								for(Map<String, ?> rubros : listadoRubrosPago){
									String vrrubro = rubros.get("idRubro").toString();
									if(!vrrubro.equals("1103")){
										numRubros++;
									}else{
										contraSAP=true;	
									}
								}
								
								if(numRubros>1){
									errorSAP(cuenta, "Los rubros para la cuenta son mas de uno.", SIARP);
									generarArchivo = false;
								}
								
								
								for (Map<String, ?> rubros : listadoRubrosPago) {									
									String vrrubro = rubros.get("idRubro").toString();
									Double vrPagar = Double.valueOf(rubros.get("ValorPago").toString());
									Double vrIva = Double.valueOf(rubros.get("ValorIVA").toString());
									PA_PM_Rubros rubro = facturaDao.consultarRubrosID(vrrubro);
									if (rubro.getCreditoSAP() == null || rubro.getDebitoSAP() == null) {
										errorSAP(cuenta, "La informacion del Rubro esta incompleta.", SIARP);
										generarArchivo = false;
										break;
									}
									
									if (rubro.getIdRubro().equals("1103")) {									
										valorComision = vrPagar;
									}else{
										valorPagar += vrPagar;
									}									
									
									if(contraSAP){
										rubro.setCreditoSAP(contrSAP);
									}
									
									lineaSAP3.add(llenarDetalleHaber(factura, proveedor, rubro, consecutivo, vrPagar, ciudad, SIARP,acuerdo,embargo));
									consecutivo++;
									lineaSAP3.add(llenarDetalleDebe(factura, rubro, consecutivo, vrPagar, vrIva, SIARP));
									consecutivo++;
									}
								
								if(generarArchivo){
									
									llenarCabecera(factura, proveedor,acuerdo);
									lineaSAP2.setIdSiarp(SIARP.toString());			
									lineaSAP1 = new SAPIdentificacion("0",SIARP.toString());
									SAPDAO.actualizarIDSOLPAGO(cuenta,SIARP);
									SAPDAO.actualizarPAGOSAP(cuenta, SIARP, new Date());								
									SAPDAO.insertarHistoricoSAP(SIARP, cuenta, valorPagar, valorComision, new Date(), null, null, null);								
									if(!SAPservice.crearArchivoCM(lineaSAP1, lineaSAP2, lineaSAP3, SIARP.toString())){
										errorSAP(cuenta,"Se genero un error al generar el archivo para pagar.",SIARP);
									}									
								}
							}else{
								errorSAP(cuenta,"Se genero un error en los rubros para pagar.",SIARP);
							}
						}
					}				
			}
		}
	}	
	
	public void llenarCabecera(Factura factura, Proveedor proveedor, String Acuerdo){
		lineaSAP2 = new SAPCabecera();
		lineaSAP2.setTipoLinea("1");
		lineaSAP2.setTipoRegistro(Acuerdo);
		lineaSAP2.setFechaDocumento(factura.getFechaFactura());
		lineaSAP2.setSociedad("1000");
		Date fechaCont = new Date();
		LocalDate fecha = fechasUTIL.convertirDateLD(fechaCont);
		if (fecha.getDayOfMonth() <= 24) {
			lineaSAP2.setFechaContab(fechaCont);
		} else {
			LocalDate fechaNueva = (fecha.withDayOfMonth(1)).plusMonths(1);
			lineaSAP2.setFechaContab(fechasUTIL.convertirLD(fechaNueva));
		}
		lineaSAP2.setCodMoneda("COP");		
		lineaSAP2.setTextoOP("CM-"+proveedor.getIdProveedor()+"-"+factura.getPrefijoFactura()+factura.getFactura());
	}
	
	public SAPDetalle llenarDetalleHaber(Factura factura,Proveedor proveedor,PA_PM_Rubros rubro, int consecutivo, Double vr, String ciudad, Long siarp, String Acuerdo,InfoEmbargo embargo){
		
		SAPDetalle HaberFactura = new SAPDetalle(); 
		HaberFactura.setTipoLinea("2");
		HaberFactura.setTipo("A");
		HaberFactura.setCodAbona("PO33");
		HaberFactura.setCuentaAsociada("2511050001");
		HaberFactura.setGrTesoreria("P033");
		HaberFactura.setDigitoVer("X");
		HaberFactura.setViaPagoTercero("T");
		HaberFactura.setDebeHaber("H");
		HaberFactura.setPosicion(consecutivo);
		HaberFactura.setRetAplic("X");
		HaberFactura.setImpValor(vr);
		HaberFactura.setCondPago("0001");
		HaberFactura.setViaPagoFactura("T");
		HaberFactura.setSnCausacion("A");//dialogando todo lo que ingrese por aqui es una cuenta medica, ...
		HaberFactura.setImpIva(null);
		HaberFactura.setCodSucCta(factura.getCodSAP());
		HaberFactura.setCECO(factura.getCodSAP());
		HaberFactura.setCuentaCont(rubro.getCreditoSAP());
		LocalDate fecha = fechasUTIL.convertirDateLD(factura.getFechaFactura());
		HaberFactura.setFechaBase(fechasUTIL.convertirLD(fecha.plusDays(30)));		
		HaberFactura.setNroDoc(proveedor.getIdProveedor() + proveedor.getDvEmpresa());
		HaberFactura.setNroCta(proveedor.getCuentaBancaria());
		HaberFactura.setCiudad(ciudad);
		HaberFactura.setIdSiarp(siarp.toString());
		HaberFactura.setClaseImpuesto(proveedor.getTipoPersona());
		if (proveedor.getRazonSocial().length() > 80) {
			HaberFactura.setTxtNombre(proveedor.getRazonSocial().substring(0, 40));
			HaberFactura.setTxtApellido1(proveedor.getRazonSocial().substring(40, 80));
			HaberFactura.setTxtApellido2(proveedor.getRazonSocial().substring(80, proveedor.getRazonSocial().length()));
		} else if (proveedor.getRazonSocial().length() > 40) {
			HaberFactura.setTxtNombre(proveedor.getRazonSocial().substring(0, 40));
			HaberFactura.setTxtApellido1(proveedor.getRazonSocial().substring(40, proveedor.getRazonSocial().length()));
		} else {
			HaberFactura.setTxtNombre(proveedor.getRazonSocial());
		}
		
		switch (proveedor.getIdTipoDoc()) {
		case "CC":			
			HaberFactura.setCodTipoDoc("C");
			break;
		case "CE":			
			HaberFactura.setCodTipoDoc("E");
			break;
		case "NI":			
			HaberFactura.setCodTipoDoc("N");
			break;
		case "TI":			
			HaberFactura.setCodTipoDoc("I");
			break;
		}
		if (proveedor.getIdBanco() <= 9) {
			HaberFactura.setCodBco("0" + proveedor.getIdBanco().toString());
		} else {
			HaberFactura.setCodBco(proveedor.getIdBanco().toString());
		}
		if (proveedor.getTipoCuentaBancaria().equals("A")) {
			HaberFactura.setCodTipoCta("02");
		} else {
			HaberFactura.setCodTipoCta("01");
		}
		
		if(Acuerdo.equals("ZF")){
			HaberFactura.setNumExp(embargo.getNumeroProceso());
			HaberFactura.setCodJuzgado(embargo.getIdJuzgado());
			HaberFactura.setValDeposito(vr);
			HaberFactura.setTipoDocDemando(proveedor.getIdTipoDoc());
			HaberFactura.setIdDemando(proveedor.getIdProveedor());
			if (proveedor.getRazonSocial().length() > 20) {
				HaberFactura.setNomDemando(proveedor.getRazonSocial().substring(0, 20));
				HaberFactura.setApellDemando(proveedor.getRazonSocial().substring(20, proveedor.getRazonSocial().length()));
			} else {
				HaberFactura.setNomDemando(proveedor.getRazonSocial());
			}

			if (embargo.getDescripcion().length() > 20) {
				HaberFactura.setNomDemandante(embargo.getDescripcion().substring(0, 20));
				HaberFactura.setApellDemandante(embargo.getDescripcion().substring(20, embargo.getDescripcion().length()));
			} else {
				HaberFactura.setNomDemandante(embargo.getDescripcion());
			}
		}
		
		return HaberFactura;
	}
		
	public SAPDetalle llenarDetalleDebe(Factura factura,PA_PM_Rubros rubro, int consecutivo, Double vr,Double vrIVA,Long siarp ){
		SAPDetalle DebeFactura = new SAPDetalle();
		DebeFactura.setTipoLinea("2");
		DebeFactura.setTipo("C");
		DebeFactura.setCuentaCont(rubro.getDebitoSAP());
		DebeFactura.setDebeHaber("D");
		DebeFactura.setPosicion(consecutivo);
		DebeFactura.setImpValor(vr);
		DebeFactura.setImpIva(vrIVA);
		DebeFactura.setCodSucCta(factura.getCodSAP());
		DebeFactura.setCECO(factura.getCodSAP());		
		return DebeFactura;		
	}
	
	public void informacionProveedor(Proveedor proveedor){
		
		proveedor.setIdProveedor(objcuenta.getDocumento().toString());
		proveedor.setIdTipoDoc(objcuenta.getTipoDocumento());
		proveedor.setRazonSocial(objcuenta.getRazonSocial());
		proveedor.setDvEmpresa(objcuenta.getDvEmpresa());
		proveedor.setIdBanco(objcuenta.getCodigoBanco());
		proveedor.setTipoCuentaBancaria(objcuenta.getTipoCuenta());
		proveedor.setCuentaBancaria(objcuenta.getNumeroCuenta());
		proveedor.setTipoPersona(objcuenta.getTipoPersona());
		
	}
	
 	public void errorSAP(Long cuenta,String error,Long SIARP){
		
		List<Pago> pagos = SAPDAO.listadoPagosSAP(cuenta);
		String comentario = "";
		for (Pago registro : pagos) {
			if (registro.getRetornoSAP() == null) {
				comentario = "\n" + ((ZonedDateTime.now().format(zonaHoraria))) + "\n" + SIARP + " " + error;
				SAPDAO.actualizarPagoError(registro.getIdCuenta(), registro.getIdFactura(), SIARP, "ERROR", comentario);							
			} else {
				comentario = registro.getRetornoSAP() + "\n" + ((ZonedDateTime.now().format(zonaHoraria))) + "\n" + SIARP + " " + error;
				SAPDAO.actualizarPagoError(registro.getIdCuenta(), registro.getIdFactura(), SIARP, "ERROR", comentario);						
			}
		}
		
		SAPDAO.insertarHistoricoSAP(SIARP, cuenta, 0.0, 0.0, null, new Date(), "E", error);	
	}
	
}