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
    // If we're running the app from the commandline, (via Gradle), the initial
    // working directory is different from if we're running it from IntelliJ.
    private static final String PATH_TO_HTML = "./app/src/main/resources/html";
    private static final String ALTERNATE_PATH_TO_HTML = "./src/main/resources/html";

    private static final String ERROR_404_MESSAGE = "<!DOCTYPE html>" +
            "<html>" +
            "<head><title>404 Error</title></head>" +
            "<body>" +
            "<h1>404: File Not Found</h1>" +
            "<a href='/index.html'>Back to the app!</a>" +
            "</body>" +
            "</html>";

    private static final Pattern GET_REQUEST_PATTERN = Pattern.compile("^GET (.*) HTTP/1.1");
    private static final Pattern API_REQUEST_PATTERN = Pattern.compile("^GET /api\\?(.*) HTTP/1.1");
    private static final Pattern API_POST_PATTERN = Pattern.compile("^POST /api\\?(.*) HTTP/1.1");
    private final Scanner mInput;
    private final PrintWriter mOutput;
    private final HTMLGUI mGui;

    public ClientHandler(HTMLGUI gui, InputStream in, OutputStream out) {
        mInput = new Scanner(in);
        mOutput = new PrintWriter(out);
        mGui = gui;
    }

    /**
     * @param filename What we should try to detect the content type using.
     */
    private String getContentType(String filename) {
        if (filename.endsWith("js")) {
            return "text/javascript";
        }
        else if (filename.endsWith(".png")) {
            return "image/png";
        }
        else if (filename.endsWith(".css")) {
            return "text/css";
        }

        return "text/html";
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
        mOutput.printf("Content-type: %s%n", getContentType(file.getName()));
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

        if (!htmlDir.exists()) {
            htmlDir = new File(ALTERNATE_PATH_TO_HTML);
        }

        assert(htmlDir.isDirectory());

        System.out.println("Current directory: ");
        System.out.println(System.getProperty("user.dir"));

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

        if (request.startsWith("GET /") && !request.startsWith("GET /api?")) {
            sendFile(getFile(request));
        } else {
            // API Queries

            StringBuilder data = new StringBuilder();
            int contentLength = -1;
            while (mInput.hasNextLine()) {
                String line = mInput.nextLine();
                if (line.startsWith("Content-Length: ")){
                    contentLength = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    System.out.println(contentLength);
                }

                // Headers/post content normally ends with a blank line.
                if (line.trim().equals("")) {
                    for(int i = 0; i < contentLength; i++) {
                        String line_content = mInput.nextLine();
                        data.append(line_content + '\n');
                        i = i + line_content.length();
                    }
                    break;
                }
            }

            Matcher matcher = API_REQUEST_PATTERN.matcher(request);
            if (!request.startsWith("POST") && matcher.find()) {
                request = matcher.group(1);
            }
            else {
                matcher = API_POST_PATTERN.matcher(request);

                if (matcher.find()) {
                    request = matcher.group(1);
                }
            }

            String response = mGui.apiRequest(request, data.toString());
            mOutput.println("HTTP/1.1 200 OK");
            mOutput.println("Content-type: text/plain");
            mOutput.printf("Content-length: %d%n", response.length() + 1);
            mOutput.println();
            mOutput.println();
            mOutput.println(response);
            mOutput.println();
            mOutput.println();
        }

        // Finish sending content.
        mOutput.flush();
        mInput.close();
        mOutput.close();
    }
}
