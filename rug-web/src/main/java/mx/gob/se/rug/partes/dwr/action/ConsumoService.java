/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*package mx.gob.se.rug.partes.dwr.action;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumoService {

    private final RestTemplate restTemplate;

    public ConsumoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken(OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
    }

    public String obtenerAccessToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "https://wsconsumo.mineco.gob.gt/api/auth/login",
            HttpMethod.POST,
            request,
            String.class
        );

        return response.getBody();
    }

    public ResponseEntity<String> consultarDatos(String accessToken, String nit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
            "https://wsconsumo.mineco.gob.gt/consulta/sat?nit=" + nit,
            HttpMethod.GET,
            request,
            String.class
        );
    }
}
*/