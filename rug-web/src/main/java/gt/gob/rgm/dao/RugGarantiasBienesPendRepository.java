package gt.gob.rgm.dao;

import java.util.List;

import gt.gob.rgm.util.FormTramites;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gt.gob.rgm.model.RugGarantiasBienesPend;

public interface RugGarantiasBienesPendRepository extends CrudRepository<RugGarantiasBienesPend, Integer>{

    @Query(value="SELECT p.tipoBienEspecial as Codigo FROM RugGarantiasBienesPend p WHERE p.idTramiteTemp = :idTramiteTemp", nativeQuery = false)
    public List<RugGarantiasBienesPend> findByTramiteTemp(@Param("idTramiteTemp") Integer idTramiteTemp);



    @Query(value="SELECT ID_ULTIMO_TRAMITE FROM RUG_GARANTIAS WHERE ID_GARANTIA = :idGarantia", nativeQuery = true)
    public List<String> getBienesByGarantia(@Param("idGarantia") Integer idGarantia);


    @Query(value="SELECT TIPO_BIEN_ESPECIAL FROM RUG_GARANTIAS_BIENES WHERE ID_TRAMITE = :idTramite", nativeQuery = true)
    public List<String> getBienesTramite(@Param("idTramite") Integer idTramite);


    @Query(value="SELECT COUNT(*) AS FACTURAS FROM RUG_GARANTIAS_BIENES_PEND WHERE ID_TRAMITE_TEMP = :idTramite and TIPO_BIEN_ESPECIAL = 2", nativeQuery = true)
    public List<String> getCountFacByTramite(@Param("idTramite") Integer IdTramite);

    @Query(value="SELECT ID_TRAMITE_TEMP FROM V_TRAMITES_TERMINADOS WHERE ID_GARANTIA = :idGarantia ORDER BY ID_TRAMITE_TEMP DESC", nativeQuery = true)
    public List<String> getCountFacturaByGarantia(@Param("idGarantia") Integer idGarantia);


    
}



