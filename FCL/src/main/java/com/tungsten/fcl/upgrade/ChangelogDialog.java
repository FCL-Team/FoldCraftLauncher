package com.tungsten.fcl.upgrade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

/**
 * 只读的版本更新内容对话框（设置页"查看更新内容"入口）。
 * 与 {@link UpdateDialog} 同一套信息结构，但不含下载/忽略等更新操作。
 */
public class ChangelogDialog extends FCLDialog {

    private View parent;
    private ScrollView scrollView;
    private View layout;

    public ChangelogDialog(@NonNull Context context, RemoteVersion version) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_changelog);

        parent = findViewById(R.id.parent);
        scrollView = findViewById(R.id.text_scroll);
        layout = findViewById(R.id.layout);

        FCLTextView versionName = findViewById(R.id.version);
        FCLTextView date = findViewById(R.id.date);
        FCLTextView type = findViewById(R.id.type);
        FCLTextView description = findViewById(R.id.description);

        versionName.setText(String.format(context.getString(R.string.update_version), version.getVersionName()));
        date.setText(String.format(context.getString(R.string.update_date), version.getDate()));
        type.setText(String.format(context.getString(R.string.update_type), version.getDisplayType(context)));
        description.setText(String.format(context.getString(R.string.update_description), version.getDisplayDescription(context)));

        findViewById(R.id.positive).setOnClickListener(v -> dismiss());

        // 内容不足一屏时窗口收缩到内容高度，超出时限制在屏幕 70% 内滚动
        parent.post(() -> layout.post(() -> {
            int maxHeight = (int) (AndroidUtilKt.getScreenHeight() * 0.7f);
            if (parent.getMeasuredHeight() < maxHeight) {
                ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                layoutParams.height = layout.getMeasuredHeight();
                scrollView.setLayoutParams(layoutParams);
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            } else {
                getWindow().setLayout(ConvertUtils.dip2px(context, 450f), maxHeight);
            }
        }));
    }
}
