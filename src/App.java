/*
Testing Encryption/Decryption messages 
*/

package src;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Scanner;

public class App{

    public static void main(String[] args) throws Exception {

        KeyPair prkey = AssimetricEncryption.generateKeyPair();

        Scanner stdin = new Scanner(System.in);
        String str = stdin.nextLine();

        System.out.println("String: " + str);

        //System.out.print("Pub Key: ");
        //for(byte a : prkey.getPublic().getEncoded()) System.out.print((char) a);
        //System.out.println();

        //System.out.print("Priv Key: ");
        //for(byte a : prkey.getPrivate().getEncoded()) System.out.print((char) a);
        //System.out.println();

        byte[] enc = AssimetricEncryption.encrypt(prkey.getPublic().getEncoded(),str.getBytes());
        System.out.println("Encryption: " + Arrays.toString(enc));

        System.out.print("Encryption: ");
        for(byte a : enc) System.out.print((char) a);
        System.out.println();

        System.out.println("Desencryption: " + new String(AssimetricEncryption.decrypt(prkey.getPrivate().getEncoded(),enc), StandardCharsets.UTF_8));

        System.out.println("---------------------------------------------------------------------------------------");

        SecretKey key = SimetricEncryption.createAESKey();
        byte[] vector = SimetricEncryption.createInitializationVector();

        //System.out.print("Key: ");
        //for(byte a : key.getEncoded()) System.out.print((char) a);
        //System.out.println();

        //Scanner stdin = new Scanner(System.in);
        //String str = stdin.nextLine();


        System.out.println("String: " + str);
        byte[] ence = SimetricEncryption.doAESEncryption(str, key, vector);

        System.out.println("Encryption: " + Arrays.toString(ence));
        System.out.print("Encryption: ");
        for(byte a : ence) System.out.print((char) a);
        System.out.println();

        System.out.println("Desencryption: " + SimetricEncryption.doAESDecryption(ence, key, vector));

        System.out.println("---------------------------------------------------------------------------------------");
        // Double Encryption

        String s = new String(ence, StandardCharsets.UTF_8);
        byte[] snd = AssimetricEncryption.encrypt(prkey.getPublic().getEncoded(),s.getBytes());

        System.out.println("String: " + str);
        System.out.println("1 Layer Encryption: " + Arrays.toString(ence));
        System.out.print("1 Layer Encryption: ");
        for(byte a : ence) System.out.print((char) a);
        System.out.println();
        System.out.println("2 Layer Encryption: " + Arrays.toString(snd));
        System.out.print("2 Layer Encryption: ");
        for(byte a : snd) System.out.print((char) a);
        System.out.println();

        String s2 = new String(AssimetricEncryption.decrypt(prkey.getPrivate().getEncoded(),enc), StandardCharsets.UTF_8);
        System.out.println("Desencryption: " + SimetricEncryption.doAESDecryption(s2.getBytes(), key, vector));
    }

}