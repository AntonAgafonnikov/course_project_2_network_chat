package ru.netology;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    private final Socket socket;
    private final Thread listeningThread;
    private ConnectionListener connectionListener;
    BufferedReader in;
    BufferedWriter out;

    public Connection(ConnectionListener connectionListener, String ipAddress, int port) throws IOException {
        this(connectionListener, new Socket(ipAddress, port));
    }
    public Connection(ConnectionListener connectionListener, Socket socket) throws IOException {
        this.connectionListener = connectionListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        listeningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectionListener.onConnectionReady(Connection.this);
                    while (!listeningThread.isInterrupted()) {
                        String msg = in.readLine();
                        if (msg.equals("/exit")) {
                            listeningThread.interrupt();
                        }
                        connectionListener.onReceiveString(Connection.this, msg);
                    }
                } catch (IOException e) {
                    connectionListener.onException(Connection.this, e);
                } finally {
                    connectionListener.onDisconnect(Connection.this);
                }
            }
        });
        listeningThread.start();
    }

    public synchronized void sendMsg(String msg){
        try {
            out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            connectionListener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        listeningThread.interrupt();
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            connectionListener.onException(Connection.this, e);
        }
    }

    @Override
    public String toString() {
        return "Connection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
