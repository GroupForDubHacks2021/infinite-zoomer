package infinite_zoomer.server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Handles an HTTP request from a client
 */

public class ClientHandler extends Thread {
    // TODO: Find the HTML directory???
    private static final String PATH_TO_HTML = "app/src/resources/html";

    private final Scanner mInput;
    private final PrintWriter mOutput;

    public ClientHandler(InputStream in, OutputStream out) {
        mInput = new Scanner(in);
        mOutput = new PrintWriter(out);
    }

    /**
     * Sends a 200 OK response to the client or 404 ERROR
     * if the given file is null.
     *
     * @param file File to send
     */
    private void sendFile(File file) {
        // Handle 404 error case.
        if (file == null) {
            mOutput.println("HTTP/1.1 404 ERROR");
            mOutput.println("Content-type: text/html");

        }
    }

    /**
     * Get a file that was requested.
     *
     * @param requestName Name of the requested file.
     * @return The requested file or null if no such file was found.
     */
    private File getFile(String requestName) {
        File htmlDir = new File(PATH_TO_HTML);
        assert(htmlDir.isDirectory());

        requestName = requestName.trim();
        if (requestName.startsWith("/") || requestName.startsWith("\\")) {
            requestName = requestName.substring(1);
        }

        for (File f : htmlDir.listFiles()) {
            if (f.getName().equals(requestName)) {
                return f;
            }
        }

        return null;
    }

    /**
     * Run the ClientHandler synchronously.
     * {@see Thread::start}
     */
    @Override
    public void run() {
        String request = mInput.nextLine();

        if (request.startsWith("HTTP/1.1 GET /index.html")) {
            mOutput.println("HTTP/1.1 200 OK");
            mOutput.println("Content-type: text/html");
        } else {
            // TODO: Remove this, it's for debugging
            System.out.println("Got a request!");
            System.out.println(request);
        }

        mInput.close();
        mOutput.close();
    }
}
