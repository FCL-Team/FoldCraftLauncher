package com.tungsten.fclcore.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.tungsten.fclcore.util.io.FileUtils;

public final class GetTask extends FetchTask<String> {

    private final Charset charset;

    public GetTask(URL url) {
        this(url, UTF_8);
    }

    public GetTask(URL url, Charset charset) {
        this(url, charset, 3);
    }

    public GetTask(URL url, Charset charset, int retry) {
        this(Collections.singletonList(url), charset, retry);
    }

    public GetTask(List<URL> url) {
        this(url, UTF_8, 3);
    }

    public GetTask(List<URL> urls, Charset charset, int retry) {
        super(urls, retry);
        this.charset = charset;

        setName(urls.get(0).toString());
    }

    @Override
    protected EnumCheckETag shouldCheckETag() {
        return EnumCheckETag.CHECK_E_TAG;
    }

    @Override
    protected void useCachedResult(Path cachedFile) throws IOException {
        setResult(FileUtils.readText(cachedFile));
    }

    @Override
    protected Context getContext(URLConnection conn, boolean checkETag) {
        return new Context() {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            @Override
            public void write(byte[] buffer, int offset, int len) {
                baos.write(buffer, offset, len);
            }

            @Override
            public void close() throws IOException {
                if (!isSuccess()) return;

                String result = baos.toString(charset.name());
                setResult(result);

                if (checkETag) {
                    repository.cacheText(result, conn);
                }
            }
        };
    }

}
