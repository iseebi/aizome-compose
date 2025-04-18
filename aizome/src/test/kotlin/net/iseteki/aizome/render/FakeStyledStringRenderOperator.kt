package net.iseteki.aizome.render

import net.iseteki.aizome.FakeStyledString
import net.iseteki.aizome.FakeStyledStringBuilder
import net.iseteki.aizome.Span
import net.iseteki.aizome.StringStyle

class FakeStyledStringRenderOperator : RenderOperator<FakeStyledString> {
    override fun create(fromString: String): FakeStyledString {
        return FakeStyledString(
            spans = listOf(
                Span(
                    text = fromString,
                )
            )
        )
    }

    override fun merge(toStyled: FakeStyledString, withStyled: FakeStyledString): FakeStyledString {
        val merged = FakeStyledString(
            spans = toStyled.spans + withStyled.spans
        )
        return merged
    }

    override fun apply(
        toStyled: FakeStyledString,
        inRange: IntRange,
        styles: List<String>,
        withDictionary: Map<String, StringStyle<FakeStyledString>>
    ): FakeStyledString {
        var builders = toStyled.spans.map { FakeStyledStringBuilder(
            text = it.text,
            color = it.color,
            fontSize = it.fontSize,
            isBold = it.isBold
        ) }

        for (style in styles) {
            for (builder in builders) {
                withDictionary[style]?.apply(builder, inRange)
            }
        }

        return FakeStyledString(
            spans = builders.flatMap { it.toStyledString().spans }
        )
    }
}