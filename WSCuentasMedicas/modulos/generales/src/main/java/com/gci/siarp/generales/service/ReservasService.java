package com.gci.siarp.generales.service;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import com.gci.siarp.api.annotation.SiarpDatabase;
import com.gci.siarp.api.annotation.SiarpService;
import com.gci.siarp.generales.dao.ReservasDao;
import com.gci.siarp.generales.domain.CalculoPension;
import com.gci.siarp.generales.domain.SaldosRubro;
import com.gci.siarp.generales.domain.StructRetorno;
import com.vaadin.server.ServiceException;
import lombok.extern.log4j.Log4j;


@SiarpService
@Log4j
public class ReservasService extends SpringBeanAutowiringSupport{
	@Autowired
	private ReservasDao iReservasDao;
	//@Autowired
	//private GeneralesService iGeneralesServ;

	//private Siniestro iSiniestro;
	
	//private UtilidadesFechas iUtilFec=new UtilidadesFechas();
	
	public String f_crear_reserva(Long alIdSiniestro,String asUsuario,String asMaquina){

		String lsXml="<?xml version=\"1.0\" encoding=\"UTF-16LE\" standalone=\"no\" ?> \n<codigo>";
		StructRetorno lStRet;
		try{
			lStRet=constituirReservaSoa(alIdSiniestro,asUsuario,asMaquina,"SI");
			
			lsXml+=lStRet.getICodigo().toString()+"</codigo>\n";
			lsXml+="<mensaje>"+lStRet.getSMensaje()+"</mensaje>\n";
				
		}catch (ServiceException e){
			lsXml+="-1</codigo>\n";
			lsXml+="<mensaje>"+e.getMessage()+"</mensaje>\n";
		}
		
		return lsXml;

	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public StructRetorno constituirReservaSoa(Long alIdSiniestro,String asUsuario,String asMaquina,String asModulo) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.constituirReserva(alIdSiniestro, asUsuario, asMaquina, asModulo);
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error constituyendo reserva del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
			}
			return lStRet;
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error constituyendo reserva del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarAF(Long alIdSiniestro,Double adbValor,Double adbIBC,Long alIdAuxilio,String asModulo,String asUsuario,String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarAF(alIdSiniestro, adbValor, adbIBC, alIdAuxilio, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando AF del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error Ajustando AF del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerAF(Long alIdSiniestro,Double adbValor,Double adbIBC,Long alIdAuxilio,String asModulo,String asUsuario,String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerAF(alIdSiniestro, adbValor, adbIBC, alIdAuxilio, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando AF del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error Ajustando AF del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarAS(
			Long alIdSiniestro,			Double adbValor,			String asTipoMovOtr,
			Long alIdAutorizacion,		Long alIdCuenta,			Long alIdFactura,
			String asModulo,			String asUsuario,			String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarAS(alIdSiniestro, adbValor, asTipoMovOtr, alIdAutorizacion, alIdCuenta, alIdFactura, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando AS del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error Ajustando AS del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerAS(
			Long alIdSiniestro,			Double adbValor,			String asTipoMovOtr,
			Integer aiLiberarSaldo,		Long alIdAutorizacion,		Long alIdCuenta,
			Long alIdFactura,			String asModulo,			String asUsuario,
			String asMaquina
		)throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerAS(alIdSiniestro, adbValor, asTipoMovOtr,aiLiberarSaldo,alIdAutorizacion, alIdCuenta, alIdFactura, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error reconociento AS del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error reconociento AS del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarHO(
			Long alIdSiniestro,			Double adbValor,			String asTipoMovOtr,
			String asModulo,			Long alIdRelacion,			Long alItemRel,
			String asUsuario,			String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarHO(alIdSiniestro, adbValor, asTipoMovOtr, asModulo, alIdRelacion, alItemRel, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando HO del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando HO del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerHO(
			Long alIdSiniestro,			Double adbValor,			String asTipoMovOtr,
			String asModulo,			Long alIdRelacion,			Long alItemRel,
			String asUsuario,			String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerHO(alIdSiniestro, adbValor, asTipoMovOtr, asModulo, alIdRelacion, alItemRel, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error reconociendo HO del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error reconociendo HO del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarITXRadica(
			Long alIdSolicitudIt,			String asModulo,			String asUsuario,					String asMaquina ) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarITXRadica(alIdSolicitudIt, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando siniestros de la soliciud "+alIdSolicitudIt.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando siniestros de la soliciud "+alIdSolicitudIt.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarIT(
			Long alIdSiniestro,				Integer aiDias,							Double adbValorIT,
			Double adbValorApSalud,			Double adbValorApPension,				Double adbIBC,
			Double adbIBCActu,				Long alIdSolicitudIt,					Integer aiSecuencialIT,
			String asModulo,				String asUsuario,						String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarIT(alIdSiniestro, aiDias, adbValorIT, adbValorApSalud, adbValorApPension, adbIBC, adbIBCActu, alIdSolicitudIt, aiSecuencialIT, asModulo, asUsuario, asMaquina) ;
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando IT del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando IT del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerIT(
			Long alIdSiniestro,				Integer aiDias,							Double adbValorIT,
			Double adbValorApSalud,			Double adbValorApPension,				Double adbIBC,
			Double adbIBCActu,				Long alIdSolicitudIt,					Integer aiSecuencialIT,
			String asModulo,				String asUsuario,						String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerIT(alIdSiniestro, aiDias, adbValorIT, adbValorApSalud, adbValorApPension, adbIBC, adbIBCActu, alIdSolicitudIt, aiSecuencialIT, asModulo, asUsuario, asMaquina) ;
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error reconociendo IT del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error reconociendo IT del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarOT(
			Long alIdSiniestro,				Double adbValor,				String asTipoMovOtr,
			String asModulo,				Long alIdRelacion,				Long alItemRel,
			Long alSubItemRel,				String asUsuario,				String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarOT(alIdSiniestro, adbValor, asTipoMovOtr, asModulo, alIdRelacion, alItemRel, alSubItemRel, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando OT del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando OT del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerOT(
			Long alIdSiniestro,				Double adbValor,				String asTipoMovOtr,
			String asModulo,				Long alIdRelacion,				Long alItemRel,
			Long alSubItemRel,				String asUsuario,				String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerOT(alIdSiniestro, adbValor, asTipoMovOtr, asModulo, alIdRelacion, alItemRel, alSubItemRel, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando OT del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando OT del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarPCL(
			Long alIdSiniestro,			String asModulo,						Double adbPCL,
			Integer aiLiberarIPP,		Integer aiLiberarPI,
			Long alIdCalificacion,		Integer aiIdSolicitudPE,				Double adbValor,			
			Double adbIBL,				Double adbIBLActu,
			String asUsuario,			String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustarPCL(alIdSiniestro, asModulo, adbPCL, aiLiberarIPP,aiLiberarPI, alIdCalificacion, aiIdSolicitudPE, adbValor, adbIBL, adbIBLActu, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando por PCL el siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando por PCL el siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerIPP(
			Long alIdSiniestro,				String asModulo,					Integer aiIdSolicitudPE,
			Double adbValor,				Double adbIBL,						Double adbIBLActu,
			Double adbPCL,					String asUsuario,					String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerIPP(alIdSiniestro, asModulo, aiIdSolicitudPE, adbValor, adbIBL, adbIBLActu, adbPCL, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando OT del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando OT del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustePensXPension(
			Long alIdSiniestro,				String asTipo,						Double adbPCL,
			Double adbIBL,					Integer aiIdSolicitud,				Integer aiCalcularPension,
			Double adbPorcentPJ,			Double adbMesada,					Double adbRetro,
			String asModulo,				String asUsuario,					String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajustePensXPension(alIdSiniestro, asTipo, adbPCL, adbIBL, aiIdSolicitud, aiCalcularPension,adbPorcentPJ, adbMesada, adbRetro, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando Pensión del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando Pensión del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void liberarAsist(Long alIdSiniestro, String asModulo, String asUsuario, String asMaquina) throws ServiceException{
		String lsError;
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.liberarAsist(alIdSiniestro, asUsuario, asMaquina, asModulo);
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error liberando reserva del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error liberando reserva del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void liberarPeconomicas(Long alIdSiniestro, String asModulo, String asUsuario, String asMaquina) throws ServiceException{
		String lsError;
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.liberarPeconomicas(alIdSiniestro, asUsuario, asMaquina, asModulo);
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error liberando reserva del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error liberando reserva del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void liberarXMatem(Long alIdSiniestro, Long alIdSolicitud, String asModulo, String asUsuario, String asMaquina) throws ServiceException{
		String lsError;
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.liberarXMatematica(alIdSiniestro, alIdSolicitud, asUsuario, asMaquina, asModulo);
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error liberando reserva del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error liberando reserva del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void reconocerRetro(Long alIdSiniestro,Double adbValor,String asTipo,Double adbPCL,Double adbIBL,Double adbMesada,Long alIdSolicitud,String asUsuario,String asMaquina,String asModulo) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.reconocerRetro(alIdSiniestro, asTipo, adbPCL, adbIBL, alIdSolicitud, adbMesada, adbValor, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error reconociendo retroactivo del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error reconociendo retroactivo del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajusteSOA(
			Long alIdSiniestro,			String asModulo,
			String asUsuario,			String asMaquina) throws ServiceException {
		
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajusteSOA(alIdSiniestro, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando el siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando el siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajusteRetroactivo(Long alIdSiniestro, Date adFechaAt, Date adFechaCorte, Date adFechaMuerte,Double adbSmin,
			Double adbMesada, String asTipo, Long alNroMov, BigDecimal abPorcentPj, String asModulo, String asUsuario,
			String asMaquina) throws ServiceException {
		String lsError="";
		StructRetorno lStRet;
		
		try{
			lStRet=iReservasDao.ajusteRetroactivo(alIdSiniestro, adFechaAt, adFechaCorte, adFechaMuerte, adbSmin, adbMesada, asTipo, alNroMov, abPorcentPj, asModulo, asUsuario, asMaquina);
			
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error ajustando el retroactivo del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error ajustando el retroactivo del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public CalculoPension calcularPension(String asTipoPension,String asTDoc,String asDocu,Double adbIbl,Date adFechaSini)throws ServiceException{
		CalculoPension lCalculo=new CalculoPension();
		String lsError="";
		try{
			lCalculo=iReservasDao.calcularPension(asTipoPension, asTDoc, asDocu, adbIbl, adFechaSini);
			if (lCalculo.getValor()==null || lCalculo.getValor().equals(-1) ){
				lsError=getClass().getName()+";"+lCalculo.getMensaje()+" Error calculando pensión del Afiliado "+asDocu;
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
				
		}catch (Exception e) {
			lsError=getClass().getName()+";"+e.getMessage()+" Error calculando pensión del Afiliado "+asDocu;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
		return lCalculo;
	}
	
	public Integer consultarReserva(Long alIdSiniestro){
		return iReservasDao.consultarReserva(alIdSiniestro);
	}
	//se utiliza en procesos juridicos, por eso se creo este metodo
	public Double traerIbcConts(Long alIdSiniestro){
		return iReservasDao.ibcConst(alIdSiniestro);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//se utiliza en procesos juridicos, por eso se creo este metodo
	public Long siguienteMovRes(Long alIdSiniestro){
		return iReservasDao.maxMovRes(alIdSiniestro);
	}
	/////////////////////////////////////////////////////////////////////////////////
	//se utiliza en procesos juridicos, por eso se creo este metodo
	public Integer tieneMovLiberaMatemat(Long alIdSiniestro){
		return iReservasDao.tieneMovLiberaMatemat(alIdSiniestro);
	}

	public SaldosRubro getSaldosOtrJur(Long idSiniestro,String tipoReserva,  String tipoMovOtr, String asModuloOrigina){
		return iReservasDao.getSaldosOtrJur( idSiniestro, tipoReserva,   tipoMovOtr,  asModuloOrigina);
	}
	
}


	/*
	@Transactional(SiarpDatabase.transactionManagerBeanName)
	public StructRetornos ajustarAsistencialCM(Long alIdSiniestro, Long idCuenta, Long idFactura, Double valor, String usuario, String maquina){
		
		//Insertar Movimiento
		LocalDateTime today = LocalDateTime.now();
		Instant instant = today.atZone(ZoneId.systemDefault()).toInstant();
		Date ahora = Date.from(instant);
		
		iReservasDao.bloqueaTabla(alIdSiniestro);
		
		MovimientoReserva movReserva = new MovimientoReserva(alIdSiniestro);
		movReserva.setIdTipoMov("A");
		movReserva.setFechaMovimiento(ahora);
		movReserva.setAsistencial(valor);
		movReserva.setUsuarioAud(usuario);
		movReserva.setMaquinaAud(maquina);
		movReserva.setFechaModificacionAud(ahora);
		movReserva.setTipoMovOtr("AS");
		movReserva.setModuloOrigina("CM");
		movReserva.setNroMov(iReservasDao.maxMovRes(alIdSiniestro));	
		
		
		try {
			iReservasDao.insertMovReserva(movReserva);
			iReservasDao.insertReservasCM(alIdSiniestro, movReserva.getNroMov(), idCuenta, idFactura, usuario, maquina);
			iReservasDao.modificaMaestro(iReservasDao.saldosReserva(alIdSiniestro),movReserva);
			
		} catch (Exception e) {
			log.error("ajustarAsistencialCM " + e.getMessage());
			return new StructRetornos(-1, "Error insertando movimiento de Reservas Cuenta " + idCuenta + " Factura "+ idFactura);
		}
		return new StructRetornos(0, "");
	}
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarReservaIT(Long alIdSiniestro, Integer aiDias, Date adFecha, String asModulo, String asUsuario, String asMaquina) throws ServiceException{
			
		String lsError;
		Siniestro lSiniestro;
		IbcIbl lIbcIbl=new IbcIbl();
		MovimientoReserva lMovReserva=new MovimientoReserva(alIdSiniestro);
		try{
			if (aiDias<=0){
				lsError="Error -----  El número de días no puede ser menor o igual a cero";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			iReservasDao.bloqueaTabla(alIdSiniestro);
			if (iReservasDao.consultarReserva(alIdSiniestro)==0){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". No se ha constituido la reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
			if (lSiniestro.getEstado().equals("I")){
				lsError="Error -----  el siniestro "+alIdSiniestro.toString()+" se encuentra inactivo, no se puede modificar la reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			lIbcIbl=iGeneralesServ.traerIbcIbl(lSiniestro);
			
			if (iUtilFec.convertirDateLD(lSiniestro.getFechaAT()).getYear()<iUtilFec.convertirDateLD(adFecha).getYear()){
				lIbcIbl.setIbc(iGeneralesServ.iblActualizado( lIbcIbl.getIbc(), lSiniestro.getFechaAT(), adFecha,"I"));
			}
			
			porcentSaludPension lPorcent=iReservasDao.traerPorcentSaludPension(lSiniestro.getFechaAT());
			
			lMovReserva.setIt(aiDias * lIbcIbl.getIbc() / 30.0);
			lMovReserva.setAportesPension(lIbcIbl.getIbc()*aiDias*lPorcent.getPorcentPension()*lPorcent.getPorcentEmpresaPension()/30.0);
			lMovReserva.setAportesSalud(lIbcIbl.getIbc()*aiDias*lPorcent.getPorcentSalud()*lPorcent.getPorcentEmpresaSalud()/30.0);
			lMovReserva.setDiasIt(aiDias);
			
			lMovReserva.setAportesPension(new Double(MathUtils.round(lMovReserva.getAportesPension().floatValue())));
			lMovReserva.setAportesSalud(new Double(MathUtils.round(lMovReserva.getAportesSalud().floatValue())));
			lMovReserva.setIt(new Double(MathUtils.round(lMovReserva.getIt().floatValue())));
			
			Long llNmov = iReservasDao.maxMovRes(alIdSiniestro);
			lMovReserva.setNroMov(llNmov);
			
			lMovReserva.setIdTipoMov("A");
			lMovReserva.setFechaMovimiento(adFecha);
			lMovReserva.setUsuarioAud(asUsuario);
			lMovReserva.setMaquinaAud(asMaquina);
			lMovReserva.setModuloOrigina(asModulo);
			lMovReserva.setFechaModificacionAud(new Date());
			lMovReserva.setIndMortal(lSiniestro.getMortal());
			lMovReserva.setIbc(lIbcIbl.getIbc());
			lMovReserva.setIdParteCuerpo(lSiniestro.getParteCuerpo());
			lMovReserva.setIdTipoLesion(lSiniestro.getNaturalezaLesion());
			lMovReserva.setIdDx(lSiniestro.getDiagnostico());
			
			iReservasDao.insertMovReserva(lMovReserva);
			iReservasDao.modificaMaestro(iReservasDao.saldosReserva(alIdSiniestro),lMovReserva);
		}catch(ServiceException e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error creando ajuste It siniestro "+alIdSiniestro;
			log.error(lsError);
			throw (new ServiceException(lsError));
		}catch(Exception e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error creando ajuste It siniestro "+alIdSiniestro;
			log.error(lsError);
			throw (new ServiceException(lsError));
		}
		
	}
	
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarReservaPCL(Long alIdSiniestro,BigDecimal adbPCL,String asUsuario,String asMaquina,String asModulo) throws ServiceException {
		Double ldbIPPparcial=0.0;
		Double ldbAjIpp=0.0;
		Double ldbAjPi=0.0,ldbAjRi=0.0;
		IbcIbl lIbcIbl=new IbcIbl();
		Double ldbIBLActu=0.0;
		Boolean lbnAjusteRetro=false;

		String lsError;
		ParametrosPensiones lPension=new ParametrosPensiones();
		try{
			if (iGeneralesServ.estadoSini(alIdSiniestro).equals("I")){
				lsError=getClass().getName()+" Error- El siniestro se encuentra inactivo";
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
			
			if (adbPCL.signum()==-1){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". El valor de PCL no puede ser menor a cero";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			if (adbPCL.compareTo(new BigDecimal(100))==1){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". El valor de PCL no puede ser mayor a 100";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			iReservasDao.bloqueaTabla(alIdSiniestro);
			if (iReservasDao.consultarReserva(alIdSiniestro)==0){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". No se ha constituido la reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			Siniestro lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
			Double ldbIbcConst=iReservasDao.ibcConst(alIdSiniestro);
			Double ldbSminFAT=iGeneralesServ.SMLV(lSiniestro.getFechaAT());
			lIbcIbl=iGeneralesServ.traerIbcIbl(lSiniestro);
			
			if (lIbcIbl.getIbl().compareTo(0.0)==0)
				lIbcIbl.setIbl(ldbIbcConst);
			
			String lsTipoIndexacion="";
			if (adbPCL.intValue()>=5 && adbPCL.intValue()<50) lsTipoIndexacion="P";
			ldbIBLActu=iGeneralesServ.iblActualizado(lIbcIbl.getIbl(), lSiniestro.getFechaAT(), new Date(), lsTipoIndexacion) ;
			
			if (lIbcIbl.getIbl()==null || lIbcIbl.getIbl().compareTo(0.0)==0){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". No se pudo hallar valor de IBC en la constitución del siniestro ni en recaudos";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			if (adbPCL.intValue()>=5 && adbPCL.intValue()<50){
				
				/////     F I N      I   B  C    ahora es IBL
						
				ldbIPPparcial = (adbPCL.divide(new BigDecimal(2)).subtract(new BigDecimal(0.5))).multiply( new BigDecimal(ldbIBLActu)).doubleValue();
				ldbIPPparcial=(double)ldbIPPparcial.longValue();
			}else{
				ldbIPPparcial = 0.0;
			}
			Double ldbMesada=0.0;
			
			if(adbPCL.intValue()>=50){
				lbnAjusteRetro=true;
				
				ldbMesada= calcularMesada("I",adbPCL.doubleValue(),lIbcIbl.getIbl(),ldbSminFAT);

				Integer liTieneMovM=iReservasDao.tieneMovLiberaMatemat(lSiniestro.getIdSiniestro());
				if (liTieneMovM>0){
					lsError="Atencion -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". Ya fue liberado por Matemática no puede modificar la reserva.";
					log.error(lsError);
					//resPI.setValorPI(0.0);
				}else
					lPension=calcularPension("I", lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT()); //la mesada es traida a valor presente en calcularPension
					//resPI= calcularResPInvalidez(iSiniestro.getTDocAfil(), iSiniestro.getDocAfil(), ldbMesada, iSiniestro.getFechaAT(),adbPCL,iGeneralesServ.SMLV(new Date()),alIdSiniestro) ;
			}
			SaldosReserva saldoRes=iReservasDao.saldosReserva(lSiniestro.getIdSiniestro());
			
			
			if (ldbIPPparcial.compareTo(0.0)==0){
					ldbAjIpp = saldoRes.getReconIPP() - saldoRes.getConstIPP();
					ldbIPPparcial = saldoRes.getReconIPP();
			}else if (ldbIPPparcial > saldoRes.getConstIPP())
				ldbAjIpp = ldbIPPparcial - saldoRes.getConstIPP();
			else if (ldbIPPparcial < saldoRes.getConstIPP())
				if (saldoRes.getReconIPP() > ldbIPPparcial){
					ldbAjIpp = saldoRes.getReconIPP() - saldoRes.getConstIPP();
					ldbIPPparcial = saldoRes.getReconIPP();
				}
				else
					ldbAjIpp = ldbIPPparcial - saldoRes.getConstIPP();
			
			if (lPension.getValor()==null) lPension.setValor(0.0);
			if (lPension.getValor().compareTo(0.0)==0){
				ldbAjPi = saldoRes.getReconPI() - saldoRes.getConstPI();
				ldbAjRi=saldoRes.getReconRI() - saldoRes.getConstRI();
				//resPI.setValorPI(saldoRes.getReconPI());
			}else if (lPension.getValor().compareTo(saldoRes.getConstPI())!=0){
				ldbAjPi = lPension.getValor() - saldoRes.getConstPI();
			}
			
			EstadoPJ estPj=iGeneralesServ.buscarEstadoPJ(lSiniestro.getIdSiniestro());
			
			if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsIpp().equals("1")){//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
				ldbAjIpp=0.0; //no se debe mover la reserva
				//st_r.texto = 'f_ajustarReservaPCL - Siniestro ' + string(id_siniestro) + ' tiene proceso jurídico vinculado. Ajuste IPP 0'
				//iel_jag.log('f_ajustarReservaPCL - Siniestro ' + string(id_siniestro) + ' tiene proceso jurídico vinculado. Ajuste IPP 0')
			}
			if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPi().equals("1")){ //si el sineistro está en un PJ debe dejar la plata que reservó juridicos
				ldbAjPi=0.0; //no se debe mover la reserva
				//st_r.texto = 'f_ajustarReservaPCL - Siniestro ' + string(id_siniestro) + ' tiene proceso jurídico vinculado. Ajuste PI 0'
				//iel_jag.log('f_ajustarReservaPCL - Siniestro ' + string(id_siniestro) + ' tiene proceso jurídico vinculado. Ajuste PI 0')
			}
			ldbAjIpp=(double)ldbAjIpp.longValue();
			
			Long llNmov=iReservasDao.maxMovRes(alIdSiniestro);
			MovimientoReserva lMov=new MovimientoReserva(alIdSiniestro);
			lMov.setNroMov(llNmov);
			lMov.setIdTipoMov("A");
			lMov.setFechaMovimiento(new Date());
			lMov.setFechaModificacionAud(new Date());
			lMov.setUsuarioAud(asUsuario);
			lMov.setMaquinaAud(asMaquina);
			lMov.setModuloOrigina(asModulo);
			lMov.setIpp(ldbAjIpp);
			lMov.setPi(ldbAjPi);
			lMov.setRetroPi(ldbAjRi);
			lMov.setPCL(adbPCL.doubleValue());
			
			
			if (ldbMesada!=0.0) lMov.setMesada(ldbMesada);
			lMov.setIbc(lIbcIbl.getIbc());
			lMov.setIbl(lIbcIbl.getIbl());
			lMov.setIblActu(ldbIBLActu);
			
			if(!(ldbAjIpp==0 &&ldbAjPi==0 &&ldbAjRi==0)){
				iReservasDao.insertMovReserva(lMov);
				iReservasDao.modificaMaestro(saldoRes,lMov);	
			}
			
			
			
			if (ldbAjPi.compareTo(0.0)!=0){ //puede ser diferente a cero cuando se libera, pero no tiene que ajustar retro
				if (lbnAjusteRetro )
					ajusteRetroactivo(alIdSiniestro, lSiniestro.getFechaAT(), new Date(), lSiniestro.getFechaMuerte(), ldbSminFAT, ldbMesada, "I", llNmov, new BigDecimal(1), asModulo, asUsuario, asMaquina);
			}
		}catch(ServiceException e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error ajustando la reserva por PCL del siniestro "+alIdSiniestro;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}catch(Exception e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error ajustando la reserva por PCL del siniestro "+alIdSiniestro;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	
	private LocalDate f3AnyosAtras(LocalDate adtFecha){
		if (adtFecha.getMonthValue()==2 && adtFecha.getDayOfMonth()==29)
			return LocalDate.of(adtFecha.getYear()-3,adtFecha.getMonthValue(),28 );
		else
			return LocalDate.of(adtFecha.getYear()-3,adtFecha.getMonthValue(),adtFecha.getDayOfMonth() );
	}
	
	public String f_crear_reserva(Long alIdSiniestro,String asUsuario,String asMaquina){

		String lsXml="<?xml version=\"1.0\" encoding=\"UTF-16LE\" standalone=\"no\" ?> \n<codigo>";

		try{
			constituirReservaSoa(alIdSiniestro,asUsuario,asMaquina,"SI");
			lsXml+="1</codigo>\n";
			lsXml+="<mensaje>Reserva creada con exito</mensaje>\n";
		}catch (ServiceException e){
			lsXml+="-1</codigo>\n";
			lsXml+="<mensaje>"+e.getMessage()+"</mensaje>\n";
		}
		
		return lsXml;

	}

	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajusteRetroactivo(Long alIdSiniestro, Date adFechaAt, Date adFechaCorte, Date adFechaMuerte,Double adbSmin,
			Double adbMesada, String asTipo, Long alNroMov, BigDecimal abPorcentPj, String asModulo, String asUsuario,
			String asMaquina) throws ServiceException {
		String lsError;
		try{
			Double ldbValor=0.0;
			Double ldbSmin=0.0;
			Boolean lbnEsMin = false;
			String lsPar14 = par14(adFechaAt,adbSmin,adbMesada);
			Date ldTemp;
			
			LocalDate ldtFechaAt=iUtilFec.convertirDateLD(adFechaAt);
			LocalDate ldtFechaMuerte=(adFechaMuerte==null)?null:iUtilFec.convertirDateLD(adFechaMuerte);
			LocalDate ldtFechaCorte=ultimoDiaMes(adFechaCorte);
			ldTemp=iReservasDao.fechaEstructuracion(alIdSiniestro);
			LocalDate ldtFechaEstructura = (ldTemp==null)?null:iUtilFec.convertirDateLD(ldTemp);
			ldTemp=iReservasDao.fechaReclama(alIdSiniestro);
			LocalDate ldtFechaReclama = (ldTemp==null)?null:iUtilFec.convertirDateLD(ldTemp);
			LocalDate ldtFechaDesde,ldtUltimaIncap ;
			
			if (ldtFechaReclama!=null){
				if (asTipo.equals("I")){
					ldTemp=iReservasDao.fechaUltimaIncapacidad(alIdSiniestro);
					ldtUltimaIncap=(ldTemp==null)?null:iUtilFec.convertirDateLD(ldTemp);
					
					if ((ldtUltimaIncap!=null && ldtFechaEstructura!=null && ldtUltimaIncap.compareTo(ldtFechaEstructura)>0) || 
							(ldtUltimaIncap!=null && ldtFechaEstructura==null && ldtUltimaIncap.compareTo(ldtFechaAt)>0)) {
						if (ldtUltimaIncap.compareTo(f3AnyosAtras(ldtFechaReclama))>0)
							ldtFechaDesde=ldtUltimaIncap;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaReclama);
					}
					else if (ldtFechaEstructura!=null){
						if (ldtFechaEstructura.compareTo(f3AnyosAtras(ldtFechaReclama))>0) 
							ldtFechaDesde=ldtFechaEstructura;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaReclama);
					}else{ //contra la fecha de at
						if (ldtFechaAt.compareTo(f3AnyosAtras(ldtFechaReclama))>0)
							ldtFechaDesde=ldtFechaAt;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaReclama);
						}
				}
				else {//Sobrevivencia
					if (ldtFechaMuerte!=null){
						if (ldtFechaMuerte.compareTo(f3AnyosAtras(ldtFechaReclama))>0) 
							ldtFechaDesde=ldtFechaMuerte;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaReclama);
					}else{//contra la fecha de at
						if (ldtFechaAt.compareTo(f3AnyosAtras(ldtFechaReclama))>0) 
							ldtFechaDesde=ldtFechaAt;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaReclama);
					}
				}
			}else{//fecha de reclamacion es nula (no hay una solicitud de pension con este siniestro)
				if (asTipo.equals("I")){
					ldTemp=iReservasDao.fechaUltimaIncapacidad(alIdSiniestro);
					ldtUltimaIncap=(ldTemp==null)?null:iUtilFec.convertirDateLD(ldTemp);
					
					if ((ldtUltimaIncap!=null && ldtFechaEstructura!=null && ldtUltimaIncap.compareTo(ldtFechaEstructura)>0) ||
						(ldtUltimaIncap!=null && ldtFechaEstructura==null && ldtUltimaIncap.compareTo(ldtFechaAt)>0)){
						if (ldtUltimaIncap.compareTo(f3AnyosAtras(ldtFechaCorte))>0) 
							ldtFechaDesde=ldtUltimaIncap;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaCorte);
						}else if (ldtFechaEstructura!=null) {
							if (ldtFechaEstructura.compareTo(f3AnyosAtras(ldtFechaCorte))>0)
								ldtFechaDesde=ldtFechaEstructura;
							else
								ldtFechaDesde=f3AnyosAtras(ldtFechaCorte);
						}
					else //contra la fecha de at
						if (ldtFechaAt.compareTo(f3AnyosAtras(ldtFechaCorte))>0) 
							ldtFechaDesde=ldtFechaAt;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaCorte);
				
				}else{//Sobrevivencia
					if (ldtFechaMuerte!=null){
						if (ldtFechaMuerte.compareTo(f3AnyosAtras(ldtFechaCorte))>0) 
							ldtFechaDesde=ldtFechaMuerte;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaCorte);
					}else //contra la fecha de at
						if (ldtFechaAt.compareTo(f3AnyosAtras(ldtFechaCorte))>0) 
							ldtFechaDesde=ldtFechaAt;
						else
							ldtFechaDesde=f3AnyosAtras(ldtFechaCorte);
				}
			}

			
			if (adbMesada.longValue() <= adbSmin.longValue()){
				lbnEsMin = true;
				adbMesada=adbSmin;
			}
			if (ldtFechaAt.getYear()<ldtFechaDesde.getYear()) adbMesada=iGeneralesServ.mesadaActual(adbMesada, adFechaAt, iUtilFec.convertirLD(ldtFechaDesde), lbnEsMin);
			
			
			if (ldtFechaAt.getYear()==ldtFechaCorte.getYear())
				ldbValor=pagarPorAnyo(iUtilFec.convertirLD(ldtFechaDesde), iUtilFec.convertirLD(ldtFechaCorte), adbMesada, lsPar14);
			else{//1
						
				LocalDate ldtFecha1=ldtFechaDesde,ldtFecha2=LocalDate.now();
				ldtFecha2=LocalDate.of(ldtFechaDesde.getYear(), 12, 31);
				
				for (Integer j = ldtFechaDesde.getYear(); j <= ldtFechaCorte.getYear(); j++){

					
					
					//if (ldtFecha1.compareTo(ldtFechaT)>=0) //mayor o igual 
						ldbValor += pagarPorAnyo(iUtilFec.convertirLD(ldtFecha1),iUtilFec.convertirLD(ldtFecha2),adbMesada,lsPar14); 
						
					//else if (ldtFecha1.getYear()==ldtFechaCorte.getYear() -3)//si es del mismo año hay que calcularlo desde ahí (los tres años para acá)
						//ldbValor += pagarPorAnyo(iUtilFec.convertirLD(ldtFechaT),iUtilFec.convertirLD(ldtFecha2),adbMesada,lsPar14);
					
					if (j<ldtFechaCorte.getYear()){ /// para que no aumente la mesada en el ultimo ciclo
						
						adbMesada = adbMesada * ( 1+ iGeneralesServ.ipc(iUtilFec.convertirLD(LocalDate.of(j,11,30))));
						
						ldbSmin=iGeneralesServ.SMLV(iUtilFec.convertirLD(LocalDate.of(j+1, 12, 30))); 
						if (lbnEsMin)
							adbMesada=ldbSmin;
						else
							if (adbMesada.longValue()< ldbSmin.longValue())
								adbMesada=ldbSmin;
						
						ldtFecha1=LocalDate.of(j+1, 1, 1);
						if (j+1==ldtFechaCorte.getYear())
							ldtFecha2=ldtFechaCorte;
						else{
							ldtFecha2=LocalDate.of(j+1, 12, 31);
						}
					}
				}//fin for
			}//fin 1
			ldbValor=(double) Math.round(ldbValor * abPorcentPj.doubleValue());
			
			
			SaldosReserva saldos=iReservasDao.saldosReserva(alIdSiniestro);
			Double ldbValorActu=0.0;
			
			if (asTipo.equals("I") || asTipo.equals("IJ")){
				Long llNum1=saldos.getConstRI().longValue()-saldos.getReconRI().longValue();
				if (llNum1.compareTo(ldbValor.longValue())!=0){ 
				
					ldbValorActu=ldbValor -(saldos.getConstRI()-saldos.getReconRI());
					iReservasDao.actuRetroPiMaestro(alIdSiniestro, ldbValorActu, asUsuario, asMaquina);
					iReservasDao.actuRetroPiDetalle(alIdSiniestro, alNroMov, ldbValorActu);
				}
			}
			if (asTipo.equals("S") || asTipo.equals("SJ")){
				Long llNum2=saldos.getConstRS().longValue()-saldos.getReconRS().longValue();
				if (llNum2.compareTo(ldbValor.longValue())!=0){ 
				
					ldbValorActu=ldbValor -(saldos.getConstRS()-saldos.getReconRS());
					iReservasDao.actuRetroPsMaestro(alIdSiniestro, ldbValorActu, asUsuario, asMaquina);
					iReservasDao.actuRetroPsDetalle(alIdSiniestro, alNroMov, ldbValorActu);
				}
			}
		}catch(Exception e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error en ajuste de retroactivo siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw (new ServiceException(lsError));
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public Long ajustarReservaManual(Long idSiniestro, String tipoReserva, Double valor, Date adFecha, String tipoMovOtro, String asModuloOrigina, String usuario, String maquina) throws ServiceException{
	
		String lsError;
		MovimientoReserva movReserva = new MovimientoReserva(idSiniestro);
		SaldosReserva saldos=new SaldosReserva(idSiniestro);
		if (iGeneralesServ.estadoSini(idSiniestro).equals("I")){
			lsError=getClass().getName()+" Error- El siniestro se encuentra inactivo";
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
		if ( (tipoReserva.equals("AS") || tipoReserva.equals("HO") || tipoReserva.equals("OT") ) && (tipoMovOtro==null || tipoMovOtro.equals("")) ){
			lsError=getClass().getName()+" Error- El rubro "+tipoReserva+" debe indicar que tipoMovOtr debe mover";
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
		try{
			Double ajAsistencial=0.0, ajIT=0.0, ajIPP=0.0, ajPI=0.0, ajPS=0.0, ajAF=0.0, ajSalud=0.0, ajPension=0.0, ajHon=0.0, ajOtro=0.0,ajRS=0.0, ajRI=0.0;
			
			if(tipoMovOtro != null)
				if(tipoMovOtro.equals("") || tipoMovOtro.equals("-99")) tipoMovOtro=null;
			
			if (valor < 0) {
				lsError="ajustarReservaManual - Error. El valor suministrado es negativo";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			valor = (double)valor.longValue();
			
			iReservasDao.bloqueaTabla(idSiniestro);
			
			EstadoPJ estadoPJ = iGeneralesServ.buscarEstadoPJ(idSiniestro);
			saldos = iReservasDao.saldosReserva(idSiniestro);
			
			//////// Asistencial
			if (tipoReserva.equals("AS")) {
									
				SaldosRubro lSaldoRubro;			
				
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsPa().equals("1")){
					lSaldoRubro=getSaldosJur(idSiniestro, tipoReserva,tipoMovOtro, asModuloOrigina);
				}else{
					lSaldoRubro=getSaldosJur(idSiniestro, tipoReserva,tipoMovOtro, null);
				}
				
				if (valor<lSaldoRubro.getReconocido()){
					lsError="ajustarReservaManual AS - El siniestro "+ idSiniestro +" debe tener por lo menos  " + lSaldoRubro.getReconocido() +" correspondiente a lo reconocido de "+tipoMovOtro;
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajAsistencial=valor - lSaldoRubro.getConstituido();
			}
			
			//////// IT
			if (tipoReserva.equals("IT")) {
				if (valor < saldos.getReconIT()){
					lsError="ajustarReservaManual IT - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconIT();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsIt().equals("1")) {
					 //Si el siniestro está en un PJ debe dejar la plata que reservó juridicos
					Double vrJuridico = iReservasDao.reservaJuridico(idSiniestro, "IT",null);
					if (vrJuridico==null) vrJuridico = 0.0;
					
					if (valor < vrJuridico){
						lsError="ajustarReservaManual IT - El siniestro "+ idSiniestro +" debe tener por lo menos  " + vrJuridico +" correspondientes a Jurídicos, no lo puede ajustar con este valor";
						log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				ajIT = valor - saldos.getConstIT();
			}
			
			//////// IPP
			if (tipoReserva.equals("PP")) {
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsIpp().equals("1") && (!asModuloOrigina.equals("PJ"))) {
					lsError="ajustarReservaManual PP - Error. El siniestro "+idSiniestro+" pertenece a un Proceso Jurídico no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if ( valor < saldos.getReconIPP() ){
					lsError="ajustarReservaManual PP - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconIPP();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajIPP = valor - saldos.getConstIPP();
			}
			
			//////// PI
			if (tipoReserva.equals("PI")) {
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsPi().equals("1") && ( (!asModuloOrigina.equals("PJ") && !asModuloOrigina.equals("PE")))) {
					lsError="ajustarReservaManual PI - Error. El siniestro "+idSiniestro+" pertenece a un Proceso Jurídico no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if ( valor < saldos.getReconPI()){
					lsError="ajustarReservaManual PI - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconPI();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (iReservasDao.tieneMovLiberaMatemat(idSiniestro) > 0 ){
					lsError="ajustarReservaManual PI - Error. El siniestro "+idSiniestro+" ya fue liberado por Matemática no puede modificar la reserva.";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajPI = valor - saldos.getConstPI();
			}
	
			//////// RI
			if (tipoReserva.equals("RI")) {
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsPi().equals("1") && ((!asModuloOrigina.equals("PJ") &&!asModuloOrigina.equals("PE")))) {
					lsError="ajustarReservaManual RI - Error. El siniestro "+idSiniestro+" pertenece a un Proceso Jurídico no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if ( valor < saldos.getReconRI()){
					lsError="ajustarReservaManual RI - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconRI();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (iReservasDao.tieneMovLiberaMatemat(idSiniestro) > 0 ){
					lsError="ajustarReservaManual RI - Error. El siniestro "+idSiniestro+" ya fue liberado por Matemática no puede modificar la reserva.";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajRI = valor - saldos.getConstRI();
			}
			
			//////// PS
			if (tipoReserva.equals("PS")) {
				//if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsPs().equals("1") && ( (!asModuloOrigina.equals("PJ") &&!asModuloOrigina.equals("PE")))) {
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsPs().equals("1") && ( (!asModuloOrigina.equals("PJ") ))) {
					lsError="ajustarReservaManual PS - Error. El siniestro "+idSiniestro+" pertenece a un Proceso Jurídico no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if ( valor < saldos.getReconPS()){
					lsError="ajustarReservaManual PS - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconPS();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (iReservasDao.tieneMovLiberaMatemat(idSiniestro) > 0 ){
					lsError="ajustarReservaManual PS - Error. El siniestro "+idSiniestro+" ya fue liberado por Matemática no puede modificar la reserva.";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajPS = valor - saldos.getConstPS();
			}
	
			//////// RS
			if (tipoReserva.equals("RS")) {
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsPs().equals("1") && ( (!asModuloOrigina.equals("PJ") && !asModuloOrigina.equals("PE")))) {
					lsError="ajustarReservaManual RS - Error. El siniestro "+idSiniestro+" pertenece a un Proceso Jurídico no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if ( valor < saldos.getReconRI()){
					lsError="ajustarReservaManual RS - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconRS();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (iReservasDao.tieneMovLiberaMatemat(idSiniestro) > 0 ){
					lsError="ajustarReservaManual RS - Error. El siniestro "+idSiniestro+" ya fue liberado por Matemática no puede modificar la reserva.";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajRS = valor - saldos.getConstRS();
			}
	
			//////// AF
			if (tipoReserva.equals("AF")) {
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) && estadoPJ.getAsAf().equals("1") && (!asModuloOrigina.equals("PJ"))) {
					lsError="ajustarReservaManual AF - Error. El siniestro "+idSiniestro+" pertenece a un Proceso Jurídico no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if ( valor < saldos.getReconAF()){
					lsError="ajustarReservaManual AF - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconAF();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajAF = valor - saldos.getConstAF();
			}
	
			//////// SALUD
			if (tipoReserva.equals("SA")) {
				if ( valor < saldos.getReconApS()){
					lsError="ajustarReservaManual SALUD - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconApS();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajSalud = valor - saldos.getConstApS();
			}
	
			//////// PENSION
			if (tipoReserva.equals("PE")) {
				if ( valor < saldos.getReconApP()){
					lsError="ajustarReservaManual SALUD - Error. El valor "+valor+" suministrado es menor al reconocido "+ saldos.getReconApP();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajPension = valor - saldos.getConstApP();
			}
	
			//////// Honorarios
			if (tipoReserva.equals("HO")) {
				SaldosRubro lSaldoRubro;			
				
				if (estadoPJ.getAlProceso() > 0 && (!estadoPJ.getAsEstado().toUpperCase().equals("Z")) ){
					if (asModuloOrigina.equals("PJ"))
						lSaldoRubro=getSaldosJur(idSiniestro, tipoReserva,tipoMovOtro, asModuloOrigina);
					else{
						lsError="ajustarReservaManual HO - El siniestro "+ idSiniestro +" es de un proceso juridico, no le puede modificar la reserva desde este módulo";
						log.error(lsError);
						throw new ServiceException(lsError);
					}
				}else{
					lSaldoRubro=getSaldosJur(idSiniestro, tipoReserva,tipoMovOtro, null);
				}
				
				if (valor<lSaldoRubro.getReconocido()){
					lsError="ajustarReservaManual HO - El siniestro "+ idSiniestro +" debe tener por lo menos  " + lSaldoRubro.getReconocido() +" correspondiente a lo reconocido de "+tipoMovOtro;
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajHon=valor - lSaldoRubro.getConstituido();
			}
		
			//////// Otros
			if (tipoReserva.equals("OT")) {
				SaldosRubro lSaldoRubro;			
				
				lSaldoRubro=getSaldosJur(idSiniestro, tipoReserva,tipoMovOtro, null);
								
				if (valor<lSaldoRubro.getReconocido()){
					lsError="ajustarReservaManual OT - El siniestro "+ idSiniestro +" debe tener por lo menos  " + lSaldoRubro.getReconocido() +" correspondiente a lo reconocido de "+tipoMovOtro;
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				ajOtro=valor - lSaldoRubro.getConstituido();
			}
			
			if (	ajAF.equals(0.0) && 		ajIT.equals(0.0) && 		ajRI.equals(0.0) && 			ajIPP.equals(0.0) 
					&& ajPI.equals(0.0) && 		ajPS.equals(0.0) && 		ajAsistencial.equals(0.0) && 	ajSalud.equals(0.0) 
					&& ajPension.equals(0.0) && ajHon.equals(0.0) && 		ajOtro.equals(0.0) &&			ajRS.equals(0.0)) 
				return new Long(0);
			
			LocalDateTime today = LocalDateTime.now();
			Instant instant = today.atZone(ZoneId.systemDefault()).toInstant();
			Date ahora = Date.from(instant);
			
			movReserva.setIdTipoMov("A");
			movReserva.setFechaMovimiento(ahora);
			movReserva.setAuxFunerario(ajAF);
			movReserva.setIt(ajIT);
			movReserva.setIpp(ajIPP);
			movReserva.setPi(ajPI);
			movReserva.setRetroPi(ajRI);
			movReserva.setPs(ajPS);
			movReserva.setRetroPs(ajRS);
			movReserva.setAsistencial(ajAsistencial);
			movReserva.setAportesSalud(ajSalud);
			movReserva.setAportesPension(ajPension);
			movReserva.setHonorarios(ajHon);
			movReserva.setOtros(ajOtro);
			
			movReserva.setUsuarioAud(usuario);
			movReserva.setMaquinaAud(maquina);
			movReserva.setEstadoAud("A");
			movReserva.setOperacionAud("I");
			movReserva.setFechaModificacionAud(ahora);
			
			movReserva.setTipoMovOtr(tipoMovOtro);
			movReserva.setModuloOrigina(asModuloOrigina);
			movReserva.setNroMov(iReservasDao.maxMovRes(idSiniestro));		
	
			iReservasDao.insertMovReserva(movReserva);
			iReservasDao.modificaMaestro(saldos,movReserva);
			
			
		} catch (ServiceException e) {
			lsError=getClass().getName()+": "+ e.getMessage()+" - Error creando movimiento de ajuste siniestro"+idSiniestro;
			log.error(lsError);
			throw new ServiceException(lsError);
		}
		catch (Exception e) {
			lsError=getClass().getName()+": "+ e.getMessage()+" - Error creando movimiento de ajuste siniestro"+idSiniestro;
			log.error(lsError);
			throw new ServiceException(lsError);
		}


		return movReserva.getNroMov();
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarReservaRubro(Long idSiniestro, String asTipoReserva, Double valor, String usuario, String maquina, String moduloOrigen,String asTipoMovOtr) throws ServiceException{
		
		//Insertar Movimiento
		LocalDateTime today = LocalDateTime.now();
		Instant instant = today.atZone(ZoneId.systemDefault()).toInstant();
		Date ahora = Date.from(instant);
		String lsError;
		
		if ( (asTipoReserva.equals("AS") || asTipoReserva.equals("HO") || asTipoReserva.equals("OT")) && (asTipoMovOtr==null || asTipoMovOtr.equals("")) ){
			lsError=getClass().getName()+" El rubro "+asTipoMovOtr+" debe indicar que tipo de movimiento afecta (siniestro: "+idSiniestro+")";
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
		try{
			iReservasDao.bloqueaTabla(idSiniestro);
			
			MovimientoReserva movReserva = new MovimientoReserva(idSiniestro);
			movReserva.setIdTipoMov("A");
			movReserva.setFechaMovimiento(ahora);
			movReserva.setUsuarioAud(usuario);
			movReserva.setMaquinaAud(maquina);
			movReserva.setFechaModificacionAud(ahora);
			movReserva.setModuloOrigina(moduloOrigen);
			movReserva.setTipoMovOtr(asTipoMovOtr);
			
			if (asTipoReserva.equals("AS")) movReserva.setAsistencial(valor);
			if (asTipoReserva.equals("AF")) movReserva.setAuxFunerario(valor);
			if (asTipoReserva.equals("IT")) movReserva.setIt(valor);
			if (asTipoReserva.equals("SA")) movReserva.setAportesSalud(valor);
			if (asTipoReserva.equals("PE")) movReserva.setAportesPension(valor);
			if (asTipoReserva.equals("PP")) movReserva.setIpp(valor);
			if (asTipoReserva.equals("PI")) movReserva.setPi(valor);
			if (asTipoReserva.equals("RI")) movReserva.setRetroPi(valor);
			if (asTipoReserva.equals("PS")) movReserva.setPs(valor);
			if (asTipoReserva.equals("RS")) movReserva.setRetroPs(valor);
			
			if (asTipoReserva.equals("HO")) movReserva.setHonorarios(valor);
			if (asTipoReserva.equals("OT")) movReserva.setOtros(valor);
			
			movReserva.setNroMov(iReservasDao.maxMovRes(idSiniestro));		
			
			iReservasDao.insertMovReserva(movReserva);
			iReservasDao.modificaMaestro(iReservasDao.saldosReserva(idSiniestro),movReserva);
		}catch (Exception e){
			lsError=getClass().getName()+":"+e.getMessage()+"- Error insertando movimiento " + asTipoReserva + " - Siniestro " +idSiniestro;
			log.error(lsError);
			throw new ServiceException(lsError);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarResPensxPension(Long alIdSiniestro,String asTipo,BigDecimal adbPCL,Double adbIBL,Double adbMesada,Double adbRetro,String asUsuario,String asMaquina,String asModulo) throws ServiceException{
		
		// Se utiliza para ajustar reserva de pension de invalidez cuando se liquida en el modulo de pensiones
		String lsError;
		SaldosReserva saldoRes;
		
		if (iGeneralesServ.estadoSini(alIdSiniestro).equals("I")){
			lsError=getClass().getName()+" Error- El siniestro se encuentra inactivo";
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
		ParametrosPensiones lPension=new ParametrosPensiones();
		Siniestro lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
		Double ldbAjPi=0.0,ldbAjPs=0.0,ldbAjRi=0.0,ldbAjRs=0.0;
		saldoRes=iReservasDao.saldosReserva(alIdSiniestro);
		
		Integer liTieneMovM=iReservasDao.tieneMovLiberaMatemat(alIdSiniestro);
		if (liTieneMovM>0){
			lsError="Atencion -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". Ya fue liberado por Matemática no puede modificar la reserva.";
			log.error(lsError);
			//resPI.setValorPI(0.0);
		}else{
			lPension=calcularPension(asTipo, lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), adbMesada, lSiniestro.getFechaAT());
		}
		
		iReservasDao.bloqueaTabla(alIdSiniestro);
		
		Long llNmov=iReservasDao.maxMovRes(alIdSiniestro);
		
		Double ldbAjIpp = saldoRes.getReconIPP() - saldoRes.getConstIPP(); //liberar el rubro IPP
		MovimientoReserva lMovReconNegativo=null;
		
		if (lPension.getValor()==null) lPension.setValor(0.0);
		if (asTipo.equals("I")){
			
			ldbAjPi = lPension.getValor() - saldoRes.getConstPI();
			
			if (adbRetro < saldoRes.getReconRI() && saldoRes.getReconRI()>0.0){
				lMovReconNegativo=new MovimientoReserva(alIdSiniestro);
				lMovReconNegativo.setNroMov(llNmov +1);
				lMovReconNegativo.setIdTipoMov("R");
				lMovReconNegativo.setFechaMovimiento(new Date());
				lMovReconNegativo.setFechaModificacionAud(new Date());
				lMovReconNegativo.setUsuarioAud(asUsuario);
				lMovReconNegativo.setMaquinaAud(asMaquina);
				lMovReconNegativo.setModuloOrigina(asModulo);
				lMovReconNegativo.setRetroPi(adbRetro - saldoRes.getReconRI());
				lMovReconNegativo.setIbc(adbMesada);
			}
			ldbAjRi=adbRetro - saldoRes.getConstRI();
		}else{
			
			ldbAjPs = lPension.getValor() - saldoRes.getConstPS();
			if (adbRetro < saldoRes.getReconRS() && saldoRes.getReconRS()>0.0){
				lMovReconNegativo=new MovimientoReserva(alIdSiniestro);
				lMovReconNegativo.setNroMov(llNmov +1);
				lMovReconNegativo.setIdTipoMov("R");
				lMovReconNegativo.setFechaMovimiento(new Date());
				lMovReconNegativo.setFechaModificacionAud(new Date());
				lMovReconNegativo.setUsuarioAud(asUsuario);
				lMovReconNegativo.setMaquinaAud(asMaquina);
				lMovReconNegativo.setModuloOrigina(asModulo);
				lMovReconNegativo.setRetroPs(adbRetro - saldoRes.getReconRS());
				lMovReconNegativo.setIbc(adbMesada);
			}
			ldbAjRs=adbRetro - saldoRes.getConstRS();
		}
		
		MovimientoReserva lMov=new MovimientoReserva(alIdSiniestro);
		lMov.setNroMov(llNmov);
		lMov.setIdTipoMov("A");
		lMov.setFechaMovimiento(new Date());
		lMov.setFechaModificacionAud(new Date());
		lMov.setUsuarioAud(asUsuario);
		lMov.setMaquinaAud(asMaquina);
		lMov.setModuloOrigina(asModulo);
		lMov.setIpp(ldbAjIpp);
		lMov.setPi(new Double(ldbAjPi.longValue()));
		lMov.setPs(new Double(ldbAjPs.longValue()));
		if (asTipo.equals("I"))
			lMov.setRetroPi(new Double(ldbAjRi.longValue()));
		else
			lMov.setRetroPs(new Double(ldbAjRs.longValue()));
		if (adbPCL!=null) lMov.setPCL(adbPCL.doubleValue());
		lMov.setIbc(adbMesada);
		if (ldbAjIpp==0.0 && ldbAjPs==0.0 && ldbAjRi==0.0 && ldbAjRs==0.0) return;
		iReservasDao.insertMovReserva(lMov);
		iReservasDao.modificaMaestro(saldoRes,lMov);
		
		if (lMovReconNegativo!=null){
			iReservasDao.insertMovReserva(lMovReconNegativo);
			iReservasDao.modificaMaestroRecon(saldoRes,lMovReconNegativo);
		}
		
	}
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void ajustarReservaSOA(Long alIdSiniestro, String asTipoSini, Integer aiParteCuerpo,Integer aiNaturalezaLesion,String asIdDiagnostico,Integer aiMortal,Date adFecha, String asModulo, String asUsuario, String asMaquina) throws ServiceException{
		String lsError;
		try{
			
			Siniestro lSiniestro;
			IbcIbl lIbcIbl=new IbcIbl();
			MovimientoReserva lMovReserva=new MovimientoReserva(alIdSiniestro);
			SaldosReserva lSaldos=iReservasDao.saldosReserva(alIdSiniestro);
			Boolean lbnMoverRi=true,lbnMoverRs=true;
			
			iReservasDao.bloqueaTabla(alIdSiniestro);
			if (iReservasDao.consultarReserva(alIdSiniestro)==0){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". No se ha constituido la reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
			if (lSiniestro.getEstado().equals("I")){
				lsError="Error -----  el siniestro "+alIdSiniestro.toString()+" se encuentra inactivo, no le puede constiuir reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			Double ldbIbcConst=iReservasDao.ibcConst(alIdSiniestro);
			if (ldbIbcConst==null){
				lsError="Error -----  No se puede hayar el IBC de constitución del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			lIbcIbl=iGeneralesServ.traerIbcIbl(lSiniestro);
			Double ldbIblActu=iGeneralesServ.iblActualizado(lIbcIbl.getIbl(), lSiniestro.getFechaAT(), new Date(), "I") ;
			Double ldbSMLV_FAT=iGeneralesServ.SMLV(lSiniestro.getFechaAT());
			EstadoPJ estPj=iGeneralesServ.buscarEstadoPJ(alIdSiniestro);
			Double ldbValor,ldbMesada=0.0;
			ParametrosPensiones lPension=new ParametrosPensiones();
			
			Integer liLiberaMatem=iReservasDao.tieneMovLiberaMatemat(alIdSiniestro);
			
			if (aiMortal==1){
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPa().equals("1")){
					//si el sineistro está en un PJ debe dejar la plata que reservó juridicos, de lo contrario debe liberar el rubro (solo cuando pasa a ser mortal)
					ldbValor=iReservasDao.reservaJuridico(alIdSiniestro,"AS",null);
					if (ldbValor==null) ldbValor=0.0;
					lMovReserva.setAsistencial(lSaldos.getReconAs() + ldbValor - lSaldos.getConstAs());
				}else{
					lMovReserva.setAsistencial(lSaldos.getReconAs() - lSaldos.getConstAs());
				}
				if (lMovReserva.getAsistencial()!=0.0) lMovReserva.setTipoMovOtr("AS");
				
				lMovReserva.setAportesPension(lSaldos.getReconApP() - lSaldos.getConstApP());
				lMovReserva.setAportesSalud(lSaldos.getReconApS() - lSaldos.getConstApS());
				
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsIpp().equals("1")){
					lMovReserva.setIpp(0.0);
				}else{
					lMovReserva.setIpp(lSaldos.getReconIPP() - lSaldos.getConstIPP());
				}
				
				ldbValor=calcularResAFunerario(lIbcIbl.getIbc(), ldbSMLV_FAT,lSiniestro.getFechaAT(),adFecha);
				lMovReserva.setAuxFunerario(ldbValor - lSaldos.getConstAF());
				
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPi().equals("1")){
					lMovReserva.setPi(0.0);
					lMovReserva.setRetroPi(0.0);
				}else{
					lMovReserva.setPi(lSaldos.getReconPI() - lSaldos.getConstPI());
					lMovReserva.setRetroPi(lSaldos.getReconRI() - lSaldos.getConstRI());
					lbnMoverRi=false;
				}
				
				ldbMesada=calcularMesada("S", null, lIbcIbl.getIbl(), ldbSMLV_FAT);
				
				if (liLiberaMatem==0){
					//resPS=calcularResPSobrev(lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT(), iGeneralesServ.SMLV(new Date()), alIdSiniestro);
					lPension=calcularPension("S", lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT());
					if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPs().equals("1")){
						lMovReserva.setPs(0.0);
						lMovReserva.setRetroPs(0.0);
					}else{
						lMovReserva.setPs(lPension.getValor() - lSaldos.getConstPS());
						lMovReserva.setMesada(ldbMesada);
						lMovReserva.setIbc(lIbcIbl.getIbc());
						lMovReserva.setIbl(lIbcIbl.getIbl());
						lMovReserva.setIblActu(ldbIblActu);
					}
				}else{
					lsError="Error -----  el siniestro "+alIdSiniestro.toString()+" ya fue liberado por Matemática, no puede modificar la reserva";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
			}else{//no es mortal
				lMovReserva.setAsistencial(calcularResAsistencial(lSiniestro.getTipo(), lSiniestro.getParteCuerpo(), lSiniestro.getNaturalezaLesion(), lSiniestro.getDiagnostico()));
				if (lMovReserva.getAsistencial()!=0.0) lMovReserva.setTipoMovOtr("AS");
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPa().equals("1")){
					ldbValor=iReservasDao.reservaJuridico(alIdSiniestro, "AS", null);
					if (ldbValor==null) ldbValor=0.0;
					if (lMovReserva.getAsistencial()<lSaldos.getReconAs())
						lMovReserva.setAsistencial(lSaldos.getReconAs() + ldbValor - lSaldos.getConstAs());
					else
						lMovReserva.setAsistencial(lMovReserva.getAsistencial() + ldbValor - lSaldos.getConstAs());
				}else{
					if (lMovReserva.getAsistencial()<lSaldos.getReconAs())
						lMovReserva.setAsistencial(lSaldos.getReconAs() -  lSaldos.getConstAs());
					else
						lMovReserva.setAsistencial(lMovReserva.getAsistencial() - lSaldos.getConstAs());
				}
								
				ValoresITParametrica lValoresIT=calcularReservaIT(lSiniestro.getTipo(),lSiniestro.getParteCuerpo(),lSiniestro.getNaturalezaLesion(),lSiniestro.getDiagnostico(),lIbcIbl.getIbc(),lSiniestro.getFechaAT(),adFecha, iReservasDao.traerPorcentSaludPension(lSiniestro.getFechaAT()));
				if (lValoresIT.getDiasIT().equals(-1)){
					lsError=lValoresIT.getDescripError()+" del siniestro "+alIdSiniestro.toString();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsIt().equals("1")){
					ldbValor=iReservasDao.reservaJuridico(alIdSiniestro, "IT", null);
					if (ldbValor==null) ldbValor=0.0;
					if (lValoresIT.getValorIT()<lSaldos.getReconIT()){
						lMovReserva.setIt(lSaldos.getReconIT() + lValoresIT.getValorIT() -lSaldos.getConstIT());
						lMovReserva.setAportesSalud(lSaldos.getReconApS() + lValoresIT.getValorSalud() - lSaldos.getConstApS() );
						lMovReserva.setAportesPension(lSaldos.getReconApP() + lValoresIT.getValorPension() - lSaldos.getConstApP() );
					}else{
						lMovReserva.setIt(lValoresIT.getValorIT()+ lValoresIT.getValorIT() -lSaldos.getConstIT());
						lMovReserva.setAportesSalud(lValoresIT.getValorSalud() + lValoresIT.getValorSalud() - lSaldos.getConstApS());
						lMovReserva.setAportesPension(lValoresIT.getValorPension() + lValoresIT.getValorPension() - lSaldos.getConstApP());
					}
				}else{
					if (lValoresIT.getValorIT()<lSaldos.getReconIT()){
						lMovReserva.setIt(lSaldos.getReconIT() - lSaldos.getConstIT());
						lMovReserva.setAportesSalud(lSaldos.getReconApS() - lSaldos.getConstApS());
						lMovReserva.setAportesPension(lSaldos.getReconApP() - lSaldos.getConstApP());
					}else{
						lMovReserva.setIt(lValoresIT.getValorIT() - lSaldos.getConstIT());
						lMovReserva.setAportesSalud(lValoresIT.getValorSalud() - lSaldos.getConstApS());
						lMovReserva.setAportesPension(lValoresIT.getValorPension() - lSaldos.getConstApP());
					}
				}
				
				valoresIPPParametrica lValorIpp=calcularResIPPParcial(lSiniestro.getTipo(), lSiniestro.getParteCuerpo(), lSiniestro.getNaturalezaLesion(), lSiniestro.getDiagnostico(), ldbIblActu);
				if (lValorIpp.getPCL().equals(new Double(-1))){
					lsError=lValorIpp.getDescripError()+" del siniestro "+alIdSiniestro.toString();
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setPCL(lValorIpp.getPCL());
				
				if (lValorIpp.getValorIPP()<lSaldos.getReconIPP())
					lMovReserva.setIpp(lSaldos.getReconIPP() - lSaldos.getConstIPP());
				else
					lMovReserva.setIpp(lValorIpp.getValorIPP() - lSaldos.getConstIPP());
				
				if (estPj.getAlProceso()>0 && estPj.getAsAf().equals("1"))
					lMovReserva.setAuxFunerario(0.0);
				else
					lMovReserva.setAuxFunerario(-lSaldos.getConstAF());
			
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPi().equals("1")){
					lMovReserva.setPi(0.0);
					lMovReserva.setRetroPi(0.0);
				}else{
					if (lValorIpp.getPCL()>=50.0){
						
						ldbMesada=calcularMesada("I", lValorIpp.getPCL(), lIbcIbl.getIbl(), ldbSMLV_FAT);

						if (liLiberaMatem>0){
							lsError="Error -----  el siniestro "+alIdSiniestro.toString()+" ya fue liberado por Matemática, no puede modificar la reserva";
							log.error(lsError);
							throw new ServiceException(lsError);
						}
						//resPI=calcularResPInvalidez(lSiniestro.getDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT(), new BigDecimal(lValorIpp.getPCL()), ldbSMLV_FAT, alIdSiniestro);
						lPension=calcularPension("I", lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT());
						lMovReserva.setMesada(ldbMesada);
						lMovReserva.setIbc(lIbcIbl.getIbc());
						lMovReserva.setIbl(lIbcIbl.getIbl());
						lMovReserva.setIblActu(ldbIblActu);
					}
					lMovReserva.setPi(lPension.getValor() - lSaldos.getConstPI());
				}
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPs().equals("1")){
					lMovReserva.setPs(0.0);
					lMovReserva.setRetroPs(0.0);
				}else{
					lMovReserva.setPs(lSaldos.getReconPS() - lSaldos.getConstPS());
					lMovReserva.setRetroPs(lSaldos.getReconRS() -lSaldos.getConstRS());
					lbnMoverRs=false;
				}
			}//fin if si es mortal o no
			Long llNmov = iReservasDao.maxMovRes(alIdSiniestro);
			lMovReserva.setNroMov(llNmov);
			lMovReserva.setFechaMovimiento(adFecha);
			lMovReserva.setFechaModificacionAud(adFecha);
			lMovReserva.setUsuarioAud(asUsuario);
			lMovReserva.setMaquinaAud(asMaquina);
			lMovReserva.setModuloOrigina(asModulo);
			lMovReserva.setIdTipoMov("A");
			
			
			iReservasDao.insertMovReserva(lMovReserva);
			
			LocalDate ldUltimoDiaMes=ultimoDiaMes(adFecha);
			
			
			if (lMovReserva.getPi()>0.0 && lbnMoverRi){
				ajusteRetroactivo(alIdSiniestro, lSiniestro.getFechaAT(), iUtilFec.convertirLD(ldUltimoDiaMes), lSiniestro.getFechaMuerte(), ldbSMLV_FAT, ldbMesada, "I", llNmov, new BigDecimal(1), asModulo, asUsuario, asMaquina);
			}
			if (lMovReserva.getPs()>0.0 && lbnMoverRs){
				ajusteRetroactivo(alIdSiniestro, lSiniestro.getFechaAT(), iUtilFec.convertirLD(ldUltimoDiaMes), lSiniestro.getFechaMuerte(), ldbSMLV_FAT, ldbMesada, "S", llNmov, new BigDecimal(1), asModulo, asUsuario, asMaquina);
			}
			
			iReservasDao.modificaMaestro(lSaldos,lMovReserva);
			
		}catch(Exception e){
			lsError=getClass().getName()+":"+e.getMessage()+"- Error ajustando reserva SOA - Siniestro " +alIdSiniestro;
			log.error(lsError);
			throw new ServiceException(lsError);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	private ParametrosPensiones beneficiarios(String asTdocAfil,String asDocAfil,ArrayList<BeneficiarioResMatem> aaBenef,String asTipoPension,Date adFechaSini,Date adFechaCorte,String asSexo,Date adFechaNaceCau){
		ParametrosPensiones lParam=new ParametrosPensiones();

		LocalDate lcdFecha25=null, lcdTiempo0=null, lcdFechaCorte=iUtilFec.convertirDateLD(adFechaCorte);
		int  z,m, k;
		
		//  CÓDIGO	        PARENTESCO
		//		1	Cónyuge o Compañero(a)
		//		2	Hijo(a)
		//		3	Padre o Madre
		//		4	2do Grado de consanguinidad
		//		5	3er Grado de consanguinidad
		//		6	Menor 12 años sin consanguinidad
		//		7	Padre o Madre del conyugue
		//		8	Hijos Inválidos
		//		9	Hermanos Inválidos
		//		15	Otros

		lcdTiempo0= LocalDate.of(2010,9,30);
		
		lParam.setFnace(new ArrayList<Date>());
		lParam.setParentesco(new ArrayList<String>());
		lParam.setIndicador(new ArrayList<String>());
		lParam.setEdad(new ArrayList<Double>());
		lParam.setEdad10(new ArrayList<Double>());
		lParam.setTipopersona(new ArrayList<String>());
		lParam.setTipopersona10(new ArrayList<String>());
		lParam.setLimit(new ArrayList<Double>());
		lParam.setLimit10(new ArrayList<Double>());
		lParam.setMortalidad(new ArrayList<Double>());
		lParam.setMortalidad10_rv89(new ArrayList<Double>());
		lParam.setMortalidad10_rv08(new ArrayList<Double>());
		lParam.setNombre(new ArrayList<String>());
		
		for ( z = 0; z < 3; z++) {
			lParam.getFnace().add(null);
			lParam.getParentesco().add(null);
			lParam.getIndicador().add(null);
			lParam.getEdad().add(null);
			lParam.getEdad10().add(null);
			lParam.getTipopersona().add(null);
			lParam.getTipopersona10().add(null);
			lParam.getLimit().add(null);
			lParam.getLimit10().add(null);
			lParam.getMortalidad().add(null);
			lParam.getMortalidad10_rv89().add(null);
			lParam.getMortalidad10_rv08().add(null);
			lParam.getNombre().add(null);
		}
		
		if (asTipoPension.equals("S")){ // Afiliado Fallecido
			k = 0;
			for (int j = 0 ;  j< aaBenef.size() ;j++){ 
				if (k > 2) break;
				
				// Parentesco 4 = Hijos - según correo 18 Feb 2013, dejo el 7 por si acaso
				
				if (aaBenef.get(j).getParentesco()==null) continue;
				
				if (aaBenef.get(j).getParentesco()==4 || aaBenef.get(j).getParentesco()==7){
					// Hijo estudiante o Esposa mayor 25 sin hijos
					if (aaBenef.get(j).getParentesco()==4)// Hijo estudiante
						lcdFecha25 = iUtilFec.convertirDateLD(aaBenef.get(j).getFECHA_NACIMIENTO()).plusYears(25);

					else if (aaBenef.get(j).getParentesco()==7) // Esposa sin hijos
						lcdFecha25 = iUtilFec.convertirDateLD(adFechaSini).plusYears(20);
					
					if (lcdFecha25.compareTo(iUtilFec.convertirDateLD(adFechaCorte))<0) continue;
					
				}
				
				lParam.getEdad().set(k, calcularEdad(aaBenef.get(j).getFECHA_NACIMIENTO(), adFechaCorte));
				lParam.getMortalidad().set(k, aaBenef.get(j).getMortalidad());
				lParam.getTipopersona().set(k, aaBenef.get(j).getTipo_persona());
				lParam.getEdad10().set(k, aaBenef.get(j).getEdad2010());
				lParam.getMortalidad10_rv89().set(k, aaBenef.get(j).getMortalidad2010RV89());
				lParam.getMortalidad10_rv08().set(k, aaBenef.get(j).getMortalidad2010RV08());
				lParam.getIndicador().set(k, "2");
				lParam.getFnace().set(k,aaBenef.get(j).getFECHA_NACIMIENTO());
				lParam.getParentesco().set(k,parentescoLRS(aaBenef.get(j).getParentesco(), aaBenef.get(j).getSexo()).toString());
				lParam.getNombre().set(k, iReservasDao.nombreBeneficiario(asTdocAfil, asDocAfil, aaBenef.get(j).getID_TIPO_DOC_BEN(), aaBenef.get(j).getID_BENEFICIARIO()));
				
				if (aaBenef.get(j).getParentesco()==4 || aaBenef.get(j).getParentesco()==7){// Hijo estudiante
					lParam.getLimit().set(k, (lcdFecha25.getYear() - lcdFechaCorte.getYear())*12.0 + lcdFecha25.getMonthValue() - lcdFechaCorte.getMonthValue() );
					
					if (lcdFecha25.getDayOfMonth() - lcdFechaCorte.getDayOfMonth()>=0) lParam.getLimit().set(k, lParam.getLimit().get(k)+1);
					
					lParam.getLimit10().set(k, (lcdFecha25.getYear() - lcdTiempo0.getYear())*12.0 + lcdFecha25.getMonthValue() - lcdTiempo0.getMonthValue() );
					
					if (lcdFecha25.getDayOfMonth() - lcdTiempo0.getDayOfMonth()>=0) lParam.getLimit10().set(k, lParam.getLimit10().get(k)+1);
					
				}else{// Pension Vitalicia
					lParam.getLimit().set(k, 1000000.0);
					lParam.getLimit10().set(k, 1000000.0);
				}
				lParam.getTipopersona10().set(k,lParam.getTipopersona().get(k));
				k ++;
			}//fin for j
			
		}else{//Afiliado vivo
			lParam.getEdad().set(0, calcularEdad(adFechaNaceCau,adFechaCorte));
			if (asSexo.toUpperCase().equals("M")){			//Masculino
				lParam.getTipopersona().set(0, "HI");
				lParam.getTipopersona10().set(0, "HI");
			}else if (asSexo.toUpperCase().equals("F")){		//Femenino
				lParam.getTipopersona().set(0, "MI");
				lParam.getTipopersona10().set(0, "MI");
			}
			lParam.getMortalidad().set(0, mortalidadVersion( "RV08", lParam.getTipopersona().get(0), lParam.getEdad().get(0)));
			
			lParam.getEdad10().set(0, calcularEdad(adFechaNaceCau,iUtilFec.convertirLD(lcdTiempo0)));
			lParam.getMortalidad10_rv89().set(0, mortalidadVersion("RV89", lParam.getTipopersona10().get(0), lParam.getEdad10().get(0)));
			lParam.getMortalidad10_rv08().set(0, mortalidadVersion("RV08", lParam.getTipopersona10().get(0), lParam.getEdad10().get(0)));
			lParam.getLimit().set(0, 1000000.0);
			lParam.getLimit10().set(0, 1000000.0);
			lParam.getIndicador().set(0, "2");//datos reales
			lParam.getFnace().set(0,adFechaNaceCau);
			lParam.getParentesco().set(0, "1");
			
			k = 1;
			for (int j = 0 ; j< aaBenef.size() ;j ++){
				if (k > 2) break;		
				
				// Parentesco 4 = Hijos - según correo 18 Feb 2013, dejo el 7 por si acaso
				if (aaBenef.get(j).getParentesco()==4 || aaBenef.get(j).getParentesco()==7){ // Hijo estudiante o Esposa mayor 25 sin hijos
					if (aaBenef.get(j).getParentesco()==4){ // Hijo estudiante
						lcdFecha25 =iUtilFec.convertirDateLD(aaBenef.get(j).getFECHA_NACIMIENTO()) ;
						lcdFecha25=lcdFecha25.plusYears(25);

					}else if (aaBenef.get(j).getParentesco()==7){ // Esposa sin hijos
						lcdFecha25 = iUtilFec.convertirDateLD(adFechaSini);
						lcdFecha25 = lcdFecha25.plusYears(20);
					}
					if (lcdFecha25.compareTo(lcdFechaCorte) < 0) continue;
					
				}
		
				if (aaBenef.get(j).getParentesco()==null) continue; 

				lParam.getEdad().set(k, aaBenef.get(j).getEdad());
				lParam.getMortalidad().set(k, aaBenef.get(j).getMortalidad());
				lParam.getTipopersona().set(k, aaBenef.get(j).getTipo_persona());
				lParam.getEdad10().set(k, aaBenef.get(j).getEdad2010());
				lParam.getMortalidad10_rv89().set(k,aaBenef.get(j).getMortalidad2010RV89());
				lParam.getMortalidad10_rv08().set(k,aaBenef.get(j).getMortalidad2010RV08());
				lParam.getIndicador().set(k,"2"); //datos reales
				lParam.getFnace().set(k, aaBenef.get(j).getFECHA_NACIMIENTO());
				lParam.getParentesco().set(k, parentescoLRS(aaBenef.get(j).getParentesco(), aaBenef.get(j).getSexo()).toString());
				lParam.getNombre().set(k,iReservasDao.nombreBeneficiario(asTdocAfil, asDocAfil, aaBenef.get(j).getID_TIPO_DOC_BEN(), aaBenef.get(j).getID_BENEFICIARIO()));
						
				
				if (aaBenef.get(j).getParentesco()==4 || aaBenef.get(j).getParentesco()==7){
					lParam.getLimit().set(k, (lcdFecha25.getYear() - lcdFechaCorte.getYear() )*12.0 +lcdFecha25.getMonthValue() - lcdFechaCorte.getMonthValue());
					
					if (lcdFecha25.getDayOfMonth() - lcdFechaCorte.getDayOfMonth() >=0 ) lParam.getLimit().set(k, lParam.getLimit().get(k) +1);
					
					lParam.getLimit10().set(k, (lcdFecha25.getYear() - lcdTiempo0.getYear() )*12.0 +lcdFecha25.getMonthValue() - lcdTiempo0.getMonthValue());
					
					if (lcdFecha25.getDayOfMonth() - lcdTiempo0.getDayOfMonth()>=0) lParam.getLimit10().set(k, lParam.getLimit10().get(k) +1);
				}else{// Pension Vitalicia
					lParam.getLimit().set(k, 1000000.0);
					lParam.getLimit10().set(k, 1000000.0);
				}
				
				lParam.getTipopersona10().set(k, lParam.getTipopersona().get(k));
				k++;
			}//for
			
		}//fin afiliado vivo
		
		// Caso en que no ha nacido a corte 2010, pero si a corte actual
		z=0;
		while (z<=2){
			if ((lParam.getEdad10().get(z)!=null && lParam.getEdad10().get(z)<0.0) || (lParam.getLimit10().get(z)!=null && lParam.getLimit10().get(z)<0.0)){
				for (m=z;m<2;m++){
					lParam.getEdad10().set(m, lParam.getEdad10().get(m+1));
					lParam.getTipopersona10().set(m, lParam.getTipopersona10().get(m+1));
					lParam.getLimit10().set(m, lParam.getLimit10().get(m+1));
					lParam.getMortalidad10_rv89().set(m, lParam.getMortalidad10_rv89().get(m+1));
					lParam.getMortalidad10_rv08().set(m, lParam.getMortalidad10_rv08().get(m+1));
					lParam.getIndicador().set(m, lParam.getIndicador().get(m+1));
					lParam.getFnace().set(m, lParam.getFnace().get(m+1));
					lParam.getParentesco().set(m, lParam.getParentesco().get(m +1));
					lParam.getNombre().set(m, lParam.getNombre().get(m+1));
					
				}
				lParam.getEdad10().set(2, null);
				lParam.getTipopersona10().set(2, null);
			}else{
				z++;
			}
		}
			
		//Pensión de invalidez, si no tiene un beneficiario se asume un supuesto a 2010
		if (lParam.getEdad10().get(1)==null && asTipoPension.equals("I")){ 
		
			if (asSexo.toUpperCase().equals("M")){
				lParam.getEdad10().set(1, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)),iUtilFec.convertirLD(LocalDate.of(2010, 9, 30))));
				
				lParam.getFnace().set(1,iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)));
				
				lParam.getTipopersona10().set(1, "MV");
			}else{
				lParam.getEdad10().set(1, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)),iUtilFec.convertirLD(LocalDate.of(2010, 9, 30))));
				lParam.getFnace().set(1,iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)));
				lParam.getTipopersona10().set(1, "HV");
			}
			lParam.getMortalidad10_rv89().set(1, mortalidadVersion("RV89", lParam.getTipopersona10().get(1), lParam.getEdad10().get(1)));
			lParam.getMortalidad10_rv08().set(1, mortalidadVersion("RV08", lParam.getTipopersona10().get(1), lParam.getEdad10().get(1)));
			lParam.getLimit10().set(1, 1000000.0);
			lParam.getIndicador().set(1,"1");// Datos del beneficiario son estimados por la aseguradora
  
			lParam.getParentesco().set(1, "4");  //Conyuge
			lParam.getNombre().set(1, "Desconocido");
		}
			
		//Pensión de sobrevivencia, si no tiene un beneficiario se asume un supuesto a 2010
		if (lParam.getEdad10().get(0)==null && asTipoPension.equals("S")){
		
			if (asSexo.toUpperCase().equals("M")){
				lParam.getEdad10().set(0, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)),iUtilFec.convertirLD(LocalDate.of(2010, 9, 30))));
				
				lParam.getFnace().set(0,iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)));  
						
				lParam.getTipopersona10().set(0, "MV");
			}else{
				lParam.getEdad10().set(0, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)),iUtilFec.convertirLD(LocalDate.of(2010, 9, 30))));
				
				lParam.getFnace().set(0,iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)));  
				lParam.getTipopersona10().set(0, "HV");
			}
			lParam.getMortalidad10_rv89().set(0, (lParam.getTipopersona10().get(0)!=null && lParam.getEdad10().get(0)!=null)?mortalidadVersion("RV89",lParam.getTipopersona10().get(0),lParam.getEdad10().get(0)):0.0);
			lParam.getMortalidad10_rv08().set(0, (lParam.getTipopersona10().get(0)!=null && lParam.getEdad10().get(0)!=null)?mortalidadVersion("RV08",lParam.getTipopersona10().get(0),lParam.getEdad10().get(0)):0.0);
			lParam.getLimit10().set(0, 1000000.0);
			lParam.getIndicador().set(0,"1");// Datos del beneficiario son estimados por la aseguradora
  
			lParam.getParentesco().set(0, "4");  //Conyuge
			lParam.getNombre().set(0, "Desconocido");
		}
		//// Caso en que no hay beneficiarios a corte
		//Pensión de invalidez, si no tiene un beneficiario se asume un supuesto
		if (lParam.getEdad().get(1)==null && asTipoPension.equals("I")){
			if (asSexo.toUpperCase().equals("M")){
				lParam.getEdad().set(1, calcularEdad( iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)),adFechaCorte));
				lParam.getFnace().set(1, iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)));		
				lParam.getTipopersona().set(1, "MV");		
			}else{
				lParam.getEdad().set(1, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)),adFechaCorte));
				lParam.getFnace().set(1, iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)));		
				lParam.getTipopersona().set(1, "HV");
			}
			lParam.getMortalidad().set(1, mortalidadVersion("RV08", lParam.getTipopersona().get(1), lParam.getEdad().get(1)));
			lParam.getLimit().set(1, 1000000.0);
			lParam.getIndicador().set(1, "1");     // Datos del beneficiario son estimados por la asegurador
			lParam.getParentesco().set(1, "4");   //Conyuge
			lParam.getNombre().set(1,"Desconocido");
		}
		
		//Pensión de sobrevivencia, si no tiene un beneficiario se asume un supuesto
		if (lParam.getEdad().get(0)==null && asTipoPension.equals("S")){
			if (asSexo.toUpperCase().equals("M")){
				lParam.getEdad().set(0, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)),iUtilFec.convertirLD(lcdFechaCorte))); 
						
				lParam.getFnace().set(0, iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).plusYears(5)));  
						
				lParam.getTipopersona().set(0,"MV");
			}else{
				lParam.getEdad().set(0, calcularEdad(iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)),iUtilFec.convertirLD(lcdFechaCorte))); 
				
				lParam.getFnace().set(0, iUtilFec.convertirLD(iUtilFec.convertirDateLD(adFechaNaceCau).minusYears(5)));  
						
				lParam.getTipopersona().set(0,"HV");
			}
			lParam.getMortalidad().set(0, mortalidadVersion("RV08", lParam.getTipopersona().get(0), lParam.getEdad().get(0)));
			lParam.getLimit().set(0, 1000000.0);
			lParam.getIndicador().set(0, "1");     // Datos del beneficiario son estimados por la asegurador
			lParam.getParentesco().set(0, "4");   //Conyuge
			lParam.getNombre().set(0,"Desconocido");
		}
		
		Double ldbTemp;
		String lsTemp;
		Date ldTemp;

		//Para sobrevivencia solo deben ir dos beneficiarios
		if  (asTipoPension.equals("S")  && lParam.getEdad().get(2)!=null && lParam.getEdad().get(2) > 0.0){
			lParam.getEdad().set(2, null);
			lParam.getLimit().set(2,null);
		}
		if  (asTipoPension.equals("S")  && lParam.getEdad10().get(2)!=null && lParam.getEdad10().get(2) > 0.0){
			lParam.getEdad10().set(2, null);
			lParam.getLimit10().set(2, null);
		}
		// Si el beneficiario temporal es mas costoso que el vitalicio debe cambiarse el orden
		if ( asTipoPension.equals("S") && lParam.getEdad().get(2)==null && lParam.getEdad().get(1)!=null && (lParam.getLimit().get(0)!=null && lParam.getLimit().get(0) < 1000000.0) && (lParam.getLimit().get(1)!=null && lParam.getLimit().get(1)== 1000000.0) ){
			ldbTemp = lParam.getEdad().get(0);
			lParam.getEdad().set(0, lParam.getEdad().get(1));
			lParam.getEdad().set(1,  ldbTemp);
			lsTemp = lParam.getTipopersona().get(0);
			lParam.getTipopersona().set(0,lParam.getTipopersona().get(1));
			lParam.getTipopersona().set(1, lsTemp);
			ldbTemp = lParam.getMortalidad().get(0);
			lParam.getMortalidad().set(0, lParam.getMortalidad().get(1));
			lParam.getMortalidad().set(1,  ldbTemp);
			ldbTemp = lParam.getLimit().get(0);
			lParam.getLimit().set(0, lParam.getLimit().get(1));
			lParam.getLimit().set(1, ldbTemp);
			lsTemp = lParam.getIndicador().get(0);
			lParam.getIndicador().set(0, lParam.getIndicador().get(1));
			lParam.getIndicador().set(1, lsTemp);
			lsTemp = lParam.getParentesco().get(0);
			lParam.getParentesco().set(0, lParam.getParentesco().get(1));
			lParam.getParentesco().set(1,  lsTemp);
			ldTemp = lParam.getFnace().get(0);
			lParam.getFnace().set(0,lParam.getFnace().get(1));
			lParam.getFnace().set(1,  ldTemp);
			lsTemp = lParam.getNombre().get(0);
			lParam.getNombre().set(0, lParam.getNombre().get(1));
			lParam.getNombre().set(1,  lsTemp);	
	
		}else if (asTipoPension.equals("I") && lParam.getEdad().get(2)!=null && (lParam.getLimit().get(1)!=null && lParam.getLimit().get(1) < 1000000.0) && (lParam.getLimit().get(2)!=null && lParam.getLimit().get(2)== 1000000.0)){
			ldbTemp = lParam.getEdad().get(1);
			lParam.getEdad().set(1, lParam.getEdad().get(2));
			lParam.getEdad().set(2, ldbTemp);
			lsTemp = lParam.getTipopersona().get(1);
			lParam.getTipopersona().set(1, lParam.getTipopersona().get(2));
			lParam.getTipopersona().set(2, lsTemp);
			ldbTemp = lParam.getMortalidad().get(1);
			lParam.getMortalidad().set(1, lParam.getMortalidad().get(2));
			lParam.getMortalidad().set(2,ldbTemp);
			ldbTemp = lParam.getLimit().get(1);
			lParam.getLimit().set(1,lParam.getLimit().get(2));
			lParam.getLimit().set(2, ldbTemp);
			lsTemp = lParam.getIndicador().get(1);
			lParam.getIndicador().set(1, lParam.getIndicador().get(2));
			lParam.getIndicador().set(2, lsTemp);
			lsTemp = lParam.getParentesco().get(1);
			lParam.getParentesco().set(1,lParam.getParentesco().get(2));
			lParam.getParentesco().set(2,  lsTemp);
			ldTemp = lParam.getFnace().get(1);
			lParam.getFnace().set(1,lParam.getFnace().get(2));
			lParam.getFnace().set(2, ldTemp);
			lsTemp = lParam.getNombre().get(1);
			lParam.getNombre().set(1 , lParam.getNombre().get(2));
			lParam.getNombre().set(2,lsTemp);
		}
		
		if ( asTipoPension.equals("S") && lParam.getEdad10().get(2)==null && lParam.getEdad10().get(1)!=null && (lParam.getLimit10().get(0)!=null && lParam.getLimit10().get(0)< 1000000.0) && (lParam.getLimit10().get(1)!=null && lParam.getLimit10().get(1)== 1000000.0) ) {
			ldbTemp = lParam.getEdad10().get(0);
			lParam.getEdad10().set(0, lParam.getEdad().get(1));
			lParam.getEdad10().set(1, ldbTemp);
			lsTemp = lParam.getTipopersona10().get(0);
			lParam.getTipopersona10().set(0,lParam.getTipopersona10().get(1));
			lParam.getTipopersona10().set(1,lsTemp);
			ldbTemp = lParam.getMortalidad10_rv08().get(0);
			lParam.getMortalidad10_rv08().set(0,lParam.getMortalidad10_rv08().get(1));
			lParam.getMortalidad10_rv08().set(1, ldbTemp);
			ldbTemp = lParam.getMortalidad10_rv89().get(0);
			lParam.getMortalidad10_rv89().set(0, lParam.getMortalidad10_rv89().get(1));
			lParam.getMortalidad10_rv89().set(1,ldbTemp);
			ldbTemp = lParam.getLimit10().get(0);
			lParam.getLimit10().set(0,lParam.getLimit10().get(1));
			lParam.getLimit10().set(1, ldbTemp);
			lsTemp = lParam.getNombre().get(0);
			lParam.getNombre().set(0,lParam.getNombre().get(1));
			lParam.getNombre().set(1, lsTemp);
		}else if ( (asTipoPension.equals("I") && lParam.getEdad10().get(2)!=null && (lParam.getLimit10().get(1)!=null && lParam.getLimit10().get(1)< 1000000.0) && (lParam.getLimit10().get(2)!=null && lParam.getLimit10().get(2) == 1000000.0)) || ( asTipoPension.equals("S") && lParam.getEdad10().get(1)!=null && (lParam.getLimit10().get(0)!=null && lParam.getLimit10().get(0) < 1000000.0) && (lParam.getLimit10().get(1)!=null && lParam.getLimit10().get(1)== 1000000.0)) ){
			ldbTemp = lParam.getEdad10().get(1);
			lParam.getEdad10().set(1, lParam.getEdad10().get(2));
			lParam.getEdad10().set(2,ldbTemp);
			lsTemp = lParam.getTipopersona10().get(1);
			lParam.getTipopersona10().set(1, lParam.getTipopersona10().get(2));
			lParam.getTipopersona10().set(2,lsTemp);
			ldbTemp = lParam.getMortalidad10_rv08().get(1);
			lParam.getMortalidad10_rv08().set(1,lParam.getMortalidad10_rv08().get(2));
			lParam.getMortalidad10_rv08().set(2,ldbTemp);
			ldbTemp = lParam.getMortalidad10_rv89().get(1);
			lParam.getMortalidad10_rv89().set(1, lParam.getMortalidad10_rv89().get(2));
			lParam.getMortalidad10_rv89().set(2, ldbTemp);
			ldbTemp = lParam.getLimit10().get(1);
			lParam.getLimit10().set(1, lParam.getLimit10().get(2));
			lParam.getLimit10().set(2, ldbTemp);
			lsTemp = lParam.getNombre().get(1);
			lParam.getNombre().set(1,lParam.getNombre().get(2));
			lParam.getNombre().set(2, lsTemp);
		}
		
		return lParam;
	}

	private Double calcularEdad(Date adFechaNace,Date adFechaCorte){
		LocalDate lcdFechaNace=iUtilFec.convertirDateLD(adFechaNace);
		LocalDate lcdFechaCorte=iUtilFec.convertirDateLD(adFechaCorte);
		LocalDate lcdTemp=ultimoDiaMes(adFechaCorte);
		
		Double ldbEdad;
		
		ldbEdad = lcdFechaCorte.getYear() - lcdFechaNace.getYear() + ( lcdFechaCorte.getMonthValue() - lcdFechaNace.getMonthValue() + new Double(lcdFechaCorte.getDayOfMonth() - lcdFechaNace.getDayOfMonth())/new Double(lcdTemp.getDayOfMonth()) )/12.0;
				
		return ldbEdad;
	}
	
	private Double calcularMesada(String asTipo,Double adbPCL,Double adbIBL,Double adbSMLV_FAT){
		Double ldbMesada;
		if (asTipo.equals("I")){

			if (adbPCL<=66.0){
				if (0.6*adbIBL<adbSMLV_FAT)
					ldbMesada=adbSMLV_FAT;
				else
					ldbMesada=0.6*adbIBL;
			}else{
				if (0.75 * adbIBL<adbSMLV_FAT) 
					ldbMesada= adbSMLV_FAT;
				else
					ldbMesada= 0.75 * adbIBL;
			}
		}else {
			if (0.75 * adbIBL < adbSMLV_FAT)
				ldbMesada= adbSMLV_FAT;
			else
				ldbMesada=0.75 * adbIBL;
		}
		return ldbMesada;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public ParametrosPensiones calcularPension(String asTipoPension,String asTDoc,String asDocu,Double adbIbl,Date adFechaSini){
		
		Double ldbMesada,	ldbMesada2010=0.0,	ldbValor,	ldbSminSin,	ldbSmin,	ldbSmin2010;
		String lsPar14,lsError;
		Date ldFCorte;
		Integer liMesCorte;
		Integer liT;
		LocalDate lcd2010_09_30=LocalDate.of(2010, 9, 30);
		LocalDate lcdFechaSini=iUtilFec.convertirDateLD(adFechaSini);
		IyK lIyK;
		ParametrosPensiones lBenefVal=new ParametrosPensiones();
		
		ldFCorte=iUtilFec.convertirLD(ultimoDiaMes(new Date()));
		LocalDate lcdFcorte=iUtilFec.convertirDateLD(ldFCorte);
		
		ldbMesada=adbIbl;
		ldbSminSin=iGeneralesServ.SMLV(adFechaSini);
		ldbSmin=iGeneralesServ.SMLV(ldFCorte);
		ldbSmin2010=iGeneralesServ.SMLV(iUtilFec.convertirLD(lcd2010_09_30));
		
		
		// Calcula mesada a fecha de corte y 2010
		
		if (ldbMesada<=ldbSminSin){
			ldbMesada=ldbSminSin;
			if (lcdFechaSini.getYear()!=lcdFcorte.getYear()) ldbMesada=iGeneralesServ.mesadaActual(ldbMesada,adFechaSini,ldFCorte,true);
			if (lcdFechaSini.compareTo(lcd2010_09_30)<0) ldbMesada2010=iGeneralesServ.mesadaActual(ldbSminSin,adFechaSini,iUtilFec.convertirLD(lcd2010_09_30),true);
		}else{
			if (lcdFechaSini.getYear()!=lcdFcorte.getYear()) ldbMesada=iGeneralesServ.mesadaActual(ldbMesada,adFechaSini,ldFCorte,false);
			if (lcdFechaSini.compareTo(lcd2010_09_30)<0) ldbMesada2010=iGeneralesServ.mesadaActual(adbIbl,adFechaSini,iUtilFec.convertirLD(lcd2010_09_30),false);
		}
		
		ldbMesada = round2(ldbMesada);
		ldbMesada2010 = round2(ldbMesada2010);
		
		Afiliado lAfil=iGeneralesServ.findAfiliado(asTDoc, asDocu);
		
		if (lAfil==null){
			lBenefVal.setValor(-1.0);
			lBenefVal.setMensaje("No existe el afiliado "+asTDoc+"-"+asDocu);
			return lBenefVal;
		}
		
		liMesCorte=lcdFcorte.getMonthValue();
		
		ArrayList<BeneficiarioResMatem> lArrayBenef=iReservasDao.traerBeneficiarios(lAfil.getIdTipoDoc(), lAfil.getIdPersona(), ldFCorte);
		
		lBenefVal=beneficiarios(asTDoc,asDocu,lArrayBenef,asTipoPension,adFechaSini,ldFCorte,lAfil.getSexo(),lAfil.getFechaNacimiento());
		
		lBenefVal.setMesada(ldbMesada);
		
		if (lcdFechaSini.compareTo(LocalDate.of(2005,7,25))<0 || (lcdFechaSini.compareTo(LocalDate.of(2005,7,25))>=0 && lcdFechaSini.compareTo(LocalDate.of(2011, 7, 31))<=0 && ldbMesada<=3*ldbSmin)){
			lsPar14="1";
			lBenefVal.setMesadas(14);
		}else{
			lsPar14="0";
			lBenefVal.setMesadas(13);
		}
		
		
		Double ldbA, ldbIncSalud, ldbRV08, ldbRV08_2010, ldbRV89_2010;
		
		ldbA = Double.min(Double.max(ldbMesada, 5.0*ldbSmin),10.0*ldbSmin);
		
		if (lcdFechaSini.compareTo(LocalDate.of(1994,1,1))<0)
			ldbIncSalud = ldbMesada * 0.0804;
		else
			ldbIncSalud = 0.0;

		lIyK=iReservasDao.buscarIyK(ldFCorte);
		
		PensionNewNota lPension=iReservasDao.pension2b(lAfil.getIdTipoDoc(), lAfil.getIdPersona(), asTipoPension, lBenefVal.getEdad().get(0), lBenefVal.getEdad().get(1), lBenefVal.getEdad().get(2), lBenefVal.getTipopersona().get(0), lBenefVal.getTipopersona().get(1), lBenefVal.getTipopersona().get(2),  "0", ldbMesada, lsPar14, liMesCorte	, lIyK.getK(), ldbSmin, lIyK.getI(), adFechaSini, ldbA, lBenefVal.getMortalidad().get(0), lBenefVal.getMortalidad().get(1), lBenefVal.getMortalidad().get(2), lBenefVal.getLimit().get(0), lBenefVal.getLimit().get(1), lBenefVal.getLimit().get(2), "RV08", ldbIncSalud, 0.0);
		
		if (lPension==null){
			lsError="Error trayendo valores de pension";
			log.error(lsError);
			lBenefVal.setValor(-1.0);
			lBenefVal.setMensaje(lsError);
			return lBenefVal;
		}
		
		ldbRV08=totalPension(lPension);
			
		if (lcdFechaSini.compareTo(lcd2010_09_30)<0){
			if (lcdFechaSini.compareTo(LocalDate.of(2005,7,25))<0 || (lcdFechaSini.compareTo(LocalDate.of(2005,7,25))>=0 && lcdFechaSini.compareTo(LocalDate.of(2011, 7, 31))<=0 && ldbMesada<=3*ldbSmin))
				lsPar14="1";
			else
				lsPar14="0";
			liMesCorte=9;
			ldbA = Double.min(Double.max(ldbMesada2010, 5.0*ldbSmin2010), 10.0*ldbSmin2010);
			
			Double ldbI=iReservasDao.buscarIyK(iUtilFec.convertirLD(LocalDate.of(2010,9,30))).getI();
			Double ldbK=iReservasDao.buscarIyK(iUtilFec.convertirLD(LocalDate.of(2010,10,1))).getK();
			
			if (ldbIncSalud>0.0) ldbIncSalud=ldbMesada2010*0.0804;
			
			lPension=iReservasDao.pension2b(lAfil.getIdTipoDoc(), lAfil.getIdPersona(), asTipoPension, lBenefVal.getEdad10().get(0), lBenefVal.getEdad10().get(1), lBenefVal.getEdad10().get(2), lBenefVal.getTipopersona10().get(0), lBenefVal.getTipopersona10().get(1), lBenefVal.getTipopersona10().get(2),"0", ldbMesada2010, lsPar14, liMesCorte, ldbK, ldbSmin2010, ldbI, adFechaSini, ldbA, lBenefVal.getMortalidad10_rv08().get(0), lBenefVal.getMortalidad10_rv08().get(1), lBenefVal.getMortalidad10_rv08().get(2), lBenefVal.getLimit10().get(0), lBenefVal.getLimit10().get(1), lBenefVal.getLimit10().get(2), "RV08", ldbIncSalud, 0.0);
			if (lPension==null){
				lsError="Error trayendo valores de pension";
				log.error(lsError);
				lBenefVal.setValor(-1.0);
				lBenefVal.setMensaje(lsError);
				return lBenefVal;
			}
			
			ldbRV08_2010 = totalPension(lPension);
			
			lPension=iReservasDao.pension2b(lAfil.getIdTipoDoc(), lAfil.getIdPersona(), asTipoPension, lBenefVal.getEdad10().get(0), lBenefVal.getEdad10().get(1), lBenefVal.getEdad10().get(2), lBenefVal.getTipopersona10().get(0), lBenefVal.getTipopersona10().get(1), lBenefVal.getTipopersona10().get(2),"0", ldbMesada2010, lsPar14, liMesCorte, ldbK, ldbSmin2010, ldbI, adFechaSini, ldbA, lBenefVal.getMortalidad10_rv89().get(0), lBenefVal.getMortalidad10_rv89().get(1), lBenefVal.getMortalidad10_rv89().get(2), lBenefVal.getLimit10().get(0), lBenefVal.getLimit10().get(1), lBenefVal.getLimit10().get(2), "RV89", ldbIncSalud, 0.0);
			if (lPension==null){
				lsError="Error trayendo valores de pension";
				log.error(lsError);
				lBenefVal.setValor(-1.0);
				lBenefVal.setMensaje(lsError);
				return lBenefVal;
			}
			
			ldbRV89_2010 = totalPension(lPension);
			
			liT = (iUtilFec.convertirDateLD(ldFCorte).getYear() - 2010)*12 + iUtilFec.convertirDateLD(ldFCorte).getMonthValue() - 9;
					
			if (ldbRV08_2010 > 0.0)
				ldbValor = ldbRV08 * (ldbRV89_2010/ldbRV08_2010 + liT*(1.0 - ldbRV89_2010/ldbRV08_2010) / 240.0);
			else
				ldbValor = 0.0;
		}else
			ldbValor=totalPension(lPension);
		
		lBenefVal.setValor(new Double(Math.round(ldbValor))); 
		
		return lBenefVal;
	}
	
	
	public Double calcularResAFunerario(Double adbIBC, Double adbSminFAT,Date adFechaSini,Date adFechaCorte) {
		Double valor;
		if (adbIBC.longValue() <= 5 * adbSminFAT.longValue())
			valor = adbSminFAT * 5.0;
		else if (adbIBC.longValue() > 5 * adbSminFAT.longValue() && adbIBC.longValue() <= 10 * adbSminFAT.longValue())
			valor = adbIBC;
		else
			valor = 10.0 * adbSminFAT;
		valor=(double)valor.longValue();
		if (iUtilFec.convertirDateLD(adFechaSini).getYear()<iUtilFec.convertirDateLD(adFechaCorte).getYear()){
			valor=iGeneralesServ.indexarValor(valor, adFechaSini, adFechaCorte);
		}
		return valor;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Double calcularResAsistencial(String asTipoSini,Integer aiParteCuerpo,Integer aiNaturalezaLesion,String asDiagnostico){
		if (asTipoSini.equals("AT"))
			return iReservasDao.valorAsistencialAT(aiParteCuerpo, aiNaturalezaLesion);
		else
			return iReservasDao.valorAsistencialEP(asDiagnostico);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ValoresITParametrica calcularReservaIT(String asTipoSini,Integer aiParteCuerpo,Integer aiNaturalezaLesion,String asDiagnostico,Double adbIBC,
			Date adFechaSini,Date adFechaCorte,	porcentSaludPension aPorcentajes){
		ValoresITParametrica lValoresIT=new ValoresITParametrica();
		Integer liDias;
		if (asTipoSini.equals("AT")){
			liDias=iReservasDao.diasITAT(aiParteCuerpo, aiNaturalezaLesion);
			if (liDias==null){
				lValoresIT.setDiasIT(-1);
				lValoresIT.setDescripError("Error !!! No existe valor paramétrico de días IT (tabla res_pm_reserva_soa) para parteCuerpo="+aiParteCuerpo.toString()+" y NaturalesaLesion="+aiNaturalezaLesion.toString());
				return lValoresIT;
			}
		}else{
			liDias=iReservasDao.diasITEP(asDiagnostico);
			if (liDias==null){
				lValoresIT.setDiasIT(-1);
				lValoresIT.setDescripError("Error !!! No existe valor paramétrico de días IT (tabla siarp.dbo.gral_pm_diagnostico) para el diagnóstico="+asDiagnostico);
				return lValoresIT;
			}
		}
		if (iUtilFec.convertirDateLD(adFechaSini).getYear()<iUtilFec.convertirDateLD(adFechaCorte).getYear()){
			adbIBC=iGeneralesServ.iblActualizado(adbIBC, adFechaSini, adFechaCorte,"I");
		}
		
		lValoresIT.setValorIT(liDias * adbIBC/30.0);
		lValoresIT.setValorSalud(liDias * adbIBC * aPorcentajes.getPorcentSalud() * aPorcentajes.getPorcentEmpresaSalud()/30.0);
		lValoresIT.setValorPension(liDias * adbIBC * aPorcentajes.getPorcentPension() * aPorcentajes.getPorcentEmpresaPension()/30.0);
		lValoresIT.setDiasIT(liDias);
		
		lValoresIT.setValorPension(new Double(MathUtils.round(lValoresIT.getValorPension().floatValue())));
		lValoresIT.setValorSalud(new Double(MathUtils.round(lValoresIT.getValorSalud().floatValue())));
		lValoresIT.setValorIT(new Double(MathUtils.round(lValoresIT.getValorIT().floatValue())));
		
		return lValoresIT;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public valoresIPPParametrica calcularResIPPParcial(String asTipoSini,Integer aiParteCuerpo,Integer aiNaturalezaLesion,String asDiagnostico,Double adbIBL){
		valoresIPPParametrica lValores=new valoresIPPParametrica();
		Double ldbPCL,ldbValor=0d;
		if (asTipoSini.equals("AT")){
			ldbPCL=iReservasDao.valorPCLAT(aiParteCuerpo, aiNaturalezaLesion);
			if (ldbPCL==null){
				lValores.setPCL(new Double(-1));
				lValores.setDescripError("Error!!! no existe valor parametrico de PCL (tabla res_pm_reserva_soa) para parteCuerpo="+aiParteCuerpo.toString()+" y NaturalezaLesion="+aiNaturalezaLesion.toString());
				return lValores;
			}
		}else{
			ldbPCL=iReservasDao.valorPCLEP(asDiagnostico);
			if (ldbPCL==null){
				lValores.setPCL(new Double(-1));
				lValores.setDescripError("Error!!! no existe valor parametrico de PCL (tabla siarp.dbo.gral_pm_diagnostico) del diagnóstico "+asDiagnostico);
				return lValores;
			}
		}
			
		if (ldbPCL>=5.0d && ldbPCL<50.0d)	
			ldbValor=((ldbPCL/2.0d) - 0.5d ) *adbIBL;
		lValores.setPCL(ldbPCL);
		lValores.setValorIPP(ldbValor);
		return lValores;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ReservaPI calcularResPInvalidez1(String asTipoDoc, String asDoc, Double adbIbl, Date adFechaSini,
			BigDecimal adcPCL, Double adbSminimo,Long alIdSiniestro) {
		Siniestro lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
		ReservaPI lReservaPI = new ReservaPI();
		LocalDate ldtFSiniestro = iUtilFec.convertirDateLD(adFechaSini);
		LocalDate ldtAhora = LocalDate.now();
		Double ldbMesada;
		ldbMesada = adbIbl;
		IyK iyk = new IyK();

		iyk = iReservasDao.buscarIyK(iUtilFec.convertirLD(ldtAhora));

		if (ldbMesada < adbSminimo) {
			ldbMesada = adbSminimo;

			if (ldtFSiniestro.getYear() != ldtAhora.getYear()) //calcular_la_mesada_a_la_fecha_de_corte
				ldbMesada = iGeneralesServ.mesadaActual(ldbMesada, lSiniestro.getFechaAT(), new Date(), true); //aquiSeActualizaALaFcehaDeCorte
		} else {
			if (ldtFSiniestro.getYear() != ldtAhora.getYear())//calcular_la_mesada_a_la_fecha_de_corte
				ldbMesada = iGeneralesServ.mesadaActual(ldbMesada, lSiniestro.getFechaAT(), new Date(), false);//aquiSeActualizaALaFcehaDeCorte
		}

		Double valor=0.0, edad1, edad2, mortalidadIni1, mortalidadIni2, dAFunerario;
		String sTipo1, sTipo2;
		LocalDate fCorte =LocalDate.now();
		Long mesCorte;
		Afiliado afiliado;
		// Traer información afiliado
		afiliado = iGeneralesServ.findAfiliado(asTipoDoc, asDoc);

		if (afiliado.getFechaNacimiento() == null) {
			lReservaPI.setRetorno(-1);
			lReservaPI.setMensaje("El afiliado no tiene fecha de nacimiento");
			return lReservaPI;
		}

		lReservaPI.setPar14(par14(lSiniestro.getFechaAT(), adbSminimo, adbIbl));
		if (lReservaPI.getPar14().equals("0"))
			lReservaPI.setNMesadas(13);
		else
			lReservaPI.setNMesadas(14);

		fCorte = ultimoDiaMes(iUtilFec.convertirLD(fCorte));

		edad1 = iGeneralesServ.calcularEdad(afiliado.getFechaNacimiento(), iUtilFec.convertirLD(fCorte));

		if (afiliado.getSexo().equals("M")) {
			sTipo1 = "HI";
			edad2 = edad1 - 5;
			sTipo2 = "MV";
		} else {
			edad2 = edad1 + 5;
			sTipo1 = "MI";
			sTipo2 = "HV";
		}
		mesCorte = new Long(fCorte.getMonthValue());

		mortalidadIni1 = mortalidadInicial(edad1, sTipo1);
		mortalidadIni2 = mortalidadInicial(edad2, sTipo2);
		dAFunerario = calcularResAFunerario(adbIbl, iGeneralesServ.SMLV(adFechaSini),adFechaSini,iUtilFec.convertirLD(fCorte));

		ResultadoPension result = valorPension(asTipoDoc, asDoc, "I", edad1, edad2, sTipo1, sTipo2, "0", ldbMesada,
				lReservaPI.getPar14(), mesCorte, iyk.getK(), adbSminimo, iyk.getI(), adFechaSini, mortalidadIni1,
				dAFunerario, mortalidadIni2);
		
		if (result==null){
			lReservaPI.setValorPI(-1.0);
			return lReservaPI;
		}
		if (result.getAfun()!=null) 	valor+=result.getAfun();
		if (result.getPpal()!=null) 	valor+=result.getPpal();
		if (result.getSob2a()!=null) 	valor+=result.getSob2a();
		if (result.getSob2b()!=null) 	valor+=result.getSob2b();
		if (result.getSob2c()!=null) 	valor+=result.getSob2c();
		if (result.getSobre1()!=null) 	valor+=result.getSobre1();
		if (result.getSobre2()!=null) 	valor+=result.getSobre2();
		
		valor=(double)valor.longValue();
		lReservaPI.setValorPI(valor);
		lReservaPI.setMesada(ldbMesada);

		return lReservaPI;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ReservaPS calcularResPSobrev1(String asTipoDoc, String asDoc, Double adbIbl, Date adFechaSini,Double adbSminimo,Long alIdSiniestro){
		ReservaPS lReservaPS = new ReservaPS();
		Double ldbMesada=adbIbl;
		Siniestro lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
		
		IyK iyk = new IyK();

		iyk = iReservasDao.buscarIyK(iUtilFec.convertirLD(LocalDate.now()));
		
		if (ldbMesada<adbSminimo){
			ldbMesada=adbSminimo;
			if (iUtilFec.convertirDateLD(adFechaSini).getYear()!=LocalDate.now().getYear())
				ldbMesada=iGeneralesServ.mesadaActual(ldbMesada,adFechaSini,new Date(),true);
		}else{
			if (iUtilFec.convertirDateLD(adFechaSini).getYear()!=LocalDate.now().getYear())
				ldbMesada=iGeneralesServ.mesadaActual(ldbMesada,adFechaSini,new Date(),false);
		}
		
		Double valor=0.0, edad1, mortalidadIni1;
		String sTipo1;
		LocalDate fCorte = LocalDate.now();
		Long mesCorte;
		
		Afiliado afiliado= iGeneralesServ.findAfiliado(asTipoDoc, asDoc); // Traer información afiliado
		if (afiliado.getFechaNacimiento() == null) {
			lReservaPS.setRetorno(-1);
			lReservaPS.setMensaje("El afiliado no tiene fecha de nacimiento");
			return lReservaPS;
		}
		fCorte=ultimoDiaMes(iUtilFec.convertirLD(fCorte));
		
		lReservaPS.setPar14(par14(lSiniestro.getFechaAT(), adbSminimo, adbIbl));
		if (lReservaPS.getPar14().equals("0"))
			lReservaPS.setNMesadas(13);
		else
			lReservaPS.setNMesadas(14);
		
		if (afiliado.getSexo().equals("M")){
			edad1 = iGeneralesServ.calcularEdad(afiliado.getFechaNacimiento(), iUtilFec.convertirLD(fCorte)) -5.0;
			sTipo1="MV";
		}else{
			edad1 = iGeneralesServ.calcularEdad(afiliado.getFechaNacimiento(), iUtilFec.convertirLD(fCorte)) +5.0;
			sTipo1="HV";
		}
		mesCorte = new Long(fCorte.getMonthValue());
		mortalidadIni1 = mortalidadInicial(edad1, sTipo1);
		
		ResultadoPension result = valorPension(asTipoDoc, asDoc, "S", edad1, edad1, sTipo1, sTipo1, "0", ldbMesada,
				lReservaPS.getPar14(), mesCorte, iyk.getK(), adbSminimo, iyk.getI(), adFechaSini, mortalidadIni1,
				0.0, mortalidadIni1);
		
		if (result==null){
			lReservaPS.setValorPS(-1.0);
			return lReservaPS;
		}
		if (result.getAfun()!=null) 	valor+=result.getAfun();
		if (result.getPpal()!=null) 	valor+=result.getPpal();
		if (result.getSob2a()!=null) 	valor+=result.getSob2a();
		if (result.getSob2b()!=null) 	valor+=result.getSob2b();
		if (result.getSob2c()!=null) 	valor+=result.getSob2c();
		if (result.getSobre1()!=null) 	valor+=result.getSobre1();
		if (result.getSobre2()!=null) 	valor+=result.getSobre2();
		
		valor=(double)valor.longValue();
		lReservaPS.setValorPS(valor);
		lReservaPS.setMesada(ldbMesada);
		
		return lReservaPS;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		

		try{
			Siniestro lSiniestro;
			
			IbcIbl lIbcIbl=new IbcIbl();
			Double ldbIBLOriginal;
	
			ParametrosPensiones lPension=new ParametrosPensiones();
			MovimientoReserva lMovReserva=new MovimientoReserva(alIdSiniestro);
			
			if (iReservasDao.consultarReserva(alIdSiniestro)>0){
				lsError="Error -----  Ya existe reserva para el siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			lSiniestro=iReservasDao.consultarSiniestro(alIdSiniestro);
			if (lSiniestro==null){
				lsError="Error -----  el siniestro "+alIdSiniestro.toString()+" no existe";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			if (lSiniestro.getEstado().equals("I")){
				lsError="Error -----  el siniestro "+alIdSiniestro.toString()+" se encuetra inactivo, no le puede constiuir reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			if (lSiniestro.getTipo()==null){
				lsError="Error -----  No se puede determinar si el siniestro "+alIdSiniestro.toString()+" es de tipo AL o EL";
				log.error(lsError);
				Throwable lTH=new Throwable("-1");
				throw new ServiceException(lsError,lTH);
			}
			
			lIbcIbl=iGeneralesServ.traerIbcIbl(lSiniestro);
			ldbIBLOriginal=lIbcIbl.getIbl();
			lIbcIbl.setIbl(iGeneralesServ.iblActualizado(lIbcIbl.getIbl(), lSiniestro.getFechaAT(), new Date(), "") );//2016-10-13 CMB usuario funcional solicita que se traiga a valor presente el IBL.
	
			LocalDate ldFechaSini=iUtilFec.convertirDateLD(lSiniestro.getFechaAT());
			LocalDate ldFechaAviso=iUtilFec.convertirDateLD(lSiniestro.getFechaAviso());
			
			valoresIPPParametrica lValoresIPP=calcularResIPPParcial(lSiniestro.getTipo(), (lSiniestro.getParteCuerpo()==null)?null:lSiniestro.getParteCuerpo(), (lSiniestro.getNaturalezaLesion()==null)?null:lSiniestro.getNaturalezaLesion(), (lSiniestro.getDiagnostico()==null)?null:lSiniestro.getDiagnostico(), lIbcIbl.getIbl());
			if (lValoresIPP.getPCL().equals(new Double(-1))){
				lsError=lValoresIPP.getDescripError()+" del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			ValoresITParametrica lValoresIT=calcularReservaIT(lSiniestro.getTipo(), (lSiniestro.getParteCuerpo()==null)?null:lSiniestro.getParteCuerpo(), (lSiniestro.getNaturalezaLesion()==null)?null:lSiniestro.getNaturalezaLesion(), (lSiniestro.getDiagnostico()==null)?null:lSiniestro.getDiagnostico(), lIbcIbl.getIbc(),lSiniestro.getFechaAT(),new Date(), iReservasDao.traerPorcentSaludPension(lSiniestro.getFechaAT()));
			if (lValoresIT.getDiasIT().equals(-1)){
				lsError=lValoresIT.getDescripError()+" del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			
			Double ldbSMLV_FAT=iGeneralesServ.SMLV(lSiniestro.getFechaAT());
			Double ldbMesada=0.0;
			
			if (ldFechaAviso.isAfter(ldFechaSini.plusDays(365))){
				ParametroGen lParamValorAsist=iGeneralesServ.recuperarParametroGen(220);
				if (lParamValorAsist==null){
					lsError="Error -----  Error Consultando información del parametro 220 debe crear este registro en la tabla Parametros_gen. (Valor asistencial)";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (lParamValorAsist.getEntero()==null) lParamValorAsist.setEntero(40000);
				lMovReserva.setAsistencial(new Double(lParamValorAsist.getEntero()));
				
			}else{//la fecha de aviso esta dentro de los 365 dias despues de la fecha de siniestro
				if (lSiniestro.getMortal()==null || lSiniestro.getMortal()==0){
					if (lSiniestro.getTipo().equals("AT")){
						if (lSiniestro.getParteCuerpo()==null){
							lsError="Error -----  El siniestro "+alIdSiniestro.toString()+" no tiene información de parte del cuerpo afectada";
							log.error(lsError);
							throw new ServiceException(lsError);
						}
						if (lSiniestro.getNaturalezaLesion()==null){
							lsError="Error -----  El siniestro "+alIdSiniestro.toString()+" no tiene información de Narulareza de la Lesión";
							log.error(lsError);
							throw new ServiceException(lsError);
						}
					}else{//es EP
						if (lSiniestro.getDiagnostico()==null){
							lsError="Error -----  El siniestro "+alIdSiniestro.toString()+" no tiene información de diagnóstico";
							log.error(lsError);
							throw new ServiceException(lsError);
						}
					}
					lMovReserva.setAsistencial(calcularResAsistencial(lSiniestro.getTipo(), lSiniestro.getParteCuerpo(), lSiniestro.getNaturalezaLesion(), lSiniestro.getDiagnostico()));
					lMovReserva.setIpp(lValoresIPP.getValorIPP());
					lMovReserva.setIt(lValoresIT.getValorIT());
					lMovReserva.setAportesPension(lValoresIT.getValorPension());
					lMovReserva.setAportesSalud(lValoresIT.getValorSalud());
					lMovReserva.setDiasIt(lValoresIT.getDiasIT());
					//fin NO es mortal
				}else { //es mortal
					lMovReserva.setAuxFunerario(calcularResAFunerario(lIbcIbl.getIbc(),ldbSMLV_FAT,lSiniestro.getFechaAT(),new Date())); 
				}
				
				if ((lSiniestro.getMortal()==null || lSiniestro.getMortal()==0) && lValoresIPP.getPCL()>=50.0){
					
 					ldbMesada=calcularMesada("I", lValoresIPP.getPCL(), ldbIBLOriginal, ldbSMLV_FAT);//en calcularPension se actualiza a valor presente la mesada
					
					//resPI= calcularResPInvalidez(lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT(),new BigDecimal(lValoresIPP.getPCL()),iGeneralesServ.SMLV(new Date()),alIdSiniestro) ;
					lPension=calcularPension("I", lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT());
					lMovReserva.setPi(lPension.getValor());
				}else if(lSiniestro.getMortal()!=null && lSiniestro.getMortal()!=0){
					
					ldbMesada=calcularMesada("S", null, ldbIBLOriginal, ldbSMLV_FAT); //en calcularPension se actualiza a valor presente la mesada
					
					lPension=calcularPension("S", lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT());
					lMovReserva.setPs(lPension.getValor());
					//resPS=calcularResPSobrev(lSiniestro.getTDocAfil(), lSiniestro.getDocAfil(), ldbMesada, lSiniestro.getFechaAT(), iGeneralesServ.SMLV(new Date()), alIdSiniestro);
				}
			}
			lIbcIbl.setIbl(new Double(lIbcIbl.getIbl().longValue()));
			lMovReserva.setAuxFunerario(new Double(lMovReserva.getAuxFunerario().longValue()));
			lMovReserva.setIt(new Double(lMovReserva.getIt().longValue()));
			lMovReserva.setIpp(new Double(lMovReserva.getIpp().longValue()));
			lMovReserva.setPi(new Double(lMovReserva.getPi().longValue()));
			lMovReserva.setPs(new Double(lMovReserva.getPs().longValue()));
			lMovReserva.setAsistencial(new Double(lMovReserva.getAsistencial().longValue()));
			lMovReserva.setAportesSalud(new Double(lMovReserva.getAportesSalud().longValue()));
			lMovReserva.setAportesPension(new Double(lMovReserva.getAportesPension().longValue()));
			lMovReserva.setPCL(lValoresIPP.getPCL());
			ldbMesada=new Double(ldbMesada.longValue());
			
			SaldosReserva lMaestroReserva=new SaldosReserva(alIdSiniestro);
			lMaestroReserva.setConstAF(lMovReserva.getAuxFunerario());
			lMaestroReserva.setConstIT(lMovReserva.getIt());
			lMaestroReserva.setConstIPP(lMovReserva.getIpp());
			lMaestroReserva.setConstPI(lMovReserva.getPi());
			lMaestroReserva.setConstPS(lMovReserva.getPs());
			lMaestroReserva.setConstAs(lMovReserva.getAsistencial());
			if (lMovReserva.getAsistencial()!=0.0) lMovReserva.setTipoMovOtr("AS");
			lMaestroReserva.setConstApS(lMovReserva.getAportesSalud());
			lMaestroReserva.setConstApP(lMovReserva.getAportesPension());
			lMaestroReserva.setConstHon(0.0);
			lMaestroReserva.setConstOtr(0.0);
			lMaestroReserva.setFechaSiniestro(lSiniestro.getFechaAT());
			lMaestroReserva.setUsuario(asUsuario);
			lMaestroReserva.setMaquina(asMaquina);
			lMaestroReserva.setMesada(ldbMesada);
			lMaestroReserva.setIBL(ldbIBLOriginal);
			lMaestroReserva.setIBLActualizado(lIbcIbl.getIbl());  
			if (lPension.getValor()!=null)
				lMaestroReserva.setNroMesadas(lPension.getMesadas());
			
			
			iReservasDao.insertMaestroReserva(lMaestroReserva);
			
			Long llNmov = iReservasDao.maxMovRes(alIdSiniestro);
			lMovReserva.setNroMov(llNmov);
			
			lMovReserva.setIdTipoMov("C");
			lMovReserva.setFechaMovimiento(new Date());
			lMovReserva.setUsuarioAud(asUsuario);
			lMovReserva.setMaquinaAud(asMaquina);
			lMovReserva.setModuloOrigina(asModulo);
			lMovReserva.setEstadoAud("A");
			lMovReserva.setOperacionAud("I");
			lMovReserva.setFechaModificacionAud(new Date());
			lMovReserva.setIndMortal(lSiniestro.getMortal());
			
			lMovReserva.setIdParteCuerpo(lSiniestro.getParteCuerpo());
			lMovReserva.setIdTipoLesion(lSiniestro.getNaturalezaLesion());
			lMovReserva.setIdDx(lSiniestro.getDiagnostico());
			
			lMovReserva.setIbc(lIbcIbl.getIbc());
			lMovReserva.setIbl(ldbIBLOriginal);
			lMovReserva.setIblActu(lIbcIbl.getIbl());
			if (ldbMesada!=0.0) lMovReserva.setMesada(ldbMesada);
			
			iReservasDao.insertMovReserva(lMovReserva);
			
		
			LocalDate ldUltimoDiaMes=ultimoDiaMes(new Date());
			
			if (lMovReserva.getPi()>0.0){
				ajusteRetroactivo(alIdSiniestro, lSiniestro.getFechaAT(), iUtilFec.convertirLD(ldUltimoDiaMes), lSiniestro.getFechaMuerte(), ldbSMLV_FAT, ldbMesada, "I", llNmov, new BigDecimal(1), asModulo, asUsuario, asMaquina);
			}
			if (lMovReserva.getPs()>0.0){
				ajusteRetroactivo(alIdSiniestro, lSiniestro.getFechaAT(), iUtilFec.convertirLD(ldUltimoDiaMes), lSiniestro.getFechaMuerte(), ldbSMLV_FAT, ldbMesada, "S", llNmov, new BigDecimal(1), asModulo, asUsuario, asMaquina);
			}
		}catch (ServiceException e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error constituyendo reserva del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error constituyendo reserva del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	private SaldosRubro getSaldosJur(Long alIdSiniestro,String asTipoReserva,String asTipoMovOtr,String asModuloOrigina) throws ServiceException{
		//se usa para traer saldos de los rubros cuando tienen que ver con procesos juridicos
		SaldosRubro lSaldos=new SaldosRubro();
		String lsError;
		try{
			lSaldos=iReservasDao.getSaldosOtrJur(alIdSiniestro, asTipoReserva, asTipoMovOtr, asModuloOrigina);
		}catch(Exception e){
			lsError=getClass().getName()+": "+e.getMessage();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		if (lSaldos==null) lSaldos=new SaldosRubro();
		if (lSaldos.getConstituido()==null) lSaldos.setConstituido(0.0);
		if (lSaldos.getReconocido()==null) lSaldos.setReconocido(0.0);
		return lSaldos;
	}
	
	
	@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public void liberarRubro(Long alIdSiniestro, String asRubro,String asTipoMovOtr,Date adFecha, String asModulo, String asUsuario, String asMaquina) throws ServiceException{
		String lsError;
		MovimientoReserva lMovReserva=new MovimientoReserva(alIdSiniestro);
		try{
			SaldosReserva lSaldos=iReservasDao.saldosReserva(alIdSiniestro);
			
			iReservasDao.bloqueaTabla(alIdSiniestro);
			
			if (iReservasDao.consultarReserva(alIdSiniestro)==0){
				lsError="Error -----  Error liberando reserva del siniestro "+alIdSiniestro.toString()+". No se ha constituido la reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			EstadoPJ estPj=iGeneralesServ.buscarEstadoPJ(alIdSiniestro);
			
			if (asRubro.equals("AS")){
				if (asTipoMovOtr==null || asTipoMovOtr.equals("")){
					lsError="Error -----  Debe indicar el TipoMovOtr a liberar par el rubro AS";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsPa().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro asistencial";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setAsistencial(-iReservasDao.reservaNoJuridico(alIdSiniestro, "AS", asTipoMovOtr));
				lMovReserva.setTipoMovOtr(asTipoMovOtr);
			}else if (asRubro.equals("IT")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsIt().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de IT";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setIt(lSaldos.getReconIT() - lSaldos.getConstIT());
			}else if (asRubro.equals("PP")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsIpp().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de IPP";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setIpp(lSaldos.getReconIPP()-lSaldos.getConstIPP());
			}else if (asRubro.equals("PI")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsPi().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de PI";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setPi(lSaldos.getReconPI()-lSaldos.getConstPI());
				lMovReserva.setRetroPi(lSaldos.getReconRI()-lSaldos.getConstRI()); //SE LIBERA TAMBIEN ESTE RUBRO POR PETICION DE USUARIO FUNCIONAL
			}else if (asRubro.equals("RI")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsPi().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de Retroactivo de Invalidez";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setRetroPi(lSaldos.getReconRI()-lSaldos.getConstRI());
			}else if (asRubro.equals("PS")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsPs().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de PS";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setPs(lSaldos.getReconPS()-lSaldos.getConstPS());
				lMovReserva.setRetroPs(lSaldos.getReconRS()-lSaldos.getConstRS());//SE LIBERA TAMBIEN ESTE RUBRO POR PETICION DE USUARIO FUNCIONAL
			}else if (asRubro.equals("RS")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsPs().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de retroactivo de sobrevivientes";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setRetroPs(lSaldos.getReconRS()-lSaldos.getConstRS());
			}else if (asRubro.equals("AF")){
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z") || estPj.getAbnEnContra()) &&  estPj.getAsAf().equals("1") && (!asModulo.equals("PJ"))) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de AF";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setAuxFunerario(lSaldos.getReconAF()-lSaldos.getConstAF());
			}else if (asRubro.equals("SA")){
				lMovReserva.setAportesSalud(lSaldos.getReconApS()-lSaldos.getConstApS());
			}else if (asRubro.equals("PE")){
				lMovReserva.setAportesPension(lSaldos.getReconApP()-lSaldos.getConstApP());
			}else if (asRubro.equals("HO")){
				if (asTipoMovOtr==null || asTipoMovOtr.trim().equals("")){
					lsError="Error -----  Debe elegir el tipo de Honorarios a liberar";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				if  (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z"))  && (!asModulo.equals("PJ")) && asTipoMovOtr.equals("HE")) {//si el sineistro está en un PJ debe dejar la plata que reservó juridicos
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de Honorarios Externos";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setHonorarios(-iReservasDao.reservaNoJuridico(alIdSiniestro, "HO", asTipoMovOtr));
				lMovReserva.setTipoMovOtr(asTipoMovOtr);
			}else if (asRubro.equals("OT")){
				if (asTipoMovOtr==null || asTipoMovOtr.trim().equals("")){
					lsError="Error -----  Debe elegir la clase de otros a liberar";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
				lMovReserva.setOtros(-iReservasDao.reservaNoJuridico(alIdSiniestro, "OT", asTipoMovOtr));
				lMovReserva.setTipoMovOtr(asTipoMovOtr);
			}
			
			if (	lMovReserva.getAuxFunerario()==0.0 && 	lMovReserva.getAsistencial()==0.0 && 		lMovReserva.getIt()==0.0 &&
					lMovReserva.getAportesSalud()==0.0 && 	lMovReserva.getAportesPension()==0.0 && 	lMovReserva.getIpp()==0.0 &&
					lMovReserva.getPi()==0.0 &&				lMovReserva.getRetroPi()==0.0 &&			lMovReserva.getPs()==0.0 &&
					lMovReserva.getRetroPs()==0.0 &&		lMovReserva.getHonorarios()==0.0 &&			lMovReserva.getOtros()==0.0)
				return;
			
			lMovReserva.setIdTipoMov("P");
			lMovReserva.setFechaMovimiento(new Date());
			lMovReserva.setNroMov(iReservasDao.maxMovRes(alIdSiniestro));
			lMovReserva.setUsuarioAud(asUsuario);
			lMovReserva.setMaquinaAud(asMaquina);
			lMovReserva.setModuloOrigina(asModulo);
			lMovReserva.setFechaModificacionAud(new Date());
			iReservasDao.insertMovReserva(lMovReserva);
		
			iReservasDao.modificaMaestro(lSaldos,lMovReserva);
		}catch(ServiceException e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error liberando rubro del siniestro "+alIdSiniestro;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}catch(Exception e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error liberando rubro del siniestro "+alIdSiniestro;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
		
	}
	
		
		MovimientoReserva lMovReserva=new MovimientoReserva(alIdSiniestro);
		try{
			SaldosReserva lSaldos=iReservasDao.saldosReserva(alIdSiniestro);
			iReservasDao.bloqueaTabla(alIdSiniestro);
			
			if (iReservasDao.consultarReserva(alIdSiniestro)==0){
				lsError="Error -----  Error actualizando reserva del siniestro "+alIdSiniestro.toString()+". No se ha constituido la reserva";
				log.error(lsError);
				throw new ServiceException(lsError);
			}
			
			EstadoPJ estPj=iGeneralesServ.buscarEstadoPJ(alIdSiniestro);
			
			lMovReserva.setPi(lSaldos.getReconPI() -  lSaldos.getConstPI());
			lMovReserva.setRetroPi(lSaldos.getReconRI() - lSaldos.getConstRI());
			lMovReserva.setPs(lSaldos.getReconPS() - lSaldos.getConstPS());
			lMovReserva.setRetroPs(lSaldos.getReconRS() - lSaldos.getConstRS());
			
			if (lMovReserva.getPi()!=0.0 || lMovReserva.getRetroPi()!=0.0)
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPi().equals("1")){
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de PI";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
			if (lMovReserva.getPs()!=0.0 || lMovReserva.getRetroPs()!=0.0)
				if (estPj.getAlProceso()>0 && (!estPj.getAsEstado().toLowerCase().equals("z")) && estPj.getAsPs().equals("1")){
					lsError="Error -----  El siniestro "+alIdSiniestro.toString()+", pertenece a un proceso Jurídico, no puede liberar el rubro de PS";
					log.error(lsError);
					throw new ServiceException(lsError);
				}
			
			if (lMovReserva.getPi()==0.0 && lMovReserva.getRetroPi()==0.0 && lMovReserva.getPs()==0.0 && lMovReserva.getRetroPs()==0.0) return;
			
			lMovReserva.setIdTipoMov("M");
			lMovReserva.setFechaMovimiento(adFecha);
			Long llNmov = iReservasDao.maxMovRes(alIdSiniestro);
			lMovReserva.setNroMov(llNmov);
			lMovReserva.setFechaModificacionAud(new Date());
			lMovReserva.setUsuarioAud(asUsuario);
			lMovReserva.setMaquinaAud(asMaquina);
			lMovReserva.setModuloOrigina(asModulo);
			
			iReservasDao.insertMovReserva(lMovReserva);
			
			SaldosReserva lSaldoReaseguro=iReservasDao.saldosReservaReaseguro(alIdSiniestro);
			if (lSaldoReaseguro==null) lSaldoReaseguro=new SaldosReserva(alIdSiniestro);
			
			if (lSaldoReaseguro.getConstPI()!=0.0 || lSaldoReaseguro.getConstRI()!=0.0 || lSaldoReaseguro.getConstPS()!=0.0 || lSaldoReaseguro.getConstRS()!=0.0){
				MovimientoReserva lMovReaseguro=new MovimientoReserva(alIdSiniestro);
				lMovReaseguro.setPi(-lSaldoReaseguro.getConstPI());
				lMovReaseguro.setRetroPi(-lSaldoReaseguro.getConstRI());
				lMovReaseguro.setPs(-lSaldoReaseguro.getConstPS());
				lMovReaseguro.setRetroPs(-lSaldoReaseguro.getConstRS());
				lMovReaseguro.setIdTipoMov("S");
				lMovReaseguro.setFechaMovimiento(adFecha);
				lMovReaseguro.setNroMov(llNmov +1);
				lMovReaseguro.setFechaModificacionAud(new Date());
				lMovReaseguro.setUsuarioAud(asUsuario);
				lMovReaseguro.setMaquinaAud(asMaquina);
				lMovReaseguro.setModuloOrigina(asModulo);
				iReservasDao.insertMovReserva(lMovReaseguro);
			}
			
			iReservasDao.modificaMaestro(lSaldos,lMovReserva);
		}catch(ServiceException e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error liberando por matematica el siniestro "+alIdSiniestro;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}catch(Exception e){
			lsError=getClass().getName()+": "+e.getMessage()+" Error liberando por matematica el siniestro "+alIdSiniestro;
			log.error(lsError);
			throw(new ServiceException(lsError));
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Double mortalidadInicial(Double adbEdad, String asTipo) {
		Double valorInf = 0.0, valorSup = 0.0, f, mortalidad;
		Long edadEnt;
		edadEnt = adbEdad.longValue();
		Double l = adbEdad * 10000;
		if (l.longValue() - edadEnt * 10000 > 0)
			valorSup = iReservasDao.consultarMortalidad(edadEnt+1, asTipo);
		if (valorSup == null)
			valorSup = 0.0;
		valorInf = iReservasDao.consultarMortalidad(edadEnt, asTipo);

		if (valorInf == null)
			valorInf = 0.0;
		f = adbEdad - adbEdad.longValue();
		mortalidad = f * valorSup + (1.0 - f) * valorInf;
		return mortalidad;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Double mortalidadVersion(String asVersion,String asTipoPer,Double adbEdad){
		Double ldbMort, ldbMort1;
		
		int liEdad;

		liEdad = adbEdad.intValue();
		ldbMort=iReservasDao.consultarMortalidadVersion(asVersion, asTipoPer, liEdad);
		ldbMort1=iReservasDao.consultarMortalidadVersion(asVersion, asTipoPer, liEdad+1);
		
		Double ldbDecimal=adbEdad - new Double(liEdad);
		
		if (ldbMort==null || ldbMort1==null) return null;
			
		ldbMort = ldbDecimal*ldbMort1 + (1.0 - ldbDecimal) * ldbMort;
		
		return ldbMort;
	}
	
	
	public Double pagarPorAnyo(Date adFechaAt,Date adFechaCorte,Double adbMesada,String asPar14){
		Double ldbMeses=0.0;
		Double ldbValor=0.0;
		LocalDate ldtFechaAt=iUtilFec.convertirDateLD(adFechaAt);
		LocalDate ldtFechaCorte=iUtilFec.convertirDateLD(adFechaCorte);

		ldbMeses=new Double(ldtFechaCorte.getMonthValue() - ldtFechaAt.getMonthValue());//los meses enteros
				
				
		if (ldtFechaAt.getDayOfMonth()==1) 
			ldbMeses +=1;
		else if (ldtFechaAt.getDayOfMonth()>=30 || (ldtFechaAt.getDayOfMonth()==2 && ldtFechaAt.getDayOfMonth()>=28))
			ldbMeses=ldbMeses +1 -1;//no debe hacer nada
		else
			ldbMeses += (30 -ldtFechaAt.getDayOfMonth())/30.0; // el pedacito del mes

		if (ldtFechaCorte.getMonthValue()>=6 && asPar14.equals("1") && ldtFechaAt.getMonthValue()<6) ldbMeses +=1; //si tiene derecho a la prima 14 es completa si at es menor al mes 5, si no, toca el pedacito de la prima tambien
		if (ldtFechaCorte.getMonthValue()>=6 && asPar14.equals("1") && ldtFechaAt.getMonthValue()==6) ldbMeses+=(30.0 - ldtFechaAt.getDayOfMonth())/30.0;// el pedacito de la measada 14
		if (ldtFechaCorte.getMonthValue()==12 && ldtFechaAt.getMonthValue()<12) ldbMeses+=1;// la mesada 13 cuando es completa
		if (ldtFechaCorte.getMonthValue()==12 && ldtFechaAt.getMonthValue()==12) ldbMeses += (30.0 - ldtFechaAt.getDayOfMonth())/30.0;// el pedacito de la mesada 13 cuando no es completa
		
		ldbValor=ldbMeses * adbMesada;
		
		return ldbValor;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String par14(Date adFechaAt, Double adbSmin, Double adbIbc) {
		LocalDate t=LocalDate.of(2005, 7, 25),t2=LocalDate.of(2011, 7, 31);
		

		String lsPar14 = "0";
		if (adFechaAt.compareTo(iUtilFec.convertirLD(t)) < 0)//menor
			lsPar14 = "1";
		else if (adFechaAt.compareTo(iUtilFec.convertirLD(t)) >= 0)//mayor o igual en Date
			if (adbIbc.longValue() <= 3 * adbSmin.longValue() && adFechaAt.compareTo(iUtilFec.convertirLD(t2)) < 0) //menor que
				lsPar14 = "1";
		return lsPar14;
	}
	
	private Integer parentescoLRS(Integer aiParentesco,String asSexo){
		if (aiParentesco==1) return 4;
		if (aiParentesco==2) return 4;
		if (aiParentesco==3)
			if (asSexo.toLowerCase().equals("m")) return 5;
			else return 6;
		if (aiParentesco==4) return 3;
		if (aiParentesco==5) return 2;
		if (aiParentesco==6) return 8;
		if (aiParentesco==7) return 7;
		return -1;
	}
	
	
	@Transactional(SiarpDatabase.transactionManagerBeanName)
	public void reconocerCMedicas(
			Long alIdSiniestro, 		Double adbValor, 		String asTipoMovOtr, 		Long alIdAutorizacion,
			Long alIdCuenta,			Long alIdFactura,		String asModulo,			String asUsuario, 		String asMaquina)
	throws ServiceException{
		
		String lsError="";
		StructRetorno lStRet;

		try{
			lStRet=iReservasDao.reconocerAs(alIdSiniestro, adbValor, asTipoMovOtr, alIdAutorizacion, alIdCuenta, alIdFactura, asModulo, asUsuario, asMaquina);
			if (lStRet.getICodigo()<0){
				lsError=getClass().getName()+";"+lStRet.getSMensaje()+" Error reconociento asistenciales del siniestro "+alIdSiniestro.toString();
				log.error(lsError);
				throw(new ServiceException(lsError));
			}
		}catch (Exception e){
			lsError=getClass().getName()+";"+e.getMessage()+" Error reconociento asistenciales del siniestro "+alIdSiniestro.toString();
			log.error(lsError);
			throw(new ServiceException(lsError));
		}

		
		StructRetornos stRetorno = new StructRetornos(0, null);
		try {
			
			stRetorno =reconocerPrestaciones(idSiniestro, tipoReserva, valor, tipoMovOtr, moduloOrigen, usuario, maquina);
			iReservasDao.insertReservasCM(idSiniestro, stRetorno.getICodigo().longValue(), idCuenta, idFactura, usuario, maquina);
			
		} 
		catch (ServiceException e) {
			stRetorno.setICodigo(-1);
			stRetorno.setSMensaje(getClass().getName()+"- Error Reconociendo Reserva " + tipoReserva + " - Siniestro " +idSiniestro);
			log.error(stRetorno.getSMensaje());
			return stRetorno;
		}
		return stRetorno;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*@Transactional(rollbackFor=Exception.class, value=SiarpDatabase.transactionManagerBeanName)
	public StructRetornos reconocerPrestaciones(Long idSiniestro, String tipoReserva, Double adbValor, String asTipoMovOtr, String asModuloOrigen, String usuario, String maquina)  throws ServiceException {
		String lsError;
		MovimientoReserva movReserva = new MovimientoReserva(idSiniestro);
		StructRetornos lstRet=new StructRetornos(1,"OK");
		
		if ( (tipoReserva.equals("AS") || tipoReserva.equals("HO") || tipoReserva.equals("OT") ) && (asTipoMovOtr==null || asTipoMovOtr.equals("")) ){
			lsError=getClass().getName()+" Error - el rubro "+tipoReserva+" debe indicar que clase TipoMovOtr debe mover "+idSiniestro;
			log.error(lsError);
			throw(new ServiceException (lsError));
		}
		
		try {
			SaldosReserva saldos = iReservasDao.saldosReserva(idSiniestro);
			Date ldAhora = new Date();
			
			if(asTipoMovOtr!=null && asTipoMovOtr.equals("")) asTipoMovOtr = null;
			
			EstadoPJ estadoPJ = iGeneralesServ.buscarEstadoPJ(idSiniestro);
			Double ldbResJuridico,ldbResNoJuridico;
			//AS ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("AS")) {
				if (adbValor < 0) {
					if (saldos.getReconAs() < -adbValor) {
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && !estadoPJ.getAsEstado().toLowerCase().equals("z") &&  estadoPJ.getAsPa().equals("1")){
					ldbResJuridico = iReservasDao.reservaJuridico(idSiniestro, "AS",asTipoMovOtr);
					ldbResNoJuridico = iReservasDao.reservaNoJuridico(idSiniestro, "AS",asTipoMovOtr);
					if (ldbResJuridico==null) ldbResJuridico=0.0;
					if (ldbResNoJuridico==null) ldbResJuridico=0.0;
					if (asModuloOrigen.equals("PJ"))
						if (adbValor > ldbResJuridico)  //ajustar solo por el pedazo que le falta y tocando solo lo de PJ
							ajustarReservaRubro(idSiniestro, "AS", adbValor - ldbResJuridico, usuario, maquina, asModuloOrigen,asTipoMovOtr);				
					else 
						if(adbValor > ldbResNoJuridico)
							ajustarReservaRubro(idSiniestro, "AS", adbValor - ldbResNoJuridico, usuario, maquina, asModuloOrigen,asTipoMovOtr);
				}
				else //no es de proceso juridico y se comporta igual que antes
					if (iReservasDao.reservaNoJuridico(idSiniestro, "AS",asTipoMovOtr) < adbValor)
						ajustarReservaManual(idSiniestro, tipoReserva, saldos.getReconAs()+adbValor, ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
			}//fin "AS"
			//IT ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("IT")) {
				if (adbValor < 0) {
					if (saldos.getReconIT() < -adbValor) {
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && !estadoPJ.getAsEstado().toLowerCase().equals("z") &&  estadoPJ.getAsIt().equals("1")){
					ldbResJuridico = iReservasDao.reservaJuridico(idSiniestro, "IT",null);
					ldbResNoJuridico = iReservasDao.reservaNoJuridico(idSiniestro, "IT",null);
					if (ldbResJuridico==null) ldbResJuridico=0.0;
					if (ldbResNoJuridico==null) ldbResJuridico=0.0;
					if (asModuloOrigen.equals("PJ"))
						if (adbValor > ldbResJuridico)  //ajustar solo por el pedazo que le falta y tocando solo lo de PJ
							ajustarReservaRubro(idSiniestro, "IT", adbValor - ldbResJuridico, usuario, maquina, asModuloOrigen,asTipoMovOtr);				
					else 
						if(adbValor > ldbResNoJuridico)
							ajustarReservaRubro(idSiniestro, "IT", adbValor - ldbResNoJuridico, usuario, maquina, asModuloOrigen,asTipoMovOtr);
				}
				else //no es de proceso juridico y se comporta igual que antes
					if (saldos.getConstIT()- saldos.getReconIT() < adbValor)
						ajustarReservaManual(idSiniestro, tipoReserva, saldos.getReconIT()+adbValor, ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
			}//fin "IT"
			//AF ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("AF")) {
				if (adbValor<0){ 
					if (saldos.getReconAF() < -adbValor){
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && estadoPJ.getAsAf().equals("1") && !estadoPJ.getAsEstado().toLowerCase().equals("z") &&  !asModuloOrigen.equals("PJ")){
					lsError="ReservasService.reconocerPrestaciones - Atención , este siniestro es de un proceso jurídico no lo puede reconocer";
					Log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (saldos.getConstAF() - saldos.getReconAF() < adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor + saldos.getReconAF(), ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
			}//fin "AF"
			
			//IPP ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("PP")) {
				if (adbValor<0){ 
					if (saldos.getReconIPP() < -adbValor){
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && estadoPJ.getAsIpp().equals("1") && !estadoPJ.getAsEstado().toLowerCase().equals("z") && !asModuloOrigen.equals("PJ")){
					lsError="ReservasService.reconocerPrestaciones - Atención , este siniestro es de un proceso jurídico no lo puede reconocer";
					Log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (saldos.getConstIPP() - saldos.getReconIPP() < adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor + saldos.getReconIPP(), ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
			}//fin IPP ---------------------------------------------------------------------------------------
			//SALUD ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("SA")) {
				if (adbValor<0){ 
					if (saldos.getReconApS() < -adbValor){
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (saldos.getConstApS() - saldos.getReconApS() < adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor + saldos.getReconApS(), ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
			}//fin SA ---------------------------------------------------------------------------------------
			//aportes PENSION ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("PE")) {
				if (adbValor<0){ 
					if (saldos.getReconApP() < -adbValor){
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (saldos.getConstApP() - saldos.getReconApP() < adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor + saldos.getReconApP(), ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
			}//fin SA ---------------------------------------------------------------------------------------
			//PS ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("PS")) {
				if (adbValor<0){ 
					if (saldos.getReconPS() < -adbValor){
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && estadoPJ.getAsPs().equals("1") && !estadoPJ.getAsEstado().toLowerCase().equals("z") &&  (!asModuloOrigen.equals("PJ") && !asModuloOrigen.equals("PE"))){
					lsError="ReservasService.reconocerPrestaciones - Atención , este siniestro es de un proceso jurídico no lo puede reconocer";
					Log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (saldos.getConstPS() - saldos.getReconPS() < adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor + saldos.getReconPS(), ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
			}//fin PS ---------------------------------------------------------------------------------------
			//PI ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("PI")) {
				if (adbValor<0){ 
					if (saldos.getReconPI() < -adbValor){
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && estadoPJ.getAsPi().equals("1") && !estadoPJ.getAsEstado().toLowerCase().equals("z") &&  ( !asModuloOrigen.equals("PJ") && !asModuloOrigen.equals("PE")) ){
					lsError="ReservasService.reconocerPrestaciones - Atención , este siniestro es de un proceso jurídico no lo puede reconocer";
					Log.error(lsError);
					throw new ServiceException(lsError);
				}
				if (saldos.getConstPS() - saldos.getReconPI() < adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor + saldos.getReconPI(), ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
			}//fin PI ---------------------------------------------------------------------------------------
			Double ldbAjRI=0.0;
			//RI ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("RI")) {
				//el reconocimiento de Retroactivos es diferente a los otros rubros, aqui el sistema toma el valor que llega como argumento
				//como el total de lo reconocido, osea que no se pueden enviar reconocimientos negativos y el sistema calcularñá la diferencia de
				//lo reconocido antes contra el argumento que llega
				if (adbValor<0){ 
					lsError="ReservasService.reconocerPrestaciones - En reconocimiento de Retroactivos no se puede enviar valores negativos, envíe el total reconocido";
					Log.error(lsError);
					throw new ServiceException(lsError);
				}
				ldbAjRI=adbValor-saldos.getReconRI();
				
				if (saldos.getConstRI() != adbValor)
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor , ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
				if (ldbAjRI==0.0) return lstRet;
				
			}//fin RI ---------------------------------------------------------------------------------------
			//RS ---------------------------------------------------------------------------------------
			Double ldbAjRS=0.0;
			if (tipoReserva.equals("RS")) {
				//el reconocimiento de Retroactivos es diferente a los otros rubros, aqui el sistema toma el valor que llega como argumento
				//como el total de lo reconocido, osea que no se pueden enviar reconocimientos negativos y el sistema calcularñá la diferencia de
				//lo reconocido antes contra el argumento que llega
				if (adbValor<0){ 
					lsError="ReservasService.reconocerPrestaciones - En reconocimiento de Retroactivos no se puede enviar valores negativos, envíe el total reconocido";
					Log.error(lsError);
					throw new ServiceException(lsError);
				}
				ldbAjRS=adbValor-saldos.getReconRS();
				
				if (saldos.getConstRS() != adbValor) 
					ajustarReservaManual(idSiniestro, tipoReserva, adbValor , ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
				
				if (ldbAjRS==0.0) return lstRet;
			}//fin RS ---------------------------------------------------------------------------------------
			//HO ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("HO")) {
				ldbResJuridico = iReservasDao.reservaJuridico(idSiniestro, "HO",asTipoMovOtr);
				ldbResNoJuridico = iReservasDao.reservaNoJuridico(idSiniestro, "HO",asTipoMovOtr);
				SaldosRubro lSaldoHO=iReservasDao.getSaldosOtrJur(idSiniestro, "HO", asTipoMovOtr, null);//al poner modulo origina null trae el saldo general del rubro
				
				if (adbValor < 0) {
					if (lSaldoHO.getReconocido()< -adbValor) {
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (estadoPJ.getAlProceso() > 0 && !estadoPJ.getAsEstado().toLowerCase().equals("z") &&  asTipoMovOtr.equals("HE")){
				
					if (ldbResJuridico==null) ldbResJuridico=0.0;
					if (ldbResNoJuridico==null) ldbResJuridico=0.0;
					if (asModuloOrigen.equals("PJ"))
						if (adbValor > ldbResJuridico)  //ajustar solo por el pedazo que le falta y tocando solo lo de PJ
							ajustarReservaRubro(idSiniestro, "HO", adbValor - ldbResJuridico, usuario, maquina, asModuloOrigen,asTipoMovOtr);				
					else 
						if(adbValor > ldbResNoJuridico)
							ajustarReservaRubro(idSiniestro, "HO", adbValor - ldbResNoJuridico, usuario, maquina, asModuloOrigen,asTipoMovOtr);
				}
				else //no es de proceso juridico y se comporta igual que antes
					if (lSaldoHO.getConstituido() -lSaldoHO.getReconocido() < adbValor && adbValor>0.0)
						ajustarReservaManual(idSiniestro, tipoReserva, lSaldoHO.getReconocido()+adbValor, ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
			}//fin "HO"
			//OT ---------------------------------------------------------------------------------------
			if (tipoReserva.equals("OT")) {
				SaldosRubro lSaldoOT=iReservasDao.getSaldosOtrJur(idSiniestro, "OT", asTipoMovOtr, null);//al poner modulo origina null trae el saldo general del rubro
				
				if (adbValor < 0) {
					if (lSaldoOT.getReconocido()< -adbValor) {
						lsError="ReservasService.reconocerPrestaciones - No puede realizar un ajuste negativo mayor a lo reconocido actualmente porque quedaría en saldo reconocido negativo";
						Log.error(lsError);
						throw new ServiceException(lsError);
					}
				}
				if (lSaldoOT.getConstituido()-lSaldoOT.getReconocido() < adbValor && adbValor>0.0)
					ajustarReservaManual(idSiniestro, tipoReserva, lSaldoOT.getReconocido()+adbValor, ldAhora, asTipoMovOtr, asModuloOrigen, usuario, maquina);
			}//fin "OT"
			
			//Insertar Movimiento
			
			movReserva.setIdTipoMov("R");
			movReserva.setFechaMovimiento(ldAhora);
			movReserva.setUsuarioAud(usuario);
			movReserva.setMaquinaAud(maquina);
			movReserva.setFechaModificacionAud(ldAhora);
			movReserva.setTipoMovOtr(asTipoMovOtr);
			movReserva.setModuloOrigina(asModuloOrigen);
				
			if (tipoReserva.equals("AS")) movReserva.setAsistencial(adbValor);
			if (tipoReserva.equals("AF")) movReserva.setAuxFunerario(adbValor);
			if (tipoReserva.equals("IT")) movReserva.setIt(adbValor);
			if (tipoReserva.equals("SA")) movReserva.setAportesSalud(adbValor);
			if (tipoReserva.equals("PE")) movReserva.setAportesPension(adbValor);
			if (tipoReserva.equals("PP")) movReserva.setIpp(adbValor);
			if (tipoReserva.equals("PI")) movReserva.setPi(adbValor);
			if (tipoReserva.equals("PS")) movReserva.setPs(adbValor);
			movReserva.setRetroPi(ldbAjRI);
			movReserva.setRetroPs(ldbAjRS);
			if (tipoReserva.equals("HO")) movReserva.setHonorarios(adbValor);
			if (tipoReserva.equals("OT")) movReserva.setOtros(adbValor);
			
			movReserva.setNroMov(iReservasDao.maxMovRes(idSiniestro));	
			
			iReservasDao.insertMovReserva(movReserva);
			iReservasDao.modificaMaestroRecon(saldos,movReserva);
	
		} catch (ServiceException e) {
			lsError=getClass().getName()+":"+e.getMessage()+"- Error Reconociendo Reserva " + tipoReserva + " - Siniestro " +idSiniestro;
			log.error(lsError);
			throw new ServiceException(lsError);
		} catch (Exception e){
			lsError=getClass().getName()+":"+e.getMessage()+"- Error Reconociendo Reserva " + tipoReserva + " - Siniestro " +idSiniestro;
			log.error(lsError);
			throw new ServiceException(lsError);
		}
		lstRet.setICodigo(movReserva.getNroMov().intValue());
		return lstRet;
		
	}

	private Double round2(Double adbValor){
		return new Double( (new Double((adbValor*100.0)+0.5)).longValue()/100.0);
	}
	
	public SaldosReserva saldosReserva(Long alIdSiniestro){
		return iReservasDao.saldosReserva(alIdSiniestro);
	}
	

	
///////////////////////////////////////////////////////////////////////////////////////
	private Double totalPension(PensionNewNota aPension){
		Double ldbValor=0.0;
		
		if (aPension.getRp()!=null) ldbValor+=aPension.getRp();
		if (aPension.getRa()!=null) ldbValor+=aPension.getRa();
		if (aPension.getRs()!=null) ldbValor+=aPension.getRs();
		if (aPension.getInc_salud()!=null) ldbValor+=aPension.getInc_salud();
		if (aPension.getInc_pensional()!=null) ldbValor+=aPension.getInc_pensional();
		
		return ldbValor;
	}
	
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public LocalDate ultimoDiaMes(Date adFecha) {
		LocalDate ldFecha=iUtilFec.convertirDateLD(adFecha);
		LocalDate ldUltimoDiaMes;
		if (ldFecha.getMonthValue()==12)
			ldUltimoDiaMes=LocalDate.of(ldFecha.getYear(), 12, 31);
		else
			ldUltimoDiaMes=LocalDate.of(ldFecha.getYear(), ldFecha.getMonthValue() +1, 1).minusDays(1);

		return ldUltimoDiaMes;
	}
	
	
	public ResultadoPension valorPension(String tDoc, String docu, String tipoPension, Double edad1, Double edad2,
			String tipoPersona1, String tipoPersona2, String a47, Double p, String par14, Long mes_corte, Double k,
			Double smin, Double i, Date feCau, Double mortalIni, Double A, Double mortalIni2v) {
		return iReservasDao.valorPension(tDoc, docu, tipoPension, edad1, edad2, tipoPersona1, tipoPersona2, a47, p,
				par14, mes_corte, k, smin, i, feCau, mortalIni, A, mortalIni2v);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
}
*/
