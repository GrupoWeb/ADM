package gt.gob.mineco.bancosrgm.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.4.0
 * 2023-11-05T12:59:15.652-06:00
 * Generated source version: 3.4.0
 *
 */
@WebServiceClient(name = "pagoService",
                  wsdlLocation = "file:/C:/Users/jjolon/Desktop/RGM/bancosRMG/src/main/resources/wsdl/pagoService.wsdl",
                  targetNamespace = "https://operaciones.rgm.gob.gt/rug-rgm-ws/Pagos")
public class PagoService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("https://operaciones.rgm.gob.gt/rug-rgm-ws/Pagos", "pagoService");
    public final static QName PagoEndpointPort = new QName("https://operaciones.rgm.gob.gt/rug-rgm-ws/Pagos", "PagoEndpointPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/jjolon/Desktop/RGM/bancosRMG/src/main/resources/wsdl/pagoService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PagoService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/jjolon/Desktop/RGM/bancosRMG/src/main/resources/wsdl/pagoService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PagoService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PagoService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PagoService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public PagoService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public PagoService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public PagoService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns PagoServicePortType
     */
    @WebEndpoint(name = "PagoEndpointPort")
    public PagoServicePortType getPagoEndpointPort() {
        return super.getPort(PagoEndpointPort, PagoServicePortType.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PagoServicePortType
     */
    @WebEndpoint(name = "PagoEndpointPort")
    public PagoServicePortType getPagoEndpointPort(WebServiceFeature... features) {
        return super.getPort(PagoEndpointPort, PagoServicePortType.class, features);
    }

}