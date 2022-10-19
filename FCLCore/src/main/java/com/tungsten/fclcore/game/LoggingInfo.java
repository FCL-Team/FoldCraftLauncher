package com.tungsten.fclcore.game;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.TolerableValidationException;
import com.tungsten.fclcore.util.gson.Validation;

public final class LoggingInfo implements Validation {

    @SerializedName("file")
    private final IdDownloadInfo file;
    @SerializedName("argument")
    private final String argument;
    @SerializedName("type")
    private final String type;

    public LoggingInfo() {
        this(new IdDownloadInfo());
    }

    public LoggingInfo(IdDownloadInfo file) {
        this(file, "");
    }

    public LoggingInfo(IdDownloadInfo file, String argument) {
        this(file, argument, "");
    }

    public LoggingInfo(IdDownloadInfo file, String argument, String type) {
        this.file = file;
        this.argument = argument;
        this.type = type;
    }

    public IdDownloadInfo getFile() {
        return file;
    }

    public String getArgument() {
        return argument;
    }

    public String getType() {
        return type;
    }

    @Override
    public void validate() throws JsonParseException, TolerableValidationException {
        file.validate();
        if (StringUtils.isBlank(argument))
            throw new JsonParseException("LoggingInfo.argument is empty.");
        if (StringUtils.isBlank(type))
            throw new JsonParseException("LoggingInfo.type is empty.");
    }
}
