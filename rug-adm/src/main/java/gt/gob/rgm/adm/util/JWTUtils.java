package gt.gob.rgm.adm.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class JWTUtils {
	public static String generateToken(String subject, Map<String, Object> claims, long ttlMillis) {
		try {
			Builder builder = JWT.create();
			if(ttlMillis >= 0) {
				long nowMillis = System.currentTimeMillis();
				long expMillis = nowMillis + ttlMillis;
				Date exp = new Date(expMillis);
				builder.withExpiresAt(exp);
				builder.withSubject(subject);
				
				for(Map.Entry<String, Object> claim : claims.entrySet()) {
					builder.withClaim(claim.getKey(), claim.getValue());
				}
			}
			
			String token = builder.sign(Algorithm.HMAC256(Constants.SECRET));
			return token;
		} catch(JWTCreationException | IllegalArgumentException | UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static void verifyToken(String token) throws JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Constants.SECRET)).build();
		verifier.verify(token);
	}
	
	public static String decodeToken(String token) {
		JWT jwt = JWT.decode(token);
		return jwt.getClaim("email").asString();
	}
}
