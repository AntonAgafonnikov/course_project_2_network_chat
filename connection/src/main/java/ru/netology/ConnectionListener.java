package ru.netology;

public interface ConnectionListener {
    // Соединение готово
    void onConnectionReady(Connection connection);
    // Соединение приняло строчку
    void onReceiveString(Connection connection, String msg);
    // Разрыв соединения
    void onDisconnect(Connection connection);
    // Непредвиденное событие
    void onException(Connection connection, Exception e);
}
