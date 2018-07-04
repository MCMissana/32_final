
import java.io.*;
import java.net.*;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class Server {

    public static void main(String args[]) {
        // file to write from, may change
        File log = new File("./src/logdata.txt");

        // number of threads to create read from file
        int threadCount = 0;

        try {
            ServerSocket serSock = new ServerSocket(4380);
            Socket clientSock = serSock.accept();
            System.out.println("Client connected");
            
            PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSock.getInputStream()));
            
            // infinite loop until a message says to end
            while (true) {
                System.out.println("Loop started");
                String message = "0";
                while((message = in.readLine()) != null && message != ""){
                    System.out.println("Message read");
                    System.out.println(message);
                }
                if (message.equals("TERMINATE")) {
                    break;
                } else {
                    threadCount = Integer.parseInt(in.readLine());

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
                }
                System.out.println("End");
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Exception" + ex);
        }
    }
}