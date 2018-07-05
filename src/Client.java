
import java.io.*;
import java.net.*;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class Client {

    public static void main(String args[]) {
        // file to read from
        File file = new File("./src/testdata.txt"); // src dirctory not working

        int currentChar;
        try {
            // create a socket to our server
            Socket sock = new Socket();
            // connect to server
            sock.connect(new InetSocketAddress("localhost", 4380));
            System.out.println("socket: " + sock);
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

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
                    System.out.println("sending: (" + threadCount + ")");
                    out.println(threadCount);
                    out.flush();
                    // message sent on flush

                    parameter = ""; //reset our parameter

                    //wait for servers response for x amount of threads
                    String response;
                    System.out.println("Waiting For response");
                    try {
                        // only waits for one response
                        response = in.readLine();
                        if (response != null && !response.equals("")) {
                            System.out.println("server response (" + response + ")");
                        }
                    } catch (IOException ex) {
                    }
                } else {
                    if ((char) currentChar != (char) 13) {
                        parameter += (char) currentChar;
                    }
                }
            }
            //we are now done the config file and can stop our server
            System.out.println("config finished sending terminate");
            out.write("TERMINATE");
            out.flush();
            sock.close();

        } catch (IOException | NumberFormatException ex) {
            System.out.println("Exception" + ex);
        }
            
    }
}
