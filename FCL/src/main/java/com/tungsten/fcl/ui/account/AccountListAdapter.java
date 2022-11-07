package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class AccountListAdapter extends FCLAdapter {

    private final ObservableList<Account> list;

    public AccountListAdapter(Context context, ObservableList<Account> list) {
        super(context);
        this.list = list;
    }

    static class ViewHolder {
        FCLRadioButton radioButton;
        AppCompatImageView avatar;
        FCLTextView name;
        FCLTextView type;
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
            viewHolder.radioButton = view.findViewById(R.id.radio);
            viewHolder.avatar = view.findViewById(R.id.avatar);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.type = view.findViewById(R.id.type);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Account account = list.get(i);
        viewHolder.radioButton.setChecked(account == Accounts.getSelectedAccount());
        viewHolder.avatar.setBackground(TexturesLoader.avatarBinding(account, ConvertUtils.dip2px(getContext(), 30f)).get());
        viewHolder.name.setText(account.getUsername());
        viewHolder.type.setText(Accounts.getLocalizedLoginTypeName(getContext(), Accounts.getAccountFactory(account)));
        viewHolder.radioButton.setOnClickListener(view1 -> {
            Accounts.setSelectedAccount(account);
            UIManager.getInstance().getAccountUI().refresh();
        });
        viewHolder.delete.setOnClickListener(view1 -> {
            Accounts.getAccounts().remove(account);
            UIManager.getInstance().getAccountUI().refresh();
        });
        return view;
    }
}
