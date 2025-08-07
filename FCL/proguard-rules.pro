# Keep annotation attributes for runtime reflection
-keepattributes *Annotation*

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep classes used by Android framework via reflection
-keep class androidx.appcompat.** { *; }
-keep class androidx.core.** { *; }
-keep class com.google.android.material.** { *; }

# Keep entry points (Activities, Services, Receivers, Providers)
-keep class ** extends android.app.Activity { *; }
-keep class ** extends android.app.Service { *; }
-keep class ** extends android.content.BroadcastReceiver { *; }
-keep class ** extends android.content.ContentProvider { *; }

# Keep Glide generated API (if used)
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

# Keep Gson model classes with reflective access
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Keep NanoHTTPD public API
-keep class fi.iki.elonen.** { *; }

# Keep opennbt API
-keep class com.github.steveice10.** { *; }

# Keep org.tukaani.xz API
-keep class org.tukaani.** { *; }

# Keep Apache Commons APIs
-keep class org.apache.commons.** { *; }
-dontwarn org.apache.commons.**

# Keep Netty/okio style shaded classes if present
-dontwarn sun.misc.Unsafe

# Do not obfuscate BuildConfig and R (for clarity; optional)
-keep class **.BuildConfig { *; }
-keep class **.R { *; }
-keep class **.R$* { *; }