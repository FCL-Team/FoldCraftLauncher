package com.tungsten.fcl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtil extends Thread {

    private Process process;
    private BufferedReader output;
    private final Callback callback;

    public ShellUtil(String home, Callback callback) {
        this.callback = callback;
        ProcessBuilder pb = new ProcessBuilder("sh");
        pb.directory(new File(home));
        pb.redirectErrorStream(true);
        pb.environment().clear();
        try {
            process = pb.start();
            output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            append("export HOME=" + home + "&&cd\n");
        } catch (IOException e) {
            callback.output(e + "\n");
        }
    }

    public void append(String command) {
        try {
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
        } catch (IOException e) {
            callback.output(e + "\n");
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = output.readLine()) != null) {
                callback.output(line);
            }
            output.close();
            process.getInputStream().close();
            process.getErrorStream().close();
            process.getOutputStream().close();
        } catch (IOException e) {
            callback.output(e + "\n");
        }
        process.destroy();
    }

    public interface Callback {
        void output(String output);
    }
}
