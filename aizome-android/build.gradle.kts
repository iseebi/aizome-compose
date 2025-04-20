plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}
android {
    namespace = "net.iseteki.aizome.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
dependencies {
    implementation(libs.androidx.compose.ui.text)
    implementation(project(":aizome"))
}
