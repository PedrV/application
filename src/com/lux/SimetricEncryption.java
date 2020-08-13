/*
Third draft for Simetric Type Encryption
	- Encrypton seems to work fine, but can be improved and Block cipher mode of operation, Counter Mode, Padding are to be
	studied
*/


//package src.com.lux;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

public class Other {

    private static final String ALGORITHM = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/GCM/NoPadding";

    private Other() {
    }

    public static byte[] createInitializationVector() {
        // Used with encryption
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }

    public static byte[] doAESEncryption(String plainText, SecretKey secretKey, byte[] initializationVector) {

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128,initializationVector);

        try {
            assert cipher != null;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,gcmParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            return cipher.doFinal(plainText.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return initializationVector;
        }
    }

    public static String doAESDecryption(byte[] cipherText, SecretKey secretKey, byte[] initializationVector) {

        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128,initializationVector);

        try {
            assert cipher != null;
            cipher.init(Cipher.DECRYPT_MODE, secretKey,gcmParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] result = new byte[16];
        try {
            result = cipher.doFinal(cipherText);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return new String(result);
    }

    public static SecretKey createAESKey() {
        SecureRandom securerandom = new SecureRandom();

        KeyGenerator keygenerator;
        SecretKey key = null; 		// TODO: Possible vulnerability in the event of Algorithm Error, the SecretKey gets forced to null
        try {

            keygenerator = KeyGenerator.getInstance(ALGORITHM);
            keygenerator.init(256, securerandom);
            key = keygenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return key;
    }
}