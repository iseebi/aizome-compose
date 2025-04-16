plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
android {
    namespace = "net.iseteki.aizome.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}
dependencies {
    implementation(libs.androidx.compose.ui.text)
    implementation(project(":aizome"))
}
