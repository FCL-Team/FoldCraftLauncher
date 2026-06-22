package com.tungsten.fcl.ui.manage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.PageManageWorldBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.game.World
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.FutureCallback
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.function.ExceptionalConsumer
import com.tungsten.fclcore.util.function.ExceptionalRunnable
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.browser.options.SelectionMode
import com.tungsten.fcllibrary.component.ResultListener
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function
import java.util.logging.Level
import java.util.stream.Collectors

class WorldListPage(context: Context?, id: Int, parent: FCLUILayout?, resId: Int) :
    FCLCommonPage(context, id, parent, resId), VersionLoadable, View.OnClickListener {
    private val itemsProperty: ListProperty<WorldListItem?> =
        SimpleListProperty<WorldListItem?>(FXCollections.observableArrayList<WorldListItem?>())

    private val showAll: BooleanProperty = SimpleBooleanProperty(this, "showAll", false)

    private var savesDir: Path? = null
    private var worlds: MutableList<World?>? = null
    private var profile: Profile? = null
    private var id: String? = null
    private var gameVersion: String? = null
    private var binding: PageManageWorldBinding? = null

    init {
        create()
    }

    fun create() {
        binding = PageManageWorldBinding.bind(getContentView())

        showAll.addListener(InvalidationListener { e: Observable? ->
            if (worlds != null) itemsProperty.setAll(
                worlds!!.stream()
                    .filter { world: World? -> isShowAll() || world!!.getGameVersion() == null || world.getGameVersion() == gameVersion }
                    .map<WorldListItem?> { it: World? ->
                        WorldListItem(
                            getContext(),
                            getActivity(),
                            getParent(),
                            it
                        )
                    }.collect(
                        Collectors.toList()
                    )
            )
        })

        binding!!.showAll.addCheckedChangeListener()
        binding!!.showAll.checkProperty().bindBidirectional(showAll)
        binding!!.add.setOnClickListener(this)
        binding!!.refresh.setOnClickListener(this)

        val adapter = WorldListAdapter(getContext())
        adapter.listProperty().bind(itemsProperty)
        binding!!.recyclerView.setLayoutManager(LinearLayoutManager(getContext()))
        binding!!.recyclerView.setAdapter(adapter)
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }

    override fun loadVersion(profile: Profile, version: String?) {
        this.profile = profile
        this.id = version
        this.savesDir = profile.getRepository().getRunDirectory(id).toPath().resolve("saves")
        refresh()
    }

    override fun onClick(v: View?) {
        if (v === binding!!.add) {
            add()
        }
        if (v === binding!!.refresh) {
            refresh()
        }
    }

    fun refresh(): CompletableFuture<*>? {
        if (profile == null || id == null) return CompletableFuture.completedFuture<Any?>(null)

        setLoading(true)
        return CompletableFuture
            .runAsync(Runnable {
                gameVersion = profile!!.getRepository().getGameVersion(id).orElse(null)
            })
            .thenApplyAsync<MutableList<World?>?>(Function { unused: Void? ->
                World.getWorlds(
                    savesDir
                )
            })
            .whenCompleteAsync(BiConsumer { result: MutableList<World?>?, exception: Throwable? ->
                worlds = result
                setLoading(false)
                if (exception == null) {
                    itemsProperty.setAll(
                        result!!.stream()
                            .filter { world: World? -> isShowAll() || world!!.getGameVersion() == null || world.getGameVersion() == gameVersion }
                            .map<WorldListItem?> { it: World? ->
                                WorldListItem(
                                    getContext(),
                                    getActivity(),
                                    getParent(),
                                    it
                                )
                            }.collect(
                                Collectors.toList()
                            )
                    )
                }
            }, Schedulers.androidUIThread())
    }

    private fun setLoading(loading: Boolean) {
        binding!!.progress.setVisibility(if (loading) View.VISIBLE else View.GONE)
        binding!!.recyclerView.setVisibility(if (loading) View.GONE else View.VISIBLE)
        binding!!.showAll.setEnabled(!loading)
        binding!!.add.setEnabled(!loading)
        binding!!.refresh.setEnabled(!loading)
    }

    fun add() {
        val builder = FileBrowser.Builder(getContext())
        builder.setLibMode(LibMode.FILE_CHOOSER)
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION)
        val suffix = ArrayList<String?>()
        suffix.add(".zip")
        builder.setSuffix(suffix)
        builder.create().browse(
            getActivity(),
            RequestCodes.SELECT_WORLD_CODE,
            (ResultListener.Listener { requestCode: Int, resultCode: Int, data: Intent? ->
                if (requestCode == RequestCodes.SELECT_WORLD_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    var path = FileBrowser.getSelectedFiles(data).get(0)
                    val uri = Uri.parse(path)
                    if (AndroidUtils.isDocUri(uri)) {
                        path =
                            AndroidUtils.copyFileToDir(getActivity(), uri, File(FCLPath.CACHE_DIR))
                    }
                    val file = File(path)
                    installWorld(file)
                }
            })
        )
    }

    private fun installWorld(zipFile: File) {
        // Only accept one world file because user is required to confirm the new world name
        // Or too many input dialogs are popped.
        val builder = FCLAlertDialog.Builder(getContext())
        builder.setCancelable(false)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
        builder.setMessage(getContext().getString(R.string.world_add))
        val installDialog = builder.create()
        installDialog.show()
        Task.supplyAsync<World?>(Callable { World(zipFile.toPath()) })
            .whenComplete<RuntimeException?, RuntimeException?>(
                Schedulers.androidUIThread(),
                ExceptionalConsumer { world: World? ->
                    installDialog.dismiss()
                    val dialog = WorldNameDialog(
                        getContext(),
                        world!!.getWorldName(),
                        FutureCallback { name: String?, resolve: Runnable?, reject: Consumer<String?>? ->
                            Task.runAsync(ExceptionalRunnable { world.install(savesDir, name) })
                                .whenComplete<IOException?, RuntimeException?>(
                                    Schedulers.androidUIThread(),
                                    ExceptionalRunnable {
                                        itemsProperty.add(
                                            WorldListItem(
                                                getContext(),
                                                getActivity(),
                                                getParent(),
                                                World(savesDir!!.resolve(name))
                                            )
                                        )
                                        resolve!!.run()
                                    },
                                    ExceptionalConsumer { e: Exception? ->
                                        if (e is FileAlreadyExistsException) reject!!.accept(
                                            AndroidUtils.getLocalizedText(
                                                getContext(),
                                                "world_import_failed",
                                                getContext().getString(R.string.world_import_already_exists)
                                            )
                                        )
                                        else if (e is IOException && e.cause is InvalidPathException) reject!!.accept(
                                            AndroidUtils.getLocalizedText(
                                                getContext(),
                                                getContext().getString(R.string.install_new_game_malformed)
                                            )
                                        )
                                        else reject!!.accept(
                                            AndroidUtils.getLocalizedText(
                                                getContext(),
                                                e!!.javaClass.getName() + ": " + e.getLocalizedMessage()
                                            )
                                        )
                                    }).start()
                        })
                    dialog.show()
                },
                ExceptionalConsumer { e: Exception? ->
                    installDialog.dismiss()
                    Logging.LOG.log(Level.WARNING, "Unable to parse world file " + zipFile, e)
                    val builder1 = FCLAlertDialog.Builder(getContext())
                    builder1.setCancelable(false)
                    builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                    builder1.setMessage(getContext().getString(R.string.world_import_invalid))
                    builder1.setNegativeButton(
                        getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                        null
                    )
                    builder1.create().show()
                }).start()
    }

    fun isShowAll(): Boolean {
        return showAll.get()
    }
}
