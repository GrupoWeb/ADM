package mx.gob.se.rug.grupos.dwr.action;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.dao.impl.AcreedoresCatalogosDaoJdbcImpl;
import mx.gob.se.rug.acreedores.dao.impl.GrupoDaoJdbcImpl;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.PerfilTO;
import mx.gob.se.rug.to.MessageDwr;
import mx.gob.se.rug.util.MyLogger;

public class GruposDwrAction {
	public MessageDwr eliminarGrupo(String idGrupo, String idUsuario,
			String idAcreedor) {
		try {
			MyLogger.Logger.log(Level.INFO, idUsuario);
			MyLogger.Logger.log(Level.INFO, idAcreedor);
			GrupoDaoJdbcImpl daoJdbcImpl = new GrupoDaoJdbcImpl();
			GrupoPerfilTO grupoPerfilTO = new GrupoPerfilTO();
			grupoPerfilTO.setIdPersonaCrea(idAcreedor);
			grupoPerfilTO.setId(idGrupo);
			daoJdbcImpl.deleteGrupo(grupoPerfilTO, Integer.valueOf(idUsuario));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getParteGrupo(idUsuario, idAcreedor);
	}

	public MessageDwr eliminarGrupoAlerta(String idGrupo, String idUsuario,
			String idAcreedor) {
		MessageDwr dwr= new MessageDwr();
		try {
			MyLogger.Logger.log(Level.INFO, "usuario: "+ idUsuario);
			MyLogger.Logger.log(Level.INFO, "Acreedor: "+ idAcreedor);
			MyLogger.Logger.log(Level.INFO, "Grupo: "+  idGrupo);
			
			
			GrupoDaoJdbcImpl daoJdbcImpl = new GrupoDaoJdbcImpl();
			int numeroUsuarios = daoJdbcImpl.getNumbersOfUsersByGroup(Integer.valueOf(idUsuario), Integer.valueOf(idAcreedor), Integer.valueOf(idGrupo));
		
			if(numeroUsuarios>0){
				dwr.setCodeError(new Integer(0));
				dwr.setMessage("El grupo que esta intentando eliminar contiene "+numeroUsuarios+" usuarios asignados, los cuales seran tambien eliminados. ¿Desea continuar?");
			}else{
				dwr.setCodeError(new Integer(0));
				dwr.setMessage("El grupo que esta intentando eliminar no contiene usuarios asignados. ¿Desea continuar?");
			}
		} catch (Exception e) {
			e.printStackTrace();
			dwr.setCodeError(new Integer(2));
		}
		return dwr;
	}

	public MessageDwr actualizaGrupo(String nombreGrupo ,String perfiles, String idGrupo,
			String idUsuario, String idAcreedor) {
		String cad = "";
		try {
			GrupoDaoJdbcImpl daoJdbcImpl = new GrupoDaoJdbcImpl();
			if(daoJdbcImpl.isNotExistGroup(nombreGrupo, Integer.valueOf(idAcreedor),Integer.valueOf( idGrupo))){
			   boolean bol1 = daoJdbcImpl.setNombreGrupo(Integer.valueOf(idGrupo), nombreGrupo);
				if (bol1) {
					cad = cad +"El nombre se modifico satisfactoriamente \n";
				} else {
					cad = cad +"El nombre se NO se modifico satisfactoriamente \n ";
				}
			}else{
				
				cad = cad +"Ya existe un grupo con ese nombre \n ";
			}
			
			boolean bol = daoJdbcImpl.updatePrivilegiosGrupo(
					Integer.valueOf(idUsuario), Integer.valueOf(idAcreedor),
					Integer.valueOf(idGrupo), perfiles);
			
			MyLogger.Logger.log(Level.INFO, "Resultado de BOL  = " + bol);
			if (bol) {
				cad = "Los permisos fueron modificados satisfactoriamente. \n ";
			} else {
				cad = "Ya existe un grupo con ese nombre \n ";
			}
			
			
			cad = " <script> alert('"+cad+"'); </script> ";
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		MessageDwr md = getParteGrupo(idUsuario, idAcreedor);
		md.setMessage(md.getMessage() + cad);
		return md;
	}

	public MessageDwr guardaGrupo(String nombreGrupo, String perfiles,
			String idUsuario, String idAcreedor) {
		String cad = "";
		try {
			MyLogger.Logger.log(Level.INFO, perfiles);
			GrupoPerfilTO grupoPerfilTO = new GrupoPerfilTO();
			grupoPerfilTO.setIdPersonaCrea(idAcreedor);
			grupoPerfilTO.setDescripcion(nombreGrupo);
			GrupoDaoJdbcImpl daoJdbcImpl = new GrupoDaoJdbcImpl();
			Integer bol = daoJdbcImpl.saveGrupo(grupoPerfilTO, perfiles,
					Integer.valueOf(idUsuario));
			MyLogger.Logger.log(Level.INFO, "Resultado de BOL  = " + bol);
			if (bol.intValue() != 0) {
				cad = " <script> alert('Ya existe un grupo con ese nombre'); </script> ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MessageDwr md = getParteGrupo(idUsuario, idAcreedor);
		md.setMessage(md.getMessage() + cad);
		return md;
	}

	public MessageDwr getParteDesGrupo(String idGrupo) {
		MessageDwr dwr = new MessageDwr();
		dwr.setMessage(writeDescGrupo(Integer.valueOf(idGrupo)).toString());
		dwr.setCodeError(0);
		return dwr;
	}

	public MessageDwr getParteEditarGrupo(String idGrupo, String idUsuario,
			String idAcreedor) {
		MessageDwr dwr = new MessageDwr();
		dwr.setMessage(writeEditarGrupo(Integer.valueOf(idGrupo), idUsuario,
				idAcreedor).toString());
		dwr.setCodeError(0);
		return dwr;
	}

	public MessageDwr getParteGrupo(String idPersona, String idAcreedor) {
		MessageDwr dwr = new MessageDwr();
		dwr.setCodeError(0);
		MyLogger.Logger.log(Level.INFO, "Entro a parte dwr getParteGrupo");
		MyLogger.Logger.log(Level.INFO, "idPErsona 2" + idPersona);
		MyLogger.Logger.log(Level.INFO, "idAcreedor" + idAcreedor);
		try {
			AcreedoresCatalogosDaoJdbcImpl acreedoresCatalogosDao = new AcreedoresCatalogosDaoJdbcImpl();
			List<GrupoPerfilTO> lista = acreedoresCatalogosDao
					.getGrupos(Integer.valueOf(idAcreedor));
			MyLogger.Logger.log(Level.INFO, "tamaño de la lista:" + lista.size());
			List<PerfilTO> listaPerfil = acreedoresCatalogosDao.getPerfiles();
			dwr.setCodeError(0);
			String mensaje = writeParteGrupo(lista, listaPerfil, idPersona,
					idAcreedor).toString();
			MyLogger.Logger.log(Level.INFO, "########################################################");
			MyLogger.Logger.log(Level.INFO, "Mensaje del write:");
			MyLogger.Logger.log(Level.INFO, mensaje);
			dwr.setMessage(mensaje);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MyLogger.Logger.log(Level.INFO, "#####################################################");
		MyLogger.Logger.log(Level.INFO, "mensaje en msj:");
		MyLogger.Logger.log(Level.INFO, dwr.getMessage().toString());
		return dwr;
	}

	private StringBuffer writeParteGrupo(List<GrupoPerfilTO> lista,
			List<PerfilTO> listaPerfil, String idUsuario, String idAcreedor) {
		StringBuffer sb = new StringBuffer();
		sb.append(writeTablaGrupos(lista, idUsuario, idAcreedor));
		sb.append(" <table>");
		sb.append(" <tr><td style=\"visibility: hidden; display: none\" id=\"descGrupo\">");
		sb.append(" </td></tr> </table>");
		sb.append("<table style=\"cursor: pointer;\" class=\"nota\"><tr><td class=\"imgNota\" width=\"44\"> <img src=\"/Rug/resources/imgs/ico_nota.png\" align=\"center\"> </td><td align=\"justify\" class=\"contenidoNota\">Podrá crear diversos grupos para que los usuarios que agregue a los mismos puedan realizar las operaciones permitidas (privilegios) para el grupo correspondiente.</td></tr></table>");
		sb.append(" <table>");
		sb.append(" <tr><td><a onclick=\"mostrarAltaGrupo();ocultarDesGrupo();\" style=\"cursor: pointer;\" class=\"tituloInteriorRojo\"> + Agregar Grupo</a></td></tr>");
		sb.append(" <tr><td style=\"visibility: hidden; display: none\" id=\"agreNuevo\">");
		sb.append(writeFormularioAltaGrupo(listaPerfil, idUsuario, idAcreedor));
		sb.append(" </td></tr> </table>");

		return sb;
	}

	/*
	 * sb.append("<table><tr>");
	 * 
	 * sb .append(
	 * "	<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">"
	 * ); sb.append("Nombre, Denominaci&oacute;n o Raz&oacute;n Social</td>");
	 * // sb.append(
	 * "<td width=\"8%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">"
	 * ); // sb.append("Folio Electr&oacute;nico</td>"); sb .append(
	 * "<td width=\"12%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">RFC, CURP, Documento que acredita su legal estancia en el pa&iacute;s </td>"
	 * ); sb .append(
	 * "	<td width=\"18%\" class=\"encabezadoTablaResumen\" style=\"text-align: center\">Opciones</td>"
	 * ); sb.append("</tr>"); Iterator<AcreedorTO> it =
	 * listaAcreedores.iterator(); AcreedorTO acreedorTO; while (it.hasNext()) {
	 * acreedorTO = it.next(); sb.append("	<tr>"); String rfcurdoc = ""; if
	 * (acreedorTO.getRfc() != null) {
	 * MyLogger.Logger.log(Level.INFO,"--writeTablaAcreedorRep 1--"); rfcurdoc =
	 * acreedorTO.getRfc(); } else {
	 * MyLogger.Logger.log(Level.INFO,"--writeTablaAcreedorRep 2--"); rfcurdoc =
	 * acreedorTO.getCurp(); } sb.append(
	 * "<td class=\"cuerpo1TablaResumen\"> <a href=\"/Rug/controlusuario/setAcreedor.do?idAcreedor="
	 * +acreedorTO.getIdPersona()+"\"> " +
	 * notNull(acreedorTO.getNombreCompleto()) + " </a></td>");
	 * sb.append("<td class=\"cuerpo1TablaResumen\">" + notNull(rfcurdoc) +
	 * "</td>"); //
	 * sb.append("<td class=\"cuerpo1TablaResumen\">"+acreedorTO.getRfc
	 * ()+"</td>"); sb.append(
	 * "<td class=\"cuerpo1TablaResumen\"> <table> <tr> <td> <input type=\"button\" value=\"Eliminar\" onclick=\"eliminaParteAcreedorRepresentado('"
	 * + elementId + "','" + idPersona + "','" + acreedorTO.getIdPersona() +
	 * "')\"></td> <td> <input type=\"button\" value=\"Modificar\" onclick=\"cargaParteAcreedorRepresentado('"
	 * + elementId + "','" + idPersona + "','" + acreedorTO.getIdPersona() +
	 * "')\">    </td> </tr> </table></td>"); sb.append("</tr>"); }
	 * 
	 * sb.append("</table>");
	 */
	private StringBuffer writeDescGrupo(Integer idGrupo) {
		StringBuffer sb = new StringBuffer();
		MyLogger.Logger.log(Level.INFO, "ID GRUPO = " + idGrupo);
		AcreedoresCatalogosDaoJdbcImpl acreedoresCatalogosDao = new AcreedoresCatalogosDaoJdbcImpl();
		List<GrupoPerfilTO> lista = acreedoresCatalogosDao
				.getPrivilegiosByGrupo(idGrupo);
		sb.append(" <table>");
		sb.append(" <tr> <td> <b>Privilegios:</b> </td> <td> </td> </tr>");
		Iterator<GrupoPerfilTO> it = lista.iterator();
		GrupoPerfilTO grupoPerfilTO;
		while (it.hasNext()) {
			grupoPerfilTO = it.next();
			sb.append(" <tr> <td> </td> <td> "
					+ grupoPerfilTO.getDescPrivilegio() + " </td> </tr>");
		}
		sb.append(" <tr> <td> <input type=\"button\" value=\"Ocultar\" onclick=\"ocultarDesGrupo()\" />  </td> <td> </td> </tr>");

		sb.append(" </table>");
		return sb;
	}

	private StringBuffer writeEditarGrupo(Integer idGrupo, String idUsuario,
			String idAcreedor) {
		StringBuffer sb = new StringBuffer();
		AcreedoresCatalogosDaoJdbcImpl acreedoresCatalogosDao = new AcreedoresCatalogosDaoJdbcImpl();
		List<GrupoPerfilTO> listaTiene = acreedoresCatalogosDao.getPrivilegiosByGrupo(idGrupo);
		GrupoPerfilTO grupoPerfilTO = acreedoresCatalogosDao.getGrupo(idGrupo);
		List<PerfilTO> lista = acreedoresCatalogosDao.getPerfiles();
		
		sb.append(" <form> <table>");
		sb.append(" <tr> <td class=\"textoGeneralAzul\"><b class=\"textoGeneralRojo\">*</b>Nombre del Grupo: </td> <td><input type=\"text\" name=\"nombreGrupo\" id=\"idNombreGrupo\" value=\""+grupoPerfilTO.getDescripcion().trim()+"\" size=\"30\" /></td> </tr> ");
		sb.append(" <tr> <td class=\"textoGeneralAzul\"><b class=\"textoGeneralRojo\">*</b>Seleccione privilegios: </td> <td></td> </tr> ");
		Iterator<PerfilTO> itPerfil = lista.iterator();
		PerfilTO perfilTO;
		while (itPerfil.hasNext()) {
			perfilTO = itPerfil.next();
			if (tienePermiso(Integer.valueOf(perfilTO.getId()), listaTiene)) {
				sb.append(" <tr> <td> </td> <td> <input type=\"checkbox\" checked=\"checked\" name=\"perfiles[]\" id=\"perfiles\" value=\""
						+ perfilTO.getId()
						+ "\">"
						+ perfilTO.getDescripcion()
						+ " </td> </tr> ");
			} else {
				sb.append(" <tr> <td> </td> <td> <input type=\"checkbox\" name=\"perfiles[]\" id=\"perfiles\" value=\""
						+ perfilTO.getId()
						+ "\">"
						+ perfilTO.getDescripcion()
						+ " </td> </tr> ");
			}
		}
		sb.append(" <tr> <td> <input type=\"button\" value=\"Aceptar\" onclick=\"guardaCambiosGrupo('"
				+ idGrupo
				+ "','"
				+ idUsuario
				+ "','"
				+ idAcreedor
				+ "')\" /> </td> <td> <input type=\"button\" value=\"Ocultar\" onclick=\"ocultarDesGrupo()\" />  </td></tr> ");

		sb.append(" <tr> <td>  </td></tr> ");
		sb.append(" </table> </form> ");
		return sb;

	}

	private Boolean tienePermiso(Integer idPermiso,
			List<GrupoPerfilTO> listaTiene) {
		boolean regresa = false;
		Iterator<GrupoPerfilTO> it = listaTiene.iterator();
		while (it.hasNext() && !regresa) {
			if (Integer.valueOf(it.next().getId()).intValue() == idPermiso) {
				regresa = true;
			}
		}
		return regresa;
	}

	private StringBuffer writeTablaGrupos(List<GrupoPerfilTO> lista,
			String idUsuario, String idAcreedor) {
		StringBuffer sb = new StringBuffer();
		MyLogger.Logger.log(Level.INFO, "writeTablaGrupos-idUsuarios:" + idUsuario);
		MyLogger.Logger.log(Level.INFO, "writeTablaGrupos-idAcreedor:" + idAcreedor);
		if (lista.size() > 0) {
			sb.append(" <table width=\"650\"> ");
			sb.append(" <tr>	<td class=\"encabezadoTablaResumen\" style=\"text-align: center\"> Nombre del grupo</td> ");
			sb.append("	<td class=\"encabezadoTablaResumen\" style=\"text-align: center\"> opciones</td> </tr> ");
			Iterator<GrupoPerfilTO> it = lista.iterator();
			while (it.hasNext()) {
				GrupoPerfilTO pg = it.next();
				sb.append(" <tr>	<td class=\"cuerpo1TablaResumen\" > <a style=\"cursor: pointer;\" style=\"cursor: pointer;\" onclick=\"ocultarAltaGrupo();mostrarDescGrupo('"
						+ pg.getId()
						+ "');\"> "
						+ pg.getDescripcion()
						+ " </a> </td> ");
				sb.append("	<td class=\"cuerpo1TablaResumen\" ><a style=\"cursor: pointer;\" style=\"cursor: pointer;\" onclick=\"ocultarAltaGrupo();ocultarDesGrupo();mostrarEditarGrupo('"
						+ pg.getId()
						+ "','"
						+ idUsuario
						+ "','"
						+ idAcreedor
						+ "');\"> Editar </a> | <a style=\"cursor: pointer;\" onclick=\"eliminarGrupo('"
						+ pg.getId()
						+ "','"
						+ idUsuario
						+ "','"
						+ idAcreedor
						+ "');\"> Eliminar </a> </td> </tr> ");
			}
			sb.append(" </table> ");
		} else {
			sb.append(" <script> mostrarAltaGrupo(); </script> ");
		}

		return sb;

	}

	private StringBuffer writeFormularioAltaGrupo(List<PerfilTO> lista,
			String idUsuario, String idAcreedor) {
		StringBuffer sb = new StringBuffer();
		sb.append(" <form> <table>");
		sb.append(" <tr> <td class=\"textoGeneralAzul\"><b class=\"textoGeneralRojo\">*</b>Nombre del Grupo: </td> <td> <input type=\"text\" name=\"nombreGrupo\" id=\"nombreGrupo\" /> </td></tr>");
		sb.append(" <tr> <td class=\"textoGeneralAzul\"><b class=\"textoGeneralRojo\">*</b>Seleccione privilegios: </td> <td></td> </tr> ");
		Iterator<PerfilTO> itPerfil = lista.iterator();
		PerfilTO perfilTO;
		while (itPerfil.hasNext()) {
			perfilTO = itPerfil.next();
			sb.append(" <tr> <td> </td> <td> <input type=\"checkbox\" name=\"perfiles[]\" id=\"perfiles\" value=\""
					+ perfilTO.getId()
					+ "\">"
					+ perfilTO.getDescripcion()
					+ " </td> </tr> ");
		}
		sb.append(" <tr> <td> <input type=\"button\" value=\"Aceptar\" onclick=\"guardaGrupo('"
				+ idUsuario
				+ "','"
				+ idAcreedor
				+ "')\" /> </td> <td> <input type=\"button\" value=\"Ocultar\" onclick=\"ocultarAltaGrupo()\" />  </td></tr> ");

		sb.append(" <tr> <td>  </td></tr> ");
		sb.append(" </table> </form> ");
		return sb;
	}

	public MessageDwr refrescarGrupo(String idAcreedor) {
		StringBuffer sb = new StringBuffer();
		MessageDwr msg = new MessageDwr();	
		try {
			MyLogger.Logger.log(Level.INFO, "--- id acreedor :::---"+ idAcreedor+"----");
			AcreedoresCatalogosDaoJdbcImpl acreedoresCatalogosDao = new AcreedoresCatalogosDaoJdbcImpl();			
			sb.append(" <select id='idGrupoSelect' name='idGrupoSelect' >" );
			sb.append(" <option value=\"3\"> ADMINISTRADOR </option> ");
			for (GrupoPerfilTO grupo :acreedoresCatalogosDao.getGrupos(Integer.valueOf(idAcreedor))){
				sb.append(" <option value=\""+grupo.getId()+"\"> "+grupo.getDescripcion()+" </option> ");
				MyLogger.Logger.log(Level.INFO, "item on List....."+ grupo.getDescripcion());
			}
			sb.append(" </select> " );		
			msg.setMessage(sb.toString());
			msg.setCodeError( new Integer(0));
		} catch (Exception e) {
			msg.setMessage("<script> alert('sucedio el siguiente error : "+e.getMessage()+"'); </script>");
			msg.setCodeError( new Integer(0));
		}
		return msg;
	}

}
