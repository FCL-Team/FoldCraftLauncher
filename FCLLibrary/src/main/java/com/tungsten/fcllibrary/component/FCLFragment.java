package com.tungsten.fcllibrary.component;

import android.view.View;

import androidx.fragment.app.Fragment;

public class FCLFragment extends Fragment {

    public final <T extends View> T findViewById(View view, int id) {
        return view.findViewById(id);
    }

}
