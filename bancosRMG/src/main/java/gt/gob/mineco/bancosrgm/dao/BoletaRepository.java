package gt.gob.mineco.bancosrgm.dao;

import gt.gob.mineco.bancosrgm.models.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public interface BoletaRepository extends JpaRepository<Boleta, Integer> {

    @Query(value = "SELECT b.saldo FROM Boleta b WHERE b.persona = :persona AND b.usada = 1",nativeQuery = true)
    BigDecimal findSaldo(@Param("persona") String pIdUsuario);

    @Query("SELECT b FROM Boleta b WHERE b.usada = :status ORDER BY b.fechaHora ASC")
    List<Boleta> findByStatusOrderedByFecha(@Param("status") Integer status);

    @Query(
            value = "SELECT * FROM RUG_UTIL.BOLETA b WHERE " +
                    " b.NUMERO = TO_CHAR(:pId) " +
                    "AND (b.USADA = 0 OR b.USADA = 1)",
            nativeQuery = true
    )
    Boleta findByIdActivasNumero(@Param("pId") Long pId);

    @Query(
            value = "SELECT * FROM Boleta b WHERE " +
                    "b.ID_TRANSACCION = TO_CHAR(:pId) " +
                    "AND (b.USADA = 0 OR b.USADA = 1)",
            nativeQuery = true
    )
    Boleta findByIdActivasTransaccion(@Param("pId") Long pId);

    @Query("SELECT b FROM Boleta b WHERE b.idTransaccion = :idTransaccion AND " +
            "(b.serie IS NULL OR b.serie = :serie) AND b.numero = :numero AND " +
            "b.monto = :monto AND (b.usada = 0 OR b.usada = 1)")
    Boleta findByBoleta(@Param("idTransaccion") String idTransaccion, @Param("serie") String serie,
                        @Param("numero") String numero, @Param("monto") BigDecimal monto);


    @Transactional
    @Procedure(name = "UpdateSaldoBoletas")
    List<Object []> UpdateSaldoBoletas(@Param("costo") Integer costo,
                               @Param("usuario") Integer usuario,
                               @Param("flag") Integer flag);
}
