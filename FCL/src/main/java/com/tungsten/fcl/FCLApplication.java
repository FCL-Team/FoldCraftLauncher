package com.tungsten.fcl;

import android.app.Application;
import android.os.StrictMode;

public class FCLApplication extends Application {

    @Override
    public void onCreate() {
        // enabledStrictMode();
        super.onCreate();
    }

    private void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork()
                .detectCustomSlowCalls()
                .detectDiskReads()
                .detectDiskWrites() 
                .detectAll()
                .penaltyLog() 
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectActivityLeaks()
                .detectAll()
                .penaltyLog()
                .build());
    }

}