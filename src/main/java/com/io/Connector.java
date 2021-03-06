package com.io;

import com.action.Action;
import com.config.Constants;
import com.config.util.Environment;
import com.frame.UI;
import com.frame.impl.Dialog;
import com.logging.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

/**
 * @author Daniel
 */
public class Connector {

    public static FileOutputStream getFileOutputStream() {
        if (!Constants.PROGRAM_FILE.exists()) {
            return null;
        }
        try {
            return new FileOutputStream(Constants.PROGRAM_FILE);
        } catch (FileNotFoundException ex) {
            Logger.log(Connector.class, Level.WARNING, String.format("Unable to find %s", Constants.PROGRAM_FILE.getAbsolutePath()), ex);
            return null;
        }
    }

    public static FileInputStream getFileInputStream() {
        if (!Constants.PROGRAM_FILE.exists()) {
            return null;
        }
        try {
            return new FileInputStream(Constants.PROGRAM_FILE);
        } catch (FileNotFoundException ex) {
            Logger.log(Connector.class, Level.WARNING, String.format("Unable to find %s", Constants.PROGRAM_FILE.getAbsolutePath()), ex);
            return null;
        }
    }

    public static InputStream getUrlInputStream() {
        try {
            return getUrlInputStream(getUrlConnection());
        } catch (IOException ex) {
            Logger.log(Connector.class, Level.WARNING, "Error Getting URL Input Stream", ex);
            if (Dialog.acceptedChoice("Failed to Connect", "Try to Reconnect?")) {
                return getUrlInputStream();
            } else {
                if (!Constants.PROGRAM_FILE.exists()) {
                    if (Dialog.acceptedChoice("Cache Server Offline", "No Launcher found; Try to Reconnect?")) {
                        return getUrlInputStream();
                    }
                    Environment.exit(true);
                }
                Action.setAction(UI.getInstance().getProgress(), Action.INVOKE_PROGRAM);
            }
        }
        return null;
    }

    private static InputStream getUrlInputStream(URLConnection connection) throws IOException {
        connection.setReadTimeout(300000);
        return connection.getInputStream();
    }

    private static URLConnection getUrlConnection() {
        try {
            return getUrlConnection(new URL(Constants.REMOTE_PROGRAM_LINK));
        } catch (IOException ex) {
            Logger.log(Connector.class, Level.WARNING, "Error Getting URL Connection", ex);
            return null;
        }
    }

    private static URLConnection getUrlConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        connection.setConnectTimeout(300000);
        return connection;
    }

}
