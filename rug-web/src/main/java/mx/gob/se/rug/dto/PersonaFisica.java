package mx.gob.se.rug.dto;

import java.util.Date;

import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.constants.TipoProcedencia;

@SuppressWarnings("serial")
public class PersonaFisica extends Persona {
	
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private Date fechaNacimiento;
	private String sexo;
	private String estadoCivil;
	private String ocupacion;
	private Integer idPais;
	private Domicilio lugarNacimiento;
	private Identificacion identificacion;
	private Acreditacion acreditacion;
	private Procedencia procedencia;
	private String bRepresentante;
	private GrupoPerfilTO grupo;
	private String claveUsuarioPadre;
	private String situacion;
	private String perfil;
	private String descripcionPerfil;
	private String claveAcreedor;
	private String nSerieCert;
	private String token;
	private String documentoIdentificacion;
	private String tipoPersona;
	private String nacionalidadInscrito;

	public PersonaFisica() {
		super();
	}
	
	public void setExtranjero(Extranjero extranjero){
		procedencia=extranjero;
	}
	public Extranjero getExtranjero(){
		return (Extranjero)procedencia;
	}
	
	public void setNacional(Nacional nacional){
		procedencia=nacional;
	}
	
	public Nacional getNacional(){
		return (Nacional)procedencia;
	}
	
	public DomicilioNacional getLugarNacimientoNacional(){
		if(lugarNacimiento instanceof DomicilioNacional){
			return (DomicilioNacional)lugarNacimiento;
		}
		return null;
	}
	
	public void setLugarNacimientoNacional(DomicilioNacional lugarNacimiento){
		this.lugarNacimiento=lugarNacimiento;
	}
	
	/**
	 * @param nombre
	 * @param apellidoPaterno
	 * @param apellidoMaterno
	 */
	public PersonaFisica(String nombre, String apellidoPaterno,
			String apellidoMaterno) {
		
		setNombre(nombre);
		setApellidoPaterno(apellidoPaterno);
		setApellidoMaterno(apellidoMaterno);
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre != null ? nombre.trim() : "";
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apellidoPaterno
	 */
	public String getApellidoPaterno() {
		return apellidoPaterno != null ? apellidoPaterno.trim() : "";
	}

	/**
	 * @param apellidoPaterno
	 *            the apellidoPaterno to set
	 */
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	/**
	 * @return the apellidoMaterno
	 */
	public String getApellidoMaterno() {
		return apellidoMaterno != null ? apellidoMaterno.trim() : "";
	}

	/**
	 * @param apellidoMaterno
	 *            the apellidoMaterno to set
	 */
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento
	 *            the fechaNacimiento to set
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo
	 *            the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the estadoCivil
	 */
	public String getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil
	 *            the estadoCivil to set
	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return the ocupacion
	 */
	public String getOcupacion() {
		return ocupacion;
	}

	/**
	 * @param ocupacion
	 *            the ocupacion to set
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * @return the lugarNacimiento
	 */
	public Domicilio getLugarNacimiento() {
		return lugarNacimiento;
	}

	/**
	 * @param lugarNacimiento
	 *            the lugarNacimiento to set
	 */
	public void setLugarNacimiento(final Domicilio lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	/**
	 * @return the procedencia
	 */
	public Procedencia getProcedencia() {
		return procedencia;
	}

	/**
	 * @param procedencia
	 *            the procedencia to set
	 */
	public final void setProcedencia(final Procedencia procedencia) {
		if (procedencia != null) {
			if (procedencia instanceof Nacional) {
				setIdProcedencia(TipoProcedencia.NACIONAL.getId());
			} else if (procedencia instanceof Extranjero) {
				setIdProcedencia(TipoProcedencia.EXTRANJERO.getId());
			} else {
				throw new IllegalArgumentException(
						"La procedencia de la persona solo puede ser Nacional o Extranjera.");
			}
		}
		this.procedencia = procedencia;
	}

	public String getNombreCompleto() {
		String nom = getNombre();
		String ap = getApellidoPaterno();
		String am = getApellidoMaterno();

		return (nom + " " + ap + " " + am).trim();
	}

	public void setIdentificacion(Identificacion identificacion) {
		this.identificacion = identificacion;
	}

	public Identificacion getIdentificacion() {
		return identificacion;
	}

	public void setAcreditacion(Acreditacion acreditacion) {
		this.acreditacion = acreditacion;
	}

	public Acreditacion getAcreditacion() {
		return acreditacion;
	}
	

	public void setBRepresentante(String bRepresentante) {
		this.bRepresentante = bRepresentante;
	}

	public String getBRepresentante() {
		return bRepresentante;
	}

	public void setGrupo(GrupoPerfilTO grupo) {
		this.grupo = grupo;
	}

	public GrupoPerfilTO getGrupo() {
		return grupo;
	}

	public void setClaveUsuarioPadre(String claveUsuarioPadre) {
		this.claveUsuarioPadre = claveUsuarioPadre;
	}

	public String getClaveUsuarioPadre() {
		return claveUsuarioPadre;
	}

	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public String getSituacion() {
		return situacion;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setClaveAcreedor(String claveAcreedor) {
		this.claveAcreedor = claveAcreedor;
	}

	public String getClaveAcreedor() {
		return claveAcreedor;
	}

	public void setDescripcionPerfil(String descripcionPerfil) {
		this.descripcionPerfil = descripcionPerfil;
	}

	public String getDescripcionPerfil() {
		return descripcionPerfil;
	}

	public Integer getIdPais() {
		return idPais;
	}

	public void setIdPais(Integer idPais) {
		this.idPais = idPais;
	}

	public String getnSerieCert() {
		return nSerieCert;
	}

	public void setnSerieCert(String nSerieCert) {
		this.nSerieCert = nSerieCert;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDocumentoIdentificacion() {
		return documentoIdentificacion;
	}

	public void setDocumentoIdentificacion(String documentoIdentificacion) {
		this.documentoIdentificacion = documentoIdentificacion;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getNacionalidadInscrito() {
		return nacionalidadInscrito;
	}

	public void setNacionalidadInscrito(String nacionalidadInscrito) {
		this.nacionalidadInscrito = nacionalidadInscrito;
	}	
}
