package mx.gob.se.rug.constants;

/**
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
public enum TipoPersona {
	FISICA("PF"), MORAL("PM");

	private TipoPersona(String id) {
		this.id = id;
	}

	private String id;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}

