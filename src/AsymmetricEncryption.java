/* 
Very first draft of Asymmetric Type Encryption
    - Possible change for BouncyCastle provider, operates differntly regarding RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING, 
    Java default crypto provider uses SHA1 as the MGF1 function has, BouncyCastle appears to use SHA256 for both MG1 and OAEP and may cause compatibility issues
*/


package src;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricEncryption {

    private static final String RSA_CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String ALGORITHM = "RSA";

    private AsymmetricEncryption() {
    }

    public static byte[] encrypt(byte[] inputData, RSAPublicKey pubkey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubkey);

        return Base64.getEncoder().encode(cipher.doFinal(inputData));
    }

    public static byte[] decrypt(byte[] inputData, RSAPrivateKey privkey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privkey);

        return cipher.doFinal(Base64.getDecoder().decode(inputData));
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN"); // TODO: Search possible PRNG of SecureRandom that may compromise security

        keyGen.initialize(2048, random);

        KeyPair kp = keyGen.generateKeyPair();
        /* pubkey = (RSAPublicKey) kp.getPublic();
        privkey = (RSAPrivateKey) kp.getPrivate(); */

        // See generated keys
        // -------------------------------------------------------- \\
        /*
        System.out.println ("-----BEGIN PRIVATE KEY-----");
        System.out.println (Base64.getMimeEncoder().encodeToString( kp.getPublic().getEncoded()));
        System.out.println ("-----END PRIVATE KEY-----");
        System.out.println ("-----BEGIN PUBLIC KEY-----");
        System.out.println (Base64.getMimeEncoder().encodeToString( kp.getPrivate().getEncoded()));
        System.out.println ("-----END PUBLIC KEY-----"); 
        */
        // -------------------------------------------------------- \\

        return kp;
    }
}