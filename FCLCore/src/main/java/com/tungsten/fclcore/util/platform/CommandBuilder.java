package com.tungsten.fclcore.util.platform;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.tungsten.fclcore.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class CommandBuilder {
    private static final Pattern UNSTABLE_OPTION_PATTERN = Pattern.compile("-XX:(?<key>[a-zA-Z0-9]+)=(?<value>.*)");
    private static final Pattern UNSTABLE_BOOLEAN_OPTION_PATTERN = Pattern.compile("-XX:(?<value>[+\\-])(?<key>[a-zA-Z0-9]+)");

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

    public String addUnstableDefault(String opt, boolean value) {
        for (Item item : raw) {
            final Matcher matcher = UNSTABLE_BOOLEAN_OPTION_PATTERN.matcher(item.arg);
            if (matcher.matches()) {
                if (matcher.group("key").equals(opt)) {
                    return item.arg;
                }
            }
        }

        if (value) {
            raw.add(new Item("-XX:+" + opt, true));
        } else {
            raw.add(new Item("-XX:-" + opt, true));
        }
        return null;
    }

    public String addUnstableDefault(String opt, String value) {
        for (Item item : raw) {
            final Matcher matcher = UNSTABLE_OPTION_PATTERN.matcher(item.arg);
            if (matcher.matches()) {
                if (matcher.group("key").equals(opt)) {
                    return item.arg;
                }
            }
        }

        raw.add(new Item("-XX:" + opt + "=" + value, true));
        return null;
    }

    public boolean removeIf(Predicate<String> pred) {
        return raw.removeIf(i -> pred.test(i.arg));
    }

    @Override
    public String toString() {
        return raw.stream().map(i -> i.parse ? parse(i.arg) : i.arg).collect(Collectors.joining(" "));
    }

    public List<String> asList() {
        return raw.stream().map(i -> i.arg).collect(Collectors.toList());
    }

    public List<String> asMutableList() {
        return raw.stream().map(i -> i.arg).collect(Collectors.toCollection(ArrayList::new));
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
