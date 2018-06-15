package com.gci.siarp.generales.domain;

import lombok.Data;

@Data
public class MailParameters {
	private String smtp;
    private String host;
    private int port;
    private String usuario;
    private String clave;
    private String starttls;
    private String adressfrom;
    private String adressto;
}