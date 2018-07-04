
import java.io.*;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class MainClass {

    public static void main(String args[]) {
        // file to read from
        File file = new File("./src/testdata.txt"); // src dirctory not working
        // file to write from, may change
        File log = new File("./src/logdata.txt");
        // start the server
        Server skynet = new Server(log); // note: avoid waiting for connection in constructor
        
        // wait here until skynet can finish
        
        skynet.close(); // closes writers in server
    }
}
