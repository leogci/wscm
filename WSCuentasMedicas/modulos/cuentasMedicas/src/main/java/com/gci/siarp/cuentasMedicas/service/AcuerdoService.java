package com.gci.siarp.cuentasMedicas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.cuentasMedicas.dao.AcuerdosDao;
import com.gci.siarp.cuentasMedicas.domain.Acuerdo;
import com.gci.siarp.cuentasMedicas.domain.Factura;
import com.gci.siarp.generales.domain.StructRetornos;

@SiarpService
public class AcuerdoService {

	@Autowired
	AcuerdosDao acuerdosDao;

	public StructRetornos registrarAcuerdo(Long idCuenta, String usuario, String maquina) {

		List<Acuerdo> acuerdosProveedor;
		
		if(!(acuerdosDao.consultaComunMora(idCuenta) == null)){
			return new StructRetornos(-10, "El siniestro de la cuenta tiene origen comun o es moroso.");
		}
		
		if (!cuentaSinGlosa(idCuenta)) {
			return new StructRetornos(-6, "La cuenta se encuentra glosada");
		}

		if (!auditoriaCompleta(idCuenta)) {
			return new StructRetornos(-5, "La Auditoria de la Cuenta esta incompleta");
		}

		Factura factura = acuerdosDao.facturaAcuerdo(idCuenta);
		if (factura == null) {
			return new StructRetornos(-4, "No existe el proveedor");
		}		

		acuerdosProveedor = acuerdosDao.acuerdosByProveedor(factura.getIdTipoDocProv(), factura.getIdProveedor());

		if (acuerdosProveedor.size() == 0) {
			return new StructRetornos(-3, "El proveedor no tiene Acuerdos");
		}

		Double valorCuenta = acuerdosDao.validarCuenta(idCuenta);

		if (valorCuenta == -1) {
			return new StructRetornos(-1, "La Cuenta no esta Completa");
		}
		if (valorCuenta == -2) {
			return new StructRetornos(-2, "El valor de la cuenta no coincide con la suma del de las facturas");
		}

		Acuerdo acuerdo = null;

		for (Acuerdo a : acuerdosProveedor) {
			if (a.getSaldoAcuerdo() >= valorCuenta) {
				acuerdo = a;
				break;
			}
		}

		if (acuerdo == null) {
			return new StructRetornos(-7, "Ningun Acuerdo del Proveedor tiene saldo suficiente");
		} else {
			try {
				acuerdosDao.insertarFac_Acuerdo(acuerdo.getIdAcuerdo(), idCuenta, factura.getPrefijoFactura(), factura.getFactura(), maquina, usuario, valorCuenta);
				
				System.err.println("ACUERDO "+ acuerdo.getIdAcuerdo());
				acuerdosDao.actualizarSaldoAcuerdo(acuerdo.getIdAcuerdo(), usuario, maquina);
				
				System.err.println("CORRECTO");
				return new StructRetornos(0, "Correcto");
			} catch (DuplicateKeyException e) {
				return new StructRetornos(-8, "La cuenta ya se encuentra asociada a un acuerdo! ");
			} catch (Exception e) {
				e.printStackTrace();
				return new StructRetornos(-9, "Ocurrio un Error. Consulte con el Administrador!");
			}
		}
	}

	public boolean auditoriaCompleta(Long idCuenta) {
		return acuerdosDao.auditoriaCompletaCuenta(idCuenta) == 0;
	}

	public boolean cuentaSinGlosa(Long idCuenta) {
		return acuerdosDao.glosaByCuenta(idCuenta) == 0;
	}
}
