package com.tungsten.fclcore.util;

import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.*;

public final class Logging {
    private Logging() {
    }

    public static final Logger LOG = Logger.getLogger("FCL");
    private static final ByteArrayOutputStream storedLogs = new ByteArrayOutputStream(IOUtils.DEFAULT_BUFFER_SIZE);

    private static final ConcurrentMap<String, String> forbiddenTokens = new ConcurrentHashMap<>();

    public static void registerForbiddenToken(String token, String replacement) {
        forbiddenTokens.put(token, replacement);
    }

    public static void registerAccessToken(String accessToken) {
        registerForbiddenToken(accessToken, "<access token>");
    }

    public static String filterForbiddenToken(String message) {
        for (Map.Entry<String, String> entry : forbiddenTokens.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

    public static void start(String logFolder) {
        LOG.setLevel(Level.ALL);
        LOG.setUseParentHandlers(false);
        LOG.setFilter(record -> {
            record.setMessage(filterForbiddenToken(record.getMessage()));
            return true;
        });

        try {
            FileUtils.makeDirectory(logFolder);
            FileHandler fileHandler = new FileHandler(logFolder + "/fcl.log");
            fileHandler.setLevel(Level.FINEST);
            fileHandler.setFormatter(DefaultFormatter.INSTANCE);
            fileHandler.setEncoding("UTF-8");
            LOG.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Unable to create fcl.log, " + e.getMessage());
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(DefaultFormatter.INSTANCE);
        consoleHandler.setLevel(Level.FINER);
        LOG.addHandler(consoleHandler);

        StreamHandler streamHandler = new StreamHandler(storedLogs, DefaultFormatter.INSTANCE) {
            @Override
            public synchronized void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        try {
            streamHandler.setEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        streamHandler.setLevel(Level.ALL);
        LOG.addHandler(streamHandler);
    }

    public static void initForTest() {
        LOG.setLevel(Level.ALL);
        LOG.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(DefaultFormatter.INSTANCE);
        consoleHandler.setLevel(Level.FINER);
        LOG.addHandler(consoleHandler);
    }

    public static byte[] getRawLogs() {
        return storedLogs.toByteArray();
    }

    public static String getLogs() {
        try {
            return storedLogs.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    private static final class DefaultFormatter extends Formatter {

        static final DefaultFormatter INSTANCE = new DefaultFormatter();
        private static final MessageFormat format = new MessageFormat("[{0,date,HH:mm:ss}] [{1}.{2}/{3}] {4}\n");

        @Override
        public String format(LogRecord record) {
            String log = format.format(new Object[]{
                    new Date(record.getMillis()),
                    record.getSourceClassName(), record.getSourceMethodName(), record.getLevel().getName(),
                    record.getMessage()
            }, new StringBuffer(128), null).toString();
            if (record.getThrown() != null)
                log += StringUtils.getStackTrace(record.getThrown());

            return log;
        }

    }
}