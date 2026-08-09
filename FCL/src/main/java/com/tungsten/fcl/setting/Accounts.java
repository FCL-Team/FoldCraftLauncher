/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.setting;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.util.Lang.immutableListOf;
import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fclcore.util.Pair.pair;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.R;
import com.tungsten.fcl.game.OAuthServer;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AccountFactory;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.CharacterDeletedException;
import com.tungsten.fclcore.auth.NoCharacterException;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fclcore.auth.ServerDisconnectException;
import com.tungsten.fclcore.auth.ServerResponseMalformedException;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccountFactory;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorArtifactInfo;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorArtifactProvider;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorDownloadException;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorDownloader;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.auth.authlibinjector.BoundAuthlibInjectorAccountFactory;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccountFactory;
import com.tungsten.fclcore.auth.microsoft.MicrosoftService;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.auth.offline.OfflineAccountFactory;
import com.tungsten.fclcore.auth.yggdrasil.RemoteAuthenticationException;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.InvocationDispatcher;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.skin.InvalidSkinException;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public final class Accounts {
    private Accounts() {
    }

    private static final AuthlibInjectorArtifactProvider AUTHLIB_INJECTOR_DOWNLOADER = createAuthlibInjectorArtifactProvider();

    private static void triggerAuthlibInjectorUpdateCheck() {
        if (AUTHLIB_INJECTOR_DOWNLOADER instanceof AuthlibInjectorDownloader) {
            Schedulers.io().execute(() -> {
                try {
                    ((AuthlibInjectorDownloader) AUTHLIB_INJECTOR_DOWNLOADER).checkUpdate();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to check update for authlib-injector", e);
                }
            });
        }
    }

    public static final OAuthServer.Factory OAUTH_CALLBACK = new OAuthServer.Factory();

    public static final OfflineAccountFactory FACTORY_OFFLINE = new OfflineAccountFactory(AUTHLIB_INJECTOR_DOWNLOADER);
    public static final AuthlibInjectorAccountFactory FACTORY_AUTHLIB_INJECTOR = new AuthlibInjectorAccountFactory(AUTHLIB_INJECTOR_DOWNLOADER, Accounts::getOrCreateAuthlibInjectorServer);
    public static final MicrosoftAccountFactory FACTORY_MICROSOFT = new MicrosoftAccountFactory(new MicrosoftService(OAUTH_CALLBACK));
    public static final List<AccountFactory<?>> FACTORIES = immutableListOf(FACTORY_OFFLINE, FACTORY_MICROSOFT, FACTORY_AUTHLIB_INJECTOR);

    // ==== login type / account factory mapping ====
    private static final Map<String, AccountFactory<?>> type2factory = new HashMap<>();
    private static final Map<AccountFactory<?>, String> factory2type = new HashMap<>();

    static {
        type2factory.put("offline", FACTORY_OFFLINE);
        type2factory.put("authlibInjector", FACTORY_AUTHLIB_INJECTOR);
        type2factory.put("microsoft", FACTORY_MICROSOFT);

        type2factory.forEach((type, factory) -> factory2type.put(factory, type));
    }

    public static String getLoginType(AccountFactory<?> factory) {
        String type = factory2type.get(factory);
        if (type != null) return type;

        if (factory instanceof BoundAuthlibInjectorAccountFactory) {
            return factory2type.get(FACTORY_AUTHLIB_INJECTOR);
        }

        throw new IllegalArgumentException("Unrecognized account factory");
    }

    public static AccountFactory<?> getAccountFactory(String loginType) {
        return Optional.ofNullable(type2factory.get(loginType))
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized login type"));
    }

    public static BoundAuthlibInjectorAccountFactory getAccountFactoryByAuthlibInjectorServer(AuthlibInjectorServer server) {
        return new BoundAuthlibInjectorAccountFactory(AUTHLIB_INJECTOR_DOWNLOADER, server);
    }
    // ====

    public static AccountFactory<?> getAccountFactory(Account account) {
        if (account instanceof OfflineAccount)
            return FACTORY_OFFLINE;
        else if (account instanceof AuthlibInjectorAccount)
            return FACTORY_AUTHLIB_INJECTOR;
        else if (account instanceof MicrosoftAccount)
            return FACTORY_MICROSOFT;
        else
            throw new IllegalArgumentException("Failed to determine account type: " + account);
    }

    private static final String GLOBAL_PREFIX = "$GLOBAL:";

    // 阶段 4a：列表/选中项已 StateFlow 化。元素冒泡（extractor 语义）由对每个账户
    // revisionFlow 的直接订阅承接：账户内部任何变更 → accountsSignal 递增 →
    // 存盘（updateAccountStorages）+ 选中项校验 + UI 刷新，触发时机与原 wasUpdated 一致；
    // 账户移出列表时取消订阅。
    private static final MutableStateFlow<List<Map<Object, Object>>> globalAccountStorages = StateFlowKt.MutableStateFlow(new ArrayList<>());

    private static final MutableStateFlow<List<Account>> accounts = StateFlowKt.MutableStateFlow(new ArrayList<>());

    /** 任何账户列表变化（成员增删或元素内部变更）时递增的信号流（供 UI 刷新）。 */
    private static final MutableStateFlow<Long> accountsSignal = StateFlowKt.MutableStateFlow(0L);

    private static final MutableStateFlow<Account> selectedAccount = StateFlowKt.MutableStateFlow(null);

    private static final Map<Account, FlowSubscriptions.Subscription> accountSubscriptions = new IdentityHashMap<>();

    private static void bumpAccountsSignal() {
        accountsSignal.setValue(accountsSignal.getValue() + 1);
    }

    private static void attachAccountSubscription(Account account) {
        if (accountSubscriptions.containsKey(account))
            return;
        accountSubscriptions.put(account,
                FlowSubscriptions.subscribe(account.revisionFlow(), revision -> bumpAccountsSignal()));
    }

    private static void detachAccountSubscription(Account account) {
        FlowSubscriptions.Subscription subscription = accountSubscriptions.remove(account);
        if (subscription != null)
            subscription.cancel();
    }

    private static void addAccountInternal(Account account) {
        List<Account> newList = new ArrayList<>(accounts.getValue());
        newList.add(account);
        attachAccountSubscription(account);
        accounts.setValue(newList);
        bumpAccountsSignal();
    }

    private static void addAccountInternal(int index, Account account) {
        List<Account> newList = new ArrayList<>(accounts.getValue());
        newList.add(index, account);
        attachAccountSubscription(account);
        accounts.setValue(newList);
        bumpAccountsSignal();
    }

    private static void removeAccountInternal(Account account) {
        List<Account> newList = new ArrayList<>(accounts.getValue());
        if (newList.remove(account)) {
            detachAccountSubscription(account);
            accounts.setValue(newList);
            bumpAccountsSignal();
        }
    }

    private static void removeAccountInternal(int index) {
        List<Account> newList = new ArrayList<>(accounts.getValue());
        Account removed = newList.remove(index);
        if (removed != null)
            detachAccountSubscription(removed);
        accounts.setValue(newList);
        bumpAccountsSignal();
    }

    /**
     * True if {@link #init()} hasn't been called.
     */
    private static boolean initialized = false;

    private static Map<Object, Object> getAccountStorage(Account account) {
        Map<Object, Object> storage = account.toStorage();
        storage.put("type", getLoginType(getAccountFactory(account)));
        return storage;
    }

    private static void updateAccountStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized)
            return;
        // update storage

        ArrayList<Map<Object, Object>> global = new ArrayList<>();
        ArrayList<Map<Object, Object>> portable = new ArrayList<>();

        for (Account account : accounts.getValue()) {
            Map<Object, Object> storage = getAccountStorage(account);
            if (account.isPortable())
                portable.add(storage);
            else
                global.add(storage);
        }

        if (!global.equals(globalAccountStorages.getValue()))
            globalAccountStorages.setValue(global);
        if (!portable.equals(config().getAccountStorages()))
            config().setAccountStorages(portable);
    }

    @SuppressWarnings("unchecked")
    private static void loadGlobalAccountStorages() {
        Path globalAccountsFile = new File(FCLPath.FILES_DIR, "accounts.json").toPath();
        if (Files.exists(globalAccountsFile)) {
            try (Reader reader = Files.newBufferedReader(globalAccountsFile)) {
                globalAccountStorages.setValue((List<Map<Object, Object>>)
                        Config.CONFIG_GSON.fromJson(reader, TypeToken.getParameterized(List.class, TypeToken.getParameterized(Map.class, Object.class, Object.class).getType()).getType()));
            } catch (Throwable e) {
                LOG.log(Level.WARNING, "Failed to load global accounts", e);
            }
        }

        InvocationDispatcher<String> dispatcher = InvocationDispatcher.runOn(Lang::thread, json -> {
            LOG.info("Saving global accounts");
            synchronized (globalAccountsFile) {
                try {
                    synchronized (globalAccountsFile) {
                        FileUtils.saveSafely(globalAccountsFile, json);
                    }
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Failed to save global accounts", e);
                }
            }
        });

        FlowSubscriptions.subscribe(globalAccountStorages, list -> {
            try {
                dispatcher.accept(Config.CONFIG_GSON.toJson(list));
            } catch (Throwable ignore) {
            }
        });
    }

    private static Account parseAccount(Map<Object, Object> storage) {
        AccountFactory<?> factory = type2factory.get(storage.get("type"));
        if (factory == null) {
            LOG.warning("Unrecognized account type: " + storage);
            return null;
        }

        try {
            return factory.fromStorage(storage);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to load account: " + storage, e);
            return null;
        }
    }

    /**
     * Called when it's ready to load accounts from {@link ConfigHolder#config()}.
     */
    static void init() {
        if (initialized)
            return;

        loadGlobalAccountStorages();

        // load accounts
        Account selected = null;
        for (Map<Object, Object> storage : config().getAccountStorages()) {
            Account account = parseAccount(storage);
            if (account != null) {
                account.setPortable(true);
                addAccountInternal(account);
                if (Boolean.TRUE.equals(storage.get("selected"))) {
                    selected = account;
                }
            }
        }

        for (Map<Object, Object> storage : globalAccountStorages.getValue()) {
            Account account = parseAccount(storage);
            if (account != null) {
                addAccountInternal(account);
            }
        }

        String selectedAccountIdentifier = config().getSelectedAccount();
        if (selected == null && selectedAccountIdentifier != null) {
            boolean portable = true;
            if (selectedAccountIdentifier.startsWith(GLOBAL_PREFIX)) {
                portable = false;
                selectedAccountIdentifier = selectedAccountIdentifier.substring(GLOBAL_PREFIX.length());
            }

            for (Account account : accounts.getValue()) {
                if (selectedAccountIdentifier.equals(account.getIdentifier())) {
                    if (portable == account.isPortable()) {
                        selected = account;
                        break;
                    } else if (selected == null) {
                        selected = account;
                    }
                }
            }
        }

        if (selected == null && !accounts.getValue().isEmpty()) {
            selected = accounts.getValue().get(0);
        }

        selectedAccount.setValue(selected);

        // 选中项有效性校验（对齐原同时挂在 selectedAccount 与 accounts 上的失效监听）：
        // 选中项变化或列表任何变化（含元素冒泡）时校验，失效则回退到第 0 项（或 null）。
        FlowSubscriptions.subscribe(selectedAccount, account -> validateSelectedAccount());
        FlowSubscriptions.subscribe(selectedAccount, account -> {
            if (account != null)
                config().setSelectedAccount(account.isPortable() ? account.getIdentifier() : GLOBAL_PREFIX + account.getIdentifier());
            else
                config().setSelectedAccount(null);
        });
        FlowSubscriptions.subscribe(accountsSignal, signal -> {
            validateSelectedAccount();
            updateAccountStorages();
        });

        initialized = true;

        FlowSubscriptions.subscribe(config().authlibInjectorServersFlow(), servers -> removeDanglingAuthlibInjectorAccounts());

        if (selected != null) {
            Account finalSelected = selected;
            Schedulers.io().execute(() -> {
                try {
                    finalSelected.logIn();
                } catch (Throwable e) {
                    LOG.log(Level.WARNING, "Failed to log " + finalSelected + " in", e);
                }
            });
        }

        triggerAuthlibInjectorUpdateCheck();

        for (AuthlibInjectorServer server : config().getAuthlibInjectorServers()) {
            if (selected instanceof AuthlibInjectorAccount && ((AuthlibInjectorAccount) selected).getServer() == server)
                continue;
            Schedulers.io().execute(() -> {
                try {
                    server.fetchMetadataResponse();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to fetch authlib-injector server metdata: " + server, e);
                }
            });
        }
    }

    private static void validateSelectedAccount() {
        // this method first checks whether the current selection is valid
        // if it's valid, the underlying storage will be updated
        // otherwise, the first account will be selected as an alternative(or null if accounts is empty)
        Account account = selectedAccount.getValue();
        List<Account> list = accounts.getValue();
        if (list.isEmpty()) {
            if (account != null) {
                // the previously selected account is gone, we can only set it to null here
                selectedAccount.setValue(null);
            }
        } else {
            if (!list.contains(account)) {
                // the previously selected account is gone
                selectedAccount.setValue(list.get(0));
            }
        }
    }

    /** 账户列表快照（只读）；任何变化经 {@link #accountsSignalFlow()} 通知。 */
    public static List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts.getValue());
    }

    /** 账户列表变化信号（成员增删与元素内部变更都会递增）。 */
    public static StateFlow<Long> accountsSignalFlow() {
        return accountsSignal;
    }

    public static Account getSelectedAccount() {
        return selectedAccount.getValue();
    }

    public static void setSelectedAccount(Account selectedAccount) {
        Accounts.selectedAccount.setValue(selectedAccount);
    }

    public static StateFlow<Account> selectedAccountFlow() {
        return selectedAccount;
    }

    public static void addAccount(Account account) {
        int oldIndex = Accounts.getAccounts().indexOf(account);
        if (oldIndex == -1) {
            addAccountInternal(account);
        } else {
            // adding an already-added account
            // instead of discarding the new account, we first remove the existing one then add the new one
            removeAccountInternal(oldIndex);
            addAccountInternal(oldIndex, account);
        }
    }

    public static void removeAccount(Account account) {
        removeAccountInternal(account);
    }

    public static void replaceAccount(UUID uuid, Account account) {
        List<Account> list = Accounts.getAccounts().stream().filter(a -> a.getUUID().equals(uuid)).collect(Collectors.toList());
        if (list.isEmpty()) {
            addAccountInternal(account);
        } else {
            int oldIndex = Accounts.getAccounts().indexOf(list.get(0));
            removeAccountInternal(oldIndex);
            addAccountInternal(oldIndex, account);
        }
    }

    // ==== authlib-injector ====
    private static AuthlibInjectorArtifactProvider createAuthlibInjectorArtifactProvider() {
        String authlibinjectorLocation = FCLPath.AUTHLIB_INJECTOR_PATH;
        return new AuthlibInjectorDownloader(
                new File(authlibinjectorLocation).toPath(),
                DownloadProviders::getDownloadProvider) {
            @Override
            public Optional<AuthlibInjectorArtifactInfo> getArtifactInfoImmediately() {
                Optional<AuthlibInjectorArtifactInfo> local = super.getArtifactInfoImmediately();
                if (local.isPresent()) {
                    return local;
                }
                // search authlib-injector.jar in current directory, it's used as a fallback
                return parseArtifact(Paths.get("authlib-injector.jar"));
            }
        };
    }

    private static AuthlibInjectorServer getOrCreateAuthlibInjectorServer(String url) {
        return config().getAuthlibInjectorServers().stream()
                .filter(server -> url.equals(server.getUrl()))
                .findFirst()
                .orElseGet(() -> {
                    AuthlibInjectorServer server = new AuthlibInjectorServer(url);
                    config().addAuthlibInjectorServer(server);
                    return server;
                });
    }

    /**
     * After an {@link AuthlibInjectorServer} is removed, the associated accounts should also be removed.
     * This method performs a check and removes the dangling accounts.
     */
    private static void removeDanglingAuthlibInjectorAccounts() {
        accounts.getValue().stream()
                .filter(AuthlibInjectorAccount.class::isInstance)
                .map(AuthlibInjectorAccount.class::cast)
                .filter(it -> !config().getAuthlibInjectorServers().contains(it.getServer()))
                .collect(toList())
                .forEach(Accounts::removeAccountInternal);
    }
    // ====

    // ==== Login type name ===
    private static final Map<AccountFactory<?>, Integer> unlocalizedLoginTypeNames = mapOf(
            pair(Accounts.FACTORY_OFFLINE, R.string.account_methods_offline),
            pair(Accounts.FACTORY_AUTHLIB_INJECTOR, R.string.account_methods_authlib_injector),
            pair(Accounts.FACTORY_MICROSOFT, R.string.account_methods_microsoft));

    public static String getLocalizedLoginTypeName(Context context, AccountFactory<?> factory) {
        return context.getString(unlocalizedLoginTypeNames.get(factory));
    }
    // ====

    public static String localizeErrorMessage(Context context, Exception exception) {
        if (exception instanceof NoCharacterException) {
            return context.getString(R.string.account_failed_no_character);
        } else if (exception instanceof ServerDisconnectException) {
            return context.getString(R.string.account_failed_connect_authentication_server) + "\n" + exception;
        } else if (exception instanceof ServerResponseMalformedException) {
            return context.getString(R.string.account_failed_server_response_malformed);
        } else if (exception instanceof RemoteAuthenticationException) {
            RemoteAuthenticationException remoteException = (RemoteAuthenticationException) exception;
            String remoteMessage = remoteException.getRemoteMessage();
            if ("ForbiddenOperationException".equals(remoteException.getRemoteName()) && remoteMessage != null) {
                if (remoteMessage.contains("Invalid credentials")) {
                    return context.getString(R.string.account_failed_invalid_credentials);
                } else if (remoteMessage.contains("Invalid token")) {
                    return context.getString(R.string.account_failed_invalid_token);
                } else if (remoteMessage.contains("Invalid username or password")) {
                    return context.getString(R.string.account_failed_invalid_password);
                } else {
                    return remoteMessage;
                }
            } else if ("ResourceException".equals(remoteException.getRemoteName()) && remoteMessage != null) {
                if (remoteMessage.contains("The requested resource is no longer available")) {
                    return context.getString(R.string.account_failed_migration);
                } else {
                    return remoteMessage;
                }
            }
            return exception.getMessage();
        } else if (exception instanceof AuthlibInjectorDownloadException) {
            return context.getString(R.string.account_failed_injector_download_failure);
        } else if (exception instanceof CharacterDeletedException) {
            return context.getString(R.string.account_failed_character_deleted);
        } else if (exception instanceof InvalidSkinException) {
            return context.getString(R.string.account_skin_invalid_skin);
        } else if (exception instanceof MicrosoftService.XboxAuthorizationException) {
            long errorCode = ((MicrosoftService.XboxAuthorizationException) exception).getErrorCode();
            if (errorCode == MicrosoftService.XboxAuthorizationException.ADD_FAMILY) {
                return context.getString(R.string.account_methods_microsoft_error_add_family);
            } else if (errorCode == MicrosoftService.XboxAuthorizationException.COUNTRY_UNAVAILABLE) {
                return context.getString(R.string.account_methods_microsoft_error_country_unavailable);
            } else if (errorCode == MicrosoftService.XboxAuthorizationException.MISSING_XBOX_ACCOUNT) {
                return context.getString(R.string.account_methods_microsoft_error_missing_xbox_account);
            } else if (errorCode == MicrosoftService.XboxAuthorizationException.BANNED) {
                return context.getString(R.string.account_methods_microsoft_error_banned);
            } else {
                return context.getString(R.string.account_methods_microsoft_error_unknown);
            }
        } else if (exception instanceof MicrosoftService.XBox400Exception) {
            return context.getString(R.string.account_methods_microsoft_error_wrong_verify_method);
        } else if (exception instanceof MicrosoftService.MinecraftJavaEditionLicenseNotFoundException) {
            return context.getString(R.string.account_methods_microsoft_error_no_license);
        } else if (exception instanceof MicrosoftService.MinecraftJavaEditionProfileNotFoundException) {
            return context.getString(R.string.account_methods_microsoft_error_no_character);
        } else if (exception instanceof MicrosoftService.NoXuiException) {
            return context.getString(R.string.account_methods_microsoft_error_add_family_probably);
        } else if (exception instanceof OAuthAccount.WrongAccountException) {
            return context.getString(R.string.account_failed_wrong_account);
        } else if (exception.getClass() == AuthenticationException.class) {
            return exception.getLocalizedMessage();
        } else {
            return exception.getClass().getName() + ": " + exception.getLocalizedMessage();
        }
    }
}
