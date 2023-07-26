//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.5 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: PM.03.29 a las 03:33:41 PM CST 
//


package mx.gob.se.rug.masiva.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="elimina-otorgantes" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="elimina-deudores" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="elimina-acreedor-adicional" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "eliminar-partes")
public class EliminarPartes {

    @XmlAttribute(name = "elimina-otorgantes")
    protected Boolean eliminaOtorgantes;
    @XmlAttribute(name = "elimina-deudores")
    protected Boolean eliminaDeudores;
    @XmlAttribute(name = "elimina-acreedor-adicional")
    protected Boolean eliminaAcreedorAdicional;

    /**
     * Obtiene el valor de la propiedad eliminaOtorgantes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEliminaOtorgantes() {
        return eliminaOtorgantes;
    }

    /**
     * Define el valor de la propiedad eliminaOtorgantes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEliminaOtorgantes(Boolean value) {
        this.eliminaOtorgantes = value;
    }

    /**
     * Obtiene el valor de la propiedad eliminaDeudores.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEliminaDeudores() {
        return eliminaDeudores;
    }

    /**
     * Define el valor de la propiedad eliminaDeudores.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEliminaDeudores(Boolean value) {
        this.eliminaDeudores = value;
    }

    /**
     * Obtiene el valor de la propiedad eliminaAcreedorAdicional.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEliminaAcreedorAdicional() {
        return eliminaAcreedorAdicional;
    }

    /**
     * Define el valor de la propiedad eliminaAcreedorAdicional.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEliminaAcreedorAdicional(Boolean value) {
        this.eliminaAcreedorAdicional = value;
    }

}
