package com.tungsten.fcl.ui.compose.dialog

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogButtonsRow
import com.tungsten.fclcore.util.StringUtils
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版控制器上传弹窗（3.2 批 4，对应 ui/controller/ControllerUploadDialog
 * + dialog_controller_upload + item_screenshot_path）。
 *
 * 行为对齐：
 * - 名称/作者/简介/描述初值取自控制器（简介与描述同取 controller.description）；
 * - 语言选择沿用遗留 AppCompatSpinner（AndroidView 内嵌，adapter 布局不变），
 *   position → 语言码映射与遗留 switch 完全一致——case 只覆盖 0-5，选乌克兰语/德语（6/7）
 *   时语言码保持上一次取值（附录 D 条目 4 登记的已知缺陷，按任务要求保持原样不修复）；
 * - 3 个设备 CheckBox（手机/平板/其他 → 0/1/2）维护 devices 列表，手机默认勾选（初始 [0]）；
 * - 图标经 MainActivity.fileLauncher 单选 .png，路径显示在按钮左侧；
 * - 截图经 fileLauncher 多选 .png，≤16 张、去重追加，超限 Toast control_info_screenshot_max；
 *   条目内删除按钮移除对应路径；
 * - 分享按钮逐条校验（名称/作者/简介/描述空白、devices 空、screenshots 空、图标路径空白）
 *   任一不满足 Toast input_not_empty，否则回调 onPositive（遗留回调后不 dismiss，保持一致）；
 * - 取消直接 dismiss；窗体宽 400dp、内容区固定高 200dp 滚动对齐遗留 ScrollView；
 *   setCancelable(false) 一致。
 *
 * 有意偏差：
 * - 遗留按钮布局为分享在左、取消在右，本实现按既有 Miuix 弹窗惯例主按钮（分享）居右；
 * - 遗留输入框 hint（input_hint_not_empty）未在 Miuix TextField 复刻，不影响交互逻辑。
 */
class MiuixControllerUploadDialog(
    context: Context,
    @Suppress("unused") private val activity: Activity,
    controller: Controller,
    private val callback: Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val nameState = mutableStateOf(controller.name)
    private val authorState = mutableStateOf(controller.author)
    private val introState = mutableStateOf(controller.description)
    private val descriptionState = mutableStateOf(controller.description)

    /** 语言码，取值迁移同遗留 onItemSelected（初始 setSelection(0) → "all"）。 */
    private var language = "all"

    /** 设备 id 列表（0 手机 / 1 平板 / 2 其他），遗留手机默认勾选 → 初始 [0]。 */
    private val devicesState = mutableStateOf(listOf(0))

    private val iconPathState = mutableStateOf("")
    private val screenshots = mutableStateListOf<String>()

    init {
        setDialogContent {
            DialogContent()
        }
    }

    private fun onPickIcon() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(
            null,
            arrayListOf(".png"),
        ) { files ->
            iconPathState.value = files[0]
        }
    }

    private fun onPickScreenshots() {
        if (screenshots.size < 16) {
            MainActivity.getInstance().fileLauncher.launchMultiSelection(
                null,
                arrayListOf(".png"),
            ) { files ->
                if (files.isNotEmpty()) {
                    files.forEach { path ->
                        if (!screenshots.contains(path) && screenshots.size < 16) {
                            screenshots.add(path)
                        }
                    }
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.control_info_screenshot_max), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDeviceChanged(id: Int, checked: Boolean) {
        val devices = devicesState.value.toMutableList()
        if (checked) {
            devices.add(id)
        } else {
            devices.remove(id)
        }
        devicesState.value = devices
    }

    private fun onShare() {
        if (StringUtils.isBlank(nameState.value)
            || StringUtils.isBlank(authorState.value)
            || StringUtils.isBlank(introState.value)
            || StringUtils.isBlank(descriptionState.value)
            || devicesState.value.isEmpty()
            || screenshots.isEmpty()
            || StringUtils.isBlank(iconPathState.value)
        ) {
            Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
        } else {
            // 遗留回调后不 dismiss（上传流程由调用方接管），保持一致
            callback.onPositive(
                nameState.value,
                authorState.value,
                introState.value,
                descriptionState.value,
                language,
                ArrayList(devicesState.value),
                ArrayList(screenshots),
                iconPathState.value,
            )
        }
    }

    @Composable
    private fun DialogContent() {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            // 对齐遗留 dialog_background（#F4F4F4 / #232323）→ surface token
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.surface),
        ) {
            Text(
                text = stringResource(R.string.control_info_edit),
                modifier = Modifier.fillMaxWidth(),
                style = MiuixTheme.textStyles.title4,
                textAlign = TextAlign.Center,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 10.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                InputRow(stringResource(R.string.control_info_name), nameState.value) {
                    nameState.value = it
                }
                InputRow(stringResource(R.string.control_info_author), authorState.value) {
                    authorState.value = it
                }
                InputRow(stringResource(R.string.control_info_intro), introState.value) {
                    introState.value = it
                }
                InputRow(stringResource(R.string.control_info_description), descriptionState.value) {
                    descriptionState.value = it
                }
                LangRow()
                DevicesRow()
                IconRow()
                ScreenshotRow()
                screenshots.forEach { path ->
                    ScreenshotItemRow(path)
                }
            }
            FCLDialogButtonsRow(
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(R.string.action_share),
                        onClick = { onShare() },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    ),
                ),
            )
        }
    }

    @Composable
    private fun InputRow(label: String, value: String, onValueChange: (String) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(250.dp),
                singleLine = true,
            )
        }
    }

    /** 语言选择：内嵌遗留 AppCompatSpinner，adapter 与选中监听逻辑完全沿用遗留实现。 */
    @Composable
    private fun LangRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.control_info_lang),
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            AndroidView(
                modifier = Modifier.width(250.dp),
                factory = { ctx ->
                    AppCompatSpinner(ctx).apply {
                        val langs = arrayListOf(
                            ctx.getString(R.string.curse_category_0),
                            ctx.getString(R.string.settings_launcher_language_english),
                            ctx.getString(R.string.settings_launcher_language_simplified_chinese),
                            ctx.getString(R.string.settings_launcher_language_russian),
                            ctx.getString(R.string.settings_launcher_language_brazilian_portuguese),
                            ctx.getString(R.string.settings_launcher_language_persian),
                            ctx.getString(R.string.settings_launcher_language_ukrainian),
                            ctx.getString(R.string.settings_launcher_language_german),
                        )
                        val langAdapter = ArrayAdapter(ctx, R.layout.item_spinner_theme_color, langs)
                        langAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
                        adapter = langAdapter
                        setSelection(0)
                        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                // 与遗留 switch 一致：只覆盖 case 0-5，6(uk)/7(de) 不更新语言码
                                //（附录 D 条目 4 登记的已知缺陷，保持原样不修复）
                                when (position) {
                                    0 -> language = "all"
                                    1 -> language = "en"
                                    2 -> language = "zh_CN"
                                    3 -> language = "ru"
                                    4 -> language = "pt"
                                    5 -> language = "fa"
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                },
            )
        }
    }

    @Composable
    private fun DevicesRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.control_info_device),
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier.width(250.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DeviceCheckbox(stringResource(R.string.control_download_device_phone), 0)
                DeviceCheckbox(stringResource(R.string.control_download_device_pad), 1)
                DeviceCheckbox(stringResource(R.string.control_download_device_other), 2)
            }
        }
    }

    @Composable
    private fun DeviceCheckbox(label: String, id: Int) {
        val checked = devicesState.value.contains(id)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                state = if (checked) ToggleableState.On else ToggleableState.Off,
                onClick = { onDeviceChanged(id, !checked) },
            )
            Text(
                text = label,
                style = MiuixTheme.textStyles.footnote1,
                maxLines = 1,
            )
        }
    }

    @Composable
    private fun IconRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.control_info_icon),
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier.width(250.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = iconPathState.value,
                    style = MiuixTheme.textStyles.footnote1,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )
                Spacer(Modifier.width(10.dp))
                IconButton(onClick = { onPickIcon() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_edit_24),
                        contentDescription = null,
                        // 对齐遗留 dialog_controller_upload：darker_gray 静态描边色，不用 color2
                        tint = MiuixTheme.colorScheme.outline,
                    )
                }
            }
        }
    }

    @Composable
    private fun ScreenshotRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.control_info_screenshot),
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier.width(250.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onPickScreenshots() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_edit_24),
                        contentDescription = null,
                        // 对齐遗留 dialog_controller_upload：darker_gray 静态描边色，不用 color2
                        tint = MiuixTheme.colorScheme.outline,
                    )
                }
            }
        }
    }

    @Composable
    private fun ScreenshotItemRow(path: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = path,
                style = MiuixTheme.textStyles.footnote1,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(10.dp))
            IconButton(onClick = { screenshots.remove(path) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_close_24),
                    contentDescription = null,
                    // 对齐遗留 item_screenshot_path：darker_gray 静态描边色，不用 color2
                    tint = MiuixTheme.colorScheme.outline,
                )
            }
        }
    }

    fun interface Callback {
        fun onPositive(
            name: String,
            author: String,
            intro: String,
            description: String,
            lang: String,
            devices: ArrayList<Int>,
            screenshots: ArrayList<String>,
            iconPath: String,
        )
    }
}
