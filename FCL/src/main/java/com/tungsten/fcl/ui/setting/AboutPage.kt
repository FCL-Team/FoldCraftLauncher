package com.tungsten.fcl.ui.setting

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.util.openLink
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemAboutBinding
import com.tungsten.fcl.databinding.ItemAboutDescBinding
import com.tungsten.fcl.databinding.PageSettingListBinding
import com.tungsten.fcl.util.LegalAgreement
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLPage
import java.io.IOException
import java.util.logging.Level
import kotlin.system.exitProcess

/**
 * 关于页：说明置顶，下方链接行合成一组（组内绘制分割线），
 * 行背景与分割线样式与版本设置页一致。
 */
class AboutPage(context: Context?, id: Int) : FCLPage(context, id, R.layout.page_setting_list) {

    private lateinit var binding: PageSettingListBinding

    init {
        create()
    }

    private fun create() {
        binding = PageSettingListBinding.bind(contentView)
        binding.settingList.layoutManager = LinearLayoutManager(context)
        // 说明行与链接组之间留 8dp 间距，组内行间留 1dp 缝并绘制分割线
        val rowSpacing = (8 * context.resources.displayMetrics.density).toInt()
        val groupDivider = (1 * context.resources.displayMetrics.density).toInt()
        binding.settingList.addItemDecoration(
            SpacingItemDecoration(
                rowSpacing,
                { parent, position ->
                    val adapter = parent.adapter as? AboutAdapter
                    if (adapter?.isNextInSameGroup(position) == true) groupDivider else rowSpacing
                },
                true,
                { ThemeEngine.getInstance().getTheme().getColor() }
            )
        )
        // 主题切换时重绘分割线颜色
        ThemeEngine.getInstance().registerEvent(binding.settingList) {
            binding.settingList.invalidate()
        }
        binding.settingList.adapter = AboutAdapter { openLinkAt(it) }
    }


    private fun openLinkAt(linkIndex: Int) {
        val item = LINKS[linkIndex]
        when {
            item.document != null -> showLegalDocument(item.document)
            item.link == null -> joinQQGroup(QQ_GROUP_KEY)
            else -> openLink(context, item.link)
        }
    }

    /** 查看随安装包分发的用户协议 / 隐私政策；隐私政策提供撤回同意的入口 */
    private fun showLegalDocument(document: LegalAgreement.Document) {
        val ctx = context
        Schedulers.io().execute {
            val text = try {
                LegalAgreement.load(ctx, document)
            } catch (e: IOException) {
                Logging.LOG.log(Level.WARNING, "Failed to load legal document $document", e)
                ctx.getString(R.string.legal_load_failed)
            }
            Schedulers.androidUIThread().execute {
                FCLAlertDialog.Builder(ctx).apply {
                    setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                    setTitle(ctx.getString(document.titleRes))
                    setMessage(text)
                    useAutoLink()
                    setPositiveButton(null)
                    if (document == LegalAgreement.Document.PRIVACY_POLICY) {
                        setNeutralButton(ctx.getString(R.string.legal_withdraw)) { confirmWithdraw() }
                    }
                    create().show()
                }
            }
        }
    }

    /** 撤回同意：清除同意记录后立即退出，下次启动重新走确认页；不删除本机数据 */
    private fun confirmWithdraw() {
        val ctx = context
        FCLAlertDialog.Builder(ctx).apply {
            setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
            setMessage(ctx.getString(R.string.legal_withdraw_confirm))
            setPositiveButton(ctx.getString(R.string.legal_withdraw_positive)) {
                LegalAgreement.withdraw(ctx)
                activity?.finishAffinity()
                exitProcess(0)
            }
            setNegativeButton(null)
            create().show()
        }
    }

    private fun joinQQGroup(key: String) {
        val intent = Intent().apply {
            data = Uri.parse(
                "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key
            )
        }
        try {
            context.startActivity(intent)
        } catch (ignored: Exception) {
        }
    }

    /** 链接行：标题资源与跳转链接（null 表示跳转 QQ 群），document 非空时改为查看协议文档；条目顺序即显示顺序 */
    private data class LinkItem(val titleRes: Int, val link: String?, val document: LegalAgreement.Document? = null)

    private class AboutAdapter(
        private val onLinkClick: (Int) -> Unit
    ) : RecyclerView.Adapter<AboutAdapter.Holder>() {

        override fun getItemCount(): Int = LINKS.size + 1

        override fun getItemViewType(position: Int): Int = if (position == 0) TYPE_DESC else TYPE_LINK

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(parent.context)
            val item = if (viewType == TYPE_DESC) {
                ItemAboutDescBinding.inflate(inflater, parent, false).root
            } else {
                ItemAboutBinding.inflate(inflater, parent, false).root
            }
            return Holder(item)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            // 行背景为圆角形状：链接行合成一组（组首上圆角/组尾下圆角/中间无圆角），
            // 颜色 tint 取主题浅色；组内行间缝隙绘制分割线
            holder.itemView.setBackgroundResource(
                when (position) {
                    0 -> R.drawable.bg_item_rounded
                    1 -> R.drawable.bg_item_rounded_top
                    itemCount - 1 -> R.drawable.bg_item_rounded_bottom
                    else -> R.drawable.bg_item_rounded_middle
                }
            )
            ThemeEngine.getInstance().unregisterEvent(holder.itemView)
            ThemeEngine.getInstance().registerEvent(holder.itemView) {
                holder.itemView.backgroundTintList =
                    ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
            }
            if (position == 0) {
                holder.itemView.setOnClickListener(null)
                ItemAboutDescBinding.bind(holder.itemView).title.setText(R.string.about_desc)
            } else {
                holder.itemView.setOnClickListener {
                    onLinkClick(holder.bindingAdapterPosition - 1)
                }
                ItemAboutBinding.bind(holder.itemView).title.setText(LINKS[position - 1].titleRes)
            }
        }

        /** 供间距装饰器判断：链接行内相邻时行间留 1dp 缝（绘制分割线） */
        fun isNextInSameGroup(position: Int): Boolean =
            position > 0 && position + 1 < itemCount

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    companion object {
        private const val TYPE_DESC = 0
        private const val TYPE_LINK = 1
        private const val QQ_GROUP_KEY = "9_Mnxe5x1l6L7giLuRYQyBh0iWBgCUbw"

        private val LINKS = listOf(
            LinkItem(R.string.about_launcher, "https://fcl-team.github.io/"),
            LinkItem(R.string.about_developer, "https://github.com/FCL-Team"),
            LinkItem(R.string.community_discord, "https://discord.gg/ffhvuXTwyV"),
            LinkItem(R.string.community_qq, null),
            LinkItem(R.string.about_sponsor, "https://afdian.com/@tungs"),
            LinkItem(R.string.about_source, "https://github.com/FCL-Team/FoldCraftLauncher"),
            LinkItem(R.string.legal_eula, null, LegalAgreement.Document.EULA),
            LinkItem(R.string.legal_privacy, null, LegalAgreement.Document.PRIVACY_POLICY)
        )
    }
}