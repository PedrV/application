/*
    "@Insert name, is a direct message application that enables users to communicate without revealing their identity to others on the internet nor having to worry about message logs. All messages are encrypted with a AES 128bit key followed by a 4096bit length RSA encryption.
    Copyright (C) 2020  Andre Silva

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

 */


/*
Third draft for Symmetric Type Encryption
*/

package src;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class SymmetricEncryption {

    private static final String ALGORITHM = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/GCM/NoPadding";

    private SymmetricEncryption() {
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
            return Base64.getEncoder().encode(cipher.doFinal(plainText.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return initializationVector;
        }
    }

    public static String doAESDecryption(byte[] cipherText, SecretKey secretKey, byte[] initializationVector) {

        Cipher cipher = null;
        cipherText = Base64.getDecoder().decode(cipherText);

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