# Aizome

Aizome is a lightweight Swift library for rendering richly styled text in Compose (Jetpack Compose/Compose Multiplatform) using a custom markup syntax. Define your own styles and apply them directly within strings, just like HTML—without the complexity.

## Installation

### for Android (Jetpack Compose)

(TODO:導入方法を書く)

### for Compose Multiplatform

(TODO:導入方法を書く)

## How to use

### 1. Define Your Styles

Before rendering styled text, define the styles you want to use:

```
Aizome.defineDefaultStyles([
    "bold": BasicStringStyle(font: .system(size: 16, weight: .bold)),
    "red": BasicStringStyle(color: .red),
    "highlight": BasicStringStyle(
        font: .system(size: 14),
        color: .white,
        backgroundColor: .blue
    )
])
```

### 2. Write Markup-Style Text


