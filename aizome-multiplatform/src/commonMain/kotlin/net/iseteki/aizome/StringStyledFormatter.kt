package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString

class StringStyledFormatter(
    formatString: String,
    styles: Map<String, StringStyle<AnnotatedString>> = Aizome.instance.defaultStyles,
) : AbstractStringStyledFormatter<AnnotatedString>(
    formatString,
    styles,
    Aizome.instance.createParserRender()
)
