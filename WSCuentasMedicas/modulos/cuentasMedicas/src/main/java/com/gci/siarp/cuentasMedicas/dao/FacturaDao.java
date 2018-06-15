package com.gci.siarp.cuentasMedicas.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.cuentasMedicas.domain.Cuenta;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.cuentasMedicas.domain.Glosa;
import com.gci.siarp.cuentasMedicas.domain.InfoEmbargo;
import com.gci.siarp.cuentasMedicas.domain.PA_PM_Rubros;
import com.gci.siarp.cuentasMedicas.domain.Pago;
import com.gci.siarp.cuentasMedicas.domain.Parametros;
import com.gci.siarp.cuentasMedicas.domain.Proveedor;
import com.gci.siarp.cuentasMedicas.domain.ReservaMov;
import com.gci.siarp.cuentasMedicas.domain.Rubro;
import com.gci.siarp.cuentasMedicas.domain.TipoGlosa;
import com.gci.siarp.generales.domain.Siniestro;

@SiarpDatabase
public interface FacturaDao {

	public Long consultarCuenta(Cuenta cuenta);

	public void insertarCuenta(Cuenta cuenta);

	public Integer consultaridTipoSol(Factura factura);

	public Long consultarSucursalDM(
			@Param("idTipoDocProv") String idTipoDocProv,
			@Param("idProveedor") String idProveedor,
			@Param("idDepartamento") Integer idDepartamento,
			@Param("idMunicipio") Integer idMunicipio);

	public Long consultarSucursal(@Param("idTipoDocProv") String idTipoDocProv,
			@Param("idProveedor") String idProveedor);

	public Integer consultarCODSISE(
			@Param("idDepartamento") Integer idDepartamento,
			@Param("idMunicipio") Integer idMunicipio);

	public void insertarFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("IdTipoDocEmpresa") String IdTipoDocEmpresa,
			@Param("IdEmpresa") String IdEmpresa,
			@Param("IdTipoDoc") String IdTipoDoc,
			@Param("IdPersona") String IdPersona, @Param("IdDX") String IdDX,
			@Param("NombreDX") String NombreDX,
			@Param("IdFuratFurep") Long IdFuratFurep,
			@Param("FechaAtep") Date FechaAtep,
			@Param("TipoSiniestro") String TipoSiniestro,
			@Param("ValorNeto") Double ValorNeto,
			@Param("ValorBruto") Double ValorBruto,
			@Param("NumeroAutorizacion") String NumeroAutorizacion,
			@Param("IndEstadoFactura") String IndEstadoFactura,
			@Param("NotaCredito") Double NotaCredito,
			@Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina,
			@Param("Factura") String Factura,
			@Param("FechaFactura") Date FechaFactura,
			@Param("IdSeccional") Integer IdSeccional,
			@Param("ValorIVA") Double ValorIVA,
			@Param("IdProveedor") String IdProveedor,
			@Param("IdTipoDocProv") String IdTipoDocProv,
			@Param("PrefijoFactura") String PrefijoFactura,
			@Param("FechaServicio") Date FechaServicio,
			@Param("FechaAutorizacion") Date FechaAutorizacion,
			@Param("IdSucursal") Long IdSucursal,
			@Param("IdTipoSOl") Integer IdTipoSOl,
			@Param("CodSTIKKER") Long CodSTIKKER,
			@Param("IdDepartamento") Integer IdDepartamento,
			@Param("IdMunicipio") Integer IdMunicipio,
			@Param("AudConcurrente") String AudConcurrente,
			@Param("FechaRadicacion") Date FechaRadicacion,
			@Param("IdAuditorConcurrente") String IdAuditorConcurrente,
			@Param("IdRadicador") String IdRadicador,
			@Param("l_codsise") Integer l_codsise,
			@Param("indVlrCertificado") Integer indVlrCertificado,
			@Param("codSAP") String codSAP,
			@Param("reconReserva") String reconReserva);

	public void actualizarValorCuenta(Cuenta cuenta);

	public Long existeSiniestro(Factura factura);

	public String consultarNombreRubro(@Param("idRubro") String idRubro);

	public Parametros consultarNombreRubroComision();

	public void insertarRubro(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("consecutivo") Long consecutivo,
			@Param("idRubro") String idRubro,
			@Param("s_nombreRubro") String s_nombreRubro,
			@Param("valorNeto") Double valorNeto,
			@Param("ValorIVA") Double ValorIVA,
			@Param("variableSaldos") Double variableSaldos,
			@Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina);

	public Long facturaSOlPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public Long consultarPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public void actualizarFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("IdTipoDocEmpresa") String IdTipoDocEmpresa,
			@Param("IdEmpresa") String IdEmpresa,
			@Param("IdTipoDoc") String IdTipoDoc,
			@Param("IdPersona") String IdPersona, @Param("IdDX") String IdDX,
			@Param("NombreDX") String NombreDX,
			@Param("IdFuratFurep") Long IdFuratFurep,
			@Param("FechaAtep") Date FechaAtep,
			@Param("TipoSiniestro") String TipoSiniestro,
			@Param("ValorNeto") Double ValorNeto,
			@Param("ValorBruto") Double ValorBruto,
			@Param("NumeroAutorizacion") String NumeroAutorizacion,
			@Param("NotaCredito") Double NotaCredito,
			@Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina,
			@Param("Factura") String Factura,
			@Param("FechaFactura") Date FechaFactura,
			@Param("IdSeccional") Integer IdSeccional,
			@Param("ValorIVA") Double ValorIVA,
			@Param("IdProveedor") String IdProveedor,
			@Param("IdTipoDocProv") String IdTipoDocProv,
			@Param("PrefijoFactura") String PrefijoFactura,
			@Param("FechaServicio") Date FechaServicio,
			@Param("FechaAutorizacion") Date FechaAutorizacion,
			@Param("IdSucursal") Long IdSucursal,
			@Param("IdTipoSOl") Integer IdTipoSOl,
			@Param("CodSTIKKER") Long CodSTIKKER,
			@Param("FechaRadicacion") Date FechaRadicacion,
			@Param("IdAuditorConcurrente") String IdAuditorConcurrente,
			@Param("IdRadicador") String IdRadicador,
			@Param("IdDepartamento") Integer IdDepartamento,
			@Param("IdMunicipio") Integer IdMunicipio,
			@Param("AudConcurrente") String AudConcurrente,
			@Param("l_codsise") Integer l_codsise,
			@Param("IndEstadoFactura") String IndEstadoFactura,
			@Param("indVlrCertificado") Integer indVlrCertificado,
			@Param("codSAP") String codSAP,
			@Param("reconReserva") String reconReserva);

	public Factura getFacturaById(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura, @Param("factura") String factura);

	public List<Rubro> buscarRubros(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public List<Rubro> buscarRubrosSAP(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public Integer existeGlosa(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public void actualizarRubro(@Param("idGlosa") Integer idGlosa,
			@Param("nombreGlosa") String nombreGlosa,
			@Param("valorGlosa") Double valorGlosa,
			@Param("ps_fecha") Date ps_fecha,
			@Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina,
			@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura, @Param("idRubro") String idRubro);

	public String nombreGlosa(@Param("tipoGlosa") Integer tipoGlosa);

	public void insertarGlosa(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("consecutivo") Integer consecutivo,
			@Param("idGlosa") Integer idGlosa,
			@Param("idRubro") String idRubro,
			@Param("valorGlosa") Double valorGlosa,
			@Param("nombreGlosa") String nombreGlosa,
			@Param("ps_fecha") Date ps_fecha,
			@Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina,
			@Param("estado") String estado,
			@Param("tipoRespuesta") Integer tipoRespuesta,
			@Param("fecha_respuesta") Date fecha_respuesta,
			@Param("valorSustentado") Double valorSustentado,
			@Param("estadoAuditoria") String estadoAuditoria,
			@Param("valor_iva") Integer valor_iva);

	public Double obtenerSaldoSustentacion(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public String consultarCambioEstado(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public void actualizarEstadoFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura, @Param("estado") String estado,
			@Param("valorGlosa") Double valorGlosa);

	public String valorRubroComision();// cambio

	public Integer maxConsecutivoGlosa(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public Long registrosPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public Double sumGlosa(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura, @Param("idRubro") String idRubro);

	public Double sumSustentado(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura, @Param("idRubro") String idRubro);

	public Double sumVRpagado(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura, @Param("idRubro") String idRubro);

	public void insertarPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("consecutivoP") Long consecutivoP,
			@Param("vrPagar") Double vrPagar,
			@Param("usuarioAUD") String usuarioAUD,
			@Param("maquinaAUD") String maquinaAUD,
			@Param("operacionAUD") String operacionAUD,
			@Param("idRubro") String idRubro);

	public List<Factura> buscarFacturas(@Param("idCuenta") Long idCuenta,
			@Param("indEstadoFactura") String indEstadoFactura,
			@Param("docProv") String docProv, @Param("idProv") String idProv,
			@Param("factura") String factura,
			@Param("idSeccional") Integer idSeccional,
			@Param("idSiarp") Long idSiarp,
			@Param("fechaInicial") Date fechaInicial,
			@Param("fechaFinal") Date fechaFinal,
			@Param("prefFactura") String prefFactura,
			@Param("tipoDocEmpresa") String tipoDocEmpresa,
			@Param("idEmpresa") String idEmpresa);

	public List<Glosa> listaGlosasByFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public List<Pago> listaPagosByFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public List<ReservaMov> listaReservasByFactura(
			@Param("idCuenta") Long idCuenta, @Param("idFactura") Long idFactura);

	public Proveedor consultarProveedor(@Param("idTipoDoc") String idTipoDoc,
			@Param("idProveedor") String idProveedor);

	public Cuenta consultarInfoCuenta(@Param("idCuenta") Long idCuenta);

	public String nombreTipoSolProveedor(@Param("idTipoSol") Integer idTipoSol);

	public List<PA_PM_Rubros> consultarListadoRubros();

	public List<Siniestro> consultarSiniestros(
			@Param("idTipoDoc") String idTipoDoc,
			@Param("idPersona") String idPersona);

	public void actualizarRubroInterface(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("consecutivo") Integer consecutivo,
			@Param("idRubro") String idRubro,
			@Param("nombreRubro") String nombreRubro);

	public String consultarNombreRubroInterface(@Param("idRubro") String idRubro);

	public Integer consultarProvEnbargado(@Param("idTipoDoc") String idTipoDoc,
			@Param("idProveedor") String idProveedor);

	public List<InfoEmbargo> detalleEmbargo(
			@Param("idTipoDoc") String idTipoDoc,
			@Param("idProveedor") String idProveedor);

	public InfoEmbargo inforEmbargo(@Param("idTipoDoc") String idTipoDoc,
			@Param("idProveedor") String idProveedor);

	public void devolverCuenta(@Param("idCuenta") Long idCuenta,
			@Param("observacion") String observacion,
			@Param("codigoGlosa") Integer codigoGlosa);

	public Long consultarPagoBycuenta(@Param("idCuenta") Long idCuenta);

	public String consultarNombreDiagnostico(@Param("idDX") String idDX);

	public void inactivarRubro(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public void inactivarAuditoria(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public void inactivarPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public String estadoFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public List<TipoGlosa> consultarTipoDevolucion();

	public Long consultarIDSiniestro(@Param("idTipoDoc") String idTipoDoc,
			@Param("idPersona") String idPersona, @Param("fechaAT") Date fechaAT);

	public List<Factura> consultarFacturasVR();

	public void actualizarFlagVRcertificado(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("vrCertificado") Integer vrCertificado);

	public Double countVrcertificado();

	public Cuenta getCuentaById(@Param("idCuenta") Long idCuenta);/*
																 * AUN NO SE
																 * USAAAAAAAAAAAAAA
																 */

	public Long totalFacturas(@Param("idCuenta") Long idCuenta);

	public Factura existeFactura(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public String consultarCiudad(
			@Param("idDepartamento") Integer idDepartamento,
			@Param("idMunicipio") Integer idMunicipio);

	public PA_PM_Rubros consultarRubrosID(@Param("idRubro") String idRubro);

	public Long maxConsecutivoRubro(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public Long existeProveedor(@Param("DOC") String DOC, @Param("ID") String ID);

	public Boolean existeAfiliado(@Param("tipoDoc") String tipoDoc,
			@Param("documento") String documento);

	// public Long existeEmpresa(@Param("DOC") String DOC,@Param("ID") String
	// ID);

	public void habilitarPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura);

	public void insertarReembolso(Cuenta cuenta);

	public Cuenta cuentaReembolso(@Param("idCuenta") Long idCuenta);

	public List<Factura> buscarFacturasPagoSAP(
			@Param("idCuenta") Long idCuenta,
			@Param("indEstadoFactura") String indEstadoFactura,
			@Param("docProv") String docProv, @Param("idProv") String idProv,
			@Param("factura") String factura,
			@Param("idSeccional") Integer idSeccional,
			@Param("idSiarp") Long idSiarp,
			@Param("fechaInicial") Date fechaInicial,
			@Param("fechaFinal") Date fechaFinal,
			@Param("prefFactura") String prefFactura,
			@Param("tipoDocEmpresa") String tipoDocEmpresa,
			@Param("idEmpresa") String idEmpresa,
			@Param("estadoSAP") String estadoSAP);

	Integer numeroFacturas(@Param("idCuenta") Long idCuenta);

	public int editarRubroPago(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("newIdRubro") String newIdRubro,
			@Param("oldIdRubro") String oldIdRubro,
			@Param("usuario") String usuario, @Param("maquina") String maquina);

	public int editarRubroGlosa(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("newIdRubro") String newIdRubro,
			@Param("oldIdRubro") String oldIdRubro,
			@Param("usuario") String usuario, @Param("maquina") String maquina);

	public Integer existeBanco(@Param("idBanco") Integer idBanco);

	public Boolean existeDiagnostico(@Param("idDX") String idDX);

	public Boolean existeDepartamento(
			@Param("idDepartamento") Integer idDepartamento);

	public Boolean existeMunicipio(
			@Param("idDepartamento") Integer idDepartamento,
			@Param("idMunicipio") Integer idMunicipio);

	public Boolean existeRubro(@Param("idRubro") String idRubro);

	public Boolean existeCuenta(@Param("idCuenta") Long idCuenta);

	public Double obtenerValorPendiente(@Param("idCuenta") Long id_cuenta,
			@Param("idFactura") Long id_factura);

	public void actualizarIVARubro(@Param("idCuenta") Long idCuenta,
			@Param("idFactura") Long idFactura,
			@Param("consecutivo") int consecutivo,
			@Param("codigo_glosa") Integer codigo_glosa,
			@Param("idRubro") String idRubro,
			@Param("valor_glosado") Double valor_glosado,
			@Param("nombreGlosa") String nombreGlosa,
			@Param("ps_fecha") Date ps_fecha,
			@Param("ps_usuario") String ps_usuario,
			@Param("ps_maquina") String ps_maquina,
			@Param("ind_glosa") String ind_glosa,
			@Param("cod_respuesta") Integer cod_respuesta,
			@Param("fechaActual") Date fechaActual,
			@Param("valorPendiente") Double valorPendiente,
			@Param("estado_auditoriaﬁ") String estado_auditoria,
			@Param("valor_iva") Integer valor_iva);

}
