package gt.gob.rgm.adm.rs;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;



@Provider
@PreMatching
public class CorsFilter implements ContainerResponseFilter {
 
    private final static Logger log = Logger.getLogger( CorsFilter.class.getName() );

    
    @Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    	responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
    	responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    	responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
//    	responseContext.getHeaders().add("Access-Control-Allow-Credentials", "*");
//    	responseContext.getHeaders().add("Access-Control-Allow-Headers", "*");
//    	responseContext.getHeaders().add("Access-Control-Allow-Methods", "*");
    }
}