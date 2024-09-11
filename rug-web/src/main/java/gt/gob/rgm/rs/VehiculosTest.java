package gt.gob.rgm.rs;


import gt.gob.rgm.model.BitacoraOperaciones;
import gt.gob.rgm.service.UsuariosService;
import gt.gob.rgm.util.HashUtils;
import mx.gob.se.rug.busqueda.dao.BusquedaDAO;
import mx.gob.se.rug.busqueda.to.BusquedaTO;
import gt.gob.rgm.domain.Usuario;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import sun.misc.BASE64Decoder;

import org.springframework.stereotype.Component;


@Component
@Path("/vehiculos")
public class VehiculosTest {

    private UsuariosService usuariosService;

    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findVehiculo(
        @QueryParam(value="uso")  String uso, 
        @QueryParam(value="placa")  String placa,
        @QueryParam(value="vin")  String vin,
        @HeaderParam("authorization")  String authString
    ) throws URISyntaxException{

        String resultado = "";
        String parametro = "";
        String vinString = "";
        String placaString = "";
        
        boolean check = false;
        boolean returnVin = false;
        boolean returnPlaca = false;

        System.out.println("test");
        /**
         * creación de Bitacora
         * se almacena los datos a la bitacora de busqueda
         */

        BitacoraOperaciones bitacora = new BitacoraOperaciones();
         bitacora.setAgencia(0L);
         bitacora.setOperacion("getVehiculoSAT");
         bitacora.setFecha(Timestamp.valueOf(LocalDateTime.now()));

         /********************************************************** */


         /**
          * Verificación de existencia de usuario
          */

        Usuario user = isUserAuthenticated(authString, bitacora);

        try {
            if(vin != null && uso == null && placa == null){
                if (vin != null && !vin.equalsIgnoreCase("")) {
                    parametro = vin;
                    
                }
            }else if(vin == null && uso != null && placa == null ){
                resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
                bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado );			
                usuariosService.addBitacora(bitacora);
                return resultado;
            }else if(vin == null && uso == null && placa != null){
                resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
                bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
                usuariosService.addBitacora(bitacora);
                return resultado;
            }
            else if(vin != null && uso != null && placa != null){
                vinString = vin;
                placaString = uso + "-" + placa;
                check = true;
            }
            else{
                if(uso != null && !uso.equalsIgnoreCase("") && placa != null && !placa.equalsIgnoreCase("")){
                    parametro = uso + "-" + placa;
                }
                else{
                    resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
                    bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado );			
                    usuariosService.addBitacora(bitacora);
                    return resultado;
                }
            }
        } catch (Exception e) {
            resultado = "{\"estado\": \"503\",\"descripcion\":\"NO SE PUEDE REALIZAR LA CONSULTA, VERIFIQUE EL SERVICIO\"}";
            bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
            usuariosService.addBitacora(bitacora);
            return resultado;
        }

        if (user != null) {
            BusquedaDAO busquedaDAO = new BusquedaDAO();

            try {
                // System.out.println(busquedaGeneral.get(0).getIdGarantia());
                
                if(check == true){
                    List<BusquedaTO> busquedaVin = busquedaDAO.busquedaVehiculo(vinString);
                    List<BusquedaTO> busquedaPlaca = busquedaDAO.busquedaVehiculo(placaString);
                    if(busquedaVin !=  null && !busquedaVin.isEmpty()){
                        if(busquedaVin.get(0).getIdGarantia() ==  null || busquedaVin.get(0).getIdGarantia().equalsIgnoreCase("")){
                            returnVin = false;
                        }else{
                            returnVin = true;
                        }
                    }else{
                        returnVin = false;
                    }
                    if(busquedaPlaca !=  null && !busquedaPlaca.isEmpty()){
                        if(busquedaPlaca.get(0).getIdGarantia() ==  null || busquedaPlaca.get(0).getIdGarantia().equalsIgnoreCase("")){
                            returnPlaca = false;
                        }else{
                            returnPlaca = true;
                        }
                    }else{
                        returnPlaca = false;
                    }

                    System.out.println("Vin = " + vinString);
                    System.out.println("Placa = " + placaString);
                    if(returnVin == false && returnPlaca == false){
                        System.out.println("vin String: " + returnVin + " placa String: " + returnPlaca);
                        resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEHICULO CON VIN NO. " + vinString + " Y PLACA NO. " + placaString +"  NO ESTA ASOCIADO A UNA GARANTIA\"}";
                        bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado + " Parametros: Vin " + vinString + " Placa " + placaString);			
                        usuariosService.addBitacora(bitacora);
                        return resultado;
                    }else{
                        System.out.println("vin String: " + returnVin + " placa String: " + returnPlaca);
                        resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEH\u00CDCULO CON VIN NO. " + vinString + " Y PLACA NO. " + placaString +" EST\u00C1 ASOCIADO A UNA GARANT\u00CDA MOBILIARIA.\\r\\n PARA M\u00C1S INFORMACI\u00D3N PUEDE VISITAR https://www.rgm.gob.gt/noticias/rgm/sat\"}";
                        bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado + " Parametros: Vin " + vinString + " Placa " + placaString);			
                        usuariosService.addBitacora(bitacora);
                        return resultado;
                    }
                }else{
                    
                   
                    
                    List<BusquedaTO> busquedaGeneral = busquedaDAO.busquedaVehiculo(parametro);
                    
                    System.out.println("busquedaGeneral = " + busquedaGeneral);
                    if(busquedaGeneral !=  null && !busquedaGeneral.isEmpty()){
                        if(busquedaGeneral.get(0).getIdGarantia() ==  null || busquedaGeneral.get(0).getIdGarantia().equalsIgnoreCase("")){
                           resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEHICULO CON REGISTRO NO. " + parametro + " NO ESTA ASOCIADO A UNA GARANTIA\"}";
                           bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado + " Parametros: " + parametro);			
                           usuariosService.addBitacora(bitacora);
                           return resultado;
                       }else{
                           resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEH\u00CDCULO CON REGISTRO NO. " + parametro + " EST\u00C1 ASOCIADO A UNA GARANT\u00CDA MOBILIARIA.\\r\\n PARA M\u00C1S INFORMACI\u00D3N PUEDE VISITAR https://www.rgm.gob.gt/noticias/rgm/sat\"}";
                           bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado + " Parametros: " + parametro);			
                           usuariosService.addBitacora(bitacora);
                           return resultado;
                       }
                   }else{
                       resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEHICULO CON REGISTRO NO. " + parametro + " NO ESTA ASOCIADO A UNA GARANTIA\"}";
                       bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado + " Parametros: " + parametro);			
                       usuariosService.addBitacora(bitacora);
                       return resultado;
                    }
                }


            } catch (Exception e) {
               resultado = "{\"estado\": \"503\",\"descripcion\":\"NO SE PUEDE REALIZAR LA CONSULTA, VERIFIQUE EL SERVICIO\"}";
               bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado + " Parametros: " + parametro) ;			
               usuariosService.addBitacora(bitacora);
               return resultado;
            }

        }else{
            resultado = "{\"estado\": \"401\",\"descripcion\":\"USUARIO NO AUTORIZADO\"}";
            bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
            usuariosService.addBitacora(bitacora);
            return resultado;
        }


    }

    /**
     * 
     * @param authString
     * @param bitacora
     * @return
     */

    private Usuario isUserAuthenticated(String authString, BitacoraOperaciones bitacora){

        usuariosService = (UsuariosService) SpringApplicationContext.getBean("usuariosBusinessService");

        String decodedAuth = "";
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        byte[] bytes = null;

        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        decodedAuth = new String(bytes);

        String[] values = decodedAuth.split(":");
        bitacora.setDetalle(bitacora.getDetalle() + "|" + values[0]);

        Usuario user = usuariosService.getUsuarioExterno(values[0]);

        if (user != null && user.getPassword().equalsIgnoreCase(HashUtils.hash(values[1], "SHA-1"))) {
            return user;
        }

        return null;
    }
}