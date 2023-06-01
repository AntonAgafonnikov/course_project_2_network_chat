package ru.netology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static void loggingInFile(File logFile, String msg) {
        try (BufferedWriter bwLog = new BufferedWriter(new FileWriter(logFile, true))){
            bwLog.write(msg);
            bwLog.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
