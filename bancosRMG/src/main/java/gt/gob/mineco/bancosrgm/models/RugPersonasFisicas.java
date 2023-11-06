package gt.gob.mineco.bancosrgm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "RUG_PERSONAS_FISICAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RugPersonasFisicas {

    @Id
    @Column(name="ID_PERSONA")
    private long idPersona;

    @Column(name="AP_MATERNO")
    private String apMaterno;

    @Column(name="AP_PATERNO")
    private String apPaterno;

    @Column(name="CURP")
    private String curp;

    @Column(name="CVE_ESCOLARIDAD")
    private String cveEscolaridad;

    @Column(name="CVE_ESTADO_NACIM")
    private String cveEstadoNacim;

    @Column(name="CVE_MUN_DEL_NACIM")
    private BigDecimal cveMunDelNacim;

    @Column(name="CVE_PAIS_NACIM")
    private String cvePaisNacim;

    @Column(name="EDAD")
    private String edad;

    @Column(name="ESTADO_CIVIL")
    private String estadoCivil;

    @Temporal(TemporalType.DATE)
    @Column(name="F_NACIMIENTO")
    private Date fNacimiento;

    @Column(name="FOLIO_DOCTO_MIGRAT")
    private String folioDoctoMigrat;

    @Column(name="ID_CALIDAD_MIGRAT")
    private BigDecimal idCalidadMigrat;

    @Column(name="ID_PAIS_NACIM")
    private BigDecimal idPaisNacim;

    @Column(name="LUGAR_NAC_PERS_EXT")
    private String lugarNacPersExt;

    @Column(name="NOMBRE_PERSONA")
    private String nombrePersona;

    @Column(name="NUM_SERIE")
    private String numSerie;

    @Column(name="OCUPACION_ACTUAL")
    private String ocupacionActual;

    @Column(name="SEXO")
    private String sexo;
}
