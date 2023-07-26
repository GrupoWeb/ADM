package mx.gob.se.rug.transmision.dwr.action;

import java.util.Iterator;
import java.util.List;

import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.to.MessageDwr;

public class OtorganteDwrAction extends AbstractBaseDwrAction{
	
	public MessageDwr Agregar(){		
		MessageDwr dwrTO= new MessageDwr();
		dwrTO.setCodeError(new Integer(0));
		dwrTO.setMessage(tableSearch());
		return dwrTO;
	}

	private String tableSearch(){
		StringBuffer html= new StringBuffer();
			
		html.append("<table id='mytable' class='mytable' cellpadding='3' cellspacing='2' border='0' width='100%' align='left'>");
		html.append("<tbody>");
		html.append("  <tr>");
		html.append("<td class='texto_azul' width='10'>*</td>");
		html.append("<td class='textoGeneralRojo' align='left' colspan='2'>Tipo de Persona:</td> ");
		html.append("</tr><tr>");
		html.append("<td>&nbsp;</td>");
		html.append("<td class='texto_general' align='left'>");
		html.append("<select tabindex='42' name='tipoPersona' id='tipoPersonaOtorgante' class='campo_general' onchange='CamposTipoPersonaOtorgante();'>");
		html.append("<option value='1'>--Seleccione--</option>");
		html.append("<option value='2' selected='selected' >Moral</option>");
		html.append("<option value='3'>F&iacute;sica</option>");
		html.append("</select>");
		html.append("<script type='text/javascript'>");		
		html.append("ajax().Link('jsp/contenido/datosgenerales/CamposPM.jsp', 'DescOtorgante');");
		html.append("</script> " );
		html.append("<td class='texto_general' valign='top'>&nbsp;</td>");
		html.append("</tr><tr><td></td>");
		html.append("<td id='DescOtorgante'></td>" );
		html.append("</tr><tr>");
		html.append("<td class='texto_azul' width='10'>*</td>");
		html.append("<td colspan='2' class='texto_general' align='left'><span class='textoGeneralRojo'>RFC : </span></td>");
		html.append("</tr><tr> <td>&nbsp;</td>");
		html.append("<td colspan='2' class='texto_general' align='left'><input tabindex='38' class='campo_general' size='15' maxlength='60'");
		html.append(" name='rfc' id='rfc' value=''	onfocus='estilo_foco(this, 'tipocalle', 'errornumero', 'error_cp', 'HelpComodin'); onFocusSwitch(this,1);");
		html.append(" onblur='estilo_sinfoco(this, 'tipocalle', 'errornumero', '', 'HelpComodin'); onBlurSwitch(this,1);' /></td>");		
		html.append("</tr><tr>");
		html.append("<td class='texto_azul' width='10'>*</td>");
		html.append("<td colspan='2' class='texto_general' align='left'><span class='textoGeneralRojo'>Folio Electrónico : </span></td>");
		html.append("</tr><tr>");
		html.append("<td colspan='1' class='texto_general' align='left'>");
		html.append("<input	tabindex='38' class='campo_general' size='15' maxlength='60'");
		html.append("name='folioMercantil' id='folioMercantil' value=''");
		html.append("onfocus='estilo_foco(this, 'tipocalle', 'errornumero', 'error_cp', 'HelpComodin'); onFocusSwitch(this,1);");
		html.append("onblur='estilo_sinfoco(this, 'tipocalle', 'errornumero', '', 'HelpComodin'); onBlurSwitch(this,1);' /></td>");
		html.append("</tr> <tr>");
		html.append("<td align='center'><span class='textoGeneralRojo'> ");
		html.append("<input name='button2' type='button' class='tituloInteriorRojo'");
		html.append(" id='button2' value='Guardar' onclick=agregarOtorgante();/></TD>");
		html.append("<td><input type='button' class='tituloInteriorRojo' name='Submit2' value='Cancelar' onclick=index.jsp;>");
		html.append(" </span></td>");
		html.append("</td>");
		html.append("</tr>");
		html.append("</tbody>");
		html.append("</table>");
		
		return html.toString();
	}
}
