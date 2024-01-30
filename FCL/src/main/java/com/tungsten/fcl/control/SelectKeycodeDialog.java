package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.view.KeycodeView;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SelectKeycodeDialog extends FCLDialog implements View.OnClickListener {

    private final ObservableList<Integer> list;
    private final boolean singleSelection;

    private final SimpleIntegerProperty selectionProperty = new SimpleIntegerProperty(this, "selection", -1);

    private FCLButton positive;

    private FCLLinearLayout container;

    private GameMenu gameMenu;

    public SimpleIntegerProperty selectionProperty() {
        return selectionProperty;
    }

    public SelectKeycodeDialog(@NonNull Context context, ObservableList<Integer> list, boolean singleSelection, boolean mouse) {
        super(context);
        this.list = list;
        this.singleSelection = singleSelection;
        setCancelable(false);
        setContentView(R.layout.dialog_select_keycode);

        if (singleSelection) {
            selectionProperty.set(list.get(0));
        }

        container = findViewById(R.id.parent_layout);
        initializeAllButtons(container);
        checkSelection(container);

        FCLLinearLayout mouseLayout = findViewById(R.id.mouse);
        assert mouseLayout != null;
        mouseLayout.setVisibility(mouse ? View.VISIBLE : View.GONE);

        positive = findViewById(R.id.positive);
        assert positive != null;
        positive.setOnClickListener(this);
    }

    public SelectKeycodeDialog(@NonNull Context context, ObservableList<Integer> list, boolean singleSelection, boolean mouse, GameMenu gameMenu) {
        this(context, list, singleSelection, mouse);
        this.gameMenu = gameMenu;
    }

    private void checkSelection(ViewGroup container) {
        for (int i = 0; i < Objects.requireNonNull(container).getChildCount(); i++) {
            if (container.getChildAt(i) instanceof KeycodeView) {
                ArrayList<Integer> l = new ArrayList<>();
                if (singleSelection) {
                    l.add(selectionProperty.get());
                } else {
                    l.addAll(list);
                }
                ((KeycodeView) container.getChildAt(i)).checkSelection(l);
            } else if (container.getChildAt(i) instanceof ViewGroup) {
                checkSelection((ViewGroup) container.getChildAt(i));
            }
        }
    }

    private void initializeAllButtons(ViewGroup container) {
        for (int i = 0; i < Objects.requireNonNull(container).getChildCount(); i++) {
            if (container.getChildAt(i) instanceof KeycodeView) {
                ((KeycodeView) container.getChildAt(i)).setOnKeycodeChangeListener(new KeycodeView.OnKeycodeChangeListener() {
                    @Override
                    public void onKeycodeAdd(KeycodeView view, int keycode) {
                        if (singleSelection) {
                            selectionProperty.set(keycode);
                            checkSelection(SelectKeycodeDialog.this.container);
                        } else {
                            list.add(keycode);
                        }
                    }

                    @Override
                    public void onKeycodeRemove(KeycodeView view, int keycode) {
                        if (singleSelection) {
                            view.setSelectedWithoutCallback(true);
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i) == keycode) {
                                    list.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                });
            } else if (container.getChildAt(i) instanceof ViewGroup) {
                initializeAllButtons((ViewGroup) container.getChildAt(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (gameMenu != null) {
                list.forEach(key -> {
                    gameMenu.getInput().sendKeyEvent(key, true);
                    gameMenu.getInput().sendKeyEvent(key, false);
                });
            }
            dismiss();
        }
    }
}
