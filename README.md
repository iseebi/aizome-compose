# Aizome


[![Release](https://img.shields.io/github/v/release/iseebi/aizome-compose)](https://github.com/iseebi/aizome-compose/releases/latest)
[![License](https://img.shields.io/github/license/iseebi/aizome-compose)](https://github.com/iseebi/aizome-compose/blob/main/LICENSE)


See also: [aizome-swiftui(for iOS native)](https://github.com/iseebi/aizome-swiftui)

Aizome is a lightweight library for rendering richly styled text in Compose (Jetpack Compose/Compose Multiplatform) using a custom markup syntax. Define your own styles and apply them directly within strings, just like HTML—without the complexity.

## Installation

### Configure repository

Aizome is distributed via GitHub Package Registry.

Add the following repository to your `settings.gradle.kts` (or `build.gradle.kts`) to use it:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/iseebi/aizome-compose")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

Or if you are using a `build.gradle.kts` without dependencyResolutionManagement, add:

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/iseebi/aizome-compose")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

> Note:
Accessing GitHub Packages requires authentication.
You must provide a GitHub Personal Access Token with the read:packages permission via GITHUB_TOKEN environment variable or Gradle properties (gpr.user, gpr.key).

### for Android (Jetpack Compose)

Add the following dependencies to your `build.gradle.kts` (or `build.gradle`) file:

```kotlin
dependencies {
    implementation("net.iseteki.aizome:aizome:<version>")
    implementation("net.iseteki.aizome:aizome-android:<version>")
}
```

### for Compose Multiplatform

In your commonMain source set dependencies:

```kotlin
commonMain {
    dependencies {
        implementation("net.iseteki.aizome:aizome:<version>")
        implementation("net.iseteki.aizome:aizome-multiplatform:<version>")
    }
}
```

For platform-specific targets like androidMain or iosMain, no additional dependencies are needed.
Compose Multiplatform will automatically resolve the correct platform-specific artifacts.

## How to use

### 1. Define Your Styles

Before rendering styled text, define the styles you want to use:

```kotlin
Aizome.instance.setDefaultStyles(
    mapOf(
        "bold" to SpanStyleStringStyle(SpanStyle(fontWeight = FontWeight.Bold)),
        "red" to SpanStyleStringStyle(SpanStyle(color = Color.Red)),
        "highlight" to SpanStyleStringStyle(SpanStyle(color = Color.White, background = Color.Blue, fontSize = 18.sp)),
    ),
)
```

### 2. Write Markup-Style Text

Use custom tags like `<bold>`, `<red>`, or any tag name you've defined:

```kotlin
val message = "<bold>Hello</bold>, <red>world</red>! Let's <highlight>shine</highlight>."
```

### 3. Display with styledString

Render the styled string in Compose:

```kotlin
styledString(message)
```

### 4. Format Strings with Parameters

You can use placeholders like `%s`, `%d`, and other String.format-style specifiers inside your styled text.

For performance reasons, we need to creating a StringStyleFormatter instance in advance and reusing it:

```kotlin
val formatter = StringStyleFormatter("<bold>%@</bold> has <red>%d</red> messages.")
```

Then use the format method to produce a styled string with your arguments:

```kotlin
formatter.format("iseebi", 5)
```

This allows you to keep formatting logic efficient and consistent throughout your app.

## Copyright

see ./LICENSE

```
Aizome - Customizable Styled-String(AnnotatedString) Generator/Formatter
Copyright (c) 2025 Nobuhiro Ito
```
