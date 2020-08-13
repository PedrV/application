/*
Testing Encryption/Decryption messages 
*/

package src;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class App{

    public static void main(String[] args) throws Exception {
/*

        Scanner stdin = new Scanner(System.in);
        String str = stdin.nextLine();

        //System.out.print("Base64 testing: ");
        //for(byte a : Base64.getEncoder().encode(str.getBytes())) System.out.print((char) a);
        //System.out.println();

        System.out.println("String: " + str);

        KeyPair prkey = AssimetricEncryption.generateKeyPair();

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
*/
    /*    System.out.println("---------------------------------------------------------------------------------------");
        // Double Encryption

        Scanner stdin = new Scanner(System.in);
        String str = stdin.nextLine();

        // 1layer
        SecretKey key = SimetricEncryption.createAESKey();
        byte[] iv = SimetricEncryption.createInitializationVector();
        byte[] simetric = SimetricEncryption.doAESEncryption(str,key,iv);

        // 2layer
        KeyPair pkeys = AssimetricEncryption.generateKeyPair();
        byte[] assimetric = AssimetricEncryption.encrypt(pkeys.getPublic().getEncoded(),simetric);


        System.out.println("String: " + str);
        System.out.println("1 Layer Encryption: " + Arrays.toString(simetric));
        System.out.print("1 Layer Encryption: ");
        for(byte a : simetric) System.out.print((char) a);
        System.out.println();
        System.out.println("2 Layer Encryption: " + Arrays.toString(assimetric));
        System.out.print("2 Layer Encryption: ");
        for(byte a : assimetric) System.out.print((char) a);
        System.out.println();

        //Decryption

        //byte[] result = AssimetricEncryption.
*/
        // TESTE NUMBER 3

        Scanner stdin = new Scanner(System.in);
        String str = stdin.nextLine();
        System.out.println("String: " + str);

        // Encrypt process
        SecretKey key = SimetricEncryption.createAESKey();
        byte[] iv = SimetricEncryption.createInitializationVector();
        byte[] simetric = SimetricEncryption.doAESEncryption(str,key,iv);

        System.out.print("Simetric Encryption: ");
        for(byte a : simetric) System.out.print((char) a);
        System.out.println();

        KeyPair pkeys = AssimetricEncryption.generateKeyPair();
        byte[] keyencryption = AssimetricEncryption.encrypt(pkeys.getPublic().getEncoded(),key.getEncoded());

        System.out.print("Key Encryption: ");
        for(byte a : keyencryption) System.out.print((char) a);
        System.out.println();

        // Desencrypt process
        byte[] newkey = AssimetricEncryption.decrypt(pkeys.getPrivate().getEncoded(),keyencryption);
        String newstring = SimetricEncryption.doAESDecryption(simetric,new SecretKeySpec(newkey, 0, newkey.length, "AES"),iv);

        System.out.println("Desencrypted message: " + newstring);
        System.out.println("Success: " + (str.equals(newstring) ? "True" : "False"));


    }

}