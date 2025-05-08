
import java.io.*;
import java.net.*;
public class TupleSpaceClient {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java TupleSpaceClient <server_host> <server_port> <request_file>");
            System.exit(1);
        }
        String serverHost = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String requestFile = args[2];

        try (BufferedReader fileReader = new BufferedReader(new FileReader(requestFile));
             Socket socket = new Socket(serverHost, serverPort);
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
