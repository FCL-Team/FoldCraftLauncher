package com.tungsten.fcl.control.keyboard;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;

public class LwjglCharSender implements CharacterSenderStrategy {

    private final GameMenu gameMenu;

    public LwjglCharSender(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    @Override
    public void sendBackspace() {
        if (gameMenu.getBridge() != null) {
            gameMenu.getBridge().pushEventKey(FCLKeycodes.KEY_BACKSPACE, '\u0008', true);
            gameMenu.getBridge().pushEventKey(FCLKeycodes.KEY_BACKSPACE, '\u0008', false);
        }
    }

    @Override
    public void sendEnter() {
        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, true);
        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, false);
    }

    @Override
    public void sendChar(char character) {
        gameMenu.getInput().sendChar(character);
    }
}
