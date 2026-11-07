package com.tungsten.fcl.ui.download

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import com.mio.data.ModsChanged
import com.mio.util.openLink
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageQuestVivecraftBinding
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.vr.QuestVivecraftService
import com.tungsten.fcl.vr.QuestVivecraftTarget
import com.tungsten.fcl.vr.VrWarnings
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.ui.FCLPage
import java.io.File
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import java.util.logging.Level

/**
 * Quest Vivecraft 下载临时页：列出 QuestCraft 上游适配当前实例的 release，
 * 校验通过后安装到实例 mods 目录。
 */
class QuestVivecraftDownloadPage(context: Context, target: QuestVivecraftTarget) :
    FCLPage(context, PAGE_ID_TEMP, R.layout.page_quest_vivecraft) {

    // 本页不覆写 onCreate：属性初始化器在超类构造（含 inflate）完成后执行，可直接 bind contentView
    private val target: QuestVivecraftTarget = target
    private val service = QuestVivecraftService()
    private val releases = ArrayList<QuestVivecraftService.ReleaseAsset>()
    private val installed = ArrayList<QuestVivecraftService.InstalledMod>()
    private val adapter = ReleaseAdapter()
    private val binding: PageQuestVivecraftBinding = PageQuestVivecraftBinding.bind(contentView)
    private var cancellation: QuestVivecraftService.Cancellation? = null
    private var busy = false
    private var detached = false

    init {
        binding.qvTarget.text = context.getString(R.string.qv_download_target, target.instanceId,
            target.minecraftVersion.ifEmpty { "?" },
            target.loader.name + " " + target.loaderVersion,
            File(target.gameDirectory, "mods").absolutePath)
        binding.qvList.adapter = adapter
        binding.qvRefresh.setOnClickListener { load() }
        binding.qvCancel.setOnClickListener {
            cancellation?.cancel()
            binding.qvCancel.isEnabled = false
            binding.qvStatus.setText(R.string.qv_download_cancelling)
        }
        binding.qvWebsite.setOnClickListener { openLink(context, VrWarnings.VIVECRAFT_DOWNLOAD_URL) }
        binding.qvBack.setOnClickListener { UIManager.instance.downloadUI.dismissCurrentTempPage() }
        contentView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                detached = false
            }

            override fun onViewDetachedFromWindow(v: View) {
                detached = true
                cancellation?.cancel()
            }
        })
        load()
    }

    private fun setBusy(value: Boolean) {
        busy = value
        binding.qvRefresh.isEnabled = !value
        binding.qvCancel.isEnabled = true
        binding.qvCancel.visibility = if (value) View.VISIBLE else View.GONE
        binding.qvProgress.visibility = if (value) View.VISIBLE else View.GONE
        adapter.notifyDataSetChanged()
    }

    private fun load() {
        if (busy) return
        if (target.loader == QuestVivecraftTarget.Loader.NONE || target.minecraftVersion.isEmpty()
            || target.loaderVersion.isEmpty()
        ) {
            binding.qvStatus.setText(R.string.qv_download_unsupported)
            return
        }
        val operation = QuestVivecraftService.Cancellation()
        cancellation = operation
        setBusy(true)
        binding.qvProgress.isIndeterminate = true
        binding.qvStatus.setText(R.string.qv_download_loading)
        CompletableFuture.supplyAsync {
            try {
                Listing(service.scanInstalled(target, operation), service.fetchReleases(target, operation))
            } catch (error: Exception) {
                throw CompletionException(error)
            }
        }.whenCompleteAsync({ listing, error ->
            if (activity.isDestroyed) return@whenCompleteAsync
            setBusy(false)
            if (error != null) {
                showError(error)
                return@whenCompleteAsync
            }
            installed.clear()
            installed.addAll(listing.mods)
            releases.clear()
            releases.addAll(listing.assets)
            showListingStatus()
            adapter.notifyDataSetChanged()
        }, Schedulers.androidUIThread())
    }

    private fun showListingStatus() {
        if (installed.isNotEmpty()) {
            val names = installed.joinToString(", ") { it.file.name }
            binding.qvStatus.text = context.getString(
                if (installed.size == 1 && installed[0].compatible) R.string.qv_download_installed
                else R.string.qv_download_conflict, names)
        } else {
            binding.qvStatus.setText(
                if (releases.isEmpty()) R.string.qv_download_empty else R.string.qv_download_choose)
        }
    }

    private fun confirmInstall(asset: QuestVivecraftService.ReleaseAsset) {
        if (busy) return
        FCLAlertDialog.Builder(context)
            .setTitle(context.getString(R.string.qv_download_install_title))
            .setMessage(context.getString(R.string.qv_download_install_prompt,
                asset.fileName, target.instanceId, File(target.gameDirectory, "mods").absolutePath))
            .setPositiveButton(context.getString(R.string.button_download)) { install(asset) }
            .setNegativeButton(context.getString(R.string.button_cancel), null)
            .create().show()
    }

    private fun install(asset: QuestVivecraftService.ReleaseAsset) {
        if (busy || detached) return
        val operation = QuestVivecraftService.Cancellation()
        cancellation = operation
        setBusy(true)
        binding.qvProgress.isIndeterminate = true
        binding.qvStatus.setText(R.string.qv_download_downloading)
        val cache = File(context.cacheDir, "quest-vivecraft")
        CompletableFuture.supplyAsync {
            try {
                var lastUpdate = 0L
                var previous: QuestVivecraftService.Phase? = null
                // 进度经 UI 线程节流：同阶段 200ms 一帧，阶段切换与完成时立即刷新
                val result = service.install(target, asset, cache, operation) { phase, completed, total ->
                    val now = SystemClock.uptimeMillis()
                    if (phase != previous || completed == total || now - lastUpdate >= 200) {
                        previous = phase
                        lastUpdate = now
                        Schedulers.androidUIThread().execute {
                            if (!detached && busy && operation === cancellation) {
                                showProgress(phase, completed, total)
                            }
                        }
                    }
                }
                if (!result.alreadyInstalled) ModsChanged.notifyChanged()
                result
            } catch (error: Exception) {
                throw CompletionException(error)
            }
        }.whenCompleteAsync({ result, error ->
            if (activity.isDestroyed) return@whenCompleteAsync
            setBusy(false)
            if (error != null) {
                showError(error)
                return@whenCompleteAsync
            }
            installed.clear()
            installed.add(result.mod)
            binding.qvStatus.text = context.getString(
                if (result.alreadyInstalled) R.string.qv_download_already else R.string.qv_download_success,
                result.mod.file.name)
            adapter.notifyDataSetChanged()
        }, Schedulers.androidUIThread())
    }

    private fun showProgress(phase: QuestVivecraftService.Phase, done: Long, total: Long) {
        if (phase == QuestVivecraftService.Phase.DOWNLOADING) {
            binding.qvProgress.isIndeterminate = total <= 0
            if (total > 0) binding.qvProgress.progress = (done * 1000 / total).coerceAtMost(1000).toInt()
            binding.qvStatus.text = context.getString(R.string.qv_download_progress,
                size(done), if (total > 0) size(total) else "?")
        } else {
            binding.qvProgress.isIndeterminate = true
            binding.qvStatus.setText(
                if (phase == QuestVivecraftService.Phase.VERIFYING) R.string.qv_download_verifying
                else R.string.qv_download_installing)
        }
    }

    private fun showError(error: Throwable) {
        var cause = error
        while (cause is CompletionException && cause.cause != null) cause = cause.cause!!
        if (cause is CancellationException) {
            binding.qvStatus.setText(R.string.qv_download_cancelled)
            return
        }
        Logging.LOG.log(Level.WARNING, "Quest Vivecraft download for " + target.instanceId, cause)
        val message = when ((cause as? QuestVivecraftService.ServiceException)?.error) {
            QuestVivecraftService.Error.UNSUPPORTED -> R.string.qv_download_unsupported
            QuestVivecraftService.Error.INVALID_ASSET,
            QuestVivecraftService.Error.INCOMPATIBLE -> R.string.qv_download_incompatible
            QuestVivecraftService.Error.CONFLICT -> R.string.qv_download_conflict_error
            QuestVivecraftService.Error.DESTINATION -> R.string.qv_download_destination_error
            QuestVivecraftService.Error.INTEGRITY -> R.string.qv_download_integrity_error
            else -> R.string.qv_download_network
        }
        binding.qvStatus.setText(message)
    }

    private fun size(bytes: Long): String =
        String.format(Locale.getDefault(), "%.1f MiB", bytes / 1048576.0)

    private class Listing(
        val mods: List<QuestVivecraftService.InstalledMod>,
        val assets: List<QuestVivecraftService.ReleaseAsset>,
    )

    private inner class ReleaseAdapter : BaseAdapter() {
        override fun getCount(): Int = releases.size

        override fun getItem(position: Int): QuestVivecraftService.ReleaseAsset = releases[position]

        override fun getItemId(position: Int): Long = releases[position].id

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val row = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_mod_version, parent, false).apply {
                    layoutParams = AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
            val asset = releases[position]
            row.findViewById<TextView>(R.id.name).text = "${asset.releaseName} · ${asset.tag}"
            val present = installed.any { mod ->
                asset.sha256.isNotEmpty() && asset.sha256.equals(mod.sha256, ignoreCase = true)
            }
            row.findViewById<TextView>(R.id.tag).text = context.getString(when {
                present -> R.string.qv_download_row_installed
                asset.prerelease -> R.string.qv_download_prerelease
                else -> R.string.qv_download_release
            })
            row.findViewById<TextView>(R.id.date).text = asset.fileName + "  ·  " + size(asset.size) +
                    "  ·  " + asset.publishedAt.replace('T', ' ').replace("Z", " UTC")
            val button = row.findViewById<View>(R.id.parent)
            button.isEnabled = !busy
            button.alpha = if (busy) 0.55f else 1f
            button.setOnClickListener { confirmInstall(asset) }
            return row
        }
    }

    companion object {
        /** 打开下载临时页（切到下载区模组页后压入临时页栈） */
        @JvmStatic
        fun open(target: QuestVivecraftTarget) {
            val activity = MainActivity.getInstance()
            if (activity.isFinishing || activity.isDestroyed) return
            activity.refreshMenuView(null)
            activity.binding.download.isSelected = true
            val ui = UIManager.instance.downloadUI
            UIManager.instance.switchUI(ui)
            ui.showDownloadPage(DownloadUI.PAGE_ID_DOWNLOAD_MOD)
            ui.showTempPage(QuestVivecraftDownloadPage(activity, target))
        }
    }
}
