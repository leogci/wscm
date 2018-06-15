package com.gci.siarp.cuentasMedicas.dao;

import org.apache.ibatis.annotations.Param;
import com.gci.siarp.api.annotation.SiarpDatabase;

@SiarpDatabase
public interface RadicacionSYCDao {
	
	public Long consultarConsecutivoRadicacion(@Param("idCuenta") Long idCuenta,@Param("idFactura") Long idFactura);
	public void insertarRadicacionSYS(@Param("idCuenta")Long idCuenta,@Param("idFactura")Long idFactura,@Param("l_consecutivo")Long l_consecutivo,@Param("tipoMov") String tipoMov,@Param("numFacturas")Integer numFacturas,@Param("ps_visado") String ps_visado,@Param("ps_factura") String ps_factura,@Param("ps_usuario") String ps_usuario,@Param("ps_maquina") String ps_maquina);
	public void actualizarSYC(@Param("retorno") String retorno,@Param("idCuenta") Long idCuenta,@Param("idFactura") Long idFactura,@Param("l_consecutivo")Long l_consecutivo );
	
}
