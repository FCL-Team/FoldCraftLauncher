package com.tungsten.fcl.ui.manage

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.util.showErrorDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageManageWorldBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.game.World
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.ui.FCLPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.logging.Level
import java.util.stream.Collectors
import kotlin.coroutines.resume
import kotlin.io.path.pathString

class WorldListPage(context: Context?, id: Int, resId: Int) : FCLPage(context, id, resId), VersionLoadable, View.OnClickListener {
    private val itemsProperty: ListProperty<WorldListItem> =
        SimpleListProperty(FXCollections.observableArrayList())

    private val showAll: BooleanProperty = SimpleBooleanProperty(this, "showAll", false)

    private lateinit var savesDir: Path
    private var worlds: MutableList<World?> = mutableListOf()
    private var profile: Profile? = null
    private var id: String? = null
    private var gameVersion: String? = null
    private lateinit var binding: PageManageWorldBinding

    init {
        create()
    }

    fun create() {
        binding = PageManageWorldBinding.bind(contentView)

        showAll.addListener { _: Observable? ->
            itemsProperty.setAll(
                worlds.stream()
                    .filter { world: World? -> isShowAll() || world!!.gameVersion == null || world.gameVersion == gameVersion }
                    .map { it: World? ->
                        WorldListItem(
                            context,
                            activity,
                            it
                        )
                    }.collect(
                        Collectors.toList()
                    )
            )
        }

        binding.showAll.addCheckedChangeListener()
        binding.showAll.checkProperty().bindBidirectional(showAll)
        binding.add.setOnClickListener(this)
        binding.refresh.setOnClickListener(this)
        binding.fixPrivate.setOnClickListener(this)

        val adapter = WorldListAdapter(context)
        adapter.listProperty().bind(itemsProperty)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(context))
        binding.recyclerView.setAdapter(adapter)
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }

    override fun loadVersion(profile: Profile, version: String?) {
        this.profile = profile
        this.id = version
        this.savesDir = profile.repository.getRunDirectory(id).toPath().resolve("saves")
        refresh()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.add -> add()
            binding.refresh -> refresh()
            binding.fixPrivate -> {
                // use 关闭 Files.walk 的目录流，避免文件描述符泄漏
                Files.walk(savesDir).use { stream ->
                    stream.forEach { path ->
                        Files.setAttribute(
                            path,
                            "unix:mode",
                            1535
                        )
                    }
                }
                Toast.makeText(context, R.string.message_success, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun refresh() {
        if (profile == null || id == null) return
        setLoading(true)
        MainActivity.getInstance().lifecycleScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    gameVersion = profile!!.repository.getGameVersion(id).orElse(null)
                    World.getWorlds(
                        savesDir
                    )
                }
            }.getOrElse {
                worlds.clear()
                return@launch
            }
            setLoading(false)
            worlds.addAll(result)
            itemsProperty.setAll(
                result.stream()
                    .filter { isShowAll() || it.gameVersion == null || it.gameVersion == gameVersion }
                    .map {
                        WorldListItem(
                            context,
                            activity,
                            it
                        )
                    }.collect(
                        Collectors.toList()
                    )
            )
            if (savesDir.pathString.startsWith(FCLPath.PRIVATE_COMMON_DIR) or savesDir.pathString.contains(
                    "/Android/data/${context.applicationInfo.packageName}/"
                )
            ) {
                if (worlds.isNotEmpty())
                    binding.fixPrivate.visibility = View.VISIBLE
            } else {
                binding.fixPrivate.visibility = View.GONE
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (loading) View.GONE else View.VISIBLE
        binding.showAll.setEnabled(!loading)
        binding.add.setEnabled(!loading)
        binding.refresh.setEnabled(!loading)
    }

    fun add() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, listOf(".zip")) {
            var path = it?.get(0) ?: return@launchSingleSelection
            val uri = path.toUri()
            if (AndroidUtils.isDocUri(uri)) {
                path =
                    AndroidUtils.copyFileToDir(activity, uri, File(FCLPath.CACHE_DIR))
            }
            val file = File(path)
            installWorld(file)
        }
    }

    private fun installWorld(zipFile: File) {
        // Only accept one world file because user is required to confirm the new world name
        // Or too many input dialogs are popped.
        val builder = FCLAlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
        builder.setMessage(context.getString(R.string.world_add))
        val installDialog = builder.create()
        installDialog.show()
        MainActivity.getInstance().lifecycleScope.launch(Dispatchers.Main) {
            val world = runCatching {
                withContext(Dispatchers.IO) {
                    World(zipFile.toPath())
                }
            }.getOrElse {
                installDialog.dismiss()
                Logging.LOG.log(Level.WARNING, "Unable to parse world file $zipFile", it)
                val builder1 = FCLAlertDialog.Builder(context)
                builder1.setCancelable(false)
                builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                builder1.setMessage(context.getString(R.string.world_import_invalid))
                builder1.setNegativeButton(
                    context.getString(com.tungsten.fcl.R.string.dialog_positive),
                    null
                )
                builder1.create().show()
                return@launch
            }
            installDialog.dismiss()
            val name = showEditDialog(world.worldName) ?: return@launch
            runCatching {
                withContext(Dispatchers.IO) {
                    world.install(savesDir, name)
                }
            }.onFailure {
                val error = when (it) {
                    is FileAlreadyExistsException -> AndroidUtils.getLocalizedText(
                        context,
                        "world_import_failed",
                        context.getString(R.string.world_import_already_exists)
                    )

                    is IOException if it.cause is InvalidPathException -> AndroidUtils.getLocalizedText(
                        context,
                        context.getString(R.string.install_new_game_malformed)
                    )

                    else -> AndroidUtils.getLocalizedText(
                        context,
                        it.javaClass.getName() + ": " + it.localizedMessage
                    )
                }
                showErrorDialog(context, error)
            }.onSuccess {
                itemsProperty.add(
                    WorldListItem(
                        context,
                        activity,
                        World(savesDir.resolve(name))
                    )
                )
            }
        }
    }

    private suspend fun showEditDialog(worldName: String): String? =
        suspendCancellableCoroutine {
            val dialog = EditDialog(context, worldName) { name ->
                it.resume(name)
            }
            dialog.onCancelListener = {
                it.resume(null)
            }
            dialog.show()
            it.invokeOnCancellation {
                dialog.dismiss()
            }
        }

    fun isShowAll(): Boolean {
        return showAll.get()
    }
}
