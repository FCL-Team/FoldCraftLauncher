package com.tungsten.fcl.util;

import com.tungsten.fcl.ui.TaskDialog;

import java.util.function.Consumer;

public class TaskCancellationAction {
    public static TaskCancellationAction NORMAL = new TaskCancellationAction(() -> {
    });

    private final Consumer<TaskDialog> cancellationAction;

    public TaskCancellationAction(Runnable cancellationAction) {
        this.cancellationAction = it -> cancellationAction.run();
    }

    public TaskCancellationAction(Consumer<TaskDialog> cancellationAction) {
        this.cancellationAction = cancellationAction;
    }

    public Consumer<TaskDialog> getCancellationAction() {
        return cancellationAction;
    }
}