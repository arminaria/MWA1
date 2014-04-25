import javafx.concurrent.Task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jab on 22.04.2014.
 */
public class Server implements Runnable {

    private final Integer port;
    private Server thread;
    private ExecutorService pool;
    private ServerSocket serverSocket;

    public Server(Integer port) {
        System.out.println("initializing server...");
        this.port = port;
        pool = Executors.newCachedThreadPool();
        thread = this;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            InetAddress inetAddress = serverSocket.getInetAddress();
            System.out.println(String.format("Server started on port %d", port));
            System.out.println("Host address: "+ inetAddress.getHostName());
        } catch (IOException e) {
            System.out.println(String.format("could not create Server socket on port %d", port));
            System.exit(1);
        }

        while (thread != null) {
            // wait for a client
            System.out.println("waiting for new client");
            final Socket socket;
            try {
                socket = serverSocket.accept();
                System.out.println("client connected");

                // create an handler for the client
                HandlerTask handlerTask = new HandlerTask(socket);

                // run the handler in a thread from the pool
                pool.execute(handlerTask);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
