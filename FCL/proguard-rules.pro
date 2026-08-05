# FCL R8 规则（Compose/Miuix 迁移后启用 R8 优化）
#
# 目标：对第三方库做压缩+优化；应用自有代码全量 keep 且不混淆。
# 原因：gson 反射 DTO（Version/TLauncherVersion 等）、@JsonAdapter 注解、
# JNI 回调（CallbackBridge/VMLauncher）、getIdentifier 动态查字符串，
# 均依赖类名/字段/注解完整保留。应用代码 keep 后，R8 收益来自 androidx/compose/kotlinx。

# ---- 应用自有代码全量保留（含字段/内部类/枚举）----
-keep class com.tungsten.** { *; }
-keep class com.mio.** { *; }
-keep class org.jackhuang.** { *; }
-keep class org.lwjgl.** { *; }
-keep class net.java.** { *; }
-keep class net.minecraft.** { *; }
-keep class net.burningtnt.** { *; }
-keep class com.oracle.** { *; }
-keep class org.main.** { *; }

-dontobfuscate
-keepattributes Signature,Exceptions,InnerClasses,EnclosingMethod,Annotation,*Annotation*,RuntimeVisible*Annotation*,AnnotationDefault

# ---- androidx.startup：manifest metadata 反射发现 Initializer ----
-keep class androidx.startup.** { *; }

# ---- JNI 本地方法（任何包）----
-keepclasseswithmembernames class * { native <methods>; }

# ---- gson：注解类 + 被注解模型 + TypeAdapter 实现 ----
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
# gson 反射枚举（CompatibilityRule.Action 等）
-keepclassmembers enum * { *; }
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ---- Compose / Kotlin 基础 ----
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.** { *; }
-keep class kotlin.reflect.** { *; }

# ---- slf4j 无绑定实现（运行时 no-op 回退）----
-dontwarn org.slf4j.impl.StaticLoggerBinder
