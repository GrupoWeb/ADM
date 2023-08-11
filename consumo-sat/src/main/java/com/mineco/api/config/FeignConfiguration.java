package com.mineco.api.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

import feign.Client;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public Client feignClient() {
        try {
            final TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null; // No se aceptan emisores de confianza
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            // No se realiza ninguna comprobación
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            // No se realiza ninguna comprobación
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            final HostnameVerifier allowAllHosts = (hostname, session) -> true; // Aceptar cualquier nombre de host

            return new Client.Default(sslContext.getSocketFactory(), allowAllHosts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(500000, 3000000);
    }
}
