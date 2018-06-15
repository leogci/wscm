package com.gci.siarp.generales.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.generales.domain.AFP;
import com.gci.siarp.generales.domain.ARP;
import com.gci.siarp.generales.domain.Afiliado;
import com.gci.siarp.generales.domain.Aseguradora;
import com.gci.siarp.generales.domain.Banco;
import com.gci.siarp.generales.domain.PM_Cargo;
import com.gci.siarp.generales.domain.PM_ComiteInterdisiplinario;
import com.gci.siarp.generales.domain.CoberturaSiniestro;
import com.gci.siarp.generales.domain.DatosCalifOrigen;
import com.gci.siarp.generales.domain.DatosCalifPCL;
import com.gci.siarp.generales.domain.Departamento;
import com.gci.siarp.generales.domain.DetalleAnulados;
import com.gci.siarp.generales.domain.DetalleError;
import com.gci.siarp.generales.domain.DetallePagados;
import com.gci.siarp.generales.domain.DiagGral;
import com.gci.siarp.generales.domain.PM_Documento;
import com.gci.siarp.generales.domain.EPS;
import com.gci.siarp.generales.domain.Empresa;
import com.gci.siarp.generales.domain.PM_Escolaridad;
import com.gci.siarp.generales.domain.PM_EstadoCivil;
import com.gci.siarp.generales.domain.EstadoPJ;
import com.gci.siarp.generales.domain.FormaNotificacion;
import com.gci.siarp.generales.domain.IPS;
import com.gci.siarp.generales.domain.MotivoDevolucion;
import com.gci.siarp.generales.domain.Municipio;
import com.gci.siarp.generales.domain.OficinaGiro;
import com.gci.siarp.generales.domain.ParametroGen;
import com.gci.siarp.generales.domain.PM_PrimeraOportunidad;
import com.gci.siarp.generales.domain.PM_TipoAT;
import com.gci.siarp.generales.domain.Seccional;
import com.gci.siarp.generales.domain.Siniestro;
import com.gci.siarp.generales.domain.TipoDocumento;
import com.gci.siarp.generales.domain.IbcIbl;
import com.gci.siarp.generales.domain.MaestroNominas;
import com.gci.siarp.generales.domain.MailParameters;
import com.gci.siarp.generales.domain.PM_TipoPrueba;

@SiarpDatabase
public interface GeneralesDao {

	int obtenerCantRegistroHorarioExt(@Param("asUsuario") String asUsuario, @Param("lt_ahora") String lt_ahora);

	int obtenerCantRegistroHorario(@Param("asUsuario") String asUsuario, @Param("ls_diasemana") String ls_diasemana,
			@Param("lt_ahora") String lt_ahora);

	int esFestivo(@Param("fechaHoy") Date fechaHoy);

	String estadoSini(@Param("alIdSiniestro") Long alIdSiniestro);

	Collection<TipoDocumento> traerTiposDocu();

	void actualizarUsuario(@Param("asUsuario") String asUsuario, @Param("aiDias") Integer aiDias,
			@Param("asMaquina") String asMaquina);

	Afiliado traerDatosAfiliado(@Param("asTDoc") String asTDoc, @Param("asDocu") String asDocu);

	Empresa traerDatosEmpresa(@Param("asTDoc") String asTDoc, @Param("asDocu") String asDocu);

	Siniestro traerDatosSiniestro(@Param("alSini") Long alSini);

	Collection<Siniestro> traerSiniestros(@Param("asTdoc") String asTdoc, @Param("asDocu") String asDocu);

	Collection<Siniestro> traerSiniestrosPension(@Param("asTdoc") String asTdoc, @Param("asDocu") String asDocu);

	Siniestro traerSiniestroPension(@Param("alIdSiniestro") Long alIdSiniestro);

	Collection<Siniestro> traerSiniestrosMuerte(@Param("asTdoc") String asTdoc, @Param("asDocu") String asDocu);

	Siniestro traerSiniestroMuerte(@Param("alIdSiniestro") Long alIdSiniestro);

	Collection<Siniestro> traerSiniestrosPJInvalidez(@Param("asTdoc") String asTdoc, @Param("asDocu") String asDocu);

	Collection<Siniestro> traerSiniestrosPJSobrevivientes(@Param("asTdoc") String asTdoc,
			@Param("asDocu") String asDocu);

	Collection<DiagGral> buscarDiags(@Param("asIdDiag") String asIdDiag, @Param("asDescripDiag") String asDescripDiag);

	Collection<DiagGral> traerDiagnosticos();

	Double SMLV(@Param("adtFecha") Date adtFecha);

	IbcIbl traerIbcIbl(@Param("alIdSiniestro") long alIdSiniestro);

	Long traerSalarioRelacionLab(@Param("alIdSiniestro") long alIdSiniestro);

	Double ipc(@Param("adFecha") Date adFecha);

	Long buscarProcesoPJSiniestro(@Param("alIdSiniestro") Long alIdSiniestro);

	EstadoPJ pretensionesPJ(@Param("alIdProceso") Long alIdProceso);

	Long instanciasEnContra(@Param("alIdProceso") Long alIdProceso);

	Long cuantosMovPJ(@Param("alIdSiniestro") Long alIdSiniestro, @Param("asRubro") String asRubro);

	Collection<Seccional> traerSeccionales();

	Collection<Departamento> traerDepartamentos();

	Collection<Banco> traerBancos();

	Banco consultarBancoById(@Param("idBanco") Integer idBanco);

	Collection<Municipio> traerMunicipios(@Param("aiIdDepto") Integer aiIdDepto);

	Collection<AFP> traerAFPs();

	Collection<EPS> traerEPSs();

	Collection<IPS> traerIPSs();

	BigDecimal edad(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin,
			@Param("decimales") Integer decimales);

	CoberturaSiniestro coberturaSiniestro(@Param("aiIdSiniestro") Integer aiIdSiniestro);

	ParametroGen recuperarParametroGen(@Param("aiCodigo") Integer aiCodigo);

	void actualizarConsecutivoGCI(@Param("aplicacion") Integer aplicacion);

	Integer recuperarConsecutivoGCI(@Param("aplicacion") Integer aplicacion);

	Date funcionDiaHabil(@Param("fecha") Date fecha, @Param("diasHabiles") Integer diasHabiles);

	public Collection<Aseguradora> traerAseguradoras();

	public Collection<Departamento> traerDeptosGiro();

	public Collection<Banco> traerBancosGiro(@Param("aiIdDepto") Integer aiIdDepto,
			@Param("aiIdMunic") Integer aiIdMunic);

	public Collection<OficinaGiro> traerOficinasGiro(@Param("aiIdDepto") Integer aiIdDepto,
			@Param("aiIdMunic") Integer aiIdMunic, @Param("aiIdBanco") Integer aiIdBanco);

	public OficinaGiro traerDatosGiro(@Param("aiIdOficinaGiro") Integer aiIdOficinaGiro);

	public Collection<Municipio> traerMunicsGiro(@Param("aiIdDepto") Integer aiIdDepto);

	public Collection<ARP> traerARPs();

	public Collection<MotivoDevolucion> buscaMotivoDevolucion();

	public Collection<FormaNotificacion> buscarFormaNotificacion();

	public MailParameters getMailParameters();

	public Long getConsecutivoSAP(@Param("codigo_consecutivo") int codigo_consecutivo);

	public Collection<PM_Escolaridad> PM_Escolaridades();

	public Collection<PM_EstadoCivil> PM_EstadosCiviles();

	public Collection<PM_PrimeraOportunidad> PM_PrimerasOportunidades();

	public Collection<PM_Cargo> PM_Cargos();

	public Collection<PM_Documento> PM_Documentos();

	public Collection<PM_TipoPrueba> PM_TipoPruebas();

	public Collection<PM_TipoAT> PM_TiposAT();

	public Collection<PM_ComiteInterdisiplinario> PM_comInterdisiplinarios();

	void guardarRegistroMaestroEnvio(@Param("id_siarp") Long id_siarp, @Param("modulo") String modulo,
			@Param("username") String username, @Param("machine") String machine,
			@Param("arc_sap_envio") String arc_sap_envio);
	
	public List<MaestroNominas> buscarNominasIT(@Param("nroNomina") Integer nroNomina,
			@Param("idSolicitudIt") Integer idSolicitudIt, @Param("tipoDoc") String tipoDoc,
			@Param("nroDocumento") String nroDocumento, @Param("fechaDesde") Date fechaDesde,
			@Param("fechaHasta") Date fechaHasta);
	
	public List<MaestroNominas> buscarNominasCM(@Param("nroNomina") Integer nroNomina,
			@Param("idCuenta") Long idCuenta, @Param("tipoDoc") String tipoDoc,
			@Param("nroDocumento") String nroDocumento, @Param("fechaDesde") Date fechaDesde,
			@Param("fechaHasta") Date fechaHasta);
	
	public List<MaestroNominas> buscarNominasIPP(@Param("nroNomina") Integer nroNomina,
			@Param("idSolicitudPe") Long idSolicitudPe, @Param("tipoDoc") String tipoDoc,
			@Param("nroDocumento") String nroDocumento, @Param("fechaDesde") Date fechaDesde,
			@Param("fechaHasta") Date fechaHasta);
	
	public List<DetalleAnulados> buscarDetalleAnulados(@Param("id_siarp") Double id_siarp);

	public List<DetalleError> buscarDetalleError(@Param("id_siarp") Double id_siarp);

	public List<DetallePagados> buscarDetallePagados(@Param("id_siarp") Double id_siarp);
	
	public DatosCalifOrigen traerDatosCalifOrigen(@Param("alIdSiniestro")Long alIdSiniestro);
	
	public DatosCalifPCL traerDatosCalifPCL(@Param("alIdSiniestro")Long alIdSiniestro);

}