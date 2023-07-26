package mx.gob.se.rug.boleta.serviceImpl;

import java.math.BigInteger;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import mx.gob.se.firma.FooterAcuseBeanRemote;
import mx.gob.se.rug.anotacion.tramites.dao.TramitesDAO;
import mx.gob.se.rug.anotacion.tramites.to.AnotacionSnGarantiaTO;
import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.boleta.dao.FirmaTramiteDAO;
import mx.gob.se.rug.boleta.service.BoletaInfService;
import mx.gob.se.rug.boleta.to.BoletaAnotacionSnGarantia;
import mx.gob.se.rug.boleta.to.BoletaAvisoPreventivoTO;
import mx.gob.se.rug.boleta.to.BoletaTO;
import mx.gob.se.rug.boleta.to.DetalleTO;
import mx.gob.se.rug.boleta.to.FirmaMasiva;
import mx.gob.se.rug.boleta.to.GarantiaTO;
import mx.gob.se.rug.boleta.to.Otorgante;
import mx.gob.se.rug.boleta.to.PersonaTO;
import mx.gob.se.rug.boleta.to.Tramite;
import mx.gob.se.rug.boletaservice.BoletaServices;
import mx.gob.se.rug.common.util.NullsFree;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.detallegarantia.to.PartesTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.firma.service.FooterAcuse;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.NumberToLetterConverter;
import mx.gob.se.rug.util.pdf.to.PdfTO;

public class BoletaServiceImpl {
	
	private List<String> textosFormulario;
	
	public String getProcedencia(Integer idTramite) {
		return new BoletaDAO().getProcedenciaByTramite(idTramite);
	}

	public String getTipoTramite(Integer idtramite) {
		return new BoletaDAO().getTipoTramite(idtramite);
	}

	public String getDeudor(Integer idtramite) {
		return new BoletaDAO().getDeudor(idtramite);
	}

	public String getAcreedor(Integer idtramite, String tipo) {
		return new BoletaDAO().getAcreedor(idtramite, tipo);
	}

	public String getTablaBienes(int idgarantia, Integer idTramite) {
		return new BoletaDAO().getTablaBienes(idgarantia, idTramite);
	}

	public String getTipoBienes(Integer relacion) {
		return new BoletaDAO().getTipoBienes(relacion);
	}

	public BoletaTO AnotacionC(Integer idtramite) {
		return new BoletaDAO().AnotacionC(idtramite);
	}

	public BoletaTO AnotacionS(Integer idtramite) {
		return new BoletaDAO().AnotacionS(idtramite);
	}

	public PartesTO getOtorgante(Integer idtramite, Integer idgarantia) {
		return new BoletaDAO().getOtorgante(idtramite, idgarantia);
	}

	public BoletaTO getTramitetrans(Integer idTramiteinc, Integer idTipoTramite) {
		return new BoletaDAO().getTramitetrans(idTramiteinc, idTipoTramite);
	}

	public String getParteTramite(Integer idBoleta, Integer idTramite,
			Integer idTipoGarantia, Integer idParte)
			throws InfrastructureException {
		BoletaDAO boletaDAO = new BoletaDAO();
		BoletaInfService boletaInfService = new BoletaInfService();
		List<PersonaTO> partes = null;
		PdfTO template = null;
		StringBuffer sb = new StringBuffer();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			partes = boletaDAO.getPartes(idTramite, idParte);
			String templateParte = boletaInfService.getTemplateParte(idBoleta,
					idParte, idTipoGarantia, connection);

			Iterator<PersonaTO> iterator = partes.iterator();
			while (iterator.hasNext()) {
				PersonaTO persona = (PersonaTO) iterator.next();
				template = new PdfTO();
				template.setHtml(templateParte);

				template.setValue("[*nomRazonSocial*]", persona.getNombre());
				template.setValue("[*rfcPersona*]", persona.getRfc());
				template.setValue("[*correoElect*]", persona.getCorreoElectronico());
				template.setValue("[*telefonoPersona*]", persona.getTelefono());
				template.setValue("[*extensionPersona*]", persona.getExtension());

				template.setValue("[*domicilioEfectosRug*]", persona.getDomicilio());
				template.setValueSmall("[*folioElectronico*]", persona.getFolioElectronico());
				template.setValue("[*nacionalidad*]", persona.getNacionalidad());
				template.setValue("[*perJuridica*]", persona.getPersoalidadJuridica());

				template.setValue("[*otorganteEsDeudor*]", persona.getMismoOtorgante());
				sb.append(template.getHtml());
			}
		} catch (NoDataFoundException e) {
			sb.append("N/A");
			e.printStackTrace();
		} finally {
			bd.close(connection, null, null);
		}
		return sb.toString();

	}

	public PdfTO getBoletaRenovacion(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		//pdfTO.setHtml(html.getBoletaHtml());
		pdfTO = getRenovacionInfo(detalleTO);
		return pdfTO;		
	}
	
	public PdfTO getBoletaEjecucion(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		//pdfTO.setHtml(html.getBoletaHtml());
		pdfTO = getEjecucionInfo(detalleTO);
		return pdfTO;		
	}

	public PdfTO getBoletaCancelacion(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		//pdfTO.setHtml(html.getBoletaHtml());
		pdfTO = getCancelacionInfo(detalleTO);
		return pdfTO;
	}

	public PdfTO getBoletaRectificacion(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		pdfTO.setHtml(html.getBoletaHtml());
		pdfTO.setHtml("[*boletaHtml*]",
				((PdfTO) getRectificacionInfo(detalleTO)).getHtml());
		return pdfTO;
	}

	public PdfTO getBoletaTransmision(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		pdfTO.setHtml(html.getBoletaHtml());
		pdfTO.setHtml("[*boletaHtml*]",
				((PdfTO) getTransmisionInfo(detalleTO)).getHtml());
		return pdfTO;
	}

	public PdfTO getBoletaModificacion(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		//pdfTO.setHtml(html.getBoletaHtml());
		pdfTO = getModificacionInfo(detalleTO);
		return pdfTO;
	}

	public PdfTO getBoletaInscripcion(DetalleTO detalleTO) throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		//pdfTO.setHtml(html.getBoletaHtml());
		pdfTO = getInscripcionInfo(detalleTO);		
		return pdfTO;
	}

	public PdfTO getBoletaAnotacion(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		pdfTO.setHtml(html.getBoletaHtml());
		pdfTO.setHtml("[*boletaHtml*]",
				((PdfTO) getAnotacionConGInfo(detalleTO)).getHtml());
		return pdfTO;
	}

	public PdfTO getInscripcionInfo(DetalleTO detalleTO) throws NoDataFoundException, InfrastructureException {
		// MyLogger.Logger.log(Level.INFO, "getBoletaGarantia::idTramite::" + detalleTO.getIdGarantia());
		PdfTO pdfTO = getInfoGrantia(detalleTO, 1);
		return pdfTO;

	}

	public PdfTO getTransmisionInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "getBoletaTransmision::idTramite::"
				+ detalleTO.getIdTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,11);
		return pdfTO;
	}

	public PdfTO getModificacionInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "getBoletaModificacion::idTramite::"
				+ detalleTO.getIdTramite()
                                + "data " + detalleTO.getIdTipoTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,2);
		return pdfTO;
	}

	public PdfTO getAnotacionConGInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "getAnotacionConG::idTramite::"
				+ detalleTO.getIdTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,11);
		return pdfTO;

	}

	public PdfTO getRectificacionInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "getBoletaRecticacion::idTramite::"
				+ detalleTO.getIdTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,11);
		return pdfTO;

	}

	public PdfTO getCancelacionInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "getCancelacion::idTramite::"
				+ detalleTO.getIdTramite()
                                + detalleTO.getTipoTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,3);
		return pdfTO;

	}

	public PdfTO getRenovacionInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "getRenovacion::idTramite::"
				+ detalleTO.getIdTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,4);
		return pdfTO;

	}
	
	public PdfTO getEjecucionInfo(DetalleTO detalleTO)
			throws NoDataFoundException, InfrastructureException {
//		MyLogger.Logger.log(Level.INFO, "getEjecucion::idTramite::"
//				+ detalleTO.getIdTramite());
		PdfTO pdfTO = getInfoGrantia(detalleTO,30);
		return pdfTO;

	}

	private PdfTO getInfoGrantia(DetalleTO detalleTO, Integer tipoRazon) throws NoDataFoundException, InfrastructureException {
		BoletaServices html = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		BoletaDAO boletaDAO = new BoletaDAO();

		//Integer idBoleta = boletaDAO.getIdBoletaByIdTipoTramite(detalleTO.getIdTipoTramite());
		//pdfTO.setHtml(html.getTemplateBoleta(idBoleta));		
		if(tipoRazon==2) {
			pdfTO.setHtml(html.getModificacionHtml());
		} else if(tipoRazon==3) {
			pdfTO.setHtml(html.getCancelacionHtml());
		} else if(tipoRazon==4) {
			pdfTO.setHtml(html.getRenovacionHtml());	
		} else if(tipoRazon==30) {
			pdfTO.setHtml(html.getEjecucionHtml());
		} else {
			pdfTO.setHtml(html.getBoletaHtml());
		}
		pdfTO.setIdTramite(detalleTO.getIdTramite());
		pdfTO.setIdGarantiaTO(detalleTO.getIdGarantia());
		/*MyLogger.Logger.log(Level.INFO, "HTML___________________________________________________");
		MyLogger.Logger.log(Level.INFO, pdfTO.getHtml());
		MyLogger.Logger.log(Level.INFO, "HTML___________________________________________________");*/		
		boletaDAO.getDetalleGarantiaTramite(detalleTO);
		setInfoGarantia(pdfTO, null, detalleTO);
		//pdfTO = setFirma(pdfTO);
		return pdfTO;
	}
        
	String getBienesParte(Integer idTramite) {
		InscripcionDAO inscripcionDAO = new InscripcionDAO();
		List<BienEspecialTO> listaBienes = new ArrayList<BienEspecialTO>();
		StringBuffer sb = new StringBuffer();
		
		try {
			listaBienes = inscripcionDAO.getListaBienes(new Integer(idTramite), 2);
			if(listaBienes.isEmpty()) return sb.toString();
			else sb.append("<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>");
			
			Iterator<BienEspecialTO> it = listaBienes.iterator();
			// clase para factoraje
			sb.append("<div class=\"col s12\">");
			sb.append("<table class=\"responsive-table striped\">");
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<th>Tipo Bien Especial</th>");
			sb.append("<th>Tipo Identificador</th>");
			sb.append("<th>Identificador</th>");
//			sb.append("<th>Serie</th>");
			sb.append("<th>Descripcion</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			
			BienEspecialTO bienEspecialTO;
			while (it.hasNext()) {
				bienEspecialTO = it.next();
				sb.append("<tr>");
				sb.append("<td>"	+ bienEspecialTO.getTipoBien() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getTipoIdentificador() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getIdentificador() + "</td>");
//				sb.append("<td>"	+ bienEspecialTO.getSerie() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getDescripcion() + "</td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			sb.append("</table>");
			sb.append("</div>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return sb.toString();
	}
        
        
        
        
	
	String getBienesParteFactoraje(Integer idTramite) {
		InscripcionDAO inscripcionDAO = new InscripcionDAO();
		List<BienEspecialTO> listaBienes = new ArrayList<BienEspecialTO>();
		StringBuffer sb = new StringBuffer();
		
		try {
			listaBienes = inscripcionDAO.getListaBienes(new Integer(idTramite), 2);
			if(listaBienes.isEmpty()) return sb.toString();
			else sb.append("<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>");
			
			Iterator<BienEspecialTO> it = listaBienes.iterator();
			// clase para factoraje
			sb.append("<div class=\"col s12\">");
			sb.append("<table class=\"responsive-table striped\">");
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<th>Tipo Bien Especial</th>");
			sb.append("<th>Serie</th>");
			sb.append("<th>Numero de Factura</th>");
//			sb.append("<th>Serie</th>");
			sb.append("<th>Descripcion</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			
			BienEspecialTO bienEspecialTO;
			while (it.hasNext()) {
				bienEspecialTO = it.next();
				sb.append("<tr>");
				sb.append("<td>"	+ bienEspecialTO.getTipoBien() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getSerie() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getIdentificador() + "</td>");
//				sb.append("<td>"	+ bienEspecialTO.getSerie() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getDescripcion() + "</td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			sb.append("</table>");
			sb.append("</div>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return sb.toString();
	}
        
        
        
        
	
	String getPersonaParte(Integer idTramite, Integer idParte) {
		BoletaDAO boletaDAO = new BoletaDAO();
		List<PersonaTO> partes = null;
		StringBuffer sb = new StringBuffer();
		try {
			partes = boletaDAO.getPartes(idTramite, idParte);
			
			Iterator<PersonaTO> it = partes.iterator();
			
			sb.append("<table class=\"striped\" >");
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>");
			sb.append("<th>Identificador</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			PersonaTO person;
			while (it.hasNext()) {
				person = it.next();
				sb.append("<tr>");
				sb.append("<td>"	+ person.getNombre() + "</td>");
				sb.append("<td>"	+ (person.getPersoalidadJuridica().equalsIgnoreCase("PM")?NullsFree.getNotNullValue(person.getRfc()):NullsFree.getNotNullValue(person.getCurp())) + "</td>");
				sb.append("</tr>");
			}

			sb.append("</tbody>");
			sb.append("</table>");
		} catch (NoDataFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return sb.toString();
	}

	private PdfTO setInfoGarantia(PdfTO pdfTO, Integer idBoleta,DetalleTO detalleTO) throws InfrastructureException {

		InscripcionServiceImpl inscripcionService = new InscripcionServiceImpl();
		//pdfTO.setHtml("[*4*]",getParteTramite(idBoleta, detalleTO.getIdTramite(), detalleTO.getGarantiaTO().getIdTipoGarantia(), 4));
		//pdfTO.setHtml("[*3*]",getParteTramite(idBoleta, detalleTO.getIdTramite(), detalleTO.getGarantiaTO().getIdTipoGarantia(), 3));
		//pdfTO.setHtml("[*1*]",getParteTramite(idBoleta, detalleTO.getIdTramite(), detalleTO.getGarantiaTO().getIdTipoGarantia(), 1));
		setTextosFormulario(inscripcionService.getTextosFormularioByIdGarantia(detalleTO.getGarantiaTO().getIdTipoGarantia()));
		pdfTO.setIdTipoGarantia(detalleTO.getGarantiaTO().getIdTipoGarantia());
				
                /*corellana: Hector pidio que para los de leasing el titulo sea otro */
                if(detalleTO.getGarantiaTO().getIdTipoGarantia().equals(16))
                {
                    pdfTO.setHtml("[*titulo_garantia*]", "          <span class=\"card-title center-align\">INSCRIPCI&Oacute;N DE LA GARANT&Iacute;A MOBILIARIA (LEASING)</span>" );
                }
                 else
                {
                    pdfTO.setHtml("[*titulo_garantia*]", "          <span class=\"card-title center-align\">INSCRIPCI&Oacute;N DE LA GARANT&Iacute;A MOBILIARIA</span>" );
                }
		
                
                pdfTO.setHtml("[*deudoresTable*]", "<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(1)+"</span>" + getPersonaParte(detalleTO.getIdTramite(),2));
		pdfTO.setHtml("[*acreedoresTable*]", "<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(2)+"</span>" + getPersonaParte(detalleTO.getIdTramite(),3));
		pdfTO.setHtml("[*otorgantesTable*]", getTextosFormulario().get(3)==null||getTextosFormulario().get(3).equalsIgnoreCase("")?"":"<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(3)+"</span>" + getPersonaParte(detalleTO.getIdTramite(),1));
		pdfTO.setHtml("[*bienes*]",
				"<div class=\"input-field col s12\">" 
			+   "<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(4)+"</span>"
			+	"<p>" + detalleTO.getGarantiaTO().getDescripcionBienes() + "</p></div>");
                if (detalleTO.getGarantiaTO().getIdTipoGarantia().equals(2))                    
                    pdfTO.setHtml("[*bienesTable*]", "<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>" + getBienesParteFactoraje(detalleTO.getIdTramite()));			
                else
                    pdfTO.setHtml("[*bienesTable*]", "<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>" + getBienesParte(detalleTO.getIdTramite()));			
                
                
		pdfTO.setHtml("[*infoContrato*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(6)+"</span><p>"
				+ NullsFree.getNotNullValue(detalleTO.getGarantiaTO().getInstrumentoPublico()) + "</p></div>");
		pdfTO.setHtml("[*observacionesAdicionales*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(8)+"</span><p>"
                            + NullsFree.getNotNullValue(detalleTO.getGarantiaTO().getOtrosTerminosCondiciones()) + "</p></div>");
		
                /*corellana: unicamente para leasing*/                
                if(detalleTO.getGarantiaTO().getIdTipoGarantia().equals(16))
                {
                    String monto_maximo  = detalleTO.getGarantiaTO().getMontoMaximo();
                    String monto_original = monto_maximo;
                    try{
                        monto_maximo = monto_maximo.replace("$", "");
                        monto_maximo = monto_maximo.replace("Bolivar", "");

                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        double amount = Double.parseDouble(monto_maximo);
                        monto_maximo = formatter.format(amount);
                    }
                    catch (Exception e) {
                         monto_maximo = monto_original;
                    }
                    
                    
                    pdfTO.setHtml("[*montoEstimado*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">"+getTextosFormulario().get(10)+"</span><p>"
				+ NullsFree.getNotNullValue(monto_maximo) + "</p></div>");
                }
                else
                {
                    pdfTO.setHtml("[*montoEstimado*]", "");
                }
                
		if(detalleTO.getIdTipoTramite()==1) {// solo aplica a inscripciones
			pdfTO.setHtml("[*operacion*]", "[*cert*]Inscripci\u00f3n " + (getTextosFormulario().get(9)==null?"":getTextosFormulario().get(9)));
                        
                        if(detalleTO.getGarantiaTO().getIdTipoGarantia().equals(2))
                            pdfTO.setTypeValue("Factoraje");
                        else
                            pdfTO.setTypeValue("Inscripci\u00f3n " + (getTextosFormulario().get(9)==null?"":getTextosFormulario().get(9)));
                        
		}
		//pdfTO.setHtml("[*2*]",getParteTramite(idBoleta, detalleTO.getIdTramite(), detalleTO.getGarantiaTO().getIdTipoGarantia(), 2));
	
		/*String[] listNumbers = detalleTO.getGarantiaTO().getMontoMaximo().split("\\s+");
		pdfTO.setValue("[*moneda*]", listNumbers[0]);
		if(listNumbers[0].equalsIgnoreCase("$")) {
			pdfTO.setValue("[*monedaLetras*]", "DOLARES DE LOS ESTADOS UNIDOS DE AMERICA");
		} else {
			pdfTO.setValue("[*monedaLetras*]", "QUETZALES");
		}*/
			
		/*pdfTO.setValue("[*tipoTramite*]", detalleTO.getTipoTramite());
		pdfTO.setValue("[*tipoGarantia*]", detalleTO.getGarantiaTO().getTipoGarantia());
		pdfTO.setValue("[*fechaCelebActo*]", detalleTO.getGarantiaTO().getFechaActoConvenioGarantia());
		pdfTO.setValue("[*monto*]", String.format("%,.2f", new Double(listNumbers[1])));
		pdfTO.setValue("[*montoLetras*]", NumberToLetterConverter.convertNumberToLetter(detalleTO.getGarantiaTO().getMontoMaximo()));*/		
		//pdfTO.setValue("[*informacionDelContrato*]", detalleTO.getGarantiaTO().getInformacionDelContrato());
		
		//pdfTO.setValue("[*noGarantiaPreviaOt*]", detalleTO.getGarantiaTO().getNoGarantiaPreviaOt());
		
		//pdfTO.setValue("[*tiposBienes*]", detalleTO.getGarantiaTO().getTipoBienes());	
		pdfTO.setValue("[*noGarantia*]", detalleTO.getGarantiaTO().getIdGarantia());
		pdfTO.setValue("[*vigencia*]", detalleTO.getGarantiaTO().getVigencia());
		/*pdfTO.setValue("[*nombreCargo*]", detalleTO.getGarantiaTO().getJuezAnotacion());
		pdfTO.setValue("[*contResolucion*]", detalleTO.getGarantiaTO().getContenidoResolucion());
		pdfTO.setValue("[*cambiosBienesMonto*]", detalleTO.getGarantiaTO().getCambiosBienesMonto());*/
		////instrumento publico
		if(!NullsFree.getNotNullValue(detalleTO.getGarantiaTO().getOtrosTerminosContrato()).equalsIgnoreCase("")) {
			pdfTO.setHtml("[*representantes*]", "<div class=\"input-field col s12\">"
					+ "<span class=\"blue-text text-darken-2\">Representante(s):</span><p>"
					+ detalleTO.getGarantiaTO().getOtrosTerminosContrato() + "</p></div>");
		} else {
			pdfTO.setValue("[*representantes*]", "");
		}
		
		/*pdfTO.setValueSmall("[*inscritoEnFolio*]", detalleTO.getGarantiaTO().getFolioOtorgante());
		pdfTO.setValue("[*terminosGarantia*]", detalleTO.getGarantiaTO().getOtrosTerminosGarantia());
		pdfTO.setValue("[*actoContratoObliga*]", detalleTO.getGarantiaTO().getTipoContrato());*/
		DateFormat currentFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es"));
		SimpleDateFormat formateador = new SimpleDateFormat(
				   "dd 'de' MMMM 'de' yyyy HH:mm:ss", new Locale("es","ES"));
				   Date fechaDate = new Date();
				   String fecha = formateador.format(fechaDate);
		/*try {
			pdfTO.setValue("[*fechaInicio*]", formateador.format(currentFormat.parse(detalleTO.getGarantiaTO().getFechaInicioContrato())).toUpperCase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			pdfTO.setValue("[*fechaFin*]", formateador.format(currentFormat.parse(detalleTO.getGarantiaTO().getFechaFinContrato())).toUpperCase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
		try {
			pdfTO.setValue("[*fechaRegistro*]", detalleTO.getGarantiaTO().getFechaInscripcion().substring(0, detalleTO.getGarantiaTO().getFechaInscripcion().indexOf("*")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		/*try {
			pdfTO.setValue("[*fechaInscripcion*]", fecha);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			pdfTO.setValue("[*numeroGaranLetras*]", NumberToLetterConverter.convertNumberToLetter("R " + detalleTO.getGarantiaTO().getIdGarantia()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		*/
		/*pdfTO.setValue("[*honorarios*]", "Q.2,532.00");
		pdfTO.setValue("[*terminosContratoObliga*]", detalleTO.getGarantiaTO().getOtrosTerminosContrato());
		pdfTO.setValue("[*tipoContratoBasa*]", detalleTO.getGarantiaTO().getTipoContratoBasa());
		pdfTO.setValue("[*fechaInicioContratoBasa*]", detalleTO.getGarantiaTO().getFechaInicioContratoBasa());
		pdfTO.setValue("[*fechaFinContratoBasa*]", detalleTO.getGarantiaTO().getFechaFinContratoBasa());
		pdfTO.setValue("[*terminosBasa*]", detalleTO.getGarantiaTO().getOtrosTerminosContratoBasa());
		pdfTO.setValue("[*numeroGarantia*]", "REG-" + detalleTO.getGarantiaTO().getIdGarantia());		
		pdfTO.setValue("[*vigencia*]", detalleTO.getGarantiaTO().getVigencia());*/
		pdfTO.setValue("[*noOperacion*]", detalleTO.getIdTramite().toString());
		/*pdfTO.setValue("[*infoAdicional*]", detalleTO.getGarantiaTO().getInfoAdicional());
		pdfTO.setValue("[*nombreUsuario*]", detalleTO.getPersonaInscribe().getNombre());
		pdfTO.setValue("[*perfilUsuario*]", detalleTO.getPersonaInscribe().getPerfil());*/
		pdfTO.setValue("[*razonOperacion*]", detalleTO.getGarantiaTO().getTipoContratoBasa());			
				
		return pdfTO;
	}

	public String getTramitesByGarantia(Integer idGarantia) {
		return new BoletaDAO().getTramitesByGarantia(idGarantia);
	}

	public PdfTO setFirma(PdfTO pdfTO) throws InfrastructureException {

		FooterAcuse footerAcuse = new FooterAcuse();
		FooterAcuseBeanRemote footerAcuseBean = footerAcuse.getAcuseBean();

		MyLogger.Logger.log(Level.INFO, "param pdfTO.getIdTramite()::"
				+ pdfTO.getIdTramite().toString());

		footerAcuseBean.getFirmaHistorico(new BoletaDAO()
				.getIdTramiteTempByIdTramite(pdfTO.getIdTramite()));

		if (footerAcuseBean.getFirmasTO().getMr().getCode().intValue() == 0) {

			pdfTO.setValue("[*cadenaOrigSolicitante*]", cut(footerAcuseBean
					.getFirmasTO().getStringOriginalUser()));
			pdfTO.setValue("[*selloSolicitante*]", cut(footerAcuseBean
					.getFirmasTO().getSignUser()));
			pdfTO.setValue("[*certifSolcitante*]", cut(footerAcuseBean
					.getFirmasTO().getCertUser()));
			pdfTO.setValue("[*selloTiempo*]", cut(footerAcuseBean.getFirmasTO()
					.getTsHuman()));
			pdfTO.setValue("[*cadenaOrigRug*]", cut(footerAcuseBean
					.getFirmasTO().getStringOriginalCentral()));
			pdfTO.setValue("[*selloRug*]", cut(footerAcuseBean.getFirmasTO()
					.getSignCentral()));
			pdfTO.setValue("[*numSerieRug*]", cut(footerAcuseBean.getFirmasTO()
					.getCertNumber()));
			pdfTO.setValue("[*certifRUG*]", cut(footerAcuseBean.getFirmasTO()
					.getCertCentral()));

			Constants constants = new Constants();
			BoletaServiceImpl boletaServiceImpl = new BoletaServiceImpl();
			if (boletaServiceImpl.getProcedencia(pdfTO.getIdTramite())
					.equalsIgnoreCase("RUG_FIRMA_DOCTOS")) {// Nuevo
				pdfTO.setValue("[*GMT*]",
						constants.getParamValue(Constants.BOLETA_GMT_ZULU));
			} else {// Viejo
				pdfTO.setValue("[*GMT*]",
						constants.getParamValue(Constants.BOLETA_GMT_MEXICO));
			}

		} else {
			throw new InfrastructureException("Code Error:: "
					+ footerAcuseBean.getFirmasTO().getMr().getCode()
					+ "  Message:: "
					+ footerAcuseBean.getFirmasTO().getMr().getMessage());
		}

		return pdfTO;
	}

	public PdfTO setFirmaCert(PdfTO pdfTO, Integer idTramiteTemp,
			String idTramites, mx.gob.se.rug.to.UsuarioTO usuarioTO)
			throws InfrastructureException {

		FooterAcuse footerAcuse = new FooterAcuse();
		FooterAcuseBeanRemote footerAcuseBean = footerAcuse.getAcuseBean();

		MyLogger.Logger.log(Level.INFO,
				"param:: pdfTO.getIdTramite().toString() ="
						+ pdfTO.getIdTramite().toString());

		MyLogger.Logger.log(Level.INFO, "--doCertificacion WS--");
		footerAcuseBean.doCertificacion(idTramites, idTramiteTemp, usuarioTO
				.getPersona().getIdPersona());

		MyLogger.Logger.log(Level.INFO, "--getFirmaHistorico WS--");
		footerAcuseBean.getFirmaHistorico(idTramiteTemp);

		if (footerAcuseBean.getFirmasTO().getMr().getCode().intValue() == 0) {

			pdfTO.setValue("[*cadenaOrigSolicitante*]", cut(footerAcuseBean
					.getFirmasTO().getStringOriginalUser()));
			pdfTO.setValue("[*selloSolicitante*]", cut(footerAcuseBean
					.getFirmasTO().getSignUser()));
			pdfTO.setValue("[*certifSolcitante*]", cut(footerAcuseBean
					.getFirmasTO().getCertUser()));
			pdfTO.setValue("[*selloTiempo*]", cut(footerAcuseBean.getFirmasTO()
					.getTsHuman()));
			pdfTO.setValue("[*cadenaOrigRug*]", cut(footerAcuseBean
					.getFirmasTO().getStringOriginalCentral()));
			pdfTO.setValue("[*selloRug*]", cut(footerAcuseBean.getFirmasTO()
					.getSignCentral()));
			pdfTO.setValue("[*numSerieRug*]", cut(footerAcuseBean.getFirmasTO()
					.getCertNumber()));
			pdfTO.setValue("[*certifRUG*]", cut(footerAcuseBean.getFirmasTO()
					.getCertCentral()));

			Constants constants = new Constants();
			BoletaServiceImpl boletaServiceImpl = new BoletaServiceImpl();
			if (boletaServiceImpl.getProcedencia(pdfTO.getIdTramite())
					.equalsIgnoreCase("RUG_FIRMA_DOCTOS")) {// Nuevo
				pdfTO.setValue("[*GMT*]",
						constants.getParamValue(Constants.BOLETA_GMT_ZULU));
			} else {// Viejo
				pdfTO.setValue("[*GMT*]",
						constants.getParamValue(Constants.BOLETA_GMT_MEXICO));
			}

		} else {
			throw new InfrastructureException("Code Error:: "
					+ footerAcuseBean.getFirmasTO().getMr().getCode()
					+ "  Message:: "
					+ footerAcuseBean.getFirmasTO().getMr().getMessage());
		}
		return pdfTO;
	}

	private String cut(String value) {
		StringBuffer sb = new StringBuffer();
		if (value != null) {
			char a[] = value.toCharArray();
			for (int i = 0; i <= a.length - 1; i++) {
				sb.append(a[i]);
				if (i % 80 == 0) {
					sb.append(" ");
				}
			}
		} else {
			sb.append("N/A");
		}
		return sb.toString();
	}

	public PdfTO getCertificacionDetalle(Integer idGarantia, Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		PdfTO pdfTO = new PdfTO();
		BoletaDAO boletaDAO = new BoletaDAO();
		List<DetalleTO> detalleTOs = boletaDAO.getHistoricoDetalle(idGarantia,
				idTramite);
		Iterator<DetalleTO> iterator = detalleTOs.iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("<table width=\"650\" border=\"0\"><tbody> <tr> <td colspan=\"2\" class=\"tituloEncabezado2Left\">");
		sb.append("Asiento(s)");
		sb.append("</td> </tr></tbody></table>");
		while (iterator.hasNext()) {
			DetalleTO detalleTO = (DetalleTO) iterator.next();
			sb.append("<table width=\"650\" border=\"0\"><tbody> <tr> <td colspan=\"2\" class=\"tituloEncabezado2Left\">");
			sb.append(detalleTO.getTipoTramite() + ":");
			sb.append("</td> </tr></tbody></table>");
			sb.append(((PdfTO) getInfoGrantia(detalleTO, 1)).getHtml());
		}
		pdfTO.setHtml(sb.toString());
		return pdfTO;
	}

	public PdfTO getCertificacion(Integer idGarantia, Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();
		PdfTO pdfTO = new PdfTO();
		pdfTO.setHtml(boletaServices.getCertificacionHtml());
		pdfTO.setHtml("[*boletaHtml*]",
				((PdfTO) getCertificacionDetalle(idGarantia, idTramite))
						.getHtml());
		return pdfTO;
	}

	public PdfTO getAvisoPreventivo(Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();
		PdfTO pdfTOA = new PdfTO();

		pdfTOA.setHtml(boletaServices.getBoletaAvisoPreventivol());

		pdfTOA.setIdTramite(idTramite);
		pdfTOA = setFirma(pdfTOA);

		BoletaAvisoPreventivoTO avisoPreventivoTO = boletaServices
				.getAvisoPrevetivo(idTramite);

		pdfTOA.setHtml("[*4*]", getParteTramite(2, idTramite, null, 4));
		pdfTOA.setHtml("[*1*]", getParteTramite(2, idTramite, null, 1));

		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*idGarantia*]", "N/A");
		pdfTOA.setValue("[*descBienesMuebles*]",
				avisoPreventivoTO.getDescripcionBienes());
		pdfTOA.setValue("[*fechaCreacion*]",
				avisoPreventivoTO.getFechaCreacion());
		pdfTOA.setValue("[*vigencia*]", avisoPreventivoTO.getVigencia());
		pdfTOA.setValue("[*nombreUsuario*]",
				avisoPreventivoTO.getNombreUsuarioInscribe());
		pdfTOA.setValue("[*perfilUsuario*]", avisoPreventivoTO.getPerfil());
		pdfTOA.setValue("[*nombreCargo*]", avisoPreventivoTO.getAnotacionJuez());
		pdfTOA.setValue("[*inscritoEnFolio*]",
				avisoPreventivoTO.getFolioOtorgante());

		return pdfTOA;
	}

	public PdfTO getCertificacionDetalleAnotacion(Integer idPadre)
			throws NoDataFoundException, InfrastructureException,
			mx.gob.se.exception.InfrastructureException {
		PdfTO pdfTO = new PdfTO();
		TramitesDAO tramitesDAO = new TramitesDAO();

		List<AnotacionSnGarantiaTO> detalleTOs = tramitesDAO
				.getTramitesHByIdTramite(idPadre);
		Iterator<AnotacionSnGarantiaTO> iterator = detalleTOs.iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("<table width=\"650\" border=\"0\"><tbody> <tr> <td colspan=\"2\" class=\"tituloEncabezado2Left\">");
		sb.append("Asiento(s)");
		sb.append("</td> </tr></tbody></table>");
		while (iterator.hasNext()) {
			AnotacionSnGarantiaTO detalleTO = (AnotacionSnGarantiaTO) iterator
					.next();
			sb.append("<table width=\"650\" border=\"0\"><tbody> <tr> <td colspan=\"2\" class=\"tituloEncabezado2Left\">");
			sb.append(detalleTO.getTipoTramiteStr() + ":");
			sb.append("</td> </tr></tbody></table>");
			switch (detalleTO.getIdTipoTramite().intValue()) {
			case 10:// Anotacion
				sb.append(((PdfTO) getAnotacionSNGarantia(detalleTO
						.getIdTramiteFinal())).getHtml());
				break;
			case 26:
				sb.append(((PdfTO) getAnotacionSNGarantiaRenovacion(detalleTO
						.getIdTramiteFinal())).getHtml());
				break;
			case 27:
				sb.append(((PdfTO) getAnotacionSNGarantiaCancelacion(detalleTO
						.getIdTramiteFinal())).getHtml());
				break;
			case 28:
				sb.append(((PdfTO) getAnotacionSNGarantiaModificacion(detalleTO
						.getIdTramiteFinal())).getHtml());
				break;
			case 29:
				sb.append(((PdfTO) getAnotacionSNGarantiaRectificacion(detalleTO
						.getIdTramiteFinal())).getHtml());
				break;
			}

		}
		pdfTO.setHtml(sb.toString());
		return pdfTO;
	}

	public PdfTO getAnotacionSNGarantia(Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();

		BoletaDAO boletaDAO = new BoletaDAO();
		PdfTO pdfTOA = new PdfTO();

		pdfTOA.setHtml(boletaServices.getTemplateBoleta(6));

		pdfTOA.setIdTramite(idTramite);
		pdfTOA = setFirma(pdfTOA);

		BoletaAnotacionSnGarantia anotacionSnGarantia = boletaDAO
				.getAnotacionSnGarantia(idTramite);

		pdfTOA.setHtml("[*1*]", getParteTramite(1, idTramite, null, 1));

		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*idGarantia*]", "N/A");
		pdfTOA.setValue("[*nombreCargo*]",
				anotacionSnGarantia.getAutoridadInstruye());
		pdfTOA.setValue("[*contResolucion*]",
				anotacionSnGarantia.getAnotacion());
		pdfTOA.setValue("[*vigencia*]", anotacionSnGarantia.getVigencia());
		pdfTOA.setValue("[*fechaCreacion*]",
				anotacionSnGarantia.getFechaInscripcion());
		pdfTOA.setValue("[*nombreUsuario*]",
				anotacionSnGarantia.getNombreUsuario());
		pdfTOA.setValue("[*perfilUsuario*]", anotacionSnGarantia.getPerfil());
		pdfTOA.setValue("[*inscritoEnFolio*]",
				anotacionSnGarantia.getFolioOtorgante());

		return pdfTOA;
	}

	public PdfTO getAnotacionSNGarantiaModificacion(Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();

		BoletaDAO boletaDAO = new BoletaDAO();
		PdfTO pdfTOA = new PdfTO();

		TramitesDAO tramitesDAO = new TramitesDAO();
		AnotacionSnGarantiaTO anotacionSnGarantiaTO = new AnotacionSnGarantiaTO();
		anotacionSnGarantiaTO = tramitesDAO.getAnotacionSnGarantia_H(idTramite);

		pdfTOA.setHtml(boletaServices.getTemplateBoleta(16));

		pdfTOA.setIdTramite(idTramite);
		pdfTOA = setFirma(pdfTOA);

		pdfTOA.setHtml("[*1*]", getParteTramite(1, idTramite, null, 1));

		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*idGarantia*]", "N/A");
		pdfTOA.setValue("[*nombreCargo*]",
				anotacionSnGarantiaTO.getAutoridadAutoriza());
		pdfTOA.setValue("[*contResolucion*]",
				anotacionSnGarantiaTO.getAnotacion());
		pdfTOA.setValue("[*vigencia*]", anotacionSnGarantiaTO.getVigenciaStr());
		pdfTOA.setValue("[*fechaCreacion*]",
				anotacionSnGarantiaTO.getFechaFirma());
		pdfTOA.setValue("[*nombreUsuario*]",
				anotacionSnGarantiaTO.getNombreUsario());
		pdfTOA.setValue("[*perfilUsuario*]", anotacionSnGarantiaTO.getPerfil());
		pdfTOA.setValue("[*inscritoEnFolio*]",
				anotacionSnGarantiaTO.getFoliomercantil());

		return pdfTOA;
	}

	public PdfTO getAnotacionSNGarantiaRectificacion(Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();

		BoletaDAO boletaDAO = new BoletaDAO();
		PdfTO pdfTOA = new PdfTO();

		TramitesDAO tramitesDAO = new TramitesDAO();
		AnotacionSnGarantiaTO anotacionSnGarantiaTO = new AnotacionSnGarantiaTO();
		anotacionSnGarantiaTO = tramitesDAO.getAnotacionSnGarantia_H(idTramite);

		pdfTOA.setHtml(boletaServices.getTemplateBoleta(17));

		pdfTOA.setIdTramite(idTramite);
		pdfTOA = setFirma(pdfTOA);

		pdfTOA.setHtml("[*1*]", getParteTramite(1, idTramite, null, 1));
		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*idGarantia*]", "N/A");
		pdfTOA.setValue("[*nombreCargo*]",anotacionSnGarantiaTO.getAutoridadAutoriza());
		// pdfTOA.setValue("[*contResolucion*]",anotacionSnGarantiaTO.getAnotacion());
		pdfTOA.setValue("[*contResolucion*]",anotacionSnGarantiaTO.getResolucion());
		pdfTOA.setValue("[*vigencia*]", anotacionSnGarantiaTO.getVigenciaStr());
		pdfTOA.setValue("[*fechaCreacion*]",
				anotacionSnGarantiaTO.getFechaFirma());
		pdfTOA.setValue("[*nombreUsuario*]",
				anotacionSnGarantiaTO.getNombreUsario());
		pdfTOA.setValue("[*perfilUsuario*]", anotacionSnGarantiaTO.getPerfil());
		pdfTOA.setValue("[*inscritoEnFolio*]",
				anotacionSnGarantiaTO.getFoliomercantil());

		return pdfTOA;
	}

	public PdfTO getAnotacionSNGarantiaRenovacion(Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();

		BoletaDAO boletaDAO = new BoletaDAO();
		PdfTO pdfTOA = new PdfTO();

		TramitesDAO tramitesDAO = new TramitesDAO();
		AnotacionSnGarantiaTO anotacionSnGarantiaTO = new AnotacionSnGarantiaTO();
		anotacionSnGarantiaTO = tramitesDAO.getAnotacionSnGarantia_H(idTramite);

		pdfTOA.setHtml(boletaServices.getTemplateBoleta(14));

		pdfTOA.setIdTramite(idTramite);
		pdfTOA = setFirma(pdfTOA);

		pdfTOA.setHtml("[*1*]", getParteTramite(1, idTramite, null, 1));

		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*idGarantia*]", "N/A");
		pdfTOA.setValue("[*nombreCargo*]",
				anotacionSnGarantiaTO.getAutoridadAutoriza());
		pdfTOA.setValue("[*contResolucion*]",
				anotacionSnGarantiaTO.getAnotacion());
		pdfTOA.setValue("[*vigencia*]", anotacionSnGarantiaTO.getVigenciaStr());
		pdfTOA.setValue("[*fechaCreacion*]",
				anotacionSnGarantiaTO.getFechaFirma());
		pdfTOA.setValue("[*nombreUsuario*]",
				anotacionSnGarantiaTO.getNombreUsario());
		pdfTOA.setValue("[*perfilUsuario*]", anotacionSnGarantiaTO.getPerfil());
		pdfTOA.setValue("[*inscritoEnFolio*]",
				anotacionSnGarantiaTO.getFoliomercantil());

		return pdfTOA;
	}

	public PdfTO getAnotacionSNGarantiaCancelacion(Integer idTramite)
			throws NoDataFoundException, InfrastructureException {
		BoletaServices boletaServices = new BoletaServices();

		PdfTO pdfTOA = new PdfTO();

		TramitesDAO tramitesDAO = new TramitesDAO();
		AnotacionSnGarantiaTO anotacionSnGarantiaTO = new AnotacionSnGarantiaTO();
		anotacionSnGarantiaTO = tramitesDAO.getAnotacionSnGarantia_H(idTramite);

		pdfTOA.setHtml(boletaServices.getTemplateBoleta(15));

		pdfTOA.setIdTramite(idTramite);
		pdfTOA = setFirma(pdfTOA);

		pdfTOA.setHtml("[*1*]", getParteTramite(1, idTramite, null, 1));

		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*idGarantia*]", "N/A");
		pdfTOA.setValue("[*nombreCargo*]",
				anotacionSnGarantiaTO.getAutoridadAutoriza());
		pdfTOA.setValue("[*contResolucion*]",
				anotacionSnGarantiaTO.getAnotacion());
		pdfTOA.setValue("[*vigencia*]", anotacionSnGarantiaTO.getVigenciaStr());
		pdfTOA.setValue("[*fechaCreacion*]",
				anotacionSnGarantiaTO.getFechaFirma());
		pdfTOA.setValue("[*nombreUsuario*]",
				anotacionSnGarantiaTO.getNombreUsario());
		pdfTOA.setValue("[*perfilUsuario*]", anotacionSnGarantiaTO.getPerfil());
		pdfTOA.setValue("[*inscritoEnFolio*]",
				anotacionSnGarantiaTO.getFoliomercantil());

		return pdfTOA;
	}

	public String getAnotacion(Integer idTramite) throws NoDataFoundException {
		BoletaServices boletaServices = new BoletaServices();
		BoletaDAO boletaDAO = new BoletaDAO();
		PdfTO pdfTOA = new PdfTO();

		BoletaAnotacionSnGarantia anotacionSnGarantia = boletaDAO
				.getAnotacionSnGarantia(idTramite);

		pdfTOA.setHtml(boletaServices.getBoletaAnotacionCNGarantia());
		MyLogger.Logger.log(Level.INFO,
				"-Anotacion con garantia solo anotacion-");
		pdfTOA.setValue("[*idTramite*]", idTramite.toString());
		pdfTOA.setValue("[*autoridadInstruye*]",
				anotacionSnGarantia.getAutoridadInstruye());
		pdfTOA.setValue("[*anotacion*]", anotacionSnGarantia.getAnotacion());
		pdfTOA.setValue("[*vigencia*]", anotacionSnGarantia.getVigencia());
		pdfTOA.setValue("[*fechaInscripcion*]",
				anotacionSnGarantia.getFechaInscripcion());
		pdfTOA.setValue("[*nombreUsuario*]",
				anotacionSnGarantia.getNombreUsuario());
		pdfTOA.setValue("[*infoAdicional*]", "N/A");
		pdfTOA.setValue("[*perfil*]", anotacionSnGarantia.getPerfil());

		return pdfTOA.getHtml();
	}

	public String getNombreAcreedor(Integer idTramite) {
		return new BoletaDAO().getNombreAcreedorByTramite(idTramite);
	}

	public String getAsientos(Integer idTramite, FirmaMasiva firmaMasiva) {
		StringBuffer regresa = new StringBuffer();
		Iterator<GarantiaTO> it = new BoletaDAO().getGarantiaTramite(idTramite)
				.iterator();
		GarantiaTO garantiaTO;
		Tramite tramite;
		while (it.hasNext()) {
			garantiaTO = it.next();
			tramite = new Tramite();
			String otorgante = getOtorgantesFirma(
					Integer.valueOf(garantiaTO.getIdTramite()), tramite);
			regresa.append(" <tr> <td class=\"parrafo2\"> ");

			regresa.append("<![CDATA[");
			regresa.append(garantiaTO.getClaveRastreo() == null
					|| garantiaTO.getClaveRastreo().equals("null") ? "N/A"
					: garantiaTO.getClaveRastreo());
			regresa.append("]]>");

			regresa.append(" </td> <td class=\"parrafo2\">");
			regresa.append("<![CDATA[");
			regresa.append(garantiaTO.getIdTramite());
			regresa.append("]]>");

			regresa.append(" </td> <td class=\"parrafo2\">");
			regresa.append("<![CDATA[");
			regresa.append((garantiaTO.getIdGarantia() == null
					|| garantiaTO.getIdGarantia().equalsIgnoreCase("0") ? "N/A"
					: garantiaTO.getIdGarantia()));
			regresa.append("]]>");

			regresa.append(" </td> <td class=\"parrafo2\"> ");
			regresa.append("<![CDATA[");
			regresa.append(garantiaTO.getFechaInscripcion());
			regresa.append("]]>");

			regresa.append(" </td> <td class=\"parrafo2\"> ");
			regresa.append("<![CDATA[");
			regresa.append((garantiaTO.getVigencia() == null
					|| garantiaTO.getVigencia().equalsIgnoreCase("0") ? "N/A"
					: garantiaTO.getVigencia()));
			regresa.append("]]>");

			regresa.append("</td> <td class=\"parrafo2\"> ");
			regresa.append(otorgante);
			regresa.append("</td> </tr> ");

			tramite.setClaveRastreo(garantiaTO.getClaveRastreo() == null
					|| garantiaTO.getClaveRastreo().equals("null") ? "N/A"
					: garantiaTO.getClaveRastreo());
			tramite.setCadenaUnicaDatos(new BigInteger(garantiaTO
					.getIdTramite()));
			tramite.setIdGarantia(new BigInteger(garantiaTO.getIdGarantia()));
			tramite.setVigencia(new BigInteger(garantiaTO.getVigencia()));
			tramite.setFechaAsiento(garantiaTO.getFechaInscripcion());
			firmaMasiva.getTramite().add(tramite);
		}
		return regresa.toString();
	}

	public String getOtorgantesFirma(Integer idTramite, Tramite tramite) {
		StringBuffer sb = new StringBuffer();
		sb.append(" <table>");

		MyLogger.Logger.log(Level.INFO, "Id del tramite terminado : "
				+ idTramite);
		// sb.append(" <tr>  <td class=\"tituloCampo\"> Nombre </td> <td class=\"tituloCampo\"> Folio</td> </tr> ");
		Iterator<OtorganteTO> lista = new BoletaDAO().getOtorganteByTramite(
				idTramite).iterator();
		Otorgante otorgante;
		while (lista.hasNext()) {
			otorgante = new Otorgante();
			OtorganteTO oto = lista.next();
			otorgante.setFolioElectronico(oto.getFolioMercantil());
			otorgante.setNombreCompleto(oto.getNombreCompleto());
			otorgante.setCurp(oto.getCurp());
			tramite.getOtorgante().add(otorgante);
			sb.append(" <tr><td> ");
			sb.append("<![CDATA[");
			sb.append(oto.getNombreCompleto());
			sb.append("]]>");
			sb.append(" </td><td>");
			sb.append("<![CDATA[");
			sb.append((oto.getFolioMercantil() == null
					|| oto.getFolioMercantil().equalsIgnoreCase("") ? "N/A"
					: oto.getFolioMercantil()));
			sb.append("]]>");
			sb.append(" </td> </tr> ");
		}

		sb.append(" </table>");
		return sb.toString();
	}

	public Integer getSizeAsientos(Integer idTramite) {
		return new FirmaTramiteDAO().idTramitesByTramite(idTramite).size();

	}

	public String getNombreUsuario(Integer idTramite) {
		return new FirmaTramiteDAO().getNombreUsuarioByTramite(idTramite);
	}

	public List<String> getTextosFormulario() {
		return textosFormulario;
	}

	public void setTextosFormulario(List<String> textosFormulario) {
		this.textosFormulario = textosFormulario;
	}

}
