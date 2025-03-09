package com.jwtdemo.service;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/*
 * Service pour signer et vérifier un JWT (RS256)
 */
@Service
public class JwtService {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtService() throws Exception {
        // Génération d'une paire de clés RSA 2048 bits
        KeyPair keyPair = generateRSAKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    public String generateSignedJwt(String user) throws Exception {
        // Création du header JWT en JSON
        String headerJson = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";

        // Création du payload JWT (avec `iss`, `sub`, et `exp`)
        String payloadJson = String.format("{\"iss\":\"MyApp\",\"sub\":\"%s\",\"exp\":%d}",
                user, System.currentTimeMillis() / 1000 + 600);

        // Encodage Base64 URL-safe sans padding
        String headerBase64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String payloadBase64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

        // Signature du JWT avec SHA256 + RSA
        String signature = signData(headerBase64 + "." + payloadBase64, privateKey);

        // Construction finale du JWT : `header.payload.signature`
        return headerBase64 + "." + payloadBase64 + "." + signature;
    }

    public boolean verifySignedJwt(String jwt) {
        try {
            // Découpage du JWT en 3 parties
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) return false;

            // Vérification de la signature avec SHA256 + RSA
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update((parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8));

            // Comparaison de la signature du JWT avec la clé publique
            byte[] signatureBytes = Base64.getUrlDecoder().decode(parts[2]);
            
            return verifier.verify(signatureBytes);
            
        } catch (Exception e) {
            return false;
        }
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        // 🔹 Génération de la paire de clés RSA (2048 bits)
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /*
     * Signature SHA256 + RSA
     */
    private String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data.getBytes(StandardCharsets.UTF_8));
        
        return base64UrlEncode(signer.sign());
    }

    /*
     * Encodage Base64 URL-safe sans padding (conforme JWT)
     */
    private String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
}