package com.tungsten.fcl.setting;

import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制布局文件扫描器：按字节读取控制器与各布局的元数据，直接按括号跳过
 * 大体积的按键数据（viewData），只把需要的片段交给 JSON 解析。
 * <p>
 * 启动时列出布局不需要解析按键，逐 token 解析整个文件在复杂布局上耗时明显，
 * 按字节扫描可跳过按键数据的内容。
 */
final class LayoutMetadataScanner {

    private static final int BOM_0 = 0xEF;
    private static final int BOM_1 = 0xBB;
    private static final int BOM_2 = 0xBF;

    private final byte[] data;
    private final String targetGroupId;

    private int pos;

    private LayoutMetadataScanner(byte[] data, String targetGroupId) {
        this.data = data;
        this.targetGroupId = targetGroupId;
    }

    /**
     * @param targetGroupId 需要读取按键数据的布局 id，null 表示只读取元数据
     */
    static Metadata scan(File file, String targetGroupId) throws IOException {
        return new LayoutMetadataScanner(Files.readAllBytes(file.toPath()), targetGroupId).parse();
    }

    /**
     * 扫描结果：布局文件元数据。字符串字段为原始值，按键数据仅在匹配 targetGroupId 时截取。
     */
    static final class Metadata {
        String id;
        String name = "";
        String version = "";
        String author = "";
        String description = "";
        int versionCode = 1;
        /** 文件未声明时为 -1 */
        int controllerVersion = -1;
        final List<GroupInfo> groups = new ArrayList<>();
        String buttonStylesJson;
        String directionStylesJson;
        String viewDataJson;
    }

    static final class GroupInfo {
        String id;
        String name = "";
        String visibility = "VISIBLE";
    }

    private Metadata parse() {
        Metadata meta = new Metadata();
        if (data.length >= 3 && (data[0] & 0xFF) == BOM_0 && (data[1] & 0xFF) == BOM_1 && (data[2] & 0xFF) == BOM_2) {
            pos = 3;
        }
        skipWhitespace();
        expect('{');
        while (true) {
            skipWhitespace();
            if (end()) {
                throw new JsonParseException("Unexpected end of layout file!");
            }
            if (peek() == '}') {
                pos++;
                break;
            }
            String key = readString();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            switch (key) {
                case "id":
                    meta.id = readString();
                    break;
                case "name":
                    meta.name = readString();
                    break;
                case "version":
                    meta.version = readString();
                    break;
                case "author":
                    meta.author = readString();
                    break;
                case "description":
                    meta.description = readString();
                    break;
                case "versionCode":
                    meta.versionCode = readInt();
                    break;
                case "controllerVersion":
                    meta.controllerVersion = readInt();
                    break;
                case "buttonStyles":
                    meta.buttonStylesJson = readRawValue();
                    break;
                case "directionStyles":
                    meta.directionStylesJson = readRawValue();
                    break;
                case "viewGroups":
                    readViewGroups(meta);
                    break;
                default:
                    skipValue();
                    break;
            }
            skipWhitespace();
            if (!end() && peek() == ',') {
                pos++;
            }
        }
        return meta;
    }

    private void readViewGroups(Metadata meta) {
        expect('[');
        while (true) {
            skipWhitespace();
            if (peek() == ']') {
                pos++;
                return;
            }
            readViewGroup(meta);
            skipWhitespace();
            if (peek() == ',') {
                pos++;
            } else if (peek() == ']') {
                pos++;
                return;
            } else {
                throw new JsonParseException("Malformed viewGroups!");
            }
        }
    }

    private void readViewGroup(Metadata meta) {
        expect('{');
        GroupInfo group = new GroupInfo();
        while (true) {
            skipWhitespace();
            if (peek() == '}') {
                pos++;
                break;
            }
            String key = readString();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            switch (key) {
                case "id":
                    group.id = readString();
                    break;
                case "name":
                    group.name = readString();
                    break;
                case "visibility":
                    group.visibility = readString();
                    break;
                case "viewData":
                    // 按键数据按括号整体跳过，命中目标布局时才截取原文
                    if (targetGroupId != null && targetGroupId.equals(group.id)) {
                        meta.viewDataJson = readRawValue();
                    } else {
                        skipValue();
                    }
                    break;
                default:
                    skipValue();
                    break;
            }
            skipWhitespace();
            if (peek() == ',') {
                pos++;
            }
        }
        if (group.id != null) {
            meta.groups.add(group);
        }
    }

    private void skipValue() {
        int b = peek();
        switch (b) {
            case '"':
                skipString();
                break;
            case '{':
                skipComposite('{', '}');
                break;
            case '[':
                skipComposite('[', ']');
                break;
            default:
                skipPrimitive();
                break;
        }
    }

    private void skipComposite(int open, int close) {
        pos++;
        int depth = 1;
        while (!end()) {
            int b = peek();
            if (b == '"') {
                skipString();
                continue;
            }
            pos++;
            if (b == open) {
                depth++;
            } else if (b == close && --depth == 0) {
                return;
            }
        }
        throw new JsonParseException("Unexpected end of layout file!");
    }

    private void skipString() {
        expect('"');
        while (!end()) {
            int b = peek();
            if (b == '\\') {
                pos += 2;
            } else if (b == '"') {
                pos++;
                return;
            } else {
                pos++;
            }
        }
        throw new JsonParseException("Unclosed string in layout file!");
    }

    private void skipPrimitive() {
        while (!end()) {
            int b = peek();
            if (b == ',' || b == '}' || b == ']' || isWhitespace(b)) {
                return;
            }
            pos++;
        }
    }

    private String readString() {
        expect('"');
        int start = pos;
        boolean escaped = false;
        while (!end()) {
            int b = peek();
            if (b == '\\') {
                escaped = true;
                pos += 2;
            } else if (b == '"') {
                break;
            } else {
                pos++;
            }
        }
        if (end()) {
            throw new JsonParseException("Unclosed string in layout file!");
        }
        int end = pos;
        pos++;
        return escaped ? unescape(start, end) : new String(data, start, end - start, StandardCharsets.UTF_8);
    }

    private String readRawValue() {
        int start = pos;
        skipValue();
        return new String(data, start, pos - start, StandardCharsets.UTF_8);
    }

    private int readInt() {
        int start = pos;
        if (!end() && peek() == '-') {
            pos++;
        }
        while (!end() && peek() >= '0' && peek() <= '9') {
            pos++;
        }
        try {
            return Integer.parseInt(new String(data, start, pos - start, StandardCharsets.US_ASCII));
        } catch (NumberFormatException e) {
            throw new JsonParseException("Malformed number in layout file!");
        }
    }

    /** 反转义 JSON 字符串（非转义片段按 UTF-8 解码） */
    private String unescape(int start, int end) {
        StringBuilder builder = new StringBuilder();
        int sliceStart = start;
        int i = start;
        while (i < end) {
            if (data[i] != '\\') {
                i++;
                continue;
            }
            if (i > sliceStart) {
                builder.append(new String(data, sliceStart, i - sliceStart, StandardCharsets.UTF_8));
            }
            i++;
            if (i >= end) {
                throw new JsonParseException("Malformed escape in layout file!");
            }
            char escaped = (char) (data[i] & 0xFF);
            switch (escaped) {
                case 'b':
                    builder.append('\b');
                    i++;
                    break;
                case 'f':
                    builder.append('\f');
                    i++;
                    break;
                case 'n':
                    builder.append('\n');
                    i++;
                    break;
                case 'r':
                    builder.append('\r');
                    i++;
                    break;
                case 't':
                    builder.append('\t');
                    i++;
                    break;
                case 'u':
                    if (i + 4 >= end) {
                        throw new JsonParseException("Malformed unicode escape in layout file!");
                    }
                    try {
                        builder.append((char) Integer.parseInt(new String(data, i + 1, 4, StandardCharsets.US_ASCII), 16));
                    } catch (NumberFormatException e) {
                        throw new JsonParseException("Malformed unicode escape in layout file!");
                    }
                    i += 5;
                    break;
                default:
                    builder.append(escaped);
                    i++;
                    break;
            }
            sliceStart = i;
        }
        if (end > sliceStart) {
            builder.append(new String(data, sliceStart, end - sliceStart, StandardCharsets.UTF_8));
        }
        return builder.toString();
    }

    private void skipWhitespace() {
        while (!end() && isWhitespace(peek())) {
            pos++;
        }
    }

    private static boolean isWhitespace(int b) {
        return b == ' ' || b == '\n' || b == '\r' || b == '\t';
    }

    private void expect(char expected) {
        if (end() || peek() != expected) {
            throw new JsonParseException("Expected '" + expected + "' in layout file!");
        }
        pos++;
    }

    private int peek() {
        return pos < data.length ? data[pos] & 0xFF : -1;
    }

    private boolean end() {
        return pos >= data.length;
    }
}
