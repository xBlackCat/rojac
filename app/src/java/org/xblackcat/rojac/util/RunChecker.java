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
    public static final String REQUEST_STRING = "Are you alive?";
    public static final String RESPONSE_STRING = "I'm still alive!";
    public static final int CHECKER_PORT = 29898;

    private final String requestString;
    private final String responseString;
    private final int port;

    /**
     * Create a checker with default parameters
     */
    public RunChecker() {
        this(CHECKER_PORT, REQUEST_STRING, RESPONSE_STRING);
    }

    public RunChecker(int port) {
        this(port, REQUEST_STRING, RESPONSE_STRING);
    }

    public RunChecker(int port, String requestString, String responseString) {
        this.port = port;
        this.requestString = requestString;
        this.responseString = responseString;
    }

    public boolean installNewInstanceListener(Runnable process) {
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

        Thread thread = new Thread(new LaunchListener(serverSocket, process));
        thread.setDaemon(true);
        thread.start();

        return true;
    }

    public boolean performCheck() {
        // First - check if already run
        Socket socket = new Socket();
        try {
            try {
                socket.connect(new InetSocketAddress("127.0.0.1", port), 500);

                if (!socket.isConnected()) {
                    return false;
                }

                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                try {
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    try {

                        os.writeUTF(requestString);
                        os.flush();

                        String resp = is.readUTF();
                        if (responseString.equals(resp)) {
                            // Application is running and moved to the front
                            return true;
                        }
                    } finally {
                        is.close();
                    }
                } finally {
                    os.close();
                }
            } finally {
                socket.close();
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

        public LaunchListener(ServerSocket serverSocket, Runnable process) {
            this.serverSocket = serverSocket;
            this.process = process;
        }

        @Override
        public void run() {
            try {
                try {
                    while (!serverSocket.isClosed()) {
                        try {
                            Socket socket = serverSocket.accept();

                            try {
                                DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                                try {
                                    String string = inputStream.readUTF();
                                    if (requestString.equals(string)) {
                                        // show main frame

                                        process.run();

                                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                        try {
                                            outputStream.writeUTF(responseString);
                                        } finally {
                                            outputStream.close();
                                        }
                                    }
                                } finally {
                                    inputStream.close();
                                }
                            } finally {
                                socket.close();
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
