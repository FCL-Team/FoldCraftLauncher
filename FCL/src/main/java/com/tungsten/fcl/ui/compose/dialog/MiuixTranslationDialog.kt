package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.mod.RemoteModRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版翻译贡献名单查询弹窗（3.2 批 1，对应 ui/download/TranslationDialog + dialog_translation）。
 *
 * 行为对齐遗留实现：
 * - 输入即搜（后台线程 searchMod，主线程刷新列表）；
 * - 列表项文本 "name subname abbr"，点击回调 subname（空则 abbr）并 dismiss；
 * - 取消按钮 dismiss；遗留未调 setCancelable，默认可取消，此处一致。
 */
class MiuixTranslationDialog(
    context: Context,
    repository: RemoteModRepository,
    callback: (String) -> Unit,
) : FCLComposeDialog(context, cancelable = true) {

    // 状态提升为字段（对齐 MiuixTaskDialog），避免组合内裸 mutableStateOf 在重组时重置
    private val queryState = mutableStateOf("")
    private val modsState = mutableStateOf<List<ModTranslations.Mod>>(emptyList())
    private val searchScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        setDialogContent {
            FCLDialogCard(
                // 遗留 dialog_translation 标题为硬编码中文，保持一致
                title = "模组/整合包对应英文查询",
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(R.string.button_cancel),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                FCLTextField(
                    value = queryState.value,
                    onValueChange = { str ->
                        queryState.value = str
                        searchScope.launch(Dispatchers.Default) {
                            val result = ModTranslations.getTranslationsByRepositoryType(repository.type)
                                .searchMod(str)
                            withContext(Dispatchers.Main) {
                                modsState.value = result
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = stringResource(R.string.search),
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                ) {
                    items(modsState.value) { mod ->
                        Text(
                            text = "${mod.name} ${mod.subname} ${mod.abbr}",
                            style = MiuixTheme.textStyles.body2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    dismiss()
                                    callback(mod.subname.ifEmpty { mod.abbr })
                                }
                                .padding(vertical = 10.dp),
                        )
                    }
                }
            }
        }
    }

    override fun dismiss() {
        searchScope.cancel()
        super.dismiss()
    }
}
