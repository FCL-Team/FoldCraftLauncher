package com.tungsten.fclcore.util;

import static org.apache.commons.io.FileUtils.listFiles;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

public class Pack200Utils {

    /**
     * Unpacks all .pack files into .jar
     * @param nativeLibraryDir The native lib path, required to execute the unpack200 binary
     * @param dir The path of the directory which contains .pack file
     */
    public static void unpack(String nativeLibraryDir, String dir) {
        File basePath = new File(dir);
        Collection<File> files = listFiles(basePath, new String[] { "pack" }, true);

        File workdir = new File(nativeLibraryDir);

        ProcessBuilder processBuilder = new ProcessBuilder().directory(workdir);
        for(File jarFile : files) {
            try {
                Process process = processBuilder.command("./libunpack200.so", "-r", jarFile.getAbsolutePath(), jarFile.getAbsolutePath().replace(".pack", "")).start();
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new IOException("unpack200 failed with exit code " + exitCode);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logging.LOG.log(Level.WARNING, "Failed to unpack file: " + jarFile.getAbsolutePath(), e);
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Failed to unpack file: " + jarFile.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Unpacks .pack files into .jar
     * @param in Input file path
     * @param out Output file path
     */
    public static void unpack(String nativeLibraryDir, String in, String out) {
        try {
            File workdir = new File(nativeLibraryDir);
            ProcessBuilder processBuilder = new ProcessBuilder().directory(workdir);
            Process process = processBuilder.command("./libunpack200.so", "-r", in, out).start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("unpack200 failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logging.LOG.log(Level.WARNING, "Failed to unpack file: " + in, e);
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Failed to unpack file: " + in, e);
        }
    }

}
