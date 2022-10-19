package com.tungsten.fclcore.util.io;

import java.io.IOException;
import java.net.URL;

public class ResponseCodeException extends IOException {

    private final URL url;
    private final int responseCode;
    private final String data;

    public ResponseCodeException(URL url, int responseCode) {
        super("Unable to request url " + url + ", response code: " + responseCode);
        this.url = url;
        this.responseCode = responseCode;
        this.data = null;
    }

    public ResponseCodeException(URL url, int responseCode, String data) {
        super("Unable to request url " + url + ", response code: " + responseCode + ", data: " + data);
        this.url = url;
        this.responseCode = responseCode;
        this.data = data;
    }

    public URL getUrl() {
        return url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getData() {
        return data;
    }
}
