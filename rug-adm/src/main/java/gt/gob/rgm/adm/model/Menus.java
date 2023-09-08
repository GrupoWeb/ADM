package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the MENUS database table.
 * 
 */
@Entity
@NamedQuery(name="Menus.findAll", query="SELECT u FROM Menus u")
public class Menus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
        @SequenceGenerator(name="MENU_MENUID_GENERATOR", sequenceName="SEQ_MENU", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MENU_MENUID_GENERATOR")
	@Column(name="ID")
	private Integer menuId;
        
        @Column(name="NOMMENU")
	private String nomMenu;
        
        @Column(name="NOMSUBMENU")
	private String nomSubMenu;
        
        @Column(name="ROL")
	private String rol;
        
        @Column(name="LINK")
	private String link;

	
	public Menus() {
	}

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getNomMenu() {
        return nomMenu;
    }

    public void setNomMenu(String nomMenu) {
        this.nomMenu = nomMenu;
    }

    public String getNomSubMenu() {
        return nomSubMenu;
    }

    public void setNomSubMenu(String nomSubMenu) {
        this.nomSubMenu = nomSubMenu;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

   	
}
