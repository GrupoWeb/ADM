
package gt.gob.mineco.bancosrgm.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para transaccion complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="transaccion"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="pIdTransaccion" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="pSerie" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="pNumero" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="pMonto" type="{http://www.w3.org/2001/XMLSchema}float"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
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
     * Obtiene el valor de la propiedad pIdTransaccion.
     * 
     */
    public long getPIdTransaccion() {
        return pIdTransaccion;
    }

    /**
     * Define el valor de la propiedad pIdTransaccion.
     * 
     */
    public void setPIdTransaccion(long value) {
        this.pIdTransaccion = value;
    }

    /**
     * Obtiene el valor de la propiedad pSerie.
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
     * Define el valor de la propiedad pSerie.
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
     * Obtiene el valor de la propiedad pNumero.
     * 
     */
    public long getPNumero() {
        return pNumero;
    }

    /**
     * Define el valor de la propiedad pNumero.
     * 
     */
    public void setPNumero(long value) {
        this.pNumero = value;
    }

    /**
     * Obtiene el valor de la propiedad pMonto.
     * 
     */
    public float getPMonto() {
        return pMonto;
    }

    /**
     * Define el valor de la propiedad pMonto.
     * 
     */
    public void setPMonto(float value) {
        this.pMonto = value;
    }

}
