package gt.gob.rgm.adm.domain;

import java.io.Serializable;
import java.util.List;

public class ExternalUser implements Serializable {
	private Long personaId;
	private String email;
	private String name;
	private String docId;
	private String nit;
	private String status;
	private String registered;
	private String registryCode;
	private List<ExternalUserFile> files;
	private String cause;
	private Boolean migration;
	private Long correosNoEnviados;
	private Long correosError;
	private String respuesta;
	private Integer nationality;
	private String personType;
	private String securityQuestion;
	private String securityAnswer;
	private String password;
	private String parentEmail;
	private String isJudicial;
	private Integer groupId;
	private String address;
	private String legalInscription;
	private String representativeInfo;
	
	public ExternalUser() {
	}

	public Long getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Long personaId) {
		this.personaId = personaId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public String getRegistryCode() {
		return registryCode;
	}

	public void setRegistryCode(String registryCode) {
		this.registryCode = registryCode;
	}

	public List<ExternalUserFile> getFiles() {
		return files;
	}

	public void setFiles(List<ExternalUserFile> files) {
		this.files = files;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Boolean getMigration() {
		return migration;
	}

	public void setMigration(Boolean migration) {
		this.migration = migration;
	}

	public Long getCorreosNoEnviados() {
		return correosNoEnviados;
	}

	public void setCorreosNoEnviados(Long correosNoEnviados) {
		this.correosNoEnviados = correosNoEnviados;
	}

	public Long getCorreosError() {
		return correosError;
	}

	public void setCorreosError(Long correosError) {
		this.correosError = correosError;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Integer getNationality() {
		return nationality;
	}

	public void setNationality(Integer nationality) {
		this.nationality = nationality;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}

	public String getIsJudicial() {
		return isJudicial;
	}

	public void setIsJudicial(String isJudicial) {
		this.isJudicial = isJudicial;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLegalInscription() {
		return legalInscription;
	}

	public void setLegalInscription(String legalInscription) {
		this.legalInscription = legalInscription;
	}

	public String getRepresentativeInfo() {
		return representativeInfo;
	}

	public void setRepresentativeInfo(String representativeInfo) {
		this.representativeInfo = representativeInfo;
	}
}
