package com.gci.siarp.cuentasMedicas.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.FacturaDao;
import com.gci.siarp.cuentasMedicas.dao.SAPDao;
import com.gci.siarp.cuentasMedicas.domain.Cuenta;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.cuentasMedicas.domain.Parametros;
import com.gci.siarp.cuentasMedicas.utils.ValidacionesProceso;
import com.gci.siarp.generales.service.ReservasService;

@Log4j
@SiarpService
public class GestionFactura {

	@Autowired
	FacturaDao facturaDao;

	@Autowired
	LogSYC logSYC;

	@Autowired
	ReservasService reservaService;

	@Autowired
	SAPDao SAPDAO;

	@Autowired
	ValidacionesProceso vp;

	Cuenta cuenta;
	Factura factura;
	NodeList listadoXML;
	SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String idRubro, Reemb;
	Long l_consecutivo = 0l;

	/*public String getRetorno(String s_retorno) {
		return s_retorno;
	}*/

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, noRollbackFor = DuplicateKeyException.class)
	public String WScrearFactura(String ps_factura, String ps_visado,
			int ps_multiusuario, String ps_radicador, String ps_usuario,
			Date ps_fecha, String ps_maquina) throws Exception {
		String s_retorno = null;

		boolean xmlValido = (validarXML2(ps_factura, "R", ps_multiusuario,
				ps_radicador, ps_usuario, ps_maquina, ps_visado));

		if (cuenta.getIdCuenta() != null && factura.getIdFactura() != null) {
			asignarConsecutivo(cuenta.getIdCuenta(), factura.getIdFactura());
			logSYC.insertarRadicacionSYS(cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo, "M",
					cuenta.getNumeroFacturas(), factura.getAudConcurrente(),
					ps_factura, cuenta.getUsuarioAud(), cuenta.getMaquinaAud());
		} else {
			log.error("ERROR: En el archivo XML ha presentado un error en los campos Cuenta e idFactura");
			return "ERROR: En el archivo XML ha presentado un error en los campos Cuenta e idFactura";
		}

		if (!xmlValido) {
			s_retorno = "ERROR:" + s_retorno;
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error(s_retorno);
			return s_retorno;
		}
		log.error("Numero de radicado: " + cuenta.getIdCuenta()
				+ " Factura numero: " + factura.getIdFactura() + " Re:" + Reemb);
		System.out
				.println("Numero de radicado: " + cuenta.getIdCuenta()
						+ " Factura numero: " + factura.getIdFactura() + " Re:"
						+ Reemb);

		if (cuenta.getNumeroFacturas() <= facturaDao.totalFacturas(cuenta
				.getIdCuenta())) {
			s_retorno = "ERROR: No se puede ingresar esta factura, debido a que el campo multiusuario solo permite "
					+ cuenta.getNumeroFacturas() + " facturas";
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			return s_retorno;

		}

		if (actualizarAUD(factura.getAudConcurrente(), factura) == false) {
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			return s_retorno;
		}

		try {
			validacionesExtra(factura, idRubro);
		} catch (RuntimeException e) {
			logSYC.actualizarSYC(e.getMessage(), cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error(e.getMessage());
			return e.getMessage();
		}

		try {
			setEstadoFactura(factura);
		} catch (RuntimeException e) {

			/* Validación de la existencia del siniestro */

			logSYC.actualizarSYC(e.getMessage(), cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error(e.getMessage());
			return e.getMessage();
		}

		if (crearCuenta(cuenta, factura, l_consecutivo)) {
			return "ERROR: Se ha producido un error general al intentar crear la cuenta con el numero: "
					+ cuenta.getIdCuenta()
					+ ", consulte el Log para mas informacion";
		}

		if ((crearFactura(factura, cuenta, l_consecutivo)) == false) {
			return s_retorno;
		}

		if ((actualizarVrCuenta(cuenta, factura, l_consecutivo)) == false) {
			return s_retorno;
		}

		if ((insertarRubro(factura, cuenta, idRubro)) == false) {
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			throw new RuntimeException(s_retorno);
		}

		if (Reemb.equals("S")) {
			if ((ingresarReembolso(cuenta, factura, l_consecutivo)) == false) {
				return s_retorno;
			}
		}

		s_retorno = "EXITO: Se creo la factura " + factura.getFactura() + ".";
		logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
				factura.getIdFactura(), l_consecutivo);
		log.info(s_retorno);
		return s_retorno;
	}

	public void validacionesExtra(Factura objfactura, String idRubro) {

		/* Validacion Existencia del afiliado */
		if (!facturaDao.existeAfiliado(objfactura.getIdTipoDoc(),
				objfactura.getIdPersona())) {
			throw new RuntimeException(
					"ERROR: El afiliado con los campos id_persona, id_tipo_doc no se encontro en la tabla GRAL_MA_PERSONA");
		}

		/* Validacion diagnostico */
		if (!facturaDao.existeDiagnostico(objfactura.getIdDX())) {
			throw new RuntimeException(
					"ERROR: El diagnostico con el valor del campo id_dx no se encontro en la tabla GRAL_PM_DIAGNOSTICO");
		}

		/* Validacion Departamento */
		if (!facturaDao.existeDepartamento(objfactura.getIdDepartamento())) {
			throw new RuntimeException(
					"ERROR: El Departamento con el valor del campo cod_departamento no se encontro en la tabla GRAL_PM_DEPARTAMENTO");
		}
		/* Validacion Municipio */
		if (!facturaDao.existeMunicipio(objfactura.getIdDepartamento(),
				objfactura.getIdMunicipio())) {
			throw new RuntimeException(
					"ERROR: El Municipio con el valor de los campos cod_departamento y cod_municipio no se encontro en la tabla GRAL_PM_MUNICIPIO");
		}

		/* Validacion id_seccional */
		// if(true){
		//
		// throw new
		// RuntimeException("ERROR: El diagnostico con el valor del campo id_dx no se encontro en la tabla GRAL_PM_DIAGNOSTICO");
		// }

		/* Validacion idRubro */
		if (!facturaDao.existeRubro(String.valueOf(idRubro))) {
			throw new RuntimeException(
					"ERROR: El Rubro con el valor del campo id_rubro no se encontro en la tabla PA_PM_RUBROS");
		}

	}

	@Transactional(value = SiarpDatabase.transactionManagerBeanName, noRollbackFor = DuplicateKeyException.class)
	public String WSmodificarFactura(String ps_factura, String ps_visado,
			int ps_multiusuario, String ps_usuario, Date ps_fecha,
			String ps_maquina, int pi_reproceso, String ps_radicador) {

		String s_retorno = null;
		boolean xmlValido = validarXML2(ps_factura, "M", ps_multiusuario,
				ps_radicador, ps_usuario, ps_maquina, ps_visado);

		if (cuenta.getIdCuenta() != null && factura.getIdFactura() != null) {
			asignarConsecutivo(cuenta.getIdCuenta(), factura.getIdFactura());
			logSYC.insertarRadicacionSYS(cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo, "M",
					cuenta.getNumeroFacturas(), factura.getAudConcurrente(),
					ps_factura, cuenta.getUsuarioAud(), cuenta.getMaquinaAud());
		} else {
			log.error("ERROR: En el archivo XML ha presentado un error en los campos Cuenta e idFactura");
			return "ERROR: En el archivo XML ha presentado un error en los campos Cuenta e idFactura";
		}

		if (!xmlValido) {
			s_retorno = "ERROR: , " + s_retorno;
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error(s_retorno);
			return s_retorno;
		}

		System.out.println("Numero de radicado: " + cuenta.getIdCuenta()
				+ " Factura numero: " + factura.getIdFactura());

		Factura facOld = facturaDao.existeFactura(cuenta.getIdCuenta(),
				factura.getIdFactura());

		if (facOld == null) {
			s_retorno = "ERROR: No se puede modificar esta factura, porque no existe en la base de datos";
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error("ERROR: No se puede modificar esta factura, porque no existe en la base de datos");
			return s_retorno;
		}

		String estadoFactura = facturaDao.estadoFactura(cuenta.getIdCuenta(),
				factura.getIdFactura());

		if (actualizarAUD(factura.getAudConcurrente(), factura) == false) {

			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			return s_retorno;
		}

		try {
			validacionesExtra(factura, idRubro);
		} catch (RuntimeException e) {
			logSYC.actualizarSYC(e.getMessage(), cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error(e.getMessage());
			return e.getMessage();
		}

		try {
			setEstadoFactura(factura);
		} catch (RuntimeException e) {
			/* Validación de la existencia del siniestro */
			logSYC.actualizarSYC(e.getMessage(), cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			log.error(e.getMessage());
			return e.getMessage();
		}

		if (estadoFactura != null) {
			if (estadoFactura.equals("DE") || estadoFactura.equals("DI")) {
				s_retorno = "ERROR: La factura no se puede modificar porque se encuentra en el estado devuelta";
				logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
						factura.getIdFactura(), l_consecutivo);
				return s_retorno;
			}
		}

		factura.setIdSolPago(facturaDao.facturaSOlPago(cuenta.getIdCuenta(),
				factura.getIdFactura()));
		if (factura.getIdSolPago() == null) {
			factura.setIdSolPago(0l);
		}

		if (!(facturaCP(cuenta.getIdCuenta(), factura.getIdFactura(),
				factura.getIdSolPago()))) {
			s_retorno = "ERROR: Esta intentando modificar la factura "
					+ factura.getFactura()
					+ ", pero esta se encuentra Causada y Pagada";
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			return s_retorno;
		}

		cuenta.setNumeroFacturas(facturaDao.numeroFacturas(cuenta.getIdCuenta()));

		if ((modificarFactura(factura, cuenta)) == false) {
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			return s_retorno;
		}

		if ((actualizarVrCuenta(cuenta, factura, l_consecutivo)) == false) {
			return s_retorno;
		}

		inactivarPago(cuenta.getIdCuenta(), factura.getIdFactura());
		inactivarAuditoria(cuenta.getIdCuenta(), factura.getIdFactura());
		inactivarRubro(cuenta.getIdCuenta(), factura.getIdFactura());

		if ((insertarRubro(factura, cuenta, idRubro)) == false) {
			logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
					factura.getIdFactura(), l_consecutivo);
			throw new RuntimeException(s_retorno);
		}

		if (Reemb.equals("S")) {
			if ((ingresarReembolso(cuenta, factura, l_consecutivo)) == false) {
				return s_retorno;
			}
		}

		s_retorno = "EXITO: Se modifico la factura " + factura.getFactura()
				+ ".";
		logSYC.actualizarSYC(s_retorno, cuenta.getIdCuenta(),
				factura.getIdFactura(), l_consecutivo);
		return s_retorno;
	}

	public boolean crearFactura(Factura objFactura, Cuenta objCuenta,
			Long consecutivo) throws Exception {
		String s_retorno = null;
		try {
			facturaDao.insertarFactura(objCuenta.getIdCuenta(),
					objFactura.getIdFactura(),
					objFactura.getIdTipoDocEmpresa(),
					objFactura.getIdEmpresa(), objFactura.getIdTipoDoc(),
					objFactura.getIdPersona(), objFactura.getIdDX(),
					objFactura.getNombreDX(), objFactura.getIdFuratFurep(),
					objFactura.getFechaAtep(), objFactura.getTipoSiniestro(),
					objFactura.getValorNeto(), objFactura.getValorBruto(),
					objFactura.getNumeroAutorizacion(),
					objFactura.getIndEstadoFactura(),
					objFactura.getNotaCredito(), objCuenta.getUsuarioAud(),
					objCuenta.getMaquinaAud(), objFactura.getFactura(),
					objFactura.getFechaFactura(), objFactura.getIdSeccional(),
					objFactura.getValorIVA(), objFactura.getIdProveedor(),
					objFactura.getIdTipoDocProv(),
					objFactura.getPrefijoFactura(),
					objFactura.getFechaServicio(),
					objFactura.getFechaAutorizacion(),
					objFactura.getIdSucursal(), objFactura.getIdTipoSOl(),
					objFactura.getCodSTIKKER(), objFactura.getIdDepartamento(),
					objFactura.getIdMunicipio(),
					objFactura.getAudConcurrente(),
					objFactura.getFechaRadicacion(),
					objFactura.getIdAuditorConcurrente(),
					objFactura.getIdRadicador(), objFactura.getCodSucursal(),
					objFactura.getIndVRCertificado(), objFactura.getCodSAP(),
					objFactura.getReconReserva());

		} catch (DuplicateKeyException e) {
			s_retorno = "ERROR: La factura " + objFactura.getFactura()
					+ " ya ha sido radicada. No se puede volver a ingresar";
			logSYC.actualizarSYC(s_retorno, objCuenta.getIdCuenta(),
					objFactura.getIdFactura(), consecutivo);
			log.error(e.getClass());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean modificarFactura(Factura objFactura, Cuenta objCuenta) {
		String s_retorno = null;
		try {

			facturaDao.actualizarFactura(objCuenta.getIdCuenta(),
					objFactura.getIdFactura(),
					objFactura.getIdTipoDocEmpresa(),
					objFactura.getIdEmpresa(), objFactura.getIdTipoDoc(),
					objFactura.getIdPersona(), objFactura.getIdDX(),
					objFactura.getNombreDX(), objFactura.getIdFuratFurep(),
					objFactura.getFechaAtep(), objFactura.getTipoSiniestro(),
					objFactura.getValorNeto(), objFactura.getValorBruto(),
					objFactura.getNumeroAutorizacion(),
					objFactura.getNotaCredito(), objCuenta.getUsuarioAud(),
					objCuenta.getMaquinaAud(), objFactura.getFactura(),
					objFactura.getFechaFactura(), objFactura.getIdSeccional(),
					objFactura.getValorIVA(), objFactura.getIdProveedor(),
					objFactura.getIdTipoDocProv(),
					objFactura.getPrefijoFactura(),
					objFactura.getFechaServicio(),
					objFactura.getFechaAutorizacion(),
					objFactura.getIdSucursal(), objFactura.getIdTipoSOl(),
					objFactura.getCodSTIKKER(),
					objFactura.getFechaRadicacion(),
					objFactura.getIdAuditorConcurrente(),
					objFactura.getIdRadicador(),
					objFactura.getIdDepartamento(),
					objFactura.getIdMunicipio(),
					objFactura.getAudConcurrente(),
					objFactura.getCodSucursal(),
					objFactura.getIndEstadoFactura(),
					objFactura.getIndVRCertificado(), objFactura.getCodSAP(),
					objFactura.getReconReserva());

		} catch (Exception e) {
			s_retorno = "ERROR: No se pudo actualizar la informacion de la factura "
					+ objFactura.getFactura()
					+ " verifique el log para mas detalle";
			log.error(e);
			return false;
		}

		return true;

	}

	public void asignarConsecutivo(Long cuenta, Long factura) {

		l_consecutivo = logSYC.consultarConsecutivoRadicacion(cuenta, factura);
		if (l_consecutivo == null || l_consecutivo == 0) {
			l_consecutivo = 1l;
		} else {
			l_consecutivo++;
		}

	}

	public boolean crearCuenta(Cuenta objCuenta, Factura objfactura,
			Long consecutivo) {
		String s_retorno = null;
		if ((facturaDao.consultarCuenta(objCuenta)) == null) {
			try {
				facturaDao.insertarCuenta(objCuenta);
				return false;
			} catch (Exception e) {
				s_retorno = "ERROR: Se ha producido un error general al intentar crear la cuenta con el numero: "
						+ objCuenta.getIdCuenta()
						+ ", consulte el Log para mas informacion";
				logSYC.actualizarSYC(s_retorno, objCuenta.getIdCuenta(),
						objfactura.getIdFactura(), consecutivo);
				log.error(e.getMessage());
				return true;
			}
		}
		return false;
	}

	public boolean facturaCP(Long idcuenta, Long idfactura, Long idSolPago) {
		Long cuentaPagada = facturaDao.consultarPago(idcuenta, idfactura);
		if ((idSolPago > 0 || cuentaPagada > 0)) {
			return false;
		}
		return true;
	}

	public boolean insertarRubro(Factura objfactura, Cuenta objcuenta,
			String idRubro) {
		String s_retorno = null;
		boolean rubroEPS = false;
		Double valorNCcomis = 0.0;
		String nombreRubro = facturaDao.consultarNombreRubro(idRubro);
		nombreRubro = (nombreRubro == null) ? "Sin Nombre" : nombreRubro;

		if (objfactura.getIdTipoSOl() == 3) {
			objfactura.setValorNeto((double) Math.round(objfactura
					.getValorNeto() * 100 / 110));
			objfactura.setNotaCredito((double) Math.round(objfactura
					.getNotaCredito() * 100 / 110));
			rubroEPS = true;
		}

		if (objfactura.getValorNeto() > 0) {

			Long ConsecutivoRubro = facturaDao.maxConsecutivoRubro(
					objcuenta.getIdCuenta(), objfactura.getIdFactura());

			try {

				facturaDao.insertarRubro(objcuenta.getIdCuenta(),
						objfactura.getIdFactura(), ++ConsecutivoRubro, idRubro,
						nombreRubro, objfactura.getValorNeto(),
						objfactura.getValorIVA(), objfactura.getNotaCredito(),
						objcuenta.getUsuarioAud(), objcuenta.getMaquinaAud());

			} catch (Exception e) {
				s_retorno = "ERROR: La factura " + objfactura.getFactura()
						+ " presento un error al insertar el rubro " + idRubro
						+ ": " + nombreRubro;
				log.error("ERROR: La factura " + objfactura.getFactura()
						+ " presento un error al insertar el rubro " + idRubro
						+ ": " + nombreRubro + " " + e.getMessage());
				return false;
			}

			if (rubroEPS) {

				Parametros parametros = new Parametros();
				parametros = facturaDao.consultarNombreRubroComision();
				if (parametros.getDescripcion().length() > 0) {

					objfactura.setValorNeto((double) Math.round(objfactura
							.getValorNeto() * 0.1));
					valorNCcomis = (double) Math.round(objfactura
							.getNotaCredito() * 0.1);

					try {
						facturaDao.insertarRubro(objcuenta.getIdCuenta(),
								objfactura.getIdFactura(), ++ConsecutivoRubro,
								parametros.getValor(),
								parametros.getDescripcion(),
								objfactura.getValorNeto(), 0.0, valorNCcomis,
								objcuenta.getUsuarioAud(),
								objcuenta.getMaquinaAud());

					} catch (Exception e) {
						s_retorno = "ERROR: La factura "
								+ objfactura.getFactura()
								+ " presento un error al insertar el rubro EPS, "
								+ idRubro + ": " + nombreRubro;
						log.error("ERROR: La factura "
								+ objfactura.getFactura()
								+ " presento un error al insertar el rubro EPS, "
								+ idRubro + ": " + nombreRubro + " "
								+ e.getMessage());
						return false;
					}
				}
			} else {
				return true;
			}
		} else {
			s_retorno = "ERROR: La factura "
					+ objfactura.getFactura()
					+ ", presento un error al insertar los rubros, el valor neto presenta valores negativos";
			return false;
		}

		return true;
	}

	public Boolean actualizarAUD(String AUD, Factura objfactura) {
		String s_retorno = null;
		if (AUD.equals("N")) {
			// objfactura.setIndEstadoFactura("IS");
			if (!objfactura.getIdAuditorConcurrente().trim().equals("")) {
				s_retorno = "ERROR: Si el parametro ps_visado = N no puede existir valor en el campo id_auditor_concurrente";
				return false;
			}

			return true;
		} else if (AUD.equals("S")) {
			objfactura.setIndEstadoFactura("IC");
			if (objfactura.getIdAuditorConcurrente().isEmpty()
					|| objfactura.getIdAuditorConcurrente().length() == 0) {
				s_retorno = "ERROR: Si el parametro ps_visado = S no puede venir vacio el campo id_auditor_concurrente";
				return false;
			}
			return true;
		} else {
			s_retorno = "ERROR: El parametro ps_visado no cuenta con los valores establecidos (S o N)";
			return false;
		}
	}

	public void setEstadoFactura(Factura objfactura) {
		// try {
		objfactura.setIndVRCertificado(0);
		objfactura.setReconReserva("P");
		objfactura.setCodSAP(SAPDAO.consultarCodigoSAP(
				objfactura.getIdDepartamento(), objfactura.getIdMunicipio()));

		validarFactura(objfactura);

		objfactura.setIdTipoSOl(facturaDao.consultaridTipoSol(objfactura));

		if (objfactura.getIdTipoSOl() == null
				|| objfactura.getIdTipoSOl().equals(null)
				|| objfactura.getIdTipoSOl() == 0) {
			if (Reemb.equals("S")) {
				if (cuenta.getTipoPersona().equals("PN")) {
					objfactura.setIdTipoSOl(1);
				} else {
					objfactura.setIdTipoSOl(4);
				}
			} else {
				objfactura.setIdTipoSOl(2);
			}
		}

		objfactura.setIdSucursal(facturaDao.consultarSucursalDM(
				objfactura.getIdTipoDocProv(), objfactura.getIdProveedor(),
				objfactura.getIdDepartamento(), objfactura.getIdMunicipio()));

		if (objfactura.getIdSucursal() == null
				|| objfactura.getIdSucursal().equals(null)) {

			objfactura
					.setIdSucursal(facturaDao.consultarSucursal(
							objfactura.getIdTipoDocProv(),
							objfactura.getIdProveedor()));
		}

		if (objfactura.getIdSucursal() == null
				|| objfactura.getIdSucursal().equals(null)) {

			objfactura.setIdSucursal(0l);
		}

		objfactura.setCodSucursal(facturaDao.consultarCODSISE(
				objfactura.getIdDepartamento(), objfactura.getIdMunicipio()));

		if (objfactura.getCodSucursal() == 0) {
			objfactura.setCodSucursal(80);
		}

		objfactura.setNombreDX(facturaDao.consultarNombreDiagnostico(objfactura
				.getIdDX()));
		if (objfactura.getNombreDX() == null) {
			objfactura.setNombreDX("");
		}

		// } catch (Exception e) {
		// log.error(e.getClass());
		// }
	}

	public boolean actualizarVrCuenta(Cuenta objcuenta, Factura ojbfactura,
			Long consecutivo) {
		String s_retorno = null;
		try {
			facturaDao.actualizarValorCuenta(objcuenta);
		} catch (Exception e) {
			s_retorno = "ERROR: La factura "
					+ ojbfactura.getFactura()
					+ " presento un error al actualizar el valor total de la cuenta (VALOR_NETO - NOTA_CREDITO)";
			logSYC.actualizarSYC(s_retorno, objcuenta.getIdCuenta(),
					ojbfactura.getIdFactura(), consecutivo);
			log.error(e.getClass());
			return false;
		}

		return true;
	}

	public void inactivarRubro(Long idcuenta, Long idfactura) {
		facturaDao.inactivarRubro(idcuenta, idfactura);
	}

	public void inactivarAuditoria(Long idcuenta, Long idfactura) {
		facturaDao.inactivarAuditoria(idcuenta, idfactura);
	}

	public void inactivarPago(Long idcuenta, Long idfactura) {

		facturaDao.inactivarPago(idcuenta, idfactura);
	}

	public void validarFactura(Factura objFactura) {
		Long validadProv = 0l;
		boolean proveedor = true, siniestro = true;

		if (Reemb.equals("N")) {
			validadProv = facturaDao.existeProveedor(
					objFactura.getIdTipoDocProv(), objFactura.getIdProveedor());
			if (validadProv == 0) {
				proveedor = false;
				throw new RuntimeException(
						"ERROR: El proveedor con el valor de los campos id_tipo_doc_prov y id_proveedor no se encontro en la tabla PA_MA_PROVEEDOR");
			}
		}

		Long afiliado = facturaDao.existeSiniestro(objFactura);
		if (afiliado == null) {
			objFactura.setIdFuratFurep(null);
			siniestro = false;
			/* Validación de la existencia del siniestro */
			throw new RuntimeException(
					"ERROR :El siniestro con los campos id_persona, id_tipo_doc, fecha_atep y id_siniestro no se encontro en la tabla ATEP_MA_SINIESTRO");
		}
		// if (siniestro == false || proveedor == false) {
		// if (objFactura.getAudConcurrente().equals("S")) {
		// objFactura.setIndEstadoFactura("IC");
		// } else {
		// objFactura.setIndEstadoFactura("IS");
		// }
		// } else {
		objFactura.setIdFuratFurep(afiliado);
		if (objFactura.getAudConcurrente().equals("S")) {
			objFactura.setIndEstadoFactura("CC");
			objFactura.setIndVRCertificado(-1);
		} else {
			objFactura.setIndEstadoFactura("CS");
		}
		// }
	}

	public boolean ingresarReembolso(Cuenta objCuenta, Factura objFactura,
			Long consecutivo) {
		String s_retorno = null;
		try {
			facturaDao.insertarReembolso(objCuenta);
		} catch (Exception e) {
			s_retorno = "ERROR: La factura presento un error al insertar la informacion de reembolso";
			logSYC.actualizarSYC(s_retorno, objCuenta.getIdCuenta(),
					objFactura.getIdFactura(), consecutivo);
			log.error(e.getClass());
			return false;
		}
		return true;
	}

	public boolean validarXML2(String XML, String accion, int ps_multiusuario,
			String ps_radicador, String ps_usuario, String ps_maquina,
			String ps_visado) {
		String s_retorno = null;
		cuenta = new Cuenta();
		factura = new Factura();
		Date fechaActual = new Date();
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(XML)));
			listadoXML = doc.getElementsByTagName("Ejemplo_radicacion_row");

			if (listadoXML.getLength() > 0) {
				Element datos = (Element) listadoXML.item(0);

				String valor = null;

				/* id_cuenta */
				valor = datos.getElementsByTagName("id_cuenta").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_cuenta es requerido";
					return false;
				} else {
					try {
						cuenta.setIdCuenta(Long.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_cuenta debe ser numerico";
						return false;
					}
				}

				/* id_factura */
				valor = datos.getElementsByTagName("id_factura").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_factura es requerido";
					return false;
				} else {
					try {
						factura.setIdFactura(Long.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_factura debe ser numerico";
						return false;
					}
				}

				cuenta.setNumeroFacturas(ps_multiusuario);
				cuenta.setUsuarioAud(ps_usuario);
				cuenta.setMaquinaAud(ps_maquina);
				factura.setAudConcurrente(ps_visado);
				factura.setIdRadicador(ps_radicador);

				/* id_siniestro */
				valor = datos.getElementsByTagName("id_siniestro").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_siniestro es requerido";
					return false;
				} else {
					try {
						factura.setIdFuratFurep(Long.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_siniestro debe ser numerico";
						return false;
					}
				}

				/* factura */
				valor = datos.getElementsByTagName("factura").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo factura es requerido";
					return false;
				} else {
					factura.setFactura(valor);
				}

				/* fecha_servicio */
				valor = datos.getElementsByTagName("fecha_servicio").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo fecha_servicio es requerido";
					return false;
				} else {
					try {
						factura.setFechaServicio(formatoFecha.parse(valor));
						if (factura.getFechaServicio().getTime() > fechaActual
								.getTime()) {
							s_retorno = "El valor del campo fecha_servicio no puede superar la fecha actual";
							return false;
						}
					} catch (ParseException e) {
						s_retorno = "El formato de fecha para el campo fecha_servicio debe ser yyyy-MM-dd HH:mm:ss";
						return false;
					}
				}

				/* fecha_factura */
				valor = datos.getElementsByTagName("fecha_factura").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo fecha_factura es requerido";
					return false;
				} else {
					try {
						factura.setFechaFactura(formatoFecha.parse(valor));
						if (factura.getFechaServicio().getTime() > fechaActual
								.getTime()) {
							s_retorno = "El valor del campo fecha_factura no puede superar la fecha actual";
							return false;
						}
					} catch (ParseException e) {
						s_retorno = "El formato de fecha para el campo fecha_factura debe ser yyyy-MM-dd HH:mm:ss";
						return false;
					}
				}

				/* numero_autorizacion */
				valor = datos.getElementsByTagName("numero_autorizacion")
						.item(0).getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo numero_autorizacion es requerido";
					return false;
				} else {
					factura.setNumeroAutorizacion(valor);
				}

				/* fecha_autorizacion */
				valor = datos.getElementsByTagName("fecha_autorizacion")
						.item(0).getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo fecha_autorizacion es requerido";
					return false;
				} else {
					try {
						factura.setFechaAutorizacion(formatoFecha.parse(valor));
						if (factura.getFechaAutorizacion().getTime() > fechaActual
								.getTime()) {
							s_retorno = "El valor del campo fecha_autorizacion no puede superar la fecha actual";
							return false;
						}
					} catch (ParseException e) {
						s_retorno = "El formato de fecha para el campo fecha_autorizacion debe ser yyyy-MM-dd HH:mm:ss";
						return false;
					}
				}

				/* id_tipo_doc_emp */
				valor = datos.getElementsByTagName("id_tipo_doc_emp").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_tipo_doc_emp es requerido";
					return false;
				} else {
					if (valor.equals("CC") || valor.equals("NI")
							|| valor.equals("CE") || valor.equals("CD")
							|| valor.equals("NU") || valor.equals("PA")
							|| valor.equals("PE") || valor.equals("RC")
							|| valor.equals("SC") || valor.equals("TI")) {
						factura.setIdTipoDocEmpresa(valor);
					} else {
						s_retorno = "El campo id_tipo_doc_emp debe ser CC, NI o CE";
						return false;
					}
				}

				/* id_empresa */
				valor = datos.getElementsByTagName("id_empresa").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_empresa es requerido";
					return false;
				} else {
					try {
						Long.parseLong(valor);
						factura.setIdEmpresa(valor);
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_empresa debe ser numerico";
						return false;
					}
				}

				/* id_tipo_doc */
				valor = datos.getElementsByTagName("id_tipo_doc").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_tipo_doc es requerido";
					return false;
				} else {
					if (valor.equals("CC") || valor.equals("NI")
							|| valor.equals("CE") || valor.equals("CD")
							|| valor.equals("NU") || valor.equals("PA")
							|| valor.equals("PE") || valor.equals("RC")
							|| valor.equals("SC") || valor.equals("TI")) {
						factura.setIdTipoDoc(valor);
					} else {
						s_retorno = "El campo id_tipo_doc debe ser CC, TI o CE";
						return false;
					}
				}

				/* id_persona */
				valor = datos.getElementsByTagName("id_persona").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_persona es requerido";
					return false;
				} else {
					try {
						Long.valueOf(valor);
						factura.setIdPersona(valor);
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_persona debe ser numerico";
						return false;
					}
				}

				/* fecha_atep */
				valor = datos.getElementsByTagName("fecha_atep").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo fecha_atep es requerido";
					return false;
				} else {
					try {
						factura.setFechaAtep(formatoFecha.parse(valor));
						if (factura.getFechaAtep().getTime() > fechaActual
								.getTime()) {
							s_retorno = "El valor del campo fecha_atep no puede superar la fecha actual";
							return false;
						}
					} catch (ParseException e) {
						s_retorno = "El formato de fecha para el campo fecha_atep debe ser yyyy-MM-dd HH:mm:ss";
						return false;
					}
				}

				/* Evento */
				valor = datos.getElementsByTagName("evento").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo evento es requerido";
					return false;
				} else {
					if (valor.equals("AT") || valor.equals("EP")) {
						factura.setTipoSiniestro(valor);
					} else {
						s_retorno = "El campo evento debe ser AT o EP";
						return false;
					}
				}

				/* valor_neto */
				valor = datos.getElementsByTagName("valor_neto").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo valor_neto es requerido";
					return false;
				} else {
					try {
						factura.setValorNeto(Double.valueOf(valor));
						if (factura.getValorNeto() <= 0) {
							s_retorno = "El campo valor_neto debe ser mayor a 0";
							return false;
						}
					} catch (NumberFormatException e) {
						s_retorno = "El campo valor_neto debe ser numerico";
						return false;
					}
				}

				/* valor_iva */
				valor = datos.getElementsByTagName("valor_iva").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo valor_iva es requerido";
					return false;
				} else {
					try {
						factura.setValorIVA(Double.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo valor_iva debe ser numerico";
						return false;
					}
				}

				/* valor_nc */
				valor = datos.getElementsByTagName("valor_nc").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo valor_nc es requerido";
					return false;
				} else {
					try {
						factura.setNotaCredito(Double.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo valor_nc debe ser numerico";
						return false;
					}
				}

				/* id_rubro */
				valor = datos.getElementsByTagName("id_rubro").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_rubro es requerido";
					return false;
				} else {
					try {
						Long.parseLong(valor);
						idRubro = valor;
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_rubro debe ser numerico";
						return false;
					}
				}

				/* id_seccional */
				valor = datos.getElementsByTagName("id_seccional").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo id_seccional es requerido";
					return false;
				} else {
					try {
						factura.setIdSeccional(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo id_seccional debe ser numerico";
						return false;
					}
				}

				/* cod_stikker */
				valor = datos.getElementsByTagName("cod_stikker").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo cod_stikker es requerido";
					return false;
				} else {
					try {
						factura.setCodSTIKKER(Long.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo cod_stikker debe ser numerico";
						return false;
					}
				}

				/* cod_departamento */
				valor = datos.getElementsByTagName("cod_departamento").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo cod_departamento es requerido";
					return false;
				} else {
					try {
						factura.setIdDepartamento(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo cod_departamento debe ser numerico";
						return false;
					}
				}

				/* cod_municipio */
				valor = datos.getElementsByTagName("cod_municipio").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo cod_municipio es requerido";
					return false;
				} else {
					try {
						factura.setIdMunicipio(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						s_retorno = "El campo cod_municipio debe ser numerico";
						return false;
					}
				}

				/* fecha_radicacion_factura */
				valor = datos.getElementsByTagName("fecha_radicacion_factura")
						.item(0).getTextContent();
				if (valor == null || valor.trim().equals("")) {
					s_retorno = "El campo fecha_radicacion_factura es requerido";
					return false;
				} else {
					try {
						factura.setFechaRadicacion(formatoFecha.parse(valor));
						if (factura.getFechaRadicacion().getTime() > fechaActual
								.getTime()) {
							s_retorno = "El valor del campo fecha_radicacion_factura no puede superar la fecha actual";
							return false;
						}
					} catch (ParseException e) {
						s_retorno = "El formato de fecha para el campo fecha_radicacion_factura debe ser yyyy-MM-dd HH:mm:ss";
						return false;
					}
				}

				factura.setIdAuditorConcurrente(datos
						.getElementsByTagName("id_auditor_concurrente").item(0)
						.getTextContent());

				/* reembolso */
				Reemb = datos.getElementsByTagName("reembolso").item(0)
						.getTextContent();
				if (Reemb == null || Reemb.trim().equals("")) {
					s_retorno = "El campo reembolso es requerido";
					return false;
				}

				switch (Reemb) {
				case "S":

					/* tipo_solicitante */
					valor = datos.getElementsByTagName("tipo_solicitante")
							.item(0).getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo tipo_solicitante es requerido";
						return false;
					} else {
						switch (valor) {
						case "EMP":
							/* dv_empresa */
							String DVEmpresa = datos
									.getElementsByTagName("dv_empresa").item(0)
									.getTextContent();
							if (DVEmpresa == null
									|| DVEmpresa.trim().equals("")) {
								s_retorno = "Si el campo el campo tipo_solicitante es EMP el campo dv_empresa es requerido";
								return false;
							}
							try {
								cuenta.setDvEmpresa(Integer.parseInt(DVEmpresa));
							} catch (Exception e) {
								s_retorno = "El campo dv_empresa debe ser numerico";
								return false;
							}
							break;
						case "TRA":
							/* dv_empresa */
							cuenta.setDvEmpresa(0);
							break;

						case "PRV":
							s_retorno = "El tipo de solicitante para Reembolso no puede ser PROVEEDOR (PRV)";
							return false;
						default:
							s_retorno = "El campo tipo_solicitante debe ser EMP, TRA o PRV";
							return false;

						}
						cuenta.setTipoSolicitante(valor);
					}

					/* ano_radicacion */
					// valor =
					// datos.getElementsByTagName("ano_radicacion").item(0).getTextContent();
					// if (valor == null || valor.trim().equals("")) {
					// s_retorno = "El campo ano_radicacion es requerido";
					// return false;
					// } else {
					// cuenta.setAnoRadicacion(valor);
					// }

					/* codigo_seccional */
					// valor =
					// datos.getElementsByTagName("codigo_seccional").item(0).getTextContent();
					// if (valor == null || valor.trim().equals("")) {
					// s_retorno = "El campo codigo_seccional es requerido";
					// return false;
					// } else {
					// try {
					// cuenta.setCodigoSeccional(Integer.parseInt(valor));
					// } catch (NumberFormatException e) {
					// s_retorno =
					// "El campo codigo_seccional debe ser numerico";
					// return false;
					// }
					// }

					/* codigo_pat */
					// valor =
					// datos.getElementsByTagName("codigo_pat").item(0).getTextContent();
					// if (valor == null || valor.trim().equals("")) {
					// s_retorno = "El campo codigo_pat es requerido";
					// return false;
					// } else {
					// try {
					// cuenta.setCodigoPAT(Long.valueOf(Integer.parseInt(valor)));
					// } catch (NumberFormatException e) {
					// s_retorno = "El campo codigo_pat debe ser numerico";
					// return false;
					// }
					// }

					/* numero_radicacion */

					// valor =
					// datos.getElementsByTagName("numero_radicacion").item(0).getTextContent();
					// if (valor == null || valor.trim().equals("")) {
					// s_retorno = "El campo numero_radicacion es requerido";
					// return false;
					// } else {
					// try {
					// cuenta.setNumeroRadicacion(Long.valueOf(Integer.parseInt(valor)));
					// } catch (NumberFormatException e) {
					// s_retorno =
					// "El campo numero_radicacion debe ser numerico";
					// return false;
					// }
					// }

					/* id_reembolso */
					valor = datos.getElementsByTagName("id_reembolso").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo id_reembolso es requerido";
						return false;
					} else {
						try {
							cuenta.setIdReembolso(Long.valueOf(Integer
									.parseInt(valor)));
						} catch (NumberFormatException e) {
							s_retorno = "El campo id_reembolso debe ser numerico";
							return false;
						}
					}

					/* razon_social */
					valor = datos.getElementsByTagName("razon_social").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo razon_social es requerido";
						return false;
					} else {
						cuenta.setRazonSocial(valor);
					}

					/* tipo_documento */
					valor = datos.getElementsByTagName("tipo_documento")
							.item(0).getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo tipo_documento es requerido";
						return false;
					} else {
						if (valor.equals("CC") || valor.equals("NI")
								|| valor.equals("CE") || valor.equals("CD")
								|| valor.equals("NU") || valor.equals("PA")
								|| valor.equals("PE") || valor.equals("RC")
								|| valor.equals("SC") || valor.equals("TI")) {
							cuenta.setTipoDocumento(valor);
						} else {
							s_retorno = "El campo tipo_documento debe ser CC, CE, TI o NI";
							return false;
						}
					}

					/* documento */
					valor = datos.getElementsByTagName("documento").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo documento es requerido";
						return false;
					} else {
						try {
							/**
							 * LJBT.2017.09.20
							 * cuenta.setIdReembolso(Long.valueOf
							 * (Integer.parseInt(valor)));
							 */
							cuenta.setDocumento(Long.valueOf(Integer
									.parseInt(valor)));
						} catch (NumberFormatException e) {
							s_retorno = "El campo documento debe ser numerico";
							return false;
						}
					}

					/* codigo_banco */
					valor = datos.getElementsByTagName("codigo_banco").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo codigo_banco es requerido";
						return false;
					} else {
						try {
							cuenta.setCodigoBanco(Integer.parseInt(valor));
							if (facturaDao.existeBanco(cuenta.getCodigoBanco()) == null) {
								s_retorno = "El valor del campo codigo_banco no se encuentra en la tabla GRAL_MA_BANCO";
								return false;
							}
						} catch (NumberFormatException e) {
							s_retorno = "El campo codigo_banco debe ser numerico";
							return false;
						}
					}

					/* naturaleza_cuenta */
					valor = datos.getElementsByTagName("naturaleza_cuenta")
							.item(0).getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo naturaleza_cuenta es requerido";
						return false;
					} else {
						cuenta.setNaturalezaCuenta(valor);
					}

					/* nombre_banco */
					valor = datos.getElementsByTagName("nombre_banco").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo nombre_banco es requerido";
						return false;
					} else {
						cuenta.setNombreBanco(valor);
					}

					/* tipo_cuenta */
					valor = datos.getElementsByTagName("tipo_cuenta").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo tipo_cuenta es requerido";
						return false;
					} else {
						if (valor.equals("A") || valor.equals("C")) {
							cuenta.setTipoCuenta(valor);
						} else {
							s_retorno = "El campo tipo_cuenta debe ser A o C";
							return false;
						}
					}

					/* numero_cuenta */
					valor = datos.getElementsByTagName("numero_cuenta").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo numero_cuenta es requerido";
						return false;
					} else {
						try {
							Long.parseLong(valor);
							cuenta.setNumeroCuenta(valor);
						} catch (NumberFormatException e) {
							s_retorno = "El campo numero_cuenta debe ser numerico";
							return false;
						}
					}

					/* sucursal_bancaria */
					/*
					 * LJBT.2017.10.5. INI MODIFICACION DE VALIDACION DE
					 * SUCURSAL BANCARIA
					 */
					/*
					 * valor =
					 * datos.getElementsByTagName("sucursal_bancaria").item
					 * (0).getTextContent();
					 * if(!datos.getElementsByTagName("id_reembolso"
					 * ).item(0).getTextContent().equals("S")){ if (valor ==
					 * null || valor.trim().equals("")) { s_retorno =
					 * "El campo sucursal_bancaria es requerido"; return false;
					 * } else { cuenta.setSucursalBancaria(valor); } }
					 */
					/*
					 * LJBT.2017.10.5. FIN MODIFICACION DE VALIDACION DE
					 * SUCURSAL BANCARIA
					 */

					/* tipo_persona */
					valor = datos.getElementsByTagName("tipo_persona").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo tipo_persona es requerido";
						return false;
					} else {
						if (valor.equals("PN") || valor.equals("PJ")) {
							cuenta.setTipoPersona(valor);
						} else {
							s_retorno = "El campo tipo_persona debe ser PN o PJ";
							return false;
						}

					}

					/* direccion */
					valor = datos.getElementsByTagName("direccion").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo direccion es requerido";
						return false;
					} else {
						cuenta.setDireccion(valor);
					}

					/* correo */
					valor = datos.getElementsByTagName("correo").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo correo es requerido";
						return false;
					} else {
						cuenta.setCorreo(valor);
					}

					/* celular */
					valor = datos.getElementsByTagName("celular").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo celular es requerido";
						return false;
					} else {
						cuenta.setCelular(valor);
					}

					/* telefono */
					valor = datos.getElementsByTagName("telefono").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo telefono es requerido";
						return false;
					} else {
						cuenta.setTelefono(valor);
					}

					factura.setIdTipoDocProv(null);
					factura.setIdProveedor(null);
					break;
				case "N":

					/* id_tipo_doc_prov */
					valor = datos.getElementsByTagName("id_tipo_doc_prov")
							.item(0).getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo id_tipo_doc_prov es requerido";
						return false;
					} else {
						if (valor.equals("CC") || valor.equals("NI")
								|| valor.equals("CE") || valor.equals("CD")
								|| valor.equals("NU") || valor.equals("PA")
								|| valor.equals("PE") || valor.equals("RC")
								|| valor.equals("SC") || valor.equals("TI")) {
							factura.setIdTipoDocProv(valor);
						} else {
							s_retorno = "El campo id_tipo_doc_prov debe ser CC, NI o CE";
							return false;
						}
					}

					/* id_proveedor */
					valor = datos.getElementsByTagName("id_proveedor").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "El campo id_proveedor es requerido";
						return false;
					} else {
						try {
							Long.valueOf(valor);
							factura.setIdProveedor(valor);
						} catch (NumberFormatException e) {
							s_retorno = "El campo id_proveedor debe ser numerico";
							return false;
						}

					}

					/* prefijo_factura */
					valor = datos.getElementsByTagName("prefijo_factura")
							.item(0).getTextContent();

					factura.setPrefijoFactura(valor);

					/* id_dx */
					valor = datos.getElementsByTagName("id_dx").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "Si el campo reembolso es N el campo id_dx es requerido";
						return false;
					}

					/* cod_prestador */
					valor = datos.getElementsByTagName("cod_prestador").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						s_retorno = "Si el campo reembolso es N el campo cod_prestador es requerido";
						return false;
					} else {
						try {
							Integer.parseInt(valor);
						} catch (NumberFormatException e) {
							s_retorno = "El campo cod_prestador debe ser numerico";
							return false;
						}

					}
					break;
				default:
					s_retorno = "El campo reembolso no cumple con el formato establecido (S o N)";
					return false;
				}

				/* id_dx */
				factura.setIdDX(datos.getElementsByTagName("id_dx").item(0)
						.getTextContent());

				factura.setValorBruto((factura.getValorNeto())
						- (factura.getNotaCredito()));

				if (idRubro.equals("") || factura.getIdPersona().equals("")
						|| factura.getIdTipoDoc().equals("")
						|| factura.getIdEmpresa().equals("")
						|| factura.getIdTipoDocEmpresa().equals("")
						|| factura.getFactura().equals("")
						|| factura.getTipoSiniestro().equals("")
						|| factura.getIdFuratFurep().equals("")) {
					s_retorno = " se encontraron parametros vacios que son indispensables, id_rubro, id_persona, id_tipo_doc, id_empresa, id_tipo_doc_emp, factura, id_siniestro o evento ";

					return false;
				}

			} else {
				s_retorno = " Existen errores en el formato del XML enviado";

				return false;
			}
		} catch (NumberFormatException e) {
			s_retorno = " Verifique los campos numericos en el XML, no se puede convertir un valor nulo o texto a un numero";

			return false;
		} catch (Exception e) {
			log.error(e.getClass());
			s_retorno = " Existen errores en el formato del XML enviado";
			return false;
		}
		return true;
	}
}
