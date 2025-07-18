package com.tungsten.fcl.ui.version;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.mio.util.AnimUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class ProfileListAdapter extends FCLAdapter {

    private ObservableList<Profile> list;

    public ProfileListAdapter(Context context, ObservableList<Profile> list) {
        super(context);
        this.list = list;
    }

    static class ViewHolder {
        ConstraintLayout parent;
        FCLTextView name;
        FCLTextView path;
        FCLImageButton delete;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_profile, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.path = view.findViewById(R.id.path);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Profile profile = list.get(i);
        viewHolder.parent.setBackground(profile == Profiles.getSelectedProfile() ? getContext().getDrawable(R.drawable.bg_container_transparent_selected) : getContext().getDrawable(R.drawable.bg_container_transparent_clickable));
        viewHolder.name.setText(profile.getName());
        viewHolder.path.setText(profile.getGameDir().getAbsolutePath());
        viewHolder.path.setSelected(true);
        viewHolder.parent.setOnClickListener(view1 -> {
            if (!MainActivity.getInstance().isVersionLoading()) {
                Profiles.setSelectedProfile(profile);
                notifyDataSetChanged();
            } else {
                playAnim(viewHolder.parent);
            }
        });
        viewHolder.delete.setOnClickListener(view1 -> {
            if (Profiles.getProfiles().size() == 1) {
                playAnim(viewHolder.parent);
                return;
            }
            Profiles.getProfiles().remove(profile);
            ((VersionListPage) VersionPageManager.getInstance().getAllPages().get(0)).refreshProfile();
        });
        return view;
    }

    private void playAnim(View v) {
        ObjectAnimator animator = AnimUtil.playTranslationX(v, 700, 0f, 50f, -50f, 50f, -50f, 0f);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }
}
