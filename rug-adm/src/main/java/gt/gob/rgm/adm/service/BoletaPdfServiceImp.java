package gt.gob.rgm.adm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

//import com.itextpdf.kernel.events.PdfDocumentEvent;



import gt.gob.rgm.adm.dao.RugBoletaPdfRepository;
import gt.gob.rgm.adm.dao.RugCatTextoFormRepository;
import gt.gob.rgm.adm.dao.RugGarantiasBienesHRepository;
import gt.gob.rgm.adm.dao.RugParametroConfRepository;
import gt.gob.rgm.adm.dao.TramitesRepository;
import gt.gob.rgm.adm.dao.VDetalleBoletaNuevoRepository;
import gt.gob.rgm.adm.dao.VDetalleBoletaPartesRepository;
import gt.gob.rgm.adm.model.RugBoletaPdf;
import gt.gob.rgm.adm.model.RugCatTextoForm;
import gt.gob.rgm.adm.model.RugGarantiasBienesH;
import gt.gob.rgm.adm.model.Tramites;
import gt.gob.rgm.adm.model.VDetalleBoletaNuevo;
import gt.gob.rgm.adm.model.VDetalleBoletaPartes;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.PageXofY;
import gt.gob.rgm.adm.util.PdfTO;
import gt.gob.rgm.adm.util.Random;
//import gt.gob.rgm.adm.util.signatureInfo;
//import gt.gob.rgm.adm.util.signaturePDF;
//import gt.gob.rgm.security.domain.SignatureInfo;
//import gt.gob.rgm.security.service.DigitalSignatureServiceImp;
//import gt.gob.rgm.security.domain.SignatureInfo;

import gt.gob.rgm.adm.constants.ConstantsCv;

@Stateless
public class BoletaPdfServiceImp implements BoletaPdfService {

	@Inject
	private VDetalleBoletaNuevoRepository detalleBoletaDao;
	
	@Inject
	private VDetalleBoletaPartesRepository partesBoletaDao;
	
	@Inject
	private RugGarantiasBienesHRepository bienesBoletaDao;
	
	@Inject
	private RugParametroConfRepository parametroDao;
	
	@Inject
	private RugBoletaPdfRepository	pdfDao;
	
	@Inject
	private TramitesRepository tramitesDao;
	
	@Inject
	private RugCatTextoFormRepository rugCatTextoFormDao;
	
	@Override
	public byte[] getBoletaPdf(Long pIdTramite, Long pIdGarantia) {

            System.out.println("Generar PDF");

//		DigitalSignatureServiceImp digitalSignatureSvc = new DigitalSignatureServiceImp();
//		SignatureInfo info = new SignatureInfo();
//                signaturePDF digitalSignatureSvc = new signaturePDF();
//		signatureInfo info = new signatureInfo();
		// Variables Firma
                
                String signText = "";
		String signImage = "";
		String signFile = "";
		String signPassword = "";
		String signLocation = "";
		String signLlx = "";
		String signLly = "";
		String signUrx = "";
		String signUry = "";
		String signPage = "";
		String signFieldname = "";
		

		PdfTO pdfTO = new PdfTO();
		
		Tramites tram = new Tramites();
		tram = tramitesDao.findByIdTemp(pIdTramite);
		
		if(tram==null) return null;
		
                System.out.println("Generar PDF 2");
		VDetalleBoletaNuevo detalleTO = new VDetalleBoletaNuevo();
		detalleTO = detalleBoletaDao.findByTramite(tram.getIdTramite(), pIdGarantia);
		List<String> textos = rugCatTextoFormDao.findByIdTipoGarantia(detalleTO.getIdTipoGarantia().longValue());
		
		String anexo = textos.get(9)==null?"":textos.get(9);
		
		switch (detalleTO.getIdTipoTramite().intValue()) {
			case 1:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_INSCRIPCION).getValorParametro());				
				pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n " + anexo);		
                                   /*corellana: Hector pidio que para los de leasing el titulo sea otro */
                                pdfTO.setValue("[*titulo_garantia*]", "          <span class=\"card-title center-align\">INSCRIPCI&Oacute;N DE LA GARANT&Iacute;A MOBILIARIA</span>" );
                
		
                                
                                
				break;
			case 31:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_INSCRIPCION).getValorParametro());				
				pdfTO.setValue("[*operacion*]", "Inscripci\\u00f3n Traslado " + anexo);				
				break;
			case 7:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Modificaci\u00f3n");
				pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
				break;
			case 4:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_CANCELACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Cancelaci\u00f3n");
				break;
			case 9:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_RENOVACION).getValorParametro());
				pdfTO.setValue("[*operacion*]",
						"Pr\u00f3rroga de vigencia");
				break;
			case 30:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_EJECUCION).getValorParametro());
				pdfTO.setValue("[*operacion*]",
						"Ejecuci\u00f3n");
				break;
			case 13:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Embargo");
				pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
				break;
			case 17:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Levantamiento de Embargo");
				pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTAMIENTO DE EMBARGO DE LA");
				break;
			case 21:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Fin de Vigencia");
				pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE FIN DE VIGENCIA DE LA");
				break;
                                
                        case 16:
                                pdfTO.setValue("[*titulo_garantia*]", " <span class=\"card-title center-align\">INSCRIPCI&Oacute;N DE LA GARANT&Iacute;A MOBILIARIA (LEASING)</span>" );
                                break; 
			default:
				break;
		}
		System.out.println("Generar PDF 3");
		pdfTO.setHtml("[*fechaCert*]", "");		
		/*StringBuffer sbCert = new StringBuffer();
		sbCert.append("<div class=\"input-field col s6\">");
		sbCert.append("<span class=\"blue-text text-darken-2\">Fecha Certificaci&oacute;n: </span>"); 
		sbCert.append("<span>" + "16/05/2019 14:40:44" + "</span></div>");
		sbCert.append("<div class=\"input-field col s6\"></div>");
		pdfTO.setHtml("[*fechaCert*]", sbCert.toString());*/
		String server = parametroDao.findByKey(Constants.URL_SERVER).getValorParametro();
		
		pdfTO.setHtml("[*deudoresTable*]", "<span class=\"blue-text text-darken-2\">"+textos.get(1)+"</span>" + getPersonaParte(detalleTO.getIdTramite().longValue(),detalleTO.getIdGarantia().longValue(),2L));
		pdfTO.setHtml("[*acreedoresTable*]", "<span class=\"blue-text text-darken-2\">"+textos.get(2)+"</span>" + getPersonaParte(detalleTO.getIdTramite().longValue(),detalleTO.getIdGarantia().longValue(),3L));
		if(textos.get(3)!=null && textos.get(3)!=null && !textos.get(3).toString().equalsIgnoreCase("")) {
			pdfTO.setHtml("[*otorgantesTable*]", "<span class=\"blue-text text-darken-2\">"+textos.get(3)+"</span>" + getPersonaParte(detalleTO.getIdTramite().longValue(), detalleTO.getIdTramite().longValue(),1L));
		} else {
			pdfTO.setHtml("[*otorgantesTable*]","");
		}
		pdfTO.setHtml("[*bienes*]",
				"<div class=\"input-field col s12\">" 
			+   "<span class=\"blue-text text-darken-2\">"+textos.get(4)+"</span>"
			+	"<p>" + getNotNullValue(detalleTO.getDescGarantia()) + "</p></div>");
		pdfTO.setHtml("[*bienesTable*]", "<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>" + getBienesParte(detalleTO.getIdTramite().longValue()));
		pdfTO.setHtml("[*infoContrato*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">"+textos.get(6)+"</span><p>"
				+ getNotNullValue(detalleTO.getInstrumentoPublico()) + "</p></div>");
		pdfTO.setHtml("[*observacionesAdicionales*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">"+textos.get(8)+"</span><p>"
				+ getNotNullValue(detalleTO.getOtrosTerminosGarantia()) + "</p></div>");
			
		pdfTO.setValue("[*noGarantia*]", detalleTO.getIdGarantia().toString());
		pdfTO.setValue("[*vigencia*]", detalleTO.getVigencia().toString());
		
		if(detalleTO.getOtrosTerminosContrato()!=null&&!detalleTO.getOtrosTerminosContrato().equalsIgnoreCase("")) {
			pdfTO.setHtml("[*representantes*]", "<div class=\"input-field col s12\">"
					+ "<span class=\"blue-text text-darken-2\">Representante(s):</span><p>"
					+ getNotNullValue(detalleTO.getOtrosTerminosContrato()) + "</p></div>");
		} else {
			pdfTO.setValue("[*representantes*]", "");
		}
		
		pdfTO.setValue("[*fechaRegistro*]", detalleTO.getFechaCreacion().substring(0, detalleTO.getFechaCreacion().indexOf("*")));			
		pdfTO.setValue("[*noOperacion*]", detalleTO.getIdTramite().toString());
		pdfTO.setValue("[*razonOperacion*]", getNotNullValue(detalleTO.getObservaciones()));
		
                
		//Se arma el pdf
		pdfTO.setKey(""+tram.getTramiteIncomp().getIdTramiteTemp()+Random.generateRandom(100000));


		
		try {
                
			

			byte file[] = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			PdfWriter writer = new PdfWriter(os);
			ConverterProperties converterProperties = new ConverterProperties();
			PdfDocument pdf = new PdfDocument(writer);
                        PageXofY footerHandler = new PageXofY(pdfTO.getKey(), server);
                        //PageXofY footerHandler = new PageXofY(pdfTO.getKey());
                        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, footerHandler);
			Document doc = HtmlConverter.convertToDocument(pdfTO.getHtml(), pdf, converterProperties);
			//doc.setMargins(100, 50, 50, 100);
			doc.close();
			file = os.toByteArray();

			

                System.out.println("Generar PDF 4");
                    
                    
                signText = ConstantsCv.getParamValue(ConstantsCv.SIGN_TEXT);
		//signImage = ConstantsCv.getParamValue(ConstantsCv.SIGN_IMAGE);
                signImage = "C:/certificado_RGM/firma.png";
                
		//signFile = ConstantsCv.getParamValue(ConstantsCv.SIGN_FILE);
                signFile = "C:/certificado_RGM/rgm.p12";
		signPassword = ConstantsCv.getParamValue(ConstantsCv.SIGN_PASSWORD);
		signLocation = ConstantsCv.getParamValue(ConstantsCv.SIGN_LOCATION);
		signLlx = ConstantsCv.getParamValue(ConstantsCv.SIGN_LLX);
		signLly = ConstantsCv.getParamValue(ConstantsCv.SIGN_LLY);
		signUrx = ConstantsCv.getParamValue(ConstantsCv.SIGN_URX);
		signUry = ConstantsCv.getParamValue(ConstantsCv.SIGN_URY);
		signPage = ConstantsCv.getParamValue(ConstantsCv.SIGN_PAGE);
		signFieldname = ConstantsCv.getParamValue(ConstantsCv.SIGN_FIELDNAME);
                
//                info.setSignText(signText);
//                info.setGraphicSignature(signImage);
//                info.setKeyFile(signFile);
//                info.setKeyPassword(signPassword);
//                info.setLocation(signLocation);
//                info.setLlx(Integer.valueOf(signLlx));
//                info.setLly(Integer.valueOf(signLly));
//                info.setUrx(Integer.valueOf(signUrx));
//                info.setUry(Integer.valueOf(signUry));
//                info.setSignPage(Integer.valueOf(signPage));
//                info.setFieldName(signFieldname);
//                info.setTypeDocument("Consulta");
//                info.setReason("Tramite #");
//
//                // firma digital
//                info.setDocument(file);
//                
//                try{
//                    
////                    System.out.println("Nombre" + info.getFieldName() + " texto " + info.getSignText());
////                    System.out.println("Imagen" + info.getGraphicSignature() + " Key " + info.getKeyFile());
////                    System.out.println("Location " + info.getLocation() + " page " + info.getSignPage());
////                    System.out.println("Documento " + info.getDocument());
//                    ByteArrayOutputStream  signedOs = digitalSignatureSvc.signDocument(info);
//                    System.out.println("Generar PDF 5"); 
//
//                    file = signedOs.toByteArray();
//
//                }catch(GeneralSecurityException | com.itextpdf.text.DocumentException e){
//                   	System.out.println("Errir de firma" + e);
//                }
                        
                        
                        
                        
			pdfTO.setFile(file);
//			showPdf(pdfTO);
			
                        
			// guardar boleta
			RugBoletaPdf boleta = new RugBoletaPdf();
			boleta.setIdTramite(new BigDecimal(tram.getTramiteIncomp().getIdTramiteTemp()));
			boleta.setArchivo(pdfTO.getFile());
			boleta.setPdfKey(pdfTO.getKey());
			boleta.setIdPersona(detalleTO.getIdUsuario());
			boleta.setFechaReg(Timestamp.valueOf(LocalDateTime.now()));
			boleta.setStatusReg("AC");
			pdfDao.save(boleta);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
                        System.out.println("error de firma 2");
			//e.printStackTrace();
			pdfTO.setFile(null);
		}
                
		          
		return pdfTO.getFile();
	}

	private void showPdf(PdfTO pdfTO, HttpServletResponse resp) {
		try {
			resp.setContentType("application/pdf");
			resp.setCharacterEncoding("UTF-8");
			resp.setHeader("Content-Disposition", "attachment; filename=\"Boleta.pdf\"");
			OutputStream os = resp.getOutputStream();
			os.write(pdfTO.getFile());
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getPersonaParte(Long idTramite, Long idGarantia, Long idParte) {
		
            
		List<VDetalleBoletaPartes> partes = null;
		StringBuffer sb = new StringBuffer();
		try {
			partes = partesBoletaDao.findByTramite(idTramite, idGarantia, idParte);
			
			Iterator<VDetalleBoletaPartes> it = partes.iterator();
			
			sb.append("<table class=\"striped\" >");
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<th>Nombre, Denominaci&oacute;n o Raz&oacute;n Social</th>");
			sb.append("<th>Identificador</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			VDetalleBoletaPartes person;
			while (it.hasNext()) {
				person = it.next();
				sb.append("<tr>");
				sb.append("<td>"	+ person.getNombre() + "</td>");
				sb.append("<td>"	+ (person.getPerJuridica().equalsIgnoreCase("PM")?getNotNullValue(person.getRfc()):getNotNullValue(person.getCurp())) + "</td>");
				sb.append("</tr>");
			}

			sb.append("</tbody>");
			sb.append("</table>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return sb.toString();
	}
	
	String getBienesParte(Long idTramite) {
		
		List<RugGarantiasBienesH> listaBienes = new ArrayList<RugGarantiasBienesH>();
		StringBuffer sb = new StringBuffer();
		
		try {
			listaBienes = bienesBoletaDao.findByTramite(idTramite);
			if(listaBienes.isEmpty()) return sb.toString();
			else sb.append("<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>");
			
			Iterator<RugGarantiasBienesH> it = listaBienes.iterator();
			
			sb.append("<table class=\"striped\">");
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
			
			RugGarantiasBienesH bienEspecialTO;
			while (it.hasNext()) {
				bienEspecialTO = it.next();
				sb.append("<tr>");
				sb.append("<td>"	+ changeTipoBien(bienEspecialTO.getTipoBienEspecial().intValue()) + "</td>");
				sb.append("<td>"	+ changeTipoId(bienEspecialTO.getTipoIdentificador().intValue()) + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getIdentificador() + "</td>");
//				sb.append("<td>"	+ bienEspecialTO.getSerie() + "</td>");
				sb.append("<td>"	+ bienEspecialTO.getDescripcionBien() + "</td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			sb.append("</table>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String getNotNullValue(String value){
		return value == null ? "" : value;
	}
	
	private String changeTipoBien(Integer tipoBien) {
		switch (tipoBien) {
			case 1: return "Vehiculo";
			case 2: return "Factura";
			default: return "Otro";
		}
	}
	
	private String changeTipoId(Integer tipoId) {
		switch (tipoId) {
			case 1: return "Placa";
			case 2: return "VIN";
			case 3: return "No. Factura";
			default: return "No. Serie";
		}
	}

}
