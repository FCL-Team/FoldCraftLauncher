package com.tungsten.fclcore.task;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TaskExecutor {
    protected final Task<?> firstTask;
    protected final List<TaskListener> taskListeners = new ArrayList<>();
    protected final AtomicInteger totTask = new AtomicInteger(0);
    protected final AtomicBoolean cancelled = new AtomicBoolean(false);
    protected Exception exception;
    private final List<String> stages;

    public TaskExecutor(Task<?> task) {
        this.firstTask = task;
        this.stages = task instanceof Task.StagesHintTask ? ((Task<?>.StagesHintTask) task).getStages() : Collections.emptyList();
    }

    public void addTaskListener(TaskListener taskListener) {
        taskListeners.add(taskListener);
    }

    /**
     * Reason why the task execution failed.
     * If cancelled, null is returned.
     */
    @Nullable
    public Exception getException() {
        return exception;
    }

    public abstract TaskExecutor start();

    public abstract boolean test();

    /**
     * Cancel the subscription ant interrupt all tasks.
     */
    public abstract void cancel();

    public boolean isCancelled() {
        return cancelled.get();
    }

    public int getTaskCount() {
        return totTask.get();
    }

    public List<String> getStages() {
        return stages;
    }
}
