package gt.gob.mineco.bancosrgm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "RUG_PERSONAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RugPersonas {

    @Id
    @Column(name="ID_PERSONA")
    private long idPersona;

    @Column(name="CODIGO_REGISTRO")
    private Long codigoRegistro;

    @Column(name="CURP_DOC")
    private String curpDoc;

    @Column(name="CVE_NACIONALIDAD")
    private String cveNacionalidad;

    @Column(name="E_MAIL")
    private String eMail;

    @Temporal(TemporalType.DATE)
    @Column(name="FECHA_INSCR_CC")
    private Date fechaInscrCc;

    @Temporal(TemporalType.DATE)
    @Column(name="FH_REGISTRO")
    private Date fhRegistro;

    @Column(name="FOLIO_MERCANTIL")
    private String folioMercantil;

    @Column(name="ID_DOMICILIO")
    private BigDecimal idDomicilio;

    @Column(name="ID_NACIONALIDAD")
    private BigDecimal idNacionalidad;

    @Column(name="ID_PERSONA_MODIFICAR")
    private BigDecimal idPersonaModificar;

    @Column(name="INSCRITO")
    private String inscrito;

    @Column(name="NIFP")
    private String nifp;

    @Column(name="PER_JURIDICA")
    private String perJuridica;

    @Column(name="PROCEDENCIA")
    private String procedencia;

    @Column(name="PROCESADO")
    private String procesado;

    @Column(name="REG_TERMINADO")
    private String regTerminado;

    @Column(name="RFC")
    private String rfc;

    @Column(name="SIT_PERSONA")
    private String sitPersona;
}
