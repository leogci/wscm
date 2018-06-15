package com.gci.siarp.cuentasMedicas.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.AuditoriaSYCDao;
import com.gci.siarp.cuentasMedicas.dao.FacturaDao;
import com.gci.siarp.cuentasMedicas.domain.AuditoriaSYC;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.cuentasMedicas.domain.RetornoAuditoria;
import com.gci.siarp.cuentasMedicas.domain.Rubro;
import com.gci.siarp.cuentasMedicas.domain.XMLAuditoria;
import com.gci.siarp.generales.service.ReservasService;
import com.vaadin.server.ServiceException;

@Log4j
@SiarpService
public class ImportaAuditoria {

	@Autowired
	FacturaDao facturaDao;

	@Autowired
	AuditoriaSYCDao auditoriaSYCDao;
	
	@Autowired 
	ReservasService reservaService;

	@Autowired
	LogSYC logSYC;

	private NodeList listadoXML;
	private XMLAuditoria xmlAuditoria;
	private Factura factura;
	private List<Rubro> listaRubros;
	//public String retorno;
	// SimpleDateFormat formatoFecha = new
	// SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");
	SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*public String getRetorno() {
		return retorno;
	}*/

	@Transactional(value = SiarpDatabase.transactionManagerBeanName)
	public String registrarAuditoria(String ps_archivo, String ps_usuario,
			Date ps_fecha, String ps_maquina) {
		Integer vrGlosa;
		//boolean validacionXML = obtenerInformacionXML();
		RetornoAuditoria validacionXML = obtenerInformacionXML();
		AuditoriaSYC AudSYC = new AuditoriaSYC();
		AudSYC.setIdRadicado(xmlAuditoria.getId_cuenta());
		AudSYC.setFechaAuditoria(new Date());// Pendiente del XML?
		AudSYC.setXmlRecibido(ps_archivo);
		AudSYC.setFechaModificacionAud(new Date());
		AudSYC.setUsuarioAud(ps_usuario);
		AudSYC.setMaquinaAud(ps_maquina);
		AudSYC.setIdFactura(xmlAuditoria.getId_factura());

		if (xmlAuditoria.getId_cuenta() != null
				&& xmlAuditoria.getId_factura() != null) {
			try {
				logSYC.insertarAuditoriaSYC(AudSYC);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getClass());
			}
		} else {
			return "ERROR: En el archivo XML ha presentado un error en los campos id_cuenta e id_factura";
		}

		if (!validacionXML.isBandera()) {
			AudSYC.setRetornoSiarp(validacionXML.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			return "ERROR: " + validacionXML.getRetorno();
		}

		try {
			validacionesExtra(xmlAuditoria);
		} catch (RuntimeException e) {
			return e.getMessage();
		}

		vrGlosa = facturaDao.existeGlosa(xmlAuditoria.getId_cuenta(),
				xmlAuditoria.getId_factura());

		// if (xmlAuditoria.getValor_glosado() > 0 && vrGlosa >= 1) {
		if (xmlAuditoria.getEstado_auditoria().equals("G") && vrGlosa >= 1) {
			validacionXML.setRetorno("ERROR: No puede registrarse otra glosa (solo sustentaciones) para la factura");
			AudSYC.setRetornoSiarp(validacionXML.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			return validacionXML.getRetorno();
		}

		factura = facturaDao.getFacturaById(xmlAuditoria.getId_cuenta(),
				xmlAuditoria.getId_factura(), xmlAuditoria.getFactura());

		if (!(factura == null)) {

			if (factura.getIndEstadoFactura() == null
					|| !(factura.getIndEstadoFactura().equals("GP") || factura
							.getIndEstadoFactura().equals("CS"))) {
				validacionXML.setRetorno("ERROR: La factura tiene el estado "
						+ factura.getIndEstadoFactura()
						+ ", no se puede recibir auditoria");
				AudSYC.setRetornoSiarp(validacionXML.getRetorno());
				logSYC.actualizarAuditoriaSYC(AudSYC);
				return validacionXML.getRetorno();

			}

			// if(!(factura.getIdProveedor().equals(xmlAuditoria.getId_proveedor()))){
			// retorno =
			// "El campo proveedor es diferente en la factura al recibido en el XML";
			// AudSYC.setRetornoSiarp(retorno);
			// logSYC.actualizarAuditoriaSYC(AudSYC);
			// return retorno;
			// }

			String nombreGlosa = facturaDao.nombreGlosa(xmlAuditoria
					.getCodigo_glosa());
			String rubroComision = facturaDao.valorRubroComision();

			if (xmlAuditoria.getValor_aprobado() == (factura.getValorNeto() - factura
					.getNotaCredito())) {

				double valorGlosa = 0;
				facturaDao.actualizarEstadoFactura(xmlAuditoria.getId_cuenta(),
						xmlAuditoria.getId_factura(), "AC", valorGlosa);
				validacionXML.setRetorno("EXITO: El valor Aprobado es igual al valor de la factura, se ha actualizado el estado de la factura como ACEPTADA");
				AudSYC.setRetornoSiarp(validacionXML.getRetorno());

				logSYC.actualizarAuditoriaSYC(AudSYC);
				return validacionXML.getRetorno();

			} else {

				if (xmlAuditoria.getEstado_auditoria().equals("A")) {
					return "Si el campo estado_auditoria es A, el valor de la factura debe ser igual al valor aprobado.";
				}

				if (xmlAuditoria.getEstado_auditoria().equals("D")
						&& (xmlAuditoria.getValor_sustentado() > 0 || xmlAuditoria.getValor_glosado() > 0 )) {
					try {
						if ((registrarGlosaDefinitiva(AudSYC, rubroComision,
								nombreGlosa, ps_usuario, ps_fecha, ps_maquina))) {
							validacionXML.setRetorno("EXITO: Se registraron correctamente los datos de la Glosa Definitiva.");
							AudSYC.setRetornoSiarp(validacionXML.getRetorno());
						} else {
							return validacionXML.getRetorno();
						}

					} catch (Exception e) {
						return "ERROR: Se produjo un error al insertar la Glosa Definitiva "
								+ e.getClass();
					}
				} /*else {
					retorno = "ERROR: Se produjo un error al insertar la Glosa Definitiva por valor sustentado.";
					AudSYC.setRetornoSiarp(retorno);
					return retorno;
				}*/

				// if (xmlAuditoria.getValor_glosado() > 0) {
				if (xmlAuditoria.getEstado_auditoria().equals("G")
						&& xmlAuditoria.getValor_glosado() > 0) {

					try {
						if ((registrarGlosa(AudSYC, rubroComision, nombreGlosa,
								ps_usuario, ps_fecha, ps_maquina))) {
							validacionXML.setRetorno("EXITO: Se registraron correctamente los datos de la glosa");
							AudSYC.setRetornoSiarp(validacionXML.getRetorno());
						} else {
							return validacionXML.getRetorno();
						}

					} catch (Exception e) {
						return "ERROR: Se produjo un error al insertar la Glosa "
								+ e.getClass();
					}

					// } else if (xmlAuditoria.getValor_sustentado() > 0) {
				} else if (xmlAuditoria.getEstado_auditoria().equals("S")
						&& xmlAuditoria.getValor_sustentado() >= 0) {
					if (registrarSustentacion(AudSYC, rubroComision,
							nombreGlosa, ps_usuario, ps_fecha, ps_maquina)) {
						validacionXML.setRetorno("EXITO: Se registr  aron correctamente los datos de la Sustentacion");
						AudSYC.setRetornoSiarp(validacionXML.getRetorno());
					} else {
						return validacionXML.getRetorno();
					}

				} else {
					if (!xmlAuditoria.getEstado_auditoria().equals("D")) {
						validacionXML.setRetorno("ERROR: El valor de los campos valor_sustentado y valor_glosado no pueden ser menores o iguales a 0");
						AudSYC.setRetornoSiarp(validacionXML.getRetorno());
						logSYC.actualizarAuditoriaSYC(AudSYC);
						return validacionXML.getRetorno();
					}
				}
			}

			try {
				String estadoFactura = facturaDao.consultarCambioEstado(
						xmlAuditoria.getId_cuenta(),
						xmlAuditoria.getId_factura());

				if (!(estadoFactura.equals("ERROR"))) {
					double valorGlosa = xmlAuditoria.getValor_glosado()
							- xmlAuditoria.getValor_sustentado();
					facturaDao.actualizarEstadoFactura(
							xmlAuditoria.getId_cuenta(),
							xmlAuditoria.getId_factura(), estadoFactura,
							valorGlosa);
				} else {
					return "ERROR: No se pudo actualizar el estado de la factura, debido a que se encuentra en estado ="
							+ estadoFactura
							+ " posiblemente al calcular valor certificado o al realizar el pago";
				}

			} catch (Exception e) {
				// retorno =
				// "ERROR: no se pudo actualizar el estado de la Factura";
				validacionXML.setRetorno("ERROR: Se ha producido un error general al intentar actualizar el estado de la factura, consulte el Log para mas informacion");
				AudSYC.setRetornoSiarp(validacionXML.getRetorno());
				logSYC.actualizarAuditoriaSYC(AudSYC);
				log.error(e.getClass());
				log.error(e.getMessage());
				return validacionXML.getRetorno();
			}

		} else {
			validacionXML.setRetorno("ERROR: La Factura con los campos id_cuenta, id_factura, y factura no existen en la tabla PA_MV_FACTURA");
			AudSYC.setRetornoSiarp(validacionXML.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			return validacionXML.getRetorno();
		}

		logSYC.actualizarAuditoriaSYC(AudSYC);
		return validacionXML.getRetorno();

	}

	public void validacionesExtra(XMLAuditoria xmlAuditoria) {

		/* Validacion Existencia del afiliado */
		if (!facturaDao.existeAfiliado(xmlAuditoria.getId_tipo_doc(),
				xmlAuditoria.getId_persona())) {
			throw new RuntimeException(
					"ERROR: El afiliado con los campos id_persona, id_tipo_doc no se encontro en la tabla GRAL_MA_PERSONA");
		}

		/* Validacion diagnostico */
		if (!facturaDao.existeDiagnostico(xmlAuditoria.getId_dx())) {
			throw new RuntimeException(
					"ERROR: El diagnostico con el valor del campo id_dx no se encontro en la tabla GRAL_PM_DIAGNOSTICO");
		}

		/* Validacion existencia de la cuenta */

		if (!facturaDao.existeCuenta(xmlAuditoria.getId_cuenta())) {
			throw new RuntimeException(
					"ERROR: La Cuenta con el valor del campo id_cuenta no se encontro en la tabla PA_MA_CUENTA");
		}
	}

	public boolean registrarGlosa(AuditoriaSYC AudSYC, String rubroComision,
			String nombreGlosa, String ps_usuario, Date ps_fecha,
			String ps_maquina) {
		
		RetornoAuditoria ret = new RetornoAuditoria();
		if ((factura.getValorNeto() - factura.getNotaCredito()) != (xmlAuditoria
				.getValor_aprobado() + xmlAuditoria.getValor_glosado())) {
			ret.setRetorno("ERROR: El valor aprobado mas el valor de la glosa no coinciden con el valor de la factura");
			ret.setBandera(false);
			AudSYC.setRetornoSiarp(ret.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			return ret.isBandera();
		}

		int Control = 0;

		listaRubros = facturaDao.buscarRubros(xmlAuditoria.getId_cuenta(),
				xmlAuditoria.getId_factura());

		double TotalGlosa = xmlAuditoria.getValor_glosado()
				- (xmlAuditoria.getValor_sustentado() == null ? 0
						: xmlAuditoria.getValor_sustentado());

		Integer maxConsecutivoGlosa = facturaDao.maxConsecutivoGlosa(
				factura.getIdCuenta(), factura.getIdFactura());
		
		switch (listaRubros.size()) {

		case 1:// NO ES EPS (SOLO 1 RUBRO)
			Rubro r = listaRubros.get(0);
			try {
				facturaDao.actualizarRubro(xmlAuditoria.getCodigo_glosa(),
						nombreGlosa, TotalGlosa, ps_fecha, ps_usuario,
						ps_maquina, factura.getIdCuenta(),
						factura.getIdFactura(), r.getIdRubro());

			} catch (Exception e) {

				ret.setRetorno("ERROR: no se puede actualizar la informacion del Rubro "
						+ r.getIdRubro()
						+ " verifique que los campos codigo_glosa, valor_sustentado, valor_glosado, id_factura esten correctos");
				ret.setBandera(false);
				AudSYC.setRetornoSiarp(ret.getRetorno());
				logSYC.actualizarAuditoriaSYC(AudSYC);
				log.error(e.getClass());
				return ret.isBandera();
			}

			try {

				facturaDao.insertarGlosa(factura.getIdCuenta(),
						factura.getIdFactura(), maxConsecutivoGlosa + 1,
						xmlAuditoria.getCodigo_glosa(), r.getIdRubro(),
						xmlAuditoria.getValor_glosado(), nombreGlosa, ps_fecha,
						ps_usuario, ps_maquina, xmlAuditoria.getInd_glosa(),
						xmlAuditoria.getCod_respuesta(), new Date(),
						xmlAuditoria.getValor_sustentado(),
						xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
				facturaDao.actualizarIVARubro(factura.getIdCuenta(),
						factura.getIdFactura(), maxConsecutivoGlosa + 1,
						xmlAuditoria.getCodigo_glosa(), r.getIdRubro(),
						xmlAuditoria.getValor_glosado(), nombreGlosa,
						ps_fecha, ps_usuario, ps_maquina,
						xmlAuditoria.getInd_glosa(),
						xmlAuditoria.getCod_respuesta(), new Date(),
						null, xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
			} catch (Exception e) {
				ret.setRetorno("ERROR: No se puede insertar la informacion de la glosa, verifique los campos id_cuenta, id_factura, codigo_glosa, valor_glosado, ind_glosa, codRespuesta y valor_sustentado");
				ret.setBandera(false);
				AudSYC.setRetornoSiarp(ret.getRetorno());
				logSYC.actualizarAuditoriaSYC(AudSYC);
				log.error(e.getClass());
				e.printStackTrace();
				return ret.isBandera();
			}
			break;

		case 2:// ES EPS (MAX 2 RUBROS)
			double TotalGlosaRubro,
			valorSustentado,
			valorGlosa;

			for (Rubro rubro : listaRubros) {
				maxConsecutivoGlosa = maxConsecutivoGlosa + 1;
				if (rubro.getIdRubro().equals(rubroComision)) {

					TotalGlosaRubro = ((double) Math.round(TotalGlosa / 11));
					valorGlosa = ((double) Math.round(xmlAuditoria
							.getValor_glosado() / 11));
					valorSustentado = ((double) Math.round(xmlAuditoria
							.getValor_sustentado() / 11));

				} else {
					TotalGlosaRubro = ((double) Math.round(TotalGlosa / 1.1));
					valorGlosa = ((double) Math.round(xmlAuditoria
							.getValor_glosado() / 1.1));
					valorSustentado = ((double) Math.round(xmlAuditoria
							.getValor_sustentado() / 1.1));

				}

				try {
					facturaDao.actualizarRubro(xmlAuditoria.getCodigo_glosa(),
							nombreGlosa, TotalGlosaRubro, ps_fecha, ps_usuario,
							ps_maquina, xmlAuditoria.getId_cuenta(),
							xmlAuditoria.getId_factura(), rubro.getIdRubro());
				} catch (Exception e) {
					ret.setRetorno("ERROR: no se puede actualizar la informacion del Rubro "
							+ rubro.getIdRubro()
							+ " verifique que los campos codigo_glosa, valor_sustentado, valor_glosado, id_factura esten correctos");
					ret.setBandera(false);
					AudSYC.setRetornoSiarp(ret.getRetorno());
					logSYC.actualizarAuditoriaSYC(AudSYC);
					log.error(e.getClass());
					return ret.isBandera();
				}

				try {
					// xmlAuditoria.getFecha_auditoria()

					facturaDao
							.insertarGlosa(factura.getIdCuenta(),
									factura.getIdFactura(),
									maxConsecutivoGlosa,
									xmlAuditoria.getCodigo_glosa(),
									rubro.getIdRubro(), valorGlosa,
									nombreGlosa, ps_fecha, ps_usuario,
									ps_maquina, xmlAuditoria.getInd_glosa(),
									xmlAuditoria.getCod_respuesta(),
									new Date(), valorSustentado,
									xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
				} catch (Exception e) {

					ret.setRetorno("ERROR: No se puede insertar la informacion de la glosa, verifique los campos id_cuenta, id_factura, codigo_glosa, valor_glosado, ind_glosa, codRespuesta y valor_sustentado");
					ret.setBandera(false);
					AudSYC.setRetornoSiarp(ret.getRetorno());
					logSYC.actualizarAuditoriaSYC(AudSYC);
					log.error(e.getClass());
					return ret.isBandera();
				}

			}
			break;

		default:
			ret.setRetorno((listaRubros.size() == 0) ? "ERROR: No existen rubros registrados para la factura"
					: "ERROR: La factura tiene mas de 2 Rubros y no se puede procesar");
			ret.setBandera(false);
			AudSYC.setRetornoSiarp(ret.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			Control = 1;
			break;
		}
		return Control == 0;
	}

	private boolean registrarGlosaDefinitiva(AuditoriaSYC AudSYC,
			String rubroComision, String nombreGlosa, String ps_usuario,
			Date ps_fecha, String ps_maquina) {
		RetornoAuditoria ret = new RetornoAuditoria();
		Integer maxConsecutivoGlosa = facturaDao.maxConsecutivoGlosa(
				factura.getIdCuenta(), factura.getIdFactura());
		Double valorPendiente = facturaDao.obtenerValorPendiente(
				factura.getIdCuenta(), factura.getIdFactura());
		int Control = 0;
		if (valorPendiente.equals(new Double(xmlAuditoria.getValor_sustentado()
				+ xmlAuditoria.getValor_glosado()))) {
			listaRubros = facturaDao.buscarRubros(xmlAuditoria.getId_cuenta(),
					xmlAuditoria.getId_factura());
			switch (listaRubros.size()) {
			case 1:// NO ES EPS (SOLO 1 RUBRO)
				try {

					Rubro r = listaRubros.get(0);

					facturaDao.insertarGlosa(factura.getIdCuenta(),
							factura.getIdFactura(), maxConsecutivoGlosa + 1,
							xmlAuditoria.getCodigo_glosa(), r.getIdRubro(),
							xmlAuditoria.getValor_glosado(), nombreGlosa,
							ps_fecha, ps_usuario, ps_maquina,
							xmlAuditoria.getInd_glosa(),
							xmlAuditoria.getCod_respuesta(), new Date(),
							valorPendiente, xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
					
					facturaDao.actualizarIVARubro(factura.getIdCuenta(),
							factura.getIdFactura(), maxConsecutivoGlosa + 1,
							xmlAuditoria.getCodigo_glosa(), r.getIdRubro(),
							xmlAuditoria.getValor_glosado(), nombreGlosa,
							ps_fecha, ps_usuario, ps_maquina,
							xmlAuditoria.getInd_glosa(),
							xmlAuditoria.getCod_respuesta(), new Date(),
							valorPendiente, xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
				} catch (Exception e) {
					ret.setRetorno("ERROR: no se pudo insertar la GLOSA DEFINITIVA, verifique los campos id_factura, id_cuenta, codigo_glosa, valor aprobado, valor_glosado, ind_glosa, cod_respuesta, valor_sustentado y valor_iva sean correctos");
					ret.setBandera(false);
					AudSYC.setRetornoSiarp(ret.getRetorno());
					logSYC.actualizarAuditoriaSYC(AudSYC);
					log.error(e.getClass());
					log.error(e.getMessage());
					return ret.isBandera();
				}
				break;

			case 2:// ES EPS (MAX 2 RUBROS)

				double valorDefinitiva;

				for (Rubro rubro : listaRubros) {
					maxConsecutivoGlosa = maxConsecutivoGlosa + 1;
					if (rubro.getIdRubro().equals(rubroComision)) {
						valorDefinitiva = ((double) Math.round(xmlAuditoria
								.getValor_sustentado() / 11));
					} else {
						valorDefinitiva = ((double) Math.round(xmlAuditoria
								.getValor_sustentado() / 1.1));
					}
					try {
						// xmlAuditoria.getFecha_auditoria()

						facturaDao.insertarGlosa(factura.getIdCuenta(),
								factura.getIdFactura(), maxConsecutivoGlosa,
								xmlAuditoria.getCodigo_glosa(),
								rubro.getIdRubro(),
								xmlAuditoria.getValor_glosado(), nombreGlosa,
								ps_fecha, ps_usuario, ps_maquina,
								xmlAuditoria.getInd_glosa(),
								xmlAuditoria.getCod_respuesta(), new Date(),
								valorDefinitiva,
								xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
						facturaDao.actualizarIVARubro(factura.getIdCuenta(),
								factura.getIdFactura(), maxConsecutivoGlosa + 1,
								xmlAuditoria.getCodigo_glosa(), rubro.getIdRubro(),
								xmlAuditoria.getValor_glosado(), nombreGlosa,
								ps_fecha, ps_usuario, ps_maquina,
								xmlAuditoria.getInd_glosa(),
								xmlAuditoria.getCod_respuesta(), new Date(),
								valorPendiente, xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
					} catch (Exception e) {
						ret.setRetorno("ERROR: no se pudo insertar la GLOSA DEFINITIVA, verifique los campos id_factura, id_cuenta, codigo_glosa, valor_glosado, ind_glosa,  cod_respuesta y valor_sustentado sean correctos");
						ret.setBandera(false);
						AudSYC.setRetornoSiarp(ret.getRetorno());
						logSYC.actualizarAuditoriaSYC(AudSYC);
						log.error(e.getClass());
						return ret.isBandera();
					}
				}
				break;
			default:
				ret.setRetorno((listaRubros.size() == 0) ? "ERROR: No existen rubros registrados para la factura"
						: "ERROR La factura tiene mas de 2 Rubros y no se puede procesar");
				AudSYC.setRetornoSiarp(ret.getRetorno());
				logSYC.actualizarAuditoriaSYC(AudSYC);
				Control = 1;
				break;
			}
		} else {
			ret.setRetorno("ERROR: El valor de GLOSA DEFINITIVA es diferente al valor Pendiente! DIFERENCIA GLOSA DEFINITIVA SALDO");
			
			AudSYC.setRetornoSiarp(ret.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			return false;
		}
		return Control == 0;
	}

	public boolean registrarSustentacion(AuditoriaSYC AudSYC,
			String rubroComision, String nombreGlosa, String ps_usuario,
			Date ps_fecha, String ps_maquina) {
		RetornoAuditoria ret = new RetornoAuditoria();
		Integer maxConsecutivoGlosa = facturaDao.maxConsecutivoGlosa(
				factura.getIdCuenta(), factura.getIdFactura());
		Double SaldoSustentacion = facturaDao.obtenerSaldoSustentacion(
				xmlAuditoria.getId_cuenta(), xmlAuditoria.getId_factura());
		int Control = 0;

		if (SaldoSustentacion == null) {
			SaldoSustentacion = 0.0;
		}
		if (SaldoSustentacion >= xmlAuditoria.getValor_sustentado()) {

			listaRubros = facturaDao.buscarRubros(xmlAuditoria.getId_cuenta(),
					xmlAuditoria.getId_factura());

			switch (listaRubros.size()) {
			case 1:// NO ES EPS (SOLO 1 RUBRO)
				try {

					Rubro r = listaRubros.get(0);

					facturaDao.insertarGlosa(factura.getIdCuenta(),
							factura.getIdFactura(), maxConsecutivoGlosa + 1,
							xmlAuditoria.getCodigo_glosa(), r.getIdRubro(),
							xmlAuditoria.getValor_glosado(), nombreGlosa,
							ps_fecha, ps_usuario, ps_maquina,
							xmlAuditoria.getInd_glosa(),
							xmlAuditoria.getCod_respuesta(), new Date(),
							xmlAuditoria.getValor_sustentado(),
							xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
					facturaDao.actualizarIVARubro(factura.getIdCuenta(),
							factura.getIdFactura(), maxConsecutivoGlosa + 1,
							xmlAuditoria.getCodigo_glosa(), r.getIdRubro(),
							xmlAuditoria.getValor_glosado(), nombreGlosa,
							ps_fecha, ps_usuario, ps_maquina,
							xmlAuditoria.getInd_glosa(),
							xmlAuditoria.getCod_respuesta(), new Date(),
							null, xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
				} catch (Exception e) {
					ret.setRetorno("ERROR: no se pudo insertar la SUSTENTACION, verifique los campos id_factura, id_cuenta, codigo_glosa, valor_glosado, ind_glosa,  cod_respuesta y valor_sustentado sean correctos");
					AudSYC.setRetornoSiarp(ret.getRetorno());
					logSYC.actualizarAuditoriaSYC(AudSYC);
					log.error(e.getClass());
					log.error(e.getMessage());
					return false;
				}
				break;

			case 2:// ES EPS (MAX 2 RUBROS)

				double valorSustentado;

				for (Rubro rubro : listaRubros) {
					maxConsecutivoGlosa = maxConsecutivoGlosa + 1;
					if (rubro.getIdRubro().equals(rubroComision)) {
						valorSustentado = ((double) Math.round(xmlAuditoria
								.getValor_sustentado() / 11));
					} else {
						valorSustentado = ((double) Math.round(xmlAuditoria
								.getValor_sustentado() / 1.1));
					}
					try {
						// xmlAuditoria.getFecha_auditoria()

						facturaDao.insertarGlosa(factura.getIdCuenta(),
								factura.getIdFactura(), maxConsecutivoGlosa,
								xmlAuditoria.getCodigo_glosa(),
								rubro.getIdRubro(),
								xmlAuditoria.getValor_glosado(), nombreGlosa,
								ps_fecha, ps_usuario, ps_maquina,
								xmlAuditoria.getInd_glosa(),
								xmlAuditoria.getCod_respuesta(), new Date(),
								valorSustentado,
								xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
						facturaDao.actualizarIVARubro(factura.getIdCuenta(),
								factura.getIdFactura(), maxConsecutivoGlosa + 1,
								xmlAuditoria.getCodigo_glosa(), rubro.getIdRubro(),
								xmlAuditoria.getValor_glosado(), nombreGlosa,
								ps_fecha, ps_usuario, ps_maquina,
								xmlAuditoria.getInd_glosa(),
								xmlAuditoria.getCod_respuesta(), new Date(),
								null, xmlAuditoria.getEstado_auditoria(), xmlAuditoria.getValor_iva());
					} catch (Exception e) {
						ret.setRetorno("ERROR: no se pudo insertar la SUSTENTACION, verifique los campos id_factura, id_cuenta, codigo_glosa, valor_glosado, ind_glosa,  cod_respuesta y valor_sustentado sean correctos");
						AudSYC.setRetornoSiarp(ret.getRetorno());
						logSYC.actualizarAuditoriaSYC(AudSYC);
						log.error(e.getClass());
						return false;
					}
				}
				break;
			default:
				ret.setRetorno((listaRubros.size() == 0) ? "ERROR: No existen rubros registrados para la factura"
						: "ERROR La factura tiene mas de 2 Rubros y no se puede procesar");
				AudSYC.setRetornoSiarp(ret.getRetorno());
				logSYC.actualizarAuditoriaSYC(AudSYC);
				Control = 1;
				break;
			}
		} else {
			ret.setRetorno("ERROR: El valor sustentado es mayor al valor Glosado! SUSTENTACION SALDO");
			AudSYC.setRetornoSiarp(ret.getRetorno());
			logSYC.actualizarAuditoriaSYC(AudSYC);
			return false;
		}
		return Control == 0;
	}

	public boolean validarXML(String ps_archivo) {

		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(ps_archivo)));
			this.listadoXML = doc.getElementsByTagName("xml_auditoria_row");
			return true;
		} catch (Exception e) {
			log.error(e.getClass());
			return false;
		}
	}

	private RetornoAuditoria obtenerInformacionXML() {

		xmlAuditoria = new XMLAuditoria();
		Date fechaActual = new Date();
		String valor = null;
		RetornoAuditoria ret = new RetornoAuditoria();

		if (listadoXML.getLength() > 0) {
			try {
				Element datos = (Element) listadoXML.item(0);

				/* id_cuenta */
				valor = datos.getElementsByTagName("id_cuenta").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo id_cuenta es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					try {
						xmlAuditoria.setId_cuenta(Long.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo id_cuenta debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* id_factura */
				valor = datos.getElementsByTagName("id_factura").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo id_factura es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					try {
						xmlAuditoria.setId_factura(Long.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo id_factura debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* id_tipo_doc */
				valor = datos.getElementsByTagName("id_tipo_doc").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo id_tipo_doc es requerido");
					ret.setBandera(false);
					return ret;
				} else if (valor.equals("CC") || valor.equals("NI") || valor.equals("CE") || valor.equals("CD") || valor.equals("NU") 
						 || valor.equals("PA") || valor.equals("PE") || valor.equals("RC") || valor.equals("SC") || valor.equals("TI")) {
					xmlAuditoria.setId_tipo_doc(valor);
				} else {
					ret.setRetorno("El campo id_tipo_doc debe ser CC, TI, CE, CD, NU, PA, PE, RC, SC o TI");
					ret.setBandera(false);
					return ret;
				}

				/* id_persona */
				valor = datos.getElementsByTagName("id_persona").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo id_persona es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					xmlAuditoria.setId_persona(valor);
				}

				/* prefijo_factura */
				valor = datos.getElementsByTagName("prefijo_factura").item(0)
						.getTextContent();
				xmlAuditoria.setPrefijo_factura(valor);

				/* factura */
				valor = datos.getElementsByTagName("factura").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo factura es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					xmlAuditoria.setPrefijo_factura(valor);
				}

				/* id_dx */
				valor = datos.getElementsByTagName("id_dx").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo id_dx es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					xmlAuditoria.setId_dx(valor);
				}

				/* valor_neto */
				valor = datos.getElementsByTagName("valor_neto").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo valor_neto es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					try {
						xmlAuditoria.setValor_neto(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo valor_neto debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* valor_iva */
				valor = datos.getElementsByTagName("valor_iva").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo valor_iva es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					try {
						xmlAuditoria.setValor_iva(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo valor_iva debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* valor_nc */
				valor = datos.getElementsByTagName("valor_nc").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo valor_nc es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					try {
						xmlAuditoria.setValor_nc(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo valor_nc debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* estado_auditoria */
				valor = datos.getElementsByTagName("estado_auditoria").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo estado_auditoria es requerido");
					ret.setBandera(false);
					return ret;
				} else if (valor.equals("G") || valor.equals("S")
						|| valor.equals("A") || valor.equals("D")) {
					xmlAuditoria.setEstado_auditoria(valor);
				} else {
					ret.setRetorno("El campo estado_auditoria debe ser G, S, D o A");
					ret.setBandera(false);
					return ret;
				}

				String valorAprovado = datos
						.getElementsByTagName("valor_aprobado").item(0)
						.getTextContent();
				String valorGlosado = datos
						.getElementsByTagName("valor_glosado").item(0)
						.getTextContent();
				String valorSustentado = datos
						.getElementsByTagName("valor_sustentado").item(0)
						.getTextContent();
				String codigoGlosa = datos.getElementsByTagName("codigo_glosa")
						.item(0).getTextContent();
				String indGlosa = datos.getElementsByTagName("ind_glosa")
						.item(0).getTextContent();

				if (xmlAuditoria.getEstado_auditoria().equals("G")
						|| xmlAuditoria.getEstado_auditoria().equals("S")
						|| xmlAuditoria.getEstado_auditoria().equals("D")) {
					if (!indGlosa.equals("1")) {
						ret.setRetorno("Si el campo estado_auditoria es G o S, el campo ind_glosa debe venir uno (1)");
						ret.setBandera(false);
						return ret;
					}
				} else {
					if (!indGlosa.equals("0")) {
						ret.setRetorno("Si el campo estado_auditoria es A, el campo ind_glosa debe venir cero(0)");
						ret.setBandera(false);
						return ret;
					}
				}

				/** INI.LJBT.MOD.2017.08.23 */
				String estadoGlosa = null;
				try {
					estadoGlosa = auditoriaSYCDao.obtenerEstadoGlosa(
							xmlAuditoria.getId_cuenta(),
							xmlAuditoria.getId_factura());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				/** FIN.LJBT.MOD.2017.08.23 */
				switch (xmlAuditoria.getEstado_auditoria()) {
				/** INI.LJBT.MOD.2017.08.23 */
				case "D":
					if (estadoGlosa != null && estadoGlosa.equals("D")) {
						ret.setRetorno("Ya existe una glosa Definitiva para la factura.");
						ret.setBandera(false);
						return ret;
					}

					if (estadoGlosa == null || estadoGlosa.equals("A")) {
						ret.setRetorno("No es posible recibir una Glosa Definitiva si anteriormente no tiene una auditoria en estado_auditoria G o S.");
						ret.setBandera(false);
						return ret;
					}

					/*if ((valorAprovado.trim().equals("") || valorAprovado
							.equals("0"))
							&& (valorGlosado.trim().equals("") || valorGlosado
									.equals("0"))) {
						retorno = "Si el campo estado_auditoria es D, debe tener valor el campo Valor_aprobado y/o Valor_glosado";
						return false;
					}*/
					
					if(!(valorSustentado.trim().equals("")) && !(valorGlosado.trim().equals("")) && 
							(Long.valueOf(valorGlosado.trim()) + Long.valueOf(valorSustentado.trim())) < 0){
						ret.setRetorno("Si el campo estado_auditoria es D, los campos valor_glosado y/o valor_sustentado deben ser superiores a 0");
						ret.setBandera(false);
						return ret;
					}

					/*if (!(valorSustentado.trim().equals("") || Integer
							.valueOf(valorSustentado) >= 0)) {
						retorno = "Si el campo estado_auditoria es D, NO debe traer valor inferior a cero (0) el campo valor_sustentado";
						return false;
					}

					if (Integer.valueOf(valorGlosado) <= 0) {
						retorno = "Si el campo estado_auditoria es D, debe tener valor el campo valor_glosado, debe ser mayor a cero.";
						return false;
					}*/

					valor = datos.getElementsByTagName("codigo_glosa").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						ret.setRetorno("El campo codigo_glosa es requerido");
						ret.setBandera(false);
						return ret;
					} else {
						try {
							xmlAuditoria
									.setCodigo_glosa(Integer.valueOf(valor));
						} catch (NumberFormatException e) {
							ret.setRetorno("El campo codigo_glosa debe ser numerico");
							ret.setBandera(false);
							return ret;
						}
					}
					break;
				/** FIN.LJBT.MOD.2017.08.23 */
				case "G":
					if ((valorAprovado.trim().equals("") || valorAprovado
							.equals("0"))
							&& (valorGlosado.trim().equals("") || valorGlosado
									.equals("0"))) {
						ret.setRetorno("Si el campo estado_auditoria es G, debe tener valor el campo Valor_aprobado y/o Valor_glosado");
						ret.setBandera(false);
						return ret;
					}

					if (!(valorSustentado.trim().equals("") || valorSustentado
							.equals("0"))) {
						ret.setRetorno("Si el campo estado_auditoria es G, no debe traer valor el campo valor_sustentado");
						ret.setBandera(false);
						return ret;
					}

					// if(indGlosa.equals("")) 1

					/* codigo_glosa */
					valor = datos.getElementsByTagName("codigo_glosa").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						ret.setRetorno("El campo codigo_glosa es requerido");
						ret.setBandera(false);
						return ret;
					} else {
						try {
							xmlAuditoria
									.setCodigo_glosa(Integer.valueOf(valor));
						} catch (NumberFormatException e) {
							ret.setRetorno("El campo codigo_glosa debe ser numerico");
							ret.setBandera(false);
							return ret;
						}
					}

					break;
				case "S":
					if (!(valorAprovado.trim().equals("") || valorAprovado
							.equals("0"))) {
						ret.setRetorno("Si el campo estado_auditoria es S, no debe tener valor el campo Valor_aprobado");
						ret.setBandera(false);
						return ret;
					}

					if (!(valorGlosado.trim().equals("") || valorGlosado
							.equals("0"))) {
						ret.setRetorno("Si el campo estado_auditoria es S, no debe tener valor el campo Valor_glosado");
						ret.setBandera(false);
						return ret;
					}

					if (!(codigoGlosa.trim().equals("") || codigoGlosa
							.equals("0"))) {
						ret.setRetorno("Si el campo estado_auditoria es S, no debe tener valor el campo codigo_glosa");
						ret.setBandera(false);
						return ret;
					}

					if (valorSustentado.trim().equals("")) {
						ret.setRetorno("Si el campo estado_auditoria es S, debe traer valor el campo valor_sustentado");
						ret.setBandera(false);
						return ret;
					}

					valor = datos.getElementsByTagName("codigo_glosa").item(0)
							.getTextContent();
					if (valor == null || valor.trim().equals("")) {
						xmlAuditoria.setCodigo_glosa(0);
					}

					break;

				case "A":

					if (!valorGlosado.trim().equals("0")) {
						ret.setRetorno("Si el campo estado_auditoria es A, el valor del campo valor_glosado debe ser cero (0)");
						ret.setBandera(false);
						return ret;
					}

					if (Integer.valueOf(valorAprovado) != (Integer
							.valueOf(xmlAuditoria.getValor_neto()) - Integer
							.valueOf(xmlAuditoria.getValor_nc()))) {
						ret.setRetorno("Si el campo estado_auditoria es A, el valor de la factura debe ser igual al valor aprobado.");
						ret.setBandera(false);
						return ret;
					}

					break;

				default:
					break;
				}

				/* valor_aprobado */
				valor = datos.getElementsByTagName("valor_aprobado").item(0)
						.getTextContent();
				if (!valor.trim().equals("")) {
					try {
						xmlAuditoria.setValor_aprobado(Double.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo valor_aprobado debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* valor_glosado */
				valor = datos.getElementsByTagName("valor_glosado").item(0)
						.getTextContent();
				if (!valor.trim().equals("")) {
					try {
						xmlAuditoria.setValor_glosado(Double.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo valor_glosado debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* valor_sustentado */
				valor = datos.getElementsByTagName("valor_sustentado").item(0)
						.getTextContent();
				if (!valor.trim().equals("")) {
					try {
						xmlAuditoria.setValor_sustentado(Double.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo valor_sustentado debe ser numerico");
						ret.setBandera(false);
						return ret;
					}
				}

				/* valor_aprobado */
				// valor =
				// datos.getElementsByTagName("valor_aprobado").item(0).getTextContent();
				// if (valor == null || valor.trim().equals("")) {
				// retorno = "El campo valor_aprobado es requerido";
				// return false;
				// } else {
				// try {
				// xmlAuditoria.setValor_aprobado(Double.valueOf(valor));
				// } catch (NumberFormatException e) {
				// retorno = "El campo valor_aprobado debe ser numerico";
				// return false;
				// }
				// }

				// /* valor_glosado */
				// valor =
				// datos.getElementsByTagName("valor_glosado").item(0).getTextContent();
				// if (valor == null || valor.trim().equals("")) {
				// retorno = "El campo valor_glosado es requerido";
				// return false;
				// } else {
				// try {
				// xmlAuditoria.setValor_glosado(Double.valueOf(valor));
				// } catch (NumberFormatException e) {
				// retorno = "El campo valor_glosado debe ser numerico";
				// return false;
				// }
				// }

				//
				// /* valor_sustentado */
				// valor =
				// datos.getElementsByTagName("valor_sustentado").item(0).getTextContent();
				// if (valor == null || valor.trim().equals("")) {
				// retorno = "El campo valor_sustentado es requerido";
				// return false;
				// } else {
				// try {
				// xmlAuditoria.setValor_sustentado(Double.valueOf(valor));
				// } catch (NumberFormatException e) {
				// retorno = "El campo valor_sustentado debe ser numerico";
				// return false;
				// }
				// }

				/* ind_glosa */
				valor = datos.getElementsByTagName("ind_glosa").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo ind_glosa es requerido");
					ret.setBandera(false);
					return ret;
				} else if (valor.equals("1") || valor.equals("0")) {
					xmlAuditoria.setInd_glosa(valor);
				} else {
					ret.setRetorno("El campo ind_glosa debe ser 1 o 0");
					ret.setBandera(false);
					return ret;
				}

				/* cod_respuesta */
				valor = datos.getElementsByTagName("cod_respuesta").item(0)
						.getTextContent();
				if (valor == "") {
					xmlAuditoria.setCod_respuesta(0);
				} else {
					try {
						xmlAuditoria.setCod_respuesta(Integer.valueOf(valor));
					} catch (NumberFormatException e) {
						ret.setRetorno("El campo cod_respuesta debe ser numerico");
						ret.setBandera(false);
						return ret;

					}

				}

				/* fecha_auditoria */
				valor = datos.getElementsByTagName("fecha_auditoria").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo fecha_auditoria es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					try {
						xmlAuditoria.setFecha_auditoria(formatoFecha
								.parse(valor));
						if (xmlAuditoria.getFecha_auditoria().getTime() > fechaActual
								.getTime()) {
							ret.setRetorno("El valor del campo fecha_auditoria no puede superar la fecha actual");
							ret.setBandera(false);
							return ret;
						}
					} catch (ParseException e) {
						ret.setRetorno("El formato de fecha para el campo fecha_auditoria debe ser yyyy-MM-dd HH:mm:ss");
						ret.setBandera(false);
						return ret;
					}
				}

				/* fecha_respuesta */
				valor = datos.getElementsByTagName("fecha_respuesta").item(0)
						.getTextContent();
				if (!valor.trim().equals("")) {
					try {
						xmlAuditoria.setFecha_respuesta(formatoFecha
								.parse(valor));
						if (xmlAuditoria.getFecha_respuesta().getTime() > fechaActual
								.getTime()) {
							ret.setRetorno("El valor del campo fecha_respuesta no puede superar la fecha actual");
							ret.setBandera(false);
							return ret;
						}
					} catch (ParseException e) {
						ret.setRetorno("El formato de fecha para el campo fecha_respuesta debe ser yyyy-MM-dd HH:mm:ss");
						ret.setBandera(false);
						return ret;
					}
				}

				/* firma_auditora */
				valor = datos.getElementsByTagName("firma_auditora").item(0)
						.getTextContent();
				if (valor == null || valor.trim().equals("")) {
					ret.setRetorno("El campo firma_auditora es requerido");
					ret.setBandera(false);
					return ret;
				} else {
					xmlAuditoria.setFirma_auditora(valor);
				}

				// xmlAuditoria.setId_rubro(datos.getElementsByTagName("id_rubro").item(0).getTextContent());

				ret.setBandera(true);
				return ret;
			} catch (Exception e) {
				log.error(e.getMessage());
				ret.setRetorno("Ocurrio un error general en la validacion del XML");
				ret.setBandera(false);
				return ret;
			}
		}
		ret.setBandera(false);
		return ret;
	}

}