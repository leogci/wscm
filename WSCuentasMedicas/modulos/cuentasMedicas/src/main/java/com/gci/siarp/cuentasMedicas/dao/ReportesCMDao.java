package com.gci.siarp.cuentasMedicas.dao;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;

@SiarpDatabase
public interface ReportesCMDao {

	public String getDataWindowReporte(@Param("nombreReporte") String nombreReporte);
	
	public Integer getCodigoReporte(@Param("nombreReporte") String nombreReporte);

}
