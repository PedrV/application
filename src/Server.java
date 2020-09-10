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
Create a socket to act as server, this will look for incoming connections from possible clients 
Need to correct the way Server and Client act afer client disconnect without previous warning
*/

package src;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Server {
    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket svsocket = null;
    private DataInputStream in = null;
    private String name = "Client";

    void setName(String name) {
        this.name = name;
    }

    private boolean readConnection(SecretKey sKey, byte[] miv, RSAPrivateKey privkey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String inputline = "";

        try { 
            inputline = in.readUTF();

            // Commment line bellow to only see the msg
            System.out.println("[" + name + "]: " + "Before Dencryption - " + inputline);

            inputline = DoEncryption.doDecryption(inputline, sKey, miv, privkey);

            if(inputline.equalsIgnoreCase("goodbye")) return false;
            System.out.println("[" + name + "]: " + inputline);

        } catch(IOException i) { 
            System.out.println(i); 
        }
        return true;
    }

    // constructor with port 
    Server(int port, SecretKey sKey, byte[] miv, RSAPrivateKey privkey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        // starts server and waits for a connection 
        try { 
            svsocket = new ServerSocket(port);
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = svsocket.accept();
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // reads message from client until "goodbye" is sent
            while (readConnection(sKey, miv, privkey)){}

            System.out.println("Closing connection"); 
  
            // close connection 
            socket.close(); 
            in.close();
        }
        catch(IOException i) { 
            System.out.println(i); 
        } 
    } 
} 
