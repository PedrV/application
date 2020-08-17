/* 
Create a socket to act as server, this will look for incoming connections from possible clients 
Need to correct the way Server and Client act afer client disconnect without previous warning
*/

package src;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
  
public class Server { 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    svsocket = null;
    private DataInputStream in       = null;
    private String          name     = "Client";
  
    private boolean readConnection () {
        String inputline = "";

        try { 
            inputline = in.readUTF();

            if(inputline.equalsIgnoreCase("over")) return false;
            System.out.println("[" + name + "]: " + inputline);

        } catch(IOException i) { 
            System.out.println(i); 
        }
        return true;
    }

    // constructor with port 
    public Server(int port) { 
        // starts server and waits for a connection 
        try { 
            svsocket = new ServerSocket(port);
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = svsocket.accept();
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // reads message from client until "over" is sent
            while (readConnection()){}

            System.out.println("Closing connection"); 
  
            // close connection 
            socket.close(); 
            in.close();
            return;
        }
        catch(IOException i) { 
            System.out.println(i); 
        } 
    } 
} 
