
import java.io.*;
import java.net.*;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class MainClass {

    public static void main(String args[]) {
        // file to read from
        File file = new File("./src/testdata.txt"); // src dirctory not working
        
        int currentChar;
        try {
            // create a socket to our server
            Socket sock = new Socket();
            // connect to server
            sock.connect(new InetSocketAddress("localhost", 4380));
            
            // create a stream that will be reading from a file
            FileInputStream input = new FileInputStream(file);
            // stream is for StreamReader and made efficient with a BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            int threadCount;
            String parameter = ""; // builds the thread count read from file
            
            while ((currentChar = reader.read()) != -1) {
                //if new line character we have our limit parameter
                if (currentChar == (char) '\n') {
                    threadCount = Integer.valueOf(parameter);
                    
                    // send to server the threadCount
                    OutputStream out = sock.getOutputStream();
                    out.write(threadCount);
                    out.flush();
                    // message sent on flush
                    
                    parameter = ""; //reset our parameter
                } else {
                    if ((char) currentChar != (char) 13) {
                        parameter += (char) currentChar;
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Exception" + ex);
        }
    }
}
