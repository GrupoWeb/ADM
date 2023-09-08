package gt.gob.rgm.adm.rs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.dao.RugCatTextoFormRepository;
import gt.gob.rgm.adm.domain.Bienes;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.GarantiaStats;
import gt.gob.rgm.adm.domain.Guarantee;
import gt.gob.rgm.adm.domain.QueryRegistry;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.domain.SpecialGood;
import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.domain.TransactionPart;
import gt.gob.rgm.adm.domain.TransactionPreview;
import gt.gob.rgm.adm.domain.Vinculacion;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugCertificaciones;
import gt.gob.rgm.adm.model.RugGarantias;
import gt.gob.rgm.adm.model.RugGarantiasBienesH;
import gt.gob.rgm.adm.model.RugRelTramGaran;
import gt.gob.rgm.adm.model.RugRelTramPartes;
import gt.gob.rgm.adm.model.RugRelTramPartesPK;
import gt.gob.rgm.adm.model.Tramites;
import gt.gob.rgm.adm.model.VDetalleGarantia;
import gt.gob.rgm.adm.model.VGarantiaParte;
import gt.gob.rgm.adm.model.VGarantiaUtram;
import gt.gob.rgm.adm.service.BitacoraOperacionesService;
import gt.gob.rgm.adm.service.GarantiasServiceImp;
import gt.gob.rgm.adm.service.HomologadoServiceImpl;
import gt.gob.rgm.adm.service.RugBoletaPdfService;
import gt.gob.rgm.adm.service.RugCertificacionesService;
import gt.gob.rgm.adm.service.RugConsultaRegistroServiceImpl;
import gt.gob.rgm.adm.service.RugGarantiasBienesHService;
import gt.gob.rgm.adm.service.RugGarantiasService;
import gt.gob.rgm.adm.service.RugRelTramGaranService;
import gt.gob.rgm.adm.service.RugRelTramPartesService;
import gt.gob.rgm.adm.service.TransactionsServiceImp;
import gt.gob.rgm.adm.service.VDetalleGarantiaService;
import gt.gob.rgm.adm.service.VGarantiaParteService;
import gt.gob.rgm.adm.service.VGarantiaUtramService;
import gt.gob.rgm.adm.util.Constants;


@Path("/garantias")
@RequestScoped
public class GarantiasRs {

	@Inject
	RugGarantiasService rugGarantiasService;
	
	@Inject
	RugRelTramPartesService partesService;
	
	@Inject
	VGarantiaUtramService garantiaUtramService;
	
	@Inject
	VDetalleGarantiaService detalleGarantiaService;
	
	@Inject
	VGarantiaParteService garantiaParteService;
	
	@Inject
	RugGarantiasBienesHService garantiasBienesService;
	
	@Inject
	RugRelTramGaranService relTramGaranService;
	
	@Inject
	RugCertificacionesService certificacionService;
	
	@Inject
	RugConsultaRegistroServiceImpl consultaService;
	
	@Inject
	HomologadoServiceImpl homologadoService;
	
	@Inject
	RugCatTextoFormRepository rugCatTextoFormDao;
	
	@Inject
	GarantiasServiceImp garantiasService;
	
	@Inject
	RugBoletaPdfService boletaPdfService;
	
	@Inject
	TransactionsServiceImp transactionsService;
	
	@Inject
	BitacoraOperacionesService bitacoraService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs getGarantias(@QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size,
                @QueryParam(value="nombre") String nombre, @QueryParam(value="numero") Long numero, 
                @QueryParam(value="fechaInicio") String fechaInicio, @QueryParam(value="fechaFin") String fechaFin,@QueryParam(value="Idpersona") Long Idpersona) {
		ResponseRs response = new ResponseRs();
		List<Transaction> transactions = new ArrayList<>();
    	List<Tramites> tramites;
    	Long transactionsCount = 0L;
    	
        
        int iteracionD = 0;
    	Transaction transactFilter = new Transaction();
    	ExternalUser externalUserFilter = null;
    	if(nombre != null) {
    		externalUserFilter = new ExternalUser();
    		externalUserFilter.setName(nombre);
    	}
        
        if(Idpersona != null) {
            System.out.println("VALOR DE IDPERSONA DESPUES: "+Idpersona);
    		externalUserFilter = new ExternalUser();
    		externalUserFilter.setPersonaId(Idpersona);
    	}
        
    	transactFilter.setSolicitante(externalUserFilter);
    	Guarantee guaranteeFilter = null;
    	if(numero != null) {
    		guaranteeFilter = new Guarantee();
    		guaranteeFilter.setIdGarantia(numero);
    	}
    	transactFilter.setGuarantee(guaranteeFilter);
    	tramites = rugGarantiasService.listGarantias(transactFilter, page, size, fechaInicio, fechaFin);
    	transactionsCount = rugGarantiasService.countGarantias(transactFilter, fechaInicio, fechaFin);
    	
            System.out.println("Tramites " + tramites.size());
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	for(Tramites tramite : tramites) {
                iteracionD++;
                
    		Transaction transaction = new Transaction();
    		transaction.setIdTramite(tramite.getIdTramite());
    		transaction.setbCargaMasiva(tramite.getBCargaMasiva());
    		transaction.setFechaCreacion(formatter.format(tramite.getFechaCreacion()));
    		transaction.setIdStatusTramite(tramite.getIdStatusTram());
    		if(tramite.getTramiteIncomp() != null) {
        		transaction.setIdTramiteTemp(tramite.getTramiteIncomp().getIdTramiteTemp());
    		}
    		transaction.setStatusReg(tramite.getStatusReg());
    		transaction.setIdTipoTramite(tramite.getTipoTramite().getIdTipoTramite());
    		transaction.setDescripcion(tramite.getTipoTramite().getDescripcion());
    		transaction.setPrecio(tramite.getTipoTramite().getPrecio());
    		
    		RugGarantias garantia = null;
    		if(tramite.getTipoTramite().getIdTipoTramite() == 5) {
    			/*RugCertificaciones certificacion = certificacionService.findByTramite(tramite.getIdTramite());
    			garantia = certificacion.getGarantia();*/
                        System.out.println("iteracion " + iteracionD );
                        System.out.println("valor " + tramite.getIdTramite());
                        System.out.println("null " + tramite.getCertificacion().isEmpty());
                        if(!tramite.getCertificacion().isEmpty()){
                            garantia = tramite.getCertificacion().get(0).getGarantia();
                        }
    			
    		} else {
        		/*RugRelTramGaran relTramGaran = relTramGaranService.findByTramite(tramite.getIdTramite());
        		garantia = relTramGaran.getGarantia();*/
    			if(tramite.getRelGarantia() != null && !tramite.getRelGarantia().isEmpty()) {
        			garantia = tramite.getRelGarantia().get(0).getGarantia();
    			}
    		}
    		if(garantia != null) {
        		Guarantee guarantee = new Guarantee();
        		guarantee.setIdGarantia(garantia.getIdGarantia());
        		guarantee.setDescGarantia(garantia.getDescGarantia());
        		guarantee.setEsPrioritaria(garantia.getEsPrioritaria());
        		if(garantia.getFechaFinGar() != null) {
            		guarantee.setFechaFinGar(formatter.format(garantia.getFechaFinGar()));
        		}
        		if(garantia.getFechaInscr() != null) {
            		guarantee.setFechaInscr(formatter.format(garantia.getFechaInscr()));
        		}
        		if(garantia.getFechaReg() != null) {
            		guarantee.setFechaReg(formatter.format(garantia.getFechaReg()));
        		}
        		guarantee.setFolioMercantil(garantia.getFolioMercantil());
        		guarantee.setGarantiaStatus(garantia.getGarantiaStatus());
        		guarantee.setIdTipoGarantia(garantia.getIdTipoGarantia());
        		guarantee.setInstrumentoPublico(garantia.getInstrumentoPublico());
        		guarantee.setMesesGarantia(garantia.getMesesGarantia());
        		guarantee.setNumGarantia(garantia.getNumGarantia());
        		guarantee.setOtrosTerminosGarantia(garantia.getOtrosTerminosGarantia());
        		guarantee.setStatusReg(garantia.getStatusReg());
        		guarantee.setTiposBienesMuebles(garantia.getTiposBienesMuebles());
        		guarantee.setTxtRegistros(garantia.getTxtRegistros());
        		guarantee.setValorBienes(garantia.getValorBienes());
        		guarantee.setVigencia(garantia.getVigencia());
                        
                        String original = rugGarantiasService.original(garantia.getIdGarantia());
                        guarantee.setOriginal(original);
                        
        		transaction.setGuarantee(guarantee);
    		}
                
//                System.out.println("formatter = " + tramite.getListBienes().get(0).getDescripcionBien());
                  List<RugGarantiasBienesH> bienes = garantiasBienesService.findByTramite(tramite.getIdTramite());
                  for(RugGarantiasBienesH bien : bienes) {
                      Bienes listaB = new Bienes();
                      listaB.setIdentificador(bien.getIdentificador());
                      listaB.setDescripcion(bien.getDescripcionBien());
                      
                      transaction.setBienLista(listaB);
			
		}
    		// persona
    		if(tramite.getUsuario() != null) {
        		ExternalUser externalUser = new ExternalUser();
        		externalUser.setDocId(tramite.getUsuario().getPersona().getCurpDoc());
        		externalUser.setNit(tramite.getUsuario().getPersona().getRfc());
        		externalUser.setName(tramite.getUsuario().getPersonaFisica().getNombrePersona());
        		externalUser.setPersonaId(tramite.getUsuario().getIdPersona());
        		externalUser.setEmail(tramite.getUsuario().getCveUsuario());
        		externalUser.setRegistered(formatter.format(tramite.getUsuario().getFhRegistro().getTime()));
        		externalUser.setStatus(tramite.getUsuario().getSitUsuario());
        		
        		if(tramite.getUsuario().getPersona().getCodigoRegistro() != null) {
        			externalUser.setRegistryCode(String.valueOf(tramite.getUsuario().getPersona().getCodigoRegistro()) + (tramite.getUsuario().getPersona().getPerJuridica().equals("PF") ? "I" : "J"));
        		}
        		transaction.setSolicitante(externalUser);
    		}
    		
    		RugRelTramPartes partesFilter = new RugRelTramPartes();
    		RugRelTramPartesPK id = new RugRelTramPartesPK();
    		id.setIdTramite(tramite.getIdTramite());
    		partesFilter.setId(id);
    		List<RugRelTramPartes> partes = partesService.getPartes(partesFilter, null, null);
    		List<ExternalUser> deudores = new ArrayList<>();
    		List<ExternalUser> acreedores = new ArrayList<>();
    		for(RugRelTramPartes parte : partes) {
    			if(parte.getPersonaH() != null) {
        			ExternalUser userPart = new ExternalUser();
        			userPart.setDocId(parte.getPersonaH().getCurpDoc());
        			userPart.setNit(parte.getPersonaH().getRfc());
        			userPart.setName(parte.getPerJuridica().equals("PF") ? parte.getPersonaH().getNombrePersona() : parte.getPersonaH().getRazonSocial());
        			userPart.setPersonaId(parte.getPersonaH().getId().getIdPersona());
        			userPart.setEmail(parte.getPersonaH().getEMail());
        			userPart.setRegistered(formatter.format(parte.getFechaReg().getTime()));
        			
        			if(parte.getId().getIdParte() == 2) {
        				deudores.add(userPart);
        			} else if(parte.getId().getIdParte() == 3) {
        				acreedores.add(userPart);
        			}
    			}
    		}
    		// buscar deudores
    		transaction.setDeudores(deudores);
    		// buscar acreedores
    		transaction.setAcreedores(acreedores);
    		
    		transactions.add(transaction);
    	}
		
    	response.setTotal(transactionsCount);
    	response.setData(transactions);
            
        return response;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Transaction getTransaction(@PathParam(value="id") Long idGarantia) {
		//Transaction transaction = new Transaction();
		System.out.println("Dato ingresado: " + idGarantia);
		RugGarantias garantia = rugGarantiasService.getGarantia(idGarantia);
		System.out.println("Return garantia: " + garantia.getIdUltimoTramite());
		Tramites tramite = garantiasService.findTramiteById(garantia.getIdUltimoTramite());
		return transactionsService.fromTramite(tramite);
		/*if(garantia != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Tramites tramite = garantiasService.findTramiteById(garantia.getIdUltimoTramite());
    		transaction.setIdTramite(tramite.getIdTramite());
    		transaction.setbCargaMasiva(tramite.getBCargaMasiva());
    		transaction.setFechaCreacion(formatter.format(tramite.getFechaCreacion()));
    		transaction.setIdStatusTramite(tramite.getIdStatusTram());
    		transaction.setIdTramiteTemp(tramite.getTramiteIncomp().getIdTramiteTemp());
    		transaction.setStatusReg(tramite.getStatusReg());
    		transaction.setIdTipoTramite(tramite.getTipoTramite().getIdTipoTramite());
    		transaction.setDescripcion(tramite.getTipoTramite().getDescripcion());
    		transaction.setPrecio(tramite.getTipoTramite().getPrecio());
    		
			Guarantee guarantee = new Guarantee();
			guarantee.setIdGarantia(garantia.getIdGarantia());
			guarantee.setDescGarantia(garantia.getDescGarantia());
			guarantee.setEsPrioritaria(garantia.getEsPrioritaria());
			if(garantia.getFechaFinGar() != null) {
	    		guarantee.setFechaFinGar(formatter.format(garantia.getFechaFinGar()));
			}
			if(garantia.getFechaInscr() != null) {
	    		guarantee.setFechaInscr(formatter.format(garantia.getFechaInscr()));
			}
			if(garantia.getFechaReg() != null) {
	    		guarantee.setFechaReg(formatter.format(garantia.getFechaReg()));
			}
			guarantee.setFolioMercantil(garantia.getFolioMercantil());
			guarantee.setGarantiaStatus(garantia.getGarantiaStatus());
			guarantee.setIdTipoGarantia(garantia.getIdTipoGarantia());
			guarantee.setInstrumentoPublico(garantia.getInstrumentoPublico());
			guarantee.setMesesGarantia(garantia.getMesesGarantia());
			guarantee.setNumGarantia(garantia.getNumGarantia());
			guarantee.setOtrosTerminosGarantia(garantia.getOtrosTerminosGarantia());
			guarantee.setStatusReg(garantia.getStatusReg());
			guarantee.setTiposBienesMuebles(garantia.getTiposBienesMuebles());
			guarantee.setTxtRegistros(garantia.getTxtRegistros());
			guarantee.setValorBienes(garantia.getValorBienes());
			guarantee.setVigencia(garantia.getVigencia());
			
			// solicitante
    		if(garantia.getUsuario() != null) {
        		ExternalUser externalUser = new ExternalUser();
        		externalUser.setDocId(garantia.getUsuario().getPersona().getCurpDoc());
        		externalUser.setNit(garantia.getUsuario().getPersona().getRfc());
        		externalUser.setName(garantia.getUsuario().getPersonaFisica().getNombrePersona());
        		externalUser.setPersonaId(garantia.getUsuario().getIdPersona());
        		externalUser.setEmail(garantia.getUsuario().getCveUsuario());
        		externalUser.setRegistered(formatter.format(garantia.getUsuario().getFhRegistro().getTime()));
        		externalUser.setStatus(garantia.getUsuario().getSitUsuario());
        		
        		if(garantia.getUsuario().getPersona().getCodigoRegistro() != null) {
        			externalUser.setRegistryCode(String.valueOf(garantia.getUsuario().getPersona().getCodigoRegistro()) + (garantia.getUsuario().getPersona().getPerJuridica().equals("PF") ? "I" : "J"));
        		}
        		guarantee.setSolicitante(externalUser);
    		}
			
			RugRelTramPartes partesFilter = new RugRelTramPartes();
    		RugRelTramPartesPK id = new RugRelTramPartesPK();
    		id.setIdTramite(garantia.getIdUltimoTramite());
    		partesFilter.setId(id);
    		List<RugRelTramPartes> partes = partesService.getPartes(partesFilter, null, null);
    		List<ExternalUser> deudores = new ArrayList<>();
    		List<ExternalUser> acreedores = new ArrayList<>();
    		for(RugRelTramPartes parte : partes) {
    			ExternalUser userPart = new ExternalUser();
    			userPart.setDocId(parte.getPersonaH().getCurpDoc());
    			userPart.setNit(parte.getPersonaH().getRfc());
    			userPart.setName(parte.getPerJuridica().equals("PF") ? parte.getPersonaH().getNombrePersona() : parte.getPersonaH().getRazonSocial());
    			userPart.setPersonaId(parte.getPersonaH().getId().getIdPersona());
    			userPart.setEmail(parte.getPersonaH().getEMail());
    			userPart.setRegistered(formatter.format(parte.getFechaReg().getTime()));
    			userPart.setPersonType(parte.getPersonaH().getPerJuridica());
    			userPart.setNationality(parte.getPersonaH().getIdNacionalidad());
    			userPart.setAddress(parte.getPersona().getDomicilio() != null ? parte.getPersona().getDomicilio().getUbicaDomicilio1() : "");
    			if(parte.getPerJuridica().equals("PM")) {
        			userPart.setLegalInscription(parte.getPersonaMoral().getNumInscrita());
        			userPart.setRepresentativeInfo(parte.getPersonaMoral().getUbicada());
    			}
    			
    			if(parte.getId().getIdParte() == 2) {
    				deudores.add(userPart);
    			} else if(parte.getId().getIdParte() == 3) {
    				acreedores.add(userPart);
    			}
    		}
    		// buscar deudores
    		guarantee.setDeudores(deudores);
    		// buscar acreedores
    		guarantee.setAcreedores(acreedores);
			
    		transaction.setGuarantee(guarantee);
		}
		return transaction;*/
	}
	
	@GET
	@Path("/{id}/partes")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<RugRelTramPartes> getPartes(@PathParam(value="id") Long idTramite, @QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size) {
    	RugRelTramPartes partesFilter = new RugRelTramPartes();
    	if(idTramite != null) {
    		RugRelTramPartesPK id = new RugRelTramPartesPK();
    		id.setIdTramite(idTramite);
    		partesFilter.setId(id);
    	}
    	
		return partesService.getPartes(partesFilter, page, size);
	}
	
	@GET
	@Path("/reporte")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<GenericCount> getCountGarantias(@QueryParam(value = "fechaInicio") String fechaInicio,
            @QueryParam(value = "fechaFin") String fechaFin,
            @QueryParam(value = "im") Boolean incluirMigracion) {
		List<GenericCount> garantias = rugGarantiasService.countGarantias(fechaInicio, fechaFin, incluirMigracion);
		
		return garantias;
	}
	
	@GET
	@Path("/stats-tp")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<GenericCount> getCountGarantiasByTipoPersona(@QueryParam(value = "fechaInicio") String fechaInicio,
            @QueryParam(value = "fechaFin") String fechaFin,
            @QueryParam(value = "im") Boolean incluirMigracion) {
		List<GenericCount> garantias = rugGarantiasService.countGarantiasByTipoPersona(fechaInicio, fechaFin, incluirMigracion);
		
		return garantias;
	}
	
	@GET
	@Path("/reporte-tp")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<GenericCount> getTipoPersona(@QueryParam(value = "fechaInicio") String fechaInicio,
            @QueryParam(value = "fechaFin") String fechaFin,
            @QueryParam(value = "im") Boolean incluirMigracion) {
		List<GenericCount> personas = rugGarantiasService.listTipoPersona(fechaInicio, fechaFin, incluirMigracion);
		
		return personas;
	}
	
	@GET
	@Path("/stats")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<GarantiaStats> getStats(@QueryParam(value = "fechaInicio") String fechaInicio,
            @QueryParam(value = "fechaFin") String fechaFin,
            @QueryParam(value = "cf") String cfecha,
            @QueryParam(value = "fields") String fields,
            @QueryParam(value = "tt") String tipoTramite,
            @QueryParam(value = "dis") Integer diaInicioSemana,
            @QueryParam(value = "im") Boolean incluirMigracion) {
		return rugGarantiasService.statsGarantias(fechaInicio, fechaFin, cfecha, fields, diaInicioSemana, incluirMigracion);
	}
	
	@GET
	@Path("/{id}/detalle")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public TransactionPreview getDetalle(@PathParam(value="id") Long idTramite, @QueryParam(value="garantia") Long idGarantia) {
		// obtener V_GARANTIA_UTRAM
		VGarantiaUtram garantia = garantiaUtramService.findByTramite(idTramite, idGarantia);
		// obtener V_DETALLE_GARANTIA
		VDetalleGarantia detalle = detalleGarantiaService.findByTramite(idTramite, idGarantia);
		// obtener V_GARANTIA_PARTE
		List<VGarantiaParte> partes = garantiaParteService.findByTramite(idTramite, idGarantia);
		// obtener RUG_GARANTIAS_BIENES_H
		List<RugGarantiasBienesH> bienes = garantiasBienesService.findByTramite(idTramite);
		
		List<String> textos = rugCatTextoFormDao.findByIdTipoGarantia(detalle.getIdTipoGarantia().longValue());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		TransactionPreview transaction = new TransactionPreview();
		transaction.setIdTramite(idTramite);
		transaction.setIdGarantia(idGarantia);
		transaction.setFechaInscripcion(sdf.format(garantia.getFechaInscripcion()));
		transaction.setFechaUltAsiento(sdf.format(garantia.getFechaUltimo()));
		transaction.setFechaAsiento(sdf.format(garantia.getFechaCreacion()));
		transaction.setTipoAsiento(garantia.getDescripcion());
		transaction.setVigencia(detalle.getVigencia());
		transaction.setDescbienes(detalle.getDescBienesMuebles());
		transaction.setaBoolean(detalle.getCambiosBienesMonto() != null ? detalle.getCambiosBienesMonto().equals("V") : false);
		transaction.setaBooleanNoGaraOt(detalle.getNoGarantiaPreviaOt() != null ? detalle.getNoGarantiaPreviaOt().equals("V") : false);
		transaction.setaPrioridad(detalle.getEsPrioritaria().equals("V"));
		transaction.setaRegistro(detalle.getOtrosRegistros() != null ? detalle.getOtrosRegistros().equals("V") : false);
		transaction.setInstrumento(detalle.getInstrumentoPublico());
		transaction.setOtroscontrato(detalle.getOtrosTerminosContrato());
		transaction.setOtrosgarantia(detalle.getOtrosTerminosGarantia());
		transaction.setOtrosterminos(detalle.getObservaciones());
		transaction.setDeudorTOs(new ArrayList<>());
		transaction.setAcreedorTOs(new ArrayList<>());
		transaction.setOtorganteTOs(new ArrayList<>());
		transaction.setBienesEspTOs(new ArrayList<>());;
		transaction.setTextos(textos);
		
		for(VGarantiaParte parte : partes) {
			TransactionPart part = new TransactionPart();
			part.setNombre(parte.getNombre());
			part.setPerjuridica(parte.getPerJuridica());
			part.setRfc(parte.getRfc());
			part.setCurp(parte.getCurp());
			if(parte.getIdParte().longValue() == 2) {
				transaction.getDeudorTOs().add(part);
			} else if(parte.getIdParte().longValue() == 3) {
				transaction.getAcreedorTOs().add(part);
			} else if(parte.getIdParte().longValue() == 1) {
				transaction.getOtorganteTOs().add(part);
			}
		}
		
		for(RugGarantiasBienesH bien : bienes) {
			SpecialGood good = new SpecialGood();
			good.setTipoBien(bien.getTipoBienEspecial().intValue());
			good.setTipoIdentificador(bien.getTipoIdentificador().intValue());
			good.setIdentificador(bien.getIdentificador());
			good.setDescripcion(bien.getDescripcionBien());
			transaction.getBienesEspTOs().add(good);
		}
		
		return transaction;
	}

	@PUT
    @Path("/{id}/vincular")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@SecuredResource
    public ResponseRs vincularGarantia(@PathParam(value="id") Long idGarantia, Vinculacion vinculacion) {
		ResponseRs respuesta = new ResponseRs();
		Boolean resultado = rugGarantiasService.vincularGarantia(vinculacion.getSolicitante(), idGarantia, vinculacion.getCausa(), vinculacion.getUsuario());
		respuesta.setData(resultado);
    	return respuesta;
    }
	
	@GET
	@Path("/consultas")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs getConsultas(@QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size, @QueryParam(value="nombre") String nombre, @QueryParam(value="fechaInicio") String fechaInicio, @QueryParam(value="fechaFin") String fechaFin) {
		ResponseRs response = new ResponseRs();
		List<QueryRegistry> queries = consultaService.listConsultas(nombre, fechaInicio, fechaFin, page, size);
		Long consultasCount = consultaService.countConsultas(nombre, fechaInicio, fechaFin);
		response.setTotal(consultasCount);
    	response.setData(queries);  
		return response;
	}
	
	@GET
	@Path("/vinculaciones")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs getVinculaciones(@QueryParam(value="page") Integer page, @QueryParam(value="size") Integer size, @QueryParam(value="nombre") String nombre, @QueryParam(value="fechaInicio") String fechaInicio, @QueryParam(value="fechaFin") String fechaFin, @QueryParam(value="garantia") String garantia) {
		ResponseRs response = new ResponseRs();
		List<Vinculacion> vinculaciones = homologadoService.listVinculaciones(nombre, fechaInicio, fechaFin, page, size,garantia);
		Long vinculacionesCount = homologadoService.countVinculaciones(nombre, fechaInicio, fechaFin,garantia);
		response.setTotal(vinculacionesCount);
    	response.setData(vinculaciones);
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs crearGarantia(@HeaderParam("Authorization") String authorization, Transaction transaction) {
		ResponseRs response = new ResponseRs();
		// verificar que no exista garantia
		RugGarantias garantiaExistente = garantiasService.findGarantiaById(transaction.getGuarantee().getIdGarantia());
		if(garantiaExistente != null) {
			response.setData(Constants.ERROR_GARANTIA_EXISTENTE);
			return response;
		}
		// verificar que no exista tramite
		Tramites tramiteExistente = garantiasService.findTramiteById(transaction.getIdTramite());
		if(tramiteExistente != null) {
			response.setData(Constants.ERROR_TRAMITE_EXISTENTE);
			return response;
		}
		garantiasService.crear(transaction);
		// agregar a bitacora
		String token = authorization.split(" ")[1];
		bitacoraService.createEntry(token, "Se agrego la garantia " + transaction.getGuarantee().getIdGarantia());
		response.setData(Constants.RESULTADO_EXITOSO);
		return response;
	}
	
	@POST
	@Path("/{id}/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseRs subirBoleta(@FormDataParam("document") InputStream is, @FormDataParam("document") FormDataContentDisposition fileDetails, @PathParam(value="id") Long idTramite) {
		ResponseRs response = new ResponseRs();
		
		try {
			byte[] archivo = IOUtils.toByteArray(is);
			boletaPdfService.saveBoleta(idTramite, archivo);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs actualizarGarantia(@HeaderParam("Authorization") String authorization, Transaction transaction) {
		ResponseRs response = new ResponseRs();
		garantiasService.actualizarDatosGarantia(transaction);
		// agregar a bitacora
		String token = authorization.split(" ")[1];
		bitacoraService.createEntry(token, "Se actualizo el tramite " + transaction.getIdTramite() + ", se cambio [" + transaction.getControlCambios() + "]");
		response.setData(Constants.RESULTADO_EXITOSO);
		return response;
	}
	
	@GET
	@Path("/{id}/tramites")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseRs tramitesPorGarantia(@PathParam(value="id") Long idGarantia) {
		ResponseRs response = new ResponseRs();
		List<RugRelTramGaran> tramGarans = relTramGaranService.findByGarantia(idGarantia);
		List<Transaction> transactions = new ArrayList<>();
		for(RugRelTramGaran tramGaran : tramGarans) {
			transactions.add(transactionsService.fromTramite(tramGaran.getTramite()));
                        
		}
		response.setData(transactions);
		return response;
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public ResponseRs searchByDescription(@QueryParam(value="q") String query) {
		ResponseRs response = new ResponseRs();
		
		List<Guarantee> guarantees = rugGarantiasService.listGarantiasByDescripcionAndNotStatus(query, "CA");
		response.setData(guarantees);
		
		return response;
	}
}
