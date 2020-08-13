/* 
Test for Client Side Socket
*/

package src.com.lux;

public class AppClient {
    public static void main(String[] args) {
        Client cl = null;
        cl = new Client("localhost", 4441);
    }
}