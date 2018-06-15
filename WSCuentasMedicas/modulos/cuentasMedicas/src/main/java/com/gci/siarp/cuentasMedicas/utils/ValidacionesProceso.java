package com.gci.siarp.cuentasMedicas.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.domain.Cuenta;
import com.gci.siarp.cuentasMedicas.domain.Factura;

@SiarpService
public class ValidacionesProceso {

	public synchronized boolean validarXMLEstructura(String xml, String accion,
			int ps_multiusuario, String ps_radicador, String ps_usuario,
			String ps_maquina, String ps_visado) {
		Cuenta cuenta = new Cuenta();
		Factura factura = new Factura();
		
		Date fechaActual = new Date();
		Document doc;
		NodeList nodosXML;
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String retorno = null;
		
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xml)));
			nodosXML = doc.getElementsByTagName("Ejemplo_radicacion_row");
			
			/** Validación de XML recibido.*/
			if(nodosXML.getLength() > 0) {
				Element data = (Element) nodosXML.item(0);
				String value = null;
				/** ID_CUENTA */
				value = data.getElementsByTagName("id_cuenta").item(0).getTextContent();
				if(value == null || value.trim().equals("")) {
					retorno = "El campo ID_CUENTA es requerido";
					return false;
				} else {
					try {
						cuenta.setIdCuenta(Long.valueOf(value));
					} catch (NumberFormatException e) {
						retorno = "El campo ID_CUENTA debe ser númerico";
						return false;
					}
				}
				
				/** ID_FACTURA */
				value = data.getElementsByTagName("id_factura").item(0).getTextContent();
				if(value == null || value.trim().equals("")) {
					retorno = "El campo ID_FACTURA es requerido";
					return false;
				} else {
					try {
						factura.setIdFactura(Long.valueOf(value));
					} catch (NumberFormatException e) {
						retorno = "El campo id_factura debe ser numerico";
						return false;
					}
				}
				cuenta.setNumeroFacturas(ps_multiusuario);
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
		}
		
		return false;
	}

}
