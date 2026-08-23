package com.tungsten.fclcore.fakefx.beans;

@FunctionalInterface
public interface InvalidationListener {

    /**
     * This method needs to be provided by an implementation of
     * {@code InvalidationListener}. It is called if an {@link Observable}
     * becomes invalid.
     * <p>
     * In general, it is considered bad practice to modify the observed value in
     * this method.
     *
     * @param observable
     *            The {@code Observable} that became invalid
     */
    public void invalidated(Observable observable);
}
