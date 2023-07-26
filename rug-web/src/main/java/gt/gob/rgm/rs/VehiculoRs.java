package gt.gob.rgm.rs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import gt.gob.rgm.domain.Usuario;
import gt.gob.rgm.model.BitacoraOperaciones;
import gt.gob.rgm.service.UsuariosService;
import gt.gob.rgm.util.HashUtils;
import mx.gob.se.rug.busqueda.dao.BusquedaDAO;
import mx.gob.se.rug.busqueda.to.BusquedaTO;
import sun.misc.BASE64Decoder;
import mx.gob.se.rug.util.MyLogger;
import java.util.logging.Level;

@Component
@Path("/vehiculos_old")
public class VehiculoRs {

	private UsuariosService usuariosService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public String findVehiculo(@QueryParam(value="uso") String uso, 
			@QueryParam(value="placa") String placa,
			@QueryParam(value="vin") String vin,
			@HeaderParam("authorization") String authString) throws URISyntaxException{
		
		String resultado = "";
		String parametro = "";
		
		BitacoraOperaciones bitacora = new BitacoraOperaciones();
		bitacora.setAgencia(0L);
		bitacora.setOperacion("getVehiculoSAT");
		bitacora.setFecha(Timestamp.valueOf(LocalDateTime.now()));				
		
		Usuario user = isUserAuthenticated(authString, bitacora);
                
		/**
                 * verificaci�n de parametros enviados
                 */
                try {
			if(vin!=null && !vin.equalsIgnoreCase("")) parametro = vin;
			else if(!uso.equalsIgnoreCase("") && !placa.equalsIgnoreCase("")) parametro = uso + "-" + placa;
			else {
				resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
				bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
				usuariosService.addBitacora(bitacora);
				return resultado;
			}
		} catch(Exception ex) {
			//ex.printStackTrace();
			resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
			bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
			usuariosService.addBitacora(bitacora);
			return resultado;
		}
                
//                if(vin == null){
//                    if(uso != null && placa != null){
//                        if(!uso.equalsIgnoreCase("") && !placa.equalsIgnoreCase("")){
//                            parametro = uso + "-" + placa;
//                            System.out.println("parametro placa  = " + parametro );
//                        }else{
//                            parametro = "0";
//                            System.out.println("error 2 = ");
//                        }
//                    }else{
//                        parametro = "0";
//                        System.out.println("error 1");
//                    }
//                    //if(!uso.equalsIgnoreCase("") && !placa.equalsIgnoreCase("")) parametro = uso + "-" + placa;
//                    
//                }else{
//                    parametro = vin;
//                    System.out.println("parametro vin = " + parametro );
//                }
                
                
		if(user==null) {
                        //MyLogger.Logger.log(Level.INFO, user);
                        
			resultado = "{\"estado\": \"401\",\"descripcion\":\"USUARIO NO AUTORIZADO\"}";
                        //resultado = "no";
			bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
			usuariosService.addBitacora(bitacora);
			return resultado;
		}
		
//		try {
//			if(vin!=null && !vin.equalsIgnoreCase("")) parametro = vin;
//			else if(!uso.equalsIgnoreCase("") && !placa.equalsIgnoreCase("")) parametro = uso + "-" + placa;
//			else {
//				resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
//				bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
//				usuariosService.addBitacora(bitacora);
//				return resultado;
//			}
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			resultado = "{\"estado\": \"422\",\"descripcion\":\"LA SOLICITUD NO ESTA BIEN FORMADA FALTAN PARAMETROS, FAVOR REVISE\"}";
//			bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
//			usuariosService.addBitacora(bitacora);
//			return resultado;
//		}
		
		BusquedaDAO busquedaDAO =new BusquedaDAO();
		try {
			bitacora.setDetalle(parametro);
			if(parametro.equalsIgnoreCase("0")) {
				resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEHICULO NO ESTA ASOCIADO A UNA GARANTIA\"}";
				bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
				usuariosService.addBitacora(bitacora);
				return resultado;
			} else {
				List<BusquedaTO> busquedaGeneral = busquedaDAO.busquedaVehiculo(parametro);
				if(busquedaGeneral!=null && !busquedaGeneral.isEmpty()) {
					if(busquedaGeneral.get(0).getIdGarantia()==null || busquedaGeneral.get(0).getIdGarantia().equalsIgnoreCase("")) {
						resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEHICULO NO ESTA ASOCIADO A UNA GARANTIA\"}";
						bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
						usuariosService.addBitacora(bitacora);
						return resultado;
					} else {
						resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEH\u00CDCULO EST\u00C1 ASOCIADO A UNA GARANT\u00CDA MOBILIARIA.\\r\\n PARA M\u00C1S INFORMACI\u00D3N PUEDE VISITAR https://www.rgm.gob.gt/noticias/rgm/sat\"}";
						bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
						usuariosService.addBitacora(bitacora);
						return resultado;
					}
				} else {				
					resultado = "{\"estado\": \"200\",\"descripcion\":\"EL VEHICULO NO ESTA ASOCIADO A UNA GARANTIA\"}";
					bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
					usuariosService.addBitacora(bitacora);
					return resultado;	
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			resultado = "{\"estado\": \"503\",\"descripcion\":\"NO SE PUEDE REALIZAR LA CONSULTA, VERIFIQUE EL SERVICIO\"}";
			bitacora.setDetalle(bitacora.getDetalle()+ "|" + resultado);			
			usuariosService.addBitacora(bitacora);
			return resultado;
		}		
	}
	
	
	private Usuario isUserAuthenticated(String authString, BitacoraOperaciones bitacora){
        
		usuariosService = (UsuariosService) SpringApplicationContext.getBean("usuariosBusinessService");
		
        String decodedAuth = "";
        
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        //System.out.println(decodedAuth);
         
        String[] values = decodedAuth.split(":");
        bitacora.setDetalle(bitacora.getDetalle() + "|" + values[0]);
        
        Usuario user = usuariosService.getUsuarioExterno(values[0]);
        //System.out.println("user: " + user.getCveUsuario() + " password:" + user.getPassword() + "pwd ingresado" + HashUtils.hash(values[1], "SHA-1"));
        if(user!=null && user.getPassword().equalsIgnoreCase(HashUtils.hash(values[1], "SHA-1"))) {
        //if(user!=null) {
        	return user;
        } 
        
        return null;
    }
	
}
