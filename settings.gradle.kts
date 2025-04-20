pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Aizome"
include(":aizome")
include(":aizome-android")
include(":aizome-multiplatform")

val ci = System.getenv("CI")?.toBoolean() ?: false
val jitpack = System.getenv("JITPACK")?.toBoolean() ?: false

if (!ci && !jitpack) {
    include(":example-android")
    include(":example-multiplatform")
}
