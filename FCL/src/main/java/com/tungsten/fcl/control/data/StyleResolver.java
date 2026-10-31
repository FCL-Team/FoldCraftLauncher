package com.tungsten.fcl.control.data;

/**
 * 样式解析器：样式归属各布局（Controller），按键数据仅保存样式名，
 * 解析时由所属布局提供样式表。同时兼容旧格式按键内嵌完整样式对象的写法
 * （内嵌对象注册进布局样式表后按名挂回，保证对象身份一致）。
 */
public interface StyleResolver {

    ControlButtonStyle resolveButtonStyle(String name);

    ControlDirectionStyle resolveDirectionStyle(String name);

    void registerInlineButtonStyle(ControlButtonStyle style);

    void registerInlineDirectionStyle(ControlDirectionStyle style);
}
