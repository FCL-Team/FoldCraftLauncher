package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tungsten.fcl.R;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLPage;

/**
 * 关于页：RecyclerView 行级复用，说明（about_desc）置顶，下方为链接行。
 */
public class AboutPage extends FCLPage {

    private static final int TYPE_DESC = 0;
    private static final int TYPE_LINK = 1;

    private static final String QQ_GROUP_KEY = "9_Mnxe5x1l6L7giLuRYQyBh0iWBgCUbw";

    /** 条目顺序：说明置顶，随后为各链接行 */
    private static final int[] TITLES = {
            R.string.about_desc,
            R.string.about_launcher,
            R.string.about_developer,
            R.string.community_discord,
            R.string.community_qq,
            R.string.about_sponsor,
            R.string.about_source
    };

    public AboutPage(Context context, int id, int resId) {
        super(context, id, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RecyclerView recyclerView = findViewById(R.id.about_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AboutAdapter());
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    private class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.Holder> {

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? TYPE_DESC : TYPE_LINK;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int layout = viewType == TYPE_DESC ? R.layout.item_about_desc : R.layout.item_about;
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false), viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return TITLES.length;
        }

        class Holder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final boolean link;

            Holder(@NonNull View itemView, int viewType) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                link = viewType == TYPE_LINK;
                if (link) {
                    itemView.setOnClickListener(v -> openLink(getBindingAdapterPosition()));
                }
            }

            void bind(int position) {
                title.setText(TITLES[position]);
            }
        }
    }

    private void openLink(int position) {
        switch (position) {
            case 1:
                AndroidUtilKt.openLink(getContext(), "https://fcl-team.github.io/");
                break;
            case 2:
                AndroidUtilKt.openLink(getContext(), "https://github.com/FCL-Team");
                break;
            case 3:
                AndroidUtilKt.openLink(getContext(), "https://discord.gg/ffhvuXTwyV");
                break;
            case 4:
                joinQQGroup(QQ_GROUP_KEY);
                break;
            case 5:
                AndroidUtilKt.openLink(getContext(), "https://afdian.com/@tungs");
                break;
            case 6:
                AndroidUtilKt.openLink(getContext(), "https://github.com/FCL-Team/FoldCraftLauncher");
                break;
        }
    }

    private void joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        try {
            getContext().startActivity(intent);
        } catch (Exception ignored) {
        }
    }
}
