/* 
    File for isolated tests
*/

package src;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Test {
    public static void main(String[] args) {
        try {
            AssimetricEncryption.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
}