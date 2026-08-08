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
# 注意：keepattributes 属性名必须是 R8 合法值（Signature/Exceptions/InnerClasses/
# EnclosingMethod/RuntimeVisibleAnnotations 等）；此前混入的裸 `Annotation` 与
# `*Annotation*` 通配写法会导致 R8 整条忽略该选项，泛型签名（gson TypeToken
# 匿名类依赖）被整体剥离，fordebug/release 启动即崩（Config$Serializer$1）。
-keepattributes Signature,Exceptions,InnerClasses,EnclosingMethod,RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,AnnotationDefault

# ---- androidx.startup：manifest metadata 反射发现 Initializer ----
-keep class androidx.startup.** { *; }

# ---- JNI 本地方法（任何包）----
-keepclasseswithmembernames class * { native <methods>; }

# ---- gson：注解类 + 被注解模型 + TypeAdapter 实现 ----
# TypeToken 匿名子类泛型签名保留三件套（R8 官方 FAQ GSON 小节：缺一不可，
# full/compat 模式均需要；gson 2.11.0+ 内置 gson.pro 同义规则，本项目 gson 2.10.1 需手配）。
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
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
