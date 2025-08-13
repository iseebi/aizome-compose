package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberStyledString(
    formatString: String,
    styles: Map<String, StringStyle<AnnotatedString>> = emptyMap(),
    ignoreDefaultStyles: Boolean = false,
): AnnotatedString {
    return remember(formatString, styles, ignoreDefaultStyles) {
        styledString(formatString, styles, ignoreDefaultStyles)
    }
}