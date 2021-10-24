package infinite_zoomer.server;

import infinite_zoomer.gui.HTMLGUI;
import infinite_zoomer.model.DrawingModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Handles web-based connections to the application.
 */

public class HTMLServer extends Thread {
    private static final int SERVER_PORT = 8000;
    private final ServerSocket mSocket;
    private final HTMLGUI mGui;

    public HTMLServer(HTMLGUI gui) {
        mGui = gui;

        // References:
        //  * https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
        //  * https://ssaurel.medium.com/create-a-simple-http-web-server-in-java-3fc12b29d5fd
        try {
            mSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Unable to open a server!");
            e.printStackTrace();

            throw new ServerStartException();
        }
    }

    /**
     * Get the port the server can be accessed over.
     * The server can then be accessed via a URL like
     * http://localhost:SERVER_PORT_HERE, once we've started accepting
     * connections.
     * {@see Thread::start}
     *
     * @return A port number.
     */
    public int getPort() {
        return SERVER_PORT;
    }

    /**
     * Called by Thread::start()
     */
    @Override
    public void run() {
        // At this point, we're running in a thread. We can accept connections
        // without blocking program flow.
        while (true) {
            acceptConnection();
        }
    }

    /**
     * Handles a single remote connection. This method blocks
     * until a peer is ready to be handled (i.e. it's okay to call
     * from within a while true loop).
     */
    private void acceptConnection() {
        try {
            final Socket client = mSocket.accept();
            final ClientHandler handler = new ClientHandler(mGui, client.getInputStream(), client.getOutputStream());

            // TODO: We're starting a new thread here for every incoming client.
            //       Do we want to do this?
            handler.start();
        } catch (IOException ex) {
            System.err.println("[NONFATAL] Error accepting remote connection.");
            ex.printStackTrace();
        }
    }

    public static class ServerStartException extends Error {
    }
}
