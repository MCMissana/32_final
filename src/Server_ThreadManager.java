
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class Server_ThreadManager {

    public static int limit;
    private static Server_ThreadManager manager;
    private BufferedWriter writer;
    private BufferedWriter writerResult;
    File result;
    File log;
    double esta = 0; // total
    boolean[] locks; // tracks lock state of threads to limit critical region access.
    int count; // increments when thread adds term

    private Server_ThreadManager(int threadCount, File logfile) {
        // instantiate arrays and count
        result = new File("./src/output.txt");
        log = logfile;
        try {
            FileOutputStream output = new FileOutputStream(log);
            writer = new BufferedWriter(new OutputStreamWriter(output));

            FileOutputStream output2 = new FileOutputStream(result);
            writerResult = new BufferedWriter(new OutputStreamWriter(output2));

        } catch (IOException ex) {
            //nothing, need to be careful if cannot be found
        }
        limit = threadCount;
        locks = new boolean[limit]; // default value is false
        count = 0;
    }

    /**
     * With the constructor private this will return this object and will ensure
 that there will only ever be one instance of Server_ThreadManager, if
 this class has not been instantiated it will be when this method is
 called.
     *
     * @return This
     */
    public static Server_ThreadManager getInstance() {
        if (manager == null) {
            // manager = new Server_ThreadManager(limit, file); // may not be allowed to work
        }
        return manager;
    }

    /**
     * With the constructor private this will return this object and will ensure
 that there will only ever be one instance of Server_ThreadManager, if
 this class has not been instantiated it will be when this method is
 called.&nbspThis has been altered to act like the constructor of the
     * class without allowing multiple instances of the manager to exist.
     *
     * @return This
     */
    public static Server_ThreadManager setInstance(int threadCount, File logfile) {
        if (manager == null) {
            manager = new Server_ThreadManager(threadCount, logfile);
        }
        limit = threadCount;
        return manager;
    }

    /**
     * This method sends a message/value to the server for it to receive
     * and handle.
     * 
     * @param termValue 
     */
    public void sendMessage(double termValue){
        // send a message via buffer to server with a byte version of termValue
    }
    
    /**
     * This method listens for a message from the server for it to use.
     */
    public void receiveMessage(){
        // do something with byte stream sent, change void to an actual type
        byte[] message;
    }
    
     
    /**
     * This method adds the termValue to the total estimate for PI/4, and
     * increments a counter to check when enough terms have been added.
     *
     * @param termVal
     */
    public void add(double termVal) {
        esta += termVal;
    }

    /**
     * This method returns the current value of estimate for PI/4.
     *
     * @return
     */
    public double result() {
        return esta;
    }
    
    /**
     * This method returns the current count for terms added to the estimate.
     *
     * @return
     */
    public int count() {
        return count;
    }

    /**
     * This method takes the thread number and sets its lock to true, which is
     * used to prevent access into certain (critical) regions of code.
     *
     * @param threadNum
     */
    public void lock(int threadNum) {
        locks[threadNum] = true;
    }

    /**
     * This method takes the thread number and sets its lock to false, which is
     * used to prevent access into certain (critical) regions of code, releasing
     * it so it may access the region again or inform other threads it is
     * available.
     *
     * @param threadNum
     */
    public void unlock(int threadNum) {
        locks[threadNum] = false;
    }

    /**
     * This method returns the current status of the lock, and is considered
     * locked if true.
     *
     * @param threadNum
     * @return
     */
    public boolean isLock(int threadNum) {
        return locks[threadNum] == true;
    }

    /**
     * This method returns the current status of the lock, and is considered
     * unlocked if false.
     *
     * @param threadNum
     * @return
     */
    public boolean isUnlock(int threadNum) {
        return locks[threadNum] == false;
    }

    /**
     * This method is used to set the locks all to false
     */
    public void reset() {
        locks = new boolean[limit]; // default value is false
        count = 0;
    }

    /**
     * This method outputs to the result/output file.
     */
    public void printResult() {
        try {
            writerResult.write("Current Estimate: " + this.result() + '\n');
        } catch (IOException ex) {
            //nothing, need to be careful if cannot be found
        }
    }

    /**
     * This method outputs to the log file.
     */
    public void printLog(double threadVal) {
        try {
            writer.write("Current Estimate: " + this.result()
                    + " | Current Thread Value: " + threadVal + '\n');
        } catch (IOException ex) {
            //nothing, need to be careful if cannot be found
        }
    }
    
    /**
     * This method closes the writer streams that print to
     * the log and output files.
     */
    public void close() {
        try {
            writerResult.close();
            writer.close();
        } catch (IOException ex) {
            //nothing, need to be careful if cannot be found
        }
    }
}
