package src.com.lux;

import java.security.KeyPair;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.Cipher;

public class SimetricEncryption {

    private static final String ALGORITHM = "AES";

    private SimetricEncryption(){

    }

    public static byte[] do_AESEncryption(String plainText, SecretKey secretKey) throws Exception{ 

        byte[] initializationVector = new byte[16]; 
		SecureRandom secureRandom = new SecureRandom(); 
		secureRandom.nextBytes(initializationVector); 

        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM); 
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector); 

		cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec); 

		return cipher.doFinal(plainText.getBytes()); 
	} 

    public static String do_AESDecryption(byte[] cipherText, SecretKey secretKey, byte[] initializationVector) throws Exception{ 
		Cipher cipher 
			= Cipher.getInstance( 
				AES_CIPHER_ALGORITHM); 

		IvParameterSpec ivParameterSpec 
			= new IvParameterSpec( 
				initializationVector); 

		cipher.init( 
			Cipher.DECRYPT_MODE, 
			secretKey, 
			ivParameterSpec); 

		byte[] result 
			= cipher.doFinal(cipherText); 

		return new String(result); 
	} 

	public static SecretKey createAESKey() throws Exception { 
		SecureRandom securerandom = new SecureRandom(); 
		KeyGenerator keygenerator = KeyGenerator.getInstance(AES); 

		keygenerator.init(256, securerandom); 
		SecretKey key = keygenerator.generateKey(); 
		return key; 
	} 
}