package com.sou.sec;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// Java 8 example for RSA-AES encryption/decryption.
// Uses strong encryption with 2048 key size.
public class RSAEncryptionWithAES {

    public static void main(String[] args) throws Exception {
       
    	String plainText = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb3VtZW4iLCJzdWIiOiJtc2lsdmVybWFuIiwibmFtZSI6IlNvdW1lbiBDaG91ZGh1cnkiLCJSb2xlIjoiVVNFUiIsInNjb3BlIjoiYWRtaW5zIiwiaWF0IjoxNDY2Nzk2ODIyLCJleHAiOjQ2MjI0NzA0MjJ9.SFt4--xbb452UP0qn3mK5LcZCB9cPPVqJsyff7ZvheY";

        // Generate public and private keys using RSA
        Map<String, Object> keys = getRSAKeys();
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        PublicKey publicKey = (PublicKey) keys.get("public");
        
    	KeyStore keyStore = KeyStore.getInstance("JCEKS");
	    FileInputStream stream = new FileInputStream("KeyStore.jks");
	    keyStore.load(stream, "changeit".toCharArray());
	    Key key = keyStore.getKey("mydomain", "changeit".toCharArray());
    	System.out.println("Key :: "+ key.getEncoded());
    	if (key instanceof PrivateKey) {
	      // Get certificate of public key
	      Certificate cert = keyStore.getCertificate("mydomain");
	      // Get public key
	      publicKey = cert.getPublicKey();
	      // Return a key pair
	      new KeyPair(publicKey, (PrivateKey) key);
	      privateKey = (PrivateKey) key;
	    }
	

        // First create an AES Key
        String secretAESKeyString = getSecretAESKeyAsString();

        // Encrypt our data with AES key
        String encryptedText = encryptTextUsingAES(plainText, secretAESKeyString);

        // Encrypt AES Key with RSA Private Key
        String encryptedAESKeyString = encryptAESKey(secretAESKeyString, privateKey);

        // The following logic is on the other side.

        // First decrypt the AES Key with RSA Public key
        String decryptedAESKeyString = decryptAESKey(encryptedAESKeyString, publicKey);

        // Now decrypt data using the decrypted AES key!
        String decryptedText = decryptTextUsingAES(encryptedText, decryptedAESKeyString);

        System.out.println("input:" + plainText);
        System.out.println("AES Key:" + secretAESKeyString);
        System.out.println("encrypted:" + encryptedText);
        System.out.println("decrypted:" + decryptedText);

    }

    // Create a new AES key. Uses 128 bit (weak)
    public static String getSecretAESKeyAsString() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secKey.getEncoded());
        return encodedKey;
    }

    // Encrypt text using AES key
    public static String encryptTextUsingAES(String plainText, String aesKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(byteCipherText);
    }

    // Decrypt text using AES key
    public static String decryptTextUsingAES(String encryptedText, String aesKeyString) throws Exception {

        byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(bytePlainText);
    }

    // Get RSA keys. Uses key size of 2048.
    private static Map<String, Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

    // Decrypt AES Key using RSA public key
    private static String decryptAESKey(String encryptedAESKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedAESKey)));
    }

    // Encrypt AES Key using RSA private key
    private static String encryptAESKey(String plainAESKey, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainAESKey.getBytes()));
    }

}