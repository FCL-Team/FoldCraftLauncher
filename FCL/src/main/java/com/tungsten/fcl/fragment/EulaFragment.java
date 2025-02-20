package com.tungsten.fcl.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.SplashActivity;
import com.tungsten.fcllibrary.component.FCLFragment;
import com.tungsten.fcllibrary.component.view.FCLButton;

public class EulaFragment extends FCLFragment implements View.OnClickListener {

    // def: https://gitcode.net/fcl-team/fold-craft-launcher/-/raw/master/res/eula.txt?inline=false
    public static final String EULA_URL = "https://www.mcio.dev/docs/rules";
    private WebView eula;

    private FCLButton next;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eula, container, false);

        eula = findViewById(view, R.id.eula);

        next = findViewById(view, R.id.next);
        next.setOnClickListener(this);

        loadEula();

        return view;
    }

    private void loadEula() {
        eula.loadUrl(EULA_URL);
    }

    @Override
    public void onClick(View view) {
        if (view == next) {
            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstLaunch", false);
                editor.apply();
                ((SplashActivity) getActivity()).start();
            }
        }
    }
}
