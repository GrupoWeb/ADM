package gt.gob.mineco.bancosrgm.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;

@Entity
@Table(name = "BOLETA", schema = "RUG_UTIL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "UpdateSaldoBoletas", procedureName = "RUG.SP_SUMAR_SALDO", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "costo", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "usuario", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "flag", type = Integer.class),
        })
})
public class Boleta {

    @Id
    @SequenceGenerator(name="BOLETA_BOLETAID_GENERATOR", sequenceName="SEQ_BOLETA", schema="RUG_UTIL", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BOLETA_BOLETAID_GENERATOR")
    private Long id;

    @Column(name="AGENCIA")
    private String agencia;

    @Column(name="CODIGO_TRAMITE")
    private Integer codigoTramite;

    @Column(name="FECHA_HORA")
    private Timestamp fechaHora;

    @Column(name="ID_GARANTIA")
    private BigDecimal idGarantia;

    @Column(name="ID_TRANSACCION")
    private String idTransaccion;

    @Column(name="IDENTIFICADOR")
    private String identificador;

    @Column(name="MONTO")
    private BigDecimal monto;

    @Column(name="MONTO_OTROS_BANCOS")
    private BigDecimal montoOtrosBancos;

    @Column(name="NUMERO")
    private String numero;

    @Column(name="RESOLUCION")
    private String resolucion;

    @Column(name="SERIE")
    private String serie;

    @Column(name="USADA")
    private Integer usada;

    @Column(name="USUARIO")
    private String usuario;

    @Column(name="TIPO_PAGO")
    private String tipoPago;

}
