package com.gci.siarp.generales.dao;

import java.util.Collection;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpSABASSDatabase;
import com.gci.siarp.generales.domain.RecaudoSABASS;
import com.gci.siarp.generales.domain.Siniestro;

@SiarpSABASSDatabase
public interface SABASSDao {

	public Collection<RecaudoSABASS> traerIBCsSABASS(Siniestro aSini);

	/** Consulta para armar la nomina en el módulo de IT/ReconocerIT */
	public double getTotalPaganRecuadoSabass(@Param("ll_docemp") long ll_docemp, @Param("ls_tdoc") String ls_tdoc,
			@Param("ld_fech1") Date ld_fech1, @Param("ld_fech2") Date ld_fech2);

}
