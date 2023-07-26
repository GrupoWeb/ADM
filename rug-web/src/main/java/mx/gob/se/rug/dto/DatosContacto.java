package mx.gob.se.rug.dto;

import mx.gob.economia.dgi.framework.base.dto.AbstractBaseDTO;

/**
 * 
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public class DatosContacto extends AbstractBaseDTO {
	
	private Integer idTramite;
	private Integer idStatus;
	private Telefono telefonoParticular;
	private Telefono telefonoMovilPersonal;
	private Telefono telefonoOficina;
	private String emailPersonal;
	private Telefono telefonoConmutador;
	private String conmutador;
	private Telefono telefonoparticular;
	private Telefono movilparticular;
	private Telefono telefonooficina;
	private String emailpersonal;
	private String idContactos;
	private String mensagesContactos;
	private Telefono fax;
	
	private String isDatosCompletos;
	
	/**
	 * @return the telefonoParticular
	 */
	public Telefono getTelefonoParticular() {
		return telefonoParticular;
	}

	/**
	 * @param telefonoParticular
	 *            the telefonoParticular to set
	 */
	public void setTelefonoParticular(Telefono telefonoParticular) {
		this.telefonoParticular = telefonoParticular;
	}

	/**
	 * @return the telefonoMovilPersonal
	 */
	public Telefono getTelefonoMovilPersonal() {
		return telefonoMovilPersonal;
	}

	/**
	 * @param telefonoMovilPersonal
	 *            the telefonoMovilPersonal to set
	 */
	public void setTelefonoMovilPersonal(Telefono telefonoMovilPersonal) {
		this.telefonoMovilPersonal = telefonoMovilPersonal;
	}

	/**
	 * @return the telefonoOficina
	 */
	public Telefono getTelefonoOficina() {
		return telefonoOficina;
	}

	/**
	 * @param telefonoOficina
	 *            the telefonoOficina to set
	 */
	public void setTelefonoOficina(Telefono telefonoOficina) {
		this.telefonoOficina = telefonoOficina;
	}

	/**
	 * @return the emailPersonal
	 */
	public String getEmailPersonal() {
		return emailPersonal;
	}

	/**
	 * @param emailPersonal
	 *            the emailPersonal to set
	 */
	public void setEmailPersonal(String emailPersonal) {
		this.emailPersonal = emailPersonal;
	}

	/**
	 * @return the telefonoConmutador
	 */
	public Telefono getTelefonoConmutador() {
		return telefonoConmutador;
	}

	/**
	 * @param telefonoConmutador
	 *            the telefonoConmutador to set
	 */
	public void setTelefonoConmutador(Telefono telefonoConmutador) {
		this.telefonoConmutador = telefonoConmutador;
	}

	/**
	 * @return the conmutador
	 */
	public String getConmutador() {
		return conmutador;
	}

	/**
	 * @param conmutador
	 *            the conmutador to set
	 */
	public void setConmutador(String conmutador) {
		this.conmutador = conmutador;
	}

	/**
	 * @return the telefonoparticular
	 */
	public Telefono getTelefonoparticular() {
		return telefonoparticular;
	}

	/**
	 * @param telefonoparticular
	 *            the telefonoparticular to set
	 */
	public void setTelefonoparticular(Telefono telefonoparticular) {
		this.telefonoparticular = telefonoparticular;
	}

	/**
	 * @return the movilparticular
	 */
	public Telefono getMovilparticular() {
		return movilparticular;
	}

	/**
	 * @param movilparticular
	 *            the movilparticular to set
	 */
	public void setMovilparticular(Telefono movilparticular) {
		this.movilparticular = movilparticular;
	}

	/**
	 * @return the telefonooficina
	 */
	public Telefono getTelefonooficina() {
		return telefonooficina;
	}

	/**
	 * @param telefonooficina
	 *            the telefonooficina to set
	 */
	public void setTelefonooficina(Telefono telefonooficina) {
		this.telefonooficina = telefonooficina;
	}

	/**
	 * @return the emailpersonal
	 */
	public String getEmailpersonal() {
		return emailpersonal;
	}

	/**
	 * @param emailpersonal
	 *            the emailpersonal to set
	 */
	public void setEmailpersonal(String emailpersonal) {
		this.emailpersonal = emailpersonal;
	}

	/**
	 * @return the idContactos
	 */
	public String getIdContactos() {
		return idContactos;
	}

	/**
	 * @param idContactos
	 *            the idContactos to set
	 */
	public void setIdContactos(String idContactos) {
		this.idContactos = idContactos;
	}

	/**
	 * @return the mensagesContactos
	 */
	public String getMensagesContactos() {
		return mensagesContactos;
	}

	/**
	 * @param mensagesContactos
	 *            the mensagesContactos to set
	 */
	public void setMensagesContactos(String mensagesContactos) {
		this.mensagesContactos = mensagesContactos;
	}

	public void setIsDatosCompletos(String isDatosCompletos) {
		this.isDatosCompletos = isDatosCompletos;
	}

	public String getIsDatosCompletos() {
		return isDatosCompletos;
	}

	public void setFax(Telefono fax) {
		this.fax = fax;
	}

	public Telefono getFax() {
		return fax;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Integer getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}

	
}

