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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Client {
    // initialize socket and input output streams
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream out;

    // constructor to put ip address and port
    Client(String address, int port, SecretKey sKey, byte[] miv, RSAPublicKey pubkey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        // establish a connection 
        try { 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
  
            // takes input from terminal 
            input  = new BufferedReader(new InputStreamReader(System.in)); 
            

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
                out.writeUTF(DoEncryption.doEncryption(line, sKey, miv, pubkey)); 
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
