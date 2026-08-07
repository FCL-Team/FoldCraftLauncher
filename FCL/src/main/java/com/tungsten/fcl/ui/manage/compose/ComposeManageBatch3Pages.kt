package com.tungsten.fcl.ui.manage.compose

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.steveice10.opennbt.tag.builtin.ByteTag
import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import com.github.steveice10.opennbt.tag.builtin.DoubleTag
import com.github.steveice10.opennbt.tag.builtin.FloatTag
import com.github.steveice10.opennbt.tag.builtin.IntArrayTag
import com.github.steveice10.opennbt.tag.builtin.IntTag
import com.github.steveice10.opennbt.tag.builtin.ListTag
import com.github.steveice10.opennbt.tag.builtin.LongTag
import com.github.steveice10.opennbt.tag.builtin.StringTag
import com.github.steveice10.opennbt.tag.builtin.Tag
import com.mio.util.showErrorDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.InstallerItem
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.FCLDropdownField
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.fclSwitchColors
import com.tungsten.fcl.ui.download.compose.ComposeInstallerListPage
import com.tungsten.fcl.ui.download.compose.ComposeTempPage
import com.tungsten.fcl.ui.download.version.InstallFailureAlert.alertFailureMessage
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.ui.manage.ModpackTypeSelectionPage
import com.tungsten.fcl.ui.manage.WorldListItem
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.event.Event
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.game.World
import com.tungsten.fclcore.mod.Datapack
import com.tungsten.fclcore.mod.ModAdviser
import com.tungsten.fclcore.mod.ModpackExportInfo
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackExportTask
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackManifest
import com.tungsten.fclcore.mod.multimc.MultiMCModpackExportTask
import com.tungsten.fclcore.mod.server.ServerModpackExportTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.Lang
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.platform.OperatingSystem
import com.tungsten.fclcore.util.versioning.VersionNumber
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.text.DecimalFormat
import java.time.Instant
import java.util.Collections
import java.util.Optional
import java.util.logging.Level
import kotlin.io.path.pathString

/**
 * 批3管理域 Compose 页面（世界列表/世界信息/数据包/管理安装器/整合包导出），
 * 逐页对齐旧 View 实现的行为与文案；旧页面类与 XML 全部保留作回滚。
 */
class ComposeWorldListPage(context: Context, id: Int, parent: FCLUILayout) :
    FCLCommonPage(context, id, parent, R.layout.page_compose_container), VersionLoadable {
    private var profile: Profile? = null
    private var version: String? = null
    private var composeInstalled = false
    private val reloadTick = mutableIntStateOf(0)

    override fun onStart() {
        if (!composeInstalled) {
            composeInstalled = true
            val container = findViewById<FrameLayout>(R.id.compose_container)
            container.addView(LegacyBridge.createComposeView(context) { Content() }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        super.onStart()
    }

    override fun loadVersion(profile: Profile, version: String?) {
        this.profile = profile
        this.version = version
        reloadTick.intValue++
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

    @Composable
    private fun Content() {
        val scope = rememberCoroutineScope()
        val p = profile
        val v = version
        val tick = reloadTick.intValue
        var worlds by remember { mutableStateOf<List<World>>(emptyList()) }
        var loading by remember { mutableStateOf(false) }
        var showAll by rememberSaveable { mutableStateOf(false) }
        var gameVersion by remember { mutableStateOf<String?>(null) }
        val savesDir = remember(p, v) { p?.repository?.getRunDirectory(v)?.toPath()?.resolve("saves") }

        // 对齐旧 refresh()：后台读 gameVersion + 扫 saves；失败保留当前列表（旧实现失败仅清空字段不刷新界面）
        LaunchedEffect(p, v, tick) {
            if (p == null || v == null || savesDir == null) return@LaunchedEffect
            loading = true
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    p.repository.getGameVersion(v).orElse(null) to World.getWorlds(savesDir)
                }.getOrNull()
            }
            loading = false
            if (result != null) {
                gameVersion = result.first
                worlds = result.second
            }
        }

        // 对齐旧 fixPrivate 可见性：存档在私有目录/Android data 且非空时显示
        val privateDir = savesDir?.let {
            it.pathString.startsWith(FCLPath.PRIVATE_COMMON_DIR) ||
                it.pathString.contains("/Android/data/${context.applicationInfo.packageName}/")
        } ?: false
        val displayed = worlds.filter { showAll || it.gameVersion == null || it.gameVersion == gameVersion }

        Row(Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
            // 左栏（对齐 page_manage_world.xml guideline 0.3：复选框置顶，按钮组置底）
            Column(Modifier.weight(3f).fillMaxHeight()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        state = if (showAll) ToggleableState.On else ToggleableState.Off,
                        onClick = { showAll = !showAll },
                    )
                    Text(stringResource(R.string.world_show_all), color = MiuixTheme.colorScheme.onPrimary)
                }
                Spacer(Modifier.weight(1f))
                if (privateDir && worlds.isNotEmpty()) {
                    Button(
                        onClick = {
                            // 对齐旧 fixPrivate：递归 chmod 1535
                            Files.walk(savesDir).forEach { path -> Files.setAttribute(path, "unix:mode", 1535) }
                            Toast.makeText(context, R.string.message_success, Toast.LENGTH_LONG).show()
                        },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    ) { Text(stringResource(R.string.world_permission_fix)) }
                }
                Button(
                    onClick = { addWorld(scope, savesDir) { reloadTick.intValue++ } },
                    enabled = !loading && savesDir != null,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                ) { Text(stringResource(R.string.world_add)) }
                Button(
                    onClick = { reloadTick.intValue++ },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                ) { Text(stringResource(R.string.action_refresh)) }
            }
            // 右栏（对齐 recycler_view + progress）
            Box(Modifier.weight(7f).fillMaxHeight().padding(start = 10.dp)) {
                if (loading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(displayed) { world ->
                            WorldListComposeItem(context, parent, world, onChanged = { reloadTick.intValue++ })
                        }
                    }
                }
            }
        }
    }

    /** 对齐旧 add()：选 .zip，doc Uri 先拷入缓存。 */
    private fun addWorld(scope: CoroutineScope, savesDir: Path?, onAdded: () -> Unit) {
        if (savesDir == null) return
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, arrayListOf(".zip"), false) { files ->
            var path = files?.get(0) ?: return@launchSingleSelection
            val uri = path.toUri()
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(getActivity(), uri, File(FCLPath.CACHE_DIR))
            }
            installWorld(scope, savesDir, File(path), onAdded)
        }
    }

    /** 对齐旧 installWorld()：解析进度弹窗 → 输入新世界名 → 安装，错误分类提示。 */
    private fun installWorld(scope: CoroutineScope, savesDir: Path, zipFile: File, onAdded: () -> Unit) {
        val installDialog = MiuixTaskDialog(context)
        installDialog.setTitle(context.getString(R.string.world_add))
        installDialog.setCancelAction(null)
        installDialog.show()
        scope.launch {
            val world = withContext(Dispatchers.IO) {
                runCatching { World(zipFile.toPath()) }.getOrElse {
                    Logging.LOG.log(Level.WARNING, "Unable to parse world file $zipFile", it)
                    null
                }
            }
            installDialog.dismiss()
            if (world == null) {
                FCLDialogs.showAlert(context, null, context.getString(R.string.world_import_invalid), cancelable = false)
                return@launch
            }
            showWorldNameDialog(world.worldName) { name ->
                if (name == null) return@showWorldNameDialog
                scope.launch {
                    val error = withContext(Dispatchers.IO) {
                        try {
                            world.install(savesDir, name)
                            null
                        } catch (e: Throwable) {
                            when (e) {
                                is FileAlreadyExistsException -> AndroidUtils.getLocalizedText(
                                    context, "world_import_failed", context.getString(R.string.world_import_already_exists)
                                )
                                is IOException if e.cause is InvalidPathException ->
                                    AndroidUtils.getLocalizedText(context, context.getString(R.string.install_new_game_malformed))
                                else -> AndroidUtils.getLocalizedText(context, e.javaClass.name + ": " + e.localizedMessage)
                            }
                        }
                    }
                    if (error != null) {
                        showErrorDialog(context, error)
                    } else {
                        runCatching { World(savesDir.resolve(name)) }
                            .onFailure { Logging.LOG.log(Level.WARNING, "Unable to load installed world", it) }
                        onAdded()
                    }
                }
            }
        }
    }

    /** 对齐旧 EditDialog：预填世界名，确定回调输入，取消/返回回调 null。 */
    private fun showWorldNameDialog(initial: String, callback: (String?) -> Unit) {
        val dialog = FCLComposeDialog(context)
        val nameState = mutableStateOf(initial)
        dialog.setOnCancelListener { callback(null) }
        dialog.setDialogContent {
            FCLDialogCard(
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = {
                            dialog.dismiss()
                            callback(nameState.value)
                        },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = {
                            dialog.dismiss()
                            callback(null)
                        },
                    ),
                ),
            ) {
                FCLTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        }
        dialog.show()
    }
}

/** 对齐 item_world.xml：标题/描述 + 数据包/导出/删除三个图标按钮，整卡点击进世界信息页。 */
@Composable
private fun WorldListComposeItem(context: Context, parent: FCLUILayout, world: World, onChanged: () -> Unit) {
    val activity = context as? Activity ?: return
    val item = remember(world) { WorldListItem(context, activity, parent, world) }
    val title by item.titleFlow().collectAsState()
    val subtitle by item.subtitleFlow().collectAsState()
    val contentColor = MiuixTheme.colorScheme.onPrimary
    FCLCard(
        onClick = { openWorldInfo(context, parent, world) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = title ?: world.worldName,
                    fontSize = 14.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle ?: "",
                    fontSize = 12.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_baseline_settings_24),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.clickable { openDatapacks(context, parent, world) },
            )
            Icon(
                painter = painterResource(R.drawable.ic_baseline_output_24),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.padding(start = 8.dp).clickable { item.export() },
            )
            Icon(
                painter = painterResource(R.drawable.ic_baseline_delete_24),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.padding(start = 8.dp).clickable {
                    // 对齐旧 WorldListItem.delete()：确认后 forceDelete 并刷新列表；
                    // 旧实现删除后强转 WorldListPage 刷新，Compose 页改由 onChanged 回调刷新
                    FCLDialogs.showAlert(
                        context,
                        null,
                        context.getString(R.string.version_manage_remove_confirm, world.worldName),
                        negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onResult = { ok ->
                            if (ok) {
                                try {
                                    FileUtils.forceDelete(world.file.toFile())
                                } catch (e: Exception) {
                                    // 与旧实现一致静默忽略
                                }
                                onChanged()
                            }
                        },
                    )
                },
            )
        }
    }
}

private fun openWorldInfo(context: Context, parent: FCLUILayout, world: World) {
    ManagePageManager.instance?.showTempPage(ComposeWorldInfoPage(context, PageManager.PAGE_ID_TEMP, parent, world))
}

private fun openDatapacks(context: Context, parent: FCLUILayout, world: World) {
    val gameVersion = world.gameVersion
    // 对齐旧 WorldListItem.manageDatapacks()：<1.13 提示不支持
    if (gameVersion == null ||
        (VersionNumber.isIntVersionNumber(gameVersion) &&
            VersionNumber.asVersion(gameVersion).compareTo(VersionNumber.asVersion("1.13")) < 0)
    ) {
        FCLDialogs.showAlert(
            context,
            null,
            context.getString(R.string.world_datapack_1_13),
            positiveText = context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
            cancelable = false,
        )
        return
    }
    ManagePageManager.instance?.showTempPage(ComposeDatapackListPage(context, PageManager.PAGE_ID_TEMP, parent, world.file))
}

/**
 * 世界信息页（对齐 WorldInfoPage + page_manage_world_info.xml）：
 * 全部编辑即时写回 level.dat（saveLevelDat 语义与旧页一致：LOG.info + IOException 仅告警）。
 */
class ComposeWorldInfoPage(context: Context, id: Int, parent: FCLUILayout, private val world: World) : ComposeTempPage(context, id, parent) {

    private val levelDatState = mutableStateOf<CompoundTag?>(null)

    private fun saveLevelDat() {
        val levelDat = levelDatState.value ?: return
        Logging.LOG.info("Saving level.dat of world " + world.worldName)
        try {
            world.writeLevelDat(levelDat)
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Failed to save level.dat of world " + world.worldName, e)
        }
    }

    @Composable
    override fun Content() {
        val levelDat = levelDatState.value
        LaunchedEffect(Unit) {
            levelDatState.value = withContext(Dispatchers.IO) { runCatching { world.readLevelDat() }.getOrNull() }
        }
        if (levelDat == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return
        }
        val dataTag = levelDat.get<CompoundTag>("Data") ?: return
        val worldGenSettings = dataTag.get<CompoundTag>("WorldGenSettings")

        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            InfoRow(R.string.world_name, world.worldName)
            InfoRow(R.string.world_info_game_version, world.gameVersion ?: "")
            // 对齐旧页：新格式 seed 在 WorldGenSettings，旧格式在 RandomSeed
            when (val seedTag = worldGenSettings?.get<Tag>("seed") ?: dataTag.get<Tag>("RandomSeed")) {
                is LongTag -> InfoRow(R.string.world_info_random_seed, seedTag.value.toString())
            }
            InfoRow(R.string.world_info_last_played, LocaleUtils.formatDateTime(context, Instant.ofEpochMilli(world.lastPlayed)))
            when (val timeTag = dataTag.get<Tag>("Time")) {
                is LongTag -> InfoRow(
                    R.string.world_info_time,
                    AndroidUtils.getLocalizedText(context, "world_info_time_format", timeTag.value / 24000),
                )
            }

            // 允许作弊：tag 缺失或取值非 0/1 时旧页禁用开关
            val cheatTag = dataTag.get<Tag>("allowCommands")
            if (cheatTag is ByteTag && (cheatTag.value == 0.toByte() || cheatTag.value == 1.toByte())) {
                SwitchRow(R.string.world_info_allow_cheats, cheatTag.value == 1.toByte()) { on ->
                    cheatTag.value = (if (on) 1 else 0).toByte()
                    saveLevelDat()
                }
            } else {
                SwitchRow(R.string.world_info_allow_cheats, false, onChange = null)
            }

            // 生成结构：新格式 generate_features，旧格式 MapFeatures
            val genTag = worldGenSettings?.get<Tag>("generate_features") ?: dataTag.get<Tag>("MapFeatures")
            if (genTag is ByteTag && (genTag.value == 0.toByte() || genTag.value == 1.toByte())) {
                SwitchRow(R.string.world_info_generate_features, genTag.value == 1.toByte()) { on ->
                    genTag.value = (if (on) 1 else 0).toByte()
                    saveLevelDat()
                }
            } else {
                SwitchRow(R.string.world_info_generate_features, false, onChange = null)
            }

            // 难度
            val difficultyTag = dataTag.get<Tag>("Difficulty")
            val difficultyNames = remember {
                listOf("peaceful", "easy", "normal", "hard").map {
                    AndroidUtils.getLocalizedText(FCLPath.CONTEXT, "world_info_difficulty_$it")
                }
            }
            if (difficultyTag is ByteTag && difficultyTag.value >= 0 && difficultyTag.value <= 3) {
                var difficultyIndex by remember { mutableIntStateOf(difficultyTag.value.toInt()) }
                FCLDropdownField(
                    label = stringResource(R.string.world_info_difficulty),
                    items = difficultyNames,
                    selectedIndex = difficultyIndex,
                    onSelectedIndexChange = { index ->
                        difficultyIndex = index
                        difficultyTag.value = index.toByte()
                        saveLevelDat()
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                )
            } else {
                FCLDropdownField(
                    label = stringResource(R.string.world_info_difficulty),
                    items = difficultyNames,
                    selectedIndex = 0,
                    onSelectedIndexChange = {},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    enabled = false,
                )
            }

            // 玩家信息（无 Player 标签时旧页整区隐藏）
            val playerTag = dataTag.get<Tag>("Player")
            if (playerTag is CompoundTag) {
                WorldDimension.of(playerTag.get<Tag>("Dimension"))?.formatPosition(playerTag.get<Tag>("Pos"))?.let {
                    InfoRow(R.string.world_info_player_location, it)
                }
                (playerTag.get("LastDeathLocation") as? CompoundTag)?.let { lastDeathTag ->
                    WorldDimension.of(lastDeathTag.get<Tag>("dimension"))?.formatPosition(lastDeathTag.get<Tag>("pos"))?.let {
                        InfoRow(R.string.world_info_player_last_death_location, it)
                    }
                }
                WorldDimension.of(playerTag.get("SpawnDimension"))?.let { spawnDim ->
                    val x = playerTag.get<Tag>("SpawnX")
                    val y = playerTag.get<Tag>("SpawnY")
                    val z = playerTag.get<Tag>("SpawnZ")
                    if (x is IntTag && y is IntTag && z is IntTag) {
                        InfoRow(R.string.world_info_player_spawn, spawnDim.formatPosition(x.value, y.value, z.value))
                    }
                }

                val gameTypeNames = remember {
                    listOf("survival", "creative", "adventure", "spectator").map {
                        AndroidUtils.getLocalizedText(FCLPath.CONTEXT, "world_info_player_game_type_$it")
                    }
                }
                val gameTypeTag = playerTag.get<Tag>("playerGameType")
                if (gameTypeTag is IntTag && gameTypeTag.value >= 0 && gameTypeTag.value <= 3) {
                    var gameTypeIndex by remember { mutableIntStateOf(gameTypeTag.value) }
                    FCLDropdownField(
                        label = stringResource(R.string.world_info_player_game_type),
                        items = gameTypeNames,
                        selectedIndex = gameTypeIndex,
                        onSelectedIndexChange = { index ->
                            gameTypeIndex = index
                            gameTypeTag.value = index
                            saveLevelDat()
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    )
                } else {
                    FCLDropdownField(
                        label = stringResource(R.string.world_info_player_game_type),
                        items = gameTypeNames,
                        selectedIndex = 0,
                        onSelectedIndexChange = {},
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        enabled = false,
                    )
                }

                // 生命值/饱食度/经验等级：对齐旧页输入即校验并即时保存
                val healthTag = playerTag.get<Tag>("Health")
                NumberFieldRow(
                    labelRes = R.string.world_info_player_health,
                    initial = if (healthTag is FloatTag) DecimalFormat("#").format(healthTag.value.toFloat()) else "",
                    enabled = healthTag is FloatTag,
                ) { text ->
                    if (healthTag !is FloatTag) return@NumberFieldRow
                    if (StringUtils.isBlank(text) && Lang.toDoubleOrNull(text) == null) {
                        Toast.makeText(context, context.getString(R.string.input_number), Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            healthTag.value = text.toFloat()
                            saveLevelDat()
                        } catch (e: Throwable) {
                            // 与旧实现一致忽略解析失败
                        }
                    }
                }
                val foodTag = playerTag.get<Tag>("foodLevel")
                NumberFieldRow(
                    labelRes = R.string.world_info_player_food_level,
                    initial = if (foodTag is IntTag) foodTag.value.toString() else "",
                    enabled = foodTag is IntTag,
                ) { text ->
                    if (foodTag !is IntTag) return@NumberFieldRow
                    if (StringUtils.isBlank(text) && Lang.toDoubleOrNull(text) == null) {
                        Toast.makeText(context, context.getString(R.string.input_number), Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            foodTag.value = text.toInt()
                            saveLevelDat()
                        } catch (e: Throwable) {
                        }
                    }
                }
                val xpTag = playerTag.get<Tag>("XpLevel")
                NumberFieldRow(
                    labelRes = R.string.world_info_player_xp_level,
                    initial = if (xpTag is IntTag) xpTag.value.toString() else "",
                    enabled = xpTag is IntTag,
                ) { text ->
                    if (xpTag !is IntTag) return@NumberFieldRow
                    if (StringUtils.isBlank(text) && Lang.toDoubleOrNull(text) == null) {
                        Toast.makeText(context, context.getString(R.string.input_number), Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            xpTag.value = text.toInt()
                            saveLevelDat()
                        } catch (e: Throwable) {
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun InfoRow(labelRes: Int, value: String) {
        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(
                text = stringResource(labelRes),
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
            )
            Text(text = value, color = MiuixTheme.colorScheme.onPrimary)
        }
    }

    /** onChange = null 表示旧页的禁用态（开关可见但不可切换）。 */
    @Composable
    private fun SwitchRow(labelRes: Int, checked: Boolean, onChange: ((Boolean) -> Unit)?) {
        // NBT tag 变更不触发重组，开关视觉由本地状态承载
        var current by remember(checked) { mutableStateOf(checked) }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(labelRes),
                color = MiuixTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f),
            )
            Switch(checked = current, onCheckedChange = { on -> current = on; onChange?.invoke(on) }, colors = fclSwitchColors())
        }
    }

    @Composable
    private fun NumberFieldRow(labelRes: Int, initial: String, enabled: Boolean, onChange: (String) -> Unit) {
        var text by remember { mutableStateOf(initial) }
        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(
                text = stringResource(labelRes),
                color = MiuixTheme.colorScheme.onPrimary,
            )
            FCLTextField(
                value = text,
                onValueChange = {
                    text = it
                    onChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = enabled,
            )
        }
    }
}

/** 对齐 WorldInfoPage.Dimension：主世界名称为 null（坐标不带维度前缀）。 */
private class WorldDimension private constructor(private val name: String?) {
    companion object {
        private val OVERWORLD = WorldDimension(null)

        private fun theNether() = WorldDimension(FCLPath.CONTEXT.getString(R.string.world_info_dimension_the_nether))
        private fun theEnd() = WorldDimension(FCLPath.CONTEXT.getString(R.string.world_info_dimension_the_end))

        fun of(tag: Tag?): WorldDimension? = when (tag) {
            is IntTag -> when (tag.value) {
                0 -> OVERWORLD
                1 -> theNether()
                2 -> theEnd()
                else -> null
            }
            is StringTag -> when (tag.value) {
                "overworld", "minecraft:overworld" -> OVERWORLD
                "the_nether", "minecraft:the_nether" -> theNether()
                "the_end", "minecraft:the_end" -> theEnd()
                else -> WorldDimension(tag.value)
            }
            else -> null
        }
    }

    fun formatPosition(tag: Tag?): String? = when (tag) {
        is ListTag -> {
            if (tag.size() != 3) {
                null
            } else {
                val x: Tag = tag.get(0)
                val y: Tag = tag.get(1)
                val z: Tag = tag.get(2)
                if (x is DoubleTag && y is DoubleTag && z is DoubleTag) {
                    formatPosition(x.value, y.value, z.value)
                } else {
                    null
                }
            }
        }
        is IntArrayTag -> formatPosition(tag.getValue(0), tag.getValue(1), tag.getValue(2))
        else -> null
    }

    fun formatPosition(x: Int, y: Int, z: Int): String =
        if (this === OVERWORLD) String.format("(%d, %d, %d)", x, y, z)
        else String.format("%s (%d, %d, %d)", name, x, y, z)

    fun formatPosition(x: Double, y: Double, z: Double): String =
        if (this === OVERWORLD) String.format("(%.2f, %.2f, %.2f)", x, y, z)
        else String.format("%s (%.2f, %.2f, %.2f)", name, x, y, z)
}

/**
 * 数据包页（对齐 DatapackListPage + page_datapack_list.xml + item_datapack.xml）：
 * 行点击切换选中（选中=primary，未选=primaryContainer），勾选框与 pack.active 双向同步。
 */
class ComposeDatapackListPage(context: Context, id: Int, parent: FCLUILayout, private val worldDir: Path) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val datapack = remember(worldDir) { Datapack(worldDir.resolve("datapacks")) }
        var packs by remember { mutableStateOf<List<Datapack.Pack>>(emptyList()) }
        var selected by remember { mutableStateOf<Set<String>>(emptySet()) }
        var loading by remember { mutableStateOf(false) }

        // 对齐旧 refresh()：清空选择，后台 loadFromDir
        fun refresh() {
            scope.launch {
                loading = true
                selected = emptySet()
                withContext(Dispatchers.IO) { datapack.loadFromDir() }
                packs = datapack.info
                loading = false
            }
        }
        LaunchedEffect(Unit) { refresh() }

        // 对齐旧 removeSelected：逐个删除，失败仅告警；旧页经 infoFlow 刷新，此处显式重扫
        fun removeSelected() {
            packs.filter { selected.contains(it.id) }.forEach { pack ->
                try {
                    datapack.deletePack(pack)
                } catch (e: IOException) {
                    Logging.LOG.warning("Failed to delete datapack $pack")
                }
            }
            refresh()
        }

        fun import() {
            MainActivity.getInstance().fileLauncher.launchMultiSelection(null, arrayListOf(".zip")) { files ->
                if (files == null) return@launchMultiSelection
                // 对齐旧 add()：安装期间不可取消进度弹窗
                val installDialog = MiuixTaskDialog(context)
                installDialog.setTitle(context.getString(R.string.datapack_add))
                installDialog.setCancelAction(null)
                installDialog.show()
                scope.launch {
                    withContext(Dispatchers.IO) {
                        files.map(::File).forEach { file ->
                            try {
                                val zip = Datapack(file.toPath())
                                zip.loadFromZip()
                                zip.installTo(worldDir)
                            } catch (e: IOException) {
                                Logging.LOG.log(Level.WARNING, "Unable to parse datapack file $file", e)
                            }
                        }
                    }
                    installDialog.dismiss()
                    refresh()
                }
            }
        }

        Row(Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)) {
            // 左栏按钮组（对齐 page_datapack_list.xml left）
            Column(Modifier.weight(3f)) {
                Button(
                    onClick = {
                        // 对齐旧删除确认弹窗
                        FCLDialogs.showAlert(
                            context,
                            null,
                            context.getString(R.string.button_remove_confirm),
                            positiveText = context.getString(R.string.button_remove),
                            negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                            onResult = { if (it) removeSelected() },
                            cancelable = false,
                        )
                    },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(stringResource(R.string.button_remove)) }
                Button(
                    onClick = { packs.filter { selected.contains(it.id) }.forEach { it.setActive(true) } },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                ) { Text(stringResource(R.string.mods_enable)) }
                Button(
                    onClick = { packs.filter { selected.contains(it.id) }.forEach { it.setActive(false) } },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                ) { Text(stringResource(R.string.mods_disable)) }
                Button(
                    onClick = { import() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                ) { Text(stringResource(R.string.datapack_add)) }
                Button(
                    onClick = { refresh() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                ) { Text(stringResource(R.string.action_refresh)) }
            }
            Box(Modifier.weight(7f).fillMaxHeight().padding(start = 10.dp)) {
                if (loading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(packs) { pack ->
                            DatapackRow(
                                pack = pack,
                                selected = selected.contains(pack.id),
                                onToggleSelect = {
                                    selected = if (selected.contains(pack.id)) selected - pack.id else selected + pack.id
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

/** 对齐 item_datapack.xml + DatapackListAdapter：勾选框双向绑定 pack.active。 */
@Composable
private fun DatapackRow(pack: Datapack.Pack, selected: Boolean, onToggleSelect: () -> Unit) {
    val active by pack.activeFlow().collectAsState()
    val contentColor = MiuixTheme.colorScheme.onPrimary
    FCLCard(
        onClick = onToggleSelect,
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
        colors = CardDefaults.defaultColors(
            color = if (selected) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.primaryContainer
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                state = if (active) ToggleableState.On else ToggleableState.Off,
                onClick = { pack.setActive(!active) },
                modifier = Modifier.size(30.dp),
            )
            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = pack.id,
                    fontSize = 14.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = StringUtils.parseColorEscapes(pack.description.toString()),
                    fontSize = 12.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * 管理安装器页（对齐 manage/InstallerListPage + page_manage_auto_install + view_installer_item）：
 * InstallerItemGroup 状态模型与安装/移除/离线安装任务链完整复刻旧页。
 */
class ComposeManageInstallerListPage(context: Context, id: Int, parent: FCLUILayout) :
    FCLCommonPage(context, id, parent, R.layout.page_compose_container), VersionLoadable {

    private var profile: Profile? = null
    private var versionId: String? = null
    private var version: Version? = null
    private var composeInstalled = false
    private val reloadTick = mutableIntStateOf(0)

    override fun onStart() {
        if (!composeInstalled) {
            composeInstalled = true
            val container = findViewById<FrameLayout>(R.id.compose_container)
            container.addView(LegacyBridge.createComposeView(context) { Content() }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        super.onStart()
    }

    override fun loadVersion(profile: Profile, version: String?) {
        this.profile = profile
        this.versionId = version
        this.version = profile.repository.getVersion(versionId)
        reloadTick.intValue++
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

    @Composable
    private fun Content() {
        val p = profile
        val v = versionId
        val tick = reloadTick.intValue
        var items by remember { mutableStateOf<List<InstallerItem>>(emptyList()) }

        // 对齐旧 loadVersion：后台分析 LibraryAnalyzer，再在 UI 线程装配 InstallerItem 状态
        LaunchedEffect(p, v, tick) {
            if (p == null || v == null) return@LaunchedEffect
            val analyzed = withContext(Dispatchers.IO) {
                runCatching {
                    val gameVersion = p.repository.getGameVersion(version).orElse(null)
                    gameVersion to LibraryAnalyzer.analyze(p.repository.getResolvedPreservingPatchesVersion(v), gameVersion)
                }.getOrNull()
            } ?: return@LaunchedEffect
            val gameVersion = analyzed.first
            val analyzer = analyzed.second
            val group = InstallerItem.InstallerItemGroup(context, gameVersion)
            items = group.libraries.mapNotNull { installerItem ->
                val libraryId = installerItem.libraryId
                val libraryVersion = analyzer.getVersion(libraryId).orElse(null)
                val libraryConfigurable =
                    libraryVersion != null && analyzer.getLibraryStatus(libraryId) == LibraryAnalyzer.LibraryMark.LibraryStatus.CLEAR

                // 对齐旧页：跳过 fabric-api / quilt-api
                if (libraryId.contains("fabric-api") || libraryId.contains("quilt-api")) {
                    return@mapNotNull null
                }

                installerItem.libraryVersion.value = libraryVersion
                installerItem.upgradable.value = libraryConfigurable
                installerItem.installable.value = true
                installerItem.action.value = Runnable {
                    val page = ComposeInstallerListPage(context, PageManager.PAGE_ID_TEMP, parent, gameVersion, libraryId) { remoteVersion ->
                        if (libraryVersion == null) {
                            finish(p, remoteVersion)
                        } else {
                            // 对齐旧页：已安装时先弹换版确认
                            FCLDialogs.showAlert(
                                context,
                                context.getString(R.string.install_change_version),
                                AndroidUtils.getLocalizedText(
                                    context,
                                    "install_change_version_confirm",
                                    AndroidUtils.getLocalizedText(context, "install_installer_$libraryId"),
                                    libraryVersion,
                                    remoteVersion.selfVersion,
                                ),
                                negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                                onResult = { if (it) finish(p, remoteVersion) },
                                cancelable = false,
                            )
                        }
                        null
                    }
                    ManagePageManager.instance?.showTempPage(page)
                }
                val removable = !LibraryAnalyzer.LibraryType.MINECRAFT.getPatchId().equals(libraryId) && libraryConfigurable
                installerItem.removable.value = removable
                if (removable) {
                    installerItem.removeAction.value = Runnable {
                        val ver = version
                        if (ver != null) {
                            p.dependency.removeLibraryAsync(ver, libraryId)
                                .thenComposeAsync(p.repository.saveAsync(ver))
                                .withComposeAsync(p.repository.refreshVersionsAsync())
                                .withRunAsync<Exception>(Schedulers.androidUIThread()) {
                                    reloadTick.intValue++
                                    p.repository.onVersionIconChanged.fireEvent(Event(this))
                                }
                                .start()
                        }
                    }
                }
                installerItem
            }
        }

        Column(Modifier.fillMaxSize().padding(10.dp)) {
            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                itemsIndexed(items) { index, installerItem ->
                    // 对齐旧 addView：第二项起顶部 10dp 间距
                    InstallerItemRow(
                        context = context,
                        item = installerItem,
                        modifier = if (index > 0) Modifier.padding(top = 10.dp) else Modifier,
                    )
                }
            }
            Button(
                onClick = { installOffline() },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            ) { Text(stringResource(R.string.install_installer_install_offline)) }
        }
    }

    /** 对齐旧 installOffline()：选 .jar，doc Uri 拷缓存后安装。 */
    private fun installOffline() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, arrayListOf(".jar"), false) { files ->
            var path = files?.get(0) ?: return@launchSingleSelection
            val uri = Uri.parse(path)
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(getActivity(), uri, File(FCLPath.CACHE_DIR))
            }
            val file = File(path)
            if (file.exists()) {
                doInstallOffline(file)
            }
        }
    }

    /** 对齐旧 doInstallOffline()：任务链 + MiuixTaskDialog，成功/失败提示后重载。 */
    private fun doInstallOffline(file: File) {
        val p = profile ?: return
        val v = version ?: return
        val task = p.dependency.installLibraryAsync(v, file.toPath())
            .thenComposeAsync(p.repository.saveAsync(v))
            .thenComposeAsync(p.repository.refreshVersionsAsync())
        task.setName(context.getString(R.string.install_installer_install_offline))
        val executor = task.executor(object : TaskListener() {
            override fun onStop(success: Boolean, executor: TaskExecutor) {
                Schedulers.androidUIThread().execute {
                    if (success) {
                        reloadTick.intValue++
                        FCLDialogs.showAlert(
                            context,
                            null,
                            context.getString(R.string.install_success),
                            onResult = { p.repository.onVersionIconChanged.fireEvent(Event(this)) },
                            cancelable = false,
                        )
                    } else {
                        if (executor.exception == null) return@execute
                        alertFailureMessage(context, executor.exception) {}
                    }
                    reloadTick.intValue++
                }
            }
        })
        val dialog = MiuixTaskDialog(context)
        dialog.setTitle(context.getString(R.string.install_installer_install_offline))
        dialog.setExecutor(executor)
        dialog.show()
        executor.start()
    }

    /** 对齐旧 finish()：选中远程版本后安装；先移除库但不保存，失败不会破坏当前版本。 */
    private fun finish(profile: Profile, remoteVersion: RemoteVersion) {
        val v = version ?: return
        // 对齐旧 finish() 任务链：installLibraryAsync → saveAsync → refreshVersionsAsync
        val stages = ArrayList<String>()
        stages.add(String.format("fcl.install.%s:%s", remoteVersion.libraryId, remoteVersion.selfVersion))

        val task = profile.getDependency(DownloadProviders.getDownloadProvider()).installLibraryAsync(v, remoteVersion)
            .thenComposeAsync(profile.repository.saveAsync(v))
            .thenComposeAsync(profile.repository.refreshVersionsAsync())
            .withStagesHint(stages)

        Schedulers.androidUIThread().execute {
            val executor = task.executor(object : TaskListener() {
                override fun onStop(success: Boolean, executor: TaskExecutor) {
                    Schedulers.androidUIThread().execute {
                        if (success) {
                            FCLDialogs.showAlert(
                                context,
                                null,
                                context.getString(R.string.install_success),
                                onResult = {
                                    ManagePageManager.instance?.dismissCurrentTempPage()
                                    profile.repository.onVersionIconChanged.fireEvent(Event(this))
                                },
                                cancelable = false,
                            )
                        } else {
                            if (executor.exception == null) return@execute
                            alertFailureMessage(context, executor.exception) {}
                        }
                        reloadTick.intValue++
                    }
                }
            })
            val pane = MiuixTaskDialog(context)
            pane.setTitle(context.getString(R.string.install_change_version))
            pane.setExecutor(executor)
            pane.show()
            executor.start()
        }
    }
}

/** 对齐 view_installer_item.xml + InstallerItemSkin：图标/名称/状态文本 + 移除/选择图标按钮。 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun InstallerItemRow(context: Context, item: InstallerItem, modifier: Modifier = Modifier) {
    val libraryVersion by item.libraryVersion.collectAsState()
    val incompatibleLibraryName by item.incompatibleLibraryName.collectAsState()
    val incompatibleWithGame by item.incompatibleWithGame.collectAsState()
    val removable by item.removable.collectAsState()
    val installable by item.installable.collectAsState()
    val upgradable by item.upgradable.collectAsState()
    val action by item.action.collectAsState()
    val removeAction by item.removeAction.collectAsState()

    // 对齐 InstallerItemSkin.computeStateText()
    val stateText = when {
        incompatibleWithGame -> AndroidUtils.getLocalizedText(context, "install_installer_change_version", libraryVersion)
        incompatibleLibraryName != null -> AndroidUtils.getLocalizedText(
            context,
            "install_installer_incompatible",
            AndroidUtils.getLocalizedText(context, "install_installer_" + incompatibleLibraryName!!.replace("-", "_")),
        )
        libraryVersion == null -> context.getString(R.string.install_installer_not_installed)
        else -> libraryVersion!!
    }
    // 对齐 computeSelectVisibility()
    val selectVisible = installable && incompatibleLibraryName == null
    val contentColor = MiuixTheme.colorScheme.onPrimary

    FCLCard(
        onClick = { if (selectVisible) action?.run() },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                model = item.icon,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stateText,
                    fontSize = 12.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.weight(1f))
            if (removable) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_close_24),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.padding(start = 10.dp).clickable { removeAction?.run() },
                )
            }
            if (selectVisible) {
                // 对齐 computeSelectImage()：可升级显示更新图标，否则前进箭头
                Icon(
                    painter = painterResource(
                        if (upgradable) R.drawable.ic_baseline_update_24 else R.drawable.ic_baseline_arrow_forward_24
                    ),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.padding(start = 10.dp).clickable { action?.run() },
                )
            }
        }
    }
}

/** Compose 导出类型选择页，保留旧页的文案、顺序和三种导出类型。 */
class ComposeModpackTypeSelectionPage(context: Context, id: Int, parent: FCLUILayout, private val profile: Profile, private val version: String) : ComposeTempPage(context, id, parent) {
    @Composable override fun Content() {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(10.dp)) {
            FCLCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Text(stringResource(R.string.modpack_export_as), modifier = Modifier.padding(10.dp), color = MiuixTheme.colorScheme.onPrimary)
            }
            TypeButton(R.string.modpack_type_mcbbs, R.string.modpack_type_mcbbs_export, ModpackTypeSelectionPage.MODPACK_TYPE_MCBBS, McbbsModpackExportTask.OPTION)
            TypeButton(R.string.modpack_type_multimc, R.string.modpack_type_multimc_export, ModpackTypeSelectionPage.MODPACK_TYPE_MULTIMC, MultiMCModpackExportTask.OPTION)
            TypeButton(R.string.modpack_type_server, R.string.modpack_type_server_export, ModpackTypeSelectionPage.MODPACK_TYPE_SERVER, ServerModpackExportTask.OPTION)
        }
    }

    @Composable private fun TypeButton(title: Int, subtitle: Int, type: String, options: ModpackExportInfo.Options) {
        FCLCard(
            onClick = { ManagePageManager.instance?.showTempPage(ComposeModpackInfoPage(context, PageManager.PAGE_ID_TEMP, parent, profile, version, type, options)) },
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            Column(Modifier.padding(10.dp)) {
                Text(stringResource(title), color = MiuixTheme.colorScheme.onPrimary)
                Text(stringResource(subtitle), color = MiuixTheme.colorScheme.onPrimary, style = MiuixTheme.textStyles.body2)
            }
        }
    }
}

/** 导出信息页：按 ModpackExportInfo.Options 显示旧页要求的字段，并保持原校验与默认值。 */
class ComposeModpackInfoPage(context: Context, id: Int, parent: FCLUILayout, private val profile: Profile, private val version: String, private val type: String, private val options: ModpackExportInfo.Options) : ComposeTempPage(context, id, parent) {
    @Composable override fun Content() {
        var name by rememberSaveable { mutableStateOf(version) }
        var author by rememberSaveable { mutableStateOf(Optional.ofNullable(Accounts.getSelectedAccount()).map(Account::getUsername).orElse("")) }
        var packVersion by rememberSaveable { mutableStateOf("1.0") }
        var description by rememberSaveable { mutableStateOf("") }
        var fileApi by rememberSaveable { mutableStateOf("") }
        var launchArguments by rememberSaveable { mutableStateOf(profile.getVersionSetting(version).minecraftArgs ?: "") }
        var javaArguments by rememberSaveable { mutableStateOf(profile.getVersionSetting(version).javaArgs ?: "") }
        var url by rememberSaveable { mutableStateOf("") }
        var originId by rememberSaveable { mutableStateOf("") }
        var minMemory by rememberSaveable { mutableStateOf(profile.getVersionSetting(version).minMemory ?: 0) }
        var forceUpdate by rememberSaveable { mutableStateOf(false) }
        var authlibIndex by rememberSaveable { mutableStateOf(0) }
        var outputPath by rememberSaveable { mutableStateOf("") }
        var fileName by rememberSaveable { mutableStateOf(version) }
        val serverNames = listOf("") + ConfigHolder.config().authlibInjectorServers.map { it.name }
        val serverUrls = listOf<String?>(null) + ConfigHolder.config().authlibInjectorServers.map { it.url }

        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(10.dp)) {
            FCLCard(colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(10.dp)) {
                    Text(stringResource(R.string.modpack_wizard_step_initialization_exported_version), color = MiuixTheme.colorScheme.onPrimary)
                    Text(version, color = MiuixTheme.colorScheme.onPrimary)
                    ExportField(R.string.modpack_name, name) { name = it }
                    ExportField(R.string.archive_author, author) { author = it }
                    ExportField(R.string.archive_version, packVersion) { packVersion = it }
                    if (options.isRequireFileApi) ExportField(R.string.modpack_file_api, fileApi) { fileApi = it }
                    if (options.isRequireLaunchArguments) ExportField(R.string.settings_advanced_minecraft_arguments, launchArguments) { launchArguments = it }
                    if (options.isRequireJavaArguments) ExportField(R.string.settings_advanced_jvm_args, javaArguments) { javaArguments = it }
                    if (options.isRequireUrl) ExportField(R.string.modpack_origin_url, url) { url = it }
                    if (options.isRequireOrigins) ExportField(R.string.modpack_origin_mcbbs, originId) { originId = it }
                    if (options.isRequireMinMemory) ExportField(R.string.settings_memory_lower_bound, minMemory.toString()) { minMemory = it.toIntOrNull() ?: minMemory }
                    ExportField(R.string.modpack_desc, description) { description = it }
                    if (options.isRequireAuthlibInjectorServer) FCLDropdownField(stringResource(R.string.account_injector_server), serverNames, authlibIndex, { authlibIndex = it })
                    if (options.isRequireForceUpdate) FCLDropdownField(stringResource(R.string.modpack_wizard_step_initialization_force_update), listOf("false", "true"), if (forceUpdate) 1 else 0, { forceUpdate = it == 1 })
                    ExportField(R.string.modpack_wizard_step_initialization_save, outputPath) { outputPath = it }
                    ExportField(R.string.archive_name, fileName) { fileName = it }
                }
            }
            Button(onClick = { MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true) { files -> if (!files.isNullOrEmpty()) outputPath = files[0] } }, modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) { Text(stringResource(R.string.modpack_wizard_step_initialization_save)) }
            Button(onClick = { submit(name, author, packVersion, fileApi, launchArguments, javaArguments, url, originId, minMemory, forceUpdate, serverUrls.getOrNull(authlibIndex), description, outputPath, fileName) }, modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) { Text(stringResource(R.string.button_next)) }
        }
    }

    @Composable private fun ExportField(label: Int, value: String, onChange: (String) -> Unit) {
        Spacer(Modifier.height(8.dp))
        Text(stringResource(label), color = MiuixTheme.colorScheme.onPrimary)
        FCLTextField(value, onChange, modifier = Modifier.fillMaxWidth(), singleLine = label != R.string.modpack_desc)
    }

    private fun submit(name: String, author: String, packVersion: String, fileApi: String, launchArgs: String, javaArgs: String, url: String, originId: String, minMemory: Int, forceUpdate: Boolean, authlib: String?, description: String, outputPath: String, fileName: String) {
        val urlValid = if (fileApi.isBlank()) false else runCatching { URL(fileApi).toURI() }.isSuccess
        when {
            name.isBlank() || author.isBlank() || packVersion.isBlank() || fileName.isBlank() || (options.isRequireFileApi && options.isValidateFileApi && fileApi.isBlank()) -> Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
            options.isRequireFileApi && fileApi.isNotBlank() && !urlValid -> Toast.makeText(context, context.getString(R.string.input_url), Toast.LENGTH_SHORT).show()
            options.isRequireOrigins && originId.isNotBlank() && Lang.toIntOrNull(originId) == null -> Toast.makeText(context, context.getString(R.string.input_number), Toast.LENGTH_SHORT).show()
            !OperatingSystem.isNameValid(fileName) -> Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
            outputPath.isBlank() -> MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true) { files -> if (!files.isNullOrEmpty()) submit(name, author, packVersion, fileApi, launchArgs, javaArgs, url, originId, minMemory, forceUpdate, authlib, description, files[0], fileName) }
            File(outputPath, "$fileName.zip").exists() -> Toast.makeText(context, context.getString(R.string.message_file_exist), Toast.LENGTH_SHORT).show()
            else -> {
                val info = ModpackExportInfo().setName(name).setAuthor(author).setVersion(packVersion).setDescription(description).setPackWithLauncher(false).setFileApi(fileApi).setLaunchArguments(launchArgs).setJavaArguments(javaArgs).setUrl(url).setMinMemory(minMemory).setForceUpdate(forceUpdate).setAuthlibInjectorServer(authlib)
                if (originId.isNotBlank()) info.setOrigins(Collections.singletonList(McbbsModpackManifest.Origin("mcbbs", originId.toInt())))
                ManagePageManager.instance?.showTempPage(ComposeModpackFileSelectionPage(context, PageManager.PAGE_ID_TEMP, parent, profile, version, type, ModAdviser::suggestMod, info, File(outputPath, "$fileName.zip")))
            }
        }
    }
}
