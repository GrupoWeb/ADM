package gt.gob.mineco.bancosrgm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_CODIGO_PERSONAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VCodigoPersonas {

    @Id
    @Column(name="ID_PERSONA")
    private long idPersona;

    @Column(name="CODIGO")
    private String codigo;

    @Column(name="CURP_DOC")
    private String curpDoc;

    @Column(name="PER_JURIDICA")
    private String perJuridica;

    @Column(name="RFC")
    private String rfc;
}
