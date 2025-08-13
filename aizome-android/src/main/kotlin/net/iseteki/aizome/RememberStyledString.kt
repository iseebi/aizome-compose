package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberStyledString(
    formatString: String,
    styles: Map<String, StringStyle<AnnotatedString>> = Aizome.instance.defaultStyles,
): AnnotatedString {
    val rendered by remember(formatString, styles) {
        mutableStateOf(
            styledString(formatString, styles)
        )
    }
    return rendered
}