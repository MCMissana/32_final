
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Ethan Palser, Mathew Erwin, Michael Missana
 */
public class MainClass {

    public static void main(String args[]) throws IOException {
        
        
        // Selector: multiplexor of SelectableChannel objects
        Selector selector = Selector.open(); // selector is open here

        // ServerSocketChannel: selectable channel for stream-oriented listening sockets
        ServerSocketChannel Socket = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("localhost", 4381);

        // Binds the channel's socket to a local address and configures the socket to listen for connections
        Socket.bind(address);

        // Adjusts this channel's blocking mode.
        Socket.configureBlocking(false);

        int ops = Socket.validOps();
        SelectionKey selectKy = Socket.register(selector, ops, null);

        

        // Infinite loop..
        // Keep server running
        while (true) {

            System.out.println("i'm a server and i'm waiting for new connection and buffer select...");
            // Selects a set of keys whose corresponding channels are ready for I/O operations
            selector.select();

            // token representing the registration of a SelectableChannel with a Selector
            Set<SelectionKey> Keys = selector.selectedKeys();
            Iterator<SelectionKey> Iterator = Keys.iterator();

            while (Iterator.hasNext()) {
                SelectionKey myKey = Iterator.next();

                // Tests whether this key's channel is ready to accept a new socket connection
                if (myKey.isAcceptable()) {
                    SocketChannel Client = Socket.accept();

                    // Adjusts this channel's blocking mode to false
                    Client.configureBlocking(false);

                    // Operation-set bit for read operations
                    Client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Connection Accepted: " + Client.getLocalAddress() + "\n");

                    // Tests whether this key's channel is ready for reading
                } else if (myKey.isReadable()) {

                    SocketChannel Client = (SocketChannel) myKey.channel();
                    ByteBuffer crunchifyBuffer = ByteBuffer.allocate(256);
                    Client.read(crunchifyBuffer);
                    String result = new String(crunchifyBuffer.array()).trim();

                    System.out.println("Message received: " + result);

                    if (result.equals("FINISHED")) {
                        Client.close();
                        System.out.println("Closing client connection");
                        System.out.println("\nServer will keep running. Try running client again to establish new connection");
                    }
                }
                Iterator.remove();
            }
        }
        
        
        
        
        
        
        
        
        
        
        
        /*
        //////////////////////////////////////////////////////
        // file to read from
        File file = new File("./src/testdata.txt"); // src dirctory not working
        // file to write from, may change
        File log = new File("./src/logdata.txt");
        // start the server
        Server skynet = new Server(log); // note: avoid waiting for connection in constructor
        
        // wait here until skynet can finish
        
        skynet.close(); // closes writers in server

        */
    }
}
