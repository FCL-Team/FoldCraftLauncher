package com.tungsten.fclcore.util.png;

import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PNGMetadata implements Serializable {
    public final static String KEY_TITLE = "Title";                 // Short (one line) title or caption for image
    public final static String KEY_AUTHOR = "Author";               // Name of image's creator
    public final static String KEY_DESCRIPTION = "Description";     // Description of image (possibly long)
    public final static String KEY_COPYRIGHT = "Copyright";         // Copyright notice
    public final static String KEY_CREATION_TIME = "Creation Time"; // Time of original image creation
    public final static String KEY_SOFTWARE = "Software";           // Software used to create the image
    public final static String KEY_DISCLAIMER = "Disclaimer";       // Legal disclaimer
    public final static String KEY_WARNING = "Warning";             // Warning of nature of content
    public final static String KEY_SOURCE = "Source";               // Device used to create the image
    public final static String KEY_COMMENT = "Comment";             // Miscellaneous comment

    private static final long serialVersionUID = 0L;

    Map<String, String> texts = Collections.emptyMap();

    public PNGMetadata() {
    }

    public PNGMetadata setText(String key, String value) {
        if (texts.isEmpty())
            texts = new LinkedHashMap<>();

        texts.put(key, value);
        return this;
    }

    public String getText(String key) {
        return texts.get(key);
    }

    public PNGMetadata setTitle(String title) {
        setText(KEY_TITLE, title);
        return this;
    }

    public PNGMetadata setAuthor(String author) {
        setText(KEY_AUTHOR, author);
        return this;
    }

    public PNGMetadata setAuthor() {
        setText(KEY_AUTHOR, System.getProperty("user.name"));
        return this;
    }

    public PNGMetadata setDescription(String description) {
        setText(KEY_DESCRIPTION, description);
        return this;
    }

    public PNGMetadata setCopyright(String copyright) {
        setText(KEY_COPYRIGHT, copyright);
        return this;
    }

    public PNGMetadata setCreationTime(String creationTime) {
        setText(KEY_CREATION_TIME, creationTime);
        return this;
    }

    public PNGMetadata setCreationTime(LocalDateTime time) {
        setCreationTime(ZonedDateTime.of(time, ZoneOffset.UTC).toOffsetDateTime());
        return this;
    }

    public PNGMetadata setCreationTime(OffsetDateTime time) {
        setCreationTime(time.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        return this;
    }

    public PNGMetadata setCreationTime(FileTime time) {
        setCreationTime(ZonedDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC).toOffsetDateTime());
        return this;
    }

    public PNGMetadata setCreationTime() {
        setCreationTime(LocalDateTime.now());
        return this;
    }

    public PNGMetadata setSoftware(String software) {
        setText(KEY_SOFTWARE, software);
        return this;
    }

    public PNGMetadata setDisclaimer(String disclaimer) {
        setText(KEY_DISCLAIMER, disclaimer);
        return this;
    }

    public PNGMetadata setWarning(String warning) {
        setText(KEY_WARNING, warning);
        return this;
    }

    public PNGMetadata setSource(String source) {
        setText(KEY_SOURCE, source);
        return this;
    }

    public PNGMetadata setComment(String comment) {
        setText(KEY_COMMENT, comment);
        return this;
    }
}