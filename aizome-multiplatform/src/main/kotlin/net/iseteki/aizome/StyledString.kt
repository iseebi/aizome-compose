package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString
import net.iseteki.aizome.render.ConvertMode

fun styledString(
    formatString: String,
    styles: Map<String, StringStyle<AnnotatedString>> = Aizome.instance.defaultStyles,
): AnnotatedString {
    val parserRender = Aizome.instance.createParserRender()
    val parserSegment = parserRender.first.parseFormatString(formatString)
    val renderSegment =
        parserRender.second.convertSegments(parserSegment, ConvertMode.SIMPLE_CONVERT, styles)
    return parserRender.second.renderAsLiteral(renderSegment, styles)
}