package com.zugazagoitia.knag.vault;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * CryptoUtils is a utility class for encrypting and decrypting strings using AES-256-CBC, with a
 * password-based key derivation function (PBKDF2) using HMACSHA256.
 */
public class CryptoUtils {
    public static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final byte[] IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    /**
     * Encrypts the given string using the given password and salt.
     *
     * @param strToEncrypt The string to encrypt.
     * @param password The password to use.
     * @param salt The salt to use.
     * @return The encrypted, base64 encoded string.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     * @throws InvalidKeySpecException If the key is not supported.
     * @throws NoSuchPaddingException If the padding is not supported.
     * @throws InvalidAlgorithmParameterException If the parameters are not supported.
     * @throws InvalidKeyException If the key is malformed.
     * @throws IllegalBlockSizeException If the block size is malformed.
     * @throws BadPaddingException If the padding is malformed.
     */
    public static String encrypt(String strToEncrypt, String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
                    InvalidAlgorithmParameterException, InvalidKeyException,
                    IllegalBlockSizeException, BadPaddingException {

        byte[] iv = IV;
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Decrypts the given string using the given password and salt.
     *
     * @param strToDecrypt A base64 encoded and encrypted string.
     * @param password The password.
     * @param salt The salt.
     * @return The decrypted string.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     * @throws InvalidKeySpecException If the key is not supported.
     * @throws NoSuchPaddingException If the padding is not supported.
     * @throws InvalidAlgorithmParameterException If the parameters are not supported.
     * @throws InvalidKeyException If the key is malformed.
     * @throws IllegalBlockSizeException If the block size is malformed.
     * @throws BadPaddingException If the padding is malformed or the key is wrong.
     */
    public static String decrypt(String strToDecrypt, String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
                    InvalidAlgorithmParameterException, InvalidKeyException,
                    IllegalBlockSizeException, BadPaddingException {

        byte[] iv = IV;
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    /**
     * Generates a random 200 byte salt.
     *
     * @return The base64 encoded salt.
     */
    public static String generateSalt() {
        byte[] randomBytes = new byte[200];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
