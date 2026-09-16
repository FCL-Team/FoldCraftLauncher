package com.tungsten.fcl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.OAuthServer;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcllibrary.component.FCLActivity;

import java.util.function.Consumer;

public class WebActivity extends FCLActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private Consumer<OAuthServer.LoginCompletedDeviceCodeEvent> loginCompletedListener;
    private Consumer<OAuthServer.LoginFinishedEvent> loginFinishedListener;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        progressBar = findViewById(R.id.progress);
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewTrackClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getExtras().getString("url"));

        // 设备码轮询拿到 token 即视为浏览器侧登录完成，立即关闭页面，不等 XBL/profile 整条后台链跑完
        loginCompletedListener = event -> runOnUiThread(this::finish);
        Accounts.OAUTH_CALLBACK.onLoginCompletedDeviceCode.register(loginCompletedListener);
        // 兜底：登录流程终结（成功/失败/取消）时由登录对话框广播本事件，覆盖后台阶段失败等残留页面的关闭
        loginFinishedListener = event -> runOnUiThread(this::finish);
        Accounts.OAUTH_CALLBACK.onLoginFinished.register(loginFinishedListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 复用已存在的页面时加载新地址
        if (intent.getExtras() != null && intent.getExtras().getString("url") != null) {
            webView.loadUrl(intent.getExtras().getString("url"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回键直接关闭页面，避免部分系统将按键交给 WebView 执行浏览历史回退
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class WebViewTrackClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            // 微软登录完成后重定向到本地 OAuthServer 回调地址，该请求已把授权码交给后台流程，
            // 页面使命完成，立即关闭（不能在 shouldOverrideUrlLoading 拦截，否则回调请求发不出去，登录会挂起）
            if (isOAuthCallback(url)) {
                finish();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private static boolean isOAuthCallback(String url) {
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        return "/auth-response".equals(uri.getPath())
                && ("localhost".equals(host) || "127.0.0.1".equals(host));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 强注册（register）的监听必须显式注销，registerWeak 配对的 unregister 实际摘不掉
        Accounts.OAUTH_CALLBACK.onLoginCompletedDeviceCode.unregister(loginCompletedListener);
        Accounts.OAUTH_CALLBACK.onLoginFinished.unregister(loginFinishedListener);
//        AndroidUtilKt.clearWebViewCache(this);
    }
}
