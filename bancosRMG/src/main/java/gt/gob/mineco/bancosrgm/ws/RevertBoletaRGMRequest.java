
package gt.gob.mineco.bancosrgm.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * &lt;p&gt;Clase Java para anonymous complex type.
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="pFechaHora" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&amp;gt;
 *         &amp;lt;element name="pIdTransaccion" type="{http://www.w3.org/2001/XMLSchema}long"/&amp;gt;
 *         &amp;lt;element name="pUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pFechaHora",
    "pIdTransaccion",
    "pUsuario"
})
@XmlRootElement(name = "revertBoletaRGMRequest")
public class RevertBoletaRGMRequest {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar pFechaHora;
    protected long pIdTransaccion;
    @XmlElement(required = true)
    protected String pUsuario;

    /**
     * Obtiene el valor de la propiedad pFechaHora.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPFechaHora() {
        return pFechaHora;
    }

    /**
     * Define el valor de la propiedad pFechaHora.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPFechaHora(XMLGregorianCalendar value) {
        this.pFechaHora = value;
    }

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
     * Obtiene el valor de la propiedad pUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPUsuario() {
        return pUsuario;
    }

    /**
     * Define el valor de la propiedad pUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPUsuario(String value) {
        this.pUsuario = value;
    }

}
