package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xBlackCat Date: 12.07.11
 */
public final class RunChecker {
    private static final Log log = LogFactory.getLog(RunChecker.class);
    private static final String REQUEST_STRING = "Are you alive?";
    private static final String RESPONSE_STRING = "I'm still alive!";
    private static final String REQUEST_SHUTDOWN_STRING = "Die at will!";
    private static final String RESPONSE_SHUTDOWN_STRING = "Aye aye, sir!";

    public static final int DEFAULT_CHECKER_PORT = 29898;

    private final String requestString;
    private final String responseString;
    private final String requestShutdownString;
    private final String responseShutdownString;
    private final int port;

    /**
     * Create a checker with default parameters
     */
    public RunChecker() {
        this(DEFAULT_CHECKER_PORT);
    }

    public RunChecker(int port) {
        this(port, REQUEST_STRING, RESPONSE_STRING, REQUEST_SHUTDOWN_STRING, RESPONSE_SHUTDOWN_STRING);
    }

    public RunChecker(int port, String requestString, String responseString, String requestShutdownString, String responseShutdownString) {
        this.port = port;
        this.requestString = requestString;
        this.responseString = responseString;
        this.requestShutdownString = requestShutdownString;
        this.responseShutdownString = responseShutdownString;
    }

    public boolean installNewInstanceListener(Runnable process, Runnable shutdownProcess) {
        final ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port, 1);
        } catch (IOException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not install bind listener", e);
            }

            return false;
        }

        if (!serverSocket.isBound()) {
            return false;
        }

        Thread thread = new Thread(new LaunchListener(serverSocket, process, shutdownProcess));
        thread.setDaemon(true);
        thread.start();

        return true;
    }

    public boolean performCheck(boolean shutdown) {
        // First - check if already run
        try {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("127.0.0.1", port), 500);

                if (!socket.isConnected()) {
                    return false;
                }

                try (DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
                    try (DataInputStream is = new DataInputStream(socket.getInputStream())) {

                        os.writeUTF(shutdown ? requestShutdownString : requestString);
                        os.flush();

                        String resp = is.readUTF();
                        if (responseString.equals(resp)) {
                            // Application is running and moved to the front
                            return true;
                        } else if (responseShutdownString.equals(resp)) {
                            // Double check
                            return performCheck(false);
                        }
                    }

                }

            }

        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to communicate with brother", e);
            }
        }

        return false;
    }

    private class LaunchListener implements Runnable {
        private final ServerSocket serverSocket;
        private final Runnable process;
        private final Runnable shutdownProcess;

        public LaunchListener(ServerSocket serverSocket, Runnable process, Runnable shutdownProcess) {
            if (process == null) {
                throw new NullPointerException("Actions after establishing connection is not defined");
            }
            if (shutdownProcess == null) {
                throw new NullPointerException("Shutdown actions is not defined");
            }

            this.serverSocket = serverSocket;
            this.process = process;
            this.shutdownProcess = shutdownProcess;
        }

        @Override
        public void run() {
            try {
                try {
                    while (!serverSocket.isClosed()) {
                        try {

                            try (Socket socket = serverSocket.accept()) {

                                try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
                                    String string = inputStream.readUTF();
                                    if (requestString.equals(string)) {
                                        process.run();

                                        try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                                            outputStream.writeUTF(responseString);
                                        }

                                    } else if (requestShutdownString.equals(string)) {
                                        shutdownProcess.run();

                                        try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                                            outputStream.writeUTF(responseShutdownString);
                                        }


                                        if (log.isDebugEnabled()) {
                                            log.debug("Shutdown by other instance request.");
                                        }
                                        System.exit(0);
                                    }
                                }

                            }

                        } catch (IOException e) {
                            if (log.isWarnEnabled()) {
                                log.warn("Failed to communicate with brother.", e);
                            }
                        }
                    }
                } finally {
                    serverSocket.close();
                }
            } catch (IOException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Exception occurs while processing check", e);
                }
            }
        }
    }
}
