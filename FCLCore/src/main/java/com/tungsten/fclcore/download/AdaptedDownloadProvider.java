package com.tungsten.fclcore.download;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The download provider that changes the real download source in need.
 */
public class AdaptedDownloadProvider implements DownloadProvider {

    private List<DownloadProvider> downloadProviderCandidates;

    public void setDownloadProviderCandidates(List<DownloadProvider> downloadProviderCandidates) {
        this.downloadProviderCandidates = new ArrayList<>(downloadProviderCandidates);
    }

    public DownloadProvider getPreferredDownloadProvider() {
        List<DownloadProvider> d = downloadProviderCandidates;
        if (d == null || d.isEmpty()) {
            throw new IllegalStateException("No download provider candidate");
        }
        return d.get(0);
    }

    @Override
    public String getVersionListURL() {
        return getPreferredDownloadProvider().getVersionListURL();
    }

    @Override
    public String getAssetBaseURL() {
        return getPreferredDownloadProvider().getAssetBaseURL();
    }

    @Override
    public String injectURL(String baseURL) {
        return getPreferredDownloadProvider().injectURL(baseURL);
    }

    @Override
    public List<URL> getAssetObjectCandidates(String assetObjectLocation) {
        return downloadProviderCandidates.stream()
                .flatMap(d -> d.getAssetObjectCandidates(assetObjectLocation).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<URL> injectURLWithCandidates(String baseURL) {
        return downloadProviderCandidates.stream()
                .flatMap(d -> d.injectURLWithCandidates(baseURL).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<URL> injectURLsWithCandidates(List<String> urls) {
        return downloadProviderCandidates.stream()
                .flatMap(d -> d.injectURLsWithCandidates(urls).stream())
                .collect(Collectors.toList());
    }

    @Override
    public VersionList<?> getVersionListById(String id) {
        return getPreferredDownloadProvider().getVersionListById(id);
    }

    @Override
    public int getConcurrency() {
        return getPreferredDownloadProvider().getConcurrency();
    }
}
