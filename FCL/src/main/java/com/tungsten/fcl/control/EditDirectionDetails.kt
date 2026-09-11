package com.tungsten.fcl.control

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mio.util.getScreenHeight
import com.tungsten.fcl.R
import com.tungsten.fcl.control.data.ControlDirectionData
import com.tungsten.fcl.control.data.DirectionEventData
import com.tungsten.fcl.control.data.DirectionStyles
import com.tungsten.fcl.databinding.ViewEditDirectionEventBinding
import com.tungsten.fcl.databinding.ViewEditDirectionInfoBinding
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.fakefx.collections.ObservableList
import com.tungsten.fcllibrary.component.view.FCLSpinner
import com.tungsten.fcllibrary.util.ConvertUtils

/** 方向键控件编辑页：位置尺寸 / 样式 + 摇杆事件配置 */
internal class EditDirectionDetails(
    context: Context,
    menu: GameMenu?,
    private val data: ControlDirectionData,
) : EditViewDetails(context, menu) {

    override val baseInfo get() = data.baseInfo

    private val infoBinding = ViewEditDirectionInfoBinding.inflate(LayoutInflater.from(context))
    private val eventBinding = ViewEditDirectionEventBinding.inflate(LayoutInflater.from(context))

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
        setupVisibilitySpinner(visibility)
        setupSizeTypeSpinner(sizeType)
        bindNumberSeekBar(infoBinding.xPosition, baseInfo.xPositionProperty())
        bindNumberSeekBar(infoBinding.yPosition, baseInfo.yPositionProperty())
        bindSizeSeekBar(
            infoBinding.size, baseInfo.percentageWidth.sizeProperty(), baseInfo.absoluteWidthProperty(),
            ConvertUtils.px2dip(context, getScreenHeight().toFloat()), infoBinding.sizeReferenceLayout
        )

        // 方向键宽高始终一致：宽变化时同步到高
        val syncHeight = InvalidationListener {
            baseInfo.absoluteHeight = baseInfo.absoluteWidth
            baseInfo.percentageHeight = baseInfo.percentageWidth.clone()
        }
        baseInfo.absoluteWidthProperty().addListener(syncHeight)
        baseInfo.percentageWidth.addListener(syncHeight)
        baseInfo.percentageWidthProperty().addListener(syncHeight)

        infoBinding.styleText.text = data.style.name
        infoBinding.style.setOnClickListener {
            val target = DirectionStyles.findStyleByName(data.style.name)
            DirectionStyleDialog(context, true, target) { style ->
                data.setStyle(style)
                infoBinding.styleText.text = style.name
            }.apply { setGameMenu(menu) }.show()
        }
    }

    private fun setupEvent() {
        selectKeycodes(eventBinding.up, { data.event.upKeycodeList() }) { data.event.setUpKeycode(it) }
        selectKeycodes(eventBinding.down, { data.event.downKeycodeList() }) { data.event.setDownKeycode(it) }
        selectKeycodes(eventBinding.left, { data.event.leftKeycodeList() }) { data.event.setLeftKeycode(it) }
        selectKeycodes(eventBinding.right, { data.event.rightKeycodeList() }) { data.event.setRightKeycode(it) }

        eventBinding.sneakKeycode.setOnClickListener {
            val list = FXCollections.observableArrayList(data.event.sneakKeycode)
            val dialog = SelectKeycodeDialog(context, list, true, false)
            data.event.sneakKeycodeProperty().bind(dialog.selectionProperty())
            dialog.show()
        }

        eventBinding.sneak.isChecked = data.event.isSneak
        FXUtils.bindBoolean(eventBinding.sneak, data.event.sneakProperty())

        setupFollowSpinner(eventBinding.follow as FCLSpinner<String>)

        bindNumberSeekBar(eventBinding.deadZone, data.event.deadZoneProperty())
        bindNumberSeekBar(eventBinding.lockThreshold, data.event.lockThresholdProperty())

        eventBinding.canLock.isChecked = data.event.isCanLock
        FXUtils.bindBoolean(eventBinding.canLock, data.event.canLockProperty())
    }

    /** 跟随模式行：固定 / 中心跟随 / 总是跟随 */
    private fun setupFollowSpinner(spinner: FCLSpinner<String>) {
        val options = listOf(
            DirectionEventData.FollowOption.FIXED,
            DirectionEventData.FollowOption.CENTER_FOLLOW,
            DirectionEventData.FollowOption.FOLLOW
        )
        spinner.setItems(
            listOf(
                context.getString(R.string.edit_direction_event_follow_fix),
                context.getString(R.string.edit_direction_event_follow_center),
                context.getString(R.string.edit_direction_event_follow_always)
            )
        )
        spinner.setSelection(options.indexOf(data.event.followOption))
        spinner.setOnItemSelectedListener { index, _ -> data.event.followOptionProperty().set(options[index]) }
    }

    /** 方向键位行：弹出多选键位对话框，确认后整体回写 */
    private fun selectKeycodes(
        button: View,
        current: () -> List<Int>,
        commit: (ObservableList<Int>) -> Unit,
    ) {
        button.setOnClickListener {
            val list = FXCollections.observableArrayList<Int>().apply { addAll(current()) }
            SelectKeycodeDialog(context, list, false, false) { commit(list) }.show()
        }
    }
}
