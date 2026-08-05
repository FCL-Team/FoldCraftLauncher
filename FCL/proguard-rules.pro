# FCL R8 规则（Compose/Miuix 迁移后启用 R8 优化）
# 策略：压缩 + 优化，但不混淆（gson 字段序列化、JNI、大量反射依赖类名/字段名稳定）
#
# R8 会静态判定"未使用"并删除字段——gson 反射（无 @JsonAdapter 的 DTO，如
# FCLCore Version/TLauncherVersion 等）只靠反射读字段，字段被删后解析全部失败。
# 因此应用自有代码全量保留；R8 的裁剪收益主要来自第三方库（androidx/compose/kotlinx）。
-keep class com.tungsten.** { *; }
# 应用自有源码包（静态适配审查 2026-08-05）：mio 包、HMCL 遗留、JNI 回调桥
-keep class com.mio.** { *; }
-keep class org.jackhuang.** { *; }
# JNI 回调：native 侧按类名/方法名回调 Java（CallbackBridge/GLFW/VMLauncher 等）
-keep class org.lwjgl.** { *; }
-keep class net.java.** { *; }
-keep class net.minecraft.** { *; }
# Terracotta 联机模块（含 JNI）
-keep class net.burningtnt.** { *; }
-keep class com.oracle.** { *; }
-keep class org.main.** { *; }

-dontobfuscate
# R8 全量优化会改写枚举/合并类，破坏 gson 反射枚举解析（CompatibilityRule.Action 等）
-dontoptimize
-keepattributes Signature,Exceptions,InnerClasses,EnclosingMethod,Annotation,*Annotation*

# androidx.startup：通过 manifest metadata 反射发现 Initializer
-keep class androidx.startup.** { *; }

# JNI 本地方法
-keepclasseswithmembernames class * { native <methods>; }

# gson：注解类本身必须保留，否则 R8 会把模型类上的 @JsonAdapter/@SerializedName 一并剥掉
#（导致回退反射反序列化、实例化 StateFlow 接口字段而崩溃）
-keep class com.google.gson.annotations.** { *; }
-keep @com.google.gson.annotations.JsonAdapter class * { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.JsonAdapter <fields>;
}
-keep class * implements com.google.gson.TypeAdapter { *; }
-keep class * implements com.google.gson.TypeAdapterFactory { *; }
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }

# Compose 运行时（官方建议的基础保留）
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.** { *; }

# slf4j 无日志绑定实现（运行时有 no-op 回退，属正常缺失）
-dontwarn org.slf4j.impl.StaticLoggerBinder
