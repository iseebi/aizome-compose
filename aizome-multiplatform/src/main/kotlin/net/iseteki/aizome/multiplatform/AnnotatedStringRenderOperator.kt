package net.iseteki.aizome.multiplatform

import androidx.compose.ui.text.AnnotatedString
import net.iseteki.aizome.StringStyle
import net.iseteki.aizome.render.RenderOperator

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
        withDictionary: Map<String, StringStyle<AnnotatedString>>
    ): AnnotatedString {
        // Create a new AnnotatedString.Builder to apply styles
        val builder = AnnotatedStringStyledStringBuilder(toStyled)

        // Apply the styles to the specified range
        for (style in styles) {
            withDictionary[style]?.apply(builder, inRange)
        }

        return builder.toStyledString()
    }
}