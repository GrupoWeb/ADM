package mx.gob.se.rug.pago.dwr.action;


import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.pago.dao.PagoDAO;
import mx.gob.se.rug.pago.to.CarroCompraTO;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.to.UsuarioTO;

public class CarroComprasDwrAction extends AbstractBaseDwrAction {
	public MessageDwr getCarroCompra(){
		MessageDwr dwr = new MessageDwr();
		try{
			HttpSession session=  getSession();
			UsuarioTO usuarioTO = (UsuarioTO) session.getAttribute("usuario");
			PagoDAO pagoDAO = new PagoDAO();
			List<CarroCompraTO> carroCompraTOs = pagoDAO.getCarroCompra(usuarioTO);
			dwr.setCodeError(0);
			dwr.setMessage(writeCarroCompra(carroCompraTOs));
		}catch(Exception e){
			dwr.setCodeError(1);
			dwr.setMessage("No existe el CP");
			e.printStackTrace();
		}
		return dwr;
	}
	private String writeCarroCompra(List<CarroCompraTO> carroCompraTOs ){
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table cellspacing='3' cellpadding='0' class='tituloEncabezado'>");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<td align='center' colspan='3'>");
		sb.append("<a  href='" );
		sb.append(getRequest().getContextPath());
		sb.append("/home/misOperaciones.do' style='cursor: pointer; color: white;'>");
		sb.append("<b>Mis Operaciones Pendientes</b>");
		sb.append("</a>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr style='border-bottom-style: solid; border-bottom-color: white;' class='menuLateralNivel1'>");
		sb.append("<td width='63' align='left' style='font-family: sans-serif; font-size: 12px; color: white;'>Descripcion del tramite</td>");
		sb.append("<td width='36' align='center' style='font-family: sans-serif; font-size: 12px; color: white;'>Cantidad</td>");
		sb.append("<td width='62' align='right' style='font-family: sans-serif; font-size: 12px; color: white;'>Precio</td>");
           
		sb.append("</tr>");
         
		Double precioTotalCarro= new Double(0);
		Iterator< CarroCompraTO> iterator = carroCompraTOs.iterator();
		while (iterator.hasNext()) {
			CarroCompraTO carroCompraTO = (CarroCompraTO) iterator.next();
			
		sb.append("<tr>");
		sb.append("<td class='texto_general'><span class='Small2'>" );
		sb.append(carroCompraTO.getDescripcionTramite());
		sb.append("</span></td>");
		sb.append("<td align='center' class='texto_general'><span class='Small2'>" );
		sb.append(carroCompraTO.getCantidad());
		sb.append("</span></td>");
		sb.append("<td align='right' class='texto_general'><span class='Small2'>$ ");
		sb.append(carroCompraTO.getPrecioTotal());
		sb.append("</span></td>");
		sb.append("</tr>");
		precioTotalCarro= precioTotalCarro +carroCompraTO.getPrecioTotal(); 
		}
            
          
		sb.append("<td align='center' class='texto_general'><span class='Small2'>Total</span></td>");
		sb.append("<td align='center' class='texto_general'> </td>");
		sb.append("<td align='right' class='texto_general'><span class='Small2'>$ " );
		sb.append(precioTotalCarro);
		sb.append("</span></td>");		            
		sb.append("</tr>");
            
		sb.append("<tr align='center'>");
		sb.append("<td/>");
		sb.append("<td><a  href='" );;
		sb.append(getRequest().getContextPath());
		sb.append("/pago/iniciaPago.do' style='cursor: pointer;'>Pagar</a> </td></tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		return sb.toString();
	}
	
}
