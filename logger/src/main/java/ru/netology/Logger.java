package ru.netology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static final String CLIENT_CONNECTED = "Client connected: ";
    public static final String CONNECTION_CLOSE = "Connection close";
    public static final String CONNECTION_EXCEPTION = "Connection exception: ";
    public static final String CLIENT_DISCONNECTED = "Client disconnected: ";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static synchronized String loggingInFile(File logFile, String msg) {
        String record = getTime() + msg;
        try (BufferedWriter bwLog = new BufferedWriter(new FileWriter(logFile, true))){
            bwLog.write(record);
            bwLog.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return record;
    }

    private static synchronized String getTime() {
        return " (" + LocalTime.now().format(dtf) + ") ";
    }

    public static void createLogFile(File logFile) throws IOException {
        if (!logFile.exists()) {
            if (logFile.createNewFile()) {
                System.out.println("Файл для логирования создан");
            } else {
                System.out.println("Ошибка создания файла логирования!");
            }
        }
    }

    public static void createSettingsFile(File settingsFile) throws IOException {
        if(!settingsFile.exists()) {
            if (settingsFile.createNewFile()) {
                System.out.println("Файл для настроек сервера создан");
            } else {
                System.out.println("Ошибка создания файла настроек сервера!");
            }
        }
    }
}
