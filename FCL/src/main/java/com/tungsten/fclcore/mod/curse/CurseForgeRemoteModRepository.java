/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2021  huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fclcore.mod.curse;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fclcore.util.Pair.pair;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.FCLApp;
import com.tungsten.fcl.R;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModCache;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.util.MurmurHash2;
import com.tungsten.fclcore.util.Pair;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.io.NetworkUtils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.Checksum;

public final class CurseForgeRemoteModRepository implements RemoteModRepository {

    private static final String PREFIX = "https://api.curseforge.com";
    private static final String apiKey = FCLApp.getAppContext().getString(R.string.curse_api_key);

    private static final int WORD_PERFECT_MATCH_WEIGHT = 5;

    private static <R extends HttpRequest> R withApiKey(R request) {
        if (request.getUrl().startsWith(PREFIX) && !apiKey.isEmpty()) {
            request.header("X-API-KEY", apiKey);
        }
        return request;
    }

    public static boolean isAvailable() {
        return !apiKey.equals("null");
    }

    private final Type type;
    private final int section;

    public CurseForgeRemoteModRepository(Type type, int section) {
        this.type = type;
        this.section = section;
    }

    @Override
    public Type getType() {
        return type;
    }

    private int toModsSearchSortField(SortType sort) {
        // https://docs.curseforge.com/#tocS_ModsSearchSortField
        switch (sort) {
            case DATE_CREATED:
                return 1;
            case POPULARITY:
                return 2;
            case LAST_UPDATED:
                return 3;
            case NAME:
                return 4;
            case AUTHOR:
                return 5;
            case TOTAL_DOWNLOADS:
                return 6;
            default:
                return 8;
        }
    }

    private String toSortOrder(SortOrder sortOrder) {
        // https://docs.curseforge.com/#tocS_SortOrder
        switch (sortOrder) {
            case ASC:
                return "asc";
            case DESC:
                return "desc";
        }
        return "asc";
    }

    private int calculateTotalPages(Response<List<CurseAddon>> response, int pageSize) {
        return (int) Math.ceil((double) Math.min(response.pagination.totalCount, 10000) / pageSize);
    }

    @Override
    public SearchResult search(DownloadProvider downloadProvider, String gameVersion, @Nullable RemoteModRepository.Category category, int pageOffset, int pageSize, String searchFilter, SortType sortType, SortOrder sortOrder) throws IOException {
        int categoryId = 0;
        if (category != null) categoryId = ((CurseAddon.Category) category.self()).id();
        var query = new LinkedHashMap<String, String>();
        query.put("gameId", "432");
        query.put("classId", Integer.toString(section));
        if (categoryId != 0)
            query.put("categoryId", Integer.toString(categoryId));
        query.put("gameVersion", gameVersion);
        query.put("searchFilter", searchFilter);
        query.put("sortField", Integer.toString(toModsSearchSortField(sortType)));
        query.put("sortOrder", toSortOrder(sortOrder));
        query.put("index", Integer.toString(pageOffset * pageSize));
        query.put("pageSize", Integer.toString(pageSize));

        Response<List<CurseAddon>> response = null;

        IOException exception = null;
        List<URL> candidates = downloadProvider.injectURLWithCandidates(NetworkUtils.withQuery(PREFIX + "/v1/mods/search", query));
        for (URL candidate : candidates) {
            LOG.info("Fetching " + candidate);
            try {
                response = withApiKey(HttpRequest.GET(candidate.toString()))
                        .getJson(new TypeToken<Response<List<CurseAddon>>>() {
                        }.getType());
                if (searchFilter.isEmpty()) {
                    return new SearchResult(response.data().stream().map(CurseAddon::toMod), calculateTotalPages(response, pageSize));
                }
                break;
            } catch (IOException e) {
                LOG.warning("Failed to search addons: " + candidate + "\n" + e);
                if (candidates.size() == 1) {
                    exception = e;
                } else {
                    if (exception == null) {
                        exception = new IOException("Failed to search addons");
                    }
                    exception.addSuppressed(e);
                }
            }
        }

        if (response == null) {
            throw exception != null ? exception : new IOException("No candidates found");
        }

        String lowerCaseSearchFilter = searchFilter.toLowerCase(Locale.ROOT);
        Map<String, Integer> searchFilterWords = new HashMap<>();
        for (String s : StringUtils.tokenize(lowerCaseSearchFilter)) {
            searchFilterWords.merge(s, 1, Integer::sum);
        }

        StringUtils.LevCalculator levCalculator = new StringUtils.LevCalculator();

        return new SearchResult(response.data().stream().map(CurseAddon::toMod).map(remoteMod -> {
            String lowerCaseResult = remoteMod.getTitle().toLowerCase();
            int diff = levCalculator.calc(lowerCaseSearchFilter, lowerCaseResult);

            for (String s : StringUtils.tokenize(lowerCaseResult)) {
                if (searchFilterWords.containsKey(s)) {
                    diff -= WORD_PERFECT_MATCH_WEIGHT * searchFilterWords.get(s) * s.length();
                }
            }

            return pair(remoteMod, diff);
        }).sorted(Comparator.comparingInt(Pair::getValue)).map(Pair::getKey), response.data().stream().map(CurseAddon::toMod), calculateTotalPages(response, pageSize));
    }

    /**
     * 计算 CurseForge 文件指纹：剔除空白符（0x9/0xa/0xd/0x20）后计算 MurmurHash2。
     * 采用流式两遍扫描（1MB 缓冲），不在内存中保留整个过滤后的文件，避免大文件 OOM。
     */
    static long calculateFingerprint(Path file) throws IOException {
        try (SeekableByteChannel channel = Files.newByteChannel(file, StandardOpenOption.READ)) {
            long startPosition = channel.position();

            byte[] bufferArray = new byte[1024 * 1024];
            ByteBuffer buffer = ByteBuffer.wrap(bufferArray);

            // 第一遍：统计过滤空白符后的总长度
            long filteredLength = 0;
            while (channel.read(buffer) > 0) {
                int len = buffer.position();
                for (int i = 0; i < len; i++) {
                    byte b = bufferArray[i];
                    if (b != 0x9 && b != 0xa && b != 0xd && b != 0x20) {
                        filteredLength++;
                    }
                }
                buffer.clear();
            }

            channel.position(startPosition);

            // 第二遍：原地剔除空白符，喂给流式哈希
            Checksum hasher = MurmurHash2.hash32(filteredLength, 1);
            while (channel.read(buffer) > 0) {
                int len = buffer.position();

                int pos = 0;
                while (pos < len) {
                    byte b = bufferArray[pos];
                    if (b == 0x9 || b == 0xa || b == 0xd || b == 0x20) {
                        break;
                    }
                    pos++;
                }

                if (pos < len) {
                    int pos2 = pos + 1;
                    while (pos2 < len) {
                        byte b = bufferArray[pos2];
                        if (b != 0x9 && b != 0xa && b != 0xd && b != 0x20) {
                            bufferArray[pos++] = b;
                        }
                        pos2++;
                    }
                }

                hasher.update(bufferArray, 0, pos);
                buffer.clear();
            }
            return hasher.getValue();
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Optional<RemoteMod.Version> getRemoteVersionByLocalFile(LocalModFile localModFile, Path file) throws IOException {
        long hash = calculateFingerprint(file);
        // Workaround for https://github.com/HMCL-dev/HMCL/issues/4597
        // 1.20.1 Forge GeckoLib（id=388172）与wonderland.jar real one（id=1634457）
        if (hash == 811513880 || hash == 252446230) {
            return Optional.empty();
        }

        LOG.info("Matching file " + file.getFileName() + " (fingerprint: " + hash + ") via " + PREFIX + "/v1/fingerprints/432");
        // 指纹按文件内容寻址，命中结果与负结果（未命中）均永久缓存
        CurseAddon.LatestFile match = RemoteModCache.getOrFetch("cf:fp:" + hash, RemoteModCache.TTL_PERMANENT,
                CurseAddon.LatestFile.class, () -> {
                    Response<FingerprintMatchesResult> response = withApiKey(HttpRequest.POST(PREFIX + "/v1/fingerprints/432"))
                            .json(mapOf(pair("fingerprints", Collections.singletonList(hash))))
                            .getJson(new TypeToken<Response<FingerprintMatchesResult>>() {
                            }.getType());

                    if (response.data().exactMatches() == null || response.data().exactMatches().isEmpty()) {
                        return null;
                    }
                    return response.data().exactMatches().get(0).file();
                });

        return match == null ? Optional.empty() : Optional.of(match.toVersion());
    }

    @Override
    public RemoteMod getModById(String id) throws IOException {
        CurseAddon addon = RemoteModCache.getOrFetch("cf:mod:" + id, RemoteModCache.TTL_DETAIL,
                CurseAddon.class, () -> {
                    Response<CurseAddon> response = withApiKey(HttpRequest.GET(PREFIX + "/v1/mods/" + id))
                            .getJson(new TypeToken<Response<CurseAddon>>() {
                            }.getType());
                    return response.data;
                });
        return addon.toMod();
    }

    @Override
    public RemoteMod.File getModFile(String modId, String fileId) throws IOException {
        CurseAddon.LatestFile file = RemoteModCache.getOrFetch("cf:file:" + modId + ":" + fileId, RemoteModCache.TTL_PERMANENT,
                CurseAddon.LatestFile.class, () -> {
                    Response<CurseAddon.LatestFile> response = withApiKey(HttpRequest.GET(String.format("%s/v1/mods/%s/files/%s", PREFIX, modId, fileId)))
                            .getJson(new TypeToken<Response<CurseAddon.LatestFile>>() {
                            }.getType());
                    return response.data();
                });
        return file.toVersion().file();
    }

    @Override
    public Stream<RemoteMod.Version> getRemoteVersionsById(String id) throws IOException {
        List<CurseAddon.LatestFile> files = RemoteModCache.getOrFetch("cf:ver:" + id, RemoteModCache.TTL_VERSIONS,
                JsonUtils.listTypeOf(CurseAddon.LatestFile.class).getType(),
                () -> {
                    Response<List<CurseAddon.LatestFile>> response = withApiKey(HttpRequest.GET(PREFIX + "/v1/mods/" + id + "/files",
                            pair("pageSize", "10000")))
                            .getJson(new TypeToken<Response<List<CurseAddon.LatestFile>>>() {
                            }.getType());
                    return response.data();
                });
        return files.stream().map(CurseAddon.LatestFile::toVersion);
    }

    public List<CurseAddon.Category> getCategoriesImpl() throws IOException {
        // 分类几乎不变，长 TTL 缓存；缓存原始扁平列表
        return RemoteModCache.getOrFetch("cf:cat:" + section, RemoteModCache.TTL_CATEGORIES,
                JsonUtils.listTypeOf(CurseAddon.Category.class).getType(),
                () -> {
                    Response<List<CurseAddon.Category>> response = withApiKey(HttpRequest.GET(PREFIX + "/v1/categories", pair("gameId", "432")))
                            .getJson(new TypeToken<Response<List<CurseAddon.Category>>>() {
                            }.getType());
                    return response.data();
                });
    }

    @Override
    public Stream<RemoteModRepository.Category> getCategories() throws IOException {
        return reorganizeCategories(getCategoriesImpl(), section).stream();
    }

    // API 返回扁平列表，按 parentCategoryId 递归组装层级树；父项不存在的条目直接丢弃（与旧逻辑一致）
    private List<RemoteModRepository.Category> reorganizeCategories(List<CurseAddon.Category> categories, int rootId) {
        Map<Integer, List<CurseAddon.Category>> childrenMap = new HashMap<>();
        for (CurseAddon.Category category : categories) {
            childrenMap.computeIfAbsent(category.parentCategoryId(), k -> new ArrayList<>()).add(category);
        }

        List<RemoteModRepository.Category> result = new ArrayList<>();
        for (CurseAddon.Category category : childrenMap.getOrDefault(rootId, Collections.emptyList())) {
            result.add(toCategoryTree(category, childrenMap));
        }
        return result;
    }

    private RemoteModRepository.Category toCategoryTree(CurseAddon.Category category, Map<Integer, List<CurseAddon.Category>> childrenMap) {
        List<RemoteModRepository.Category> subcategories = new ArrayList<>();
        for (CurseAddon.Category subcategory : childrenMap.getOrDefault(category.id(), Collections.emptyList())) {
            subcategories.add(toCategoryTree(subcategory, childrenMap));
        }
        return new RemoteModRepository.Category(category, Integer.toString(category.id()), subcategories);
    }

    public static final int SECTION_BUKKIT_PLUGIN = 5;
    public static final int SECTION_MOD = 6;
    public static final int SECTION_RESOURCE_PACK = 12;
    public static final int SECTION_WORLD = 17;
    public static final int SECTION_MODPACK = 4471;
    public static final int SECTION_CUSTOMIZATION = 4546;
    public static final int SECTION_ADDONS = 4559; // For Pocket Edition
    public static final int SECTION_UNKNOWN1 = 4944;
    public static final int SECTION_UNKNOWN2 = 4979;
    public static final int SECTION_UNKNOWN3 = 4984;
    public static final int SECTION_SHADER_PACK = 6552;

    public static final CurseForgeRemoteModRepository MODS = new CurseForgeRemoteModRepository(RemoteModRepository.Type.MOD, SECTION_MOD);
    public static final CurseForgeRemoteModRepository MODPACKS = new CurseForgeRemoteModRepository(RemoteModRepository.Type.MODPACK, SECTION_MODPACK);
    public static final CurseForgeRemoteModRepository RESOURCE_PACKS = new CurseForgeRemoteModRepository(RemoteModRepository.Type.RESOURCE_PACK, SECTION_RESOURCE_PACK);
    public static final CurseForgeRemoteModRepository WORLDS = new CurseForgeRemoteModRepository(RemoteModRepository.Type.WORLD, SECTION_WORLD);
    public static final CurseForgeRemoteModRepository CUSTOMIZATIONS = new CurseForgeRemoteModRepository(RemoteModRepository.Type.CUSTOMIZATION, SECTION_CUSTOMIZATION);
    public static final CurseForgeRemoteModRepository SHADER_PACKS = new CurseForgeRemoteModRepository(Type.SHADER_PACK, SECTION_SHADER_PACK);

    public record Pagination(int index, int pageSize, int resultCount, int totalCount) {
    }

    public record Response<T>(T data, Pagination pagination) {
    }

    /**
     * @see <a href="https://docs.curseforge.com/#tocS_FingerprintsMatchesResult">Schema</a>
     */
    private record FingerprintMatchesResult(boolean isCacheBuilt,
                                            List<FingerprintMatch> exactMatches,
                                            List<Long> exactFingerprints) {
    }

    /**
     * @see <a href="https://docs.curseforge.com/#tocS_FingerprintMatch">Schema</a>
     */
    private record FingerprintMatch(int id, CurseAddon.LatestFile file,
                                    List<CurseAddon.LatestFile> latestFiles) {
    }
}
