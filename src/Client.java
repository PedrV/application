/* 
Create a socket to act as client
Need to correct the way Server and Client act afer client disconnect without previous warning
*/

package src;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Collectors;

public class Client { 
    // initialize socket and input output streams 
    private Socket socket            = null; 
    private BufferedReader   input   = null; 
    private DataOutputStream out     = null; 

    // constructor to put ip address and port 
    public Client(String address, int port) {

        // establish a connection 
        try { 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
  
            // takes input from terminal 
            input  = new BufferedReader(new InputStreamReader(System.in)); 
            
            // convert from BufferReader to String
            input.lines().collect(Collectors.joining());

            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream()); 

        } catch(UnknownHostException u) { 
            System.out.println(u);
             
        }  catch(IOException i) { 
            System.out.println(i); 
        } 
  
        // string to read message from input 
        String line = ""; 
  
        // keep reading until "goodbye" is input 
        while (!line.equalsIgnoreCase("goodbye"))  {
            try { 
                line = input.readLine(); 
                out.writeUTF(line); 
            }
            catch(IOException i) { 
                System.out.println(i); 
            } 
        } 
  
        // close the connection 
        try { 
            input.close(); 
            out.close(); 
            socket.close(); 
        } catch(IOException i) { 
            System.out.println(i); 
        } 
    } 
} 
