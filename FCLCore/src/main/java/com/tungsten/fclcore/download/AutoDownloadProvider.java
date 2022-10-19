package com.tungsten.fclcore.download;

import java.net.URL;
import java.util.List;

/**
 * Official Download Provider fetches version list from Mojang and
 * download files from mcbbs.
 */
public class AutoDownloadProvider implements DownloadProvider {
    private final DownloadProvider versionListProvider;
    private final DownloadProvider fileProvider;

    public AutoDownloadProvider(DownloadProvider versionListProvider, DownloadProvider fileProvider) {
        this.versionListProvider = versionListProvider;
        this.fileProvider = fileProvider;
    }

    @Override
    public String getVersionListURL() {
        return versionListProvider.getVersionListURL();
    }

    @Override
    public String getAssetBaseURL() {
        return fileProvider.getAssetBaseURL();
    }

    @Override
    public String injectURL(String baseURL) {
        return fileProvider.injectURL(baseURL);
    }

    @Override
    public List<URL> getAssetObjectCandidates(String assetObjectLocation) {
        return fileProvider.getAssetObjectCandidates(assetObjectLocation);
    }

    @Override
    public List<URL> injectURLWithCandidates(String baseURL) {
        return fileProvider.injectURLWithCandidates(baseURL);
    }

    @Override
    public List<URL> injectURLsWithCandidates(List<String> urls) {
        return fileProvider.injectURLsWithCandidates(urls);
    }

    @Override
    public VersionList<?> getVersionListById(String id) {
        return versionListProvider.getVersionListById(id);
    }

    @Override
    public int getConcurrency() {
        return fileProvider.getConcurrency();
    }
}
