package com.tungsten.fcl.setting;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Pair.pair;

import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.auth.OAuth;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.io.NetworkUtils;

import java.util.UUID;

public final class FCLAccounts {

    private static final ObjectProperty<FCLAccount> account = new SimpleObjectProperty<>();

    private FCLAccounts() {
    }

    public static FCLAccount getAccount() {
        return account.get();
    }

    public static ObjectProperty<FCLAccount> accountProperty() {
        return account;
    }

    public static void setAccount(FCLAccount account) {
        FCLAccounts.account.set(account);
    }

    public static Task<Void> login() {
        String nonce = UUIDTypeAdapter.fromUUID(UUID.randomUUID());
        String scope = "openid offline_access";

        return Task.supplyAsync(() -> {
            OAuth.Session session = Accounts.OAUTH_CALLBACK.startServer();
            Accounts.OAUTH_CALLBACK.openBrowser(NetworkUtils.withQuery(
                    "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
                    mapOf(
                            pair("client_id", Accounts.OAUTH_CALLBACK.getClientId()),
                            pair("response_type", "id_token code"),
                            pair("response_mode", "form_post"),
                            pair("scope", scope),
                            pair("redirect_uri", session.getRedirectURI()),
                            pair("nonce", nonce)
                    )));
            String code = session.waitFor();

            // Authorization Code -> Token
            String responseText = HttpRequest.POST("https://login.microsoftonline.com/common/oauth2/v2.0/token")
                    .form(mapOf(pair("client_id", Accounts.OAUTH_CALLBACK.getClientId()), pair("code", code),
                            pair("grant_type", "authorization_code"), pair("client_secret", Accounts.OAUTH_CALLBACK.getClientSecret()),
                            pair("redirect_uri", session.getRedirectURI()), pair("scope", scope)))
                    .getString();
            OAuth.AuthorizationResponse response = JsonUtils.fromNonNullJson(responseText,
                    OAuth.AuthorizationResponse.class);

            FCLAccountProfile profile = HttpRequest.GET("https://hmcl.huangyuhui.net/api/user")
                    .header("Token-Type", response.tokenType)
                    .header("Access-Token", response.accessToken)
                    .header("Authorization-Provider", "microsoft")
                    .authorization("Bearer", session.getIdToken())
                    .getJson(FCLAccountProfile.class);

            return new FCLAccount("microsoft", profile.nickname, profile.email, profile.role, session.getIdToken(), response.tokenType, response.accessToken, response.refreshToken);
        }).thenAcceptAsync(Schedulers.androidUIThread(), FCLAccounts::setAccount);
    }

    public static class FCLAccount implements HttpRequest.Authorization {
        private final String provider;
        private final String nickname;
        private final String email;
        private final String role;
        private final String idToken;
        private final String tokenType;
        private final String accessToken;
        private final String refreshToken;

        public FCLAccount(String provider, String nickname, String email, String role, String idToken, String tokenType, String accessToken, String refreshToken) {
            this.provider = provider;
            this.nickname = nickname;
            this.email = email;
            this.role = role;
            this.idToken = idToken;
            this.tokenType = tokenType;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getProvider() {
            return provider;
        }

        public String getNickname() {
            return nickname;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getIdToken() {
            return idToken;
        }

        @Override
        public String getTokenType() {
            return tokenType;
        }

        @Override
        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    private static class FCLAccountProfile {
        @SerializedName("ID")
        String id;
        @SerializedName("Provider")
        String provider;
        @SerializedName("Email")
        String email;
        @SerializedName("NickName")
        String nickname;
        @SerializedName("Role")
        String role;
    }

}
