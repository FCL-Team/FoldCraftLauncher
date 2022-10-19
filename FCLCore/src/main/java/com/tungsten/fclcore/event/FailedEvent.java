package com.tungsten.fclcore.event;

public class FailedEvent<T> extends Event {

    private final int failedTime;
    private T newResult;

    public FailedEvent(Object source, int failedTime, T newResult) {
        super(source);
        this.failedTime = failedTime;
        this.newResult = newResult;
    }

    public int getFailedTime() {
        return failedTime;
    }

    public T getNewResult() {
        return newResult;
    }

    public void setNewResult(T newResult) {
        this.newResult = newResult;
    }

}
