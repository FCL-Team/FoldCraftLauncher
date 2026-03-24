package com.tungsten.fcl.ui.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
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
        FCLImageButton move;
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

    @SuppressLint("UseCompatLoadingForDrawables")
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
            viewHolder.move = view.findViewById(R.id.move);
            viewHolder.refresh = view.findViewById(R.id.refresh);
            viewHolder.skin = view.findViewById(R.id.skin);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        AccountListItem account = list.get(i);
        viewHolder.radioButton.setChecked(account.getAccount() == Accounts.getSelectedAccount());
        viewHolder.avatar.imageProperty().unbind();
        viewHolder.avatar.imageProperty().bind(account.imageProperty());
        viewHolder.name.stringProperty().unbind();
        viewHolder.name.stringProperty().bind(account.titleProperty());
        viewHolder.name.setSelected(true);
        viewHolder.type.stringProperty().unbind();
        viewHolder.type.stringProperty().bind(account.subtitleProperty());
        viewHolder.type.setSelected(true);
        viewHolder.skin.setVisibility(account.canUploadSkin().get() ? View.VISIBLE : View.GONE);
        viewHolder.radioButton.setOnClickListener(v -> {
            Accounts.setSelectedAccount(account.getAccount());
            UIManager.getInstance().getAccountUI().refresh().start();
        });
        viewHolder.move.imageProperty().bind(Bindings.createObjectBinding(() -> account.getAccount().isPortable() ? getContext().getDrawable(R.drawable.ic_baseline_earth_24) : getContext().getDrawable(R.drawable.ic_baseline_output_24), account.getAccount().portableProperty()));
        viewHolder.move.setOnClickListener(v -> {
            Account acc = account.getAccount();
            Accounts.getAccounts().remove(acc);
            if (acc.isPortable()) {
                acc.setPortable(false);
                if (!Accounts.getAccounts().contains(acc))
                    Accounts.getAccounts().add(acc);
            } else {
                acc.setPortable(true);
                if (!Accounts.getAccounts().contains(acc)) {
                    int idx = 0;
                    for (int j = Accounts.getAccounts().size() - 1; j >= 0; j--) {
                        if (Accounts.getAccounts().get(j).isPortable()) {
                            idx = j + 1;
                            break;
                        }
                    }
                    Accounts.getAccounts().add(idx, acc);
                }
            }
        });
        viewHolder.refresh.setOnClickListener(v -> {
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
                        account.refreshSkinBinding();
                        UIManager.getInstance().getAccountUI().refresh().start();
                    })
                    .start();
        });
        viewHolder.skin.setOnClickListener(v -> {
            try {
                if (account.getAccount() instanceof AuthlibInjectorAccount) {
                    new Thread(() -> {
                        try {
                            Task<?> uploadTask = Objects.requireNonNull(account.uploadSkin()).get();
                            Schedulers.androidUIThread().execute(() -> {
                                if (uploadTask != null) {
                                    viewHolder.skin.setVisibility(View.GONE);
                                    viewHolder.skinProgress.setVisibility(View.VISIBLE);
                                    uploadTask
                                            .whenComplete(Schedulers.androidUIThread(), ex -> {
                                                viewHolder.skin.setVisibility(View.VISIBLE);
                                                viewHolder.skinProgress.setVisibility(View.GONE);
                                                account.refreshSkinBinding();
                                            })
                                            .start();
                                }
                            });
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else if (account.getAccount() instanceof OfflineAccount) {
                    OfflineAccountSkinDialog dialog = new OfflineAccountSkinDialog(getContext(), account);
                    dialog.show();
                } else {
                    Task<?> uploadTask = Objects.requireNonNull(account.uploadSkin()).get();
                    if (uploadTask != null) {
                        viewHolder.skin.setVisibility(View.GONE);
                        viewHolder.skinProgress.setVisibility(View.VISIBLE);
                        uploadTask
                                .whenComplete(Schedulers.androidUIThread(), ex -> {
                                    viewHolder.skin.setVisibility(View.VISIBLE);
                                    viewHolder.skinProgress.setVisibility(View.GONE);
                                    account.refreshSkinBinding();
                                })
                                .start();
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        viewHolder.delete.setOnClickListener(v -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(String.format(getContext().getString(R.string.version_manage_remove_confirm), account.getTitle()));
            builder.setPositiveButton(() -> {
                account.remove();
                UIManager.getInstance().getAccountUI().refresh().start();
            });
            builder.setNegativeButton(null);
            builder.create().show();
        });
        return view;
    }
}
