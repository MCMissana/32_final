/**
 * This class extends Client_Thread to be an object which can run as its own
 thread,&nbsp however, using extends prevents the object from subclassing
 * another object&nbsp restricting its usage.
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class Client_Thread extends Thread {

    public int threadNum;
    public double aprox;
    public boolean interested; // locally stored check for locking other threads, may not use

    public Client_Thread(int termNum) {
        threadNum = termNum;
    }

    /**
     * Main method of thread which starts when this.start() is called, must be
     * implemented as part of Runnable.&nbspThis does not have the thread
     * stop at any point, using busy wait until its term value is calculated.
     */
    public void run() {
        while (aprox == 0) {
            doAction();
            enter_region(threadNum); // includes lock
            leave_region(threadNum); // includes unlock
            doAction();
        }
    }

    /**
     * This method calculates a term in the Gregory-Leibniz Series,
     * and is only executed in the critical region that has restricted
     * access.
     * 
     * @return 
     */
    public double term() {
        return aprox = (Math.pow(-1, threadNum)) / (2 * threadNum + 1);
    }

    /**
     * This method calculates a random value to force the 
     * thread to waste time.
     */
    public void doAction() {
        calculate((int) (Math.random() * 4 + 36));
    }

    /**
     * Used by doAction().
     * 
     * @param n
     * @return 
     * @see doAction()
     */
    private static long calculate(int n) {
        if (n <= 1) {
            return n;
        } else {
            return calculate(n - 1) + calculate(n - 2);
        }
    }

    /**
     * This method calculates the previous thread in relation to its
     * term value.
     * 
     * @return int that is the previous term
     */
    private int prevThread() {
        int prev;
        prev = threadNum - 1;
        if (threadNum == 0) {
            prev = Client_ThreadManager.limit - 1;
        }
        return prev;
    }

    /**
     * This method has the thread to request entering a critical region
     * by checking if it has been locked out or its previous term is
     * locked, if not the it will enter the region which all threads will
     * be modifying.
     * 
     * @param termNum This method's threadNum and value used in calculation
     */
    private synchronized void enter_region(int termNum) {
        int prev = this.prevThread();
        Client_ThreadManager temp = Client_ThreadManager.getInstance();
        if (temp.isUnlock(prev) || temp.isUnlock(threadNum)) {
            temp.lock(prev);
            temp.lock(termNum);
            // sends term value to thread manager to send to server
            temp.sendMessage(this.term());
            // checks if this is the last thread to execute, using the count
            if(temp.count() >= temp.limit){
                System.out.println("Final thread of batch completed");
            }
        }
        temp = null; // dispose later by java
    }

    /**
     * This method checks if its current value has been updated
     * and if it is currently locked.
     * 
     * @param termNum 
     */
    private synchronized void leave_region(int termNum) {
        int prev = this.prevThread();
        Client_ThreadManager temp = Client_ThreadManager.getInstance();
        
        /**
         * aprox check is for if it has been modified and needs to unlock
         * this solution works specifically for a single use thread that
         * has its value changed once and is not reset
         * It needs to be an OR as it can be unlocked by its next thread if both
         * manage to enter the critical region due to a race condition for entering
         */
        // Change 1
        if (aprox != 0 || temp.isLock(threadNum)) {
            // aprox could be instead replaced with a boolean to check if modified
            temp.unlock(threadNum);
            temp.unlock(prev);
            // deadlock may occur here if unlocked by next thread (opposite side of prev)
        }
        temp = null;
    }
}
