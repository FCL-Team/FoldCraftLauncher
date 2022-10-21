package com.tungsten.fclcore.launch;

import android.content.Context;

import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;

import java.io.IOException;

public abstract class Launcher {

    protected final Context context;
    protected final GameRepository repository;
    protected final Version version;
    protected final AuthInfo authInfo;
    protected final LaunchOptions options;
    protected final boolean daemon;

    public Launcher(Context context, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options) {
        this(context, repository, version, authInfo, options, true);
    }

    public Launcher(Context context, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options, boolean daemon) {
        this.context = context;
        this.repository = repository;
        this.version = version;
        this.authInfo = authInfo;
        this.options = options;
        this.daemon = daemon;
    }

    public abstract FCLBridge launch() throws IOException, InterruptedException;

}
