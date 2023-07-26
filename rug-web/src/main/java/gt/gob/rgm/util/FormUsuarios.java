package gt.gob.rgm.util;

import java.io.Serializable;

public class FormUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;

    public FormUsuarios(){}

    private Long idUsuario;


    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
