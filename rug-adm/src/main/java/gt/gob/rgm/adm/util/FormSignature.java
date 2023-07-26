package gt.gob.rgm.adm.util;

import java.io.Serializable;

public class FormSignature implements Serializable {
    private static final long serialVersionUID = 1L;

    public FormSignature() {
    }

    private Integer tramite;

    private Integer garantia;

    private String email;

    public Integer getTramite() {
        return tramite;
    }

    public void setTramite(Integer tramite) {
        this.tramite = tramite;
    }

    public Integer getGarantia() {
        return garantia;
    }

    public void setGarantia(Integer garantia) {
        this.garantia = garantia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
