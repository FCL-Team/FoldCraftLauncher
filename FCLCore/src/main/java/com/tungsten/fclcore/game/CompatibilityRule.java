package com.tungsten.fclcore.game;

import java.util.*;

public final class CompatibilityRule {

    private final Action action;
    private final OSRestriction os;
    private final Map<String, Boolean> features;

    public CompatibilityRule() {
        this(Action.ALLOW, null);
    }

    public CompatibilityRule(Action action, OSRestriction os) {
        this(action, os, null);
    }

    public CompatibilityRule(Action action, OSRestriction os, Map<String, Boolean> features) {
        this.action = action;
        this.os = os;
        this.features = features;
    }

    public Action getAppliedAction(Map<String, Boolean> supportedFeatures) {
        if (os != null && !os.allow())
            return null;

        if (features != null)
            for (Map.Entry<String, Boolean> entry : features.entrySet())
                if (!Objects.equals(supportedFeatures.get(entry.getKey()), entry.getValue()))
                    return null;

        return action;
    }

    public static boolean appliesToCurrentEnvironment(Collection<CompatibilityRule> rules) {
        return appliesToCurrentEnvironment(rules, Collections.emptyMap());
    }

    public static boolean appliesToCurrentEnvironment(Collection<CompatibilityRule> rules, Map<String, Boolean> features) {
        if (rules == null || rules.isEmpty())
            return true;

        Action action = Action.DISALLOW;
        for (CompatibilityRule rule : rules) {
            Action thisAction = rule.getAppliedAction(features);
            if (thisAction != null)
                action = thisAction;
        }

        return action == Action.ALLOW;
    }

    public static boolean equals(Collection<CompatibilityRule> rules1, Collection<CompatibilityRule> rules2) {
        return Objects.hashCode(rules1) == Objects.hashCode(rules2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompatibilityRule that = (CompatibilityRule) o;
        return action == that.action &&
                Objects.equals(os, that.os) &&
                Objects.equals(features, that.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, os, features);
    }

    public enum Action {
        ALLOW,
        DISALLOW
    }
}
