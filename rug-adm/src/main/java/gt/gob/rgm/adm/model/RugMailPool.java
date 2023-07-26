package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_MAIL_POOL database table.
 * 
 */
@Entity
@Table(name="RUG_MAIL_POOL")
@NamedQuery(name="RugMailPool.findAll", query="SELECT r FROM RugMailPool r")
public class RugMailPool implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_MAIL")
	private long idMail;

	private String asunto;

	private String destinatario;

	@Column(name="DESTINATARIO_CC")
	private String destinatarioCc;

	@Column(name="DESTINATARIO_CCO")
	private String destinatarioCco;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_ENVIO")
	private Date fechaEnvio;

	@Column(name="ID_MAIL_ACCOUNT")
	private Long idMailAccount;

	@Column(name="ID_STATUS_MAIL")
	private Long idStatusMail;

	@Column(name="ID_TIPO_CORREO")
	private Long idTipoCorreo;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String mensaje;

	public RugMailPool() {
	}

	public long getIdMail() {
		return this.idMail;
	}

	public void setIdMail(long idMail) {
		this.idMail = idMail;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getDestinatario() {
		return this.destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getDestinatarioCc() {
		return this.destinatarioCc;
	}

	public void setDestinatarioCc(String destinatarioCc) {
		this.destinatarioCc = destinatarioCc;
	}

	public String getDestinatarioCco() {
		return this.destinatarioCco;
	}

	public void setDestinatarioCco(String destinatarioCco) {
		this.destinatarioCco = destinatarioCco;
	}

	public Date getFechaEnvio() {
		return this.fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Long getIdMailAccount() {
		return this.idMailAccount;
	}

	public void setIdMailAccount(Long idMailAccount) {
		this.idMailAccount = idMailAccount;
	}

	public Long getIdStatusMail() {
		return this.idStatusMail;
	}

	public void setIdStatusMail(Long idStatusMail) {
		this.idStatusMail = idStatusMail;
	}

	public Long getIdTipoCorreo() {
		return this.idTipoCorreo;
	}

	public void setIdTipoCorreo(Long idTipoCorreo) {
		this.idTipoCorreo = idTipoCorreo;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}