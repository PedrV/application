/* 
Create a socket to act as server, this will look for incoming connections from possible clients 
*/

package src;

// A Java program for a Server 
import javafx.application.Application;

import java.net.*;
import java.io.*; 
  
public class Server { 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    svsoket   = null; 
    private DataInputStream in       = null;
    String name = "Client";
  
    private boolean readConnection () {
        String line = "";
        try { 
            line = in.readUTF();
            if(line.equalsIgnoreCase("over")) return false;
            System.out.println("[" + name + ",d]: " + line);

        } catch(IOException i) { 
            System.out.println(i); 
        }
        return true;
    }

    // constructor with port 
    public Server(int port) { 
        // starts server and waits for a connection 
        try { 
            svsoket = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = svsoket.accept(); 
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 
  
            String line = ""; 
  
            // reads message from client until "Over" is sent 
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
