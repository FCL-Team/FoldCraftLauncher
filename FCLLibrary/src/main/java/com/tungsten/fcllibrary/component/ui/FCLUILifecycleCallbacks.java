package com.tungsten.fcllibrary.component.ui;

public interface FCLUILifecycleCallbacks {

    void onCreate();

    void onStart();

    void onStop();

    void onBackPressed();

    void onPause();

    void onResume();

    void onDestroy();
}
