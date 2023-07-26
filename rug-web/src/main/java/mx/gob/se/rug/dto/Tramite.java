
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
public abstract class Tramite extends AbstractBaseDTO {
	private String id;
	private String tipo;
	private Tramite parent;
	private String idConsecutivo;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the parent
	 */
	public Tramite getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Tramite parent) {
		this.parent = parent;
	}

	public void setIdConsecutivo(String idConsecutivo) {
		this.idConsecutivo = idConsecutivo;
	}

	public String getIdConsecutivo() {
		return idConsecutivo;
	}

}
