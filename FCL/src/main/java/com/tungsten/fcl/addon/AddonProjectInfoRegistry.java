/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2026 huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.addon;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.tungsten.fclcore.mod.AddonProjectInfo;
import com.tungsten.fclcore.mod.ModProjectInfo;
import com.tungsten.fclcore.mod.ModpackProjectInfo;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.util.Pair;
import com.tungsten.fclcore.util.StringUtils;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

/**
 * Registry of localized addon project metadata parsed from the CSV resources under {@code /assets/addon}.
 * <p>
 * Ported from HMCL's AddonProjectInfoRegistry (数据加载重构 #6987)，数据文件由上游
 * UpdateAddonProjectInfos 任务生成，按上游同步更新即可。
 */
public abstract class AddonProjectInfoRegistry<I extends AddonProjectInfo> {

    public static final AddonProjectInfoRegistry<ModProjectInfo> MOD = new AddonProjectInfoRegistry<>(
            "/assets/addon/mod.csv",
            32768
    ) {
        @Override
        protected int getFieldsCount() {
            return MOD_IDS_INDEX + 1;
        }

        @Override
        protected ModProjectInfo parseAddonProjectInfo(String[] fields) {
            return new ModProjectInfo(
                    Integer.parseInt(fields[MCMOD_ID_INDEX]),
                    fields[CURSEFORGE_ID_INDEX],
                    fields[MODRINTH_ID_INDEX],
                    fields[MAIN_NAME_INDEX],
                    fields[SUB_NAME_INDEX],
                    fields[ABBR_NAME_INDEX],
                    fields[MOD_IDS_INDEX]
            );
        }
    };

    public static final AddonProjectInfoRegistry<ModpackProjectInfo> MODPACK = new AddonProjectInfoRegistry<>(
            "/assets/addon/modpack.csv", 2048
    ) {
        @Override
        protected int getFieldsCount() {
            return ABBR_NAME_INDEX + 1;
        }

        @Override
        protected ModpackProjectInfo parseAddonProjectInfo(String[] fields) {
            return new ModpackProjectInfo(Integer.parseInt(fields[MCMOD_ID_INDEX]),
                    fields[CURSEFORGE_ID_INDEX], fields[MODRINTH_ID_INDEX],
                    fields[MAIN_NAME_INDEX], fields[SUB_NAME_INDEX], fields[ABBR_NAME_INDEX]);
        }
    };

    /**
     * Returns the registry for the addon type, or {@code null} for null or unsupported types.
     */
    public static @Nullable AddonProjectInfoRegistry<?> getByAddonType(@Nullable RemoteModRepository.Type type) {
        if (type == null) return null;
        return switch (type) {
            case MOD -> MOD;
            case MODPACK -> MODPACK;
            default -> null;
        };
    }

    protected static final int MCMOD_ID_INDEX = 0;
    protected static final int CURSEFORGE_ID_INDEX = 1;
    protected static final int MODRINTH_ID_INDEX = 2;
    protected static final int MAIN_NAME_INDEX = 3;
    protected static final int SUB_NAME_INDEX = 4;
    protected static final int ABBR_NAME_INDEX = 5;
    protected static final int MOD_IDS_INDEX = 6;

    private final ReentrantLock lock = new ReentrantLock();
    private final String assetsPath;
    private final int initialCapacity;

    private volatile @Nullable SoftReference<List<I>> infosRef;

    private volatile @Nullable SoftReference<Map<String, I>> modIdMapRef;

    private volatile @Nullable SoftReference<Map<String, I>> subnameMapRef;

    private volatile @Nullable SoftReference<Map<String, I>> curseForgeMapRef;

    private volatile @Nullable SoftReference<List<Pair<String, I>>> keywordsRef;

    AddonProjectInfoRegistry(String assetsPath, int initialCapacity) {
        this.assetsPath = assetsPath;
        this.initialCapacity = initialCapacity;
    }

    protected abstract int getFieldsCount();

    protected abstract I parseAddonProjectInfo(String[] fields) throws Exception;

    /**
     * 在后台线程预加载全部数据与索引，避免首次使用时在主线程解析大文件造成卡顿。
     */
    public void preload() {
        getInfos();
        getModIdMap();
        getSubnameMap();
        getCurseForgeMap();
    }

    private List<I> loadInfos() throws IOException {
        InputStream inputStream = AddonProjectInfoRegistry.class.getResourceAsStream(assetsPath);
        if (inputStream == null) {
            return List.of();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            ArrayList<I> infos = new ArrayList<>(initialCapacity);
            List<I> result = Collections.unmodifiableList(infos);

            int fieldsCount = getFieldsCount();
            String[] fields = new String[fieldsCount];

            StringBuilder fieldBuilder = null;

            String line;
            loop:
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int offset = 0;
                for (int fieldIndex = 0; fieldIndex < fieldsCount; fieldIndex++) {
                    if (offset > line.length()) {
                        LOG.warning("Invalid line in " + assetsPath + ": " + line);
                        continue loop;
                    }

                    int end = line.indexOf(',', offset);
                    if (end < 0)
                        end = line.length();

                    String field;
                    if (offset == end) {
                        field = "";
                    } else {
                        int cursor = offset;
                        while (cursor < end) {
                            if (line.charAt(cursor) == '\\') {
                                break;
                            } else {
                                cursor++;
                            }
                        }

                        if (cursor == end) {
                            field = line.substring(offset, end);
                        } else {
                            if (fieldBuilder == null) {
                                fieldBuilder = new StringBuilder(32);
                            } else {
                                fieldBuilder.setLength(0);
                            }

                            fieldBuilder.append(line, offset, cursor);
                            while (cursor < end) {
                                char c = line.charAt(cursor);
                                if (c == '\\') {
                                    if (cursor < end - 1) {
                                        char next = line.charAt(cursor + 1);
                                        switch (next) {
                                            case '0' -> {
                                                fieldBuilder.append('\0');
                                                cursor += 2;
                                            }
                                            case 'n' -> {
                                                fieldBuilder.append('\n');
                                                cursor += 2;
                                            }
                                            case 'r' -> {
                                                fieldBuilder.append('\r');
                                                cursor += 2;
                                            }
                                            case 't' -> {
                                                fieldBuilder.append('\t');
                                                cursor += 2;
                                            }
                                            case '\\' -> {
                                                fieldBuilder.append('\\');
                                                cursor += 2;
                                            }
                                            case 'c' -> {
                                                fieldBuilder.append(',');
                                                cursor += 2;
                                            }
                                            case 'u' -> {
                                                if (cursor < end - 5) {
                                                    try {
                                                        int codePoint = Integer.parseInt(line, cursor + 2, cursor + 6, 16);
                                                        fieldBuilder.append((char) codePoint);
                                                        cursor += 6;
                                                    } catch (NumberFormatException e) {
                                                        LOG.warning("Invalid line in " + assetsPath + ": " + line);
                                                        continue loop;
                                                    }
                                                } else {
                                                    fieldBuilder.append(c);
                                                    cursor++;
                                                }
                                            }
                                            default -> {
                                                LOG.warning("Invalid line in " + assetsPath + ": " + line);
                                                continue loop;
                                            }
                                        }
                                    } else {
                                        fieldBuilder.append(c);
                                        cursor++;
                                    }

                                } else {
                                    fieldBuilder.append(c);
                                    cursor++;
                                }
                            }
                            field = fieldBuilder.toString();
                        }
                    }

                    fields[fieldIndex] = field;
                    offset = end + 1;
                }

                try {
                    I info = parseAddonProjectInfo(fields);
                    info.setKeepAlive(result);
                    infos.add(info);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Failed to parse line in " + assetsPath + ": " + line, e);
                }
            }

            infos.trimToSize();
            return result;
        }
    }

    /**
     * Returns an unmodifiable index, rebuilding it after its soft reference is cleared.
     * Duplicate keys retain the first project in resource order.
     */
    private Map<String, I> getModIdMap() {
        SoftReference<Map<String, I>> reference = this.modIdMapRef;
        Map<String, I> cached = reference != null ? reference.get() : null;
        if (cached != null)
            return cached;
        lock.lock();
        try {
            reference = this.modIdMapRef;
            cached = reference != null ? reference.get() : null;
            if (cached != null)
                return cached;
            Map<String, I> map = new HashMap<>();
            for (I info : getInfos()) {
                if (info instanceof ModProjectInfo mod) {
                    for (String id : mod.getModIds()) {
                        if (!id.isEmpty())
                            map.putIfAbsent(id, info);
                    }
                }
            }
            cached = Collections.unmodifiableMap(map);
            this.modIdMapRef = new SoftReference<>(cached);
            return cached;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns an unmodifiable index, rebuilding it after its soft reference is cleared.
     * Duplicate keys retain the first project in resource order.
     */
    private Map<String, I> getSubnameMap() {
        SoftReference<Map<String, I>> reference = this.subnameMapRef;
        Map<String, I> cached = reference != null ? reference.get() : null;
        if (cached != null)
            return cached;
        lock.lock();
        try {
            reference = this.subnameMapRef;
            cached = reference != null ? reference.get() : null;
            if (cached != null)
                return cached;
            Map<String, I> map = new HashMap<>();
            for (I info : getInfos()) {
                String name = cleanSubname(info.subName());
                if (!name.isEmpty())
                    map.putIfAbsent(name, info);
            }
            cached = Collections.unmodifiableMap(map);
            this.subnameMapRef = new SoftReference<>(cached);
            return cached;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns an unmodifiable index, rebuilding it after its soft reference is cleared.
     * Duplicate keys retain the first project in resource order.
     */
    private Map<String, I> getCurseForgeMap() {
        SoftReference<Map<String, I>> reference = this.curseForgeMapRef;
        Map<String, I> cached = reference != null ? reference.get() : null;
        if (cached != null)
            return cached;
        lock.lock();
        try {
            reference = this.curseForgeMapRef;
            cached = reference != null ? reference.get() : null;
            if (cached != null)
                return cached;
            Map<String, I> map = new HashMap<>();
            for (I info : getInfos()) {
                String id = info.curseforgeId();
                if (StringUtils.isNotBlank(id))
                    map.putIfAbsent(id, info);
            }
            cached = Collections.unmodifiableMap(map);
            this.curseForgeMapRef = new SoftReference<>(cached);
            return cached;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the first project with the exact CurseForge identifier, or {@code null} if the
     * identifier is null, blank or unknown.
     */
    public @Nullable I getInfoByCurseForgeId(@Nullable String id) {
        return StringUtils.isBlank(id) ? null : getCurseForgeMap().get(id);
    }

    /**
     * Finds a project by its cleaned secondary name, falling back to its exact mod ID.
     * A name match is accepted only when the ID is blank or belongs to that mod project.
     * Null and blank inputs are absent; returns {@code null} if neither lookup matches.
     * Duplicate keys resolve to the first project in resource order.
     */
    public @Nullable I getInfo(@Nullable String id, @Nullable String subname) {
        String name = cleanSubname(subname);
        if (StringUtils.isNotBlank(name)) {
            I info = getSubnameMap().get(name);
            if (info != null && (StringUtils.isBlank(id)
                    || info instanceof ModProjectInfo mod && mod.getModIds().contains(id)))
                return info;
        }
        return StringUtils.isBlank(id) ? null : getModIdMap().get(id);
    }

    private static boolean isSupportedSubnameChar(int ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                || ".+\\".indexOf(ch) >= 0;
    }

    /**
     * Removes supported separators and pictographs from a secondary name.
     * Returns an empty string for null, blank or unsupported names; matching is case-sensitive.
     */
    private static String cleanSubname(@Nullable String subname) {
        if (StringUtils.isBlank(subname))
            return "";

        int offset = 0;
        while (offset < subname.length()) {
            int ch = subname.charAt(offset);
            if (!isSupportedSubnameChar(ch))
                break;
            offset++;
        }

        if (offset == subname.length())
            return subname;

        StringBuilder builder = new StringBuilder(subname.length());
        builder.append(subname, 0, offset);

        while (offset < subname.length()) {
            int ch = subname.codePointAt(offset);
            if (isSupportedSubnameChar(ch)) {
                builder.appendCodePoint(ch);
            } else if (!(Character.isWhitespace(ch)
                    || "':_-/&()[]{}|,!?~•".indexOf(ch) >= 0
                    || ch >= 0x1F300 && ch <= 0x1FAFF)) {
                return "";
            }
            offset += Character.charCount(ch);
        }
        return builder.toString();
    }

    /**
     * Returns searchable names, rebuilding the index after its soft reference is cleared.
     */
    private List<Pair<String, I>> getKeywords() {
        SoftReference<List<Pair<String, I>>> reference = keywordsRef;
        List<Pair<String, I>> cached = reference != null ? reference.get() : null;
        if (cached != null)
            return cached;
        lock.lock();
        try {
            reference = keywordsRef;
            cached = reference != null ? reference.get() : null;
            if (cached != null)
                return cached;
            List<Pair<String, I>> keywords = new ArrayList<>();
            for (I info : getInfos()) {
                if (!info.mainName().isEmpty())
                    keywords.add(Pair.pair(info.mainName(), info));
                if (!info.subName().isEmpty())
                    keywords.add(Pair.pair(info.subName(), info));
                if (!info.abbrName().isEmpty())
                    keywords.add(Pair.pair(info.abbrName(), info));
            }
            cached = List.copyOf(keywords);
            keywordsRef = new SoftReference<>(cached);
            return cached;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns projects ranked by descending longest common subsequence length after removing
     * space characters from the query. Comparison is case-sensitive and uses UTF-16 units.
     * Matches require at least one unit and at least query length minus three units.
     * Each matching name contributes a result, so projects may occur more than once.
     * Ties preserve resource order, then primary name, secondary name and abbreviation order.
     */
    public List<I> search(String query) {
        query = query.chars().filter(ch -> !Character.isSpaceChar(ch))
                .collect(StringBuilder::new, (sb, ch) -> sb.append((char) ch), StringBuilder::append).toString();
        List<Pair<String, I>> keywords = getKeywords();
        int maxLength = keywords.stream().mapToInt(keyword -> keyword.getKey().length()).max().orElse(0);
        StringUtils.LongestCommonSubsequence lcs = new StringUtils.LongestCommonSubsequence(query.length(), maxLength);
        List<Pair<Integer, I>> matches = new ArrayList<>();
        for (Pair<String, I> keyword : keywords) {
            int score = lcs.calc(query, keyword.getKey());
            if (score >= Math.max(1, query.length() - 3))
                matches.add(Pair.pair(score, keyword.getValue()));
        }
        return matches.stream().sorted((a, b) -> Integer.compare(b.getKey(), a.getKey()))
                .map(Pair::getValue).toList();
    }

    /**
     * Returns cached project metadata, loading it on demand. Missing resources and I/O failures
     * produce an empty list; malformed records are skipped. The cache is softly referenced.
     */
    public List<I> getInfos() {
        SoftReference<List<I>> infosRef = this.infosRef;
        List<I> infos = infosRef != null ? infosRef.get() : null;
        if (infos != null)
            return infos;

        lock.lock();
        try {
            infosRef = this.infosRef;
            infos = infosRef != null ? infosRef.get() : null;
            if (infos != null)
                return infos;

            try {
                infos = loadInfos();
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to load addon project info from " + assetsPath, e);
                infos = List.of();
            }
            this.infosRef = new SoftReference<>(infos);
            return infos;
        } finally {
            lock.unlock();
        }
    }
}
