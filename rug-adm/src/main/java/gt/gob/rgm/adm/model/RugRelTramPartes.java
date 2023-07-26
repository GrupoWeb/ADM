package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the RUG_REL_TRAM_PARTES database table.
 * 
 */
@Entity
@Table(name="RUG_REL_TRAM_PARTES")
@NamedQuery(name="RugRelTramPartes.findAll", query="SELECT r FROM RugRelTramPartes r")
public class RugRelTramPartes implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RugRelTramPartesPK id;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	@Column(name="STATUS_REG")
	private String statusReg;
	
	/*@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_TRAMITE")
	private Tramites tramite;*/
	
	/*@ManyToOne
	@JoinColumn(name="ID_PERSONA", insertable=false, updatable=false)
	private RugSecuUsuario usuario;*/
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ID_PERSONA", referencedColumnName="ID_PERSONA", insertable=false, updatable=false),
		@JoinColumn(name="ID_TRAMITE", referencedColumnName="ID_TRAMITE", insertable=false, updatable=false),
		@JoinColumn(name="ID_PARTE", referencedColumnName="ID_PARTE", insertable=false, updatable=false)
	})
	private RugPersonasH personaH;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ID_PERSONA", referencedColumnName="ID_PERSONA", insertable=false, updatable=false)
	})
	private RugPersonas persona;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ID_PERSONA", referencedColumnName="ID_PERSONA", insertable=false, updatable=false)
	})
	private RugPersonasFisicas personaFisica;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ID_PERSONA", referencedColumnName="ID_PERSONA", insertable=false, updatable=false)
	})
	private RugPersonasMorales personaMoral;

	public RugRelTramPartes() {
	}

	public RugRelTramPartesPK getId() {
		return this.id;
	}

	public void setId(RugRelTramPartesPK id) {
		this.id = id;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public String getPerJuridica() {
		return this.perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public RugPersonasH getPersonaH() {
		return personaH;
	}

	public void setPersonaH(RugPersonasH personaH) {
		this.personaH = personaH;
	}

	public RugPersonas getPersona() {
		return persona;
	}

	public void setPersona(RugPersonas persona) {
		this.persona = persona;
	}

	public RugPersonasFisicas getPersonaFisica() {
		return personaFisica;
	}

	public void setPersonaFisica(RugPersonasFisicas personaFisica) {
		this.personaFisica = personaFisica;
	}

	public RugPersonasMorales getPersonaMoral() {
		return personaMoral;
	}

	public void setPersonaMoral(RugPersonasMorales personaMoral) {
		this.personaMoral = personaMoral;
	}

	/*public Tramites getTramite() {
		return tramite;
	}

	public void setTramite(Tramites tramite) {
		this.tramite = tramite;
	}*/
}