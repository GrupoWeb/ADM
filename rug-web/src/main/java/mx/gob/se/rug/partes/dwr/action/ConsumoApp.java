/*package mx.gob.se.rug.partes.dwr.action;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumoApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsumoApp.class, args);
    }
}

@EnableWebSecurity
class SecurityConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    class ConsumoController {

        private final ConsumoService consumoService;

        public ConsumoController(ConsumoService consumoService) {
            this.consumoService = consumoService;
        }

        @GetMapping("/getToken")
        public String getToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
            return consumoService.getToken(authorizedClient);
        }

        @GetMapping("/consultarDatos")
        public ResponseEntity<String> consultarDatos(@AuthenticationPrincipal OidcIdToken idToken) {
            String accessToken = idToken.getTokenValue();
            String nit = "979767"; // Cambia esto por el NIT que necesitas consultar
            return consumoService.consultarDatos(accessToken, nit);
        }
    }
}
*/