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
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

class Text {
    private static final String VERSION = "1.0";
    private static final String ABOUT = "@Insert name, is a direct message application that enables users to communicate without revealing their identity to others on the internet nor having to worry about message logs.\n All messages are encrypted with a AES 128bit key followed by a 4096bit length RSA encryption.\n The RSA Public Keys must be exchanged for a communication to begin.\n For more details see the application github page";
    private Scanner scan;

    Text (Scanner scan) {
        this.scan = scan;
    }

    public Integer seeTextMenu() {
        System.out.println("@Insert name  Copyright (C) 2020  Andre Silva");
        System.out.println("Peer to Peer Message App");
        System.out.println("Options: ");
        System.out.println("1. Reveal Public Key (RSA)");
        System.out.println("2. Refresh RSA Keys");
        System.out.println("3. Establish a connection");
        System.out.println("4. See Stored RSA Public Keys");
        System.out.println("5. Details");
        System.out.println("0. Exit");

        System.out.print("> ");
        int option = scan.nextInt();
        scan.nextLine(); // Clear newline left by int scan
        return option;
    }

    public void seeDetailsMenu() {
        System.out.println();
        System.out.println("<-------------------------------------------------------->");
        System.out.println("Version: " + VERSION);
        System.out.println("About: " + ABOUT);
        System.out.println();
        System.out.println("Authors: ");
        System.out.println("Pedro Vieira @https://github.com/PedrV");
        System.out.println("Andre Silva @https://github.com/mastersilvapt");
        System.out.println();
        System.out.println("© 2020 Andre Silva.  All rights reserved.");
        System.out.println("<-------------------------------------------------------->");
        System.out.println();
        System.out.println();
        System.out.println();
    }
}

public class Menu {
    public static void main(String[] args)
            throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException, ExecutionException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        CreatePeer source = new CreatePeer();
        Scanner scan = new Scanner(System.in);
        Text getText = new Text(scan);
        
        int option = getText.seeTextMenu();

        while (option > 5 || option < 0) {
            System.out.println("Option not available!");
            System.out.println("Please try again!");
            System.out.print("> ");
            option = scan.nextInt();
        }

        while (true) {
            if (option == 1) {
                System.out.println ("-----BEGIN PUBLIC KEY-----");
                System.out.println (Base64.getMimeEncoder().encodeToString(source.getPublicKey().getEncoded()));
                System.out.println ("-----END PUBLIC KEY-----"); 

            } else if (option == 2) {
                System.out.println("This will reset all communications are you sure that you want to continue? Y/N");
                System.out.print("> ");
                String anwser = scan.nextLine();

                if (!anwser.equals("N") && !anwser.equals("No") && !anwser.equals("NO") && !anwser.equals("no") && !anwser.equals("n")) {
                    if (source.refreshKeyPair()) System.out.println("RSA Keys updated successfully!");
                    else System.out.println("RSA Keys not updated successfully!");
                }

            } else if (option == 3) {
                System.out.println("Temporary test of connection. In the future only a RSA key will be needed.");

                System.out.print("Name of the contact: ");
                source.setClientName(scan.nextLine());

                System.out.println("Insert the other user RSA PublicKey: (Deactivated)");

                System.out.print("Insert IP: ");
                source.setIP(scan.nextLine());
                System.out.print("Insert a port: ");
                source.setPort(scan.nextInt());
                scan.nextLine();
                
                source.execute();

            } else if (option == 5) {

                getText.seeDetailsMenu();

            } else if (option == 0) {
                System.out.println("Erasing data...");
                source.refreshKeyPair();
                source.refreshSecretKey();
                source.refreshIV();
                break;
            }
            
            option = getText.seeTextMenu();
        }

        scan.close();
    }
}
