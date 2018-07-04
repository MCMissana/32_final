import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author E
 */
public class Server {
    
    private BufferedWriter writer;
    private BufferedWriter writerResult;
    File result;
    File log;
    double esta = 0; // total
    
    public Server(File logfile){
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
