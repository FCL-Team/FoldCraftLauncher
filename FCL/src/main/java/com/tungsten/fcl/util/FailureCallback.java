package com.tungsten.fcl.util;

import java.util.Map;

public interface FailureCallback {
    void onFail(Map<String, Object> settings, Exception exception, Runnable next);
}