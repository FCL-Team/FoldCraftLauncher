package com.tungsten.fclcore.download;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Official Download Provider fetches version list from Mojang and
 * download files from mcbbs.
 */
public class BalancedDownloadProvider implements DownloadProvider {
    List<DownloadProvider> candidates;

    Map<String, VersionList<?>> versionLists = new HashMap<>();

    public BalancedDownloadProvider(List<DownloadProvider> candidates) {
        this.candidates = candidates;
    }

    @Override
    public String getVersionListURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAssetBaseURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String injectURL(String baseURL) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VersionList<?> getVersionListById(String id) {
        if (!versionLists.containsKey(id)) {
            versionLists.put(id, new MultipleSourceVersionList(
                    candidates.stream()
                            .map(downloadProvider -> downloadProvider.getVersionListById(id))
                            .collect(Collectors.toList())));
        }
        return versionLists.get(id);
    }

    @Override
    public int getConcurrency() {
        throw new UnsupportedOperationException();
    }
}
