
import java.util.*;
public class TupleSpaceServer {
    private static final int UPDATE_INTERVAL = 10000; // 10 秒
    private final Map<String, String> tupleSpace;
    private int clientCount;
    private int operationCount;
    private int readCount;
    private int getCount;
    public TupleSpaceServer(int port) {

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

    }
    private class ClientHandler extends Thread {

    }
}
