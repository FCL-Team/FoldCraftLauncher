package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HelpPage extends FCLCommonPage implements View.OnClickListener {

    public static final String DOC_INDEX_URL = "https://raw.githubusercontent.com/FCL-Team/FCL-Docs/main/index.json";

    private ListView categoryListView;
    private ListView listView;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;
    private FCLButton refresh;
    private FCLButton website;

    public HelpPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        categoryListView = findViewById(R.id.category_list);
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        retry.setOnClickListener(this);

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        website = findViewById(R.id.website);
        website.setOnClickListener(this);

        refresh();
    }

    private void setLoading(boolean loading, boolean... arg) {
        Schedulers.androidUIThread().execute(() -> {
            refresh.setEnabled(!loading);
            retry.setEnabled(!loading);
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            if (loading) {
                retry.setVisibility(View.GONE);
                categoryListView.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
            } else {
                if (arg.length > 0) {
                    boolean success = arg[0];
                    retry.setVisibility(success ? View.GONE : View.VISIBLE);
                    categoryListView.setVisibility(success ? View.VISIBLE : View.GONE);
                    listView.setVisibility(success ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void refresh() {
        setLoading(true);
        Task.supplyAsync(() -> {
            String res = NetworkUtils.doGet(NetworkUtils.toURL(DOC_INDEX_URL));
            return JsonUtils.GSON.fromJson(res, new TypeToken<ArrayList<DocIndex>>() {
            }.getType());
        }).thenAcceptAsync(Schedulers.androidUIThread(), res -> {
            ArrayList<DocIndex> indexes = (ArrayList<DocIndex>) res;
            DocCategoryAdapter adapter = new DocCategoryAdapter(getContext(), indexes.stream().filter(DocIndex::isVisible).collect(Collectors.toList()));
            categoryListView.setAdapter(adapter);
            showArticles(adapter.getSelectedIndex());
            adapter.selectedIndexProperty().addListener(i -> showArticles(adapter.getSelectedIndex()));
        }).whenComplete(Schedulers.androidUIThread(), e -> setLoading(false, e == null)).start();
    }

    private void showArticles(DocIndex index) {
        Schedulers.androidUIThread().execute(() -> {
            ArrayList<DocIndex.Item> items = new ArrayList<>();
            if (index != null) {
                items = (ArrayList<DocIndex.Item>) index.getItem().stream().filter(it -> it.isVisible(getContext())).collect(Collectors.toList());
            }
            ArticleAdapter adapter = new ArticleAdapter(getContext(), items);
            listView.setAdapter(adapter);
        });
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == retry || v == refresh) {
            refresh();
        }
        if (v == website) {
            AndroidUtils.openLink(getContext(), "https://fcl-team.github.io/pages/documentation.html");
        }
    }
}
