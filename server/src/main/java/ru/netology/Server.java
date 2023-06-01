package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

import static ru.netology.Logger.*;

public class Server implements ConnectionListener{
    private final ArrayList<Connection> connectionsList = new ArrayList<>();
    private static final File logFile = new File("server/src/main/resources/file.log");
    private static final File settingsFile = new File("server/src/main/resources/settingsServer.txt");

    public static void main(String[] args) throws IOException {
        createSettingsFile(settingsFile);
        createLogFile(logFile);
        new Server();
    }

    private Server() throws IOException {
        System.out.println(loggingInFile(logFile, "Server running..."));
        try (ServerSocket serverSocket = new ServerSocket(getPort())) {
            while (true) {
                try {
                    new Connection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println(loggingInFile(logFile,CONNECTION_EXCEPTION + e));
                }
            }
        }
    }

    private int getPort() throws IOException {
        try (BufferedReader brPort = new BufferedReader(new FileReader(settingsFile))) {
            String portString = brPort.readLine();
            int indexSpace = portString.indexOf(" ");
            return Integer.parseInt(portString.substring(indexSpace + 1));
        }
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connectionsList.add(connection);
        sendAllConnection(loggingInFile(logFile, CLIENT_CONNECTED + connection));
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String msg) {
        sendAllConnection(loggingInFile(logFile, msg));
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connectionsList.remove(connection);
        sendAllConnection(loggingInFile(logFile, CLIENT_DISCONNECTED + connection));
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println(loggingInFile(logFile, CONNECTION_EXCEPTION + e));
    }

    private void sendAllConnection(String info) {
        System.out.println(info);
        for (Connection connection : connectionsList) {
            connection.sendMsg(info);
        }
    }
}
