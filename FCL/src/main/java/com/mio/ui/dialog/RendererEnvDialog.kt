package com.mio.ui.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.mio.plugin.RendererPlugin.EnvSpec
import com.mio.plugin.RendererPlugin.EnvType
import com.mio.plugin.RendererPlugin.EnvValue
import com.mio.plugin.coerceOrder
import com.mio.plugin.expandOrder
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogRendererEnvBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLEditText
import com.tungsten.fcllibrary.component.view.FCLSpinner
import com.tungsten.fcllibrary.component.view.FCLSwitch
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * v2 渲染器插件与内置渲染器的可配置环境变量编辑对话框：
 * selectable → 选项 Spinner（check 非 null 时附带启用开关），
 * customizable → 输入框（留空不启用），toggleable → 开关，
 * ordered → 方案偏好序（可折叠的编号列表，上移/下移调整；函数级条目启用时刻按当前全局序快照）。
 * 确定时全量回传所有可配置项的当前值。
 */
class RendererEnvDialog(
    context: Context,
    title: String,
    private val specs: List<EnvSpec>,
    private val onConfirm: (Map<String, EnvValue>) -> Unit,
) : FCLDialog(context) {

    private val binding = DialogRendererEnvBinding.inflate(layoutInflater)

    private val switches = mutableMapOf<String, FCLSwitch>()
    private val spinners = mutableMapOf<String, FCLSpinner<String>>()
    private val inputs = mutableMapOf<String, FCLEditText>()

    // ordered 条目状态：当前偏好序 / 折叠容器 / 展开箭头 / 已展开集合
    private val orderedOrders = mutableMapOf<String, MutableList<String>>()
    private val orderedPanels = mutableMapOf<String, LinearLayout>()
    private val orderedArrows = mutableMapOf<String, ImageView>()
    private val expandedOrderedKeys = mutableSetOf<String>()
    private val density = context.resources.displayMetrics.density

    init {
        val params = window?.attributes
        params?.width = ConvertUtils.dip2px(context, 500f)
        val ratio = getScreenWidth().toFloat() / getScreenHeight().toFloat()
        if (ratio >= 1.5f) {
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            params?.height = getScreenHeight() * 1 / 2
        }
        window?.attributes = params

        setContentView(binding.root)
        binding.title.text = title
        specs.forEach { binding.envContainer.addView(buildRow(it)) }
        binding.ok.setOnClickListener { submit() }
        binding.cancel.setOnClickListener { dismiss() }
    }

    private fun buildRow(spec: EnvSpec): LinearLayout {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, (8 * density).toInt(), 0, 0)
        }
        val header = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val label = FCLTextView(context).apply {
            text = spec.title
            textSize = 15f
            // 对话框是普通白/浅底，用主题默认文字色（autoTint 是与主色对比的黑/白，主色偏深时为白色，会看不清）
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        header.addView(label)
        val descriptionView = if (!spec.description.isNullOrEmpty()) FCLTextView(context).apply {
            text = spec.description
            textSize = 12f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else null

        when (spec.type) {
            EnvType.SELECTABLE -> {
                if (spec.checkable) {
                    header.addView(FCLSwitch(context).apply {
                        isChecked = spec.enabled
                        switches[spec.key] = this
                    })
                }
                val spinner = FCLSpinner<String>(context).apply {
                    // 对话框恒浅底（dialog_background），关闭 autoTint（深色模式下其残留
                    // 白色对比色会与浅底同色）并显式使用固定深色文字
                    setAutoTextTint(false)
                    setTextColor(ContextCompat.getColor(context, R.color.primary_text))
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { topMargin = (4 * density).toInt() }
                    setItems(spec.options)
                    setSelection(spec.options.indexOf(spec.value).coerceAtLeast(0))
                    spinners[spec.key] = this
                }
                row.addView(header)
                row.addView(spinner)
            }

            EnvType.CUSTOMIZABLE -> {
                val input = FCLEditText(context).apply {
                    hint = spec.defaultValue
                    setText(spec.value)
                    maxLines = 1
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { topMargin = (4 * density).toInt() }
                    inputs[spec.key] = this
                }
                row.addView(header)
                row.addView(input)
            }

            EnvType.TOGGLEABLE -> {
                header.addView(FCLSwitch(context).apply {
                    isChecked = spec.enabled
                    switches[spec.key] = this
                })
                row.addView(header)
            }

            EnvType.ORDERED -> {
                val ordered = requireNotNull(spec.ordered) { "ORDERED spec ${spec.key} missing ordered" }
                val isException = ordered.inheritFrom != null
                orderedOrders[spec.key] = coerceOrder(spec.value, ordered).split(',').toMutableList()

                orderedArrows[spec.key] = ImageView(context).apply {
                    setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                    imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.primary_text)
                    )
                    layoutParams = LinearLayout.LayoutParams(
                        (24 * density).toInt(), (24 * density).toInt()
                    ).apply { marginEnd = (4 * density).toInt() }
                    isEnabled = !isException || spec.enabled
                    alpha = if (isEnabled) 1f else 0.3f
                    setOnClickListener {
                        if (spec.key in expandedOrderedKeys) expandedOrderedKeys.remove(spec.key)
                        else expandedOrderedKeys.add(spec.key)
                        updateOrderedPanel(spec)
                    }
                }
                header.addView(orderedArrows[spec.key])
                if (spec.checkable) {
                    header.addView(FCLSwitch(context).apply {
                        isChecked = spec.enabled
                        switches[spec.key] = this
                        setOnCheckedChangeListener { _, checked ->
                            if (checked) {
                                // 启用即快照：按当前全局序展开并冻结；此后全局序调整不再跟随
                                //（MG 只读 config.json 落盘值，无法回链全局序）
                                val parentOrder = orderedOrders[ordered.inheritFrom]?.joinToString(",").orEmpty()
                                orderedOrders[spec.key] =
                                    expandOrder(parentOrder, ordered).split(',').toMutableList()
                                renderOrderedRows(spec)
                            }
                            updateOrderedPanel(spec)
                        }
                    })
                }

                val panel = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    visibility = View.GONE
                }
                orderedPanels[spec.key] = panel
                renderOrderedRows(spec)

                row.addView(header)
                row.addView(panel)
            }
        }
        // 说明文字插在标题行之后、控件之前（row 的首个子项恒为 header）
        descriptionView?.let { row.addView(it, 1) }
        return row
    }

    /** ordered 条目折叠态同步：函数级条目仅在启用时可展开，箭头随之旋转/置灰 */
    private fun updateOrderedPanel(spec: EnvSpec) {
        val panel = orderedPanels[spec.key] ?: return
        val checked = switches[spec.key]?.isChecked
        val isException = spec.ordered?.inheritFrom != null
        val expanded = spec.key in expandedOrderedKeys && (!isException || checked == true)
        panel.visibility = if (expanded) View.VISIBLE else View.GONE
        orderedArrows[spec.key]?.apply {
            isEnabled = !isException || checked == true
            rotation = if (expanded) 180f else 0f
            alpha = if (isEnabled) 1f else 0.3f
        }
    }

    /** 重建 ordered 条目的编号行列表（上移/下移后整体重绘） */
    private fun renderOrderedRows(spec: EnvSpec) {
        val panel = orderedPanels[spec.key] ?: return
        val ordered = requireNotNull(spec.ordered)
        val labels = ordered.options.associateBy { it.value }
        val order = orderedOrders[spec.key] ?: return
        panel.removeAllViews()
        order.forEachIndexed { index, value ->
            val option = labels[value] ?: return@forEachIndexed
            val column = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            column.addView(FCLTextView(context).apply {
                text = option.label
                textSize = 14f
            })
            option.description?.let {
                column.addView(FCLTextView(context).apply {
                    text = it
                    textSize = 11f
                    alpha = 0.7f
                })
            }
            panel.addView(LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding((8 * density).toInt(), 0, 0, (2 * density).toInt())
                addView(FCLTextView(context).apply {
                    text = "${index + 1}."
                    textSize = 12f
                    alpha = 0.7f
                    setPadding(0, 0, (8 * density).toInt(), 0)
                })
                addView(column)
                addView(orderedArrowButton(
                    R.drawable.ic_baseline_arrow_upward_24,
                    enabled = index > 0
                ) {
                    order.add(index - 1, order.removeAt(index))
                    renderOrderedRows(spec)
                })
                addView(orderedArrowButton(
                    R.drawable.ic_baseline_arrow_downward_24,
                    enabled = index < order.lastIndex
                ) {
                    order.add(index + 1, order.removeAt(index))
                    renderOrderedRows(spec)
                })
            })
        }
    }

    /** ordered 行的上移/下移小箭头按钮（置灰表示到边界） */
    private fun orderedArrowButton(icon: Int, enabled: Boolean, onClick: () -> Unit): ImageView =
        ImageView(context).apply {
            setImageResource(icon)
            imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.primary_text)
            )
            layoutParams = LinearLayout.LayoutParams(
                (26 * density).toInt(), (26 * density).toInt()
            ).apply { marginEnd = (4 * density).toInt() }
            isEnabled = enabled
            alpha = if (enabled) 1f else 0.3f
            setOnClickListener { if (isEnabled) onClick() }
        }

    private fun submit() {
        val result = mutableMapOf<String, EnvValue>()
        specs.forEach { spec ->
            when (spec.type) {
                EnvType.SELECTABLE -> result[spec.key] = EnvValue(
                    enabled = switches[spec.key]?.isChecked,
                    value = spinners[spec.key]?.getSelectedItem()
                )

                EnvType.CUSTOMIZABLE -> result[spec.key] = EnvValue(
                    value = inputs[spec.key]?.text?.toString().orEmpty()
                )

                EnvType.TOGGLEABLE -> result[spec.key] = EnvValue(
                    enabled = switches[spec.key]?.isChecked
                )

                EnvType.ORDERED -> {
                    val checked = switches[spec.key]?.isChecked
                    val isException = spec.ordered?.inheritFrom != null
                    result[spec.key] = EnvValue(
                        enabled = checked,
                        // 函数级异常仅在启用时回传快照序（禁用即解除独立排序）；全局序恒回传
                        value = orderedOrders[spec.key]?.joinToString(",")
                            ?.takeIf { !isException || checked == true }
                    )
                }
            }
        }
        onConfirm(result)
        dismiss()
    }
}
