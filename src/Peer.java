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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

interface Peer {
    // Update/Start (if first contact) the Client for communication
    public void updateClient(String ip, Integer port) throws InvalidKeyException, NoSuchAlgorithmException,
    NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;

    // Update/Start (if first contact) the Server for messages
    public void updateServer(Integer port) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
    IllegalBlockSizeException, BadPaddingException;
    
    // Update the SecretKey used for Simetric encryption
    public boolean refreshSecretKey();

    // Update the KeyPairs used for Assimetric encryption
    public boolean refreshKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException;

    // Update the Initialization Vector used in Symmetric Encryption
    public boolean refreshIV();

    // Get Public key used on the Assimetric encryption
    public RSAPublicKey getPublicKey();

    // Run Client and Server Socket
    public void execute() throws InterruptedException, ExecutionException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;

}
