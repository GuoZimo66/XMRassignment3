import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class TupleSpaceServer {
    private final int port;
    private final Map<String, String> tupleSpace;
    private int clientCount;
    private int operationCount;
    private int readCount;
    private int getCount;
    private int putCount;
    private int errorCount;
    public TupleSpaceServer(int port) {
        this.port = port;
        this.tupleSpace = new ConcurrentHashMap<>();
        this.clientCount = 0;
        this.operationCount = 0;
        this.readCount = 0;
        this.getCount = 0;
        this.putCount = 0;
        this.errorCount = 0;
        startSummaryPrinter();
    }
    public static void main(String[] args) {
    }
        private void startSummaryPrinter() {
            Thread summaryThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(UPDATE_INTERVAL);
                        printSummary();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            summaryThread.start();
        }


    private void printSummary() {
        int totalSize = 0;
        int totalKeySize = 0;
        int totalValueSize = 0;
        for (Map.Entry<String, String> entry : tupleSpace.entrySet()) {
            totalSize += entry.getKey().length() + entry.getValue().length();
            totalKeySize += entry.getKey().length();
            totalValueSize += entry.getValue().length();
        }
        int tupleCount = tupleSpace.size();
        double avgTupleSize = tupleCount > 0 ? (double) totalSize / tupleCount : 0;
        double avgKeySize = tupleCount > 0 ? (double) totalKeySize / tupleCount : 0;
        double avgValueSize = tupleCount > 0 ? (double) totalValueSize / tupleCount : 0;

        System.out.printf("Tuples: %d, Avg Tuple Size: %.2f, Avg Key Size: %.2f, Avg Value Size: %.2f, " +
                        "Clients: %d, Operations: %d, Reads: %d, Gets: %d, Puts: %d, Errors: %d%n",
                tupleCount, avgTupleSize, avgKeySize, avgValueSize, clientCount, operationCount, readCount, getCount, putCount, errorCount);
    }


    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private class ClientHandler extends Thread {
            private final Socket clientSocket;

            public ClientHandler(Socket socket) {
                this.clientSocket = socket;
            }

            @Override
            public void run() {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        operationCount++;
                        String response = processRequest(inputLine);
                        out.println(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

    }

}
