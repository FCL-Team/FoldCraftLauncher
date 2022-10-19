package com.tungsten.fclcore.game;

public enum ProcessPriority {
    LOW(Thread.MIN_PRIORITY),
    NORMAL(Thread.NORM_PRIORITY),
    HIGH(Thread.MAX_PRIORITY);

    private final int priority;

    ProcessPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
