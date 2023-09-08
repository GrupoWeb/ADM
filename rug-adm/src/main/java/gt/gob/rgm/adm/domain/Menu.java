package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Menu implements Serializable {
    
    private Integer menuId;
    private String nomMenu;
    private String nomSubMenu;
    private String rol;
    private String link;
    
	
	public Menu() {
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
