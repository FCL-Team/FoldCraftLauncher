package org.lwjgl.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GLFWInputImplementation implements InputImplementation {

    public static final GLFWInputImplementation singleton = new GLFWInputImplementation();
    private final ByteBuffer eventBuffer = ByteBuffer.allocate(Mouse.EVENT_SIZE);
    private final EventQueue eventQueue = new EventQueue(Mouse.EVENT_SIZE);
    private final EventQueue keyboardEventQueue = new EventQueue(Keyboard.EVENT_SIZE);
    private final ByteBuffer keyboardEvent = ByteBuffer.allocate(Keyboard.EVENT_SIZE);
    public final byte[] keyDownBuffer = new byte[Keyboard.KEYBOARD_SIZE];
    public final byte[] mouseBuffer = new byte[3];
    private int last_x;
    private int last_y;
    public int accum_dx;
    public int accum_dy;
    public boolean grab;
    public boolean correctCursor;

    @Override
    public boolean hasWheel() {
        return true;
    }

    @Override
    public int getButtonCount() {
        return 3;
    }

    @Override
    public void createMouse() throws LWJGLException {

    }

    @Override
    public void destroyMouse() {

    }

    @Override
    public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons) {
        coord_buffer.put(0, grab ? accum_dx : last_x);
        coord_buffer.put(1, grab ? accum_dy : last_y);
        buttons.rewind();
        buttons.put(mouseBuffer);
        accum_dx = accum_dy = 0;
    }

    @Override
    public void readMouse(ByteBuffer buffer) {
        eventQueue.copyEvents(buffer);
    }

    @Override
    public void grabMouse(boolean newGrab) {
        grab = newGrab;
        correctCursor = grab;
        GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    @Override
    public int getNativeCursorCapabilities() {
        return 0;
    }

    @Override
    public void setCursorPosition(int x, int y) {

    }

    @Override
    public void setNativeCursor(Object handle) throws LWJGLException {

    }

    @Override
    public int getMinCursorSize() {
        return 0;
    }

    @Override
    public int getMaxCursorSize() {
        return 0;
    }

    @Override
    public void createKeyboard() throws LWJGLException {

    }

    @Override
    public void destroyKeyboard() {

    }

    @Override
    public void pollKeyboard(ByteBuffer keyDownBuffer) {
        int oldPosition = keyDownBuffer.position();
        keyDownBuffer.put(this.keyDownBuffer);
        keyDownBuffer.position(oldPosition);
    }

    public void readKeyboard(ByteBuffer buffer) {
        keyboardEventQueue.copyEvents(buffer);
    }

    @Override
    public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
        return null;
    }

    @Override
    public void destroyCursor(Object cursor_handle) {

    }

    @Override
    public int getWidth() {
        return Display.getWidth();
    }

    @Override
    public int getHeight() {
        return Display.getHeight();
    }

    @Override
    public boolean isInsideWindow() {
        return true;
    }

    private int transformY(int y) {
        return Display.getHeight() - 1 - y;
    }

    public void setCursorPos(int x, int y, long nanos) {
        y = transformY(y);
        if (correctCursor) {
            last_x = x;
            last_y = y;
            correctCursor = false;
            return;
        }
        int dx = x - last_x;
        int dy = y - last_y;
        if (dx != 0 || dy != 0) {
            accum_dx += dx;
            accum_dy += dy;
            last_x = x;
            last_y = y;
            if (grab) {
                putMouseEventWithCoords((byte)-1, (byte)0, dx, dy, 0, nanos * 1000000);
            } else {
                putMouseEventWithCoords((byte)-1, (byte)0, x, y, 0, nanos * 1000000);
            }
        }
    }

    private void putMouseEventWithCoords(byte button, byte state, int x, int y, int z, long nanos) {
        eventBuffer.clear();
        eventBuffer.put(button).put(state).putInt(x).putInt(y).putInt(z).putLong(nanos);
        eventBuffer.flip();
        eventQueue.putEvent(eventBuffer);
    }

    public void putMouseEvent(byte button, byte state, int dz, long nanos) {
        if (grab)
            putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
        else
            putMouseEventWithCoords(button, state, last_x, last_y, dz, nanos);
    }

    public void putKeyboardEvent(int keycode, byte state, int ch, long nanos, boolean repeat) {
        keyDownBuffer[keycode] = state;
        keyboardEvent.clear();
        keyboardEvent.putInt(keycode).put(state).putInt(ch).putLong(nanos).put(repeat ? (byte) 1 : (byte) 0);
        keyboardEvent.flip();
        keyboardEventQueue.putEvent(keyboardEvent);
    }
}
