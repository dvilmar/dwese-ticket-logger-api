package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;


@Configuration
public class KeyConfig {

    @Value("${jwt.keystore.path}") // Ruta del keystore
    private String keystorePath;

    @Value("${jwt.keystore.password}") // Contraseña del keystore
    private String keystorePassword;

    @Value("${jwt.keystore.alias}") // Alias del par de claves
    private String keystoreAlias;

    /**
     * Crea un bean que carga el par de claves (privada y pública) desde el keystore.
     *
     * @return KeyPair con la clave privada y pública.
     * @throws Exception Si ocurre un error al cargar el keystore.
     */
    @Bean
    public KeyPair jwtKeyPair() throws Exception {
        // Cargar el keystore desde la ruta especificada
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        // Obtener la clave privada y la clave pública asociada
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keystoreAlias, keystorePassword.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(keystoreAlias).getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }

}

