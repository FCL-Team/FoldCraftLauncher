package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.List;

/**
 * 单选目标控件组对话框（跨组复制/移动、控件组合并共用）。
 * 点击列表项即选定并回调，"取消"关闭。
 */
public class SelectTargetGroupDialog extends FCLDialog {

    public interface Callback {
        void onPick(ControlViewGroup target);
    }

    public SelectTargetGroupDialog(@NonNull Context context, List<ControlViewGroup> candidates, @NonNull Callback callback) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_select_target_group);
        if (getWindow() != null) {
            getWindow().setLayout(ConvertUtils.dip2px(context, 360), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ListView listView = findViewById(R.id.list);
        FCLButton negative = findViewById(R.id.negative);
        negative.setOnClickListener(v -> dismiss());

        listView.setAdapter(new ArrayAdapter<ControlViewGroup>(context, 0, candidates) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                FCLTextView view = (FCLTextView) convertView;
                if (view == null) {
                    view = new FCLTextView(context);
                    view.setPadding(ConvertUtils.dip2px(context, 12), ConvertUtils.dip2px(context, 12),
                            ConvertUtils.dip2px(context, 12), ConvertUtils.dip2px(context, 12));
                }
                view.setText(candidates.get(position).getName());
                return view;
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            dismiss();
            callback.onPick(candidates.get(position));
        });
    }
}
