package mx.gob.se.rug.boletas.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import gt.gob.rgm.util.ExcelCreator;
//import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.detallegarantia.to.DetalleTO;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.inscripcion.service.InscripcionService;
import mx.gob.se.rug.mailservice.MailRegistroService;
import mx.gob.se.rug.to.AccionTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class BoletasAction extends RugBaseAction implements ServletRequestAware{

	private static final long serialVersionUID = 1L;
	
	List<BoletaPagoTO> listaBoletas;
	List<BoletaPagoTO> listaBoletasSinFirma;
	List<AccionTO> listaTramitesR;
	
	private String mdNumeroBoleta;
	private String mdCantidad;
	private Double mdSaldo;
	private String mdBanco;
	private String mdPago;	
	
	private File   upload;
    private String uploadFileName;
    private String uploadContentType;
    private Boolean cuentaMaestra;
    
    private Integer idPersona;
    
    private String garantias;
    private String operacion;
    private String fecha;
    
    private String fechaInicial;
    private String fechaFinal;
    private String filtro;
    
    private MailRegistroService mailRegistroService;
    private AcreedoresCatalogosService acreedoresCatalogosService;
	
	private InscripcionService inscripcionService;
	
	public String cargaPagina() {
		String regresa = Constants.FAILED;		
		
		// construccion de uri
		HttpServletRequest request = ServletActionContext.getRequest();

		StringBuffer uri = request.getRequestURL();
		int posicion = uri.indexOf("/", 7);

		try {
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
			
			
			//setListaBoletas(inscripcionService.getBoletasByUsuario(usuario.getPersona().getIdPersona(), 1,0,5));
			//setListaBoletasSinFirma(inscripcionService.getBoletasByUsuario(usuario.getPersona().getIdPersona(), 0,0,5));
			//setListaTramitesR(inscripcionService.getTramitesEfectuados(usuario.getPersona().getIdPersona(),0,5));
			
			// setMdSaldo retorna Double 
			setMdSaldo(inscripcionService.getValorSaldoUsuario(new Integer(usuario.getPersona().getIdPersona()).toString()));
			
			setCuentaMaestra(Boolean.valueOf(sessionMap.get(mx.gob.se.rug.common.constants.Constants.MASTER_USER).toString()));
			
			setIdPersona(usuario.getPersona().getIdPersona());

            			
			regresa = Constants.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return regresa;
	}
	
	public String registrarBoleta() {
		String regresa = "failed";
		try {			
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
																						
			BoletaPagoTO boletaTO = new BoletaPagoTO();
			boletaTO.setBanco(getMdBanco());
			boletaTO.setFormaPago(getMdPago());
			boletaTO.setIdGarantia(1);
			boletaTO.setIdTipoGarantia(1);
			boletaTO.setSerie("A");
			boletaTO.setNumero(getMdNumeroBoleta());
			boletaTO.setMonto(new Double(getMdCantidad()));
			boletaTO.setIdPersonaCarga(usuario.getPersona().getIdPersona());
			try {
				boletaTO.setBoletaBytes(getBytesFromFile(upload));
			} catch (Exception e) {
				boletaTO.setBoletaBytes(new byte[] {});
			}
			
			boolean noHayBoleta = inscripcionService.findBoleta(boletaTO);
			if(!noHayBoleta) {
				addActionError("La boleta con No. " + boletaTO.getNumero() + " ya se encuentra registrada.");				
			} else  {			
				boolean sePudo = inscripcionService.registrarBoleta(boletaTO);
				if (sePudo) {
					PersonaFisica personaFisica = acreedoresCatalogosService.getRegistro(usuario.getNombre());
					//mailRegistroService.sendMailBoletaAprobar(personaFisica, boletaTO);					
					regresa = "success";				
				}else{
					throw new Exception("RUG-RegistraBoleta:::no se pudo realizar la operacion.");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Ocurrio un error en el ingreso de la boleta, intente de nuevo.");			
		}

		return regresa;
	}
		
	public String validoQr() throws Exception {
								
		String idTramiteAct= ServletActionContext.getRequest().getParameter(Constants.ID_TRAMITE);
		try {
			
			DetalleDAO detalleDAO = new DetalleDAO(); 
			BoletaDAO boletaDAO = new BoletaDAO();
			
			/*TODO
			 * agregar primero que valide que no sea una certificacion
			 */
			
			Integer idTramiteCert =	boletaDAO.getIdTramitebyIdTramiteNuevo(new Integer(idTramiteAct));
			
			List<DetalleTO> detalles = detalleDAO.getTramitesFinalizados(idTramiteCert);
			StringBuffer garantias = new StringBuffer();		
			int i = 0;
						
			addActionMessage("Boleta Electronica Valida.");
			if(detalles!=null && detalles.size()>0) {
				for(DetalleTO detalleTO:detalles) {
					if(i!=0) garantias.append(",");
					garantias.append(detalleTO.getIdgarantia());					
					i++;
				}
				setGarantias(garantias.toString());
				setOperacion(detalles.get(0).getDescripcionTramite() + (detalles.size()>1?" Masivas":""));
				setFecha(detalles.get(0).getDescripcionGarantia());
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			addActionMessage("Boleta Electronica Valida.");
		}
		
		return SUCCESS;
	}
	
	public String invalidoQr() throws Exception {		
		addActionError("Boleta Electronica NO valida");		
		return SUCCESS;
	}
	
	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
	
	public String exportExcel() throws Exception {
		// parametros fechaInicial y fechaFinal
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		JsonObject jsonFiltro = Json.createObjectBuilder()
				.add("nombre", filtro)
				.add("fechaInicial", fechaInicial)
				.add("fechaFinal", fechaFinal)
				.build();

				MyLogger.Logger.log(Level.INFO, "EXPORTAR A EXCEL DATA: " + jsonFiltro);
		List<AccionTO> tramites = inscripcionService.getTramitesEfectuadosOptimizado(usuario.getPersona().getIdPersona(),jsonFiltro, 0,50000);
		ExcelCreator excelCreator = new ExcelCreator();
		XSSFWorkbook workbook = excelCreator.createTramitesWorkbook(tramites);
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=Tramites.xlsx");
		ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
		workbook.write(out);
		out.flush();
		out.close();
		return null;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public String getMdNumeroBoleta() {
		return mdNumeroBoleta;
	}

	public void setMdNumeroBoleta(String mdNumeroBoleta) {
		this.mdNumeroBoleta = mdNumeroBoleta;
	}

	public String getMdCantidad() {
		return mdCantidad;
	}

	public void setMdCantidad(String mdCantidad) {
		this.mdCantidad = mdCantidad;
	}

	public void setInscripcionService(InscripcionService inscripcionService) {
		this.inscripcionService = inscripcionService;
	}

	public List<BoletaPagoTO> getListaBoletas() {
		return listaBoletas;
	}

	public void setListaBoletas(List<BoletaPagoTO> listaBoletas) {
		this.listaBoletas = listaBoletas;
	}

	public List<BoletaPagoTO> getListaBoletasSinFirma() {
		return listaBoletasSinFirma;
	}

	public void setListaBoletasSinFirma(List<BoletaPagoTO> listaBoletasSinFirma) {
		this.listaBoletasSinFirma = listaBoletasSinFirma;
	}

	public Double getMdSaldo() {
		return mdSaldo;
	}

	public void setMdSaldo(Double mdSaldo) {
		this.mdSaldo = mdSaldo;
	}

	public List<AccionTO> getListaTramitesR() {
		return listaTramitesR;
	}

	public void setListaTramitesR(List<AccionTO> listaTramitesR) {
		this.listaTramitesR = listaTramitesR;
	}

	public void setUpload(File upload){
        this.upload=upload;
    }
    public void setUploadContentType(String uploadContentType){
        this.uploadContentType=uploadContentType;
    }
    public void setUploadFileName(String uploadFileName){
        this.uploadFileName=uploadFileName;
    }

	public String getMdBanco() {
		return mdBanco;
	}

	public void setMdBanco(String mdBanco) {
		this.mdBanco = mdBanco;
	}

	public String getMdPago() {
		return mdPago;
	}

	public void setMdPago(String mdPago) {
		this.mdPago = mdPago;
	}

	public MailRegistroService getMailRegistroService() {
		return mailRegistroService;
	}

	public void setMailRegistroService(MailRegistroService mailRegistroService) {
		this.mailRegistroService = mailRegistroService;
	}

	public AcreedoresCatalogosService getAcreedoresCatalogosService() {
		return acreedoresCatalogosService;
	}

	public void setAcreedoresCatalogosService(AcreedoresCatalogosService acreedoresCatalogosService) {
		this.acreedoresCatalogosService = acreedoresCatalogosService;
	}

	public Boolean getCuentaMaestra() {
		return cuentaMaestra;
	}

	public void setCuentaMaestra(Boolean cuentaMaestra) {
		this.cuentaMaestra = cuentaMaestra;
	}

	public void setMdError(String mdError) {
		sessionMap.put("errorBoleta", mdError);
	}

	public String getGarantias() {
		return garantias;
	}

	public void setGarantias(String garantias) {
		this.garantias = garantias;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
}
