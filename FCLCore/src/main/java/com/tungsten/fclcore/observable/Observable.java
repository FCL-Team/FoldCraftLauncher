package com.tungsten.fclcore.observable;

/**
 * Minimal reimplementation of {@code fakefx.beans.Observable} (fakefx 移除阶段 2a,
 * 见 docs/migration/fakefx-removal-plan.md)。语义与 JavaFX 对齐。
 */
public interface Observable {

    void addListener(InvalidationListener listener);

    void removeListener(InvalidationListener listener);
}
