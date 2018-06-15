package com.gci.siarp.cuentasMedicas.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.cuentasMedicas.domain.Factura;

@SiarpDatabase
public interface AjustarReservaDao {
	
	public List<Factura> listadoFacturasR();
	
	public void actualizarReconocimiento(@Param("idCuenta") Long idCuenta, @Param("idFactura") Long idFactura);
	
	public Double consultarAjusteSALDO(@Param("idCuenta") Long idCuenta, @Param("idFactura") Long idFactura, @Param("idSiniestro") Long idSiniestro);
	
	public void actualizarReconRESERVA(@Param("idCuenta") Long idCuenta, @Param("idFactura") Long idFactura);
	
	public List<Factura> listaFacturasReconociento(@Param ("idCuenta") Long idCuenta);

}
