package com.tungsten.fcl.setting;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

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
import com.tungsten.fclcore.auth.authlibinjector.SimpleAuthlibInjectorArtifactProvider;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccountFactory;
import com.tungsten.fclcore.auth.microsoft.MicrosoftService;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.auth.offline.OfflineAccountFactory;
import com.tungsten.fclcore.auth.yggdrasil.RemoteAuthenticationException;
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilAccount;
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilAccountFactory;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.InvocationDispatcher;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.skin.InvalidSkinException;

public final class Accounts {
    private Accounts() {}

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
    public static final YggdrasilAccountFactory FACTORY_MOJANG = YggdrasilAccountFactory.MOJANG;
    public static final AuthlibInjectorAccountFactory FACTORY_AUTHLIB_INJECTOR = new AuthlibInjectorAccountFactory(AUTHLIB_INJECTOR_DOWNLOADER, Accounts::getOrCreateAuthlibInjectorServer);
    public static final MicrosoftAccountFactory FACTORY_MICROSOFT = new MicrosoftAccountFactory(new MicrosoftService(OAUTH_CALLBACK));
    public static final List<AccountFactory<?>> FACTORIES = immutableListOf(FACTORY_OFFLINE, FACTORY_MOJANG, FACTORY_MICROSOFT, FACTORY_AUTHLIB_INJECTOR);

    // ==== login type / account factory mapping ====
    private static final Map<String, AccountFactory<?>> type2factory = new HashMap<>();
    private static final Map<AccountFactory<?>, String> factory2type = new HashMap<>();
    static {
        type2factory.put("offline", FACTORY_OFFLINE);
        type2factory.put("yggdrasil", FACTORY_MOJANG);
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
        else if (account instanceof YggdrasilAccount)
            return FACTORY_MOJANG;
        else if (account instanceof MicrosoftAccount)
            return FACTORY_MICROSOFT;
        else
            throw new IllegalArgumentException("Failed to determine account type: " + account);
    }

    private static final String GLOBAL_PREFIX = "$GLOBAL:";
    private static final ObservableList<Map<Object, Object>> globalAccountStorages = FXCollections.observableArrayList();

    private static final ObservableList<Account> accounts = observableArrayList(account -> new Observable[] { account });
    private static final ObjectProperty<Account> selectedAccount = new SimpleObjectProperty<>(Accounts.class, "selectedAccount");

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

        for (Account account : accounts) {
            Map<Object, Object> storage = getAccountStorage(account);
            if (account.isPortable())
                portable.add(storage);
            else
                global.add(storage);
        }

        if (!global.equals(globalAccountStorages))
            globalAccountStorages.setAll(global);
        if (!portable.equals(config().getAccountStorages()))
            config().getAccountStorages().setAll(portable);
    }

    @SuppressWarnings("unchecked")
    private static void loadGlobalAccountStorages() {
        Path globalAccountsFile = new File(FCLPath.FILES_DIR, "accounts.json").toPath();
        if (Files.exists(globalAccountsFile)) {
            try (Reader reader = Files.newBufferedReader(globalAccountsFile)) {
                globalAccountStorages.setAll((List<Map<Object, Object>>)
                        Config.CONFIG_GSON.fromJson(reader, new TypeToken<List<Map<Object, Object>>>() {
                        }.getType()));
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

        globalAccountStorages.addListener(onInvalidating(() ->
                dispatcher.accept(Config.CONFIG_GSON.toJson(globalAccountStorages))));
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
            throw new IllegalStateException("Already initialized");

        loadGlobalAccountStorages();

        // load accounts
        Account selected = null;
        for (Map<Object, Object> storage : config().getAccountStorages()) {
            Account account = parseAccount(storage);
            if (account != null) {
                account.setPortable(true);
                accounts.add(account);
                if (Boolean.TRUE.equals(storage.get("selected"))) {
                    selected = account;
                }
            }
        }

        for (Map<Object, Object> storage : globalAccountStorages) {
            Account account = parseAccount(storage);
            if (account != null) {
                accounts.add(account);
            }
        }

        String selectedAccountIdentifier = config().getSelectedAccount();
        if (selected == null && selectedAccountIdentifier != null) {
            boolean portable = true;
            if (selectedAccountIdentifier.startsWith(GLOBAL_PREFIX)) {
                portable = false;
                selectedAccountIdentifier = selectedAccountIdentifier.substring(GLOBAL_PREFIX.length());
            }

            for (Account account : accounts) {
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

        if (selected == null && !accounts.isEmpty()) {
            selected = accounts.get(0);
        }

        selectedAccount.set(selected);

        InvalidationListener listener = o -> {
            // this method first checks whether the current selection is valid
            // if it's valid, the underlying storage will be updated
            // otherwise, the first account will be selected as an alternative(or null if accounts is empty)
            Account account = selectedAccount.get();
            if (accounts.isEmpty()) {
                if (account == null) {
                    // valid
                } else {
                    // the previously selected account is gone, we can only set it to null here
                    selectedAccount.set(null);
                }
            } else {
                if (accounts.contains(account)) {
                    // valid
                } else {
                    // the previously selected account is gone
                    selectedAccount.set(accounts.get(0));
                }
            }
        };
        selectedAccount.addListener(listener);
        selectedAccount.addListener(onInvalidating(() -> {
            Account account = selectedAccount.get();
            if (account != null)
                config().setSelectedAccount(account.isPortable() ? account.getIdentifier() : GLOBAL_PREFIX + account.getIdentifier());
            else
                config().setSelectedAccount(null);
        }));
        accounts.addListener(listener);
        accounts.addListener(onInvalidating(Accounts::updateAccountStorages));

        initialized = true;

        config().getAuthlibInjectorServers().addListener(onInvalidating(Accounts::removeDanglingAuthlibInjectorAccounts));

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

    public static ObservableList<Account> getAccounts() {
        return accounts;
    }

    public static Account getSelectedAccount() {
        return selectedAccount.get();
    }

    public static void setSelectedAccount(Account selectedAccount) {
        Accounts.selectedAccount.set(selectedAccount);
    }

    public static ObjectProperty<Account> selectedAccountProperty() {
        return selectedAccount;
    }

    // ==== authlib-injector ====
    private static AuthlibInjectorArtifactProvider createAuthlibInjectorArtifactProvider() {
        String authlibinjectorLocation = FCLPath.AUTHLIB_INJECTOR_PATH;
        if (!new File(authlibinjectorLocation).exists()) {
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
        } else {
            LOG.info("Using specified authlib-injector: " + authlibinjectorLocation);
            return new SimpleAuthlibInjectorArtifactProvider(Paths.get(authlibinjectorLocation));
        }
    }

    private static AuthlibInjectorServer getOrCreateAuthlibInjectorServer(String url) {
        return config().getAuthlibInjectorServers().stream()
                .filter(server -> url.equals(server.getUrl()))
                .findFirst()
                .orElseGet(() -> {
                    AuthlibInjectorServer server = new AuthlibInjectorServer(url);
                    config().getAuthlibInjectorServers().add(server);
                    return server;
                });
    }

    /**
     * After an {@link AuthlibInjectorServer} is removed, the associated accounts should also be removed.
     * This method performs a check and removes the dangling accounts.
     */
    private static void removeDanglingAuthlibInjectorAccounts() {
        accounts.stream()
                .filter(AuthlibInjectorAccount.class::isInstance)
                .map(AuthlibInjectorAccount.class::cast)
                .filter(it -> !config().getAuthlibInjectorServers().contains(it.getServer()))
                .collect(toList())
                .forEach(accounts::remove);
    }
    // ====

    // ==== Login type name ===
    private static final Map<AccountFactory<?>, Integer> unlocalizedLoginTypeNames = mapOf(
            pair(Accounts.FACTORY_OFFLINE, R.string.account_methods_offline),
            pair(Accounts.FACTORY_MOJANG, R.string.account_methods_yggdrasil),
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
            return context.getString(R.string.account_failed_connect_authentication_server);
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
            } else {
                return context.getString(R.string.account_methods_microsoft_error_unknown);
            }
        } else if (exception instanceof MicrosoftService.NoMinecraftJavaEditionProfileException) {
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
