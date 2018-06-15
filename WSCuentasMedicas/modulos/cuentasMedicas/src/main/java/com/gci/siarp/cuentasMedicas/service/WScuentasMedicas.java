package com.gci.siarp.cuentasMedicas.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;import com.gci.siarp.api.annotation.SiarpService;

@Log4j
@SiarpService
public class WScuentasMedicas extends SpringBeanAutowiringSupport {

	@Autowired
	GestionFactura gestionFactura;
	@Autowired
	ImportaAuditoria importaAuditoria;
	SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	public String recibeFactura(String ps_factura, String ps_visado, String ps_multiusuario, String ps_radicador, String ps_usuario, String ps_fecha, String ps_maquina) {
		
		Integer multiusuario;
		Date fecha;
		String s_retorno = null;

		if (ps_factura == null || ps_visado == null || ps_multiusuario == null || ps_radicador == null || ps_usuario == null || ps_fecha == null || ps_maquina == null) {
			log.error("ERROR: Los parametros no pueden ser nulos");
			return "ERROR: Los parametros no pueden ser nulos";
		}

		try {
			multiusuario = Integer.valueOf(ps_multiusuario);
			if(multiusuario != null && multiusuario <=0){
				log.error("ERROR: El parametro ps_multiusuario, debe ser mayor a 0");
				return "ERROR: El parametro ps_multiusuario, debe ser mayor a 0";
			}
			
		} catch (Exception e) {
			log.error("Web Service Crear Factura: "+e.getClass());
			return "ERROR: El parametro ps_multiusuario, debe ser numerico";
		}

		try {
			fecha = formatoFecha.parse(ps_fecha);
		} catch (Exception e) {
			log.error("Web Service Crear Factura: "+e.getClass());
			return "ERROR: El parametro ps_fecha, debe tener el formato yyyy-MM-dd HH:mm:ss";
		}
		
		Date fechaActual = new Date();
		if (fecha.getTime() > fechaActual.getTime()){
			log.error("ERROR: El parametro ps_fecha no puede ser mayor a la fecha actual");
			return "ERROR: El parametro ps_fecha no puede ser mayor a la fecha actual";
		}

		try {
			s_retorno = gestionFactura.WScrearFactura(ps_factura, ps_visado, multiusuario, ps_radicador, ps_usuario, fecha, ps_maquina);

		}catch (DuplicateKeyException e) {
			log.error("ERROR: Se esta intentando crear un registro duplicado (llave primaria duplicada)"+e.getClass());
			return "ERROR: Se esta intentando crear un registro duplicado (llave primaria duplicada)";
		} catch (UnexpectedRollbackException e) {
			log.error("Web Service Crear Factura: "+e.getClass());
			return s_retorno;
		} catch (Exception e) {
			log.error("Web Service Crear Factura: "+e.getClass());
			return e.getMessage();
		}
		return s_retorno;
		

	}

	public String modificaFactura(String ps_factura, String ps_visado, String ps_multiusuario, String ps_usuario, String ps_fecha, String ps_maquina, String pi_reproceso,
			String ps_radicador) {
		
		Integer multiusuario = null, piReproceso = null;
		Date fecha = null;
		String s_retorno = null;

		if (ps_factura == null || ps_visado == null || ps_multiusuario == null || ps_usuario == null || ps_fecha == null || ps_maquina == null || pi_reproceso == null
				|| ps_radicador == null) {
			
			log.error("ERROR: Los parametros no pueden ser nulos");
			s_retorno = "ERROR: Los parametros no pueden ser nulos";
		}

		try {
			multiusuario = Integer.valueOf(ps_multiusuario);
			
			if(multiusuario != null && multiusuario <0){
				log.error("ERROR: El parametro ps_multiusuario, debe ser mayor a 0");
				s_retorno = "ERROR: El parametro ps_multiusuario, debe ser mayor a 0";
			}
			
		} catch (Exception e) {
			log.error("Web Service Modificar Factura: "+e.getClass());
			return "ERROR: El parametro ps_multiusuario, debe ser numerico";
		} 

		try {
			fecha = formatoFecha.parse(ps_fecha);
		} catch (Exception e) {
			log.error("Web Service Modificar Factura: "+e.getClass());
			return "ERROR: El parametro ps_fecha, debe tener el formato yyyy-MM-dd HH:mm:ss";
		}

		Date fechaActual = new Date();
		if (fecha.getTime() > fechaActual.getTime()){
			log.error("ERROR: El parametro ps_fecha no puede ser mayor a la fecha actual");
			return "ERROR: El parametro ps_fecha no puede ser mayor a la fecha actual";
		}
		
		try {
			piReproceso = Integer.valueOf(pi_reproceso);
			if(piReproceso != null && piReproceso <=0){
				log.error("ERROR: El parametro pi_reproceso, debe ser mayor a 0");
				s_retorno = "ERROR: El parametro pi_reproceso, debe ser mayor a 0";
			}
		} catch (Exception e) {
			log.error("Web Service Modificar Factura: "+e.getClass());
			return "ERROR: El parametro pi_reproceso, debe ser numerico";
		}
		try {
			s_retorno = gestionFactura.WSmodificarFactura(ps_factura, ps_visado, multiusuario, ps_usuario, fecha, ps_maquina, piReproceso, ps_radicador);
		} catch (DuplicateKeyException e) {
			log.error("ERROR: Se esta intentando crear un registro duplicado (llave primaria duplicada) "+e.getClass());
			return "ERROR: Se esta intentando crear un registro duplicado (llave primaria duplicada)";
		} catch (UnexpectedRollbackException e) {
			log.error("Web Service Modificar Factura: "+e.getClass());
			return s_retorno;
		} catch (Exception e) {
			log.error("Web Service Modificar Factura: "+e.getClass());
			return e.getMessage();
		}
		return s_retorno;
	}

	public String importaAuditoria(String ps_archivo, String ps_usuario, String ps_fecha, String ps_maquina) {
		String s_retorno = null;
		if (!(ps_archivo == null || ps_usuario == null || ps_fecha == null || ps_maquina == null)) {

			Date fecha = null;/*Definir el formato de la fecha */
			try {
				fecha = formatoFecha.parse(ps_fecha);
			} catch (Exception e) {
				log.error("Web Service Cargar Auditoria: "+e.getClass());
				return "ERROR: El parametro ps_fecha, debe tener el formato yyyy-MM-dd HH:mm:ss";
			}
			
			Date fechaActual = new Date();
			if (fecha.getTime() > fechaActual.getTime()){
				s_retorno = "ERROR: El parametro ps_fecha no puede ser mayor a la fecha actual";
			}
			
			if (importaAuditoria.validarXML(ps_archivo)) {
				try {
					s_retorno = importaAuditoria.registrarAuditoria(ps_archivo, ps_usuario, fecha, ps_maquina);
					} catch (DuplicateKeyException e) {
						log.error("ERROR: Se esta intentando crear un registro duplicado (llave primaria duplicada) "+e.getClass());
						return "ERROR: Se esta intentando crear un registro duplicado (llave primaria duplicada)";
					} catch (UnexpectedRollbackException e) {
						log.error("Web Service Cargar Auditoria: "+e.getClass());
						return s_retorno;
					} catch (Exception e) {
						log.error("Web Service Cargar Auditoria: "+e.getClass());
						return e.getMessage();
					}
			} else {
				s_retorno = "ERROR: El XML presento un error de sintaxis";
			}

		} else {
			s_retorno = "ERROR: Ninguno de los parametros pueden ser nulos";
		}
		return s_retorno;
	}
	
}