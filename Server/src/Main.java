import java.io.IOException;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        Integer port = 12345;
        if (args.length <= 0) {
            System.out.println("no arguments passed");
            System.out.println("\t you can pass the port as the first argument");
            System.out.println("\t will take the default port 12345");
        } else {
            // read port
            try {
                port = Integer.parseInt(args[0]);
                System.out.println(String.format("port is %d ", port));
            } catch (Exception e) {
                System.out.println("The Port should be a number");
                System.exit(1);
            }
        }

        String localHost = InetAddress.getLocalHost().getHostName();
        for (InetAddress ia : InetAddress.getAllByName(localHost)) {
            if (!ia.isAnyLocalAddress())
                System.out.println(ia);
        }

        // Start Server Thread
        Server server = new Server(port);
        Thread serverThread = new Thread(server);
        serverThread.start();

    }
}
