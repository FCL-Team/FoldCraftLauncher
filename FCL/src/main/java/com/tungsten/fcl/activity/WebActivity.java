package com.tungsten.fcl.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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

        // OAuth 登录流程结束时由登录对话框广播本事件，页面自行退出，无需用户手动返回
        loginFinishedListener = event -> runOnUiThread(this::finish);
        Accounts.OAUTH_CALLBACK.onLoginFinished.registerWeak(loginFinishedListener);
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
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginFinishedListener != null) {
            Accounts.OAUTH_CALLBACK.onLoginFinished.unregister(loginFinishedListener);
        }
//        AndroidUtilKt.clearWebViewCache(this);
    }
}
