plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
}

kotlin {
    jvm()
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":aizome"))
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "net.iseteki.aizome.multiplatform"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}
