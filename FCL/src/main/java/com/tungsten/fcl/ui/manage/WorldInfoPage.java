package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.game.World;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class WorldInfoPage extends FCLTempPage {

    private final World world;
    private final CompoundTag levelDat;
    private final CompoundTag dataTag;
    private final CompoundTag worldGenSettings;

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

    public WorldInfoPage(Context context, int id, FCLUILayout parent, int resId, World world) throws IOException {
        super(context, id, parent, resId);
        this.world = world;
        this.levelDat = world.readLevelDat();
        this.dataTag = levelDat.get("Data");
        this.worldGenSettings = dataTag.get("WorldGenSettings");
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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onStart() {
        super.onStart();

        name.setText(world.getWorldName());
        gameVersion.setText(world.getGameVersion());
        Tag seedTag = worldGenSettings != null ? worldGenSettings.get("seed") : dataTag.get("RandomSeed");
        if (seedTag instanceof LongTag) {
            seed.setText(seedTag.getValue().toString());
        }
        lastPlayed.setText(new SimpleDateFormat(getContext().getString(R.string.world_time)).format(new Date(world.getLastPlayed())));
        Tag timeTag = dataTag.get("Time");
        if (timeTag instanceof LongTag) {
            long days = ((LongTag) timeTag).getValue() / 24000;
            time.setText(AndroidUtils.getLocalizedText(getContext(), "world_info_time_format", days));
        }
        Tag cheatTag = dataTag.get("allowCommands");
        if (cheatTag instanceof ByteTag) {
            ByteTag byteTag = (ByteTag) cheatTag;
            byte value = byteTag.getValue();
            if (value == 0 || value == 1) {
                allowCheat.setChecked(value == 1);
                allowCheat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    byteTag.setValue(isChecked ? (byte) 1 : (byte) 0);
                    saveLevelDat();
                });
            } else {
                allowCheat.setEnabled(false);
            }
        } else {
            allowCheat.setEnabled(false);
        }
        Tag genTag = worldGenSettings != null ? worldGenSettings.get("generate_features") : dataTag.get("MapFeatures");
        if (genTag instanceof ByteTag) {
            ByteTag byteTag = (ByteTag) genTag;
            byte value = byteTag.getValue();
            if (value == 0 || value == 1) {
                generateStructure.setChecked(value == 1);
                generateStructure.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    byteTag.setValue(isChecked ? (byte) 1 : (byte) 0);
                    saveLevelDat();
                });
            } else {
                generateStructure.setEnabled(false);
            }
        } else {
            generateStructure.setEnabled(false);
        }
        difficulty.setDataList(new ArrayList<>(Difficulty.items));
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, Difficulty.items.stream().map(Difficulty::toString).collect(Collectors.toList()));
        difficultyAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        difficulty.setAdapter(difficultyAdapter);
        Tag difficultyTag = dataTag.get("Difficulty");
        if (difficultyTag instanceof ByteTag) {
            ByteTag byteTag = (ByteTag) difficultyTag;
            Difficulty difficulty = Difficulty.of(byteTag.getValue());
            if (difficulty != null) {
                this.difficulty.setSelection(difficulty.getPosition());
                ObjectProperty<Difficulty> difficultyProperty = new SimpleObjectProperty<>(difficulty);
                FXUtils.bindSelection(this.difficulty, difficultyProperty);
                difficultyProperty.addListener(observable -> {
                    if (difficultyProperty.get() != null) {
                        byteTag.setValue((byte) difficultyProperty.get().ordinal());
                        saveLevelDat();
                    }
                });
            } else {
                this.difficulty.setEnabled(false);
            }
        } else {
            this.difficulty.setEnabled(false);
        }

        Tag playerTag = dataTag.get("Player");
        if (playerTag instanceof CompoundTag) {
            CompoundTag player = (CompoundTag) playerTag;
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
            Dimension spawnDim = Dimension.of(player.get("SpawnDimension"));
            if (spawnDim != null) {
                Tag x = player.get("SpawnX");
                Tag y = player.get("SpawnY");
                Tag z = player.get("SpawnZ");
                if (x instanceof IntTag && y instanceof IntTag && z instanceof IntTag)
                    spawn.setText(spawnDim.formatPosition(((IntTag) x).getValue(), ((IntTag) y).getValue(), ((IntTag) z).getValue()));
            }
            gameType.setDataList(new ArrayList<>(GameType.items));
            ArrayAdapter<String> gameTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, GameType.items.stream().map(GameType::toString).collect(Collectors.toList()));
            gameTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            gameType.setAdapter(gameTypeAdapter);
            Tag gameTypeTag = player.get("playerGameType");
            if (gameTypeTag instanceof IntTag) {
                IntTag intTag = (IntTag) gameTypeTag;
                GameType gameType = GameType.of(intTag.getValue());
                if (gameType != null) {
                    this.gameType.setSelection(gameType.getPosition());
                    ObjectProperty<GameType> gameTypeProperty = new SimpleObjectProperty<>(gameType);
                    FXUtils.bindSelection(this.gameType, gameTypeProperty);
                    gameTypeProperty.addListener(observable -> {
                        if (gameTypeProperty.get() != null) {
                            intTag.setValue(gameTypeProperty.get().ordinal());
                            saveLevelDat();
                        }
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
                health.setText(new DecimalFormat("#").format(floatTag.getValue().floatValue()));
                health.setStringValue(new DecimalFormat("#").format(floatTag.getValue().floatValue()));
                health.stringProperty().addListener(observable -> {
                    if (StringUtils.isBlank(health.getStringValue()) && Lang.toDoubleOrNull(health.getStringValue()) == null) {
                        Toast.makeText(getContext(), getContext().getString(R.string.input_number), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            floatTag.setValue(Float.parseFloat(health.getStringValue()));
                            saveLevelDat();
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
                            saveLevelDat();
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
                            saveLevelDat();
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
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    private void saveLevelDat() {
        LOG.info("Saving level.dat of world " + world.getWorldName());
        try {
            this.world.writeLevelDat(levelDat);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to save level.dat of world " + world.getWorldName(), e);
        }
    }

    private static final class Dimension {
        static final Dimension OVERWORLD = new Dimension(null);
        static final Dimension THE_NETHER = new Dimension(FCLPath.CONTEXT.getString(R.string.world_info_dimension_the_nether));
        static final Dimension THE_END = new Dimension(FCLPath.CONTEXT.getString(R.string.world_info_dimension_the_end));

        final String name;

        static Dimension of(Tag tag) {
            if (tag instanceof IntTag) {
                switch (((IntTag) tag).getValue()) {
                    case 0:
                        return OVERWORLD;
                    case 1:
                        return THE_NETHER;
                    case 2:
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
                            ? String.format("(%.2f, %.2f, %.2f)", x.getValue(), y.getValue(), z.getValue())
                            : String.format("%s (%.2f, %.2f, %.2f)", name, x.getValue(), y.getValue(), z.getValue());
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
            return d >= 0 && d <= items.size() ? items.get(d) : null;
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
            return AndroidUtils.getLocalizedText(FCLPath.CONTEXT, "world_info_difficulty_" + name().toLowerCase(Locale.ROOT));
        }
    }

    private enum GameType {
        SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR;

        static final ObservableList<GameType> items = FXCollections.observableList(Arrays.asList(values()));

        static GameType of(int d) {
            return d >= 0 && d <= items.size() ? items.get(d) : null;
        }

        public int getPosition() {
            switch (this) {
                case SURVIVAL:
                    return 0;
                case CREATIVE:
                    return 1;
                case ADVENTURE:
                    return 2;
                default:
                    return 3;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return AndroidUtils.getLocalizedText(FCLPath.CONTEXT, "world_info_player_game_type_" + name().toLowerCase(Locale.ROOT));
        }
    }
}
