package com.tungsten.fcl.control.view;

import android.view.View;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fclcore.util.Logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

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
        menuView.initPosition();

        // Initialize controller
        initializeController();
        gameMenu.controllerProperty().addListener(i -> initializeController());
        gameMenu.viewGroupProperty().addListener(i -> initializeController());
        gameMenu.editModeProperty().addListener(i -> initializeController());
    }

    public void addView(CustomControl control) {
        if (gameMenu.isEditMode()) {
            if (gameMenu.getViewGroup() != null) {
                if (control instanceof ControlButtonData) {
                    gameMenu.getViewGroup().getViewData().addButton((ControlButtonData) control);
                } else {
                    gameMenu.getViewGroup().getViewData().addDirection((ControlDirectionData) control);
                }
                saveController();
                loadView(control, true);
            } else {
                Toast.makeText(gameMenu.getActivity(), gameMenu.getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void removeView(CustomControl control) {
        if (gameMenu.getViewGroup() != null && gameMenu.isEditMode()) {
            for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
                View view = gameMenu.getBaseLayout().getChildAt(i);
                if (view instanceof CustomView) {
                    if (control.getViewId().equals(((CustomView) view).getViewId())) {
                        gameMenu.getBaseLayout().removeView(view);
                        break;
                    }
                }
            }
            if (control instanceof ControlButtonData) {
                gameMenu.getViewGroup().getViewData().removeButton((ControlButtonData) control);
            } else {
                gameMenu.getViewGroup().getViewData().removeDirection((ControlDirectionData) control);
            }
            saveController();
        }
    }

    private void loadView(CustomControl control, boolean parentVisibility) {
        if (control instanceof ControlButtonData) {
            ControlButtonData data = (ControlButtonData) control;
            ControlButton button = new ControlButton(gameMenu.getActivity(), gameMenu);
            gameMenu.getBaseLayout().addView(button);
            button.readyProperty().addListener(i -> {
                if (button.isReady()) {
                    button.setParentVisibility(parentVisibility);
                    button.setData(data);
                }
            });
        } else {
            ControlDirectionData data = (ControlDirectionData) control;
            ControlDirection direction = new ControlDirection(gameMenu.getActivity(), gameMenu, false);
            gameMenu.getBaseLayout().addView(direction);
            direction.readyProperty().addListener(i -> {
                if (direction.isReady()) {
                    direction.setParentVisibility(parentVisibility);
                    direction.setData(data);
                }
            });
        }
    }

    public void saveController() {
        try {
            gameMenu.getController().saveToDisk();
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to save controller!", e);
        }
    }

    public void initializeController() {
        removeAllCustomViews();
        if (gameMenu.isEditMode()) {
            if (gameMenu.getViewGroup() != null) {
                gameMenu.getViewGroup().getViewData().buttonList().forEach(it -> loadView(it, true));
                gameMenu.getViewGroup().getViewData().directionList().forEach(it -> loadView(it, true));
            }
        } else {
            gameMenu.getController().viewGroups().forEach(it -> {
                it.getViewData().buttonList().forEach(data -> loadView(data, it.getVisibility() == ControlViewGroup.Visibility.VISIBLE));
                it.getViewData().directionList().forEach(data -> loadView(data, it.getVisibility() == ControlViewGroup.Visibility.VISIBLE));
            });
        }
    }

    private void removeAllCustomViews() {
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
            if (gameMenu.getBaseLayout().getChildAt(i) instanceof CustomView) {
                views.add(gameMenu.getBaseLayout().getChildAt(i));
            }
        }
        for (View v : views) {
            gameMenu.getBaseLayout().removeView(v);
        }
    }

    public void switchViewGroupVisibility(ControlViewGroup viewGroup) {
        if (viewGroup == null)
            return;
        for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
            View view = gameMenu.getBaseLayout().getChildAt(i);
            if (view instanceof CustomView) {
                if (viewGroup.getViewData().buttonList().stream().anyMatch(it -> it.getId().equals(((CustomView) view).getViewId()))
                        || viewGroup.getViewData().directionList().stream().anyMatch(it -> it.getId().equals(((CustomView) view).getViewId()))) {
                    ((CustomView) view).switchParentVisibility();
                }
            }
        }
    }

}
