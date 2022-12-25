package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLConstraintLayout;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AccountListAdapter extends FCLAdapter {

    private final ObservableList<AccountListItem> list;

    public AccountListAdapter(Context context, ObservableList<AccountListItem> list) {
        super(context);
        this.list = list;
    }

    static class ViewHolder {
        FCLConstraintLayout parent;
        FCLRadioButton radioButton;
        FCLImageView avatar;
        FCLTextView name;
        FCLTextView type;
        FCLProgressBar refreshProgress;
        FCLProgressBar skinProgress;
        FCLImageButton refresh;
        FCLImageButton skin;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_account, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.radioButton = view.findViewById(R.id.radio);
            viewHolder.avatar = view.findViewById(R.id.avatar);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.type = view.findViewById(R.id.type);
            viewHolder.refreshProgress = view.findViewById(R.id.refresh_progress);
            viewHolder.skinProgress = view.findViewById(R.id.skin_progress);
            viewHolder.refresh = view.findViewById(R.id.refresh);
            viewHolder.skin = view.findViewById(R.id.skin);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        AccountListItem account = list.get(i);
        viewHolder.radioButton.setChecked(account.getAccount() == Accounts.getSelectedAccount());
        viewHolder.avatar.imageProperty().unbind();
        viewHolder.avatar.imageProperty().bind(account.imageProperty());
        viewHolder.name.stringProperty().unbind();
        viewHolder.name.stringProperty().bind(account.titleProperty());
        viewHolder.type.stringProperty().unbind();
        viewHolder.type.stringProperty().bind(account.subtitleProperty());
        viewHolder.skin.setVisibility(account.canUploadSkin().get() ? View.VISIBLE : View.GONE);
        viewHolder.radioButton.setOnClickListener(view1 -> {
            Accounts.setSelectedAccount(account.getAccount());
            UIManager.getInstance().getAccountUI().refresh().start();
        });
        viewHolder.refresh.setOnClickListener(view1 -> {
            viewHolder.refresh.setVisibility(View.GONE);
            viewHolder.refreshProgress.setVisibility(View.VISIBLE);
            account.refreshAsync()
                    .whenComplete(Schedulers.androidUIThread(), ex -> {
                        viewHolder.refresh.setVisibility(View.VISIBLE);
                        viewHolder.refreshProgress.setVisibility(View.GONE);

                        if (ex != null) {
                            FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                            builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder1.setMessage(Accounts.localizeErrorMessage(getContext(), ex));
                            builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                            builder1.create().show();
                        }

                        UIManager.getInstance().getAccountUI().refresh().start();
                    })
                    .start();
        });
        viewHolder.skin.setOnClickListener(view1 -> {
            try {
                Task<?> uploadTask = Objects.requireNonNull(account.uploadSkin()).get();
                if (uploadTask != null) {
                    viewHolder.skin.setVisibility(View.GONE);
                    viewHolder.skinProgress.setVisibility(View.VISIBLE);
                    uploadTask
                            .whenComplete(Schedulers.androidUIThread(), ex -> {
                                viewHolder.skin.setVisibility(View.VISIBLE);
                                viewHolder.skinProgress.setVisibility(View.GONE);
                            })
                            .start();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        viewHolder.delete.setOnClickListener(view1 -> {
            account.remove();
            UIManager.getInstance().getAccountUI().refresh().start();
        });
        return view;
    }
}
