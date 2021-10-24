package infinite_zoomer.server;

import infinite_zoomer.gui.HTMLGUI;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles an HTTP request from a client
 */

public class ClientHandler extends Thread {
    // TODO: Find the HTML directory???
    private static final String PATH_TO_HTML = "./app/src/main/resources/html";
    private static final String ERROR_404_MESSAGE = "<!DOCTYPE html>" +
            "<html>" +
            "<head><title>404 Error</title></head>" +
            "<body>" +
            "<h1>404: File Not Found</h1>" +
            "<a href='/index.html'>Back to the app!</a>" +
            "</body>" +
            "</html>";

    private static final Pattern GET_REQUEST_PATTERN = Pattern.compile("^GET (.*) HTTP/1.1");
    private final Scanner mInput;
    private final PrintWriter mOutput;
    private final HTMLGUI mGui;

    public ClientHandler(HTMLGUI gui, InputStream in, OutputStream out) {
        mInput = new Scanner(in);
        mOutput = new PrintWriter(out);
        mGui = gui;
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
            mOutput.printf("Content-length: %d%n", ERROR_404_MESSAGE.length() + 2);
            mOutput.println();
            mOutput.println();
            mOutput.println(ERROR_404_MESSAGE);
            mOutput.println();
            mOutput.println();

            return;
        }

        mOutput.println("HTTP/1.1 200 OK");
        mOutput.println("Content-type: text/html");
        mOutput.printf("Content-length: %d%n", file.length() + 1);
        mOutput.println();
        mOutput.println();

        try {
            int len = (int) file.length();

            if (len != file.length()) {
                throw new IOException("File too large.");
            }

            FileReader r = new FileReader(file);
            char[] buff = new char[len];

            r.read(buff);
            mOutput.write(buff);
            r.close();
        } catch (IOException ex) {
            System.err.println("Unable to read file.");
            ex.printStackTrace();
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

        Matcher matcher = GET_REQUEST_PATTERN.matcher(requestName);
        if (matcher.find()) {
            requestName = matcher.group(1);
            System.out.println(requestName);
        }

        requestName = requestName.trim();
        if (requestName.startsWith("/") || requestName.startsWith("\\")) {
            requestName = requestName.substring(1);
        }

        if (requestName.equals("")) {
            requestName = "index.html";
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
        String request = mInput.nextLine().trim();
        System.out.printf("Got a request: %s%n", request);

        if (request.startsWith("GET /")) {
            sendFile(getFile(request));
        } else {
            // TODO: Remove this, it's for debugging
            System.out.println("Got an API request!");
            System.out.println(request);

            // TODO: Forward the request to mGui, which can then respond.
            String response = "TODO: Implement";
            mOutput.println("HTTP/1.1 200 OK");
            mOutput.println();
            mOutput.println();
            mOutput.println(response);
        }

        // Finish sending content.
        mOutput.flush();
        mInput.close();
        mOutput.close();
    }
}
