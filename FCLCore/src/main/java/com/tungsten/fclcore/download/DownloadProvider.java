package com.tungsten.fclcore.download;

import com.tungsten.fclcore.util.io.NetworkUtils;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The service provider that provides Minecraft online file downloads.
 *
 * @author huangyuhui
 */
public interface DownloadProvider {

    String getVersionListURL();

    String getAssetBaseURL();

    default List<URL> getAssetObjectCandidates(String assetObjectLocation) {
        return Collections.singletonList(NetworkUtils.toURL(getAssetBaseURL() + assetObjectLocation));
    }

    /**
     * Inject into original URL provided by Mojang and Forge.
     *
     * Since there are many provided URLs that are written in JSONs and are unmodifiable,
     * this method provides a way to change them.
     *
     * @param baseURL original URL provided by Mojang and Forge.
     * @return the URL that is equivalent to [baseURL], but belongs to your own service provider.
     */
    String injectURL(String baseURL);

    /**
     * Inject into original URL provided by Mojang and Forge.
     *
     * Since there are many provided URLs that are written in JSONs and are unmodifiable,
     * this method provides a way to change them.
     *
     * @param baseURL original URL provided by Mojang and Forge.
     * @return the URL that is equivalent to [baseURL], but belongs to your own service provider.
     */
    default List<URL> injectURLWithCandidates(String baseURL) {
        return Collections.singletonList(NetworkUtils.toURL(injectURL(baseURL)));
    }

    default List<URL> injectURLsWithCandidates(List<String> urls) {
        return urls.stream().flatMap(url -> injectURLWithCandidates(url).stream()).collect(Collectors.toList());
    }

    /**
     * the specific version list that this download provider provides. i.e. "fabric", "forge", "liteloader", "game", "optifine"
     *
     * @param id the id of specific version list that this download provider provides. i.e. "fabric", "forge", "liteloader", "game", "optifine"
     * @return the version list
     * @throws IllegalArgumentException if the version list does not exist
     */
    VersionList<?> getVersionListById(String id);

    /**
     * The maximum download concurrency that this download provider supports.
     * @return the maximum download concurrency.
     */
    int getConcurrency();
}
