package gt.gob.rgm.adm.rs;

import java.sql.Date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.ExternalUserFile;
import gt.gob.rgm.adm.domain.GuaranteeTransactionPart;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.domain.SecuUsuarioStats;
import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.domain.TransactionPart;
import gt.gob.rgm.adm.domain.TransactionPreview;
import gt.gob.rgm.adm.domain.Vinculacion;
import gt.gob.rgm.adm.model.Archivo;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RechazoCuenta;
import gt.gob.rgm.adm.model.RugMailPool;
import gt.gob.rgm.adm.model.RugPersonas;
import gt.gob.rgm.adm.model.RugRelTramGaran;
import gt.gob.rgm.adm.model.RugRelTramPartes;
import gt.gob.rgm.adm.model.RugRelTramPartesPK;
import gt.gob.rgm.adm.model.RugSecuUsuario;
import gt.gob.rgm.adm.service.*;
import gt.gob.rgm.adm.solr.PersonasCollection;
import gt.gob.rgm.adm.solr.SolrResult;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.MailUtils;

@Path("/secu-usuarios")
@RequestScoped
public class SecuUsuariosRs {
	
	@Inject
	RugSecuUsuarioService usuarioService;
	
	@Inject
	RugParametroConfService parametroService;
	
	@Inject
	RechazoCuentaService rechazoService;

	@Inject
	EmailServices emailServices;
	
	@Inject
	RugPersonasService personasService;
	
	@Inject
	MailSendServiceImp mailSendService;
	
	@Inject
	BitacoraOperacionesService bitacoraService;
	
	@Inject
	RugMailPoolService mailPoolService;
	
	@Inject
	RugRelTramPartesService partesService;
	
	@Inject
	RugRelTramGaranService tramGaranService;

	@Inject
	PersonasCollection personasCollection;
	
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getUsers(@QueryParam(value="state") String state, @QueryParam(value="migracion") Boolean migracion, @QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size, @QueryParam(value="name") String name, @QueryParam(value="email") String email, @QueryParam(value="docId") String docId, @QueryParam(value="nit") String nit, @QueryParam(value="emailError") Boolean emailError) {
    	ResponseRs response = new ResponseRs();
    	List<ExternalUser> externalUsers = new ArrayList<>();
    	List<RugSecuUsuario> secuUsers = null;
    	Long usersCount = 0L;
    	ExternalUser externalUserFilter = new ExternalUser();
    	externalUserFilter.setStatus(state);
    	externalUserFilter.setName(name);
    	externalUserFilter.setEmail(email);
    	externalUserFilter.setDocId(docId);
    	externalUserFilter.setNit(nit);
    	externalUserFilter.setMigration(migracion);
    	externalUserFilter.setCorreosError(emailError == null || !emailError ? 0L : 1L);
    	secuUsers = usuarioService.listUsuarios(externalUserFilter, page, size);
    	usersCount = usuarioService.countUsuarios(externalUserFilter);
    	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
    	for(RugSecuUsuario secuUser : secuUsers) {
    		ExternalUser externalUser = new ExternalUser();
    		externalUser.setDocId(secuUser.getPersona().getCurpDoc());
    		externalUser.setNit(secuUser.getPersona().getRfc());
    		externalUser.setName(secuUser.getPersonaFisica().getNombrePersona());
    		externalUser.setPersonaId(secuUser.getIdPersona());
    		externalUser.setEmail(secuUser.getCveUsuario());
    		externalUser.setRegistered(formatter.format(secuUser.getFhRegistro().getTime()));
    		externalUser.setStatus(secuUser.getSitUsuario());
    		externalUser.setRespuesta(secuUser.getRespRecuperaPsw());
    		
    		if(secuUser.getPersona().getCodigoRegistro() != null) {
    			externalUser.setRegistryCode(String.valueOf(secuUser.getPersona().getCodigoRegistro()) + (secuUser.getPersona().getPerJuridica().equals("PF") ? "I" : "J"));
    		}
    		
    		if(secuUser.getAdjuntos() != null && secuUser.getAdjuntos().size() > 0) {
    			List<ExternalUserFile> externalUserFiles = new ArrayList<>();
    			for(Archivo adjunto  : secuUser.getAdjuntos()) {
    				if(adjunto.getEstado().equals("AC")) {
        				ExternalUserFile externalUserFile = new ExternalUserFile();
        				externalUserFile.setArchivoId(adjunto.getArchivoId());
        				externalUserFile.setTipo(adjunto.getTipo());
        				externalUserFile.setUrl(hostUrl + "/attachment/" + adjunto.getUrl());
        				
        				externalUserFiles.add(externalUserFile);
    				}
    			}
    			externalUser.setFiles(externalUserFiles);
    		}
    		
    		externalUser.setCorreosNoEnviados(mailPoolService.countMailUsuario(secuUser.getCveUsuario(), 1L));
    		externalUser.setCorreosError(mailPoolService.countMailUsuario(secuUser.getCveUsuario(), 3L));
    		externalUsers.add(externalUser);
    	}
    	response.setTotal(usersCount);
    	response.setData(externalUsers);
        return response;
    }
    
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public Long getUsersCount(@QueryParam(value="state") String state, @QueryParam(value="migracion") Boolean migracion) {
    	Long usersCount = 0L;
    	if(state != null) {
    		usersCount = usuarioService.countUsuarios(state);
    	} else {
    		if(migracion != null && !migracion) {
    			usersCount = usuarioService.countUsuariosNoMigracion();
    		} else {
    			usersCount = usuarioService.countAllUsuarios();
    		}
    	}
		
        return usersCount;
    }
    
    @PUT
    @Path("/{id}/state")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ExternalUser resolveUserRequest(@HeaderParam("Authorization") String authorization, @PathParam(value="id") Long id, ExternalUser usuario) {
		ExternalUser externalUser = new ExternalUser();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	// actualizar registro de usuario
    	if(usuarioService.update(id, usuario.getStatus()) > 0) {
    		RugSecuUsuario secuUser = usuarioService.getUsuario(id);
			int idTipoMensaje = 1;
			int idAccountSmtp = Integer.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
			String to = secuUser.getCveUsuario();
			String cc = null;
			String cco = null;
    		String hostUrl = parametroService.getParam(Constants.HOST_URL).getValorParametro();
    		// agregar a bitacora
    		String resultado = (usuario.getStatus().equals("IN") ? "aprobado" : "rechazado");
			String authToken = authorization.split(" ")[1];
			long usuarioId = bitacoraService.createEntry(authToken, "Se ha " + resultado + " al usuario RUG " + secuUser.getCveUsuario() + (usuario.getStatus().equals("IN") ? "" : ", causa: " + usuario.getCause()));
    		if(usuario.getStatus().equals("RE")) {
        		// grabar la causa de rechazo
        		RechazoCuenta rechazo = new RechazoCuenta();
        		rechazo.setIdPersona(id);
        		rechazo.setFecha(Date.valueOf(LocalDate.now()));
        		rechazo.setUsuarioId(usuarioId);
        		rechazo.setCausa(usuario.getCause());
        		// generar token 
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formatDateTime = now.format(newFormatter);
				String strToken = formatDateTime + "#" + to;
				byte[] encodedBytes = Base64.getEncoder().encode(strToken.getBytes());
				String token = new String(encodedBytes);
				rechazo.setToken(token);
				
        		rechazoService.save(rechazo);

        		// enviar correo con la causa del rechazo de la cuenta
        		String rectificaUrl = hostUrl + "rs/usuarios/rectificar?token=" + token;
    			String subject = parametroService.getParam(Constants.MAIL_SUBJECT_RECHAZO).getValorParametro();
    			String message = parametroService.getParam(Constants.TEMPLATE_REBOUND).getValorParametro();
    			subject = subject.replace("@nombreCompleto", secuUser.getPersonaFisica().getNombrePersona());
    			message = message.replace("@nombreCompleto", secuUser.getPersonaFisica().getNombrePersona());
    			message = message.replace("@cve_usuario", secuUser.getCveUsuario());
    			message = message.replace("@causa", usuario.getCause());
    			message = message.replace("@rectificaUrl", rectificaUrl);
    			try {
					emailServices.RechazoEmail(subject, message, parametroService.getParam(Constants.SEND_PATH).getValorParametro(), to, "", "");
//    				int idMail = MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
//    				mailSendService.sendMessage("ENVIAR:" + idMail);
    			} catch(Exception e) {
    				// si el envio del correo falla, se captura la excepcion pero continua el proceso
    		    	e.printStackTrace();
    			}

    		} else {
    			// generar codigo de registro de persona
    			RugPersonas persona = personasService.getPersona(id);
    			long nuevoCodigo = personasService.getNextRegistry(persona.getPerJuridica());
    			personasService.updateRegistro(id, nuevoCodigo);
    			String codigoRegistro = String.valueOf(nuevoCodigo) + (persona.getPerJuridica().equals("PF") ? "I" : "J");
	        	// enviar correo con resultado exitoso
	    		// obtener al usuario
	    		
	    		externalUser.setDocId(secuUser.getPersonaFisica().getCurp());
	    		externalUser.setName(secuUser.getPersonaFisica().getNombrePersona());
	    		externalUser.setPersonaId(secuUser.getIdPersona());
	    		externalUser.setEmail(secuUser.getCveUsuario());
	    		externalUser.setRegistered(formatter.format(secuUser.getFhRegistro().getTime()));
	    		externalUser.setStatus(secuUser.getSitUsuario());
	    				
	    		String activarUrl = hostUrl + "rs/usuarios/activar?token=" + secuUser.getToken();
				String subject = parametroService.getParam(Constants.MAIL_SUBJECT_APROBACION).getValorParametro();
				String message = parametroService.getParam(Constants.TEMPLATE_APPROVE).getValorParametro();
				subject = subject.replace("@nombreCompleto", secuUser.getPersonaFisica().getNombrePersona());
				message = message.replace("@nombreCompleto", secuUser.getPersonaFisica().getNombrePersona());
				message = message.replace("@cve_usuario", secuUser.getCveUsuario());
				message = message.replace("@uri", hostUrl);
				message = message.replace("@activarUrl", activarUrl);
				message = message.replace("@codigoRegistro", codigoRegistro);
				try {
					emailServices.AprobacionEmail(subject, message, parametroService.getParam(Constants.SEND_PATH).getValorParametro(), to, "", "");

//					MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
				} catch(Exception e) {
					// si el envio del correo falla, se captura la excepcion pero continua el proceso
			    	e.printStackTrace();
    			}
			}
		} else {
    		// enviar correo con resultado error
    	}
    	
    	return externalUser;
    }
    
    @GET
    @Path("/reporte")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public List<GenericCount> getCountUsuarios(@QueryParam(value = "fechaInicio") String fechaInicio,
    		@QueryParam(value = "fechaFin") String fechaFin,
    		@QueryParam(value = "im") Boolean incluirMigracion) {
    	List<GenericCount> usuarios = usuarioService.summaryUsuarios(fechaInicio, fechaFin, incluirMigracion);
    	
    	return usuarios;
    }
    
    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public List<SecuUsuarioStats> getStats(@QueryParam(value = "fechaInicio") String fechaInicio,
    		@QueryParam(value = "fechaFin") String fechaFin,
    		@QueryParam(value = "cf") String cfecha,
    		@QueryParam(value = "fields") String fields,
    		@QueryParam(value = "dis") Integer diaInicioSemana,
    		@QueryParam(value = "im") Boolean incluirMigracion) {
    	return usuarioService.statsSecuUsuarios(fechaInicio, fechaFin, cfecha, fields, diaInicioSemana, incluirMigracion);
    }
    
    @GET
    @Path("/{id}/mails")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public List<RugMailPool> getMails(@PathParam(value="id") Long secuUserId, @QueryParam(value="todos") Boolean todos) {
    	RugSecuUsuario usuario = usuarioService.getUsuario(secuUserId);
    	List<RugMailPool> correosNoEnviados = mailPoolService.listMailUsuario(usuario.getCveUsuario(), 1L);
    	List<RugMailPool> correosError = mailPoolService.listMailUsuario(usuario.getCveUsuario(), 3L);
    	correosNoEnviados.addAll(correosError);
    	if(todos != null && todos) {
    		List<RugMailPool> correos = mailPoolService.listMailUsuario(usuario.getCveUsuario(), 4L);
    		correosNoEnviados.addAll(correos);
    	}
    	
    	return correosNoEnviados;
    }
    
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ExternalUser searchUserByMail(@QueryParam(value="email") String email) {
    	List<RugSecuUsuario> secuUsers = null;
    	ExternalUser externalUser = new ExternalUser();
    	ExternalUser externalUserFilter = new ExternalUser();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	externalUserFilter.setEmail(email);
    	secuUsers = usuarioService.listUsuarios(externalUserFilter, null, null);
    	if(secuUsers != null && !secuUsers.isEmpty()) {
    		externalUser.setDocId(secuUsers.get(0).getPersona().getCurpDoc());
    		externalUser.setNit(secuUsers.get(0).getPersona().getRfc());
    		externalUser.setName(secuUsers.get(0).getPersonaFisica().getNombrePersona());
    		externalUser.setPersonaId(secuUsers.get(0).getIdPersona());
    		externalUser.setEmail(secuUsers.get(0).getCveUsuario());
    		externalUser.setRegistered(formatter.format(secuUsers.get(0).getFhRegistro().getTime()));
    		externalUser.setStatus(secuUsers.get(0).getSitUsuario());
    		externalUser.setRespuesta(secuUsers.get(0).getRespRecuperaPsw());
    	}
    	return externalUser;
    }
    
    @GET
    @Path("/index")
    @Produces(MediaType.APPLICATION_JSON)
    public String indexar(@QueryParam(value="id") Long idTramite) {
    	//personasCollection.indexarPersonas(idPersona);
    	personasCollection.indexarTramite(idTramite);
    	return "Finalizado";
    }
    
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs searchUser(@QueryParam(value="q") String query, @QueryParam(value="rows") Integer rows, @QueryParam(value="start") Integer start) {
    	ResponseRs response = new ResponseRs();
        System.out.println("Query" + query);
    	SolrResult result = personasCollection.searchUser(query);
    	if(result != null) {
    		response.setTotal(result.getResponse().getNumFound());
    		response.setData(result.getResponse().getDocs());
    	}
    	return response;
    }
    
    @GET
	@Path("/{id}/partes")
	@Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
	public List<GuaranteeTransactionPart> getPartes(@PathParam(value="id") Long idPersona, @QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size) {
    	RugRelTramPartes partesFilter = new RugRelTramPartes();
		RugRelTramPartesPK id = new RugRelTramPartesPK();
		id.setIdPersona(idPersona);
		partesFilter.setId(id);
    	
		List<RugRelTramPartes> partes = partesService.getPartes(partesFilter, page, size);
		List<GuaranteeTransactionPart> transactions = new ArrayList<>();
		for(RugRelTramPartes parte : partes) {
			GuaranteeTransactionPart transaction = new GuaranteeTransactionPart();
			transaction.setIdTramite(parte.getId().getIdTramite());
			transaction.setIdParte(parte.getId().getIdParte());
			transaction.setIdPersona(parte.getId().getIdPersona());
			transaction.setPerJuridica(parte.getPerJuridica());
			// obtener la relacion con garantias
			RugRelTramGaran relTramGaran = tramGaranService.findByTramite(transaction.getIdTramite());
			if(relTramGaran != null && relTramGaran.getId() != null) {
				transaction.setIdGarantia(relTramGaran.getId().getIdGarantia());
			}
			transactions.add(transaction);
		}
		return transactions;
	}
    
    @GET
    @Path("/{id}/saldo")
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public Double getSaldo(@PathParam(value="id") Long idPersona) {
    	return usuarioService.getSaldo(idPersona);
    }
    
    @PUT
    @Path("/{id}/modmigrado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseRs modificarMigrado(@PathParam(value="id") Long idPersona, Vinculacion vinculacion) {
    	ResponseRs respuesta = new ResponseRs();
    	Boolean resultado = usuarioService.modificarMigrado(idPersona, vinculacion.getCausa(), vinculacion.getUsuario());
    	respuesta.setData(resultado);    	
    	return respuesta;
    }
    
    @PUT
    @Path("/{id}/modcorreo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseRs modificarCorreo(@PathParam(value="id") Long idPersona, Vinculacion vinculacion) {
    	ResponseRs respuesta = new ResponseRs();
    	Boolean resultado = usuarioService.modificarCorreo(idPersona, vinculacion.getSolicitanteNuevo(), vinculacion.getUsuario());
    	respuesta.setData(resultado);    	
    	return respuesta;
    }
}
