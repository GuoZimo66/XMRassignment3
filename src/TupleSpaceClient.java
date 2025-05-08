import java.io.*;
import java.net.*;

class TupleSpaceClient {
    // 直接设定端口号
    private static final int PORT = 51234;
    // If you want to run it successfully, you need to change the code for the input file path in this part!!!!!!!
    // !!!
    //!!！
    //！！！
    //！！！
    private static final String REQUEST_FILE = "C:\\Users\\ASUS\\Downloads\\test-workload\\client_4.txt";

    public static void main(String[] args) {
        String serverHost = "localhost";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(REQUEST_FILE));
             Socket socket = new Socket(serverHost, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split(" ", 3);
                String command = parts[0];
                String key = parts[1];
                String value = parts.length > 2 ? parts[2] : "";
                if ((key + " " + value).length() > 970) {
                    System.err.println("Error: Request too long: " + line);
                    continue;
                }

                String request;
                switch (command) {
                    case "READ":
                        request = String.format("%03d R %s", 6 + key.length(), key);
                        break;
                    case "GET":
                        request = String.format("%03d G %s", 6 + key.length(), key);
                        break;
                    case "PUT":
                        request = String.format("%03d P %s %s", 7 + key.length() + value.length(), key, value);
                        break;
                    default:
                        System.err.println("Error: Invalid command: " + command);
                        continue;
                }

                out.println(request);
                String response = in.readLine();
                System.out.println(line + ": " + response.substring(4));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}