package org.lwjgl.openal.jsound;

/**
 * A sound source driven by the engine worker thread. All OpenAL state belongs to
 * the worker; public line objects only read volatile snapshots from it.
 */
interface Voice {

    /** Called on the worker thread roughly every 10 ms while the voice is registered. */
    void tick();

    /** Called on the worker thread exactly once when the line closes. */
    void destroy();
}