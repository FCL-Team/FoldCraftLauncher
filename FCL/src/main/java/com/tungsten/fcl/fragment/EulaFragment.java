package com.tungsten.fcl.fragment;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.core.text.method.LinkMovementMethodCompat;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.SplashActivity;
import com.tungsten.fcl.util.LegalAgreement;
import com.tungsten.fcllibrary.component.FCLFragment;
import com.tungsten.fcllibrary.component.theme.ThemeData;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.IOException;
import java.util.logging.Level;

/**
 * 用户协议与隐私政策确认页：启动流程的第一步（首次启动或协议版本更新后显示）。
 * 同意前不申请权限、不联网；同意后交回 {@link SplashActivity} 继续权限申请与初始化，不同意则退出。
 */
public class EulaFragment extends FCLFragment implements View.OnClickListener {

    private final LegalAgreement.Document[] documents = LegalAgreement.Document.values();
    /** 与 documents 一一对应的文档内容，加载完成前为 null */
    private final String[] contents = new String[documents.length];

    private FCLProgressBar progressBar;
    private FCLTabLayout tabLayout;
    private ScrollView scrollView;
    private FCLTextView content;
    private FCLButton agree;
    private FCLButton disagree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eula, container, false);

        progressBar = findViewById(view, R.id.progress);
        tabLayout = findViewById(view, R.id.tab_layout);
        scrollView = findViewById(view, R.id.eula_parent);
        content = findViewById(view, R.id.eula);
        agree = findViewById(view, R.id.next);
        disagree = findViewById(view, R.id.disagree);

        agree.setOnClickListener(this);
        disagree.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showDocument(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadDocuments();

        return view;
    }

    private void loadDocuments() {
        Context context = requireContext();
        new Thread(() -> {
            String[] loaded = new String[documents.length];
            for (int i = 0; i < documents.length; i++) {
                try {
                    loaded[i] = LegalAgreement.load(context, documents[i]);
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to load legal document " + documents[i], e);
                    loaded[i] = context.getString(R.string.legal_load_failed);
                }
            }
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    if (getView() == null)
                        return;
                    System.arraycopy(loaded, 0, contents, 0, contents.length);
                    progressBar.setVisibility(View.GONE);
                    agree.setEnabled(true);
                    showDocument(tabLayout.getSelectedTabPosition());
                });
            }
        }, "Legal Document Loader").start();
    }

    private void showDocument(int position) {
        if (position < 0 || position >= contents.length || contents[position] == null)
            return;
        content.setText(contents[position]);
        Linkify.addLinks(content, Linkify.WEB_URLS);
        content.setMovementMethod(LinkMovementMethodCompat.getInstance());
        // 正文容器被刷成主题色（见 fragment_eula.xml 的 auto_text_background_tint），
        // 链接不能再用主色：背景亮（正文黑）时取压暗变体、背景暗（正文白）时取提亮变体，
        // 与背景和正文同时拉开差距
        ThemeData theme = ThemeEngine.getInstance().getTheme();
        boolean lightBackground = ColorUtils.calculateLuminance(theme.getColor()) >= 0.5;
        content.setLinkTextColor(lightBackground ? theme.getOpaqueDkColor() : theme.getOpaqueLtColor());
        scrollView.scrollTo(0, 0);
    }

    @Override
    public void onClick(View view) {
        if (!(getActivity() instanceof SplashActivity))
            return;
        SplashActivity activity = (SplashActivity) getActivity();
        if (view == agree) {
            LegalAgreement.accept(activity);
            activity.onLegalAgreementAccepted();
        } else if (view == disagree) {
            activity.finish();
        }
    }
}
