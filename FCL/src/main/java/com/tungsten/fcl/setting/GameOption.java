/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.setting;

import android.os.Build;
import android.os.FileObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * From PojavLauncher
 */
@SuppressWarnings("ALL")
public class GameOption {

    private final String optionPath;
    private static final HashMap<String, String> parameterMap = new HashMap<>();
    private static final ArrayList<WeakReference<GameOptionListener>> optionListeners = new ArrayList<>();
    private static FileObserver fileObserver;

    public GameOption(String gameDir) {
        this.optionPath = gameDir + "/options.txt";
        load(optionPath);
    }

    public interface GameOptionListener {
        void onOptionChanged();
    }

    private void load(@NonNull String path) {
        synchronized(this) {
            File optionFile = new File(path);
            if (!optionFile.exists()) {
                try {
                    optionFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileObserver == null) {
                setupFileObserver();
            }
            parameterMap.clear();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(optionFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    int firstColonIndex = line.indexOf(':');
                    if (firstColonIndex < 0) {
                        Logging.LOG.log(Level.INFO, "No colon on line \"" + line + "\", skipping");
                        continue;
                    }
                    parameterMap.put(line.substring(0, firstColonIndex), line.substring(firstColonIndex + 1));
                }
                reader.close();
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Could not load options.txt", e);
            }
        }
    }

    public void set(String key, String value) {
        parameterMap.put(key, value);
    }

    /** Set an array of String, instead of a simple value. Not supported on all options */
    public void set(String key, List<String> values) {
        parameterMap.put(key, values.toString());
    }

    public String get(String key) {
        return parameterMap.get(key);
    }

    /** @return A list of values from an array stored as a string */
    public List<String> getAsList(String key) {
        String value = get(key);

        // Fallback if the value doesn't exist
        if (value == null) return new ArrayList<>();

        // Remove the edges
        value = value.replace("[", "").replace("]", "");
        if (value.isEmpty())
            return new ArrayList<>();

        return Arrays.asList(value.split(","));
    }

    public void save() {
        synchronized(this) {
            StringBuilder result = new StringBuilder();
            for(String key : parameterMap.keySet())
                result.append(key)
                        .append(':')
                        .append(parameterMap.get(key))
                        .append('\n');

            try {
                fileObserver.stopWatching();
                FileUtils.writeText(new File(optionPath), result.toString());
                fileObserver.startWatching();
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Could not save options.txt", e);
            }
        }
    }

    /** @return The stored Minecraft GUI scale, also auto-computed if on auto-mode or improper setting */
    public int getGuiScale(int width, int height, int iscale) {
        String str = get("guiScale");
        int guiScale;
        try {
            guiScale = (str == null ? 0 : Integer.parseInt(str));
        } catch (NumberFormatException ignore) {
            guiScale = 0;
        }
        int scale = Math.max(Math.min(width / 320, height / 240), 1);
        if (scale < guiScale || guiScale == 0) {
            guiScale = scale;
        }

//        String lang = get("lang");
//        boolean isUnicode = lang != null && (lang.equals("zh_CN") || lang.equals("zh_cn"));
//        if (isUnicode && guiScale % 2 != 0 && guiScale != 1) {
//            --guiScale;
//        }

        return guiScale + iscale;
    }

    /** Add a file observer to reload options on file change
     * Listeners get notified of the change */
    private void setupFileObserver() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            fileObserver = new FileObserver(new File(optionPath), FileObserver.MODIFY) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    load(optionPath);
                    notifyListeners();
                }
            };
        } else {
            fileObserver = new FileObserver(optionPath, FileObserver.MODIFY) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    load(optionPath);
                    notifyListeners();
                }
            };
        }

        fileObserver.startWatching();
    }

    /** Notify the option listeners */
    public void notifyListeners() {
        for (WeakReference<GameOptionListener> optionListener : optionListeners) {
            if(optionListener.get() == null) continue;

            optionListener.get().onOptionChanged();
        }
    }

    /** Add an option listener, notice how we don't have a reference to it */
    public void addGameOptionListener(GameOptionListener listener) {
        optionListeners.add(new WeakReference<>(listener));
    }

    /** Remove a listener from existence, or at least, its reference here */
    public void removeGameOptionListener(GameOptionListener listener) {
        for(WeakReference<GameOptionListener> optionListener : optionListeners) {
            if(optionListener == null) continue;
            if(optionListener == listener) {
                optionListeners.remove(optionListener);
                return;
            }
        }
    }

}
