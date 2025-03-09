package com.jwtdemo.service;

import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Service
public class EncryptionService {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public EncryptionService() throws Exception {
        // Génération d'une paire de clés RSA
        KeyPair keyPair = generateRSAKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    public String encryptJwt(String payload) throws Exception {
        // Générer une clé AES pour chiffrer le payload
        SecretKey aesKey = generateAESKey();

        // Chiffrer le payload avec AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedPayload = aesCipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        // Chiffrer la clé AES avec RSA
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(aesKey.getEncoded());

        // Retourner `AES(KEY).AES(DATA)`
        return base64UrlEncode(encryptedKey) + "." + base64UrlEncode(encryptedPayload);
    }

    public String decryptJwt(String encryptedJwt) throws Exception {
        String[] parts = encryptedJwt.split("\\.");
        byte[] encryptedKey = Base64.getUrlDecoder().decode(parts[0]);
        byte[] encryptedPayload = Base64.getUrlDecoder().decode(parts[1]);

        // Déchiffrer la clé AES avec RSA
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedKey);
        SecretKey aesKey = new javax.crypto.spec.SecretKeySpec(aesKeyBytes, "AES");

        // Déchiffrer le payload avec AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(aesCipher.doFinal(encryptedPayload));
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    private SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    private String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
}