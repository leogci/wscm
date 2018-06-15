package com.gci.siarp.generales.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;
import com.gci.siarp.api.ui.SiarpTheme;
import com.gci.siarp.generales.domain.UploadReceiverGCI;
import com.vaadin.server.ErrorHandler;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import lombok.extern.log4j.Log4j;

@SuppressWarnings("serial")
@Log4j
public class WindowUpload extends Window
		implements StartedListener, ProgressListener, FailedListener, SucceededListener, FinishedListener {

	private VerticalLayout mainLayout;
	private Upload upload_1;
	private Label porcentaje;
	private Label l_titulo;
	private ProgressBar progress;
	private UploadReceiverGCI UploadReceiver;
	private String url;
	private String[] mimeTypes;
	private String fileName;
	private VerticalLayout barLayout;
	private Button b_cerrar;
	private Consumer<String> consumer;
	private String archivoAnterior;
	private Button b_cancelar;
	private HorizontalLayout horizontalLayout_upload;

	/**
	 * 
	 * @param fileName
	 *            En caso de que el parametro sea null dejara el nombre del
	 *            archivo que se intenta subir, en caso contrario debe colocarse
	 *            sin extensión
	 * @param url
	 *            Ruta donde se dejará guardado el archivo eg. "D:\\Imagen\"
	 * @param mimeTypes
	 *            Listado de Mime Types eg. "new String [] {"application/pdf",
	 *            "image/png"}"
	 * @param consumer
	 *            Consumer que se ejecuta cuando se realiza la carga del archivo
	 * @param archivoAnterior
	 *            nombre del archivo (con extension) que sera eliminado para
	 *            mantener uno solo
	 */
	public WindowUpload(String fileName, String url, String[] mimeTypes, Consumer<String> consumer,
			String archivoAnterior) {
		buildMainLayout();
		setContent(mainLayout);
		this.setModal(true);

		this.setMimeTypes(mimeTypes);
		this.url = url;
		this.setFileName(fileName);
		this.consumer = consumer;
		this.archivoAnterior = archivoAnterior;
		init();
		style();

	}

	public void init() {

		b_cancelar.addClickListener(e -> upload_1.interruptUpload());
		progress = new ProgressBar();
		progress.setWidth("100%");
		progress.setCaption("Progreso");
		barLayout.addComponent(progress);

		upload_1.setButtonCaption("Cargar Archivo!");

		this.center();
		this.setClosable(false);
		this.setResizable(false);

		upload_1.setImmediate(true);

		b_cerrar.addClickListener(e -> this.close());

		UploadReceiver = new UploadReceiverGCI(this.url, this.getFileName(), this.getMimeTypes(), this.archivoAnterior);
		upload_1.setReceiver(UploadReceiver);
		upload_1.addFinishedListener(this);
		upload_1.addFailedListener(this);
		upload_1.addProgressListener(this);
		upload_1.addSucceededListener(this);
		upload_1.addStartedListener(this);
		upload_1.setErrorHandler(new ErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				log.error("Error al cargar el Archivo " + event.getThrowable().getMessage());
			}
		});
	}

	private void style() {
		l_titulo.addStyleName(SiarpTheme.LABEL_BOLD);
		l_titulo.addStyleName(SiarpTheme.LABEL_COLORED);
		l_titulo.addStyleName(SiarpTheme.LABEL_H3);
		porcentaje.addStyleName(SiarpTheme.LABEL_HUGE);
		porcentaje.addStyleName(SiarpTheme.LABEL_COLORED);
		porcentaje.addStyleName(SiarpTheme.LABEL_BOLD);
		b_cerrar.addStyleName(SiarpTheme.BUTTON_BLUE);
		b_cancelar.addStyleName(SiarpTheme.BUTTON_DANGER);

	}

	@Override
	public void uploadFinished(FinishedEvent event) {
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		consumer.accept(((UploadReceiverGCI) event.getUpload().getReceiver()).getFile().getName());
		b_cerrar.setVisible(true);
		horizontalLayout_upload.setVisible(false);

		if (this.archivoAnterior != null) {

			String extensionNew = event.getFilename().substring(event.getFilename().lastIndexOf("."));
			String extensionOld = this.archivoAnterior.substring(this.archivoAnterior.lastIndexOf("."));

			if (!extensionNew.equals(extensionOld)) {
				File archivoAnterior = new File(this.url + this.archivoAnterior);
				if (archivoAnterior.exists()) {
					try {
						Files.delete(archivoAnterior.toPath());
					} catch (IOException e) {
						e.printStackTrace();
						Notification.show("Error!",
								"No se pudo eliminar el archivo anterior (" + this.archivoAnterior + ")",
								Type.ERROR_MESSAGE);
					}
				}
			}
		}
		Notification.show("Aviso!", "Se ha cargado el archivo correctamente.", Type.HUMANIZED_MESSAGE);
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		porcentaje.setValue("Fallo la carga del archivo!");
		progress.setValue(0f);
		consumer.accept(null);
		b_cerrar.setVisible(true);

		String extension = event.getFilename().substring(event.getFilename().lastIndexOf("."));
		File archivoFallo = new File(this.url + this.getFileName() + extension);
		if (archivoFallo.exists()) {
			try {
				Files.delete(archivoFallo.toPath());
			} catch (IOException e) {
				e.printStackTrace();
				Notification.show("Error!",
						"No se pudo eliminar el archivo que intentaba subir (" + archivoFallo.getName() + ")",
						Type.ERROR_MESSAGE);
			}
		}

	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		progress.setValue(new Float(readBytes / (float) contentLength));
		porcentaje.setValue(Math.round(100 * progress.getValue()) + " %");
	}

	@Override
	public void uploadStarted(StartedEvent event) {
		progress.setValue(0f);
		consumer.accept(null);
		UI.getCurrent().setPollInterval(500);
		b_cerrar.setVisible(false);

	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("600px");
		mainLayout.setHeight("200px");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		// top-level component properties
		setWidth("600px");
		setHeight("200px");

		// l_titulo
		l_titulo = new Label();
		l_titulo.setImmediate(false);
		l_titulo.setWidth("-1px");
		l_titulo.setHeight("-1px");
		l_titulo.setValue("Subir Archivo");
		mainLayout.addComponent(l_titulo);

		// label_1
		porcentaje = new Label();
		porcentaje.setImmediate(false);
		porcentaje.setWidth("-1px");
		porcentaje.setHeight("-1px");
		porcentaje.setValue("0 %");
		porcentaje.setContentMode(ContentMode.HTML);
		mainLayout.addComponent(porcentaje);
		mainLayout.setComponentAlignment(porcentaje, Alignment.TOP_CENTER);

		barLayout = new VerticalLayout();
		barLayout.setWidth("100%");
		barLayout.setHeight("-1px");
		mainLayout.addComponent(barLayout);

		// upload_1
		upload_1 = new Upload();
		upload_1.setImmediate(false);
		upload_1.setWidth("-1px");
		upload_1.setHeight("-1px");

		b_cancelar = new Button("Cancelar");
		b_cancelar.setWidth("-1px");
		b_cancelar.setHeight("-1px");

		horizontalLayout_upload = new HorizontalLayout();
		horizontalLayout_upload.setWidth("-1px");
		horizontalLayout_upload.setHeight("-1px");
		horizontalLayout_upload.setSpacing(true);
		horizontalLayout_upload.addComponent(upload_1);
		horizontalLayout_upload.addComponent(b_cancelar);
		horizontalLayout_upload.setComponentAlignment(upload_1, Alignment.BOTTOM_CENTER);
		horizontalLayout_upload.setComponentAlignment(b_cancelar, Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(horizontalLayout_upload);
		mainLayout.setComponentAlignment(horizontalLayout_upload, Alignment.MIDDLE_CENTER);

		b_cerrar = new Button("Cerrar");
		b_cerrar.setWidth("-1px");
		b_cerrar.setHeight("-1px");
		mainLayout.addComponent(b_cerrar);
		mainLayout.setComponentAlignment(b_cerrar, Alignment.MIDDLE_CENTER);

		return mainLayout;
	}

	public String[] getMimeTypes() {
		return mimeTypes;
	}

	public void setMimeTypes(String[] mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
