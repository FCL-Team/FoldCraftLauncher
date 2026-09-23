package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fcllibrary.util.LocaleUtils.formatDateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.DoubleTag;
import com.github.steveice10.opennbt.tag.builtin.FloatTag;
import com.github.steveice10.opennbt.tag.builtin.IntArrayTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.tungsten.fcl.R;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.FCLApp;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.game.World;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;

public class WorldInfoPage extends FCLPage {

    private final World world;
    private final CompoundTag levelData;
    private final CompoundTag dataTag;
    private final CompoundTag worldGenSettings;
    private final CompoundTag playerData;

    private FCLTextView name;
    private FCLTextView gameVersion;
    private FCLTextView seed;
    private FCLTextView lastPlayed;
    private FCLTextView time;
    private FCLSwitch allowCheat;
    private FCLSwitch generateStructure;
    private FCLSpinner<Difficulty> difficulty;

    private FCLLinearLayout playerInfo;
    private FCLTextView location;
    private FCLTextView lastDeath;
    private FCLTextView spawn;
    private FCLSpinner<GameType> gameType;
    private FCLEditText health;
    private FCLEditText foodLevel;
    private FCLEditText xpLevel;

    public WorldInfoPage(Context context, int id, World world) throws IOException {
        super(context, id, R.layout.page_manage_world_info);
        this.world = world;
        this.levelData = world.getLevelData();
        this.dataTag = (CompoundTag) levelData.get("Data");
        this.worldGenSettings = world.getNormalizedWorldGenSettingsData();
        this.playerData = world.getPlayerData();

        name.setText(world.getWorldName());
        gameVersion.setText(world.getGameVersion() == null ? "" : world.getGameVersion().toNormalizedString());
        Long seedValue = world.getSeed();
        if (seedValue != null) {
            seed.setText(seedValue.toString());
        }
        lastPlayed.setText(formatDateTime(getContext(), Instant.ofEpochMilli(world.getLastPlayed())));
        Tag timeTag = dataTag.get("Time");
        if (timeTag instanceof LongTag) {
            long days = ((LongTag) timeTag).getValue() / 24000;
            time.setText(getContext().getString(R.string.world_info_time_format, days));
        }
        Tag cheatTag = dataTag.get("allowCommands");
        if (cheatTag instanceof ByteTag) {
            ByteTag byteTag = (ByteTag) cheatTag;
            byte value = byteTag.getValue();
            if (value == 0 || value == 1) {
                allowCheat.setChecked(value == 1);
                allowCheat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    byteTag.setValue(isChecked ? (byte) 1 : (byte) 0);
                    saveWorldData();
                });
            } else {
                allowCheat.setEnabled(false);
            }
        } else {
            allowCheat.setEnabled(false);
        }
        Tag generateFeaturesTag = null;
        if (dataTag.get("MapFeatures") instanceof ByteTag mapFeaturesTag) { // Valid before (1.16)20w20a
            generateFeaturesTag = mapFeaturesTag;
        } else if (worldGenSettings != null) { // Valid after (1.16)20w20a
            // "generate_features" is valid between (1.16)20w20a and 26.1-snapshot-6,
            // "generate_structures" is valid after 26.1-snapshot-6
            generateFeaturesTag = worldGenSettings.get("generate_features");
            if (generateFeaturesTag == null)
                generateFeaturesTag = worldGenSettings.get("generate_structures");
        }
        if (generateFeaturesTag instanceof ByteTag) {
            ByteTag byteTag = (ByteTag) generateFeaturesTag;
            byte value = byteTag.getValue();
            if (value == 0 || value == 1) {
                generateStructure.setChecked(value == 1);
                generateStructure.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    byteTag.setValue(isChecked ? (byte) 1 : (byte) 0);
                    saveWorldData();
                });
            } else {
                generateStructure.setEnabled(false);
            }
        } else {
            generateStructure.setEnabled(false);
        }
        difficulty.setItems(new ArrayList<>(Difficulty.items));
        Difficulty difficultyValue = null;
        Tag difficultyTag = null;
        // Valid before 26.1-snapshot-6
        if (dataTag.get("Difficulty") instanceof ByteTag byteTag
                && (difficultyValue = Difficulty.of(byteTag.getValue())) != null) {
            difficultyTag = byteTag;
        } else if (dataTag.get("difficulty_settings") instanceof CompoundTag difficultySettingTag
                && difficultySettingTag.get("difficulty") instanceof StringTag stringTag
                && (difficultyValue = Difficulty.of(stringTag.getValue())) != null) {
            // Valid after 26.1-snapshot-6
            difficultyTag = stringTag;
        }
        if (difficultyTag != null) {
            this.difficulty.setSelection(difficultyValue.getPosition());
            final Tag selectedDifficultyTag = difficultyTag;
            this.difficulty.setOnItemSelectedListener((index, item) -> {
                if (selectedDifficultyTag instanceof ByteTag byteTag) {
                    byteTag.setValue((byte) item.ordinal());
                } else if (selectedDifficultyTag instanceof StringTag stringTag) {
                    stringTag.setValue(item.getTagStringValue());
                }
                saveWorldData();
            });
        } else {
            this.difficulty.setEnabled(false);
        }

        if (playerData != null) {
            CompoundTag player = playerData;
            playerInfo.setVisibility(View.VISIBLE);

            Dimension dim = Dimension.of(player.get("Dimension"));
            if (dim != null) {
                String posString = dim.formatPosition(player.get("Pos"));
                if (posString != null)
                    location.setText(posString);
            }
            Tag lastDeathTag = player.get("LastDeathLocation");
            if (lastDeathTag instanceof CompoundTag) {
                Dimension lastDeathDim = Dimension.of(((CompoundTag) lastDeathTag).get("dimension"));
                if (lastDeathDim != null) {
                    String posString = lastDeathDim.formatPosition(((CompoundTag) lastDeathTag).get("pos"));
                    if (posString != null)
                        lastDeath.setText(posString);
                }
            }
            // Valid after 25w07a
            String spawnString = null;
            if (player.get("respawn") instanceof CompoundTag respawnTag
                    && respawnTag.get("pos") instanceof IntArrayTag respawnPosTag
                    && respawnPosTag.length() >= 3) {
                Dimension respawnDim = respawnTag.get("dimension") instanceof StringTag dimensionTag
                        ? Dimension.of(dimensionTag)
                        : Dimension.OVERWORLD;
                spawnString = respawnDim.formatPosition(respawnPosTag.getValue(0), respawnPosTag.getValue(1), respawnPosTag.getValue(2));
            } else if (player.get("SpawnX") instanceof IntTag && player.get("SpawnY") instanceof IntTag && player.get("SpawnZ") instanceof IntTag) {
                // Valid before 25w07a
                Dimension spawnDim = player.get("SpawnDimension") instanceof StringTag dimensionTag
                        ? Dimension.of(dimensionTag)
                        // SpawnDimension tag is valid after 20w12a, the game respawned in the Overworld before that
                        : Dimension.OVERWORLD;
                IntTag x = (IntTag) player.get("SpawnX");
                IntTag y = (IntTag) player.get("SpawnY");
                IntTag z = (IntTag) player.get("SpawnZ");
                spawnString = spawnDim.formatPosition(x.getValue(), y.getValue(), z.getValue());
            }
            if (spawnString != null)
                spawn.setText(spawnString);
            gameType.setItems(new ArrayList<>(GameType.items));
            // Valid before 26.1-snapshot-6
            Tag hardcoreTag = dataTag.get("hardcore");
            // Valid after 26.1-snapshot-6
            if (hardcoreTag == null && dataTag.get("difficulty_settings") instanceof CompoundTag difficultySettingsTag) {
                hardcoreTag = difficultySettingsTag.get("hardcore");
            }
            if (player.get("playerGameType") instanceof IntTag intTag && hardcoreTag instanceof ByteTag) {
                ByteTag hardcoreByteTag = (ByteTag) hardcoreTag;
                GameType gameType = GameType.of(intTag.getValue(), hardcoreByteTag.getValue() == 1);
                if (gameType != null) {
                    this.gameType.setSelection(gameType.getPosition());
                    this.gameType.setOnItemSelectedListener((index, item) -> {
                        if (item == GameType.HARDCORE) {
                            intTag.setValue(0); // survival (hardcore worlds are survival + hardcore flag)
                            hardcoreByteTag.setValue((byte) 1);
                        } else {
                            intTag.setValue(item.ordinal());
                            hardcoreByteTag.setValue((byte) 0);
                        }
                        saveWorldData();
                    });
                } else {
                    this.gameType.setEnabled(false);
                }
            } else {
                this.gameType.setEnabled(false);
            }
            Tag healthTag = player.get("Health");
            if (healthTag instanceof FloatTag) {
                FloatTag floatTag = (FloatTag) healthTag;
                health.setText(new DecimalFormat("#").format(floatTag.getValue()));
                health.setStringValue(new DecimalFormat("#").format(floatTag.getValue()));
                health.stringProperty().addListener(observable -> {
                    if (StringUtils.isBlank(health.getStringValue()) && Lang.toDoubleOrNull(health.getStringValue()) == null) {
                        Toast.makeText(getContext(), getContext().getString(R.string.input_number), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            floatTag.setValue(Float.parseFloat(health.getStringValue()));
                            saveWorldData();
                        } catch (Throwable ignored) {
                        }
                    }
                });
            } else {
                health.setEnabled(false);
            }
            Tag foodLevelTag = player.get("foodLevel");
            if (foodLevelTag instanceof IntTag) {
                IntTag intTag = (IntTag) foodLevelTag;
                foodLevel.setText(String.valueOf(intTag.getValue()));
                foodLevel.setStringValue(String.valueOf(intTag.getValue()));
                foodLevel.stringProperty().addListener(observable -> {
                    if (StringUtils.isBlank(foodLevel.getStringValue()) && Lang.toDoubleOrNull(foodLevel.getStringValue()) == null) {
                        Toast.makeText(getContext(), getContext().getString(R.string.input_number), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            intTag.setValue(Integer.parseInt(foodLevel.getStringValue()));
                            saveWorldData();
                        } catch (Throwable ignored) {
                        }
                    }
                });
            } else {
                foodLevel.setEnabled(false);
            }
            Tag xpLevelTag = player.get("XpLevel");
            if (xpLevelTag instanceof IntTag) {
                IntTag intTag = (IntTag) xpLevelTag;
                xpLevel.setText(String.valueOf(intTag.getValue()));
                xpLevel.setStringValue(String.valueOf(intTag.getValue()));
                xpLevel.stringProperty().addListener(observable -> {
                    if (StringUtils.isBlank(xpLevel.getStringValue()) && Lang.toDoubleOrNull(xpLevel.getStringValue()) == null) {
                        Toast.makeText(getContext(), getContext().getString(R.string.input_number), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            intTag.setValue(Integer.parseInt(xpLevel.getStringValue()));
                            saveWorldData();
                        } catch (Throwable ignored) {
                        }
                    }
                });
            } else {
                xpLevel.setEnabled(false);
            }
        } else {
            playerInfo.setVisibility(View.GONE);
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();

        name = findViewById(R.id.name);
        gameVersion = findViewById(R.id.game_version);
        seed = findViewById(R.id.seed);
        lastPlayed = findViewById(R.id.last_played);
        time = findViewById(R.id.time);
        allowCheat = findViewById(R.id.allow_cheat);
        generateStructure = findViewById(R.id.generate_structures);
        difficulty = findViewById(R.id.difficulty);

        playerInfo = findViewById(R.id.player_info);
        location = findViewById(R.id.location);
        lastDeath = findViewById(R.id.last_death);
        spawn = findViewById(R.id.spawn);
        gameType = findViewById(R.id.game_mode);
        health = findViewById(R.id.health);
        foodLevel = findViewById(R.id.food_level);
        xpLevel = findViewById(R.id.xp_level);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    private void saveWorldData() {
        LOG.info("Saving data of world " + world.getWorldName());
        try {
            this.world.writeWorldData();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to save world data of " + world.getWorldName(), e);
        }
    }

    private static final class Dimension {
        static final Dimension OVERWORLD = new Dimension(null);
        static final Dimension THE_NETHER = new Dimension(FCLApp.getAppContext().getString(R.string.world_info_dimension_the_nether));
        static final Dimension THE_END = new Dimension(FCLApp.getAppContext().getString(R.string.world_info_dimension_the_end));

        final String name;

        static Dimension of(Tag tag) {
            if (tag instanceof IntTag) {
                switch (((IntTag) tag).getValue()) {
                    case 0:
                        return OVERWORLD;
                    case -1:
                        return THE_NETHER;
                    case 1:
                        return THE_END;
                    default:
                        return null;
                }
            } else if (tag instanceof StringTag) {
                String id = ((StringTag) tag).getValue();
                switch (id) {
                    case "overworld":
                    case "minecraft:overworld":
                        return OVERWORLD;
                    case "the_nether":
                    case "minecraft:the_nether":
                        return THE_NETHER;
                    case "the_end":
                    case "minecraft:the_end":
                        return THE_END;
                    default:
                        return new Dimension(id);
                }
            } else {
                return null;
            }
        }

        private Dimension(String name) {
            this.name = name;
        }

        @SuppressLint("DefaultLocale")
        String formatPosition(Tag tag) {
            if (tag instanceof ListTag) {
                ListTag listTag = (ListTag) tag;
                if (listTag.size() != 3)
                    return null;

                Tag x = listTag.get(0);
                Tag y = listTag.get(1);
                Tag z = listTag.get(2);

                if (x instanceof DoubleTag && y instanceof DoubleTag && z instanceof DoubleTag) {
                    //noinspection MalformedFormatString
                    return this == OVERWORLD
                            ? String.format("(%.2f, %.2f, %.2f)", ((DoubleTag) x).getValue(), ((DoubleTag) y).getValue(), ((DoubleTag) z).getValue())
                            : String.format("%s (%.2f, %.2f, %.2f)", name, ((DoubleTag) x).getValue(), ((DoubleTag) y).getValue(), ((DoubleTag) z).getValue());
                }

                return null;
            }

            if (tag instanceof IntArrayTag) {
                IntArrayTag intArrayTag = (IntArrayTag) tag;

                int x = intArrayTag.getValue(0);
                int y = intArrayTag.getValue(1);
                int z = intArrayTag.getValue(2);

                return this == OVERWORLD
                        ? String.format("(%d, %d, %d)", x, y, z)
                        : String.format("%s (%d, %d, %d)", name, x, y, z);
            }

            return null;
        }

        @SuppressLint("DefaultLocale")
        String formatPosition(int x, int y, int z) {
            return this == OVERWORLD
                    ? String.format("(%d, %d, %d)", x, y, z)
                    : String.format("%s (%d, %d, %d)", name, x, y, z);
        }

        @SuppressLint("DefaultLocale")
        String formatPosition(double x, double y, double z) {
            return this == OVERWORLD
                    ? String.format("(%.2f, %.2f, %.2f)", x, y, z)
                    : String.format("%s (%.2f, %.2f, %.2f)", name, x, y, z);
        }
    }

    private enum Difficulty {
        PEACEFUL, EASY, NORMAL, HARD;

        static final ObservableList<Difficulty> items = FXCollections.observableList(Arrays.asList(values()));

        static Difficulty of(int d) {
            return d >= 0 && d < items.size() ? items.get(d) : null;
        }

        static Difficulty of(String name) {
            for (Difficulty d : items)
                if (d.name().toLowerCase(Locale.ROOT).equals(name))
                    return d;
            return null;
        }

        String getTagStringValue() {
            return name().toLowerCase(Locale.ROOT);
        }

        public int getPosition() {
            switch (this) {
                case PEACEFUL:
                    return 0;
                case EASY:
                    return 1;
                case NORMAL:
                    return 2;
                default:
                    return 3;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return AndroidUtilKt.getLocalizedText(FCLApp.getAppContext(), "world_info_difficulty_" + name().toLowerCase(Locale.ROOT));
        }
    }

    private enum GameType {
        SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR, HARDCORE;

        static final ObservableList<GameType> items = FXCollections.observableList(Arrays.asList(values()));

        static GameType of(int d, boolean hardcore) {
            if (hardcore && d == 0)
                return HARDCORE; // hardcore + survival
            return d >= 0 && d < 4 ? items.get(d) : null;
        }

        public int getPosition() {
            switch (this) {
                case SURVIVAL:
                    return 0;
                case CREATIVE:
                    return 1;
                case ADVENTURE:
                    return 2;
                case SPECTATOR:
                    return 3;
                default:
                    return 4;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return AndroidUtilKt.getLocalizedText(FCLApp.getAppContext(), "world_info_player_game_type_" + name().toLowerCase(Locale.ROOT));
        }
    }
}
