/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class Client_ThreadManager {

    public static int limit;
    private static Client_ThreadManager manager;
    
    boolean[] locks; // tracks lock state of threads to limit critical region access.
    int count; // increments when thread adds term

    private Client_ThreadManager(int threadCount) {
        // instantiate arrays and count
        limit = threadCount;
        locks = new boolean[limit]; // default value is false
        count = 0;
    }

    /**
     * With the constructor private this will return this object and will ensure
     * that there will only ever be one instance of Client_ThreadManager, if
     * this class has not been instantiated it will be when this method is
     * called.
     *
     * @return This
     */
    public static Client_ThreadManager getInstance() {
        if (manager == null) {
            // manager = new Client_ThreadManager(limit, file); // may not be allowed to work
        }
        return manager;
    }

    /**
     * With the constructor private this will return this object and will ensure
     * that there will only ever be one instance of Client_ThreadManager, if
     * this class has not been instantiated it will be when this method is
     * called.&nbspThis has been altered to act like the constructor of the
     * class without allowing multiple instances of the manager to exist.
     *
     * @return This
     */
    public static Client_ThreadManager setInstance(int threadCount) {
        if (manager == null) {
            manager = new Client_ThreadManager(threadCount);
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

}
