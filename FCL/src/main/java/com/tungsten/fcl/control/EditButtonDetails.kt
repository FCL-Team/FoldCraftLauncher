package com.tungsten.fcl.control

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.mio.util.getScreenWidth
import com.mio.util.getScreenHeight
import com.tungsten.fcl.control.data.BaseInfoData
import com.tungsten.fcl.control.data.ButtonEventData
import com.tungsten.fcl.control.data.ButtonStyles
import com.tungsten.fcl.control.data.ControlButtonData
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fcl.databinding.ViewEditButtonEventBinding
import com.tungsten.fcl.databinding.ViewEditButtonEventChildBinding
import com.tungsten.fcl.databinding.ViewEditButtonInfoBinding
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fcllibrary.component.view.FCLSpinner
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.stream.Collectors

/** 按钮控件编辑页：文本 / 位置尺寸 / 样式 + 触发事件配置 */
internal class EditButtonDetails(
    context: Context,
    menu: GameMenu?,
    private val data: ControlButtonData,
) : EditViewDetails(context, menu) {

    override val baseInfo: BaseInfoData get() = data.baseInfo

    private val infoBinding = ViewEditButtonInfoBinding.inflate(LayoutInflater.from(context))
    private val eventBinding = ViewEditButtonEventBinding.inflate(LayoutInflater.from(context))

    override val infoLayout: View get() = infoBinding.root
    override val eventLayout: View get() = eventBinding.root

    init {
        setupInfo()
        setupEvent()
    }

    override fun getView() = data.clone()

    @Suppress("UNCHECKED_CAST")
    private fun setupInfo() {
        // ViewBinding 对布局中的泛型控件生成 raw 类型，条目实际为 String
        val visibility = infoBinding.visibility as FCLSpinner<String>
        val sizeType = infoBinding.sizeType as FCLSpinner<String>

        infoBinding.text.setText(data.text)
        data.textProperty().bind(infoBinding.text.stringProperty())

        setupVisibilitySpinner(visibility)
        setupSizeTypeSpinner(sizeType)
        setupReferenceSpinner(infoBinding.widthReference as FCLSpinner<String>, baseInfo.percentageWidth)
        setupReferenceSpinner(infoBinding.heightReference as FCLSpinner<String>, baseInfo.percentageHeight)
        bindNumberSeekBar(infoBinding.xPosition, baseInfo.xPositionProperty())
        bindNumberSeekBar(infoBinding.yPosition, baseInfo.yPositionProperty())
        bindSizeSeekBar(
            infoBinding.width, baseInfo.percentageWidth.sizeProperty(), baseInfo.absoluteWidthProperty(),
            ConvertUtils.px2dip(context, getScreenWidth().toFloat()), infoBinding.widthReferenceLayout
        )
        bindSizeSeekBar(
            infoBinding.height, baseInfo.percentageHeight.sizeProperty(), baseInfo.absoluteHeightProperty(),
            ConvertUtils.px2dip(context, getScreenHeight().toFloat()), infoBinding.heightReferenceLayout
        )

        infoBinding.styleText.text = data.style.name
        infoBinding.style.setOnClickListener {
            val target = ButtonStyles.findStyleByName(data.style.name)
            ButtonStyleDialog(context, true, target) { style ->
                data.setStyle(style)
                infoBinding.styleText.text = style.name
            }.apply { setGameMenu(menu) }.show()
        }
    }

    private fun setupEvent() {
        with(eventBinding) {
            pointerFollow.isChecked = data.event.isPointerFollow
            movable.isChecked = data.event.isMovable
            swipable.isChecked = data.event.isSwipable
            FXUtils.bindBoolean(pointerFollow, data.event.pointerFollowProperty())
            FXUtils.bindBoolean(movable, data.event.movableProperty())
            FXUtils.bindBoolean(swipable, data.event.swipableProperty())
        }

        val children = listOf(
            ViewEditButtonEventChildBinding.inflate(LayoutInflater.from(context)),
            ViewEditButtonEventChildBinding.inflate(LayoutInflater.from(context)),
            ViewEditButtonEventChildBinding.inflate(LayoutInflater.from(context)),
            ViewEditButtonEventChildBinding.inflate(LayoutInflater.from(context))
        )
        val events = listOf(
            data.event.pressEvent,
            data.event.longPressEvent,
            data.event.clickEvent,
            data.event.doubleClickEvent
        )
        children.forEachIndexed { index, child -> setupEventChild(child, events[index]) }

        eventBinding.container.addView(children[0].root)
        eventBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                eventBinding.container.removeAllViews()
                eventBinding.container.addView(children[tab.position].root)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupEventChild(binding: ViewEditButtonEventChildBinding, event: ButtonEventData.Event) {
        with(binding) {
            autoKeep.isChecked = event.isAutoKeep
            autoClick.isChecked = event.isAutoClick
            openMenu.isChecked = event.isOpenMenu
            touchMode.isChecked = event.isSwitchTouchMode
            mouseMode.isChecked = event.isSwitchMouseMode
            input.isChecked = event.isInput
            quickInput.isChecked = event.isQuickInput
            FXUtils.bindBoolean(autoKeep, event.autoKeepProperty())
            FXUtils.bindBoolean(autoClick, event.autoClickProperty())
            FXUtils.bindBoolean(openMenu, event.openMenuProperty())
            FXUtils.bindBoolean(touchMode, event.switchTouchModeProperty())
            FXUtils.bindBoolean(mouseMode, event.switchMouseModeProperty())
            FXUtils.bindBoolean(input, event.inputProperty())
            FXUtils.bindBoolean(quickInput, event.quickInputProperty())

            outputText.setText(event.outputText)
            event.outputTextProperty().bind(outputText.stringProperty())

            keycode.setOnClickListener {
                SelectKeycodeDialog(context, event.outputKeycodesList(), false, true).show()
            }
            bindGroup.setOnClickListener {
                val selected = FXCollections.observableArrayList<ControlViewGroup>().apply {
                    menu?.controller?.viewGroups()
                        ?.filter { event.bindViewGroupList().contains(it.id) }
                        ?.let { addAll(it) }
                }
                ViewGroupDialog(context, menu, true, selected) { viewGroups ->
                    event.setBindViewGroup(FXCollections.observableList(viewGroups.stream().map { it.id }.collect(Collectors.toList())))
                }.show()
            }
        }
    }
}
