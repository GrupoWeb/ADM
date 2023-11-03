
package gt.gob.rgm.rug.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pFechaHora" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="pIdTransaccion" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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
     * Gets the value of the pFechaHora property.
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
     * Sets the value of the pFechaHora property.
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
     * Gets the value of the pUsuario property.
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
     * Sets the value of the pUsuario property.
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
