package com.tungsten.fclauncher.utils;

import android.os.Environment;
import android.util.Log;

import com.tungsten.fclauncher.FCLPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author mio
 */
public class LogFileUtil {
    private static final LogFileUtil INSTANCE=new LogFileUtil();
    private String logFilePath;
    private boolean firstWrite = true;
    private boolean isWrite = true;
    protected LogFileUtil(){

    }
    public static LogFileUtil getInstance(){
        return INSTANCE;
    }
    public void setLogFilePath(String logFilePath){
        this.logFilePath=logFilePath;
    }
    public void writeLog(String log) {
        Log.e("FCL", log);
        if (this.logFilePath==null){
            this.logFilePath= FCLPath.SHARED_COMMON_DIR +"/latest.log";
        }
        log+="\n";
        if (!isWrite){
            return;
        }
        File logFile = new File(this.logFilePath);
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) {
                    isWrite = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (firstWrite) {
            writeData(logFile.getAbsolutePath(), log);
            firstWrite = false;
        } else {
            addStringLineToFile(log, logFile);
        }


    }

    public void writeLog(ArrayList<String> arrayList){
        for (String s:arrayList){
            writeLog(s);
        }
    }

    public static void writeData(String path, String fileData) {
        File file = new File(path);
        try(FileOutputStream out = new FileOutputStream(file, false)) {
            out.write(fileData.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addStringLineToFile(String content, File file) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
