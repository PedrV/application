package src;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class DoEncryption {

    // Encrypt a String message with Symetric Encryption (AES)
    public static byte[] doSymmetricEncryption(SecretKey key, byte[] iv, String msg) {
        return SymmetricEncryption.doAESEncryption(msg, key, iv);
    }

    // Encrypt a String message with Asymetric Encryption (RSA)
    public static byte[] doAsymmetricEncryption (byte[] msgSimEncrypt, RSAPublicKey pubkey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return AsymmetricEncryption.encrypt(msgSimEncrypt, pubkey);
    }

     // Encrypt a SecretKey of AES (symetric encryption) with Asymetric Encryption (RSA)
    public static byte[] doAsymmetricEncryptionKEY(SecretKey key, RSAPublicKey pubkey) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return AsymmetricEncryption.encrypt(key.getEncoded(), pubkey);
    }

    // TODO: Make a method that on call encrypts with a message with AES than RSA and returns a String
    // TODO: Make a method that on call decrypts a message with a the user privKey and with the SymmetricKey received from the other person

}
