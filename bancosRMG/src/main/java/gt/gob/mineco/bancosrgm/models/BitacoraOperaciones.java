package gt.gob.mineco.bancosrgm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "BITACORA_OPERACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BitacoraOperaciones {

    @Id
    @SequenceGenerator(name = "BITACORA_OPERACIONES_BITACORAID_GENERATOR", sequenceName = "SEQ_BITACORA_OPERACIONES", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BITACORA_OPERACIONES_BITACORAID_GENERATOR")
    @Column(name = "BITACORA_ID")
    private long bitacoraId;

    @Column(name = "FECHA")
    private Timestamp fecha;

    @Column(name = "OPERACION")
    private String operacion;

    @Column(name = "AGENCIA")
    private long agencia;

    @Lob
    @Column(name = "DETALLE")
    private String detalle;

    @Column(name="USUARIO_ID")
    private long usuarioId;
}
