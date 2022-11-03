package com.tungsten.fcl.game;

import android.content.Context;

import com.tungsten.fcl.R;

public enum LoadingState {
    DEPENDENCIES(R.string.launch_state_dependencies),
    MODS(R.string.launch_state_modpack),
    LOGGING_IN(R.string.launch_state_logging_in),
    LAUNCHING(R.string.launch_state_waiting_launching),
    DONE(R.string.launch_state_done);

    private final int id;

    LoadingState(int id) {
        this.id = id;
    }

    public String getLocalizedMessage(Context context) {
        return context.getString(id);
    }
}
