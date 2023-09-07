package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Rol implements Serializable {
    
    private Integer rolId;
    private String nomRol;
    private String tipoRol;
   
    
	
	public Rol() {
	}

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

   

}
