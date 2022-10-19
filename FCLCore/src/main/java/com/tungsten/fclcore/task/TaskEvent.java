package com.tungsten.fclcore.task;

import com.tungsten.fclcore.event.Event;

public class TaskEvent extends Event {

    private final Task<?> task;
    private final boolean failed;

    public TaskEvent(Object source, Task<?> task, boolean failed) {
        super(source);
        this.task = task;
        this.failed = failed;
    }

    public Task<?> getTask() {
        return task;
    }

    public boolean isFailed() {
        return failed;
    }

}
