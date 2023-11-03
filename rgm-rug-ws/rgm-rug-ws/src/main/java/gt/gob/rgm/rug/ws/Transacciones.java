
package gt.gob.rgm.rug.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transacciones complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transacciones">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pTransaccion" type="{https://rug.rgm.gob.gt/services/rug-rgm-web-service}transaccion" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transacciones", propOrder = {
    "pTransaccion"
})
public class Transacciones {

    @XmlElement(required = true)
    protected List<Transaccion> pTransaccion;

    /**
     * Gets the value of the pTransaccion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pTransaccion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPTransaccion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transaccion }
     * 
     * 
     */
    public List<Transaccion> getPTransaccion() {
        if (pTransaccion == null) {
            pTransaccion = new ArrayList<Transaccion>();
        }
        return this.pTransaccion;
    }

}
