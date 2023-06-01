package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Server implements ConnectionListener{
    private final ArrayList<Connection> connectionsList = new ArrayList<>();
    private static final File logFile = new File("server/src/main/resources/file.log");
    private static final File settingsFile = new File("server/src/main/resources/settingsServer.txt");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws IOException {
        if(!logFile.exists()) {
            logFile.createNewFile();
        }
        if(!settingsFile.exists()) {
            settingsFile.createNewFile();
        }
        new Server();
    }

    private Server() throws IOException {
        System.out.println(getTime() + "Server running...");
        try (ServerSocket serverSocket = new ServerSocket(getPort())) {
            while (true) {
                try {
                    new Connection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println(getTime() + "Connection exception " + e);
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
        String info = getTime() + "Client connected: " + connection;
        sendAllConnection(connection, info);
        loggingInFile(info);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String msg) {
        String info = getTime() + msg;
        sendAllConnection(connection, info);
        loggingInFile(info);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connectionsList.remove(connection);
        String info = getTime() + "Client disconnected: " + connection;
        sendAllConnection(connection, info);
        loggingInFile(info);
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        String info = getTime() + "Connection exception:" + e;
        System.out.println(info);
        loggingInFile(info);
    }

    private void sendAllConnection(Connection currentConnection, String info) {
        System.out.println(info);
        for (Connection connection : connectionsList) {
            connection.sendMsg(info);
        }
    }

    public void loggingInFile(String msg) {
        try (BufferedWriter bwLog = new BufferedWriter(new FileWriter(logFile, true))){
            bwLog.write(msg);
            bwLog.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
            loggingInFile(e.toString());
        }
    }

    private String getTime() {
        return " (" + LocalTime.now().format(dtf) + ") ";
    }
}
