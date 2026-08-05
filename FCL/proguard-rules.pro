# FCL R8 规则（Compose/Miuix 迁移后启用 R8 优化）
# 策略：压缩 + 优化，但不混淆（gson 字段序列化、JNI、大量反射依赖类名/字段名稳定）

-dontobfuscate
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
