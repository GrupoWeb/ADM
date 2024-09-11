package gt.gob.rgm.rs;

import javax.ws.rs.core.MediaType;

import gt.gob.rgm.domain.Usuario;
import gt.gob.rgm.service.BitacoraService;
import gt.gob.rgm.service.UsuariosService;
import gt.gob.rgm.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.Base64;

@Component
@Path("/consulta")
public class ConsultaFactoraje {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private BitacoraService bitacoraService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findFactoraje(
            @QueryParam(value="serie") String serie,
            @HeaderParam("authorization") String auth
    ) {
        // Verificación de auth antes de continuar
        if (auth == null || auth.trim().isEmpty()) {
            bitacoraService.registrarOperacion("login_failed", "El encabezado de autorización es nulo o vacío", null);
            throw new AuthenticationException("Authorization header is missing or empty.");
        }

        // Verificación de que los servicios estén inyectados correctamente
        if (usuariosService == null || bitacoraService == null) {
            throw new IllegalStateException("Servicios no inyectados correctamente");
        }

//        Usuario user = isUserAuthenticated(auth);

        // Aquí puedes continuar con la lógica que necesites cuando el usuario esté autenticado.
        return "";
    }

    public Usuario isUserAuthenticated(String authString) throws AuthenticationException {
        String[] authParts = splitAuthString(authString);

        String authInfo = authParts[1];
        byte[] bytes = decodeBase64(authInfo);

        String[] values = splitCredentials(bytes);
        bitacoraService.registrarOperacion("login_attempt", "Usuario: " + values[0], null);

        Usuario user = usuariosService.getUsuarioExterno(values[0]);

        if (user != null && HashUtils.hash(values[1], "SHA-1").equalsIgnoreCase(user.getPassword())) {
            bitacoraService.registrarOperacion("login_success", "Usuario autenticado exitosamente", user.getIdPersona());
            return user;
        }

        bitacoraService.registrarOperacion("login_failed", "Contraseña incorrecta para el usuario " + values[0], null);
        return null;
    }

    private String[] splitAuthString(String authString) throws AuthenticationException {
        String[] authParts = authString.split("\\s+");
        if (authParts.length != 2) {
            bitacoraService.registrarOperacion("login_failed", "Formato de autenticación inválido", null);
            throw new AuthenticationException("Invalid auth format.");
        }
        return authParts;
    }

    private byte[] decodeBase64(String authInfo) throws AuthenticationException {
        try {
            return Base64.getDecoder().decode(authInfo);
        } catch (IllegalArgumentException e) {
            bitacoraService.registrarOperacion("login_failed", "Base64 decoding failed", null);
            throw new AuthenticationException("Invalid Base64 encoding.");
        }
    }

    private String[] splitCredentials(byte[] bytes) throws AuthenticationException {
        String decodedAuth = new String(bytes);
        String[] values = decodedAuth.split(":");
        if (values.length != 2) {
            bitacoraService.registrarOperacion("login_failed", "Formato de credenciales inválido", null);
            throw new AuthenticationException("Invalid credentials format.");
        }
        return values;
    }

    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
