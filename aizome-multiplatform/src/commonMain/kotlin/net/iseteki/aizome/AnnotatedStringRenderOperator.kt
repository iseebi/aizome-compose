package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString
import net.iseteki.aizome.render.RenderLogger
import net.iseteki.aizome.render.RenderOperator
import net.iseteki.aizome.render.lookupStyle

internal class AnnotatedStringRenderOperator : RenderOperator<AnnotatedString> {
    override fun create(fromString: String): AnnotatedString {
        return AnnotatedString(fromString)
    }

    override fun merge(toStyled: AnnotatedString, withStyled: AnnotatedString): AnnotatedString {
        val merged = AnnotatedString.Builder(toStyled)
        merged.append(withStyled)
        return merged.toAnnotatedString()
    }

    override fun apply(
        toStyled: AnnotatedString,
        inRange: IntRange,
        styles: List<String>,
        withDictionary: Map<String, StringStyle<AnnotatedString>>,
        logger: RenderLogger,
    ): AnnotatedString {
        // Create a new AnnotatedString.Builder to apply styles
        val builder = AnnotatedStringStyledStringBuilder(toStyled)

        // Apply the styles to the specified range
        for (style in styles) {
            lookupStyle(style, withDictionary, logger)?.apply(builder, inRange)
        }

        return builder.toStyledString()
    }
}