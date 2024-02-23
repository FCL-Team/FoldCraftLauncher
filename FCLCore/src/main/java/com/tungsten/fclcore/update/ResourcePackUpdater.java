package com.tungsten.fclcore.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.util.Pair;
import com.tungsten.fclcore.util.io.HttpRequest;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ResourcePackUpdater extends FileDownloadTask {
    public static boolean enable = false;
    private static final String OWNER = "SweetRiceMC";
    private static final String REPO = "ResourcePack";
    private static final String REMOTE_PATH = "/resource.zip";
    private static final String LOCAL_PATH = "/resourcepacks/服务器材质包.zip";
    private static final String LOCAL_SHA1 = "/SweetRiceMC/last_sha1";
    private static final String[] MIRRORS = {
            "https://ghproxy.homeboyc.cn/",
            "https://github.moeyy.cn/",
            "https://ghps.cc/",
            "https://hub.gitmirror.com/",
            "https://gh.ddlc.top/",
            "https://ghproxy.net/",
            "https://ghproxy.com/",
            ""
    };
    private static String localSha1 = "";

    private final Runnable runAfter;
    public ResourcePackUpdater(List<URL> urls, File file, Runnable runAfter) {
        super(urls, file);
        this.runAfter = runAfter;
    }

    @Override
    public void execute() throws Exception {
        super.execute();
        runAfter.run();
    }

    static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (Throwable t) {
            return s;
        }
    }

    public static Pair<Boolean, Runnable>  check(File mcDir) {
        File fileSha1 = new File(mcDir, LOCAL_SHA1);
        File fileLocal = new File(mcDir, LOCAL_PATH);
        if (localSha1.isEmpty()) {
            if (fileSha1.exists()) {
                localSha1 = readAsString(fileSha1);
            }
        }

        try {
            String result = HttpRequest.GET(
                    "https://api.github.com/repos/" + OWNER + "/" + REPO + "/commits?path=" + urlEncode(REMOTE_PATH) + "&per_page=1&page=1"
            ).header("Content-Type", "application/json").getString();
            // 解析接口返回数据
            JsonElement element = JsonParser.parseString(result);
            JsonArray array = element.isJsonArray() ? element.getAsJsonArray() : null;
            if (array == null || array.isEmpty()) {
                return Pair.pair(false, () -> {});
            }
            JsonObject obj = array.get(0).getAsJsonObject();
            String remoteSha1 = obj.get("sha").getAsString();
            if (remoteSha1.equalsIgnoreCase(localSha1)) return Pair.pair(false, () -> {});
            localSha1 = remoteSha1;

            // 下载更新
            if (fileLocal.exists()) FileUtils.deleteQuietly(fileLocal);
            return Pair.pair(true, () -> saveFromString(fileSha1, localSha1));
            //String url = "https://github.com/" + OWNER + "/" + REPO + "/blob/main" + REMOTE_PATH;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Pair.pair(false, () -> {});
    }

    public static List<URL> downloadLinks() {
        List<URL> links = new ArrayList<>();
        List<String> extraLinks = fetchExtraUpdateLinks();
        try {
            for (String extra : extraLinks) {
                links.add(new URL(extra));
            }
            for (String mirror : MIRRORS) {
                links.add(new URL(mirror + "https://github.com/" + OWNER + "/" + REPO + "/blob/main" + REMOTE_PATH));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return links;
    }

    public static String downloadLink() {
        return MIRRORS[0] + "https://github.com/" + OWNER + "/" + REPO + "/blob/main" + REMOTE_PATH;
    }

    static List<String> fetchExtraUpdateLinks() {
        String url = "https://api.github.com/repos/" + OWNER + "/" + REPO + "/contents/metadata.json";
        List<String> list = new ArrayList<>();
        try {
            HttpRequest.HttpGetRequest httpGet = HttpRequest.HttpGetRequest.GET(url);
            httpGet.header("content-type", "application/json");
            String result = httpGet.getString();

            JsonObject json = JsonParser.parseString(result).getAsJsonObject();
            if (json.get("message") != null) {
                throw new IllegalStateException(json.get("message").getAsString());
            }
            String encoding = json.get("encoding").getAsString();
            if (!encoding.equalsIgnoreCase("base64")) throw new IllegalStateException("未知的 encoding=" + encoding);
            String base64 = json.get("content").getAsString().replace("\n", "");
            String content = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            json = JsonParser.parseString(content).getAsJsonObject();

            JsonArray array = json.get("extra_urls").getAsJsonArray();
            for (JsonElement element : array) {
                list.add(element.getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String readAsString(File file) {
        StringBuilder result = new StringBuilder();
        try (
                FileInputStream input = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(reader)) {
            String lineTxt;
            boolean a = false;
            // 逐行读取
            while ((lineTxt = br.readLine()) != null) {
                // 输出内容到控制台
                if (a) result.append("\n");
                else a = true;
                result.append(lineTxt);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void saveFromString(File file, String content) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (Throwable ignored) {
        }
        try (
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            BufferedWriter out = new BufferedWriter(osw);
            out.write(content);
            out.flush();
            out.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}