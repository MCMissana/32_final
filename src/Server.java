
import java.io.*;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class Server {

    public static void main(String args[]) {
        // file to read from
        File file = new File("./src/testdata.txt"); // src dirctory not working
        // file to write from, may change
        File log = new File("./src/logdata.txt");
        // number of threads to create read from file
        int threadCount;

        int currentChar;
        try {
            // create a stream that will be reading from a file
            // stream is for StreamReader and made efficient with a BufferedReader
            FileInputStream input = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // *********** accept connection with client here ************
            // send config file's thread count from client
            String parameter = ""; // builds the thread count read from file

            while ((currentChar = reader.read()) != -1) {
                //if new line character we have our limit parameter
                if (currentChar == (char) '\n') {
                    threadCount = Integer.valueOf(parameter);
                    // setup/initialize Server_ThreadManager
                    Server_ThreadManager temp = Server_ThreadManager.setInstance(threadCount, log);
                    temp.reset();
                    Server_Thread[] threadArr = new Server_Thread[Server_ThreadManager.limit];
                    // populate terms with 0 and create each thread without starting them
                    for (int i = 0; i < Server_ThreadManager.limit; i++) {
                        threadArr[i] = new Server_Thread(i);
                    }
                    // start all threads for for our given limit
                    for (int i = 0; i < threadCount; i++) {
                        threadArr[i].start(); // starts thread which runs parallel to other threads
                    }
                    // join all threads to force main class to wait for threads to complete
                    for (int i = 0; i < threadCount; i++) {
                        try {
                            threadArr[i].join(); // ensures all these threads must complete
                        } catch (InterruptedException ex) {
                        }
                    }
                    //reset our parameter
                    parameter = "";
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
