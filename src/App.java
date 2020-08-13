/*
Testing Encryption/Decryption messages 
*/

package src;

import javax.crypto.SecretKey; 
import java.util.Arrays;

public class App{

    public static void main(String[] args){
        SecretKey key = SimetricEncryption.createAESKey();
        byte[] vector = SimetricEncryption.createInitializationVector();
        String str = "Olá mundo!!";


        System.out.println("String: " + str);
        byte[] enc = SimetricEncryption.doAESEncryption(str, key, vector);
        System.out.println("Encryption: " + Arrays.toString(enc));
        System.out.println("Desencryption: " + SimetricEncryption.doAESDecryption(enc, key, vector));

    }

}