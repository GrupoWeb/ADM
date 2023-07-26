package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Tramites implements Serializable {

    private static  final long serialVersionUID = 1L;

    private String idUsuario;


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
