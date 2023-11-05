
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
 *         &amp;lt;element name="pTransacciones" type="{https://operaciones.rgm.gob.gt/rug-rgm-ws/Pagos}transacciones"/&amp;gt;
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
    "pTransacciones",
    "pUsuario"
})
@XmlRootElement(name = "confirmBoletaRGMRequest")
public class ConfirmBoletaRGMRequest {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar pFechaHora;
    @XmlElement(required = true)
    protected Transacciones pTransacciones;
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
     * Obtiene el valor de la propiedad pTransacciones.
     * 
     * @return
     *     possible object is
     *     {@link Transacciones }
     *     
     */
    public Transacciones getPTransacciones() {
        return pTransacciones;
    }

    /**
     * Define el valor de la propiedad pTransacciones.
     * 
     * @param value
     *     allowed object is
     *     {@link Transacciones }
     *     
     */
    public void setPTransacciones(Transacciones value) {
        this.pTransacciones = value;
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
