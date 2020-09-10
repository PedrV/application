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

package src;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

class CreatePeer implements Peer {
    private Client cl; // Client Socket that invites the messages to the othe Peer Server
    private Server sv; // Server Socket that recieves messages from other Peers Clients
    private SecretKey mySKey; // Secret Key for the symmetric encryption
    private KeyPair myKP; // KeyPair (private, public) for asymmetric encryption
    private byte[] iv; // Initialization Vector for symetric Encryption
    private String ip; // IP of the connection
    private Integer port; // Port of the connection
    private String clientName; // Name to be displayed

    // TODO: discuss storing of other persons Simetric Key (key obtained from the person that sended a msg first, used to encrypt the following msg), and how to store PubKeys used to communicate

    CreatePeer() throws NoSuchAlgorithmException, NoSuchProviderException {
        this.mySKey = generateSimetricSecretKey();
        this.myKP = generateKeyPair();
        this.iv = generateInitiazationVector();
        this.ip = null;
        this.port = null;
    }

    // Setters/Initializers
    // ------------------------------------------------------------------------------------------------------------- \\
    private SecretKey generateSimetricSecretKey() {
        return SymmetricEncryption.createAESKey();
    }

    // Generate the Initialization Vector used for Simetric encryption
    private byte[] generateInitiazationVector() {
        return SymmetricEncryption.createInitializationVector();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        return AsymmetricEncryption.generateKeyPair();
    }

    void setClientName(String name) {
        this.clientName = name;
    }

    SecretKey getSecretKey() {
        return this.mySKey;
    }

    RSAPrivateKey getPrivateKey() {
        return (RSAPrivateKey) this.myKP.getPrivate();
    }

    byte[] getIV() {
        return this.iv;
    }

    void setIP(String ip) {
        this.ip = ip;
    }

    void setPort(Integer port) {
        this.port = port;
    }

    // ------------------------------------------------------------------------------------------------------------- \\


    // ------------------------------------------------------------------------------------------------------------- \\
    // Update/Start (if first contact) the Client for communication
    @Override
    public void updateClient(String ip, Integer port) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        this.cl = new Client(ip, port, this.mySKey, this.iv, (RSAPublicKey) this.myKP.getPublic());
    }

    // Update/Start (if first contact) the Server for messages
    @Override
    public void updateServer(Integer port) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
        this.sv = new Server(port, this.mySKey, this.iv, (RSAPrivateKey) this.myKP.getPrivate());
    }

    // Update the SecretKey used for Simetric encryption
    @Override
    public boolean refreshSecretKey() {
        SecretKey tempSK = this.mySKey;
        this.mySKey = SymmetricEncryption.createAESKey();
        return tempSK.equals(this.mySKey);
    }

    @Override
    public boolean refreshIV() {
        byte[] temp = this.iv;
        this.iv = SymmetricEncryption.createInitializationVector();
        return Arrays.equals(temp, this.iv);
    }

    // Update the KeyPairs used for Assimetric encryption
    @Override
    public boolean refreshKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair tempKP = this.myKP;
        this.myKP = AsymmetricEncryption.generateKeyPair();
        return tempKP.equals(this.myKP);
    }

    // Get Public key used on the Assimetric encryption
    @Override
    public RSAPublicKey getPublicKey() {
        return (RSAPublicKey) this.myKP.getPublic();
    }

    /**
     * Execute the 2 methods concurrently. Method A (updateClient) is called
     * according to the interface Callable, then Method B (updateServer) runs and
     * then future.get() will wait for the result of A. (B is going to start running
     * before the end of A)
     * 
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * 
     * @see https://stackoverflow.com/questions/30965339/executorcompletionservice-is-not-applicable-for-the-given-arguments
     * @see https://stackoverflow.com/questions/22795563/how-to-use-callable-with-void-return-type
     */
    public void execute() throws InterruptedException, ExecutionException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Implementation of Callable Interface
        Future<Void> future = executorService.submit(new Callable<Void>() {

            public Void call() throws Exception {
                updateClient(ip, port);
                return null;
            }

        });

        updateServer(port);
        this.sv.setName(clientName);
        future.get(); // Wait for completion of updateClient(ip, port);

        // Close executorService
        executorService.shutdown();
    }


}

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
/*         
        CreatePeer c = new CreatePeer();        
        String s = DoEncryption.doEncryption("pedro", c.getSecretKey(), c.getIV(), c.getPublicKey());
        System.out.println(s);
        System.out.println(DoEncryption.doDecryption(s, c.getSecretKey(), c.getIV(), c.getPrivateKey()));

        AssimetricEncryption.generateKeyPair();
        byte[] keyencryption = AssimetricEncryption.encrypt(key.getEncoded());

        System.out.print("Key Encryption: ");
        for (byte a : keyencryption)
            System.out.print((char) a);
        System.out.println();

        // Desencrypt process
        byte[] newkey = AssimetricEncryption.decrypt(keyencryption);
        String newstring = SimetricEncryption.doAESDecryption(simetric,
                new SecretKeySpec(newkey, 0, newkey.length, "AES"), iv);

        System.out.println("Desencrypted message: " + newstring);
        System.out.println("Success: " + (str.equals(newstring) ? "True" : "False")); */
    }
}