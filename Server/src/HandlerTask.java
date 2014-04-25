import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class HandlerTask implements Runnable {

    private Socket client;
    private char[] buffer = new char[32];
    private final Random random;
    private final char[] alphaNumeric;

    public HandlerTask(Socket client) {
        this.client = client;
        this.alphaNumeric = createAlphaNumericCharArray();
        this.random = new Random(client.hashCode());
    }


    @Override
    public void run() {
        try {
            // output stream for writing bytes to the client
            OutputStream outputStream = client.getOutputStream();

            // input steam for reading bytes from the client
            InputStream inputStream = client.getInputStream();

            // wait for client to send
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            int read = bufferedReader.read(buffer, 0, buffer.length);


            Integer readNumber = getNumber(buffer, read);

            // increment number and create random String
            Integer incrementedNumber = readNumber + 1;
            String randomString = getRandomString(10, 20);

            // write back to the client
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            printWriter.print(incrementedNumber + " " + randomString);
            printWriter.flush();

            // wait for client to send the modified String
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            read = bufferedReader.read(buffer, 0, buffer.length);
            String msg = new String(buffer, 0, read);

            // Verify the String
            String verify = "FAIL";
            if (msg.equals(switchCases(randomString))) {
                verify = "OK";
            }

            // write back to the client
            printWriter = new PrintWriter(outputStream, true);
            printWriter.print(verify);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param buffer the char array
     * @param read   the count of read chars from the stream
     * @return the interpreted number
     */
    private Integer getNumber(char[] buffer, int read) {
        String readString = String.valueOf(buffer, 0, read).trim();
        System.out.println(String.format("buffer: '%s'", readString));
        Integer number = getIntFromBinary(readString);
        System.out.println(String.format("Received number %d", number));
        return number;
    }

    /**
     * @param min length
     * @param max length
     * @return an array with [A-Za-z0-9]{10,20}
     */
    private String getRandomString(int min, int max) {
        int length = random.nextInt(max - min + 1) + min;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(alphaNumeric[random.nextInt(alphaNumeric.length)]);
        }
        return sb.toString();
    }

    /**
     * @return an array with [A-Za-z0-9]
     */
    private char[] createAlphaNumericCharArray() {
        StringBuilder buf = new StringBuilder(128);
        for (int i = 48; i <= 57; i++) buf.append((char) i); // 0-9
        for (int i = 65; i <= 90; i++) buf.append((char) i); // A-Z
        for (int i = 97; i <= 122; i++) buf.append((char) i); // a-z
        return buf.toString().toCharArray();
    }

    /**
     * @param msg a message to switch the cases
     * @return msg with changed the upper case letters to lower case letters and vice-versa
     */
    private String switchCases(String msg) {
        StringBuilder sb = new StringBuilder(msg.length());
        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (Character.isUpperCase(c)) {
                sb.append(Character.toUpperCase(c));
            } else if (Character.isDigit(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * @param b the binary String
     * @return int value of the binary
     */
    private int getIntFromBinary(String b) {
        return Integer.parseInt(b, 2);
    }
}
