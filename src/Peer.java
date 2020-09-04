package src;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPublicKey;

interface Peer {
    // Update/Start (if first contact) the Client for communication
    public void updateClient(String ip, Integer port);

    // Update/Start (if first contact) the Server for messages
    public void updateServer(Integer port);

    // Update the SecretKey used for Simetric encryption
    public void refreshSecretKey();

    public byte[] generateInitiazationVector();

    // Update the KeyPairs used for Assimetric encryption
    public void refreshKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException;

    // Get Public key used on the Assimetric encryption
    public RSAPublicKey getPublicKey();

    // Run Client and Server Socket
    public void execute();

}
