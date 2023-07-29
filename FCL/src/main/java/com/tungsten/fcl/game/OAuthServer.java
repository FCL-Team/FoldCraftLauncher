package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Lang.thread;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.OAuth;
import com.tungsten.fclcore.event.Event;
import com.tungsten.fclcore.event.EventManager;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public final class OAuthServer extends NanoHTTPD implements OAuth.Session {
    private final int port;
    private final CompletableFuture<String> future = new CompletableFuture<>();

    public static String lastlyOpenedURL;

    private String idToken;

    private OAuthServer(int port) {
        super(port);

        this.port = port;
    }

    @Override
    public String getRedirectURI() {
        return String.format("http://localhost:%d/auth-response", port);
    }

    @Override
    public String waitFor() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public String getIdToken() {
        return idToken;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (!"/auth-response".equals(session.getUri())) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, "");
        }

        if (session.getMethod() == Method.POST) {
            Map<String, String> files = new HashMap<>();
            try {
                session.parseBody(files);
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Failed to read post data", e);
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_HTML, "");
            } catch (ResponseException re) {
                return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
            }
        } else if (session.getMethod() == Method.GET) {
            // do nothing
        } else {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, "");
        }
        String parameters = session.getQueryParameterString();

        Map<String, String> query = mapOf(NetworkUtils.parseQuery(parameters));
        if (query.containsKey("code")) {
            idToken = query.get("id_token");
            future.complete(query.get("code"));
        } else {
            Logging.LOG.warning("Error: " + parameters);
            future.completeExceptionally(new AuthenticationException("failed to authenticate"));
        }

        String html;
        try {
            html = IOUtils.readFullyAsString(OAuthServer.class.getResourceAsStream("/assets/microsoft_auth.html"))
                    .replace("%close-page%", FCLPath.CONTEXT.getString(R.string.account_methods_microsoft_close_page));
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to load html");
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_HTML, "");
        }
        thread(() -> {
            try {
                Thread.sleep(1000);
                stop();
            } catch (InterruptedException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to sleep for 1 second");
            }
        });
        return newFixedLengthResponse(Response.Status.OK, "text/html; charset=UTF-8", html);
    }

    public static class Factory implements OAuth.Callback {
        public final EventManager<GrantDeviceCodeEvent> onGrantDeviceCode = new EventManager<>();
        public final EventManager<OpenBrowserEvent> onOpenBrowser = new EventManager<>();

        @Override
        public OAuth.Session startServer() throws IOException, AuthenticationException {
            if (StringUtils.isBlank(getClientId())) {
                throw new MicrosoftAuthenticationNotSupportedException();
            }

            IOException exception = null;
            for (int port : new int[]{29111, 29112, 29113, 29114, 29115}) {
                try {
                    OAuthServer server = new OAuthServer(port);
                    server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
                    return server;
                } catch (IOException e) {
                    exception = e;
                }
            }
            throw exception;
        }

        @Override
        public void grantDeviceCode(String userCode, String verificationURI) {
            onGrantDeviceCode.fireEvent(new GrantDeviceCodeEvent(this, userCode, verificationURI));
        }

        @Override
        public void openBrowser(String url) {
            lastlyOpenedURL = url;
            AndroidUtils.openLinkWithBuiltinWebView(FCLPath.CONTEXT, url);

            onOpenBrowser.fireEvent(new OpenBrowserEvent(this, url));
        }

        @Override
        public String getClientId() {
            return "d903cc0e-c3b4-4a3c-b347-06c2e6269be0";
        }

        @Override
        public String getClientSecret() {
            return null;
        }

        @Override
        public boolean isPublicClient() {
            return true; // We have turned on the device auth flow.
        }
    }

    public static class GrantDeviceCodeEvent extends Event {
        private final String userCode;
        private final String verificationUri;

        public GrantDeviceCodeEvent(Object source, String userCode, String verificationUri) {
            super(source);
            this.userCode = userCode;
            this.verificationUri = verificationUri;
        }

        public String getUserCode() {
            return userCode;
        }

        public String getVerificationUri() {
            return verificationUri;
        }
    }

    public static class OpenBrowserEvent extends Event {
        private final String url;

        public OpenBrowserEvent(Object source, String url) {
            super(source);
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class MicrosoftAuthenticationNotSupportedException extends AuthenticationException {
    }
}
