package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.dao.*;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.Guarantee;
import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.model.*;
import gt.gob.rgm.adm.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Stateless
public class GarantiasServiceImp  {
	private final static Logger logger = LoggerFactory.getLogger(GarantiasServiceImp.class);

	@Inject
	PersonasServiceImp personasService;
	
	@Inject
	RugCatTipoTramiteRepository tipoTramiteDao;
	
	@Inject
	TramitesRugIncompRepository tramitesRugIncompDao;
	
	@Inject
	TramitesRepository tramitesDao;
        
	
	@Inject
	RugBitacTramitesRepository bitacTramitesDao;
	
	@Inject
	RugFirmaDoctosRepository firmaDoctosDao;
	
	@Inject
	RugGarantiasPendientesRepository garantiasPendientesDao;
	
	@Inject
	RugContratoRepository contratoDao;
	
	@Inject
	RugGarantiasRepository garantiasDao;
	
	@Inject
	RugRelTramGaranRepository relTramGaranDao;
	
	@Inject
	RugGarantiasHRepository garantiasHDao;
	
	@Inject
	RugRepository rugDao;
	
	private LocalDate now;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public RugGarantias findGarantiaById(Long idGarantia) {
		return garantiasDao.findById(idGarantia);
	}
	
	public Tramites findTramiteById(Long idTramite) {
		return tramitesDao.findById(idTramite);
	}
	
	public void crear(Transaction transaction) {
		now = LocalDate.now();
		// crear solicitante
		// asignar password, grupo acreedor, pregunta y respuesta
		transaction.getSolicitante().setPassword(Constants.DEFAULT_PASSWORD);
		transaction.getSolicitante().setGroupId(Constants.GRUPO_ACREEDOR);
		transaction.getSolicitante().setSecurityQuestion(Constants.DEFAULT_PREGUNTA);
		transaction.getSolicitante().setSecurityAnswer(Constants.DEFAULT_RESPUESTA);
		transaction.getSolicitante().setRegistered(transaction.getSolicitante().getNationality() == Constants.PAIS_NACIONAL ? Constants.INSCRITO_NACIONAL : Constants.INSCRITO_EXTRANJERO);
		RugPersonas persona = personasService.crear(transaction.getSolicitante(), Constants.PARTE_SOLICITANTE, transaction.getIdTramite(), true, now);
		Long idPersona = persona.getIdPersona();
		// crear tramite
		crearSeccionesTramite(transaction, idPersona, Constants.TRAMITE_INSCRIPCION);
		Long idRelacion = rugDao.getNextRegistry("seq_relaciones");
		// insertar garantias pendientes
		Long idGarantiaPendiente = crearGarantiaPendiente(transaction, idPersona, idRelacion);
		// insertar contrato
		crearContrato(idGarantiaPendiente, transaction.getIdTramite());
		// insertar garantia
		crearGarantia(transaction, idPersona, idRelacion, idGarantiaPendiente);
		// insertar relacion tramite garantia
		crearRelacionTramiteGarantia(transaction.getIdTramite(), transaction.getGuarantee().getIdGarantia());
		// insertar en rug_garantias_h
		crearGarantiaH(transaction, idPersona, idRelacion, idGarantiaPendiente);
		// insertar partes en rug_rel_tram_partes para solicitante, deudores y acreedores
		personasService.crearRelacionTramiteParte(transaction.getIdTramite(), idPersona, Constants.PARTE_SOLICITANTE, transaction.getSolicitante().getPersonType());
		personasService.crearRelacionGarantiaParte(transaction.getGuarantee().getIdGarantia(), idPersona, Constants.PARTE_SOLICITANTE, idRelacion);
		transaction.getSolicitante().setPersonaId(idPersona);
		personasService.crearPersonasH(transaction.getIdTramite(), Constants.PARTE_SOLICITANTE, transaction.getSolicitante(), persona.getIdDomicilio());
		// insertar deudores
		for(ExternalUser deudor : transaction.getDeudores()) {
			deudor.setRegistered(Constants.CONFIRMACION);
			RugPersonas deudorPersona = personasService.crear(deudor, Constants.PARTE_DEUDOR, transaction.getIdTramite(), false, now);
			personasService.crearRelacionTramiteParte(transaction.getIdTramite(), deudorPersona.getIdPersona(), Constants.PARTE_DEUDOR, deudor.getPersonType());
			personasService.crearRelacionGarantiaParte(transaction.getGuarantee().getIdGarantia(), deudorPersona.getIdPersona(), Constants.PARTE_DEUDOR, idRelacion);
			deudor.setPersonaId(deudorPersona.getIdPersona());
			personasService.crearPersonasH(transaction.getIdTramite(), Constants.PARTE_DEUDOR, deudor, deudorPersona.getIdDomicilio());
		}
		// insertar acreedores
		for(ExternalUser acreedor : transaction.getAcreedores()) {
			acreedor.setRegistered(Constants.CONFIRMACION);
			RugPersonas acreedorPersona = personasService.crear(acreedor, Constants.PARTE_ACREEDOR, transaction.getIdTramite(), false, now);
			personasService.crearRelacionTramiteParte(transaction.getIdTramite(), acreedorPersona.getIdPersona(), Constants.PARTE_ACREEDOR, acreedor.getPersonType());
			personasService.crearRelacionGarantiaParte(transaction.getGuarantee().getIdGarantia(), acreedorPersona.getIdPersona(), Constants.PARTE_ACREEDOR, idRelacion);
			acreedor.setPersonaId(acreedorPersona.getIdPersona());
			personasService.crearPersonasH(transaction.getIdTramite(), Constants.PARTE_ACREEDOR, acreedor, acreedorPersona.getIdDomicilio());
		}
	}
	
	public void crearSeccionesTramite(Transaction transaction, Long idPersona, Integer tipoTramite) {
		Date fechaTramite;
		try {
			fechaTramite = sdf.parse(transaction.getFechaCreacion());
		} catch (ParseException e) {
			fechaTramite = java.sql.Date.valueOf(now);
		}
		// TRAMITES_RUG_INCOMP
		TramitesRugIncomp tramitesRugIncomp = new TramitesRugIncomp();
		tramitesRugIncomp.setIdTramiteTemp(transaction.getIdTramiteTemp());
		tramitesRugIncomp.setIdPersona(idPersona);
		tramitesRugIncomp.setIdTipoTramite(tipoTramite);
		tramitesRugIncomp.setFechPreInscr(fechaTramite);
		tramitesRugIncomp.setFechaInscr(fechaTramite);
		tramitesRugIncomp.setIdStatusTram(Constants.STATUS_TRAMITE);
		tramitesRugIncomp.setIdPaso(Constants.PASO_TRAMITE);
		tramitesRugIncomp.setBCargaMasiva(Constants.CREACION_SISTEMA);
		tramitesRugIncomp.setFechaStatus(java.sql.Date.valueOf(now));
		tramitesRugIncomp.setStatusReg(Constants.ESTADO_ACTIVO);
		tramitesRugIncompDao.save(tramitesRugIncomp);

		// TRAMITES
		Tramites tramite = new Tramites();
		tramite.setIdTramite(transaction.getIdTramite());
		tramite.setIdTramiteTemp(transaction.getIdTramiteTemp());
		tramite.setIdPersona(idPersona);
		RugCatTipoTramite catTipoTramite = tipoTramiteDao.getById(tipoTramite);
		tramite.setTipoTramite(catTipoTramite);
		tramite.setFechPreInscr(fechaTramite);
		tramite.setFechaInscr(fechaTramite);
		tramite.setIdStatusTram(Constants.STATUS_TRAMITE);
		tramite.setFechaCreacion(fechaTramite);
		tramite.setIdPaso(Constants.PASO_TRAMITE);
		tramite.setBCargaMasiva(Constants.CREACION_SISTEMA);
		tramite.setFechaStatus(fechaTramite);
		tramite.setStatusReg(Constants.ESTADO_ACTIVO);
		tramitesDao.save(tramite);
		
		// RUG_BITAC_TRAMITES
		RugBitacTramites bitacTramites = new RugBitacTramites();
		RugBitacTramitesPK bitacTramitesPK = new RugBitacTramitesPK();
		bitacTramites.setTramiteIncomp(tramitesRugIncomp);
		bitacTramitesPK.setIdStatus(Constants.STATUS_TRAMITE);
		bitacTramitesPK.setIdPaso(Constants.PASO_TRAMITE);
		bitacTramitesPK.setIdTipoTramite(tipoTramite);
		bitacTramitesPK.setStatusReg(Constants.ESTADO_ACTIVO);
		bitacTramites.setId(bitacTramitesPK);
		bitacTramites.setFechaStatus(fechaTramite);
		bitacTramites.setFechaReg(java.sql.Date.valueOf(now));
		bitacTramitesDao.save(bitacTramites);

		// RUG_FIRMA_DOCTOS
		RugFirmaDoctos firmaDoctos = new RugFirmaDoctos();
		firmaDoctos.setIdTramiteTemp(transaction.getIdTramiteTemp());
		firmaDoctos.setIdUsuarioFirmo(idPersona);
		firmaDoctos.setFechaReg(java.sql.Date.valueOf(now));
		firmaDoctos.setStatusReg(Constants.ESTADO_ACTIVO);
		firmaDoctos.setProcesado(Constants.CONFIRMACION);
		firmaDoctosDao.save(firmaDoctos);
	}
	
	public Long crearGarantiaPendiente(Transaction transaction, Long idPersona, Long idRelacion) {
		Date fechaGarantia;
		try {
			fechaGarantia = sdf.parse(transaction.getGuarantee().getFechaInscr());
		} catch (ParseException e) {
			fechaGarantia = java.sql.Date.valueOf(now);
		}
		// RUG_GARANTIAS_PENDIENTES
		RugGarantiasPendientes garantiaPendiente = new RugGarantiasPendientes();
		garantiaPendiente.setIdTipoGarantia(Constants.TIPO_GARANTIA);
		garantiaPendiente.setNumGarantia(transaction.getGuarantee().getNumGarantia());
		garantiaPendiente.setDescGarantia(transaction.getGuarantee().getDescGarantia());
		garantiaPendiente.setMesesGarantia(Constants.MESES_GARANTIA);
		garantiaPendiente.setIdPersona(idPersona);
		garantiaPendiente.setIdRelacion(idRelacion);
		garantiaPendiente.setOtrosTerminosGarantia(transaction.getGuarantee().getOtrosTerminosGarantia());
		garantiaPendiente.setFechaInscr(fechaGarantia);
		 // calcular a partir de la fecha de inicio del sistema
		LocalDate fechaFinVigencia = LocalDate.parse(Constants.FECHA_INICIO_SISTEMA).plusMonths(Constants.MESES_GARANTIA);
		Date fechaFin = java.sql.Date.valueOf(fechaFinVigencia);
		garantiaPendiente.setFechaFinGar(fechaFin);
		garantiaPendiente.setVigencia(Constants.VIGENCIA);
		garantiaPendiente.setGarantiaStatus(Constants.ESTADO_ACTIVO);
		garantiaPendiente.setInstrumentoPublico(transaction.getGuarantee().getInstrumentoPublico());
		garantiaPendiente.setIdMoneda(Constants.MONEDA);
		garantiaPendiente.setEsPrioritaria(Constants.FALSE);
		garantiaPendiente.setTxtRegistros(transaction.getGuarantee().getTxtRegistros());
		garantiaPendiente.setIdUltimoTramite(transaction.getIdTramite());
		garantiaPendiente.setIdGarantiaModificar(Constants.GARANTIA_MODIFICAR);
		return garantiasPendientesDao.save(garantiaPendiente);
	}
	
	public void crearContrato(Long idGarantiaPendiente, Long idTramite) {
		// RUG_CONTRATO
		RugContrato contrato = new RugContrato();
		contrato.setIdGarantiaPend(idGarantiaPendiente);
		contrato.setIdTramiteTemp(idTramite);
		contrato.setFechaReg(java.sql.Date.valueOf(now));
		contrato.setStatusReg(Constants.ESTADO_ACTIVO);
		contratoDao.save(contrato);
	}
	
	public void crearGarantia(Transaction transaction, Long idPersona, Long idRelacion, Long idGarantiaPendiente) {
		Guarantee guarantee = transaction.getGuarantee();
		// RUG_GARANTIAS
		RugGarantias garantia = new RugGarantias();
		garantia.setIdGarantia(guarantee.getIdGarantia());
		garantia.setIdTipoGarantia(Constants.TIPO_GARANTIA);
		garantia.setNumGarantia(guarantee.getIdGarantia());
		garantia.setDescGarantia(guarantee.getDescGarantia());
		garantia.setMesesGarantia(Constants.MESES_GARANTIA);
		garantia.setIdPersona(idPersona);
		garantia.setIdRelacion(idRelacion);
		garantia.setOtrosTerminosGarantia(guarantee.getOtrosTerminosGarantia());
		garantia.setFechaInscr(java.sql.Date.valueOf(now));
		LocalDate fechaFinVigencia = LocalDate.parse(Constants.FECHA_INICIO_SISTEMA).plusMonths(Constants.MESES_GARANTIA);
		Date fechaFin = java.sql.Date.valueOf(fechaFinVigencia);
		garantia.setFechaFinGar(fechaFin);
		garantia.setVigencia(Constants.VIGENCIA);
		garantia.setGarantiaStatus(Constants.ESTADO_ACTIVO);
		garantia.setIdUltimoTramite(transaction.getIdTramite());
		garantia.setFechaReg(java.sql.Date.valueOf(now));
		garantia.setStatusReg(Constants.ESTADO_ACTIVO);
		garantia.setIdGarantiaPend(idGarantiaPendiente);
		garantia.setInstrumentoPublico(guarantee.getInstrumentoPublico());
		garantia.setIdMoneda(Constants.MONEDA);
		garantia.setEsPrioritaria(Constants.FALSE);
		garantia.setTxtRegistros(guarantee.getTxtRegistros());
		garantiasDao.save(garantia);
	}
	
	public void crearRelacionTramiteGarantia (Long idTramite, Long idGarantia) {
		// RUG_REL_TRAM_GARAN
		RugRelTramGaran relTramGaran = new RugRelTramGaran();
		RugRelTramGaranPK relTramGaranPK = new RugRelTramGaranPK();
		relTramGaranPK.setIdTramite(idTramite);
		relTramGaranPK.setIdGarantia(idGarantia);
		relTramGaran.setId(relTramGaranPK);
		relTramGaran.setBTramiteCompleto(Constants.CONFIRMACION_ES);
		relTramGaran.setStatusReg(Constants.ESTADO_ACTIVO);
		relTramGaran.setFechaReg(java.sql.Date.valueOf(now));
		relTramGaranDao.save(relTramGaran);
	}
	
	public void crearGarantiaH(Transaction transaction, Long idPersona, Long idRelacion, Long idGarantiaPendiente) {
		Guarantee guarantee = transaction.getGuarantee();
		// RUG_GARANTIAS_H
		RugGarantiasH garantiaH = new RugGarantiasH();
		garantiaH.setIdGarantia(guarantee.getIdGarantia());
		garantiaH.setIdTipoGarantia(Constants.TIPO_GARANTIA);
		garantiaH.setNumGarantia(guarantee.getIdGarantia());
		garantiaH.setDescGarantia(guarantee.getDescGarantia());
		garantiaH.setMesesGarantia(Constants.MESES_GARANTIA);
		garantiaH.setOtrosTerminosGarantia(guarantee.getOtrosTerminosGarantia());
		garantiaH.setIdPersona(idPersona);
		garantiaH.setIdRelacion(idRelacion);
		garantiaH.setFechaInscr(java.sql.Date.valueOf(now));
		LocalDate fechaFinVigencia = LocalDate.parse(Constants.FECHA_INICIO_SISTEMA).plusMonths(Constants.MESES_GARANTIA);
		Date fechaFin = java.sql.Date.valueOf(fechaFinVigencia);
		garantiaH.setFechaFinGar(fechaFin);
		garantiaH.setVigencia(Constants.VIGENCIA);
		garantiaH.setGarantiaStatus(Constants.ESTADO_ACTIVO);
		garantiaH.setIdUltimoTramite(transaction.getIdTramite());
		garantiaH.setFechaModifReg(java.sql.Date.valueOf(now));
		garantiaH.setFechaReg(java.sql.Date.valueOf(now));
		garantiaH.setStatusReg(Constants.ESTADO_ACTIVO);
		garantiaH.setIdGarantiaPend(idGarantiaPendiente);
		garantiaH.setInstrumentoPublico(guarantee.getInstrumentoPublico());
		garantiaH.setIdMoneda(Constants.MONEDA);
		garantiaH.setEsPrioritaria(Constants.FALSE);
		garantiaH.setTxtRegistros(guarantee.getTxtRegistros());
		garantiasHDao.save(garantiaH);
	}
	
	public void actualizarDatosGarantia(Transaction transaction) {
		// secciones que se pueden actualizar
		// 	deudores
		//    nombre, nit, dpi, correo, direccion
		//  acreedores
		//  fecha de tramite
		//  descripcion garantia
		//  contrato de garantia
		//  otros terminos
		Tramites tramite = tramitesDao.findById(transaction.getIdTramite());
                /* modificar contrato*/
                
                RugContrato contrato = contratoDao.findByIdTemp(tramite.getIdTramiteTemp());
//                System.out.println("contrato = " + contrato.getOtrosTerminosContrato());
                System.out.println("numero de contrato " + transaction.getGuarantee().getIdContrato());
                System.out.println("valor enviado: " + transaction.getGuarantee().getOtrosTerminos());
                System.out.println("metodo = " + transaction.getControlCambios());
                /******************* idTramiteTemp */
                
		RugGarantias garantia = garantiasDao.findById(transaction.getGuarantee().getIdGarantia());
		RugGarantiasH garantiaH = garantiasHDao.findByTramite(transaction.getIdTramite());
		boolean ultimoTramite = transaction.getIdTramite().longValue() == garantia.getIdUltimoTramite().longValue();
                
		for(String cambio : transaction.getControlCambios()) {
                    
			switch(cambio) {
			case "fechaInscripcion":
				Date fechaTramite;
				try {
					fechaTramite = sdft.parse(transaction.getFechaCreacion());
				} catch (ParseException e) {
					fechaTramite = java.sql.Date.valueOf(now);
				}
				// TramitesRugIncomp.setFechPreInscr, setFechaInscr
				TramitesRugIncomp tramiteRugIncomp = tramitesRugIncompDao.findById(transaction.getIdTramiteTemp());
				tramiteRugIncomp.setFechPreInscr(fechaTramite);
				tramiteRugIncomp.setFechaInscr(fechaTramite);
				// actualizar tramites.fecha_creacion, setFechPreInscr, setFechaInscr, setFechaStatus
				tramite.setFechaCreacion(fechaTramite);
				tramite.setFechPreInscr(fechaTramite);
				tramite.setFechaInscr(fechaTramite);
				tramite.setFechaStatus(fechaTramite);
				// actualizar rug_bitac_tramites.fecha_status
				RugBitacTramites rugBitacTramite = bitacTramitesDao.findByTramiteAndStatus(transaction.getIdTramiteTemp(), Constants.STATUS_TRAMITE);
				rugBitacTramite.setFechaStatus(fechaTramite);
				rugBitacTramite.setFechaReg(fechaTramite);
				// RUG_GARANTIAS_PENDIENTES setFechaInscr
				try {
					RugGarantiasPendientes garantiaPendiente = garantiasPendientesDao.findByTramite(transaction.getIdTramiteTemp());
					garantiaPendiente.setFechaInscr(fechaTramite);
				} catch(NoResultException nre) {
					// no hay garantia pendiente asociada al tramite
				}
				// TODO: actualizar rug_garantias_h
				// si es la inscripcion actualizar rug_garantias.fecha_reg
				if(tramite.getTipoTramite().getIdTipoTramite().intValue() == Constants.TRAMITE_INSCRIPCION.intValue()) {
					garantia.setFechaReg(fechaTramite);
					// TODO: actualizar fechaInscr
				}
				break;
			case "descGarantia":
				// rug_garantias_h.desc_garantia
				garantiaH.setDescGarantia(transaction.getGuarantee().getDescGarantia());
				// si es el ultimo tramite de la garantia, actualizar la garantia
				if(ultimoTramite) {
					// rug_garantias.desc_garantia
					garantia.setDescGarantia(transaction.getGuarantee().getDescGarantia());
				}
				break;
                        case "desContrato":
                                contrato.setObservaciones(transaction.getGuarantee().getTipoContrato());

                            break;
                        case "otrosTerminos":
                                contrato.setOtrosTerminosContrato(transaction.getGuarantee().getOtrosTerminos());
//                            PreparedStatement ps = null;
//                            conectiondB db = new conectiondB();
//                            Connection connection =  db.getConnection();
//                            CallableStatement cs = null;
//                            
//                            String sql = "{ call update_contrato( ?,? ) }";
//                            
//                            try{
//                                
//                                cs = connection.prepareCall(sql);
//                                cs.setString(1, transaction.getGuarantee().getOtrosTerminos());
//                                cs.setLong(2, transaction.getGuarantee().getIdContrato());
//                                cs.execute();
//                        
//                                             
//                            }catch(SQLException e){
//                                System.out.println("e = " + e);
//                            }finally{
//                                db.close(connection, null, ps);
//                            }
                            break;
			case "instrumentoPublico":
				// rug_garantias_h.instrumento_publico
				garantiaH.setInstrumentoPublico(transaction.getGuarantee().getInstrumentoPublico());
				// si es el ultimo tramite de la garantia, actualizar la garantia
				if(ultimoTramite) {
					// rug_garantias.instrumento_publico
					garantia.setInstrumentoPublico(transaction.getGuarantee().getInstrumentoPublico());
				}
				break;
			case "otrosTerminosGarantia":
				// rug_garantias_h.otros_terminos_garantia
				garantiaH.setOtrosTerminosGarantia(transaction.getGuarantee().getOtrosTerminosGarantia());
				// si es el ultimo tramite de la garantia, actualizar la garantia
				if(ultimoTramite) {
					// rug_garantias.otros_terminos_garantia
					garantia.setOtrosTerminosGarantia(transaction.getGuarantee().getOtrosTerminosGarantia());
				}
				break;
			}
		}
	}
}
