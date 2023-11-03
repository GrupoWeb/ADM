
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
 *         &lt;element name="pAgencia" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pIdTransaccion" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="pSerie" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pNumero" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="pCodigoPersona" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pIdPersona" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tipoPago" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="pMonto" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *         &lt;element name="pChequespropios" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="pChequesotros" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="pUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pReciboContraloria" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
     * Obtiene el valor de la propiedad pAgencia.
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
     * Define el valor de la propiedad pAgencia.
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

    /**
     * Obtiene el valor de la propiedad pIdPersona.
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
     * Define el valor de la propiedad pIdPersona.
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
     * Obtiene el valor de la propiedad tipoPago.
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
     * Define el valor de la propiedad tipoPago.
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

    /**
     * Obtiene el valor de la propiedad pChequespropios.
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
     * Define el valor de la propiedad pChequespropios.
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
     * Obtiene el valor de la propiedad pChequesotros.
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
     * Define el valor de la propiedad pChequesotros.
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

    /**
     * Obtiene el valor de la propiedad pReciboContraloria.
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
     * Define el valor de la propiedad pReciboContraloria.
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
