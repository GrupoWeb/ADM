package mx.gob.se.firma;

import javax.ejb.Stateless;

import mx.gob.se.exception.InfrastructureException;
import mx.gob.se.firma.co.ClienteCadenaOriginal;
import mx.gob.se.firma.co.to.MessageResponse;
import mx.gob.se.firma.co.to.ResponseWs;
import mx.gob.se.firma.to.FirmasTO;

/**
 * Session Bean implementation class FooterAcuseBean
 */
@Stateless(mappedName = "ejb/FooterAcuseBeanJNDI")
public class FooterAcuseBean implements FooterAcuseBeanRemote, FooterAcuseBeanLocal {

	    private int status = 999;
	    private String descStatus = "";
	    private String certSolicitante;
	    private String certNSerieSolicitante;
	    private String cadenaOriginal;
	    private String cmsSolicitante;
	    private String certSE;
	    private String certNSerieSE;
	    private String cadenaOriginalSE;
	    private String cmsSE;
	    private String tspFecha;
	    private String tspNSerie;
	    private String tspCertificado;
	    private String tspEmisor;
	    private String datosEstampillados;
	    
	    private FirmasTO firmasTO;
	    private MessageResponse messageResponse;
	    
	    
	    

	    public MessageResponse getMessageResponse() {
			return messageResponse;
		}

		public void setMessageResponse(MessageResponse messageResponse) {
			this.messageResponse = messageResponse;
		}

		public FirmasTO getFirmasTO() {
			return firmasTO;
		}

		public void setFirmasTO(FirmasTO firmasTO) {
			this.firmasTO = firmasTO;
		}

		public int getStatus() {
	        return status;
	    }

	    public String getDescStatus() {
	        return descStatus;
	    }

	    public String getCadenaOriginal() {
	        return cadenaOriginal;
	    }

	    public String getCadenaOriginalSE() {
	        return cadenaOriginalSE;
	    }

	    public String getCertNSerieSE() {
	        return certNSerieSE;
	    }

	    public String getCertNSerieSolicitante() {
	        return certNSerieSolicitante;
	    }

	    public String getCertSE() {
	        return certSE;
	    }

	    public String getCertSolicitante() {
	        return certSolicitante;
	    }

	    public String getCmsSE() {
	        return cmsSE;
	    }

	    public String getCmsSolicitante() {
	        return cmsSolicitante;
	    }

	    public String getDatosEstampillados() {
	        return datosEstampillados;
	    }

	    public String getTspCertificado() {
	        return tspCertificado;
	    }

	    public String getTspEmisor() {
	        return tspEmisor;
	    }

	    public String getTspFecha() {
	        return tspFecha;
	    }

	    public String getTspNSerie() {
	        return tspNSerie;
	    }

//	    public void FooterAcuseInit(String sIdTramite, String sUrlWebserviceFirmaTramites) {
//	        try {	        		        	
//	            URL urlWebservice = getURLWebserviceFooterAcuse(sIdTramite, sUrlWebserviceFirmaTramites);
//	            
//	            //Invoca el servicio
//	            InputStreamReader inputStream = new InputStreamReader(urlWebservice.openStream(), "ISO-8859-1");	            
//	            BufferedReader br = new BufferedReader(inputStream);
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    if (sb.length() > 0) {
//                        sb.append("\n");
//                    }
//                    sb.append(line);
//                }
//                String sXMLRespuesta = new String(sb.toString().getBytes("ISO-8859-1"), "ISO-8859-1");
//                
//                System.out.println("-------XMLsXMLRespuesta Inicia");
//                System.out.println(sXMLRespuesta);
//                System.out.println("-------XMLsXMLRespuesta Termina");
//	            
//	        	String sUrlXsdServicio = sUrlWebserviceFirmaTramites + "/xsd/tramites-rug-footer-acuse.xsd";
//	            Document document = getRespuestaFooterAcuse(sXMLRespuesta, sUrlXsdServicio);
//	            XPath xpath = XPathFactory.newInstance().newXPath();
//	            status = ((Double) xpath.evaluate("/rug-footer-acuse/@status", document, XPathConstants.NUMBER)).intValue();
//                System.out.println("status="+status);
//	            descStatus = (String) xpath.evaluate("/rug-footer-acuse/@desc-status", document, XPathConstants.STRING);
//                System.out.println("descStatus="+descStatus);
//                System.out.println(sXMLRespuesta);
//	            if(status==0) {
//	            	cadenaOriginal = (String) xpath.evaluate("/rug-footer-acuse/solicitante/@cadena-original", document, XPathConstants.STRING);
//	                System.out.println("cadenaOriginal="+cadenaOriginal);
//	            	cmsSolicitante = (String) xpath.evaluate("/rug-footer-acuse/solicitante/@cms", document, XPathConstants.STRING);
//	                System.out.println("cmsSolicitante="+cmsSolicitante);
//	            	certSolicitante = (String) xpath.evaluate("/rug-footer-acuse/solicitante/@certificado", document, XPathConstants.STRING);
//	                System.out.println("certSolicitante="+certSolicitante);
//	            	certNSerieSolicitante = (String) xpath.evaluate("/rug-footer-acuse/solicitante/@cert-nserie", document, XPathConstants.STRING);
//	                System.out.println("certNSerieSolicitante="+certNSerieSolicitante);
//
//	            	cadenaOriginalSE = (String) xpath.evaluate("/rug-footer-acuse/se/@cadena-original", document, XPathConstants.STRING);
//	                System.out.println("cadenaOriginalSE="+cadenaOriginalSE);
//	            	cmsSE = (String) xpath.evaluate("/rug-footer-acuse/se/@cms", document, XPathConstants.STRING);
//	                System.out.println("cmsSE="+cmsSE);
//	            	certSE = (String) xpath.evaluate("/rug-footer-acuse/se/@certificado", document, XPathConstants.STRING);
//	                System.out.println("certSE="+certSE);
//	            	certNSerieSE = (String) xpath.evaluate("/rug-footer-acuse/se/@cert-nserie", document, XPathConstants.STRING);
//	                System.out.println("certNSerieSE="+certNSerieSE);
//	            }
//	        } catch (XPathExpressionException ex) {
//	        	status = 10;
//	        	descStatus = "Error al buscar un nodo: " + ex.getMessage();
//	        } catch (SAXException ex) {
//	        	status = 20;
//	        	descStatus = ex.getMessage();
//			} catch (MalformedURLException e) {
//	        	status = 40;
//	        	descStatus = "La dirección del servicio es incorrecta";
//	        } catch (Exception ex) {
//	        	ex.printStackTrace();
//	        	status = 30;
//	        	descStatus = ex.getMessage();
//			}
//	    }

//	    public void FooterCertificacionInit(String sUsuario, String sIdTramite, String sContenido, String sUrlWebserviceFirmaTramites) {
//	        try {	        		        	
//	            URL urlWebservice = getURLWebserviceFooterCertificacion(sUsuario, sIdTramite, sUrlWebserviceFirmaTramites);
//	            
//                String sXMLRespuesta = ClientWebServiceRestful.callRestfulWebService(urlWebservice, sContenido);
//                
//                System.out.println("-------XMLsXMLRespuesta Inicia");
//                System.out.println(sXMLRespuesta);
//                System.out.println("-------XMLsXMLRespuesta Termina");
//	            
//	        	String sUrlXsdServicio = sUrlWebserviceFirmaTramites + "/xsd/tramites-rug-footer-acuse.xsd";
//	            Document document = getRespuestaFooterAcuse(sXMLRespuesta, sUrlXsdServicio);
//	            XPath xpath = XPathFactory.newInstance().newXPath();
//	            status = ((Double) xpath.evaluate("/rug-footer-acuse/@status", document, XPathConstants.NUMBER)).intValue();
//                System.out.println("status="+status);
//	            descStatus = (String) xpath.evaluate("/rug-footer-acuse/@desc-status", document, XPathConstants.STRING);
//                System.out.println("descStatus="+descStatus);
//                System.out.println(sXMLRespuesta);
//	            if(status==0) {
//	            	certSolicitante = (String) xpath.evaluate("/rug-footer-acuse/solicitante/@certificado", document, XPathConstants.STRING);
//	                System.out.println("certSolicitante="+certSolicitante);
//
//	                cadenaOriginalSE = (String) xpath.evaluate("/rug-footer-acuse/se/@cadena-original", document, XPathConstants.STRING);
//	                System.out.println("cadenaOriginalSE="+cadenaOriginalSE);
//	            	cmsSE = (String) xpath.evaluate("/rug-footer-acuse/se/@cms", document, XPathConstants.STRING);
//	                System.out.println("cmsSE="+cmsSE);
//	            	certSE = (String) xpath.evaluate("/rug-footer-acuse/se/@certificado", document, XPathConstants.STRING);
//	                System.out.println("certSE="+certSE);
//	            	certNSerieSE = (String) xpath.evaluate("/rug-footer-acuse/se/@cert-nserie", document, XPathConstants.STRING);
//	                System.out.println("certNSerieSE="+certNSerieSE);
//	            }
//	        } catch (XPathExpressionException ex) {
//	        	status = 10;
//	        	descStatus = "Error al buscar un nodo: " + ex.getMessage();
//	        } catch (SAXException ex) {
//	        	status = 20;
//	        	descStatus = ex.getMessage();
//			} catch (MalformedURLException e) {
//	        	status = 40;
//	        	descStatus = "La dirección del servicio es incorrecta";
//	        } catch (Exception ex) {
//	        	status = 30;
//	        	descStatus = ex.getMessage();
//			}
//	    }
//
//	    private static URL buildURL(URL url, Map<String, String> parameters) throws Exception {
//	        try {
//	            StringBuilder sb = new StringBuilder();
//	            for (Map.Entry<String, String> entry : parameters.entrySet()) {
//	                String value = URLEncoder.encode(entry.getValue(), "UTF-8");
//	                sb.append("/").append(value);
//	            }
//	            return new URL(url.toString() + sb.toString());
//	        } catch (Exception ex) {
//                throw new Exception("No se pudo contruir la URL con los siguientes parametros: ["+parameters+"]");
//	        }
//	    }	    
//
//	    private URL getURLWebserviceFooterCertificacion(String sUsuario, String sIdTramite, String sUrlWebserviceFirmaTramites) throws Exception, MalformedURLException {
//	    	Map<String, String> parametrosURL = new HashMap<String, String>();
//	        parametrosURL.put("sUsuario", sUsuario);
//	        parametrosURL.put("sIdTramite", sIdTramite);
//	        URL urlWebserviceFirmaTramites = new URL(sUrlWebserviceFirmaTramites + "/resources/getCertificacion");
//	        return buildURL(urlWebserviceFirmaTramites, parametrosURL);
//	    }
//	    	    
//	    private URL getURLWebserviceFooterAcuse(String sIdTramite, String sUrlWebserviceFirmaTramites) throws Exception, MalformedURLException {
//	    	Map<String, String> parametrosURL = new HashMap<String, String>();
//	        parametrosURL.put("sIdTramite", sIdTramite);
//	        URL urlWebserviceFirmaTramites = new URL(sUrlWebserviceFirmaTramites + "/resources/getInfoAcuse");
//	        return buildURL(urlWebserviceFirmaTramites, parametrosURL);
//	    }
//	    
//	    private static Document getRespuestaFooterAcuse(String sXMLRespuesta, String sUrlXsdServicio) throws Exception, SAXException {
//	        {
//	            InputStream inputStream = null;
//	            try {
//	                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//	                URL url = new URL(sUrlXsdServicio);
//	                Schema schema = factory.newSchema(url);
//	                Validator validator = schema.newValidator();
//	                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//	                inputStream = new ByteArrayInputStream(sXMLRespuesta.getBytes("ISO-8859-1"));
//	                Document document = documentBuilder.parse(inputStream);
//	                validator.validate(new DOMSource(document));
//	                return document;
//	            } catch (MalformedURLException ex) {
//	                throw new Exception("No se pudo acceder al XSD: " + sUrlXsdServicio);
//	            } catch (UnsupportedEncodingException ex) {
//	                throw new Exception("La respuesta del XML no tiene el encoding apropiado." + ex.getMessage(), ex);
//	            } catch (ParserConfigurationException ex) {
//	                throw new Exception("Formato del XML de respuesta del servicio erroneo." + ex.getMessage(), ex);
//	            } catch (IOException ex) {
//	                throw new Exception("No se pudo acceder al XML: " + ex.getMessage(), ex);
//	            } finally {
//	                try {
//	                    if (inputStream != null) {
//	                        inputStream.close();
//	                    }
//	                } catch (IOException ex) {
//	                    throw new Exception("No se pudo cerrar el inputStream: " + ex.getMessage(), ex);
//	                }
//	            }
//	        }
//	    }

	    
	    public ResponseWs signCentral(String cadenaOriginalUsu,String certB64Usu, String firmaB64Usu, String xmlCO,Integer idTramite,Integer idUsuario  ){
	    	FirmaCentral central= new FirmaCentral();
			return central.signCentral(cadenaOriginalUsu, certB64Usu, firmaB64Usu,xmlCO, idTramite, idUsuario);
	    }
	    
	    public String getCO(String urlBase,Integer idUsuario, Integer idTramite,Integer idSolicitud){
	    	return new ClienteCadenaOriginal().getCadenaOriginal ( urlBase, idUsuario,  idTramite, idSolicitud);
	    }
	    
	    public String getDocXML(String urlBase,Integer idUsuario, Integer idTramite,Integer idSolicitud){
	    	return new ClienteCadenaOriginal().getDocumentoXML ( urlBase, idUsuario,  idTramite, idSolicitud);
	    }

		public void getFirmaHistorico(Integer idTramiteTemporal){
			FirmaCentral central= new FirmaCentral();
			try {
				setFirmasTO(central.getFirmaHistorico(idTramiteTemporal));
			} catch (InfrastructureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		    
	    public void doCertificacion(String cadenaOriginal,Integer idTramiteTemp,Integer idUsuario){
	    	FirmaCentral central= new FirmaCentral();
	    	setMessageResponse(central.doCertificacion(cadenaOriginal, idTramiteTemp, idUsuario));
	    }
	    
}
