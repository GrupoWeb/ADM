//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-548 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.16 at 10:22:20 AM CDT 
//


package mx.gob.se.rug.masiva.to.string;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{carga-masiva}otorgante" maxOccurs="unbounded"/>
 *         &lt;element ref="{carga-masiva}deudor" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{carga-masiva}acreedor-adicional" maxOccurs="unbounded" minOccurs="0"/>
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
    "otorgante",
    "deudor",
    "acreedorAdicional"
})
@XmlRootElement(name = "partes")
public class Partes {

    @XmlElement(required = true)
    protected List<Otorgante> otorgante;
    @XmlElement(name = "deudor-garante")
    protected List<Deudor> deudor;
    @XmlElement(name = "acreedor-garantizado")
    protected List<AcreedorAdicional> acreedorAdicional;

    /**
     * Gets the value of the otorgante property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otorgante property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtorgante().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Otorgante }
     * 
     * 
     */
    public List<Otorgante> getOtorgante() {
        if (otorgante == null) {
            otorgante = new ArrayList<Otorgante>();
        }
        return this.otorgante;
    }

    /**
     * Gets the value of the deudor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deudor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeudor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Deudor }
     * 
     * 
     */
    public List<Deudor> getDeudor() {
        if (deudor == null) {
            deudor = new ArrayList<Deudor>();
        }
        return this.deudor;
    }

    /**
     * Gets the value of the acreedorAdicional property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acreedorAdicional property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcreedorAdicional().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcreedorAdicional }
     * 
     * 
     */
    public List<AcreedorAdicional> getAcreedorAdicional() {
        if (acreedorAdicional == null) {
            acreedorAdicional = new ArrayList<AcreedorAdicional>();
        }
        return this.acreedorAdicional;
    }

}