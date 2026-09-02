package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mio.controlconverter.CcJson.bool
import com.mio.controlconverter.CcJson.inum
import com.mio.controlconverter.CcJson.obj
import com.mio.controlconverter.CcJson.pyNum
import com.mio.controlconverter.CcJson.str

/**
 * control-converter 常量表。
 *
 * 语义基准：control-converter-master/cc.py（Python 参考实现），
 * 与 cc-rs/src/constants.rs、go/constants.go 逐条对齐。
 */
object CcConstants {
    const val FCL_CONTROLLER_VERSION = 21
    const val ZL_EDITOR_VERSION = 12
    const val META_KEY = "_control_byIQge报错别找我"
    const val META_SCHEMA_VERSION = 1

    /** ZL2 侧键名别名 → 规范 GLFW 键名 / 启动器事件。 */
    val ZL_KEY_ALIASES: Map<String, String> = buildMap {
        put("GLFW_MOUSE_BUTTON_1", "GLFW_MOUSE_BUTTON_LEFT")
        put("GLFW_MOUSE_BUTTON_2", "GLFW_MOUSE_BUTTON_RIGHT")
        put("GLFW_MOUSE_BUTTON_3", "GLFW_MOUSE_BUTTON_MIDDLE")
        put("MOUSE_SCROLL_UP", "launcher.event.scroll_up.single")
        put("MOUSE_SCROLL_DOWN", "launcher.event.scroll_down.single")
        put("key.mouse.left", "GLFW_MOUSE_BUTTON_LEFT")
        put("key.mouse.right", "GLFW_MOUSE_BUTTON_RIGHT")
        put("key.mouse.middle", "GLFW_MOUSE_BUTTON_MIDDLE")
        put("key.mouse.4", "GLFW_MOUSE_BUTTON_4")
        put("key.mouse.5", "GLFW_MOUSE_BUTTON_5")
        put("key.mouse.6", "GLFW_MOUSE_BUTTON_6")
        put("key.mouse.7", "GLFW_MOUSE_BUTTON_7")
        put("key.mouse.8", "GLFW_MOUSE_BUTTON_8")
        put("key.keyboard.unknown", "GLFW_KEY_UNKNOWN")
        put("key.keyboard.num.lock", "GLFW_KEY_NUM_LOCK")
        put("key.keyboard.keypad.0", "GLFW_KEY_KP_0")
        put("key.keyboard.keypad.1", "GLFW_KEY_KP_1")
        put("key.keyboard.keypad.2", "GLFW_KEY_KP_2")
        put("key.keyboard.keypad.3", "GLFW_KEY_KP_3")
        put("key.keyboard.keypad.4", "GLFW_KEY_KP_4")
        put("key.keyboard.keypad.5", "GLFW_KEY_KP_5")
        put("key.keyboard.keypad.6", "GLFW_KEY_KP_6")
        put("key.keyboard.keypad.7", "GLFW_KEY_KP_7")
        put("key.keyboard.keypad.8", "GLFW_KEY_KP_8")
        put("key.keyboard.keypad.9", "GLFW_KEY_KP_9")
        put("key.keyboard.keypad.add", "GLFW_KEY_KP_ADD")
        put("key.keyboard.keypad.decimal", "GLFW_KEY_KP_DECIMAL")
        put("key.keyboard.keypad.enter", "GLFW_KEY_KP_ENTER")
        put("key.keyboard.keypad.equal", "GLFW_KEY_KP_EQUAL")
        put("key.keyboard.keypad.multiply", "GLFW_KEY_KP_MULTIPLY")
        put("key.keyboard.keypad.divide", "GLFW_KEY_KP_DIVIDE")
        put("key.keyboard.keypad.subtract", "GLFW_KEY_KP_SUBTRACT")
        put("key.keyboard.down", "GLFW_KEY_DOWN")
        put("key.keyboard.left", "GLFW_KEY_LEFT")
        put("key.keyboard.right", "GLFW_KEY_RIGHT")
        put("key.keyboard.up", "GLFW_KEY_UP")
        put("key.keyboard.apostrophe", "GLFW_KEY_APOSTROPHE")
        put("key.keyboard.backslash", "GLFW_KEY_BACKSLASH")
        put("key.keyboard.comma", "GLFW_KEY_COMMA")
        put("key.keyboard.equal", "GLFW_KEY_EQUAL")
        put("key.keyboard.grave.accent", "GLFW_KEY_GRAVE_ACCENT")
        put("key.keyboard.left.bracket", "GLFW_KEY_LEFT_BRACKET")
        put("key.keyboard.minus", "GLFW_KEY_MINUS")
        put("key.keyboard.period", "GLFW_KEY_PERIOD")
        put("key.keyboard.right.bracket", "GLFW_KEY_RIGHT_BRACKET")
        put("key.keyboard.semicolon", "GLFW_KEY_SEMICOLON")
        put("key.keyboard.slash", "GLFW_KEY_SLASH")
        put("key.keyboard.space", "GLFW_KEY_SPACE")
        put("key.keyboard.tab", "GLFW_KEY_TAB")
        put("key.keyboard.left.alt", "GLFW_KEY_LEFT_ALT")
        put("key.keyboard.left.control", "GLFW_KEY_LEFT_CONTROL")
        put("key.keyboard.left.shift", "GLFW_KEY_LEFT_SHIFT")
        put("key.keyboard.left.win", "GLFW_KEY_LEFT_SUPER")
        put("key.keyboard.left.super", "GLFW_KEY_LEFT_SUPER")
        put("key.keyboard.left.meta", "GLFW_KEY_LEFT_SUPER")
        put("key.keyboard.right.alt", "GLFW_KEY_RIGHT_ALT")
        put("key.keyboard.right.control", "GLFW_KEY_RIGHT_CONTROL")
        put("key.keyboard.right.shift", "GLFW_KEY_RIGHT_SHIFT")
        put("key.keyboard.right.win", "GLFW_KEY_RIGHT_SUPER")
        put("key.keyboard.right.super", "GLFW_KEY_RIGHT_SUPER")
        put("key.keyboard.right.meta", "GLFW_KEY_RIGHT_SUPER")
        put("key.keyboard.enter", "GLFW_KEY_ENTER")
        put("key.keyboard.escape", "GLFW_KEY_ESCAPE")
        put("key.keyboard.backspace", "GLFW_KEY_BACKSPACE")
        put("key.keyboard.delete", "GLFW_KEY_DELETE")
        put("key.keyboard.end", "GLFW_KEY_END")
        put("key.keyboard.home", "GLFW_KEY_HOME")
        put("key.keyboard.insert", "GLFW_KEY_INSERT")
        put("key.keyboard.page.down", "GLFW_KEY_PAGE_DOWN")
        put("key.keyboard.page.up", "GLFW_KEY_PAGE_UP")
        put("key.keyboard.caps.lock", "GLFW_KEY_CAPS_LOCK")
        put("key.keyboard.pause", "GLFW_KEY_PAUSE")
        put("key.keyboard.scroll.lock", "GLFW_KEY_SCROLL_LOCK")
        put("key.keyboard.menu", "GLFW_KEY_MENU")
        put("key.keyboard.print.screen", "GLFW_KEY_PRINT_SCREEN")
        put("key.keyboard.world.1", "GLFW_KEY_WORLD_1")
        put("key.keyboard.world.2", "GLFW_KEY_WORLD_2")
        put("key.keyboard.keypad.separator", "GLFW_KEY_KP_DECIMAL")
        for (i in 0..9) put("key.keyboard.$i", "GLFW_KEY_$i")
        for (code in 'a'..'z') put("key.keyboard.$code", "GLFW_KEY_${code.uppercaseChar()}")
        for (i in 1..25) put("key.keyboard.f$i", "GLFW_KEY_F$i")
    }

    val FCL_MOUSE: Map<String, Long> = mapOf(
        "GLFW_MOUSE_BUTTON_LEFT" to 1000L,
        "GLFW_MOUSE_BUTTON_MIDDLE" to 1001L,
        "GLFW_MOUSE_BUTTON_RIGHT" to 1002L,
    )
    val FCL_MOUSE_REVERSE: Map<Long, String> = FCL_MOUSE.entries.associate { (k, v) -> v to k }
    val FCL_SCROLL_REVERSE: Map<Long, Pair<String, String>> = mapOf(
        1003L to ("launcher.event.scroll_up.single" to "launcher.event.scroll_up"),
        1004L to ("launcher.event.scroll_down.single" to "launcher.event.scroll_down"),
    )

    val ZL_ONLY_KEYS: Set<String> = setOf(
        "GLFW_KEY_WORLD_1", "GLFW_KEY_WORLD_2", "GLFW_KEY_F25", "GLFW_KEY_MENU",
        "GLFW_KEY_LAST", "GLFW_MOD_SHIFT", "GLFW_MOD_CONTROL", "GLFW_MOD_ALT",
        "GLFW_MOD_SUPER", "GLFW_MOD_CAPS_LOCK", "GLFW_MOD_NUM_LOCK",
        "GLFW_MOUSE_BUTTON_4", "GLFW_MOUSE_BUTTON_5", "GLFW_MOUSE_BUTTON_6",
        "GLFW_MOUSE_BUTTON_7", "GLFW_MOUSE_BUTTON_8", "GLFW_MOUSE_BUTTON_LAST",
    )
    val UNSUPPORTED_ZL_KEY_REASONS: Map<String, String> =
        ZL_ONLY_KEYS.associateWith { "FCL controls do not define an exact matching keycode" }
    val UNSUPPORTED_FCL_KEY_REASONS: Map<Long, String> = mapOf(
        0L to "FCL KEY_RESERVED is not a real input key",
        121L to "FCL KEY_KPCOMMA has no exact GLFW/ZL control event equivalent",
    )

    /** GLFW 键名 → FCL Linux 风格整数键码。 */
    val GLFW_TO_FCL: Map<String, Long> = mapOf(
        "GLFW_KEY_UNKNOWN" to 240L,
        "GLFW_KEY_SPACE" to 57L,
        "GLFW_KEY_APOSTROPHE" to 40L,
        "GLFW_KEY_COMMA" to 51L,
        "GLFW_KEY_MINUS" to 12L,
        "GLFW_KEY_PERIOD" to 52L,
        "GLFW_KEY_SLASH" to 53L,
        "GLFW_KEY_0" to 11L,
        "GLFW_KEY_1" to 2L,
        "GLFW_KEY_2" to 3L,
        "GLFW_KEY_3" to 4L,
        "GLFW_KEY_4" to 5L,
        "GLFW_KEY_5" to 6L,
        "GLFW_KEY_6" to 7L,
        "GLFW_KEY_7" to 8L,
        "GLFW_KEY_8" to 9L,
        "GLFW_KEY_9" to 10L,
        "GLFW_KEY_SEMICOLON" to 39L,
        "GLFW_KEY_EQUAL" to 13L,
        "GLFW_KEY_A" to 30L,
        "GLFW_KEY_B" to 48L,
        "GLFW_KEY_C" to 46L,
        "GLFW_KEY_D" to 32L,
        "GLFW_KEY_E" to 18L,
        "GLFW_KEY_F" to 33L,
        "GLFW_KEY_G" to 34L,
        "GLFW_KEY_H" to 35L,
        "GLFW_KEY_I" to 23L,
        "GLFW_KEY_J" to 36L,
        "GLFW_KEY_K" to 37L,
        "GLFW_KEY_L" to 38L,
        "GLFW_KEY_M" to 50L,
        "GLFW_KEY_N" to 49L,
        "GLFW_KEY_O" to 24L,
        "GLFW_KEY_P" to 25L,
        "GLFW_KEY_Q" to 16L,
        "GLFW_KEY_R" to 19L,
        "GLFW_KEY_S" to 31L,
        "GLFW_KEY_T" to 20L,
        "GLFW_KEY_U" to 22L,
        "GLFW_KEY_V" to 47L,
        "GLFW_KEY_W" to 17L,
        "GLFW_KEY_X" to 45L,
        "GLFW_KEY_Y" to 21L,
        "GLFW_KEY_Z" to 44L,
        "GLFW_KEY_LEFT_BRACKET" to 26L,
        "GLFW_KEY_RIGHT_BRACKET" to 27L,
        "GLFW_KEY_BACKSLASH" to 43L,
        "GLFW_KEY_GRAVE_ACCENT" to 41L,
        "GLFW_KEY_ESCAPE" to 1L,
        "GLFW_KEY_ENTER" to 28L,
        "GLFW_KEY_TAB" to 15L,
        "GLFW_KEY_BACKSPACE" to 14L,
        "GLFW_KEY_INSERT" to 110L,
        "GLFW_KEY_DELETE" to 111L,
        "GLFW_KEY_RIGHT" to 106L,
        "GLFW_KEY_LEFT" to 105L,
        "GLFW_KEY_DOWN" to 108L,
        "GLFW_KEY_UP" to 103L,
        "GLFW_KEY_PAGE_UP" to 104L,
        "GLFW_KEY_PAGE_DOWN" to 109L,
        "GLFW_KEY_HOME" to 102L,
        "GLFW_KEY_END" to 107L,
        "GLFW_KEY_CAPS_LOCK" to 58L,
        "GLFW_KEY_SCROLL_LOCK" to 70L,
        "GLFW_KEY_NUM_LOCK" to 69L,
        "GLFW_KEY_PRINT_SCREEN" to 99L,
        "GLFW_KEY_PAUSE" to 119L,
        "GLFW_KEY_F1" to 59L,
        "GLFW_KEY_F2" to 60L,
        "GLFW_KEY_F3" to 61L,
        "GLFW_KEY_F4" to 62L,
        "GLFW_KEY_F5" to 63L,
        "GLFW_KEY_F6" to 64L,
        "GLFW_KEY_F7" to 65L,
        "GLFW_KEY_F8" to 66L,
        "GLFW_KEY_F9" to 67L,
        "GLFW_KEY_F10" to 68L,
        "GLFW_KEY_F11" to 87L,
        "GLFW_KEY_F12" to 88L,
        "GLFW_KEY_F13" to 183L,
        "GLFW_KEY_F14" to 184L,
        "GLFW_KEY_F15" to 185L,
        "GLFW_KEY_F16" to 186L,
        "GLFW_KEY_F17" to 187L,
        "GLFW_KEY_F18" to 188L,
        "GLFW_KEY_F19" to 189L,
        "GLFW_KEY_F20" to 190L,
        "GLFW_KEY_F21" to 191L,
        "GLFW_KEY_F22" to 192L,
        "GLFW_KEY_F23" to 193L,
        "GLFW_KEY_F24" to 194L,
        "GLFW_KEY_KP_0" to 82L,
        "GLFW_KEY_KP_1" to 79L,
        "GLFW_KEY_KP_2" to 80L,
        "GLFW_KEY_KP_3" to 81L,
        "GLFW_KEY_KP_4" to 75L,
        "GLFW_KEY_KP_5" to 76L,
        "GLFW_KEY_KP_6" to 77L,
        "GLFW_KEY_KP_7" to 71L,
        "GLFW_KEY_KP_8" to 72L,
        "GLFW_KEY_KP_9" to 73L,
        "GLFW_KEY_KP_DECIMAL" to 83L,
        "GLFW_KEY_KP_DIVIDE" to 98L,
        "GLFW_KEY_KP_MULTIPLY" to 55L,
        "GLFW_KEY_KP_SUBTRACT" to 74L,
        "GLFW_KEY_KP_ADD" to 78L,
        "GLFW_KEY_KP_ENTER" to 96L,
        "GLFW_KEY_KP_EQUAL" to 117L,
        "GLFW_KEY_LEFT_SHIFT" to 42L,
        "GLFW_KEY_LEFT_CONTROL" to 29L,
        "GLFW_KEY_LEFT_ALT" to 56L,
        "GLFW_KEY_LEFT_SUPER" to 125L,
        "GLFW_KEY_RIGHT_SHIFT" to 54L,
        "GLFW_KEY_RIGHT_CONTROL" to 97L,
        "GLFW_KEY_RIGHT_ALT" to 100L,
        "GLFW_KEY_RIGHT_SUPER" to 126L,
    )
    val FCL_TO_GLFW: Map<Long, String> = GLFW_TO_FCL.entries.associate { (k, v) -> v to k }

    val ZL_TO_FCL_FALLBACKS: Map<String, Pair<Long, String>> = mapOf(
        "GLFW_MOUSE_BUTTON_4" to (1003L to "FCL has no side mouse button 4; substituted with scroll up"),
        "GLFW_MOUSE_BUTTON_5" to (1004L to "FCL has no side mouse button 5; substituted with scroll down"),
        "GLFW_MOUSE_BUTTON_6" to (1003L to "FCL has no side mouse button 6; substituted with scroll up"),
        "GLFW_MOUSE_BUTTON_7" to (1004L to "FCL has no side mouse button 7; substituted with scroll down"),
        "GLFW_MOUSE_BUTTON_8" to (1004L to "FCL has no side mouse button 8; substituted with scroll down"),
        "GLFW_KEY_F25" to (GLFW_TO_FCL["GLFW_KEY_F24"]!! to "FCL has no F25; substituted with F24"),
        "GLFW_KEY_WORLD_1" to (GLFW_TO_FCL["GLFW_KEY_UNKNOWN"]!! to "FCL has no WORLD_1; substituted with UNKNOWN"),
        "GLFW_KEY_WORLD_2" to (GLFW_TO_FCL["GLFW_KEY_UNKNOWN"]!! to "FCL has no WORLD_2; substituted with UNKNOWN"),
        "GLFW_KEY_MENU" to (GLFW_TO_FCL["GLFW_KEY_UNKNOWN"]!! to "FCL has no menu key; substituted with UNKNOWN"),
        "GLFW_KEY_LAST" to (GLFW_TO_FCL["GLFW_KEY_UNKNOWN"]!! to "FCL has no LAST sentinel key; substituted with UNKNOWN"),
        "GLFW_MOD_SHIFT" to (GLFW_TO_FCL["GLFW_KEY_LEFT_SHIFT"]!! to "FCL has no modifier event; substituted with left shift"),
        "GLFW_MOD_CONTROL" to (GLFW_TO_FCL["GLFW_KEY_LEFT_CONTROL"]!! to "FCL has no modifier event; substituted with left control"),
        "GLFW_MOD_ALT" to (GLFW_TO_FCL["GLFW_KEY_LEFT_ALT"]!! to "FCL has no modifier event; substituted with left alt"),
        "GLFW_MOD_SUPER" to (GLFW_TO_FCL["GLFW_KEY_LEFT_SUPER"]!! to "FCL has no modifier event; substituted with left super"),
        "GLFW_MOD_CAPS_LOCK" to (GLFW_TO_FCL["GLFW_KEY_CAPS_LOCK"]!! to "FCL has no modifier event; substituted with caps lock"),
        "GLFW_MOD_NUM_LOCK" to (GLFW_TO_FCL["GLFW_KEY_NUM_LOCK"]!! to "FCL has no modifier event; substituted with num lock"),
        "GLFW_MOUSE_BUTTON_LAST" to (1004L to "FCL has no LAST mouse sentinel; substituted with scroll down"),
    )

    val FCL_TO_ZL_FALLBACKS: Map<Long, Pair<Pair<String, String>, String>> = mapOf(
        0L to (("key" to "GLFW_KEY_UNKNOWN") to "FCL KEY_RESERVED is not a real input key; substituted with GLFW_KEY_UNKNOWN"),
        121L to (("key" to "GLFW_KEY_KP_DECIMAL") to "FCL KEY_KPCOMMA has no exact GLFW key; substituted with keypad decimal"),
    )

    // --- 默认样式（与 cc.py 逐字段一致） ---

    fun defaultFclStyle(name: String = "Default"): JsonObject = obj(
        "name" to str(name),
        "textColor" to inum(-1),
        "textSize" to inum(12),
        "strokeColor" to inum(-12303292),
        "strokeWidth" to inum(10),
        "cornerRadius" to inum(100),
        "fillColor" to inum(0),
        "textColorPressed" to inum(-1),
        "textSizePressed" to inum(12),
        "strokeColorPressed" to inum(-12303292),
        "strokeWidthPressed" to inum(10),
        "cornerRadiusPressed" to inum(100),
        "fillColorPressed" to inum(-3355444),
    )

    /** 对应 ZL DefaultButtonStyleConfig：黑 50% 底 / 白字 / 无描边 / 直角 / 灰 70% 按下底。 */
    fun defaultZlFallbackFclStyle(name: String = "ZL Native Default"): JsonObject = obj(
        "name" to str(name),
        "textColor" to inum(-1),
        "textSize" to inum(14),
        "strokeColor" to inum(-1),
        "strokeWidth" to inum(0),
        "cornerRadius" to inum(0),
        "fillColor" to inum(-2147483648),
        "textColorPressed" to inum(-1),
        "textSizePressed" to inum(14),
        "strokeColorPressed" to inum(-1),
        "strokeWidthPressed" to inum(0),
        "cornerRadiusPressed" to inum(0),
        "fillColorPressed" to inum(-1282897784),
    )

    fun defaultFclDirectionStyle(): JsonObject = obj(
        "name" to str("Default"),
        "styleType" to str("BUTTON"),
        "buttonStyle" to obj(
            "interval" to inum(50),
            "textColor" to inum(-1),
            "textSize" to inum(12),
            "strokeColor" to inum(-12303292),
            "strokeWidth" to inum(10),
            "cornerRadius" to inum(100),
            "fillColor" to inum(0),
            "textColorPressed" to inum(-1),
            "textSizePressed" to inum(12),
            "strokeColorPressed" to inum(-12303292),
            "strokeWidthPressed" to inum(10),
            "cornerRadiusPressed" to inum(100),
            "fillColorPressed" to inum(-3355444),
        ),
        "rockerStyle" to obj(
            "rockerSize" to inum(400),
            "bgCornerRadius" to inum(500),
            "bgStrokeWidth" to inum(20),
            "bgStrokeColor" to inum(-12303292),
            "bgFillColor" to inum(0),
            "rockerCornerRadius" to inum(500),
            "rockerStrokeWidth" to inum(10),
            "rockerStrokeColor" to inum(-12303292),
            "rockerFillColor" to inum(-7829368),
        ),
    )

    fun emptyFclEvent(): JsonObject = obj(
        "autoKeep" to bool(false),
        "autoClick" to bool(false),
        "openMenu" to bool(false),
        "switchTouchMode" to bool(false),
        "switchMouseMode" to bool(false),
        "input" to bool(false),
        "quickInput" to bool(false),
        "outputText" to str(""),
        "outputKeycodes" to JsonArray(),
        "bindViewGroup" to JsonArray(),
    )

    fun fclButtonEvent(): JsonObject = obj(
        "pointerFollow" to bool(false),
        "Movable" to bool(false),
        "pressEvent" to emptyFclEvent(),
        "longPressEvent" to emptyFclEvent(),
        "clickEvent" to emptyFclEvent(),
        "doubleClickEvent" to emptyFclEvent(),
    )

    fun defaultZlJoystickStyleConfig(): JsonObject = obj(
        "alpha" to pyNum(1.0),
        "backgroundColor" to inum(CcUtils.fclArgbToZlColor(0x80000000L)),
        "joystickColor" to inum(CcUtils.fclArgbToZlColor(0x80FFFFFFL)),
        "joystickCanLockColor" to inum(CcUtils.fclArgbToZlColor(0x80FFFF00L)),
        "joystickLockedColor" to inum(CcUtils.fclArgbToZlColor(0x8000FF00L)),
        "lockMarkColor" to inum(CcUtils.fclArgbToZlColor(0xFFFFFFFFL)),
        "borderWidthRatio" to inum(0),
        "borderColor" to inum(CcUtils.fclArgbToZlColor(0xFFFFFFFFL)),
        "backgroundShape" to inum(50),
        "joystickShape" to inum(50),
        "joystickSize" to pyNum(0.5),
    )
}
