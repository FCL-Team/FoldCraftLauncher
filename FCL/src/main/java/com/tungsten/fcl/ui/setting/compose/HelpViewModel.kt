package com.tungsten.fcl.ui.setting.compose

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.tungsten.fcl.ui.bridge.FCLViewModel
import com.tungsten.fcl.ui.setting.DocIndex
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.io.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 帮助页 ViewModel（小步骤 3.1）：HelpPage.java 的 Compose 化承接。
 *
 * - 文档索引拉取沿用遗留 NetworkUtils.doGet + JsonUtils（IO 线程，对齐 Task.supplyAsync）；
 * - 三态加载（loading / 成功 / 失败重试）对齐 HelpPage.setLoading；
 * - 分类过滤 isVisible、默认选中第 0 项、文章按语言过滤重建列表，
 *   对齐 DocCategoryAdapter / HelpPage.showArticles（:84-102）；
 * - 链接点击发一次性事件，由 HelpScreenHost 用 Activity 上下文 openLink。
 */
class HelpViewModel(
    private val application: Application,
) : FCLViewModel<HelpUiState, HelpEvent>(HelpUiState()) {

    init {
        refresh()
    }

    /** 拉取文档索引（对齐 HelpPage.refresh :78-91）。 */
    fun refresh() {
        updateState { copy(loading = true, success = false) }
        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    val res = NetworkUtils.doGet(NetworkUtils.toURL(DOC_INDEX_URL))
                    JsonUtils.GSON.fromJson<ArrayList<DocIndex>?>(
                        res,
                        object : TypeToken<ArrayList<DocIndex>>() {}.type,
                    )
                }
            }
            result.onSuccess { indexes ->
                val categories = (indexes ?: arrayListOf()).filter { it.isVisible }
                updateState {
                    copy(
                        loading = false,
                        success = true,
                        categories = categories,
                        selectedCategory = null,
                        articles = emptyList(),
                    )
                }
                // 对齐 DocCategoryAdapter 构造：默认选中第 0 项
                selectCategory(categories.firstOrNull())
            }.onFailure {
                updateState { copy(loading = false, success = false) }
            }
        }
    }

    /** 分类选中：属性监听 → 按语言过滤重建文章列表（对齐 :84-90, 93-102）。 */
    fun selectCategory(category: DocIndex?) {
        updateState {
            copy(
                selectedCategory = category,
                articles = category?.item?.filter { it.isVisible(application) } ?: emptyList(),
            )
        }
    }

    /** 文章点击：打开文档网页（URL 拼 ?path=，对齐 ArticleAdapter :57-59）。 */
    fun onArticleClick(item: DocIndex.Item) {
        sendEvent(HelpEvent.OpenLink("$DOC_PAGE_URL?path=${item.path}"))
    }

    /** 文档站按钮（对齐 :114-116）。 */
    fun onWebsiteClick() {
        sendEvent(HelpEvent.OpenLink(DOC_PAGE_URL))
    }

    companion object {
        const val DOC_INDEX_URL = "https://raw.githubusercontent.com/FCL-Team/FCL-Docs/main/index.json"
        const val DOC_PAGE_URL = "https://fcl-team.github.io/pages/documentation.html"
    }
}

/** 帮助页 UI 状态。 */
data class HelpUiState(
    val loading: Boolean = true,
    val success: Boolean = false,
    val categories: List<DocIndex> = emptyList(),
    val selectedCategory: DocIndex? = null,
    val articles: List<DocIndex.Item> = emptyList(),
)

/** 帮助页一次性事件。 */
sealed interface HelpEvent {
    data class OpenLink(val url: String) : HelpEvent
}
