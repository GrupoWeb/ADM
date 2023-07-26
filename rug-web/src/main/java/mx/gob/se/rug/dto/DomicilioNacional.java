package mx.gob.se.rug.dto;


/**
 * 
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public class DomicilioNacional extends Domicilio {
	private String estado;
	private String municipio;
	private String calle;
	private String numeroExterior;
	private String numeroInterior;
	private int idColonia;
	private String colonia;
	private String calleColindanteUno;
	private String calleColindanteDos;
	private String callePosterior;
	private String localidad;
	private int idVialidad;
	private String puntoReferencia;
	private int tipoInmueble;
	private String caracteristicas;
	private String detalle;

	public DomicilioNacional() {
		super();
		setPais("M�xico");
	}

	/**
	 * 
	 * @param idDomicilio
	 */
	public DomicilioNacional(String idDomicilio) {
		this();
		setIdDomicilio(idDomicilio);
	}

	/**
	 * 
	 * @param idDomicilio
	 * @param tipoDomicilio
	 */
	public DomicilioNacional(String idDomicilio, String tipoDomicilio) {
		this(idDomicilio);
		setTipoDomicilio(tipoDomicilio);
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio
	 *            the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return the calle
	 */
	public String getCalle() {
		return calle;
	}

	/**
	 * @param calle
	 *            the calle to set
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}

	/**
	 * @return the numeroExterior
	 */
	public String getNumeroExterior() {
		return numeroExterior;
	}

	/**
	 * @param numeroExterior
	 *            the numeroExterior to set
	 */
	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}

	/**
	 * @return the numeroInterior
	 */
	public String getNumeroInterior() {
		return numeroInterior;
	}

	/**
	 * @param numeroInterior
	 *            the numeroInterior to set
	 */
	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}

	/**
	 * @return the idColonia
	 */
	public int getIdColonia() {
		return idColonia;
	}

	/**
	 * @param idColonia
	 *            the idColonia to set
	 */
	public void setIdColonia(int idColonia) {
		this.idColonia = idColonia;
	}

	/**
	 * @return the colonia
	 */
	public String getColonia() {
		return colonia;
	}

	/**
	 * @param colonia
	 *            the colonia to set
	 */
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	/**
	 * @return the calleColindanteUno
	 */
	public String getCalleColindanteUno() {
		return calleColindanteUno;
	}

	/**
	 * @param calleColindanteUno
	 *            the calleColindanteUno to set
	 */
	public void setCalleColindanteUno(String calleColindanteUno) {
		this.calleColindanteUno = calleColindanteUno;
	}

	/**
	 * @return the calleColindanteDos
	 */
	public String getCalleColindanteDos() {
		return calleColindanteDos;
	}

	/**
	 * @param calleColindanteDos
	 *            the calleColindanteDos to set
	 */
	public void setCalleColindanteDos(String calleColindanteDos) {
		this.calleColindanteDos = calleColindanteDos;
	}

	/**
	 * @return the callePosterior
	 */
	public String getCallePosterior() {
		return callePosterior;
	}

	/**
	 * @param callePosterior
	 *            the callePosterior to set
	 */
	public void setCallePosterior(String callePosterior) {
		this.callePosterior = callePosterior;
	}

	/**
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}

	/**
	 * @param localidad
	 *            the localidad to set
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	/**
	 * @return the idVialidad
	 */
	public int getIdVialidad() {
		return idVialidad;
	}

	/**
	 * @param idVialidad
	 *            the idVialidad to set
	 */
	public void setIdVialidad(int idVialidad) {
		this.idVialidad = idVialidad;
	}

	/**
	 * @return the puntoReferencia
	 */
	public String getPuntoReferencia() {
		return puntoReferencia;
	}

	/**
	 * @param puntoReferencia
	 *            the puntoReferencia to set
	 */
	public void setPuntoReferencia(String puntoReferencia) {
		this.puntoReferencia = puntoReferencia;
	}

	/**
	 * @return the tipoInmueble
	 */
	public int getTipoInmueble() {
		return tipoInmueble;
	}

	/**
	 * @param tipoInmueble
	 *            the tipoInmueble to set
	 */
	public void setTipoInmueble(int tipoInmueble) {
		this.tipoInmueble = tipoInmueble;
	}

	/**
	 * @return the caracteristicas
	 */
	public String getCaracteristicas() {
		return caracteristicas;
	}

	/**
	 * @param caracteristicas
	 *            the caracteristicas to set
	 */
	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getDetalle() {
		return detalle;
	}

}

