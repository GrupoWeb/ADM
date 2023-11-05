package gt.gob.mineco.bancosrgm.dao;

import gt.gob.mineco.bancosrgm.models.RugPersonas;
import gt.gob.mineco.bancosrgm.models.RugPersonasFisicas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<RugPersonas, Integer> {

    @Query(value = "SELECT t FROM RugPersonasFisicas t WHERE t.idPersona = :idPersona")
    RugPersonasFisicas findByIdPersona(long idPersona);
}
