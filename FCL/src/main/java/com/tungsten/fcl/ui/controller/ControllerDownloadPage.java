package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.download.ControllerIndex;
import com.tungsten.fcl.control.download.ControllerVersion;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class ControllerDownloadPage extends FCLTempPage implements View.OnClickListener {

    private final ArrayList<String> categories;
    private final ControllerIndex index;
    private final String url;

    private FCLLinearLayout layout;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;
    private FCLImageView icon;
    private FCLTextView name;
    private FCLTextView tag;
    private FCLTextView intro;

    private ListView listView;
    private FCLTextView author;
    private FCLTextView version;
    private FCLTextView device;
    private FCLTextView description;

    private FCLButton history;
    private FCLButton latest;

    public ControllerDownloadPage(Context context, int id, FCLUILayout parent, int resId, int source, ArrayList<String> categories, ControllerIndex index) {
        super(context, id, parent, resId);
        this.categories = categories;
        this.index = index;
        this.url = (source == 0 ? ControllerRepoPage.CONTROLLER_GITHUB : ControllerRepoPage.CONTROLLER_GIT_CN) + "repo_json/" + index.getId() + "/";
        create();
    }

    public void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            layout.setVisibility(loading ? View.GONE : View.VISIBLE);
            if (loading) {
                retry.setVisibility(View.GONE);
            }
        });
    }

    public void setFailed() {
        Schedulers.androidUIThread().execute(() -> {
            retry.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        });
    }

    private void refresh() {
        setLoading(true);
        String versionUrl = url + "version.json";
        String screenshotUrl = url + "screenshots/";
        Task.supplyAsync(() -> {
            String versionStr = NetworkUtils.doGet(NetworkUtils.toURL(versionUrl));
            return JsonUtils.GSON.fromJson(versionStr, ControllerVersion.class);
        }).thenAcceptAsync((ExceptionalConsumer<ControllerVersion, Exception>) controllerVersion -> {
            ArrayList<String> screenshotUrls = new ArrayList<>();
            for (int i = 1; i <= controllerVersion.getScreenshot(); i++) {
                String num = i + "";
                if (num.length() == 1)
                    num = "0" + num;
                screenshotUrls.add(screenshotUrl + num + ".png");
            }
            String[] allDevices = new String[] {
                    getContext().getString(R.string.control_download_device_phone),
                    getContext().getString(R.string.control_download_device_pad),
                    getContext().getString(R.string.control_download_device_other)
            };
            ArrayList<String> devices = new ArrayList<>();
            for (int d : index.getDevice()) {
                if (0 <= d && 2 >= d)
                    devices.add(allDevices[d]);
            }
            StringBuilder stringBuilder = new StringBuilder();
            devices.forEach(it -> stringBuilder.append(it).append("  "));
            String deviceText = StringUtils.removeSuffix(stringBuilder.toString(), "  ");
            listView.post(() -> {
                ControllerScreenshotAdapter adapter = new ControllerScreenshotAdapter(getContext(), screenshotUrls);
                listView.setAdapter(adapter);
            });
            author.setText(controllerVersion.getAuthor());
            version.setText(controllerVersion.getLatest().getVersionName());
            device.setText(deviceText);
            description.setText(controllerVersion.getDescription());
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            setLoading(false);
            if (exception != null) {
                setFailed();
            }
        }).start();
    }

    private void create() {
        layout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        tag = findViewById(R.id.tag);
        intro = findViewById(R.id.intro);

        listView = findViewById(R.id.list);
        author = findViewById(R.id.author);
        version = findViewById(R.id.version);
        device = findViewById(R.id.device);
        description = findViewById(R.id.description);

        history = findViewById(R.id.history);
        latest = findViewById(R.id.latest);

        Glide.with(getContext()).load(url + "icon.png").into(icon);
        name.setText(index.getName());
        StringBuilder stringBuilder = new StringBuilder();
        categories.forEach(it -> stringBuilder.append(it).append("   "));
        String tagText = StringUtils.removeSuffix(stringBuilder.toString(), "   ");
        tag.setText(tagText);
        intro.setText(index.getIntroduction());

        retry.setOnClickListener(this);
        history.setOnClickListener(this);
        latest.setOnClickListener(this);

        refresh();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onClick(View view) {
        if (view == retry) {
            refresh();
        }
        if (view == history) {

        }
        if (view == latest) {

        }
    }
}
