
package https.rug_rgm_gob_gt.services.rug_rgm_web_service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pFechaHora" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="pTransacciones" type="{https://rug.rgm.gob.gt/services/rug-rgm-web-service}transacciones"/&gt;
 *         &lt;element name="pUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
