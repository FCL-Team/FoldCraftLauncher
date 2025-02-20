package com.tungsten.fcl.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.BaseInfoData;
import com.tungsten.fcl.control.data.ButtonEventData;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fcl.control.data.DirectionEventData;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLPreciseSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EditViewDialog extends FCLDialog implements View.OnClickListener {

    private final CustomControl customControl;
    private final Callback callback;

    private FCLTextView title;
    private FCLImageButton info;
    private FCLImageButton event;
    private FCLButton positive;
    private FCLButton negative;
    private FCLButton clone;
    private FCLButton delete;

    private FCLLinearLayout container;
    private Details details;

    public interface Callback {
        void onPositive(CustomControl view);
        void onClone(CustomControl view);
        default void onDelete(){}
    }

    public EditViewDialog(@NonNull Context context, CustomControl cloneView, GameMenu menu, Callback callback, boolean cloneable) {
        super(context);
        this.customControl = cloneView;
        this.callback = callback;
        setCancelable(false);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout(ConvertUtils.dip2px(context,500), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        setContentView(R.layout.dialog_edit_view);

        title = findViewById(R.id.title);
        assert title != null;
        title.setText(cloneView.getType() == CustomControl.ViewType.CONTROL_BUTTON ? R.string.edit_button_title : R.string.edit_direction_title);
        info = findViewById(R.id.info);
        event = findViewById(R.id.event);
        info.setOnClickListener(this);
        event.setOnClickListener(this);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        clone = findViewById(R.id.clone);
        delete = findViewById(R.id.delete);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
        clone.setOnClickListener(this);
        delete.setOnClickListener(this);

        clone.setVisibility(cloneable ? View.VISIBLE : View.GONE);

        container = findViewById(R.id.container);
        assert container != null;
        if (cloneView.getType() == CustomControl.ViewType.CONTROL_BUTTON) {
            details = new EditButtonDetails(getContext(), menu, (ControlButtonData) cloneView, container);
        } else {
            details = new EditDirectionDetails(getContext(), menu, (ControlDirectionData) cloneView, container);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == info) {
            details.onLayoutChange(0);
        }
        if (v == event) {
            details.onLayoutChange(1);
        }
        if (v == clone) {
            callback.onClone(customControl.cloneView());
            dismiss();
        }
        if (v == delete) {
            callback.onDelete();
            dismiss();
        }
        if (v == positive) {
            callback.onPositive(details.getView());
            dismiss();
        }
        if (v == negative) {
            dismiss();
        }
    }

    private interface Details {
        CustomControl getView();
        void onLayoutChange(int position);
    }

    private static class EditButtonDetails implements Details {

        private final ControlButtonData data;
        private final FCLLinearLayout container;

        private final FCLLinearLayout infoLayout;
        private final FCLLinearLayout eventLayout;

        public EditButtonDetails(Context context, GameMenu menu, ControlButtonData data, FCLLinearLayout container) {
            this.data = data;
            this.container = container;

            infoLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_button_info, null);
            eventLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_button_event, null);

            {
                FCLEditText editText = findInfoView(R.id.text);
                editText.setText(data.getText());
                data.textProperty().bind(editText.stringProperty());

                FCLSpinner<BaseInfoData.VisibilityType> visibilityTypeSpinner = findInfoView(R.id.visibility);
                ArrayList<BaseInfoData.VisibilityType> visibilityTypes = new ArrayList<>();
                visibilityTypes.add(BaseInfoData.VisibilityType.ALWAYS);
                visibilityTypes.add(BaseInfoData.VisibilityType.IN_GAME);
                visibilityTypes.add(BaseInfoData.VisibilityType.MENU);
                visibilityTypeSpinner.setDataList(visibilityTypes);
                ArrayList<String> visibilityTypeString = new ArrayList<>();
                visibilityTypeString.add(context.getString(R.string.view_info_visibility_always));
                visibilityTypeString.add(context.getString(R.string.view_info_visibility_game));
                visibilityTypeString.add(context.getString(R.string.view_info_visibility_menu));
                ArrayAdapter<String> visibilityTypeAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, visibilityTypeString);
                visibilityTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                visibilityTypeSpinner.setAdapter(visibilityTypeAdapter);
                visibilityTypeSpinner.setSelection(data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.ALWAYS ? 
                        0 : (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.IN_GAME ? 
                        1 : 2));
                FXUtils.bindSelection(visibilityTypeSpinner, data.getBaseInfo().visibilityTypeProperty());

                FCLPreciseSeekBar xPosition = findInfoView(R.id.x_position);
                FCLPreciseSeekBar yPosition = findInfoView(R.id.y_position);

                FCLTextView xPositionText = findInfoView(R.id.x_position_text);
                FCLTextView yPositionText = findInfoView(R.id.y_position_text);

                xPositionText.setOnClickListener(v -> openTextEditDialog(context, xPosition.progressProperty(), true));
                yPositionText.setOnClickListener(v -> openTextEditDialog(context, yPosition.progressProperty(), true));

                xPosition.setProgress(data.getBaseInfo().getXPosition());
                yPosition.setProgress(data.getBaseInfo().getYPosition());
                data.getBaseInfo().xPositionProperty().bindBidirectional(xPosition.progressProperty());
                data.getBaseInfo().yPositionProperty().bindBidirectional(yPosition.progressProperty());

                xPositionText.stringProperty().bind(Bindings.createStringBinding(() -> xPosition.getProgress() / 10f + " %", xPosition.progressProperty()));
                yPositionText.stringProperty().bind(Bindings.createStringBinding(() -> yPosition.getProgress() / 10f + " %", yPosition.progressProperty()));

                FCLSpinner<BaseInfoData.SizeType> sizeTypeSpinner = findInfoView(R.id.size_type);
                ArrayList<BaseInfoData.SizeType> sizeTypes = new ArrayList<>();
                sizeTypes.add(BaseInfoData.SizeType.PERCENTAGE);
                sizeTypes.add(BaseInfoData.SizeType.ABSOLUTE);
                sizeTypeSpinner.setDataList(sizeTypes);
                ArrayList<String> sizeTypeString = new ArrayList<>();
                sizeTypeString.add(context.getString(R.string.view_info_size_type_percentage));
                sizeTypeString.add(context.getString(R.string.view_info_size_type_absolute));
                ArrayAdapter<String> sizeTypeAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, sizeTypeString);
                sizeTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                sizeTypeSpinner.setAdapter(sizeTypeAdapter);
                sizeTypeSpinner.setSelection(data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE ? 0 : 1);
                FXUtils.bindSelection(sizeTypeSpinner, data.getBaseInfo().sizeTypeProperty());
                
                FCLLinearLayout widthReferenceLayout = findInfoView(R.id.width_reference_layout);
                FCLLinearLayout heightReferenceLayout = findInfoView(R.id.height_reference_layout);
                widthReferenceLayout.visibilityProperty().bind(Bindings.createBooleanBinding(() -> data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE, data.getBaseInfo().sizeTypeProperty()));
                heightReferenceLayout.visibilityProperty().bind(Bindings.createBooleanBinding(() -> data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE, data.getBaseInfo().sizeTypeProperty()));

                ArrayList<BaseInfoData.PercentageSize.Reference> references = new ArrayList<>();
                references.add(BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH);
                references.add(BaseInfoData.PercentageSize.Reference.SCREEN_HEIGHT);
                ArrayList<String> referenceString = new ArrayList<>();
                referenceString.add(context.getString(R.string.view_info_reference_width));
                referenceString.add(context.getString(R.string.view_info_reference_height));
                
                FCLSpinner<BaseInfoData.PercentageSize.Reference> widthReferenceSpinner = findInfoView(R.id.width_reference);
                widthReferenceSpinner.setDataList(references);
                ArrayAdapter<String> widthReferenceAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, referenceString);
                widthReferenceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                widthReferenceSpinner.setAdapter(widthReferenceAdapter);
                widthReferenceSpinner.setSelection(data.getBaseInfo().getPercentageWidth().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ? 0 : 1);
                FXUtils.bindSelection(widthReferenceSpinner, data.getBaseInfo().getPercentageWidth().referenceProperty());

                FCLSpinner<BaseInfoData.PercentageSize.Reference> heightReferenceSpinner = findInfoView(R.id.height_reference);
                heightReferenceSpinner.setDataList(references);
                ArrayAdapter<String> heightReferenceAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, referenceString);
                heightReferenceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                heightReferenceSpinner.setAdapter(heightReferenceAdapter);
                heightReferenceSpinner.setSelection(data.getBaseInfo().getPercentageHeight().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ? 0 : 1);
                FXUtils.bindSelection(heightReferenceSpinner, data.getBaseInfo().getPercentageHeight().referenceProperty());

                FCLPreciseSeekBar width = findInfoView(R.id.width);
                FCLPreciseSeekBar height = findInfoView(R.id.height);

                FCLTextView widthText = findInfoView(R.id.width_text);
                FCLTextView heightText = findInfoView(R.id.height_text);

                widthText.setOnClickListener(v -> openTextEditDialog(context, width.progressProperty(), data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE));
                heightText.setOnClickListener(v -> openTextEditDialog(context, height.progressProperty(), data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE));

                if (data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE) {
                    width.setMax(1000);
                    height.setMax(1000);
                    data.getBaseInfo().absoluteWidthProperty().unbindBidirectional(width.progressProperty());
                    data.getBaseInfo().absoluteHeightProperty().unbindBidirectional(height.progressProperty());
                    width.setProgress(data.getBaseInfo().getPercentageWidth().getSize());
                    height.setProgress(data.getBaseInfo().getPercentageHeight().getSize());
                    data.getBaseInfo().getPercentageWidth().sizeProperty().bindBidirectional(width.progressProperty());
                    data.getBaseInfo().getPercentageHeight().sizeProperty().bindBidirectional(height.progressProperty());
                } else {
                    width.setMax(ConvertUtils.px2dip(context, AndroidUtils.getScreenWidth(menu.getActivity())));
                    height.setMax(ConvertUtils.px2dip(context, AndroidUtils.getScreenHeight(menu.getActivity())));
                    data.getBaseInfo().getPercentageWidth().sizeProperty().unbindBidirectional(width.progressProperty());
                    data.getBaseInfo().getPercentageHeight().sizeProperty().unbindBidirectional(height.progressProperty());
                    width.setProgress(data.getBaseInfo().getAbsoluteWidth());
                    height.setProgress(data.getBaseInfo().getAbsoluteHeight());
                    data.getBaseInfo().absoluteWidthProperty().bindBidirectional(width.progressProperty());
                    data.getBaseInfo().absoluteHeightProperty().bindBidirectional(height.progressProperty());
                }

                data.getBaseInfo().sizeTypeProperty().addListener(i -> {
                    if (data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE) {
                        width.setMax(1000);
                        height.setMax(1000);
                        data.getBaseInfo().absoluteWidthProperty().unbindBidirectional(width.progressProperty());
                        data.getBaseInfo().absoluteHeightProperty().unbindBidirectional(height.progressProperty());
                        width.setProgress(data.getBaseInfo().getPercentageWidth().getSize());
                        height.setProgress(data.getBaseInfo().getPercentageHeight().getSize());
                        data.getBaseInfo().getPercentageWidth().sizeProperty().bindBidirectional(width.progressProperty());
                        data.getBaseInfo().getPercentageHeight().sizeProperty().bindBidirectional(height.progressProperty());
                    } else {
                        width.setMax(ConvertUtils.px2dip(context, AndroidUtils.getScreenWidth(menu.getActivity())));
                        height.setMax(ConvertUtils.px2dip(context, AndroidUtils.getScreenHeight(menu.getActivity())));
                        data.getBaseInfo().getPercentageWidth().sizeProperty().unbindBidirectional(width.progressProperty());
                        data.getBaseInfo().getPercentageHeight().sizeProperty().unbindBidirectional(height.progressProperty());
                        width.setProgress(data.getBaseInfo().getAbsoluteWidth());
                        height.setProgress(data.getBaseInfo().getAbsoluteHeight());
                        data.getBaseInfo().absoluteWidthProperty().bindBidirectional(width.progressProperty());
                        data.getBaseInfo().absoluteHeightProperty().bindBidirectional(height.progressProperty());
                    }
                });

                widthText.stringProperty().bind(Bindings.createStringBinding(() -> data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE ? (data.getBaseInfo().getPercentageWidth().getSize() / 10f + " %") : (data.getBaseInfo().getAbsoluteWidth() + " dp"), data.getBaseInfo().sizeTypeProperty(), data.getBaseInfo().absoluteWidthProperty(), data.getBaseInfo().percentageWidthProperty(), width.progressProperty()));
                heightText.stringProperty().bind(Bindings.createStringBinding(() -> data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE ? (data.getBaseInfo().getPercentageHeight().getSize() / 10f + " %") : (data.getBaseInfo().getAbsoluteHeight() + " dp"), data.getBaseInfo().sizeTypeProperty(), data.getBaseInfo().absoluteHeightProperty(), data.getBaseInfo().percentageHeightProperty(), height.progressProperty()));

                FCLButton style = findInfoView(R.id.style);
                style.setOnClickListener(v -> {
                    ButtonStyleDialog dialog = new ButtonStyleDialog(context, true, data.getStyle(), data::setStyle);
                    dialog.show();
                });

                FCLTextView styleText = findInfoView(R.id.style_text);
                styleText.stringProperty().bind(Bindings.createStringBinding(() -> data.getStyle().getName(), data.styleProperty()));
            }

            {
                FCLSwitch pointerFollow = findEventView(R.id.pointer_follow);
                FCLSwitch movable = findEventView(R.id.movable);
                pointerFollow.setChecked(data.getEvent().isPointerFollow());
                movable.setChecked(data.getEvent().isMovable());
                FXUtils.bindBoolean(pointerFollow, data.getEvent().pointerFollowProperty());
                FXUtils.bindBoolean(movable, data.getEvent().movableProperty());

                FCLLinearLayout childContainer = findEventView(R.id.container);

                FCLLinearLayout pressLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_button_event_child, null);
                FCLLinearLayout longPressLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_button_event_child, null);
                FCLLinearLayout clickLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_button_event_child, null);
                FCLLinearLayout doubleClickLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_button_event_child, null);

                FCLLinearLayout[] eventLayouts = new FCLLinearLayout[] {
                        pressLayout,
                        longPressLayout,
                        clickLayout,
                        doubleClickLayout
                };

                for (int i = 0; i < eventLayouts.length; i++) {
                    FCLLinearLayout layout = eventLayouts[i];
                    ButtonEventData.Event e;
                    switch (i) {
                        case 1:
                            e = data.getEvent().getLongPressEvent();
                            break;
                        case 2:
                            e = data.getEvent().getClickEvent();
                            break;
                        case 3:
                            e = data.getEvent().getDoubleClickEvent();
                            break;
                        default:
                            e = data.getEvent().getPressEvent();
                            break;
                    }

                    FCLSwitch autoKeep = layout.findViewById(R.id.auto_keep);
                    FCLSwitch autoClick = layout.findViewById(R.id.auto_click);
                    FCLSwitch openMenu = layout.findViewById(R.id.open_menu);
                    FCLSwitch touchMode = layout.findViewById(R.id.touch_mode);
                    FCLSwitch mouseMode = layout.findViewById(R.id.mouse_mode);
                    FCLSwitch input = layout.findViewById(R.id.input);
                    FCLSwitch quickInput = layout.findViewById(R.id.quick_input);
                    autoKeep.setChecked(e.isAutoKeep());
                    autoClick.setChecked(e.isAutoClick());
                    openMenu.setChecked(e.isOpenMenu());
                    touchMode.setChecked(e.isSwitchTouchMode());
                    mouseMode.setChecked(e.isSwitchMouseMode());
                    input.setChecked(e.isInput());
                    quickInput.setChecked(e.isQuickInput());
                    FXUtils.bindBoolean(autoKeep, e.autoKeepProperty());
                    FXUtils.bindBoolean(autoClick, e.autoClickProperty());
                    FXUtils.bindBoolean(openMenu, e.openMenuProperty());
                    FXUtils.bindBoolean(touchMode, e.switchTouchModeProperty());
                    FXUtils.bindBoolean(mouseMode, e.switchMouseModeProperty());
                    FXUtils.bindBoolean(input, e.inputProperty());
                    FXUtils.bindBoolean(quickInput, e.quickInputProperty());

                    FCLEditText editText = layout.findViewById(R.id.output_text);
                    editText.setText(e.getOutputText());
                    e.outputTextProperty().bind(editText.stringProperty());

                    FCLButton keycode = layout.findViewById(R.id.keycode);
                    FCLButton bindGroup = layout.findViewById(R.id.bind_group);
                    keycode.setOnClickListener(v -> {
                        SelectKeycodeDialog dialog = new SelectKeycodeDialog(context, e.outputKeycodesList(), false, true);
                        dialog.show();
                    });
                    bindGroup.setOnClickListener(v -> {
                        ObservableList<ControlViewGroup> selectedViewGroups = FXCollections.observableArrayList(new ArrayList<>());
                        for (ControlViewGroup vg : menu.getController().viewGroups()) {
                            if (e.bindViewGroupList().contains(vg.getId())) {
                                selectedViewGroups.add(vg);
                            }
                        }
                        ViewGroupDialog dialog = new ViewGroupDialog(context, menu, true, selectedViewGroups, viewGroups -> e.setBindViewGroup(FXCollections.observableList(viewGroups.stream().map(ControlViewGroup::getId).collect(Collectors.toList()))));
                        dialog.show();
                    });
                }

                childContainer.addView(pressLayout);

                FCLTabLayout tabLayout = findEventView(R.id.tab_layout);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        childContainer.removeAllViews();
                        childContainer.addView(eventLayouts[tab.getPosition()]);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }

            container.addView(infoLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @Override
        public CustomControl getView() {
            return data.clone();
        }

        @Override
        public void onLayoutChange(int position) {
            container.removeAllViews();
            container.addView(position == 0 ? infoLayout : eventLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @NonNull
        public final <T extends View> T findInfoView(int id) {
            return infoLayout.findViewById(id);
        }

        @NonNull
        public final <T extends View> T findEventView(int id) {
            return eventLayout.findViewById(id);
        }

        private void openTextEditDialog(Context context, IntegerProperty property, boolean isPercentage) {
            EditDialog dialog = new EditDialog(context, s -> {
                if (s.matches("\\d+(\\.\\d+)?$")) {
                    float progress = Float.parseFloat(s);
                    if (isPercentage) {
                        progress = progress > 100 ? 100 : progress;
                        property.set((int) (progress * 10));
                    } else {
                        property.set((int) progress);
                    }
                }
            });
            dialog.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            dialog.show();
        }
    }

    private static class EditDirectionDetails implements Details {

        private final ControlDirectionData data;
        private final FCLLinearLayout container;

        private final FCLLinearLayout infoLayout;
        private final FCLLinearLayout eventLayout;

        public EditDirectionDetails(Context context, GameMenu menu, ControlDirectionData data, FCLLinearLayout container) {
            this.data = data;
            this.container = container;

            infoLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_direction_info, null);
            eventLayout = (FCLLinearLayout) LayoutInflater.from(context).inflate(R.layout.view_edit_direction_event, null);

            {
                FCLSpinner<BaseInfoData.VisibilityType> visibilityTypeSpinner = findInfoView(R.id.visibility);
                ArrayList<BaseInfoData.VisibilityType> visibilityTypes = new ArrayList<>();
                visibilityTypes.add(BaseInfoData.VisibilityType.ALWAYS);
                visibilityTypes.add(BaseInfoData.VisibilityType.IN_GAME);
                visibilityTypes.add(BaseInfoData.VisibilityType.MENU);
                visibilityTypeSpinner.setDataList(visibilityTypes);
                ArrayList<String> visibilityTypeString = new ArrayList<>();
                visibilityTypeString.add(context.getString(R.string.view_info_visibility_always));
                visibilityTypeString.add(context.getString(R.string.view_info_visibility_game));
                visibilityTypeString.add(context.getString(R.string.view_info_visibility_menu));
                ArrayAdapter<String> visibilityTypeAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, visibilityTypeString);
                visibilityTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                visibilityTypeSpinner.setAdapter(visibilityTypeAdapter);
                visibilityTypeSpinner.setSelection(data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.ALWAYS ?
                        0 : (data.getBaseInfo().getVisibilityType() == BaseInfoData.VisibilityType.IN_GAME ?
                        1 : 2));
                FXUtils.bindSelection(visibilityTypeSpinner, data.getBaseInfo().visibilityTypeProperty());

                FCLPreciseSeekBar xPosition = findInfoView(R.id.x_position);
                FCLPreciseSeekBar yPosition = findInfoView(R.id.y_position);

                FCLTextView xPositionText = findInfoView(R.id.x_position_text);
                FCLTextView yPositionText = findInfoView(R.id.y_position_text);

                xPosition.setProgress(data.getBaseInfo().getXPosition());
                yPosition.setProgress(data.getBaseInfo().getYPosition());
                data.getBaseInfo().xPositionProperty().bindBidirectional(xPosition.progressProperty());
                data.getBaseInfo().yPositionProperty().bindBidirectional(yPosition.progressProperty());

                xPositionText.stringProperty().bind(Bindings.createStringBinding(() -> xPosition.getProgress() / 10f + " %", xPosition.progressProperty()));
                yPositionText.stringProperty().bind(Bindings.createStringBinding(() -> yPosition.getProgress() / 10f + " %", yPosition.progressProperty()));

                FCLSpinner<BaseInfoData.SizeType> sizeTypeSpinner = findInfoView(R.id.size_type);
                ArrayList<BaseInfoData.SizeType> sizeTypes = new ArrayList<>();
                sizeTypes.add(BaseInfoData.SizeType.PERCENTAGE);
                sizeTypes.add(BaseInfoData.SizeType.ABSOLUTE);
                sizeTypeSpinner.setDataList(sizeTypes);
                ArrayList<String> sizeTypeString = new ArrayList<>();
                sizeTypeString.add(context.getString(R.string.view_info_size_type_percentage));
                sizeTypeString.add(context.getString(R.string.view_info_size_type_absolute));
                ArrayAdapter<String> sizeTypeAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, sizeTypeString);
                sizeTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                sizeTypeSpinner.setAdapter(sizeTypeAdapter);
                sizeTypeSpinner.setSelection(data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE ? 0 : 1);
                FXUtils.bindSelection(sizeTypeSpinner, data.getBaseInfo().sizeTypeProperty());

                FCLLinearLayout sizeReferenceLayout = findInfoView(R.id.size_reference_layout);
                sizeReferenceLayout.visibilityProperty().bind(Bindings.createBooleanBinding(() -> data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE, data.getBaseInfo().sizeTypeProperty()));

                ArrayList<BaseInfoData.PercentageSize.Reference> references = new ArrayList<>();
                references.add(BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH);
                references.add(BaseInfoData.PercentageSize.Reference.SCREEN_HEIGHT);
                ArrayList<String> referenceString = new ArrayList<>();
                referenceString.add(context.getString(R.string.view_info_reference_width));
                referenceString.add(context.getString(R.string.view_info_reference_height));

                FCLSpinner<BaseInfoData.PercentageSize.Reference> sizeReferenceSpinner = findInfoView(R.id.size_reference);
                sizeReferenceSpinner.setDataList(references);
                ArrayAdapter<String> sizeReferenceAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, referenceString);
                sizeReferenceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                sizeReferenceSpinner.setAdapter(sizeReferenceAdapter);
                sizeReferenceSpinner.setSelection(data.getBaseInfo().getPercentageWidth().getReference() == BaseInfoData.PercentageSize.Reference.SCREEN_WIDTH ? 0 : 1);
                FXUtils.bindSelection(sizeReferenceSpinner, data.getBaseInfo().getPercentageWidth().referenceProperty());

                FCLPreciseSeekBar size = findInfoView(R.id.size);

                FCLTextView sizeText = findInfoView(R.id.size_text);

                if (data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE) {
                    size.setMax(1000);
                    data.getBaseInfo().absoluteWidthProperty().unbindBidirectional(size.progressProperty());
                    size.setProgress(data.getBaseInfo().getPercentageWidth().getSize());
                    data.getBaseInfo().getPercentageWidth().sizeProperty().bindBidirectional(size.progressProperty());
                } else {
                    size.setMax(ConvertUtils.px2dip(context, AndroidUtils.getScreenHeight(menu.getActivity())));
                    data.getBaseInfo().getPercentageWidth().sizeProperty().unbindBidirectional(size.progressProperty());
                    size.setProgress(data.getBaseInfo().getAbsoluteWidth());
                    data.getBaseInfo().absoluteWidthProperty().bindBidirectional(size.progressProperty());
                }

                data.getBaseInfo().sizeTypeProperty().addListener(i -> {
                    if (data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE) {
                        size.setMax(1000);
                        data.getBaseInfo().absoluteWidthProperty().unbindBidirectional(size.progressProperty());
                        size.setProgress(data.getBaseInfo().getPercentageWidth().getSize());
                        data.getBaseInfo().getPercentageWidth().sizeProperty().bindBidirectional(size.progressProperty());
                    } else {
                        size.setMax(ConvertUtils.px2dip(context, AndroidUtils.getScreenHeight(menu.getActivity())));
                        data.getBaseInfo().getPercentageWidth().sizeProperty().unbindBidirectional(size.progressProperty());
                        size.setProgress(data.getBaseInfo().getAbsoluteWidth());
                        data.getBaseInfo().absoluteWidthProperty().bindBidirectional(size.progressProperty());
                    }
                });

                InvalidationListener listener = i -> {
                    data.getBaseInfo().setAbsoluteHeight(data.getBaseInfo().getAbsoluteWidth());
                    data.getBaseInfo().setPercentageHeight(data.getBaseInfo().getPercentageWidth().clone());
                };

                data.getBaseInfo().absoluteWidthProperty().addListener(listener);
                data.getBaseInfo().getPercentageWidth().addListener(listener);
                data.getBaseInfo().percentageWidthProperty().addListener(listener);

                sizeText.stringProperty().bind(Bindings.createStringBinding(() -> data.getBaseInfo().getSizeType() == BaseInfoData.SizeType.PERCENTAGE ? (data.getBaseInfo().getPercentageWidth().getSize() / 10f + " %") : (data.getBaseInfo().getAbsoluteWidth() + " dp"), data.getBaseInfo().sizeTypeProperty(), data.getBaseInfo().absoluteWidthProperty(), data.getBaseInfo().percentageWidthProperty(), size.progressProperty()));

                FCLButton style = findInfoView(R.id.style);
                style.setOnClickListener(v -> {
                    DirectionStyleDialog dialog = new DirectionStyleDialog(context, true, data.getStyle(), data::setStyle);
                    dialog.show();
                });

                FCLTextView styleText = findInfoView(R.id.style_text);
                styleText.stringProperty().bind(Bindings.createStringBinding(() -> data.getStyle().getName(), data.styleProperty()));
            }

            {
                FCLButton up = findEventView(R.id.up);
                FCLButton down = findEventView(R.id.down);
                FCLButton left = findEventView(R.id.left);
                FCLButton right = findEventView(R.id.right);
                FCLButton sneak = findEventView(R.id.sneak_keycode);
                up.setOnClickListener(v -> {
                    ObservableList<Integer> list = FXCollections.observableList(new ArrayList<>());
                    list.add(data.getEvent().getUpKeycode());
                    SelectKeycodeDialog dialog = new SelectKeycodeDialog(context, list, true, false);
                    data.getEvent().upKeycodeProperty().bind(dialog.selectionProperty());
                    dialog.show();
                });
                down.setOnClickListener(v -> {
                    ObservableList<Integer> list = FXCollections.observableList(new ArrayList<>());
                    list.add(data.getEvent().getDownKeycode());
                    SelectKeycodeDialog dialog = new SelectKeycodeDialog(context, list, true, false);
                    data.getEvent().downKeycodeProperty().bind(dialog.selectionProperty());
                    dialog.show();
                });
                left.setOnClickListener(v -> {
                    ObservableList<Integer> list = FXCollections.observableList(new ArrayList<>());
                    list.add(data.getEvent().getLeftKeycode());
                    SelectKeycodeDialog dialog = new SelectKeycodeDialog(context, list, true, false);
                    data.getEvent().leftKeycodeProperty().bind(dialog.selectionProperty());
                    dialog.show();
                });
                right.setOnClickListener(v -> {
                    ObservableList<Integer> list = FXCollections.observableList(new ArrayList<>());
                    list.add(data.getEvent().getRightKeycode());
                    SelectKeycodeDialog dialog = new SelectKeycodeDialog(context, list, true, false);
                    data.getEvent().rightKeycodeProperty().bind(dialog.selectionProperty());
                    dialog.show();
                });
                sneak.setOnClickListener(v -> {
                    ObservableList<Integer> list = FXCollections.observableList(new ArrayList<>());
                    list.add(data.getEvent().getSneakKeycode());
                    SelectKeycodeDialog dialog = new SelectKeycodeDialog(context, list, true, false);
                    data.getEvent().sneakKeycodeProperty().bind(dialog.selectionProperty());
                    dialog.show();
                });

                FCLSwitch sneakSwitch = findEventView(R.id.sneak);
                sneakSwitch.setChecked(data.getEvent().isSneak());
                FXUtils.bindBoolean(sneakSwitch, data.getEvent().sneakProperty());

                FCLSpinner<DirectionEventData.FollowOption> followOptionSpinner = findEventView(R.id.follow);
                ArrayList<DirectionEventData.FollowOption> followOptions = new ArrayList<>();
                followOptions.add(DirectionEventData.FollowOption.FIXED);
                followOptions.add(DirectionEventData.FollowOption.CENTER_FOLLOW);
                followOptions.add(DirectionEventData.FollowOption.FOLLOW);
                followOptionSpinner.setDataList(followOptions);
                ArrayList<String> followOptionString = new ArrayList<>();
                followOptionString.add(context.getString(R.string.edit_direction_event_follow_fix));
                followOptionString.add(context.getString(R.string.edit_direction_event_follow_center));
                followOptionString.add(context.getString(R.string.edit_direction_event_follow_always));
                ArrayAdapter<String> followOptionAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, followOptionString);
                followOptionAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                followOptionSpinner.setAdapter(followOptionAdapter);
                followOptionSpinner.setSelection(data.getEvent().getFollowOption() == DirectionEventData.FollowOption.FIXED ?
                        0 : (data.getEvent().getFollowOption() == DirectionEventData.FollowOption.CENTER_FOLLOW ?
                        1 : 2));
                FXUtils.bindSelection(followOptionSpinner, data.getEvent().followOptionProperty());
            }

            container.addView(infoLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @Override
        public CustomControl getView() {
            return data.clone();
        }

        @Override
        public void onLayoutChange(int position) {
            container.removeAllViews();
            container.addView(position == 0 ? infoLayout : eventLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @NonNull
        public final <T extends View> T findInfoView(int id) {
            return infoLayout.findViewById(id);
        }

        @NonNull
        public final <T extends View> T findEventView(int id) {
            return eventLayout.findViewById(id);
        }
    }
}
