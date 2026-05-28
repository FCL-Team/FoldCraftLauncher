package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AccountUI extends FCLCommonUI implements View.OnClickListener {

    private LinearLayoutCompat addOfflineAccount;
    private LinearLayoutCompat addMicrosoftAccount;
    private LinearLayoutCompat addLoginServer;

    private RecyclerView recyclerView;
    private AccountListAdapter accountListAdapter;

    public AccountUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        addOfflineAccount = findViewById(R.id.offline);
        addMicrosoftAccount = findViewById(R.id.microsoft);
        addLoginServer = findViewById(R.id.add_login_server);
        addOfflineAccount.setOnClickListener(this);
        addMicrosoftAccount.setOnClickListener(this);
        addLoginServer.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ListView serverListView = findViewById(R.id.server_list);
        serverListView.setAdapter(new ServerListAdapter(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        addLoadingCallback(() -> refresh().start());
    }

    @Override
    public Task<?> refresh(Object... param) {
        addLoadingCallback(() -> {
            ArrayList<AccountListItem> accountList = Accounts.getAccounts().stream()
                    .map(account -> new AccountListItem(getContext(), account))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (accountListAdapter == null) {
                accountListAdapter = new AccountListAdapter(getContext(), accountList);
                recyclerView.setAdapter(accountListAdapter);
            } else {
                accountListAdapter.refresh(accountList);
            }
        });
        return Task.runAsync(() -> {

        });
    }

    @Override
    public void onClick(View view) {
        if (view == addOfflineAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_OFFLINE);
            dialog.show();
        }
        if (view == addMicrosoftAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_MICROSOFT);
            dialog.show();
        }
        if (view == addLoginServer) {
            AddAuthlibInjectorServerDialog dialog = new AddAuthlibInjectorServerDialog(getContext());
            dialog.show();
        }
    }

}
