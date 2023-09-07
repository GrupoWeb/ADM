package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the MENUS database table.
 * 
 */
@Entity
@NamedQuery(name="Roles.findAll", query="SELECT u FROM Roles u")
public class Roles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
        @SequenceGenerator(name="ROL_ROLID_GENERATOR", sequenceName="SEQ_ROL", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROL_ROLID_GENERATOR")
	@Column(name="ID")
	private Integer rolId;
        
        @Column(name="NOMBRE")
	private String nomRol;

        @Column(name="TIPO")
	private String tipoRol;
        
    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getNomRol() {
        return nomRol;
    }

    public void setNomRol(String nomRol) {
        this.nomRol = nomRol;
    }

    public String getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(String tipoRol) {
        this.tipoRol = tipoRol;
    }
        
        
        	
	public Roles() {
	}
   	
}
