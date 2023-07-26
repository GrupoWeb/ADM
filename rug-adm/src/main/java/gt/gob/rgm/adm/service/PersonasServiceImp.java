package gt.gob.rgm.adm.service;

import java.sql.Date;
import java.time.LocalDate;

import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RelUsuAcreedorRepository;
import gt.gob.rgm.adm.dao.RugDomiciliosExtHRepository;
import gt.gob.rgm.adm.dao.RugDomiciliosExtRepository;
import gt.gob.rgm.adm.dao.RugPersonasFisicasRepository;
import gt.gob.rgm.adm.dao.RugPersonasHRepository;
import gt.gob.rgm.adm.dao.RugPersonasMoralesRepository;
import gt.gob.rgm.adm.dao.RugPersonasRepository;
import gt.gob.rgm.adm.dao.RugRelGarantiaPartesRepository;
import gt.gob.rgm.adm.dao.RugRelGrupoAcreedorRepository;
import gt.gob.rgm.adm.dao.RugRelTramIncParteRepository;
import gt.gob.rgm.adm.dao.RugRelTramPartesRepository;
import gt.gob.rgm.adm.dao.RugSecuPerfilesUsuarioRepository;
import gt.gob.rgm.adm.dao.RugSecuUsuarioRepository;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.model.RelUsuAcreedor;
import gt.gob.rgm.adm.model.RelUsuAcreedorPK;
import gt.gob.rgm.adm.model.RugDomiciliosExt;
import gt.gob.rgm.adm.model.RugDomiciliosExtH;
import gt.gob.rgm.adm.model.RugDomiciliosExtHPK;
import gt.gob.rgm.adm.model.RugPersonas;
import gt.gob.rgm.adm.model.RugPersonasFisicas;
import gt.gob.rgm.adm.model.RugPersonasH;
import gt.gob.rgm.adm.model.RugPersonasHPK;
import gt.gob.rgm.adm.model.RugPersonasMorales;
import gt.gob.rgm.adm.model.RugRelGarantiaPartes;
import gt.gob.rgm.adm.model.RugRelGarantiaPartesPK;
import gt.gob.rgm.adm.model.RugRelGrupoAcreedor;
import gt.gob.rgm.adm.model.RugRelTramIncParte;
import gt.gob.rgm.adm.model.RugRelTramIncPartePK;
import gt.gob.rgm.adm.model.RugRelTramPartes;
import gt.gob.rgm.adm.model.RugRelTramPartesPK;
import gt.gob.rgm.adm.model.RugSecuPerfilesUsuario;
import gt.gob.rgm.adm.model.RugSecuUsuario;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.CryptoUtils;

public class PersonasServiceImp {
	@Inject
	RugPersonasRepository personasDao;
	
	@Inject
	RugPersonasFisicasRepository personasFisicasDao;
	
	@Inject
	RugPersonasMoralesRepository personasMoralesDao;
	
	@Inject
	RugSecuUsuarioRepository secuUsuariosDao;
	
	@Inject
	RugSecuPerfilesUsuarioRepository secuPerfilesUsuarioDao;
	
	@Inject
	RelUsuAcreedorRepository usuAcreedorDao;
	
	@Inject
	RugRelGrupoAcreedorRepository relGrupoAcreedorDao;
	
	@Inject
	RugDomiciliosExtRepository domiciliosExtDao;
	
	@Inject
	RugRelTramIncParteRepository relTramIncParteDao;
	
	@Inject
	RugRelTramPartesRepository relTramPartesDao;
	
	@Inject
	RugRelGarantiaPartesRepository relGarantiaPartesDao;
	
	@Inject
	RugPersonasHRepository personasHDao;
	
	@Inject
	RugDomiciliosExtHRepository domiciliosExtHDao;
	
	private LocalDate now;
	
	public RugPersonas crear(ExternalUser externalUser, Integer idParte, Long idTramite, Boolean activar, LocalDate now) {
		this.now = now;
		RugPersonas persona = crearPersona(externalUser);
		if(idParte.intValue() == Constants.PARTE_SOLICITANTE.intValue() || externalUser.getPersonType().equals("PF")) {
			crearPersonaFisica(persona.getIdPersona(), externalUser);
		} else {
			crearPersonaMoral(persona.getIdPersona(), externalUser);
		}
		if(idParte.intValue() == Constants.PARTE_SOLICITANTE.intValue()) {
			crearSecuUsuario(persona.getIdPersona(), externalUser);
			crearSecuPerfilUsuario(persona.getIdPersona(), externalUser);
			if(activar) {
				activarUsuario(persona.getIdPersona(), externalUser.getGroupId());
			}
		} else {
			// insertar en domicilio
			RugDomiciliosExt domicilio = new RugDomiciliosExt();
			domicilio.setIdPaisResidencia(externalUser.getNationality());
			domicilio.setUbicaDomicilio1(externalUser.getAddress());
			Long idDomicilio = domiciliosExtDao.save(domicilio);
			// actualizar persona con domicilio
			persona.setIdDomicilio(idDomicilio);
			personasDao.save(persona);
			// insertar la relacion con tramite incompleto
			RugRelTramIncParte relTramIncParte = new RugRelTramIncParte();
			RugRelTramIncPartePK relTramIncPartePK = new RugRelTramIncPartePK();
			relTramIncPartePK.setIdPersona(persona.getIdPersona());
			relTramIncPartePK.setIdTramiteTemp(idTramite);
			relTramIncPartePK.setIdParte(idParte);
			relTramIncParte.setId(relTramIncPartePK);
			relTramIncParte.setPerJuridica(persona.getPerJuridica());
			relTramIncParte.setStatusReg(Constants.ESTADO_ACTIVO);
			relTramIncParte.setFechaReg(Date.valueOf(now));
			relTramIncParteDao.save(relTramIncParte);
		}
		return persona;
	}
	
	public RugPersonas crearPersona(ExternalUser externalUser) {
		RugPersonas persona = new RugPersonas();
		persona.setIdNacionalidad(externalUser.getNationality());
		persona.setPerJuridica(externalUser.getPersonType());
		persona.setFhRegistro(Date.valueOf(now));
		persona.setProcedencia(Constants.PERSONA_PROCEDENCIA);
		persona.setSitPersona(Constants.ESTADO_ACTIVO);
		persona.setRegTerminado(Constants.CONFIRMACION);
		persona.setInscrito(externalUser.getRegistered());
		persona.setCurpDoc(externalUser.getDocId());
		personasDao.save(persona);
		return persona;
	}
	
	public void crearPersonaFisica(Long idPersona, ExternalUser externalUser) {
		RugPersonasFisicas personaFisica = new RugPersonasFisicas();
		personaFisica.setIdPersona(idPersona);
		personaFisica.setNombrePersona(externalUser.getName());
		personaFisica.setCurp(externalUser.getDocId());
		personasFisicasDao.save(personaFisica);
	}
	
	public void crearPersonaMoral(Long idPersona, ExternalUser externalUser) {
		RugPersonasMorales personaMoral = new RugPersonasMorales();
		personaMoral.setIdPersona(idPersona);
		personaMoral.setRazonSocial(externalUser.getName());
		personaMoral.setNumInscrita(externalUser.getLegalInscription());
		personaMoral.setUbicada(externalUser.getRepresentativeInfo());
		personasMoralesDao.save(personaMoral);
	}
	
	public void crearSecuUsuario(Long idPersona, ExternalUser externalUser) {
		RugSecuUsuario secuUsuario = new RugSecuUsuario();
		secuUsuario.setCveInstitucion(Constants.INSTITUCION);
		secuUsuario.setCveUsuario(externalUser.getEmail());
		secuUsuario.setIdPersona(idPersona);
		String password = CryptoUtils.hashAlgorithm(externalUser.getPassword(), "SHA-1");
		secuUsuario.setPassword(password);
		secuUsuario.setFAsignaPsw(Date.valueOf(now));
		secuUsuario.setPregRecuperaPsw(externalUser.getSecurityQuestion());
		secuUsuario.setRespRecuperaPsw(externalUser.getSecurityAnswer());
		secuUsuario.setFhRegistro(Date.valueOf(now));
		secuUsuario.setSitUsuario(Constants.ESTADO_INACTIVO);
		secuUsuario.setCveUsuarioPadre(externalUser.getParentEmail());
		secuUsuario.setCveAcreedor(externalUser.getEmail());
		secuUsuario.setIdGrupo(Constants.GRUPO_CIUDADANO);
		secuUsuario.setIsJudicial(externalUser.getIsJudicial());
		secuUsuariosDao.save(secuUsuario);
	}
	
	public void crearSecuPerfilUsuario(Long idPersona, ExternalUser externalUser) {
		RugSecuPerfilesUsuario secuPerfilUsuario = new RugSecuPerfilesUsuario();
		secuPerfilUsuario.setCveInstitucion(Constants.INSTITUCION);
		secuPerfilUsuario.setCveUsuario(externalUser.getEmail());
		secuPerfilUsuario.setCvePerfil(Constants.PERFIL);
		secuPerfilUsuario.setCveAplicacion(Constants.APLICACION);
		secuPerfilUsuario.setIdPersona(idPersona);
		secuPerfilUsuario.setBBloqueado(Constants.FALSE);
		secuPerfilesUsuarioDao.save(secuPerfilUsuario);
	}
	
	public void activarUsuario(Long idPersona, Integer idGrupo) {
		RugSecuUsuario secuUsuario = secuUsuariosDao.findById(idPersona);
		secuUsuario.setSitUsuario(Constants.ESTADO_ACTIVO);
		secuUsuariosDao.save(secuUsuario);
		
		crearRelacionUsuarioAcreedor(idPersona);		
		crearRelacionGrupoAcreedor(idPersona, idGrupo);		
	}
	
	public void crearRelacionUsuarioAcreedor(Long idPersona) {
		RelUsuAcreedor relUsuAcreedor = new RelUsuAcreedor();
		RelUsuAcreedorPK relUsuAcreedorPk = new RelUsuAcreedorPK();
		relUsuAcreedorPk.setIdUsuario(idPersona);
		relUsuAcreedorPk.setIdAcreedor(idPersona);
		relUsuAcreedor.setId(relUsuAcreedorPk);
		relUsuAcreedor.setBFirmado(Constants.CONFIRMACION);
		relUsuAcreedor.setFechaReg(Date.valueOf(now));
		relUsuAcreedor.setStatusReg(Constants.ESTADO_ACTIVO);
		usuAcreedorDao.save(relUsuAcreedor);
	}
	
	public void crearRelacionGrupoAcreedor(Long idPersona, Integer idGrupo) {
		RugRelGrupoAcreedor rugRelGrupoAcreedor = new RugRelGrupoAcreedor();
		rugRelGrupoAcreedor.setIdAcreedor(idPersona);
		rugRelGrupoAcreedor.setIdSubUsuario(idPersona);
		rugRelGrupoAcreedor.setIdUsuario(idPersona);
		rugRelGrupoAcreedor.setStatusReg(Constants.ESTADO_ACTIVO);
		rugRelGrupoAcreedor.setFechaReg(Date.valueOf(now));
		rugRelGrupoAcreedor.setIdGrupo(idGrupo);
		relGrupoAcreedorDao.save(rugRelGrupoAcreedor);
	}
	
	public void crearRelacionTramiteParte(Long idTramite, Long idPersona, Integer idParte, String perJuridica) {
		// rug_rel_tram_partes
		RugRelTramPartes relTramParte = new RugRelTramPartes();
		RugRelTramPartesPK relTramPartePK = new RugRelTramPartesPK();
		relTramPartePK.setIdTramite(idTramite);
		relTramPartePK.setIdPersona(idPersona);
		relTramPartePK.setIdParte(idParte);
		relTramParte.setId(relTramPartePK);
		relTramParte.setPerJuridica(perJuridica);
		relTramParte.setStatusReg(Constants.ESTADO_ACTIVO);
		relTramParte.setFechaReg(Date.valueOf(now));
		relTramPartesDao.save(relTramParte);
	}
	
	public void crearRelacionGarantiaParte(Long idGarantia, Long idPersona, Integer idParte, Long idRelacion) {
		// rug_rel_garantia_partes
		RugRelGarantiaPartes relGarantiaParte = new RugRelGarantiaPartes();
		RugRelGarantiaPartesPK relGarantiaPartePK = new RugRelGarantiaPartesPK();
		relGarantiaPartePK.setIdGarantia(idGarantia);
		relGarantiaPartePK.setIdPersona(idPersona);
		relGarantiaPartePK.setIdParte(idParte);
		relGarantiaPartePK.setIdRelacion(idRelacion);
		relGarantiaParte.setId(relGarantiaPartePK);
		relGarantiaParte.setFechaReg(Date.valueOf(now));
		relGarantiaParte.setStatusReg(Constants.ESTADO_ACTIVO);
		relGarantiaPartesDao.save(relGarantiaParte);
	}
	
	public void crearPersonasH(Long idTramite, Integer idParte, ExternalUser persona, Long idDomicilio) {
		// RUG_PERSONAS_H
		RugPersonasH personaH = new RugPersonasH();
		RugPersonasHPK personaHPK = new RugPersonasHPK();
		personaHPK.setIdTramite(idTramite);
		personaHPK.setIdParte(idParte);
		personaHPK.setIdPersona(persona.getPersonaId());
		personaH.setId(personaHPK);
		if(persona.getPersonType().equals(Constants.PERSONA_INDIVIDUAL)) {
			personaH.setNombrePersona(persona.getName());
		} else {
			personaH.setRazonSocial(persona.getName());
		}
		personaH.setPerJuridica(persona.getPersonType());
		personaH.setIdNacionalidad(persona.getNationality());
		personaH.setRfc(persona.getNit());
		personaH.setCurp(persona.getDocId());
		personaH.setCurpDoc(persona.getDocId());
		personaH.setEMail(persona.getEmail());
		personaH.setCvePerfil(Constants.PERFIL);
		personasHDao.save(personaH);
		
		// RUG_DOMICILIOS_EXT_H
		if(idDomicilio != null) {
			RugDomiciliosExt domicilioExt = domiciliosExtDao.getDomicilio(idDomicilio);
			RugDomiciliosExtH domicilioExtH = new RugDomiciliosExtH();
			RugDomiciliosExtHPK domicilioExtHPK = new RugDomiciliosExtHPK();
			domicilioExtHPK.setIdTramite(idTramite);
			domicilioExtHPK.setIdParte(idParte);
			domicilioExtHPK.setIdPersona(persona.getPersonaId());
			domicilioExtHPK.setIdDomicilio(idDomicilio);
			domicilioExtH.setId(domicilioExtHPK);
			domicilioExtH.setIdPaisResidencia(persona.getNationality());
			domicilioExtH.setUbicaDomicilio1(domicilioExt.getUbicaDomicilio1());
			domiciliosExtHDao.save(domicilioExtH);
		}
	}
}
