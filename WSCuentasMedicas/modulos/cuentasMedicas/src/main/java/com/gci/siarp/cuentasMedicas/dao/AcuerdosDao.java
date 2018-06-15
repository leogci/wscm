package com.gci.siarp.cuentasMedicas.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.cuentasMedicas.domain.Acuerdo;
import com.gci.siarp.cuentasMedicas.domain.CuentaAcuerdo;
import com.gci.siarp.cuentasMedicas.domain.Factura;

@SiarpDatabase
public interface AcuerdosDao {

	public List<Acuerdo> allAcuerdos();

	public Integer maxIDAcuerdo();

	public void crearAcuerdo(@Param("idAcuerdo") Integer idAcuerdo, @Param("idTipoDoc") String idTipoDoc, @Param("idProveedor") String idProveedor, @Param("fechaAcuerdo") Date fechaAcuerdo,
			@Param("valorAcuerdo") Long valorAcuerdo, @Param("saldoAcuerdo") Long saldoAcuerdo, @Param("observacion") String observacion, @Param("maquina") String maquina,
			@Param("usuario") String usuario);

	public void cambiarEstado(@Param("idAcuerdo") Integer idAcuerdo, @Param("estado") String estado, @Param("maquina") String maquina, @Param("usuario") String usuario);

	public void cerrarAcuerdo(@Param("idAcuerdo") Integer idAcuerdo, @Param("maquina") String maquina, @Param("usuario") String usuario);

	public void insertarFac_Acuerdo(@Param("idAcuerdo") Integer idAcuerdo, @Param("idCuenta") Long idCuenta, @Param("prefijoFactura") String prefijoFactura, @Param("factura") String factura,
			@Param("maquina") String maquina, @Param("usuario") String usuario, @Param("valorCuenta") Double valorCuenta);

	public List<Acuerdo> acuerdosByProveedor(@Param("idTipoDoc") String idTipoDoc, @Param("idProveedor") String idProveedor);

	public Double validarCuenta(@Param("idCuenta") Long idCuenta);
	
	public Integer glosaByCuenta(@Param("idCuenta") Long idCuenta);
	
	public Integer auditoriaCompletaCuenta(@Param("idCuenta") Long idCuenta);
	
	public Factura facturaAcuerdo(@Param("idCuenta") Long idCuenta);
	
	public List<CuentaAcuerdo> buscarCuentasAcuerdo(@Param("idAcuerdo") Integer idAcuerdo);
	
	public void actualizarSaldoAcuerdo(@Param("idAcuerdo") Integer idAcuerdo, @Param("usuario") String usuario ,@Param("maquina") String maquina);
	
	public Long consultaComunMora(@Param("idCuenta") Long idCuenta);
	
	
}
