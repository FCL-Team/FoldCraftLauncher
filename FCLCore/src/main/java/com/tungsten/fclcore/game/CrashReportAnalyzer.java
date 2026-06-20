/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.game;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyzes Fabric Loader information from Minecraft crash logs.
 * Only contains Fabric Loader related rules; other analysis has been removed.
 */
public final class CrashReportAnalyzer {

    private CrashReportAnalyzer() {
    }

    public enum Rule {
        // Fabric mod resolution errors — these are information provided by Fabric Loader
        MOD_RESOLUTION_CONFLICT(Pattern.compile("ModResolutionException: Found conflicting mods: (?<sourcemod>.*) conflicts with (?<destmod>.*)"), "sourcemod", "destmod"),
        MOD_RESOLUTION_MISSING(Pattern.compile("ModResolutionException: Could not find required mod: (?<sourcemod>.*) requires (?<destmod>.*)"), "sourcemod", "destmod"),
        MOD_RESOLUTION_MISSING_MINECRAFT(Pattern.compile("ModResolutionException: Could not find required mod: (?<mod>.*) requires \\{minecraft @ (?<version>.*)\\}"), "mod", "version"),
        MOD_RESOLUTION_COLLECTION(Pattern.compile("ModResolutionException: Could not resolve valid mod collection \\(at: (?<sourcemod>.*) requires (?<destmod>.*)\\)"), "sourcemod", "destmod"),

        // Fabric mod loading crashes
        LOADING_CRASHED_FABRIC(Pattern.compile("Could not execute entrypoint stage '(.*?)' due to errors, provided by '(?<id>.*)'!"), "id"),

        // Fabric version compatibility (Fabric 0.12+ breaking changes)
        FABRIC_VERSION_0_12(Pattern.compile("java\\.lang\\.NoClassDefFoundError: org/spongepowered/asm/mixin/transformer/FabricMixinTransformerProxy")),

        // Fabric warnings (incompatible mod set, etc.)
        FABRIC_WARNINGS(Pattern.compile("(Warnings were found!|Incompatible mod set!|Incompatible mods found!)(.*?)[\\n\\r]+(?<reason>[^\\[]+)\\["), "reason"),

        // Generic Fabric mod resolution failure (fallback)
        MOD_RESOLUTION(Pattern.compile("ModResolutionException: (?<reason>(.*)[\\n\\r]*( - (.*)[\\n\\r]*)+)"), "reason");

        private final Pattern pattern;
        private final String[] groupNames;

        Rule(Pattern pattern, String... groupNames) {
            this.pattern = pattern;
            this.groupNames = groupNames;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String[] getGroupNames() {
            return groupNames;
        }
    }

    public static class Result {
        private final Rule rule;
        private final String log;
        private final Matcher matcher;

        public Result(Rule rule, String log, Matcher matcher) {
            this.rule = rule;
            this.log = log;
            this.matcher = matcher;
        }

        public Rule getRule() {
            return rule;
        }

        public String getLog() {
            return log;
        }

        public Matcher getMatcher() {
            return matcher;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Result result = (Result) o;

            if (rule != result.rule) return false;
            if (!log.equals(result.log)) return false;
            return matcher.equals(result.matcher);
        }

        @Override
        public int hashCode() {
            int result = rule.hashCode();
            result = 31 * result + log.hashCode();
            result = 31 * result + matcher.hashCode();
            return result;
        }
    }

    public static Set<Result> anaylze(String log) {
        Set<Result> results = new HashSet<>();
        for (Rule rule : Rule.values()) {
            Matcher matcher = rule.pattern.matcher(log);
            if (matcher.find()) {
                results.add(new Result(rule, log, matcher));
            }
        }
        return results;
    }
}
