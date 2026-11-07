package com.tungsten.fcl.vr

import com.google.gson.JsonElement
import java.util.regex.Pattern

/** Vivecraft release 元数据中的依赖版本范围匹配（Maven 区间与 npm 风格表达式的公共子集） */
internal object QuestModVersionRules {
    private val NUMERIC: Pattern = Pattern.compile("[0-9]+(?:\\.[0-9]+)*")
    private val TERM: Pattern = Pattern.compile("(>=|<=|>|<|=|~|\\^)?([0-9]+(?:\\.[0-9]+)*(?:\\.[xX*])?|[xX*])")
    private val RANGE: Pattern = Pattern.compile("([\\[(])([^,\\]()]*)?(?:,([^\\]()]*)?)?([\\])])")

    fun matches(version: String, rules: JsonElement?): Boolean {
        return when {
            rules == null || rules.isJsonNull -> false
            rules.isJsonArray -> rules.asJsonArray.any { matches(version, it) }
            rules.isJsonPrimitive && rules.asJsonPrimitive.isString -> matches(version, rules.asString)
            else -> false
        }
    }

    fun matches(version: String, rule: String?): Boolean {
        return try {
            matchesChecked(version, rule)
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun matchesChecked(version: String, rule: String?): Boolean {
        if (rule == null || version.length > 128 || rule.length > 2048
            || !NUMERIC.matcher(version).matches()
        ) return false
        val trimmed = rule.trim()
        if (trimmed.isEmpty()) return false
        if (trimmed.startsWith("[") || trimmed.startsWith("(")) return maven(version, trimmed)
        for (alternative in trimmed.split(Regex("\\s*\\|\\|\\s*"))) {
            val normalized = alternative.trim().replace(Regex("(>=|<=|>|<|=|~|\\^)\\s+"), "$1")
            var all = normalized.isNotEmpty()
            for (termExpression in normalized.split(Regex("\\s+"))) {
                all = all && term(version, termExpression)
            }
            if (all) return true
        }
        return false
    }

    private fun term(version: String, expression: String): Boolean {
        val matcher = TERM.matcher(expression)
        if (!matcher.matches()) return false
        val operator = matcher.group(1)
        val expected = matcher.group(2)
        if (expected.last() in "xX*") {
            if (operator != null && operator != "=") return false
            if (expected.length == 1) return true
            val prefix = expected.substring(0, expected.length - 2)
            return version == prefix || version.startsWith("$prefix.")
        }
        val comparison = compare(version, expected)
        if (operator == null || operator == "=") return comparison == 0
        return when (operator) {
            ">=" -> comparison >= 0
            "<=" -> comparison <= 0
            ">" -> comparison > 0
            "<" -> comparison < 0
            "~", "^" -> comparison >= 0 && compare(version, upperBound(expected, operator)) < 0
            else -> false
        }
    }

    private fun upperBound(version: String, operator: String): String {
        val values = version.split(".")
        var increment = if (operator == "~") minOf(1, values.size - 1) else 0
        if (operator == "^") {
            while (increment < values.size - 1 && values[increment] == "0") increment++
        }
        val result = StringBuilder()
        for (i in 0..increment) {
            if (i > 0) result.append('.')
            result.append(values[i].toLong() + (if (i == increment) 1 else 0))
        }
        return result.toString()
    }

    private fun maven(version: String, expression: String): Boolean {
        val matcher = RANGE.matcher(expression)
        var end = 0
        var matched = false
        while (matcher.find()) {
            if (expression.substring(end, matcher.start()).trim().replace(",", "").isNotEmpty()) return false
            val low = matcher.group(2)?.trim() ?: ""
            var high = matcher.group(3)
            val valid: Boolean
            if (high == null) {
                valid = matcher.group(1) == "[" && matcher.group(4) == "]" &&
                        NUMERIC.matcher(low).matches() && compare(version, low) == 0
            } else {
                high = high.trim()
                if ((low.isNotEmpty() && !NUMERIC.matcher(low).matches())
                    || (high.isNotEmpty() && !NUMERIC.matcher(high).matches())
                ) return false
                valid = (low.isEmpty() || compare(version, low) > 0
                        || (compare(version, low) == 0 && matcher.group(1) == "["))
                        && (high.isEmpty() || compare(version, high) < 0
                        || (compare(version, high) == 0 && matcher.group(4) == "]"))
            }
            matched = matched || valid
            end = matcher.end()
        }
        return end == expression.length && matched
    }

    private fun compare(left: String, right: String): Int {
        val a = left.split(".")
        val b = right.split(".")
        for (i in 0 until maxOf(a.size, b.size)) {
            val x = if (i < a.size) a[i].toLong() else 0L
            val y = if (i < b.size) b[i].toLong() else 0L
            val difference = x.compareTo(y)
            if (difference != 0) return difference
        }
        return 0
    }
}
