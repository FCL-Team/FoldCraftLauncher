# FCL R8 规则（Compose/Miuix 迁移后启用 R8 优化）
# 策略：压缩 + 优化，但不混淆（gson 字段序列化、JNI、大量反射依赖类名/字段名稳定）

-dontobfuscate
-keepattributes Signature,Exceptions,InnerClasses,EnclosingMethod,Annotation,*Annotation*

# androidx.startup：通过 manifest metadata 反射发现 Initializer
-keep class androidx.startup.** { *; }

# JNI 本地方法
-keepclasseswithmembernames class * { native <methods>; }

# gson：@SerializedName 与 TypeAdapter 相关保留
-keepclassmembers class * { @com.google.gson.annotations.SerializedName <fields>; }
-keep class * implements com.google.gson.TypeAdapter { *; }
-keep class * implements com.google.gson.TypeAdapterFactory { *; }
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }

# Compose 运行时（官方建议的基础保留）
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.** { *; }
