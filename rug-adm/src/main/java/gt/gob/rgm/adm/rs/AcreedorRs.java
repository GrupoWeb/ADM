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
import gt.gob.rgm.adm.domain.Acreedor;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.VRepListadoAcreedores;
import gt.gob.rgm.adm.model.VRepListadoAcreedoresUsuario;
import gt.gob.rgm.adm.service.AcreedorService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.util.Constants;

@Path("/acreedores")
@RequestScoped
public class AcreedorRs {

    @Inject
    AcreedorService acreedorService;

    @Inject
    RugParametroConfService parametroService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SecuredResource
    public ResponseRs getAcreedores(
            @QueryParam(value = "idAcreedor_b") String idAcreedor_b,
            @QueryParam(value = "textobusqueda") String textobusqueda, @QueryParam(value = "page") Integer page,
            @QueryParam(value = "size") Integer size, @QueryParam(value = "ausuarios") Integer ausuarios) {
        ResponseRs response = new ResponseRs();
        List<Acreedor> acreedor = new ArrayList<>();
        List<VRepListadoAcreedores> acreedores;
        List<VRepListadoAcreedoresUsuario> acreedores2;
        Long acreedoresCount = 0L;
        Acreedor acreedorFilter = new Acreedor();
        acreedorFilter.setTextobusqueda(textobusqueda);
        acreedorFilter.setIdAcreedor_b(idAcreedor_b);
        acreedorFilter.setAusuarios(ausuarios);
        if (ausuarios != null) {
            acreedores2 = acreedorService.listAcreedores_u(acreedorFilter, page, size);
            acreedoresCount = acreedorService.countAcreedores_u(acreedorFilter);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();

            for (VRepListadoAcreedoresUsuario acreedo : acreedores2) {
                Acreedor acreed = new Acreedor();
                System.out.println(acreedo.getNombrePersona());
                acreed.setIdPersona(acreedo.getIdPersona());
                acreed.setRfc(acreedo.getRfc());
                acreed.setNombrePersona(acreedo.getNombrePersona());
                acreed.setPerJuridica(acreedo.getPerJuridica());
                acreed.setIdNacionalidad(acreedo.getIdNacionalidad());
                acreed.setNomNacionalidad(acreedo.getNomNacionalidad());
                acreed.setEmail(acreedo.getEmail());
                acreed.setIdDomicilio(acreedo.getIdDomicilio());
                acreed.setDireccion(acreedo.getDireccion());
                acreed.setcGarantias(acreedo.getcGarantias());
                acreed.setcVigentes(acreedo.getcVigentes());
                acreed.setCnVigentes(acreedo.getCnVigentes());
                acreed.setcCanceladas(acreedo.getcCanceladas());
                acreedor.add(acreed);
            }
            response.setTotal(acreedoresCount);
            response.setData(acreedor);
            return response;
        } else {
            acreedores = acreedorService.listAcreedores(acreedorFilter, page, size);
            acreedoresCount = acreedorService.countAcreedores(acreedorFilter);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();

            for (VRepListadoAcreedores acreedo : acreedores) {
                Acreedor acreed = new Acreedor();
                System.out.println(acreedo.getNombrePersona());
                acreed.setIdPersona(acreedo.getIdPersona());
                acreed.setRfc(acreedo.getRfc());
                acreed.setNombrePersona(acreedo.getNombrePersona());
                acreed.setPerJuridica(acreedo.getPerJuridica());
                acreed.setIdNacionalidad(acreedo.getIdNacionalidad());
                acreed.setNomNacionalidad(acreedo.getNomNacionalidad());
                acreed.setEmail(acreedo.getEmail());
                acreed.setIdDomicilio(acreedo.getIdDomicilio());
                acreed.setDireccion(acreedo.getDireccion());
                acreed.setcGarantias(acreedo.getcGarantias());
                acreed.setcVigentes(acreedo.getcVigentes());
                acreed.setCnVigentes(acreedo.getCnVigentes());
                acreed.setcCanceladas(acreedo.getcCanceladas());
                acreedor.add(acreed);
            }
            response.setTotal(acreedoresCount);
            response.setData(acreedor);
            return response;
        }

        /* }else{
            acreedores = acreedorService.listAcreedores_u(acreedorFilter, page, size);
            acreedoresCount = acreedorService.countAcreedores_u(acreedorFilter);
                }*/
 /*  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String hostUrl = parametroService.getParam(Constants.HOST_URL_ADM).getValorParametro();

        for (VRepListadoAcreedores acreedo : acreedores) {
            Acreedor acreed = new Acreedor();
            System.out.println(acreedo.getNombrePersona());
            acreed.setIdPersona(acreedo.getIdPersona());
            acreed.setRfc(acreedo.getRfc());
            acreed.setNombrePersona(acreedo.getNombrePersona());
            acreed.setPerJuridica(acreedo.getPerJuridica());
            acreed.setIdNacionalidad(acreedo.getIdNacionalidad());
            acreed.setNomNacionalidad(acreedo.getNomNacionalidad());
            acreed.setEmail(acreedo.getEmail());
            acreed.setIdDomicilio(acreedo.getIdDomicilio());
            acreed.setDireccion(acreedo.getDireccion());
            acreed.setcGarantias(acreedo.getcGarantias());
            acreed.setcVigentes(acreedo.getcVigentes());
            acreed.setCnVigentes(acreedo.getCnVigentes());
            acreed.setcCanceladas(acreedo.getcCanceladas());
            acreedor.add(acreed);
        }
        response.setTotal(acreedoresCount);
        response.setData(acreedor);*/
        //return response;
    }

}
