package com.gci.siarp.generales.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.lang.ArrayUtils;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import lombok.extern.log4j.Log4j;

@Log4j
public class UploadReceiverGCI implements Receiver {

	private static final long serialVersionUID = 1L;

	private String nombreArchivo;
	private String url;
	private File file;
	private String[] mimeTypes;

	private String archivoAnterior;

	public UploadReceiverGCI(String url, String nombreArchivo, String[] mimeTypes, String archivoAnterior) {
		this.url = url;
		this.nombreArchivo = nombreArchivo;
		this.setMimeTypes(mimeTypes);
		this.setArchivoAnterior(archivoAnterior);
	}

	@SuppressWarnings("hiding")
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
		String extension = null;
		try {
			if (this.getMimeTypes().length > 0) {
				if (!ArrayUtils.contains(this.getMimeTypes(), mimeType)) {
					Notification.show("Aviso!", "El tipo del archivo que esta intentaddo cargar no esta permitido",
							Type.ERROR_MESSAGE);
					return null;
				}
			}
			if (this.nombreArchivo != null && !nombreArchivo.trim().equals("")) {
				extension = filename.substring(filename.lastIndexOf("."));
				if (extension == null) {
					Notification.show("Aviso!", "No se puede definir la extencion del archivo", Type.ERROR_MESSAGE);
					return null;
				}
			}

			String todo = this.url + (nombreArchivo != null ? nombreArchivo + extension : filename);
			file = new File(todo);

			return fos = new FileOutputStream(file);

		} catch (final java.io.FileNotFoundException e) {
			log.error("No se encontro el archivo:" + e.getMessage());
			log.error(e.getCause());
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("Error en el archivo:" + ex.getMessage());
			log.error(ex.getCause());
		}
		return fos;
	}

	public String[] getMimeTypes() {
		if (mimeTypes == null)
			mimeTypes = new String[] {};
		return mimeTypes;
	}

	public void setMimeTypes(String[] mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public File getFile() {
		return file;
	}

	public String getArchivoAnterior() {
		return archivoAnterior;
	}

	public void setArchivoAnterior(String archivoAnterior) {
		this.archivoAnterior = archivoAnterior;
	}

}
