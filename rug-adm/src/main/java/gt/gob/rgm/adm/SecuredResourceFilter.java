package gt.gob.rgm.adm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.auth0.jwt.exceptions.JWTVerificationException;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.util.JWTUtils;

@SecuredResource
@Provider
public class SecuredResourceFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		String authorization = context.getHeaderString("Authorization");
		if(authorization == null || authorization.isEmpty()) {
			context.abortWith(Response.ok("No se envió token en la petición").status(Response.Status.UNAUTHORIZED).build());
		} else {
			String token = authorization.split(" ")[1];
			try {
				JWTUtils.verifyToken(token);
			} catch(JWTVerificationException | IllegalArgumentException | UnsupportedEncodingException e) {
				context.abortWith(Response.ok("Sesión finalizada").status(Response.Status.UNAUTHORIZED).build());
			}
		}
		
	}
}
