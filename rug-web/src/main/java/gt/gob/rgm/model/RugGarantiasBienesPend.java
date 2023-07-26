package gt.gob.rgm.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="RUG_GARANTIAS_BIENES_PEND")
public class RugGarantiasBienesPend implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID_GARAN_BIEN_PEND")
    private Integer idGaranBienPend;

    @Column(name="ID_TRAMITE_TEMP")
    private Integer idTramiteTemp;

    @Column(name="TIPO_BIEN_ESPECIAL")
    private Integer tipoBienEspecial;

    @Column(name="TIPO_IDENTIFICADOR")
    private Integer tipoIdentificador;

    @Column(name="IDENTIFICADOR")
    private String identificador;

    @Column(name="DESCRIPCION_BIEN")
    private String descripcionBien;

    @Column(name="SERIE")
    private String serie;

    public RugGarantiasBienesPend(){}
    



    /**
     * @return Integer return the idGaranBienPend
     */
    public Integer getIdGaranBienPend() {
        return idGaranBienPend;
    }

    /**
     * @param idGaranBienPend the idGaranBienPend to set
     */
    public void setIdGaranBienPend(Integer idGaranBienPend) {
        this.idGaranBienPend = idGaranBienPend;
    }

    /**
     * @return Integer return the idTramiteTemp
     */
    public Integer getIdTramiteTemp() {
        return idTramiteTemp;
    }

    /**
     * @param idTramiteTemp the idTramiteTemp to set
     */
    public void setIdTramiteTemp(Integer idTramiteTemp) {
        this.idTramiteTemp = idTramiteTemp;
    }

  

    /**
     * @return Integer return the tipoIdentificador
     */
    public Integer getTipoIdentificador() {
        return tipoIdentificador;
    }

    /**
     * @param tipoIdentificador the tipoIdentificador to set
     */
    public void setTipoIdentificador(Integer tipoIdentificador) {
        this.tipoIdentificador = tipoIdentificador;
    }

    /**
     * @return String return the identificador
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * @param identificador the identificador to set
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    /**
     * @return String return the descripcionBien
     */
    public String getDescripcionBien() {
        return descripcionBien;
    }

    /**
     * @param descripcionBien the descripcionBien to set
     */
    public void setDescripcionBien(String descripcionBien) {
        this.descripcionBien = descripcionBien;
    }

    /**
     * @return String return the serie
     */
    public String getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */
    public void setSerie(String serie) {
        this.serie = serie;
    }


    /**
     * @return Integer return the tipoBienEspecial
     */
    public Integer getTipoBienEspecial() {
        return tipoBienEspecial;
    }

    /**
     * @param tipoBienEspecial the tipoBienEspecial to set
     */
    public void setTipoBienEspecial(Integer tipoBienEspecial) {
        this.tipoBienEspecial = tipoBienEspecial;
    }

}
