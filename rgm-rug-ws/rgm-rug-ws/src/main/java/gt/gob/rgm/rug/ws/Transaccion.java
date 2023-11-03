
package gt.gob.rgm.rug.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transaccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transaccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pIdTransaccion" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pSerie" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pNumero" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pMonto" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transaccion", propOrder = {
    "pIdTransaccion",
    "pSerie",
    "pNumero",
    "pMonto"
})
public class Transaccion {

    protected long pIdTransaccion;
    @XmlElement(required = true)
    protected String pSerie;
    protected long pNumero;
    protected float pMonto;

    /**
     * Gets the value of the pIdTransaccion property.
     * 
     */
    public long getPIdTransaccion() {
        return pIdTransaccion;
    }

    /**
     * Sets the value of the pIdTransaccion property.
     * 
     */
    public void setPIdTransaccion(long value) {
        this.pIdTransaccion = value;
    }

    /**
     * Gets the value of the pSerie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSerie() {
        return pSerie;
    }

    /**
     * Sets the value of the pSerie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSerie(String value) {
        this.pSerie = value;
    }

    /**
     * Gets the value of the pNumero property.
     * 
     */
    public long getPNumero() {
        return pNumero;
    }

    /**
     * Sets the value of the pNumero property.
     * 
     */
    public void setPNumero(long value) {
        this.pNumero = value;
    }

    /**
     * Gets the value of the pMonto property.
     * 
     */
    public float getPMonto() {
        return pMonto;
    }

    /**
     * Sets the value of the pMonto property.
     * 
     */
    public void setPMonto(float value) {
        this.pMonto = value;
    }

}
