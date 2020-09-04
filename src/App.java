package src;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.SecretKey;

class CreatePeer implements Peer {
    private Client cl; // Client Socket that invites the messages to the othe Peer Server
    private Server sv; // Server Socket that recieves messages from other Peers Clients
    private SecretKey mySKey; // Secret Key for the symmetric encryption
    private KeyPair myKP; // KeyPair (private, public) for asymmetric encryption
    private byte[] iv;  // Initialization Vector for symetric Encryption
    private String ip; // IP of the connection
    private Integer port; // Port of the connection

    // TODO: discuss storing of other persons Simetric Key

    CreatePeer(String ip, Integer port) throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException,
            ExecutionException {
        this.mySKey = generateSimetricSecretKey();
        this.myKP = generateKeyPair();
        this.ip = ip;
        this.port = port;
        execute();
    }

    // ------------------------------------------------------------------------------------------------------------------ \\
    private SecretKey generateSimetricSecretKey() {
        return SymmetricEncryption.createAESKey();
    }

    // ------------------------------------------------------------------------------------------------------------------ \\
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        return AsymmetricEncryption.generateKeyPair();
    }


    // ------------------------------------------------------------------------------------------------------------------ \\
    // Update/Start (if first contact) the Client for communication
    @Override
    public void updateClient(String ip, Integer port) {
        this.cl = new Client(ip, port);
    }

    // Update/Start (if first contact) the Server for messages
    @Override
    public void updateServer(Integer port) {
        this.sv = new Server(port);
    }


    // ------------------------------------------------------------------------------------------------------------------ \\
    // Generate the Initialization Vector used for Simetric encryption
    public byte[] generateInitiazationVector() {
        return SymmetricEncryption.createInitializationVector();
    }

    // Update the SecretKey used for Simetric encryption
    @Override
    public void refreshSecretKey() {
        this.mySKey = SymmetricEncryption.createAESKey();
    }

    // Update the KeyPairs used for Assimetric encryption
    @Override
    public void refreshKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        this.myKP = AsymmetricEncryption.generateKeyPair();
    }

    // Get Public key used on the Assimetric encryption
    @Override
    public RSAPublicKey getPublicKey() {
        return (RSAPublicKey) this.myKP.getPublic();
    }


    /** 
     * Execute the 2 methods concurrently.
     * Method A (updateClient) is called according to the interface Callable,
     * then Method B (updateServer) runs and then future.get() will wait for the result of A.
     * (B is going to start running before the end of A)
     * 
     * @see https://stackoverflow.com/questions/30965339/executorcompletionservice-is-not-applicable-for-the-given-arguments
     * @see https://stackoverflow.com/questions/22795563/how-to-use-callable-with-void-return-type 
    */
    public void execute() throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        
        // Implementation of Callable Interface
        Future<Void> future = executorService.submit(new Callable<Void>() {

            public Void call() throws Exception {
                updateClient(ip, port);
                return null;
            }

        });

        updateServer(port);
        future.get(); // Wait for completion of updateClient(ip, port);

        // Close executorService
        executorService.shutdown();
    }
    
}

public class App {
    public static void main(String[] args)
            throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException, ExecutionException {

        new CreatePeer("localhost", 4442);

/*         AssimetricEncryption.generateKeyPair();
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