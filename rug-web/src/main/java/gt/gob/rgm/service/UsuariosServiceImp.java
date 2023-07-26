package gt.gob.rgm.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gt.gob.rgm.dao.*;
import gt.gob.rgm.model.*;
import gt.gob.rgm.util.FormUsuarios;
import mx.gob.se.rug.constants.Constants;
import org.springframework.context.annotation.Scope;
import gt.gob.rgm.domain.Usuario;
import gt.gob.rgm.util.HashUtils;

@Scope("prototype")
public class UsuariosServiceImp implements UsuariosService {
	
	private RugPersonasRepository personaDao;
	
	private RugPersonasFisicasRepository personaFisicaDao;
	
	private RugSecuUsuariosRepository secuUsuarioDao;


	private RugGrupoRepository grupoDao;


	private RugGrupoSubUsuarioRepository grupoSubUsuario;
	
	private RugSecuPerfilesUsuarioRepository secuPerfilUsuarioDao;
	
	private RelUsuAcreedorRepository relUsuAcreedorDao;
	
	private RugRelGrupoAcreedorRepository rugRelGrupoAcreedorDao;
	
	private RugSecuUsuariosExternosRepository secuUsuarioExtDao;
		
	private BitacoraOperacionesRepository bitacoraDao;

	UsuariosService usuariosBusinessService;


	private RugRelGrupoPrivilegioRepository grupoPrivilegioDao;

	public UsuariosServiceImp() {
	}

	@Override
	public void addBitacora(BitacoraOperaciones bitacora) {
		bitacoraDao.save(bitacora);
	}

	@Override
	public int verificarIsAdmin(FormUsuarios usuarios){
		System.out.println("Resultado " + secuUsuarioDao.cveUsuario(usuarios.getIdUsuario()));
		return 1;
	}

	@Override
	public Usuario GenerateGrupo(Long IdPersona, String name){
		LocalDate now = LocalDate.now();
		RugGruposRoles rPrivilegio = new RugGruposRoles();
		rPrivilegio.setDescGrupo(name);
		rPrivilegio.setFhCreacion(Date.valueOf(now));
		rPrivilegio.setIdAcreedor(IdPersona);
		rPrivilegio.setIdPersonaCrea(IdPersona);
		rPrivilegio.setSitGrupo("AC");
		grupoDao.save(rPrivilegio);
		Usuario usuario = new Usuario();
		usuario.setIdGrupo(new Long(rPrivilegio.getIdGrupo()));

		SetGrupoSubUsuario(IdPersona, rPrivilegio.getIdGrupo());

		return usuario;
	}

	@Override
	public RugGrupoSubUsuarios SetGrupoSubUsuario(Long idPersona, Integer idGrupo){
		try{

			RugGrupoSubUsuarios grupoSubUsuarios = new RugGrupoSubUsuarios();
			grupoSubUsuarios.setIdUsuario(idPersona);
			grupoSubUsuarios.setIdGrupo(idGrupo);
			grupoSubUsuarios.setIdGrupoAnterior(idGrupo);
			grupoSubUsuario.save(grupoSubUsuarios);

			return grupoSubUsuarios;

		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public RugGrupoSubUsuarios GetGrupoSubUsuario(Long idPersona){
		try{
			RugGrupoSubUsuarios existSub = grupoSubUsuario.findGrupoActual(idPersona);

			if(existSub != null){
				return existSub;
			}else{
				return null;
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean permisosDinamicos(Long idPersona, String Nombre, String isAdmin){
		try{
			Usuario usuario = new Usuario();
			try{
				Usuario rolCredo = GenerateGrupo(idPersona,Nombre);
				usuario.setIdGrupo(rolCredo.getIdGrupo());
			}catch (Exception  e){
				e.printStackTrace();
			}

			//Asociar Privilegios al Grupo
			for (int i = 62; i <= 73; i++){
				if(i != 68){
					RugRelGrupoPrivilegio privilegio = new RugRelGrupoPrivilegio();
					privilegio.setIdGrupo(usuario.getIdGrupo());
					privilegio.setIdPrivilegio(new Long(i));
					privilegio.setSitRelacion("AC");
					grupoPrivilegioDao.save(privilegio);
				}

			}

			if(isAdmin.equals("1")){
				RugRelGrupoPrivilegio privilegio = new RugRelGrupoPrivilegio();
				privilegio.setIdGrupo(usuario.getIdGrupo());
				privilegio.setIdPrivilegio(new Long(68));
				privilegio.setSitRelacion("AC");
				grupoPrivilegioDao.save(privilegio);
			}

			rugRelGrupoAcreedorDao.updateByAcreedor(usuario.getIdGrupo(),idPersona);

			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public Usuario add(Usuario usuario) {
		LocalDate now = LocalDate.now();
		// que el correo no exista con otro usuario
		RugSecuUsuarios usuarioExiste = secuUsuarioDao.findByCveUsuario(usuario.getCveUsuario());
		if(usuarioExiste != null) {
			usuario.setMensajeError("El correo ingresado ya está siendo utilizado por otro usuario");
			return usuario;
		}
		
		// insertar en RugPersonas
		RugPersonas persona = new RugPersonas();
		persona.setIdNacionalidad(usuario.getIdNacionalidad());
		persona.setPerJuridica(usuario.getTipoPersona());
		persona.setFhRegistro(Date.valueOf(now));
		persona.setProcedencia("NAL");
		persona.setSitPersona("AC");
		persona.setRegTerminado("Y");
		persona.setInscrito(usuario.getInscritoComo());
		persona.setCurpDoc(usuario.getDocId());
		personaDao.save(persona);
		
		// insertar en RugPersonasFisicas
		RugPersonasFisicas personaFisica = new RugPersonasFisicas();
		personaFisica.setIdPersona(persona.getIdPersona());
		personaFisica.setNombrePersona(usuario.getNombre());
		personaFisica.setCurp(usuario.getDocId());
		personaFisicaDao.save(personaFisica);
		
		// insertar en RugSecuUsuarios
		RugSecuUsuarios secuUsuario = new RugSecuUsuarios();
		secuUsuario.setCveInstitucion(usuario.getCveInstitucion());
		secuUsuario.setCveUsuario(usuario.getCveUsuario());
		secuUsuario.setIdPersona(persona.getIdPersona());
		// encriptar password con algoritmo SHA-1
		String password = HashUtils.hash(usuario.getPassword(), "SHA-1");
		secuUsuario.setPassword(password);
		secuUsuario.setFAsignaPsw(Date.valueOf(now));
		secuUsuario.setPregRecuperaPsw(usuario.getPregRecupera());
		secuUsuario.setRespRecuperaPsw(usuario.getRespRecupera());
		secuUsuario.setFhRegistro(Date.valueOf(now));
		secuUsuario.setSitUsuario("IN");
		secuUsuario.setCveUsuarioPadre(usuario.getCveUsuarioPadre());
		secuUsuario.setCveAcreedor(usuario.getCveAcreedor());
		secuUsuario.setIdGrupo(2); // grupo CIUDADANO
		secuUsuario.setIsJudicial(usuario.getIsJudicial());
		secuUsuario.setIsAdmin(usuario.getIsAdmin());
		/*// generar token
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = now.format(formatter);
		String strToken = formatDateTime + "#" + usuario.getCveUsuario();
		byte[] encodedBytes = Base64.getEncoder().encode(strToken.getBytes());
		String token = new String(encodedBytes);
		secuUsuario.setToken(token);*/
		secuUsuarioDao.save(secuUsuario);
		
		// insertar en RugSecuPerfilesUsuario
		RugSecuPerfilesUsuario secuPerfilUsuario = new RugSecuPerfilesUsuario();
		secuPerfilUsuario.setCveInstitucion(usuario.getCveInstitucion());
		secuPerfilUsuario.setCveUsuario(usuario.getCveUsuario());
		secuPerfilUsuario.setCvePerfil("CIUDADANO");
		secuPerfilUsuario.setCveAplicacion("RugPortal");
		secuPerfilUsuario.setIdPersona(persona.getIdPersona());
		secuPerfilUsuario.setBBloqueado("F");
		secuPerfilUsuarioDao.save(secuPerfilUsuario);

		try{
			Usuario rolCredo = GenerateGrupo(persona.getIdPersona(),usuario.getNombre());
			usuario.setIdGrupo(rolCredo.getIdGrupo());
			usuario.setIsJudicial("");
		}catch (Exception  e){
			e.printStackTrace();
		}


		//Asociar Privilegios al Grupo
		for (int i = 62; i <= 73; i++){
			if(i != 68){
				RugRelGrupoPrivilegio privilegio = new RugRelGrupoPrivilegio();
				privilegio.setIdGrupo(usuario.getIdGrupo());
				privilegio.setIdPrivilegio(new Long(i));
				privilegio.setSitRelacion("AC");
				grupoPrivilegioDao.save(privilegio);
			}

		}

		if(usuario.getIsAdmin().equals("1")){
			RugRelGrupoPrivilegio privilegio = new RugRelGrupoPrivilegio();
			privilegio.setIdGrupo(usuario.getIdGrupo());
			privilegio.setIdPrivilegio(new Long(68));
			privilegio.setSitRelacion("AC");
			grupoPrivilegioDao.save(privilegio);
		}



		
		// activar usuario
		this.activar(persona.getIdPersona(), usuario.getIdPersonaPadre(), usuario.getIdGrupo());
		
		usuario.setIdPersona(persona.getIdPersona());
		
		return usuario;
	}
	
	@Override
	public int activar(Long id, Long idAcreedor, Long idGrupo) {
		LocalDate now = LocalDate.now();
		// actualizar el estado de RugSecuUsuarios
		Optional<RugSecuUsuarios> secuUsuario = secuUsuarioDao.findById(id);
		if(secuUsuario.isPresent()) {
			RugSecuUsuarios usuario = secuUsuario.get();
			usuario.setSitUsuario("AC");
			secuUsuarioDao.save(usuario);
			
			// insertar en RelUsuAcreedor
			RelUsuAcreedor relUsuAcreedor = new RelUsuAcreedor();
			RelUsuAcreedorPK relUsuAcreedorPk = new RelUsuAcreedorPK();
			relUsuAcreedorPk.setIdUsuario(id);
			relUsuAcreedorPk.setIdAcreedor(idAcreedor);
			relUsuAcreedor.setId(relUsuAcreedorPk);
			relUsuAcreedor.setBFirmado("Y");
			relUsuAcreedor.setFechaReg(Date.valueOf(now));
			relUsuAcreedor.setStatusReg("AC");
			relUsuAcreedorDao.save(relUsuAcreedor);
			
			// insertar en RugRelGrupoAcreedor
			RugRelGrupoAcreedor rugRelGrupoAcreedor = new RugRelGrupoAcreedor();
			rugRelGrupoAcreedor.setIdAcreedor(idAcreedor);
			rugRelGrupoAcreedor.setIdSubUsuario(id);
			rugRelGrupoAcreedor.setIdUsuario(idAcreedor);
			rugRelGrupoAcreedor.setStatusReg("AC");
			rugRelGrupoAcreedor.setFechaReg(Date.valueOf(now));
			rugRelGrupoAcreedor.setIdGrupo(idGrupo);
			rugRelGrupoAcreedorDao.save(rugRelGrupoAcreedor);		

			return 1;
		}
		return 0;
	}

	@Override
	public int update(Long id, Usuario usuario) {
		LocalDateTime now = LocalDateTime.now();
		// actualizar en RugPersonas
		Optional<RugPersonas> persona = personaDao.findById(id);
//		try {
			if (persona.isPresent()) {
				RugPersonas personaActualizar = persona.get();
				if (usuario.getIdNacionalidad() != null) {
					personaActualizar.setIdNacionalidad(usuario.getIdNacionalidad());
				}
				if (usuario.getTipoPersona() != null) {
					personaActualizar.setPerJuridica(usuario.getTipoPersona());
				}
				//personaActualizar.setFhRegistro(java.sql.Timestamp.valueOf(now));
				if (usuario.getInscritoComo() != null) {
					personaActualizar.setInscrito(usuario.getInscritoComo());
				}
				personaActualizar.setCurpDoc(usuario.getDocId());
				personaActualizar.setRfc(usuario.getRfc());
				personaDao.save(personaActualizar);

				// actualizar en RugPersonasFisicas
				RugPersonasFisicas personaFisica = personaFisicaDao.findById(id).get();
				personaFisica.setNombrePersona(usuario.getNombre());
				personaFisica.setCurp(usuario.getDocId());
				personaFisicaDao.save(personaFisica);

				// actualizar en RugSecuUsuarios
				RugSecuUsuarios secuUsuario = secuUsuarioDao.findById(id).get();
				secuUsuario.setCveUsuario(usuario.getCveUsuario());
				if(!usuario.getPassword().isEmpty()){
					// encriptar password con algoritmo SHA-1
					String password = HashUtils.hash(usuario.getPassword(), "SHA-1");
					secuUsuario.setPassword(password);
				}

				secuUsuario.setPregRecuperaPsw(usuario.getPregRecupera());
				secuUsuario.setRespRecuperaPsw(usuario.getRespRecupera());
				//secuUsuario.setFhRegistro(java.sql.Timestamp.valueOf(now));
				secuUsuario.setSitUsuario(usuario.getSitUsuario());
				// generar token
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formatDateTime = now.format(formatter);

//				String strToken = formatDateTime + "#" + usuario.getCveUsuario();
//				byte[] encodedBytes = Base64.getEncoder().encode(strToken.getBytes());
//				String token = new String(encodedBytes);
//				secuUsuario.setToken(token);

				//			activar como administrador
				secuUsuario.setIsAdmin(usuario.getIsAdmin());
				secuUsuarioDao.save(secuUsuario);

				// actualizar en RugSecuPerfilesUsuario
				RugSecuPerfilesUsuario secuPerfilUsuario = secuPerfilUsuarioDao.findById(id).get();
				secuPerfilUsuario.setCveUsuario(usuario.getCveUsuario());
				secuPerfilUsuarioDao.save(secuPerfilUsuario);

				RugRelGrupoAcreedor usuarioGrupoAcreedor = rugRelGrupoAcreedorDao.findByIdPersona(id);
				try{
					if(!usuario.getIsAdmin().isEmpty()){
						RugGrupoSubUsuarios existSubUsuario = GetGrupoSubUsuario(id);
						if(existSubUsuario != null){
							grupoSubUsuario.updateGrupoSub(existSubUsuario.getId(), Constants.SUBADMINISTRADOR.intValue());
						}else{
							RugGrupoSubUsuarios usuarioSub = SetGrupoSubUsuario(id, Constants.SUBADMINISTRADOR.intValue());
						}
						usuarioGrupoAcreedor.setIdGrupo(Constants.SUBADMINISTRADOR);
						rugRelGrupoAcreedorDao.save(usuarioGrupoAcreedor);
					}else{
						RugGrupoSubUsuarios existSubUsuario = GetGrupoSubUsuario(id);
						RugGrupoSubUsuarios usuarioSub = new RugGrupoSubUsuarios();
						if(existSubUsuario != null){
							grupoSubUsuario.updateGrupoSub(existSubUsuario.getId(), existSubUsuario.getIdGrupoAnterior());
						}else{
							String grupoUsuario = grupoDao.findByUsuario(new BigDecimal(id));
							usuarioSub = SetGrupoSubUsuario(id, new Integer(grupoUsuario));
						}
						usuarioGrupoAcreedor.setIdGrupo(existSubUsuario != null ? new Long(existSubUsuario.getIdGrupoAnterior()) : new Long(usuarioSub.getIdGrupoAnterior()));
						rugRelGrupoAcreedorDao.save(usuarioGrupoAcreedor);
					}
				}catch(Exception e){
					e.printStackTrace();
				}




				return 1;
			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return 0;
	}
	
	@Override
	public int updateState(Long id, String state) {

//		try {
//			secuUsuarioDao.updateStateUser(id);
//			return 1;
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		return 0;

		Optional<RugSecuUsuarios> secuUsuario = secuUsuarioDao.findById(id);

		if(secuUsuario.isPresent()) {
			try {
				RugSecuUsuarios usuario = secuUsuario.get();
				usuario.setSitUsuario(state);
				secuUsuarioDao.save(usuario);
			}catch (Exception e){
				e.printStackTrace();
			}

			return 1;
		}
		return 0;
	}

	@Override
	public List<Usuario> getSubusuarios(Long id) {
		List<Usuario> usuarios = new ArrayList<>();
		Optional<RugSecuUsuarios> usuarioLogeado = secuUsuarioDao.findById(id);
		System.out.println("Usuario Logeado impl " + usuarioLogeado.get().getCveUsuario());
		if(usuarioLogeado.get().getIsAdmin() != null){
			List<RugSecuUsuarios> secuUsuarios = secuUsuarioDao.findByCveUsuarioPadreAndSitUsuario(usuarioLogeado.get().getCveUsuarioPadre(), "AC");
			List<RugSecuUsuarios> secuUsuariosPadre = secuUsuarioDao.findByCveUsuarioAndSitUsuario(usuarioLogeado.get().getCveUsuarioPadre(), "AC");
			for(RugSecuUsuarios secuUsuario : secuUsuarios) {
					Usuario usuario = transformSecuToUsuario(secuUsuario);
					usuarios.add(usuario);
			}
			for(RugSecuUsuarios secuUsuarioPadre : secuUsuariosPadre) {
					Usuario usuario = transformSecuToUsuarioPadre(secuUsuarioPadre);
					usuarios.add(usuario);
			}
			return usuarios;
		}else{
			List<RugSecuUsuarios> secuUsuarios = secuUsuarioDao.findByCveUsuarioPadreAndSitUsuario(usuarioLogeado.get().getCveUsuario(), "AC");
			for(RugSecuUsuarios secuUsuario : secuUsuarios) {
				Usuario usuario = transformSecuToUsuario(secuUsuario);
				usuarios.add(usuario);
			}
			return usuarios;
		}
	}

	@Override
	public Usuario getUsuario(Long id) {
		Optional<RugSecuUsuarios> secuUsuario = secuUsuarioDao.findById(id);

		return transformSecuToUsuario(secuUsuario.get());
	}
	
	@Override
	public Usuario getUsuarioExterno(String cveUsuario) {
		RugSecuUsuariosExternos secuUsuario = secuUsuarioExtDao.findByCveUsuario(cveUsuario);
                
		Usuario usuario = new Usuario();
		if(secuUsuario!=null) {
			usuario.setIdPersona(secuUsuario.getIdUsuario());
			usuario.setCveUsuario(secuUsuario.getCveUsuario());
			usuario.setPassword(secuUsuario.getPassword());
			usuario.setSitUsuario(secuUsuario.getSitUsuario());
			usuario.setCvePerfil(secuUsuario.getCvePerfil());
			//System.out.println("usuari" + usuario + "id:" + secuUsuario.getIdUsuario() + "usuari2:" + secuUsuario.getCveUsuario()+ "id:" + secuUsuario.getCvePerfil());
			return usuario;
		}
		
		return null; 
	}
	
	private Usuario transformSecuToUsuario(RugSecuUsuarios secuUsuario) {
		Usuario usuario = new Usuario();
		usuario.setIdPersona(secuUsuario.getIdPersona());
		usuario.setRfc(secuUsuario.getPersona().getRfc());
		usuario.setIdNacionalidad(secuUsuario.getPersona().getIdNacionalidad());
		usuario.setTipoPersona(secuUsuario.getPersona().getPerJuridica());
		usuario.setInscritoComo(secuUsuario.getPersona().getInscrito());
		usuario.setDocId(secuUsuario.getPersonaFisica().getCurp());
		usuario.setNombre(secuUsuario.getPersonaFisica().getNombrePersona());
		usuario.setCveInstitucion(secuUsuario.getCveInstitucion());
		usuario.setCveUsuario(secuUsuario.getCveUsuario());
		usuario.setPregRecupera(secuUsuario.getPregRecuperaPsw());
		usuario.setCveUsuarioPadre(secuUsuario.getCveUsuarioPadre());
		usuario.setCveAcreedor(secuUsuario.getCveAcreedor());
		usuario.setIdGrupo(secuUsuario.getIdGrupo());
		usuario.setFirmado(secuUsuario.getBFirmado());
		usuario.setCvePerfil(secuUsuario.getPerfilUsuario().getCvePerfil());
		usuario.setCveAplicacion(secuUsuario.getPerfilUsuario().getCveAplicacion());
		usuario.setIsJudicial(secuUsuario.getIsJudicial());
		if(secuUsuario.getIsAdmin() == null){
			usuario.setIsAdmin("SubUsuario");
		}else if(secuUsuario.getCveUsuarioPadre().isEmpty())
		{
			usuario.setIsAdmin("Administrador");
		}
		else{
			usuario.setIsAdmin("Administrador");
		}
		usuario.setIsAdminButton(secuUsuario.getIsAdmin());

		
		return usuario;
	}
	private Usuario transformSecuToUsuarioPadre(RugSecuUsuarios secuUsuario) {
		Usuario usuario = new Usuario();
		usuario.setIdPersona(secuUsuario.getIdPersona());
		usuario.setRfc(secuUsuario.getPersona().getRfc());
		usuario.setIdNacionalidad(secuUsuario.getPersona().getIdNacionalidad());
		usuario.setTipoPersona(secuUsuario.getPersona().getPerJuridica());
		usuario.setInscritoComo(secuUsuario.getPersona().getInscrito());
		usuario.setDocId(secuUsuario.getPersonaFisica().getCurp());
		usuario.setNombre(secuUsuario.getPersonaFisica().getNombrePersona());
		usuario.setCveInstitucion(secuUsuario.getCveInstitucion());
		usuario.setCveUsuario(secuUsuario.getCveUsuario());
		usuario.setPregRecupera(secuUsuario.getPregRecuperaPsw());
		usuario.setCveUsuarioPadre(secuUsuario.getCveUsuarioPadre());
		usuario.setCveAcreedor(secuUsuario.getCveAcreedor());
		usuario.setIdGrupo(secuUsuario.getIdGrupo());
		usuario.setFirmado(secuUsuario.getBFirmado());
		usuario.setCvePerfil(secuUsuario.getPerfilUsuario().getCvePerfil());
		usuario.setCveAplicacion(secuUsuario.getPerfilUsuario().getCveAplicacion());
		usuario.setIsJudicial(secuUsuario.getIsJudicial());
		usuario.setIsAdmin("Administrador");

		usuario.setIsAdminButton(secuUsuario.getIsAdmin());


		return usuario;
	}

	public void setPersonaDao(RugPersonasRepository personaDao) {
		this.personaDao = personaDao;
	}

	public void setPersonaFisicaDao(RugPersonasFisicasRepository personaFisicaDao) {
		this.personaFisicaDao = personaFisicaDao;
	}

	public void setSecuUsuarioDao(RugSecuUsuariosRepository secuUsuarioDao) {
		this.secuUsuarioDao = secuUsuarioDao;
	}

	public void setSecuPerfilUsuarioDao(RugSecuPerfilesUsuarioRepository secuPerfilUsuarioDao) {
		this.secuPerfilUsuarioDao = secuPerfilUsuarioDao;
	}

	public void setRelUsuAcreedorDao(RelUsuAcreedorRepository relUsuAcreedorDao) {
		this.relUsuAcreedorDao = relUsuAcreedorDao;
	}

	public void setGrupoDao(RugGrupoRepository grupoDao) {
		this.grupoDao = grupoDao;
	}

	public void setGrupoPrivilegioDao(RugRelGrupoPrivilegioRepository grupoPrivilegioDao) {
		this.grupoPrivilegioDao = grupoPrivilegioDao;
	}



	public void setRugRelGrupoAcreedorDao(RugRelGrupoAcreedorRepository rugRelGrupoAcreedorDao) {
		this.rugRelGrupoAcreedorDao = rugRelGrupoAcreedorDao;
	}

	public void setSecuUsuarioExtDao(RugSecuUsuariosExternosRepository secuUsuarioExtDao) {
		this.secuUsuarioExtDao = secuUsuarioExtDao;
	}

	public void setBitacoraDao(BitacoraOperacionesRepository bitacoraDao) {
		this.bitacoraDao = bitacoraDao;
	}

	public void setGrupoSubUsuario(RugGrupoSubUsuarioRepository grupoSubUsuario) {
		this.grupoSubUsuario = grupoSubUsuario;
	}
}
