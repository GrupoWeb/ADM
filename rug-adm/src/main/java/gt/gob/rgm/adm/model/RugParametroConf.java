package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RUG_PARAMETRO_CONF database table.
 * 
 */
@Entity
@Table(name="RUG_PARAMETRO_CONF")
@NamedQuery(name="RugParametroConf.findAll", query="SELECT r FROM RugParametroConf r")
public class RugParametroConf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CVE_PARAMETRO")
	private String cveParametro;

	@Lob
	@Column(name="VALOR_PARAMETRO")
	private String valorParametro;

	public RugParametroConf() {
	}

	public String getCveParametro() {
		return this.cveParametro;
	}

	public void setCveParametro(String cveParametro) {
		this.cveParametro = cveParametro;
	}

	public String getValorParametro() {
		return this.valorParametro;
	}

	public void setValorParametro(String valorParametro) {
		this.valorParametro = valorParametro;
	}

}