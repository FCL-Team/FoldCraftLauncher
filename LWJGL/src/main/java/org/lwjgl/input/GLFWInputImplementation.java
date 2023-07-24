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
    public int mouseX = 0;
    public int mouseY = 0;
    public int mouseLastX = 0;
    public int mouseLastY = 0;
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
        coord_buffer.put(0, grab ? mouseX - mouseLastX : mouseX);
        coord_buffer.put(1, grab ? mouseY - mouseLastY : mouseY);
        buttons.rewind();
        buttons.put(mouseBuffer);
        mouseLastX = mouseX;
        mouseLastY = mouseY;
    }

    @Override
    public void readMouse(ByteBuffer buffer) {
        eventQueue.copyEvents(buffer);
    }

    @Override
    public void grabMouse(boolean newGrab) {
        grab = newGrab;
        correctCursor = newGrab;
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

    public void putMouseEventWithCoords(byte button, byte state, int x, int y, int dz, long nanos) {
        int rebaseX;
        int rebaseY;
        if (x == -1 && y == -1) {
            rebaseX = mouseX;
            rebaseY = mouseY;
        } else {
            rebaseX = x;
            rebaseY = y;
            if (correctCursor) {
                mouseX = x;
                mouseY = y;
                mouseLastX = mouseX;
                mouseLastY = mouseY;
                correctCursor = false;
                return;
            }
        }
        eventBuffer.clear();
        eventBuffer.put(button).put(state);
        if (grab) {
            eventBuffer.putInt(rebaseX - mouseX).putInt(rebaseY - mouseY);
        } else {
            eventBuffer.putInt(rebaseX).putInt(rebaseY);
        }
        if (button != -1) {
            mouseBuffer[button] = state;
        }
        eventBuffer.putInt(dz).putLong(nanos);
        eventBuffer.flip();
        eventQueue.putEvent(eventBuffer);
        mouseX = rebaseX;
        mouseY = rebaseY;
    }

    public void putKeyboardEvent(int keycode, byte state, int ch, long nanos, boolean repeat) {
        keyDownBuffer[keycode] = state;
        keyboardEvent.clear();
        keyboardEvent.putInt(keycode).put(state).putInt(ch).putLong(nanos).put(repeat ? (byte) 1 : (byte) 0);
        keyboardEvent.flip();
        keyboardEventQueue.putEvent(keyboardEvent);
    }
}

