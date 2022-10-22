package com.tungsten.fclcore.launch;

import android.content.Context;
import android.view.Surface;

import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;

import java.io.IOException;

public abstract class Launcher {

    protected final Context context;
    protected final Surface surface;
    protected final GameRepository repository;
    protected final Version version;
    protected final AuthInfo authInfo;
    protected final LaunchOptions options;
    protected final FCLBridgeCallback callback;

    public Launcher(Context context, Surface surface, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options) {
        this(context, surface, repository, version, authInfo, options, null);
    }

    public Launcher(Context context, Surface surface, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options, FCLBridgeCallback callback) {
        this.context = context;
        this.surface = surface;
        this.repository = repository;
        this.version = version;
        this.authInfo = authInfo;
        this.options = options;
        this.callback = callback;
    }

    public abstract FCLBridge launch() throws IOException, InterruptedException;

}
