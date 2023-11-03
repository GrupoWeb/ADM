
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
 *         &lt;element name="pAgencia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pIdTransaccion" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pSerie" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pNumero" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pCodigoPersona" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pIdPersona" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoPago" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="pMonto" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="pChequespropios" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="pChequesotros" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="pUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pReciboContraloria" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "pAgencia",
    "pIdTransaccion",
    "pSerie",
    "pNumero",
    "pCodigoPersona",
    "pIdPersona",
    "tipoPago",
    "pMonto",
    "pChequespropios",
    "pChequesotros",
    "pUsuario",
    "pReciboContraloria"
})
@XmlRootElement(name = "setBoletaRGMRequest")
public class SetBoletaRGMRequest {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar pFechaHora;
    @XmlElement(required = true)
    protected String pAgencia;
    protected long pIdTransaccion;
    @XmlElement(required = true)
    protected String pSerie;
    protected long pNumero;
    @XmlElement(required = true)
    protected String pCodigoPersona;
    @XmlElement(required = true)
    protected String pIdPersona;
    protected Long tipoPago;
    protected float pMonto;
    protected Float pChequespropios;
    protected Float pChequesotros;
    @XmlElement(required = true)
    protected String pUsuario;
    @XmlElement(required = true)
    protected String pReciboContraloria;

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
     * Gets the value of the pAgencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAgencia() {
        return pAgencia;
    }

    /**
     * Sets the value of the pAgencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAgencia(String value) {
        this.pAgencia = value;
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
     * Gets the value of the pCodigoPersona property.
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
     * Sets the value of the pCodigoPersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodigoPersona(String value) {
        this.pCodigoPersona = value;
    }

    /**
     * Gets the value of the pIdPersona property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIdPersona() {
        return pIdPersona;
    }

    /**
     * Sets the value of the pIdPersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIdPersona(String value) {
        this.pIdPersona = value;
    }

    /**
     * Gets the value of the tipoPago property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTipoPago() {
        return tipoPago;
    }

    /**
     * Sets the value of the tipoPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTipoPago(Long value) {
        this.tipoPago = value;
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

    /**
     * Gets the value of the pChequespropios property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPChequespropios() {
        return pChequespropios;
    }

    /**
     * Sets the value of the pChequespropios property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPChequespropios(Float value) {
        this.pChequespropios = value;
    }

    /**
     * Gets the value of the pChequesotros property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPChequesotros() {
        return pChequesotros;
    }

    /**
     * Sets the value of the pChequesotros property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPChequesotros(Float value) {
        this.pChequesotros = value;
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

    /**
     * Gets the value of the pReciboContraloria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPReciboContraloria() {
        return pReciboContraloria;
    }

    /**
     * Sets the value of the pReciboContraloria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPReciboContraloria(String value) {
        this.pReciboContraloria = value;
    }

}
