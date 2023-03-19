package com.tungsten.fcl.control.view;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.data.ControlViewGroup;

public class ViewManager {

    private final GameMenu gameMenu;

    public ViewManager(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public void setup() {
        // Initialize menu view
        MenuView menuView = new MenuView(gameMenu.getActivity());
        menuView.setup(gameMenu);
        gameMenu.getBaseLayout().addView(menuView);
    }

    public void switchViewGroupVisibility(ControlViewGroup viewGroup) {

    }

}
