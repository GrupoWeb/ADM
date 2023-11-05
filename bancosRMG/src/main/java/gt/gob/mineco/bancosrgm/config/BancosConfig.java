package gt.gob.mineco.bancosrgm.config;

import gt.gob.mineco.bancosrgm.Services.pagosServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

@Configuration
public class BancosConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint endpoint(pagosServiceImpl pagosServiceImpl){
        Endpoint endpoint = new EndpointImpl(bus, pagosServiceImpl,SOAPBinding.SOAP12HTTP_BINDING);
        endpoint.publish("/Pagos");
        return endpoint;
    }

}
