package com.tungsten.fcl.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.SplashActivity;
import com.tungsten.fcl.activity.compose.EulaScreenKt;
import com.tungsten.fcl.activity.compose.EulaStateHolder;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fcllibrary.component.FCLFragment;

import java.io.IOException;

/**
 * 用户协议页（EULA）。UI 已迁移 Compose/Miuix（activity/compose/EulaScreen.kt，
 * 经 LegacyBridge.createComposeView 嵌入）；本类保留宿主逻辑：eula.txt 异步加载、
 * 「下一步」写 isFirstLaunch 并推进 SplashActivity.start()。
 * 旧 fragment_eula.xml 已随 Compose 固化删除。
 */
public class EulaFragment extends FCLFragment {

    private final EulaStateHolder holder = new EulaStateHolder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadEula();
        return EulaScreenKt.createEulaView(requireContext(), holder, this::onNext);
    }

    private void loadEula() {
        new Thread(() -> {
            String str;
            try {
                str = IOUtils.readFullyAsString(requireActivity().getAssets().open("eula.txt"));
            } catch (IOException e) {
                e.printStackTrace();
                str = getString(R.string.splash_eula_error);
            }
            final String s = str;
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> holder.setEulaText(s));
            }
        }).start();
    }

    private void onNext() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.apply();
            ((SplashActivity) getActivity()).start();
        }
    }
}
