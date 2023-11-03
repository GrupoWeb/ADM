
package https.rug_rgm_gob_gt.services.rug_rgm_web_service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="pCodigoPersona" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "pCodigoPersona"
})
@XmlRootElement(name = "getUsuarioRGMRequest")
public class GetUsuarioRGMRequest {

    @XmlElement(required = true)
    protected String pCodigoPersona;

    /**
     * Obtiene el valor de la propiedad pCodigoPersona.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodigoPersona() {
        return pCodigoPersona;
    }

    /**
     * Define el valor de la propiedad pCodigoPersona.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodigoPersona(String value) {
        this.pCodigoPersona = value;
    }

}
