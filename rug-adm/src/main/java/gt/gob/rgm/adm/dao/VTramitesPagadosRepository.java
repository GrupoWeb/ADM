package gt.gob.rgm.adm.dao;


import gt.gob.rgm.adm.domain.Tramites;
import gt.gob.rgm.adm.model.RugPersonasFisicas;
import gt.gob.rgm.adm.model.VCodigoPersonas;
import gt.gob.rgm.adm.model.VTramitesPagados;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VTramitesPagadosRepository {

    @PersistenceContext
    private EntityManager em;

    public VCodigoPersonas findCodigo(String codigo){
        try{
            this.em.getEntityManagerFactory().getCache().evict(VCodigoPersonas.class);
            TypedQuery<VCodigoPersonas> query = em.createNamedQuery("VCodigoPersona.findByUser", VCodigoPersonas.class).setParameter("Codigo",codigo);
            return query.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    public List<VTramitesPagados> findTramiteByUser(Tramites tramite){
        VCodigoPersonas codigoPersona = findCodigo(tramite.getIdUsuario());


        String sql = "SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE ID_PERSONA = "+ String.valueOf(codigoPersona.getIdPersona()) +" \n" +
                "    UNION \n" +
                "        SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE CVE_USUARIO_PADRE = (SELECT CVE_USUARIO_PADRE FROM RUG_SECU_USUARIOS WHERE ID_PERSONA = "+ String.valueOf(codigoPersona.getIdPersona()) +" )\n" +
                "    UNION\n" +
                "        SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE CVE_USUARIO = (SELECT CVE_USUARIO_PADRE FROM RUG_SECU_USUARIOS WHERE ID_PERSONA = "+ String.valueOf(codigoPersona.getIdPersona()) +")\n" +
                "    UNION\n" +
                "        SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE CVE_USUARIO_PADRE = (SELECT CVE_USUARIO FROM RUG_SECU_USUARIOS WHERE ID_PERSONA = "+ String.valueOf(codigoPersona.getIdPersona()) + " )";

        conectiondB bd = new conectiondB();
        Connection connection = null;
        PreparedStatement psTramites = null;
        ResultSet rsTramites = null;
        List<String> lista = new ArrayList<>();
        try{
            connection = bd.getConnection();
            psTramites = connection.prepareStatement(sql);
            rsTramites = psTramites.executeQuery();

            while(rsTramites.next()){
                lista.add(rsTramites.getString("ID_PERSONA"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Lista" + lista);


        //                            cb.equal(tramites.get("idPersonaLogin"), codigoPersona.getIdPersona())
//        .where(tramites.get("idPersonaLogin").in(lista))

        try{
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VTramitesPagados> criteria = cb.createQuery(VTramitesPagados.class);
            Root<VTramitesPagados> tramites = criteria.from(VTramitesPagados.class);
            criteria.select(tramites).where(tramites.get("idPersonaLogin").in(lista))
                    .orderBy(cb.desc(tramites.get("idTramite")));
            return em.createQuery(criteria).getResultList();
        }catch(Exception e){
            return null;
        }

    }

    public List<RugPersonasFisicas> getUser(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RugPersonasFisicas> criteria = cb.createQuery(RugPersonasFisicas.class);
        Root<RugPersonasFisicas> usuarios = criteria.from(RugPersonasFisicas.class);
        criteria.select(usuarios).orderBy(cb.asc(usuarios.get("idPersona")));

        return em.createQuery(criteria).getResultList();
    }
}
