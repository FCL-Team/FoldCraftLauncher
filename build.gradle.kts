// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// AGP 8.13 捆绑的 D8/R8 8.13.19 只支持到 Kotlin 2.3，
// Kotlin 2.4 产物需 R8 9.1.29+（https://developer.android.com/studio/build/kotlin-d8-r8-versions），
// 通过 buildscript classpath 覆盖捆绑版本
buildscript {
    dependencies {
        classpath("com.android.tools:r8:9.4.17")
    }
}