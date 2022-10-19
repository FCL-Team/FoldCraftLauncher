package com.tungsten.fclcore.util.platform;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.tungsten.fclcore.util.StringUtils;

import java.util.*;

public final class CommandBuilder {

    private final List<Item> raw = new ArrayList<>();

    public CommandBuilder() {

    }

    private String parse(String s) {
        return toShellStringLiteral(s);
    }

    /**
     * Parsing will ignore your manual escaping
     *
     * @param args commands
     * @return this
     */
    public CommandBuilder add(String... args) {
        for (String s : args)
            raw.add(new Item(s, true));
        return this;
    }

    public CommandBuilder addAll(Collection<String> args) {
        for (String s : args)
            raw.add(new Item(s, true));
        return this;
    }

    public CommandBuilder addWithoutParsing(String... args) {
        for (String s : args)
            raw.add(new Item(s, false));
        return this;
    }

    public CommandBuilder addAllWithoutParsing(Collection<String> args) {
        for (String s : args)
            raw.add(new Item(s, false));
        return this;
    }

    public String addDefault(String opt) {
        for (Item item : raw) {
            if (item.arg.equals(opt)) {
                return item.arg;
            }
        }
        raw.add(new Item(opt, true));
        return null;
    }

    public String addDefault(String opt, String value) {
        for (Item item : raw) {
            if (item.arg.startsWith(opt)) {
                LOG.info("Default option '" + opt + value + "' is suppressed by '" + item.arg + "'");
                return item.arg;
            }
        }
        raw.add(new Item(opt + value, true));
        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < raw.size(); i++) {
            if (i != 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(raw.get(i).parse ? parse(raw.get(i).arg) : raw.get(i).arg);
        }
        return stringBuilder.toString();
    }

    public List<String> asList() {
        List<String> list = new ArrayList<>();
        for (Item item : raw) {
            list.add(item.arg);
        }
        return list;
    }

    private static class Item {
        String arg;
        boolean parse;

        Item(String arg, boolean parse) {
            this.arg = arg;
            this.parse = parse;
        }

        @Override
        public String toString() {
            return parse ? toShellStringLiteral(arg) : arg;
        }
    }

    public static String pwshString(String str) {
        return "'" + str.replace("'", "''") + "'";
    }

    public static boolean hasExecutionPolicy() {
        return true;
    }

    public static boolean setExecutionPolicy() {
        return true;
    }

    public static String toShellStringLiteral(String s) {
        String escaping = " \t\"!#$&'()*,;<=>?[\\]^`{|}~";
        String escaped = "\"$&`";
        if (s.indexOf(' ') >= 0 || s.indexOf('\t') >= 0 || StringUtils.containsOne(s, escaping.toCharArray())) {
            // The argument has not been quoted, add quotes.
            for (char ch : escaped.toCharArray())
                s = s.replace("" + ch, "\\" + ch);
            return '"' + s + '"';
        } else
            return s;
    }
}
