package mx.gob.se.rug.encuesta.action;

import org.apache.struts2.ServletActionContext;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.encuesta.dao.EncuestaDao;
import mx.gob.se.rug.fwk.action.RugBaseAction;

public class EncuestaAction extends RugBaseAction{

	private static final long serialVersionUID = 1L;
	
	private String registro;
	private String tiempo;
	private String proceso;
	private String facilidad;
	private String pagos;
	private String subusuarios;
	private String comentario;
	private String total;
	private String bToken;
	
	public EncuestaAction() {
		super();
	}
	
	public String crearEncuesta() {
		
		if(getbToken()!=null) {
			EncuestaDao encuestaDAO = new EncuestaDao();
			if(encuestaDAO.validarToken(getbToken())) {
				//TODO BIEN				
			} else {
				addActionError("El token ingresado no es valido.");
			}
		} else {
			addActionError("El token ingresado no es valido.");
		}
		
		return Constants.SUCCESS;
	}
	
	public String guardarEncuesta() {
		 EncuestaDao encuestaDAO = new EncuestaDao();
		 
		 encuestaDAO.insertarRespuesta(1L, registro);
		 encuestaDAO.insertarRespuesta(2L, tiempo);
		 encuestaDAO.insertarRespuesta(3L, proceso);
		 encuestaDAO.insertarRespuesta(4L, facilidad);
		 encuestaDAO.insertarRespuesta(5L, pagos);
		 encuestaDAO.insertarRespuesta(6L, subusuarios);
		 encuestaDAO.insertarRespuesta(7L, comentario);
		 encuestaDAO.insertarRespuesta(8L, total);
		 
		 System.out.println("token::"+this.parameters.get("bToken"));
		 String paramValue = ServletActionContext.getRequest().getParameter("bToken");
		 System.out.println("token::"+paramValue);
		 encuestaDAO.actualizarToken(getbToken());
		
		return Constants.SUCCESS;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getFacilidad() {
		return facilidad;
	}

	public void setFacilidad(String facilidad) {
		this.facilidad = facilidad;
	}

	public String getPagos() {
		return pagos;
	}

	public void setPagos(String pagos) {
		this.pagos = pagos;
	}

	public String getSubusuarios() {
		return subusuarios;
	}

	public void setSubusuarios(String subusuarios) {
		this.subusuarios = subusuarios;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getbToken() {
		return bToken;
	}

	public void setbToken(String bToken) {
		this.bToken = bToken;
	}
}
