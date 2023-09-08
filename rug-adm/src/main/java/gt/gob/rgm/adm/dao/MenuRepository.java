package gt.gob.rgm.adm.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gt.gob.rgm.adm.domain.Menu;
import gt.gob.rgm.adm.model.Menus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.Expression;
@ApplicationScoped
public class MenuRepository {

    @PersistenceContext
    private EntityManager em;

  

    public List<Menus> findWithFilter(Integer page, Integer size) {
        //String select = "  SELECT * FROM V_REP_LISTADO_USUARIOS ";
          //String select ="SELECT * FROM MENUS";
           String select = "  SELECT *\n" +
            "FROM (\n" +
            "    SELECT t.*, ROWNUM AS rn\n" +
            "    FROM (\n" +
            "        SELECT *\n" +
            "        FROM MENUS";
         String where = "";
         String pagina = "";
         String sql = "";
         
          if (page != null) {
            pagina =" WHERE ROWNUM <="+(page * size)+" ) WHERE rn >"+(page - 1) * size;       
        }else{
            pagina = ")";
        }
         if(where!=""){
             
              sql = select + " WHERE "+ where+"  ) t"  +pagina;
         }else{
              sql = select +" order by NOMMENU, ID ) t"  +pagina;
         }
            
         System.out.println("QUERY: "+ sql);
         
         
         List<Object[]> results = em.createNativeQuery(sql).getResultList();
         List<Menus> menus = new ArrayList<>();
         for (Object[] row  : results) 
         {
              Menus menu = new Menus();
              menu.setMenuId(((BigDecimal) row[4]).intValue());
              menu.setNomMenu((String) row[0]);
              menu.setNomSubMenu((String) row[1]);
              menu.setRol((String) row[2]);
              menu.setLink((String) row[3]);
             
              
                //garantia.setIdGarantia(((BigDecimal) row[0]).intValue());
              menus.add(menu); 
         }
         return menus;
    }
    public List<Menus> updateWithFilter(Integer page, Integer size, Menu filtro) {
        
        Menus menu = em.find(Menus.class, filtro.getMenuId());

        if (menu != null) {
            System.out.println("Encuentra el menu");
            menu.setRol(filtro.getRol());
            em.merge(menu);
        } else {
            // Manejar el caso en el que el menú no se encontró por el ID
                        System.out.println("No Encuentra el menu");
        }
     
         return findWithFilter(page,size);
    }
public List<Menus> crearWithFilter(Integer page, Integer size, Menu filtro) {
    Boolean op = false;
    try { 
       String jpql = "SELECT m FROM Menus m WHERE m.nomMenu = :nombre and m.nomSubMenu IS NULL";
       Menus menu = em.createQuery(jpql, Menus.class)
               .setParameter("nombre", filtro.getNomMenu())
               
               .getSingleResult();
       op = true;
    } catch (NoResultException ex) {
        op = false;
    }

    System.out.println("MENU");
    System.out.println(op);
         if (op == false) {
               Menus nuevoMenu = new Menus();
               nuevoMenu.setNomMenu(filtro.getNomMenu());
               try {
                    em.persist(nuevoMenu); // Insertar la nueva entidad en la base de datos

                } catch (Exception e) {
                    System.out.println("ERROR");
                    // Manejar excepciones
                }
         }
        Menus nuevoMenu = new Menus();
        nuevoMenu.setNomMenu(filtro.getNomMenu());
        nuevoMenu.setNomSubMenu(filtro.getNomSubMenu());
        nuevoMenu.setRol(filtro.getRol());
        nuevoMenu.setLink(filtro.getLink());        

        try {
            em.persist(nuevoMenu); // Insertar la nueva entidad en la base de datos
            
        } catch (Exception e) {
            System.out.println("ERROR");
            // Manejar excepciones
        }
     
         return findWithFilter(page,size);
    }


    public List<Menus> delCWithFilter(Menu filtro) {
        String jpql = "SELECT m FROM Menus m WHERE m.nomMenu = :nombre";
        String originalText = filtro.getNomMenu();
        String textToRemove = " (Cabecera de Menú)";

        String result = originalText.replace(textToRemove, "");
        List<Menus> menusToDelete = em.createQuery(jpql, Menus.class)
                                     .setParameter("nombre", result)
                                     .getResultList();
      
        for (Menus menu : menusToDelete) {
            em.remove(menu);
        }
        return findWithFilter(null,null);
    }
    public List<Menus> delWithFilter(Menu filtro) {
    Menus menu = em.find(Menus.class, filtro.getMenuId());

        if (menu != null) {
            System.out.println("Encuentra el menu");
          /*  menu.setRol(filtro.getRol());
            em.merge(menu);*/
            em.remove(menu);
        } else {
            // Manejar el caso en el que el menú no se encontró por el ID
                        System.out.println("No Encuentra el menu");
        }
     
         return findWithFilter(null,null);
    }
    public Long countWithFilter() {
       CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Menus> listusuarios = criteria.from(Menus.class);
        Predicate restrictions = null;
     
      
        criteria = criteria.select(cb.construct(Long.class,
                cb.count(listusuarios.get("menuId"))
        ));
        if (restrictions != null) {
            criteria = criteria.where(restrictions);
        }
        return em.createQuery(criteria).getSingleResult();
    }

    
}
