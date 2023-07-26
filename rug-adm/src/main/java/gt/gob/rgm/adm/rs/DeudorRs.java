package gt.gob.rgm.adm.rs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.Deudo;
import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.VRepListadoDeudorUsuario;
import gt.gob.rgm.adm.model.VRepListadoDeudores;
import gt.gob.rgm.adm.service.DeudorService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.util.Constants;

@Path("/deudores")
@RequestScoped
public class DeudorRs {

    @Inject
    DeudorService deudorService;

    @Inject
    RugParametroConfService parametroService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getDeudores(
            @QueryParam(value = "idDeudor_b") String idDeudor_b, @QueryParam(value = "textobusqueda") String textobusqueda,
            @QueryParam(value = "page") Integer page, @QueryParam(value = "size") Integer size,
            @QueryParam(value = "dusuarios") Integer dusuarios) {
        ResponseRs response = new ResponseRs();
        List<Deudo> deudos = new ArrayList<>();
        List<VRepListadoDeudores> deudores = null;
        List<VRepListadoDeudorUsuario> deudores2;

        Long deudoresCount = 0L;
        Deudo deudorFilter = new Deudo();
        deudorFilter.setTextobusqueda(textobusqueda);
        deudorFilter.setIdDeudor_b(idDeudor_b);
        deudorFilter.setDusuarios(dusuarios);

        if (dusuarios != null) {
            deudores2 = deudorService.listDeudores_u(deudorFilter, page, size);
            deudoresCount = deudorService.countDeudores_u(deudorFilter);
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
        for (VRepListadoDeudorUsuario deudor : deudores2) {
            Deudo deudo = new Deudo();
            deudo.setIdPersona(deudor.getIdPersona());
            deudo.setRfc(deudor.getRfc());
            deudo.setNombrePersona(deudor.getNombrePersona());
            deudo.setPerJuridica(deudor.getPerJuridica());
            deudo.setIdNacionalidad(deudor.getIdNacionalidad());
            deudo.setNomNacionalidad(deudor.getNomNacionalidad());
            deudo.setEmail(deudor.getEmail());
            deudo.setIdDomicilio(deudor.getIdDomicilio());
            deudo.setDireccion(deudor.getDireccion());
            deudo.setcGarantias(deudor.getcGarantias());
            deudo.setcVigentes(deudor.getcVigentes());
            deudo.setCnVigentes(deudor.getCnVigentes());
            deudo.setcCanceladas(deudor.getcCanceladas());
            deudos.add(deudo);
        }
        response.setTotal(deudoresCount);
        response.setData(deudos);
        } else {
            deudores = deudorService.listDeudores(deudorFilter, page, size);
            deudoresCount = deudorService.countDeudores(deudorFilter);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
            for (VRepListadoDeudores deudor : deudores) {
                Deudo deudo = new Deudo();
                deudo.setIdPersona(deudor.getIdPersona());
                deudo.setRfc(deudor.getRfc());
                deudo.setNombrePersona(deudor.getNombrePersona());
                deudo.setPerJuridica(deudor.getPerJuridica());
                deudo.setIdNacionalidad(deudor.getIdNacionalidad());
                deudo.setNomNacionalidad(deudor.getNomNacionalidad());
                deudo.setEmail(deudor.getEmail());
                deudo.setIdDomicilio(deudor.getIdDomicilio());
                deudo.setDireccion(deudor.getDireccion());
                deudo.setcGarantias(deudor.getcGarantias());
                deudo.setcVigentes(deudor.getcVigentes());
                deudo.setCnVigentes(deudor.getCnVigentes());
                deudo.setcCanceladas(deudor.getcCanceladas());
                deudos.add(deudo);
            }
            response.setTotal(deudoresCount);
            response.setData(deudos);
            return response;
        }

        /*   }else{
            deudores = deudorService.listDeudores_u(deudorFilter, page, size);
    	deudoresCount = deudorService.countDeudores_u(deudorFilter);
                }   */
      /*  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();
        for (VRepListadoDeudores deudor : deudores) {
            Deudo deudo = new Deudo();
            deudo.setIdPersona(deudor.getIdPersona());
            deudo.setRfc(deudor.getRfc());
            deudo.setNombrePersona(deudor.getNombrePersona());
            deudo.setPerJuridica(deudor.getPerJuridica());
            deudo.setIdNacionalidad(deudor.getIdNacionalidad());
            deudo.setNomNacionalidad(deudor.getNomNacionalidad());
            deudo.setEmail(deudor.getEmail());
            deudo.setIdDomicilio(deudor.getIdDomicilio());
            deudo.setDireccion(deudor.getDireccion());
            deudo.setcGarantias(deudor.getcGarantias());
            deudo.setcVigentes(deudor.getcVigentes());
            deudo.setCnVigentes(deudor.getCnVigentes());
            deudo.setcCanceladas(deudor.getcCanceladas());
            deudos.add(deudo);
        }
        response.setTotal(deudoresCount);
        response.setData(deudos);*/
        return response;
    }

}
