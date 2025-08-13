package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString
import net.iseteki.aizome.render.ConvertMode

fun styledString(
    formatString: String,
    styles: Map<String, StringStyle<AnnotatedString>> = emptyMap(),
    ignoreDefaultStyles: Boolean = false,
): AnnotatedString {
    val usingStyles = if (ignoreDefaultStyles) {
        styles
    } else {
        styles + Aizome.instance.defaultStyles
    }

    val parserRender = Aizome.instance.createParserRender()
    val parserSegment = parserRender.first.parseFormatString(formatString)
    val renderSegment =
        parserRender.second.convertSegments(parserSegment, ConvertMode.SIMPLE_CONVERT, usingStyles)
    return parserRender.second.renderAsLiteral(renderSegment, usingStyles)
}