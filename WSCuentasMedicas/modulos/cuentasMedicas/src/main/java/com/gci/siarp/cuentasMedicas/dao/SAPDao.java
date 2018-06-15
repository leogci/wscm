package com.gci.siarp.cuentasMedicas.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.cuentasMedicas.domain.Pago;

@SiarpDatabase
public interface SAPDao {

	public int actualizarErrorPago(@Param("idSiarp") Long idSiarp, @Param("codError") String codError, @Param("retornoSAP") String retornoSAP);

	public String recuperarRetornoSAPPago(@Param("idSiarp") Long idSiarp);

	public void actualizarPagosSAP(@Param("idSiarp") Long idSiarp, @Param("tipoRespuesta") String tipoRespuesta, @Param("respuestaSAP") String respuestaSAP, @Param("usuario") String usuario,
			@Param("maquina") String maquina);

	public void actualizarSolPagoFatura(@Param("idSiarp") Long idSiarp);


	public void actualizarRespuestaPago(@Param("idSiarp") Long idSiarp, @Param("numOP") String numOP, @Param("numOPSise") Long numOPSise, @Param("retornoSAP") String retornoSAP, @Param("usuario") String usuario,
			@Param("maquina") String maquina, @Param("fechaPago") String fechaPago);

	public void insertarDetallePago(@Param("idSiarp") Long idSiarp, @Param("numCuenta") String numCuenta, @Param("tipoRet") String tipoRet, @Param("indRet") String indRet, @Param("indIva") String indIva,
			@Param("valorMov") Double valorMov, @Param("usuario") String usuario, @Param("maquina") String maquina);

	
	public List<Factura> pagossinreconocer(@Param("idCuenta") Long idCuenta);
	
	public List<Long> CuentasPorPagarSAP();
	
	public void actualizarPAGOSAP(@Param("idCuenta") Long idCuenta,@Param("idSIARP") Long idSIARP, @Param("fechaEnvio") Date fechaEnvio);
	
	public Factura pagarSAP(@Param("idCuenta") Long idCuenta);
	
	public String consultarCodigoSAP(@Param("idDepartamento") Integer idDepartamento,@Param("idMunicipio") Integer idMunicipio);
	
	public List<Map<String,?>> valoresPagoSAP(@Param("idCuenta") Long idCuenta);
	
	public void insertarHistoricoSAP(@Param("idSIARP") Long idSIARP, @Param("idCuenta") Long idCuenta, @Param("valorPago") Double valorPago,
			@Param("valorComision") Double valorComision, @Param("fechaEnvio") Date fechaEnvio, @Param("fechaRespuesta") Date fechaRespuesta, @Param("tipo") String tipo,
			@Param("respuestaSAP") String respuestaSAP);
	
	public void actualizarPagoError(@Param("idCuenta") Long idCuenta, @Param("idFactura") Long idFactura, @Param("idSIARP") Long idSIARP,
			@Param("codERROR")String codERROR,@Param("descERROR")String descERROR);
	
	public List<Pago> listadoPagosSAP(@Param("idCuenta") Long idCuenta);
	
	public String consultaridSIARP();
	
	public void actualizaridSIARP(@Param("idSIARP") String idSIARP);
	
	public void actualizarIDSOLPAGO(@Param("idCuenta") Long idCuenta, @Param("idSIARP") Long idSIARP);
	
	public String accionReprocesar(@Param("codAnula") String codAnula);
	
	public void marcarReprocesoSAP(@Param("idSiarp") Long idSiarp);

}
