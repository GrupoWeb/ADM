package gt.gob.rgm.adm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import gt.gob.rgm.adm.dao.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.model.RugGarantiasBienesH;
import gt.gob.rgm.adm.model.Tramites;
import gt.gob.rgm.adm.model.VDetalleBoletaNuevo;
import gt.gob.rgm.adm.model.VDetalleBoletaPartes;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.PageXofY;
import gt.gob.rgm.adm.util.PdfTO;
import gt.gob.rgm.adm.util.Random;
import gt.gob.rgm.adm.util.signatureInfo;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayInputStream;



public class RugSignatureFileImp implements RugSignatureFile{
    

    @Inject
    private TramitesRepository tramitesDao;

    @Inject
	private VDetalleBoletaNuevoRepository detalleBoletaDao;

    @Inject
	private RugCatTextoFormRepository rugCatTextoFormDao;

    @Inject
	private RugParametroConfRepository parametroDao;

    @Inject
	private VDetalleBoletaPartesRepository partesBoletaDao;

    @Inject
	private RugGarantiasBienesHRepository bienesBoletaDao;

	@Inject
	private UsuarioRepository usuarioDao;


//    public static final String SIGN_URL = 	 "http://128.5.101.19:8080/api/signature";
//    public static final String SIGN_BYTES =  "http://128.5.101.19:8080/api/toBytes";
//    public static final String SIGN_VERIFY = "http://128.5.101.19:8080/api/verifyFile";

	/*public static final String SIGN_URL = 	 "https://operaciones.rgm.gob.gt/api/signature";
	public static final String SIGN_BYTES =  "https://operaciones.rgm.gob.gt/api/toBytes";
	public static final String SIGN_VERIFY = "https://operaciones.rgm.gob.gt/api/verifyFile";*/
        
        public static final String SIGN_URL = 	 "https://qasistema.rgm.gob.gt/api/signature";
	public static final String SIGN_BYTES =  "https://qasistema.rgm.gob.gt/api/toBytes";
	public static final String SIGN_VERIFY = "https://qasistema.rgm.gob.gt/api/verifyFile";



    public ResponseRs signatureFiles(Integer idTramite, Integer idGarantia, String email){
        ResponseRs response = new ResponseRs();

        PdfTO pdfTO = new PdfTO();
        signatureInfo info = new signatureInfo();

        Tramites tramiteTemp = new Tramites();
        tramiteTemp = tramitesDao.findByIdTemp(Long.valueOf(idTramite));
        if(tramiteTemp == null){return null;}

        VDetalleBoletaNuevo detalleTO = new VDetalleBoletaNuevo();
        detalleTO = detalleBoletaDao.findByTramite(tramiteTemp.getIdTramite(), Long.valueOf(idGarantia));
        List<String> textos = rugCatTextoFormDao.findByIdTipoGarantia(detalleTO.getIdTipoGarantia().longValue());

        String anexo = textos.get(9) == null ? "" : textos.get(9);

        switch (detalleTO.getIdTipoTramite().intValue()) {
			case 1:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_INSCRIPCION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Inscripci\u00f3n " + anexo);
				info.setTypeDocument("Inscripci\u00f3n ");

				pdfTO.setValue("[*titulo_garantia*]", "INSCRIPCION DE LA GARANTIA MOBILIARIA");
				pdfTO.setValue("[*montoEstimado*]", "");

				break;
			case 31:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_INSCRIPCION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Inscripci\\u00f3n Traslado " + anexo);
				info.setTypeDocument("Inscripci\\u00f3n Traslado");
				break;
			case 7:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Modificaci\u00f3n");
				pdfTO.setValue("[*title*]", "MODIFICACI\u00d3N DE LA");
				info.setTypeDocument("Modificaci\u00f3n");
				break;
			case 4:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_CANCELACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Cancelaci\u00f3n");
				info.setTypeDocument("Cancelaci\u00f3n");
				break;
			case 9:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_RENOVACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Pr\u00f3rroga de vigencia");
				info.setTypeDocument("Pr\u00f3rroga de vigencia");
				break;
			case 30:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_EJECUCION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Ejecuci\u00f3n");
				info.setTypeDocument("Ejecuci\u00f3n");
				break;
			case 13:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Embargo");
				pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE EMBARGO DE LA");
				info.setTypeDocument("Anotaci\u00f3n de Embargo");
				break;
			case 17:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Anotaci\u00f3n de Levantamiento de Embargo");
				pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE LEVANTAMIENTO DE EMBARGO DE LA");
				info.setTypeDocument("Anotaci\u00f3n de Levantamiento de Embargo");
				break;
			case 21:
				pdfTO.setHtml(parametroDao.findByKey(Constants.HTML_MODIFICACION).getValorParametro());
				pdfTO.setValue("[*operacion*]", "Fin de Vigencia");
				pdfTO.setValue("[*title*]", "ANOTACI\u00d3N DE FIN DE VIGENCIA DE LA");
				info.setTypeDocument("Fin de Vigencia");
				break;
			case 16:
				pdfTO.setValue("[*titulo_garantia*]", "INSCRIPCION DE LA GARANTIA MOBILIARIA(LEASING)");

				String monto_maximo = detalleTO.getMontoMaximoGar();

				pdfTO.setValue("[*montoEstimado*]", monto_maximo);

				break;
			default:
				break;
		}

        pdfTO.setHtml("[*fechaCert*]", "");
		String server = parametroDao.findByKey(Constants.URL_SERVER).getValorParametro();

        pdfTO.setHtml("[*deudoresTable*]", "<span class=\"blue-text text-darken-2\">" + textos.get(1) + "</span>"
				+ getPersonaParte(detalleTO.getIdTramite().longValue(), detalleTO.getIdGarantia().longValue(), 2L));
		pdfTO.setHtml("[*acreedoresTable*]", "<span class=\"blue-text text-darken-2\">" + textos.get(2) + "</span>"
				+ getPersonaParte(detalleTO.getIdTramite().longValue(), detalleTO.getIdGarantia().longValue(), 3L));
		if (textos.get(3) != null && textos.get(3) != null && !textos.get(3).toString().equalsIgnoreCase("")) {
			pdfTO.setHtml("[*otorgantesTable*]", "<span class=\"blue-text text-darken-2\">" + textos.get(3) + "</span>"
					+ getPersonaParte(detalleTO.getIdTramite().longValue(), detalleTO.getIdTramite().longValue(), 1L));
		} else {
			pdfTO.setHtml("[*otorgantesTable*]", "");
		}
		pdfTO.setHtml("[*bienes*]",
				"<div class=\"input-field col s12\">"
						+ "<span class=\"blue-text text-darken-2\">" + textos.get(4) + "</span>"
						+ "<p>" + getNotNullValue(detalleTO.getDescGarantia()) + "</p></div>");
		pdfTO.setHtml("[*bienesTable*]", "<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>"
				+ getBienesParte(detalleTO.getIdTramite().longValue()));
		pdfTO.setHtml("[*infoContrato*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">" + textos.get(6) + "</span><p>"
				+ getNotNullValue(detalleTO.getInstrumentoPublico()) + "</p></div>");
		pdfTO.setHtml("[*observacionesAdicionales*]", "<div class=\"input-field col s12\">"
				+ "<span class=\"blue-text text-darken-2\">" + textos.get(8) + "</span><p>"
				+ getNotNullValue(detalleTO.getOtrosTerminosGarantia()) + "</p></div>");

        pdfTO.setValue("[*noGarantia*]", detalleTO.getIdGarantia().toString());
        pdfTO.setValue("[*vigencia*]", detalleTO.getVigencia().toString());

        if (detalleTO.getOtrosTerminosContrato() != null
                && !detalleTO.getOtrosTerminosContrato().equalsIgnoreCase("")) {
            pdfTO.setHtml("[*representantes*]", "<div class=\"input-field col s12\">"
                    + "<span class=\"blue-text text-darken-2\">Representante(s):</span><p>"
                    + getNotNullValue(detalleTO.getOtrosTerminosContrato()) + "</p></div>");
        } else {
            pdfTO.setValue("[*representantes*]", "");
        }

        pdfTO.setValue("[*fechaRegistro*]",
                detalleTO.getFechaCreacion().substring(0, detalleTO.getFechaCreacion().indexOf("*")));
        pdfTO.setValue("[*noOperacion*]", detalleTO.getIdTramite().toString());
        pdfTO.setValue("[*razonOperacion*]", getNotNullValue(detalleTO.getObservaciones()));

        pdfTO.setKey("" + tramiteTemp.getTramiteIncomp().getIdTramiteTemp() + Random.generateRandom(100000));

        try {
            byte file[] = null;
			byte filesSignature[] = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PdfWriter writer = new PdfWriter(os);
			ConverterProperties converterProperties = new ConverterProperties();
			PdfDocument pdf = new PdfDocument(writer);
			PageXofY footerHandler = new PageXofY(pdfTO.getKey(), server);
			pdf.addEventHandler(PdfDocumentEvent.START_PAGE, footerHandler);
			Document doc = HtmlConverter.convertToDocument(pdfTO.getHtml(), pdf, converterProperties);
			doc.close();
			file = os.toByteArray();

            String retorno = sendPDF(TimeStampFile(), true, file, email);
				while (true) {
					if(verifyFiles(retorno) == 1){
						filesSignature = getBytesFile(retorno);
						pdfTO.setFile(filesSignature);
						break;
					}	
				}
				pdfTO.setFile(file);

                response.setData(retorno);

        } catch (Exception e) {
            e.printStackTrace();
            pdfTO.setFile(null);
        }
        
        
        return response;
    }

	/**
	 * @param email
	 * @return
	 */
	@Override
	public Long getIdUsuarioByEmail(String email) {
		return usuarioDao.findByEmail(email).getUsuarioId();
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
				sb.append("<td>" + person.getNombre() + "</td>");
				sb.append("<td>" + (person.getPerJuridica().equalsIgnoreCase("PM") ? getNotNullValue(person.getRfc())
						: getNotNullValue(person.getCurp())) + "</td>");
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

    public static String getNotNullValue(String value) {
		return value == null ? "" : value;
	}
    

    private String changeTipoBien(Integer tipoBien) {
		switch (tipoBien) {
			case 1:
				return "Vehiculo";
			case 2:
				return "Factura";
			default:
				return "Otro";
		}
	}


    private String changeTipoId(Integer tipoId) {
		switch (tipoId) {
			case 1:
				return "Placa";
			case 2:
				return "VIN";
			case 3:
				return "No. Factura";
			default:
				return "No. Serie";
		}
	}
    
    String getBienesParte(Long idTramite) {

		List<RugGarantiasBienesH> listaBienes = new ArrayList<RugGarantiasBienesH>();
		StringBuffer sb = new StringBuffer();

		try {
			listaBienes = bienesBoletaDao.findByTramite(idTramite);
			if (listaBienes.isEmpty())
				return sb.toString();
			else
				sb.append("<span class=\"blue-text text-darken-2\">Lista de Bienes Especiales:</span>");

			Iterator<RugGarantiasBienesH> it = listaBienes.iterator();

			sb.append("<table class=\"striped\">");
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<th>Tipo Bien Especial</th>");
			sb.append("<th>Tipo Identificador</th>");
			sb.append("<th>Identificador</th>");
			// sb.append("<th>Serie</th>");
			sb.append("<th>Descripcion</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");

			RugGarantiasBienesH bienEspecialTO;
			while (it.hasNext()) {
				bienEspecialTO = it.next();
				sb.append("<tr>");
				sb.append("<td>" + changeTipoBien(bienEspecialTO.getTipoBienEspecial().intValue()) + "</td>");
				sb.append("<td>" + changeTipoId(bienEspecialTO.getTipoIdentificador().intValue()) + "</td>");
				sb.append("<td>" + bienEspecialTO.getIdentificador() + "</td>");
				// sb.append("<td>" + bienEspecialTO.getSerie() + "</td>");
				sb.append("<td>" + bienEspecialTO.getDescripcionBien() + "</td>");
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


	public String TimeStampFile()
	{
		Date date = new Date();
		DateFormat hourDateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
		String history = hourDateFormat.format(date);
		return history;
	}

	public Integer countPage(byte[] files) throws IOException {
		PDDocument doc = null;
		doc = PDDocument.load(files);
		return doc.getNumberOfPages();
	}
    
    private String sendPDF(String pIdGarantia, boolean local, byte[] files, String email) throws IOException{


        HttpClient httpClient = new DefaultHttpClient();



		try{
			HttpPost httppost = new HttpPost(SIGN_URL);
			String fileName = pIdGarantia;

			String pageNumber = Integer.toString(countPage(files));


			ContentBody cd = new InputStreamBody(new ByteArrayInputStream(files), "files.pdf");
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", cd);
			reqEntity.addPart("name", new StringBody(fileName));
			reqEntity.addPart("page", new StringBody(pageNumber));
			reqEntity.addPart("user", new StringBody(getIdUsuarioByEmail(email).toString()));
			reqEntity.addPart("email", new StringBody(email));


			httppost.setEntity(reqEntity);


			HttpResponse response = httpClient.execute(httppost);
			String data = new BasicResponseHandler().handleResponse(response);
			System.out.println("Respuesta FIRMA " + data);
			return fileName;
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
    }

    private byte[] getBytesFile(String pIdGarantia) throws IOException {

		HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(SIGN_BYTES);
		String fileName = pIdGarantia;


        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("garantia", new StringBody(fileName));
        httpPost.setEntity(reqEntity);

        HttpResponse response = httpClient.execute(httpPost);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        response.getEntity().writeTo(baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    private Integer verifyFiles(String pIdGarantia) throws IOException{


		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(SIGN_VERIFY);
		String fileName = pIdGarantia;
        Integer very = 0;


        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("garantia", new StringBody(fileName));
        httpPost.setEntity(reqEntity);

        HttpResponse response = httpClient.execute(httpPost);
        String data = new BasicResponseHandler().handleResponse(response);
		if(data.equals("true"))
		{
			very = new Integer(1);
			return very;
		}else{
			very = new Integer(0);
			return very;

		}

    }
}