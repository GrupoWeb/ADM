package gt.gob.rgm.adm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.Guarantee;
import gt.gob.rgm.adm.domain.Transaction;
import gt.gob.rgm.adm.model.RugGarantias;
import gt.gob.rgm.adm.model.RugRelTramPartes;
import gt.gob.rgm.adm.model.RugRelTramPartesPK;
import gt.gob.rgm.adm.model.RugContrato;
import gt.gob.rgm.adm.dao.RugContratoRepository;
import gt.gob.rgm.adm.model.Tramites;

public class TransactionsServiceImp {

    @Inject
    RugGarantiasService rugGarantiasService;

    @Inject
    RugRelTramPartesService partesService;
    
    @Inject
    RugContratoRepository contratoDAO;

    public Transaction fromTramite(Tramites tramite) {
        
        Transaction transaction = new Transaction();
        System.out.println("dentro garantia: "+ tramite.getRelGarantia().get(0).getGarantia());
        RugGarantias garantia = tramite.getRelGarantia().get(0).getGarantia();
        System.out.println("data garantia: " + garantia.getIdUltimoTramite());

        
        
        
        if (garantia != null) {
            
            
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            
            RugContrato contrato = contratoDAO.findByIdTemp(tramite.getTramiteIncomp().getIdTramiteTemp());
            guarantee.setTipoContrato(contrato.getObservaciones());
            guarantee.setOtrosTerminos(contrato.getOtrosTerminosContrato());


            guarantee.setClasifContrato(tramite.getContrato().get(0).getClasifContrato());
            guarantee.setIdContrato(tramite.getContrato().get(0).getIdContrato());
          
            if (garantia.getFechaFinGar() != null) {
                guarantee.setFechaFinGar(formatter.format(garantia.getFechaFinGar()));
            }
            if (garantia.getFechaInscr() != null) {
                guarantee.setFechaInscr(formatter.format(garantia.getFechaInscr()));
            }
            if (garantia.getFechaReg() != null) {
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
            if (garantia.getUsuario() != null) {
                ExternalUser externalUser = new ExternalUser();
                externalUser.setDocId(garantia.getUsuario().getPersona().getCurpDoc());
                externalUser.setNit(garantia.getUsuario().getPersona().getRfc());
                externalUser.setName(garantia.getUsuario().getPersonaFisica().getNombrePersona());
                externalUser.setPersonaId(garantia.getUsuario().getIdPersona());
                externalUser.setEmail(garantia.getUsuario().getCveUsuario());
                externalUser.setRegistered(formatter.format(garantia.getUsuario().getFhRegistro().getTime()));
                externalUser.setStatus(garantia.getUsuario().getSitUsuario());

                if (garantia.getUsuario().getPersona().getCodigoRegistro() != null) {
                    externalUser.setRegistryCode(String.valueOf(garantia.getUsuario().getPersona().getCodigoRegistro()) + (garantia.getUsuario().getPersona().getPerJuridica().equals("PF") ? "I" : "J"));
                }
                guarantee.setSolicitante(externalUser);
            }

            
            
            RugRelTramPartes partesFilter = new RugRelTramPartes();
            RugRelTramPartesPK id = new RugRelTramPartesPK();
            id.setIdTramite(garantia.getIdUltimoTramite());
            System.out.println("tramite " + garantia.getIdUltimoTramite());
            partesFilter.setId(id);
            List<RugRelTramPartes> partes = partesService.getPartes(partesFilter, null, null);
            List<ExternalUser> deudores = new ArrayList<>();
            List<ExternalUser> acreedores = new ArrayList<>();


            try {
                for (RugRelTramPartes parte : partes) {
                    System.out.println("ids = " + (parte.getPersonaH()));
                    System.out.println("partes : " + parte.getId().getIdPersona());
//
//                    System.out.println(
//                            " Doc " + (parte.getPersonaH().getCurpDoc() != null ? parte.getPersonaH().getCurpDoc() : "null" ) +
//                            " Nit " + parte.getPersonaH().getRfc() +
//                            " Name " +   (parte.getPerJuridica().equals("PF") ? parte.getPersonaH().getNombrePersona() : parte.getPersonaH().getRazonSocial()) +
//                            " Persona " + parte.getPersonaH().getId().getIdPersona()  +
//                            " Email " +   parte.getPersonaH().getEMail()   +
//                            " Register " +         formatter.format(parte.getFechaReg().getTime()) +
//                            " Person Type " + parte.getPersonaH().getPerJuridica() +
//                            " Nationality " + parte.getPersonaH().getIdNacionalidad() +
//                            " Address " +  (parte.getPersona().getDomicilio() != null ? parte.getPersona().getDomicilio().getUbicaDomicilio1() : "")
//                    );
                    ExternalUser userPart = new ExternalUser();
                    userPart.setDocId(parte.getPersonaH().getCurpDoc() != null ? parte.getPersonaH().getCurpDoc() : "" );
                    userPart.setNit(parte.getPersonaH().getRfc() != null ? parte.getPersonaH().getRfc(): "");
                    userPart.setName(parte.getPerJuridica().equals("PF") ? parte.getPersonaH().getNombrePersona() : parte.getPersonaH().getRazonSocial());
                    userPart.setPersonaId(parte.getPersonaH().getId().getIdPersona());
                    userPart.setEmail(parte.getPersonaH().getEMail() != null ? parte.getPersonaH().getEMail() : "");
                    userPart.setRegistered(formatter.format(parte.getFechaReg().getTime()));
                    userPart.setPersonType(parte.getPersonaH().getPerJuridica() != null ? parte.getPersonaH().getPerJuridica() : "");
                    userPart.setNationality(parte.getPersonaH().getIdNacionalidad());
                    userPart.setAddress(parte.getPersona().getDomicilio() != null ? parte.getPersona().getDomicilio().getUbicaDomicilio1() : "");
                    if (parte.getPerJuridica().equals("PM") && (parte.getId().getIdParte() == 2 || parte.getId().getIdParte() == 3)) {
                        userPart.setLegalInscription(parte.getPersonaMoral().getNumInscrita());
                        userPart.setRepresentativeInfo(parte.getPersonaMoral().getUbicada());
                    }

                    if (parte.getId().getIdParte() == 2) {
                        deudores.add(userPart);
                    } else if (parte.getId().getIdParte() == 3) {
                        acreedores.add(userPart);
                    }
                }
                // buscar deudores
                guarantee.setDeudores(deudores);
                // buscar acreedores
                guarantee.setAcreedores(acreedores);

                transaction.setGuarantee(guarantee);
            }
//            catch(NullPointerException e){
//                System.out.println("Error en consulta" + e);
//            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }

        }
        return transaction;
    }
}
